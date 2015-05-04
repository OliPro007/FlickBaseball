package gamestate;

import java.applet.AudioClip;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.TexturePaint;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Random;

import javax.swing.event.EventListenerList;

import util.AudioLoader;
import util.BallListener;
import util.BufferedImageLoader;
import util.GameStateListener;
import util.ScoreListener;
import application.App05FlickBaseball;
import application.Camera;
import application.GamePanel;
import drawable.gameobject.GameObject;
import drawable.gameobject.Handler;
import drawable.gameobject.Platform;
import drawable.gameobject.ball.AbstractBall;
import drawable.gameobject.ball.BallType;
import drawable.gameobject.ball.ElectricBall;
import drawable.gameobject.ball.FastBall;
import drawable.gameobject.ball.FrictionBall;
import drawable.gameobject.ball.NormalBall;
import drawable.gameobject.ball.SinusBall;
import drawable.gameobject.ball.TrollfaceBall;
import drawable.util.Button;

/**
 * Le panneau où se déroule le jeu dans le mode de jeu normal.
 * Dans ce mode, les paramètres de jeu ne sont pas disponibles à l'utilisateur.
 * 
 * @author Olivier St-Jean
 * @author Alexandre Hua
 * @version 04-05-2015
 */
public class NormalGameState implements IGameState{	
	
	/********************** propriété finale ******************************/
	private final int LINE_LENGTH = 10;
	private final int LINE_SPACE = 5;
	private final int BALL_POS_FREQ = 5;
	private final int BTN_SIZE = 100;
	private final int SPACING = 25;
	private final int CAMERA_LOCK_WIDTH = 100;
	private final int CAMERA_LOCK_HEIGHT = 30;
	private final int NB_DATA_INDEX = 3;
	private final int DATA_INDEX_BALLTYPE = 0;
	private final int DATA_INDEX_DISTANCE = 1;
	private final int DATA_INDEX_INCOMINGSPEED = 2;
	private final int DATA_INDEX_ELECTRIC_CHARGE = 3;
	private final Font GAME_FONT = new Font("Time New Roman", Font.PLAIN, 30);
	private final EventListenerList REGISTERED_LISTENERS = new EventListenerList();
	private final int id = GameStateManager.NORMAL_MODE_STATE;
	
	/********************* tableaux ***************************************/
	private ArrayList<Ellipse2D.Double> ballTraj1;
	private ArrayList<Ellipse2D.Double> ballTraj2;
	private ArrayList<Component> components;
	private ArrayList<Object[]> saveData;
	private Object[] nextData;
	
	/********************* propriété GameObject ***************************/
	private Camera cam;
	private boolean isCamFixed;
	private Handler handler;
	private AffineTransform worldMatrix;
	private boolean cfgSfxEnabled;
	private double straightWorldHeight;
	
	/********************* propriété Balle ********************************/
	private boolean loaded;
	private boolean landed;
	private double pixelsByUnitX, pixelsByUnitY;
	private int score;
	private int ballPosCount;
	private int ballsThrown;
	private AbstractBall ball;
	private int lifes;
	private boolean gameOver;
	private int gameOverTimer;
	
	/********************** fichiers ***************************************/
	private AudioClip audio;
	private BufferedImage backgroundImg;
	private TexturePaint background;
	
	/*********************** composants *************************************/
	private Button btnPause;
	private Button btnPlay;
	
	/**
	 * Initialise les différentes propriétés du jeu.
	 */
	public void init() {
		if(!loaded){
			/********************************** initialisation **************************************/
			calculerMatriceMondeVersComposant(GamePanel.REAL_SIZE);
			isCamFixed = true;
			landed = false;
			gameOver = false;
			gameOverTimer = 0;
			ballPosCount = 0;
			
			if(saveData == null){
				saveData = new ArrayList<Object[]>();
				lifes = 3;
				score = 0;
				ballsThrown = 0;
			}else{
				lifes = (int) saveData.remove(0)[0];
				for(int i=0; i<saveData.size(); i++){
					score += (int) saveData.get(i)[DATA_INDEX_DISTANCE];
				}
				ballsThrown = saveData.size();
			}
	
			handler = new Handler(worldMatrix);
			handler.loadImage();
			cam = new Camera(0,0,0,1000*worldMatrix.getScaleX(),worldMatrix); //Le monde mesure 1000m, mais la caméra ne se déplace pas dans le monde réel.
			
			//BufferedImageLoader loader = new BufferedImageLoader();
			//BufferedImage platformTexture = loader.loadImage("/game/Ground.png");
			/*ArrayList<Shape> shapes = new ArrayList<Shape>();
			shapes.add(new Rectangle2D.Double(2,2,2,2));*/
			//Obstacles obstacle1 = new Obstacles(new Rectangle2D.Double(20,20,20,20), platformTexture, worldMatrix);
			//Obstacles obstacle1 = new Obstacles(platformTexture, worldMatrix, new Rectangle2D.Double(20,30,20,20), new Rectangle2D.Double(20,20,10,10));
			//handler.addObject(obstacle1);
			components = new ArrayList<Component>();
			btnPause = new Button("", "/game/pause_");
			btnPlay = new Button("Lancer", "/game/launch_");
			
			ballTraj1 = new ArrayList<Ellipse2D.Double>();
			ballTraj2 = new ArrayList<Ellipse2D.Double>();
			
			/********************************** corps *********************************************/
			btnPause.setBounds(GamePanel.WIDTH-BTN_SIZE*2, 10, BTN_SIZE/2, BTN_SIZE/2);
			btnPause.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					requestChange(GameStateManager.PAUSE_STATE);
				}
			});
			
			btnPlay.setBounds(BTN_SIZE, GamePanel.HEIGHT-BTN_SIZE*2, BTN_SIZE, BTN_SIZE);
			btnPlay.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(cfgSfxEnabled) {
						audio.play();
					}
					
					ball = createBall(); //créer le type de balle à lancer aléatoirement
					
					long beginTime = System.currentTimeMillis();
					while(System.currentTimeMillis() - beginTime < 2000) {}
					
					isCamFixed = true;
					
					/************************* créer la balle *******************************************/			
					ball.addScoreListener(new ScoreListener() { //ajouter score lorsque la balle attérit au sol
						public void getScore(int addScore) {
							if(addScore >= GamePanel.REAL_SIZE/2) {
								score += addScore-GamePanel.REAL_SIZE/2;
								nextData[DATA_INDEX_DISTANCE] = addScore;
							}
						}
					});
					
					ball.addBallListener(new BallListener() {
						public void resetBall() {
							resetBallObject();
						}
						
						public void landed() {
							landed = true;
						}
					});

					handler.addObject(ball);
					ballsThrown++;
					/************************************************************************************/
					
					ballTraj1.removeAll(ballTraj1);
					ballTraj2.removeAll(ballTraj2);
					
					btnPlay.setVisible(false);
				}
			});
			
			/********************************** Ajout Swing ***************************************/			
			components.add(btnPlay);
			components.add(btnPause);
			
			AudioLoader audioLoader = new AudioLoader();
			audio = audioLoader.loadAudio("audio/NBA_Sound.wav");
			BufferedImageLoader bil = new BufferedImageLoader();
			backgroundImg = bil.loadImage("/game/background_field.png");
			notifyChange();			
			loaded = true;
		} else {
			notifyChange();
		}
	}

	/**
	 * Mise à jour des propriétés de différents objets de jeu et les comportements du jeu.
	 * Appelle la méthode {@link Handler#update()} pour mettre à jour les objets de jeu, puis met à jour la
	 * caméra grâce à sa méthode {@link Camera#tick(GameObject)} et une référence à la balle active.
	 */
	public void update() {
		handler.update();
		
		//Nous sommes obligés de garder une référence à la balle à cause de l'écouteur du bouton "Lancer".
		//Puisqu'il ne s'agit que d'une référence, nous ne somme pas obligés d'obtenir celle du gestionnaire.
		if(ball != null) {
			if(isCamFixed) {
				cam.tick(ball);
			}
			
			//construire les points de trajectoire de la balle
			ballPosCount++;
			if(ballTraj1 != null && ballPosCount == BALL_POS_FREQ) {
				if(!ball.getHitByBat()) {
					ballTraj1.add(new Ellipse2D.Double(ball.getX()+ball.getWidth()/2, ball.getY()+ball.getHeight()/2, ball.getWidth()/10, ball.getHeight()/10));
					ballPosCount = 0;
				} else {
					ballTraj2.add(new Ellipse2D.Double(ball.getX()+ball.getWidth()/2, ball.getY()+ball.getHeight()/2, ball.getWidth()/10, ball.getHeight()/10));
					ballPosCount = 0;
				}
			}
		}
		
		if(ball != null) {
			if((ball.getX()+ball.getWidth()) < 0) {
				resetBallObject();
			}else if((ball.getX()) > -cam.getEndOfWorld()/worldMatrix.getScaleX()) {
				resetBallObject();
				score = (int)(-cam.getEndOfWorld()/worldMatrix.getScaleX());
			}
		}
		
		background = new TexturePaint(backgroundImg, new Rectangle2D.Double(cam.getX(), cam.getY(), GamePanel.WIDTH, GamePanel.HEIGHT));
		
		if(gameOverTimer >= 120){
			reset();
			requestChange(GameStateManager.MENU_STATE);
		}
	}

	/**
	 * Mise à jour des dessins des différents objets de jeu et le dessin du jeu.
	 * @param g2d le contexte graphique 2D du panneau.
	 */
	public void draw(Graphics2D g2d) {
		Paint oPaint = g2d.getPaint();
		g2d.setPaint(background);
		g2d.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
		g2d.setPaint(oPaint);
		
		/************ drawString *****************/
		Font oFont = g2d.getFont();
		g2d.setFont(GAME_FONT);
		FontRenderContext context = g2d.getFontRenderContext();
		TextLayout txt = new TextLayout("Pointage: "+score, GAME_FONT, context);
		g2d.drawString("Pointage: "+score, (int)(GamePanel.WIDTH-txt.getBounds().getWidth())/2, 30); //pointage
		g2d.drawString("Balles lancées "+ballsThrown, SPACING, SPACING); //balles lancées
		g2d.drawString("Vies: "+lifes, SPACING, (int)(2*SPACING+txt.getBounds().getHeight()));
		g2d.setFont(oFont);
		/*********************************************/
		
		if(gameOverTimer < 120){
			//translation de la caméra
			AffineTransform oTransform = g2d.getTransform();
			g2d.translate(cam.getX(), cam.getY());
				if(ballTraj1 != null) {
					g2d.setColor(Color.BLACK);
					for(int i=0; i<ballTraj1.size(); i+=2) { //Éviter les erreurs de concurrence en réduisant le nombre de points dessinés en jeu
						g2d.fill(worldMatrix.createTransformedShape(ballTraj1.get(i)));
					}
				}		
				if(ballTraj2 != null) {
					g2d.setColor(Color.WHITE);
					for(int i=0; i<ballTraj2.size(); i+=2) { //Éviter les erreurs de concurrence en réduisant le nombre de points dessinés en jeu
						g2d.fill(worldMatrix.createTransformedShape(ballTraj2.get(i)));
					}
				}
				//dessin des lignes du zone de frappe
				g2d.setColor(Color.BLACK);
				for(int y = (int)(-5*GamePanel.HEIGHT); y < GamePanel.HEIGHT; y+=LINE_LENGTH+LINE_SPACE) {
					g2d.fillRect(GamePanel.WIDTH/2, y, 5, LINE_LENGTH);
				}
				handler.render(g2d);
			g2d.setTransform(oTransform);
			
			if(!isCamFixed) {
				g2d.setColor(Color.BLACK);
				//traits du caméra horizontals
				g2d.fillRect(SPACING, SPACING, CAMERA_LOCK_WIDTH, CAMERA_LOCK_HEIGHT);
				g2d.fillRect(GamePanel.WIDTH-SPACING-CAMERA_LOCK_WIDTH, SPACING, CAMERA_LOCK_WIDTH, CAMERA_LOCK_HEIGHT);
				g2d.fillRect(SPACING, GamePanel.HEIGHT-SPACING-CAMERA_LOCK_HEIGHT, CAMERA_LOCK_WIDTH, CAMERA_LOCK_HEIGHT);
				g2d.fillRect(GamePanel.WIDTH-SPACING-CAMERA_LOCK_WIDTH, GamePanel.HEIGHT-SPACING-CAMERA_LOCK_HEIGHT, CAMERA_LOCK_WIDTH, CAMERA_LOCK_HEIGHT);
				
				//traits du caméra verticals
				g2d.fillRect(SPACING, SPACING, CAMERA_LOCK_HEIGHT, CAMERA_LOCK_WIDTH);
				g2d.fillRect(GamePanel.WIDTH-SPACING-CAMERA_LOCK_HEIGHT, SPACING, CAMERA_LOCK_HEIGHT, CAMERA_LOCK_WIDTH);
				g2d.fillRect(SPACING, GamePanel.HEIGHT-CAMERA_LOCK_WIDTH-SPACING, CAMERA_LOCK_HEIGHT, CAMERA_LOCK_WIDTH);
				g2d.fillRect(GamePanel.WIDTH-SPACING-CAMERA_LOCK_HEIGHT, GamePanel.HEIGHT-CAMERA_LOCK_WIDTH-SPACING, CAMERA_LOCK_HEIGHT, CAMERA_LOCK_WIDTH);
			}
		}
		
		if(gameOver){ //Dessiner le message de partie échouée.
			g2d.setColor(Color.RED);
			g2d.setFont(new Font("Time New Roman", Font.PLAIN, 120));
			TextLayout gameOverLayout = new TextLayout("ÉCHEC!", new Font("Time New Roman", Font.PLAIN, 120), context);
			g2d.drawString("ÉCHEC!", (int)(GamePanel.WIDTH-gameOverLayout.getBounds().getWidth())/2, (int)(GamePanel.HEIGHT-gameOverLayout.getBounds().getHeight())/2);
			g2d.setFont(oFont);
			gameOverTimer++;
		}
	}
	
	
	/**
	 * Calculer la matrice monde vers composant qui sera appliquée sur les dimensions de tous les objets de jeu. 
	 * @param worldSize L'indice de grandeur du monde réel.
	 */
	private void calculerMatriceMondeVersComposant(double worldSize) {
		worldMatrix = new AffineTransform();  //donne une matrice identité

		pixelsByUnitX =  GamePanel.WIDTH / worldSize;
		double ratio = GamePanel.HEIGHT / (double)GamePanel.WIDTH;
		straightWorldHeight = worldSize * ratio;
		pixelsByUnitY = GamePanel.HEIGHT / straightWorldHeight ;

		worldMatrix.scale( pixelsByUnitX, pixelsByUnitY );
		GameStateManager.GROUND_LEVEL = straightWorldHeight-Platform.SIZE;
	}
	
	/**
	 * Générer une balle associée à un type de balle aléatoirement
	 * @return la balle d'un certain type
	 */
	private AbstractBall createBall() {
		AbstractBall ball;
		nextData = new Object[NB_DATA_INDEX];
		
		int i;
		int iMax = 5;		
		Random random = new Random();
		int typeInt = 0;
		double mass = 0.0;
		for(i=0; i<iMax; i++){
			typeInt += 1 + random.nextInt(BallType.values().length); //+1 car index 0 == invalide
			mass += 0.1 + random.nextDouble()*0.1;
		}		 
		BallType type = BallType.values()[typeInt/iMax];
		mass /= (double)iMax;
		int lambda = 0;
		int period = 0;
		for(i=0; i<iMax; i++){
			lambda += 20 + random.nextInt(81);
			period += 1 + random.nextInt(10);
		}
		lambda /= iMax;
		period /= iMax;
		double charge = 0.0;
		for(i=0; i<iMax; i++){
			charge += 1 + random.nextInt(9);
		}
		charge = (charge/iMax)*Math.pow(10, -3);
		
		switch (type) {
			case NORMAL_BALL :
				ball = new NormalBall(GamePanel.REAL_SIZE, (GamePanel.HEIGHT-200)/pixelsByUnitX, 50/pixelsByUnitX, mass, worldMatrix, handler, cam);
				break;
			case FAST_BALL :
				ball = new FastBall(GamePanel.REAL_SIZE, (GamePanel.HEIGHT-200)/pixelsByUnitX, 50/pixelsByUnitX, mass, worldMatrix, handler, cam);
				break;
			case SINUS_BALL :
				ball = new SinusBall(GamePanel.REAL_SIZE, (GamePanel.HEIGHT/2)/pixelsByUnitX, 50/pixelsByUnitX, mass, lambda, period, worldMatrix, handler, cam);
				break;
			case FRICTION_BALL :
				ball = new FrictionBall(GamePanel.REAL_SIZE, (GamePanel.HEIGHT-200)/pixelsByUnitX, 50/pixelsByUnitX, mass, worldMatrix, handler, cam);
				break;
			case ELECTRIC_BALL :
				ball = new ElectricBall(GamePanel.REAL_SIZE, (GamePanel.HEIGHT-200)/pixelsByUnitX, 50/pixelsByUnitX, mass, charge, worldMatrix, handler, cam);
				nextData = new Object[NB_DATA_INDEX+1];
				nextData[DATA_INDEX_ELECTRIC_CHARGE] = charge;
				break;
			default :
				ball = new TrollfaceBall(GamePanel.REAL_SIZE, (GamePanel.HEIGHT-500)/pixelsByUnitX, 300/pixelsByUnitX, worldMatrix, handler, cam);
				ballsThrown--;
				break;
		}
		ball.setSFXEnabled(cfgSfxEnabled);
		
		if(ball instanceof SinusBall){
			cam.setPos(ball.getX()*worldMatrix.getScaleX(), 0);
			double intensityX = 0.0;
			int amplitude = 0;
			for(i=0; i<iMax; i++){
				intensityX += 25.0 + random.nextDouble() * 25.0;
				amplitude += random.nextInt(SinusBall.MAX_AMPLITUDE+1);
			}
			intensityX /= iMax;
			amplitude /= iMax;
			ball.setVelX(-ball.getMaxSpeedX()*intensityX/100); // mètre/seconde
			ball.setVelY(-amplitude);
			nextData[DATA_INDEX_INCOMINGSPEED] = new ArrayList<Double>(Arrays.asList(ball.getVelX(), ball.getVelY(), (double)lambda, (double)period, (double)amplitude));
		}else{
			cam.setPos(ball.getX()*worldMatrix.getScaleX(), 0);						
			double spdX = 0.0;
			double spdY = 0.0;
			for(i=0; i<iMax; i++){
				spdX += 25.0 + random.nextDouble() * 25.0; //Min=25m/s, Max=50m/s (25.0+1.0*25.0)
				spdY += 15.0 + random.nextDouble() * 15.0; //Min=15m/s, Max=30m/s (15.0+1.0*15.0)
			}
			spdX /= (double)iMax;
			spdY /= (double)iMax;
			if(ball instanceof FastBall)
				spdX = spdX*1.6; //Max devrait être 80 (50*1.6)	
			ball.setVelX(-spdX); // mètre/seconde
			ball.setVelY(-spdY);
			nextData[DATA_INDEX_INCOMINGSPEED] = new ArrayList<Double>(Arrays.asList(ball.getVelX(), ball.getVelY()));
		}
		
		nextData[DATA_INDEX_BALLTYPE] = type.ordinal();
		
		return ball;
	}
	
	/**
	 * Méthode qui permet de réinitialiser les propriétés et les composants du jeu
	 */
	public void reset(){
		/*System.out.println("SaveData: ");
		for(Object[] array : saveData){
			System.out.println(Arrays.toString(array));
		}*/
		if(!gameOver){
			saveData.add(0, new Object[]{lifes});
			try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(App05FlickBaseball.NM_SAVE_NAME))){
				oos.writeObject(saveData);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		for(Field field : getClass().getDeclaredFields()){
			if(!Modifier.isFinal(field.getModifiers())){ //http://stackoverflow.com/questions/7560285/what-is-the-best-way-to-check-if-a-field-is-final-in-java-using-reflection
				try{ //http://stackoverflow.com/questions/12361492/java-typeof-primitive-data-types
					if(Integer.TYPE.isAssignableFrom(field.getType()) || Double.TYPE.isAssignableFrom(field.getType())){
						field.set(this, 0);
					}else if(Boolean.TYPE.isAssignableFrom(field.getType()) && !field.getName().contains("cfg")){
						field.set(this, false);
					}else if(Boolean.TYPE.isAssignableFrom(field.getType()) && field.getName().contains("cfg")){
						field.set(this, field.get(this));
					}else if(Collection.class.isAssignableFrom(field.getType())){ //http://stackoverflow.com/questions/8850951/checking-a-class-type-class-is-equal-to-some-other-class-type
						((Collection<?>) field.get(this)).clear();
						field.set(this, null);
					}else{
						field.set(this, null); //https://docs.oracle.com/javase/tutorial/reflect/member/fieldValues.html
					}
				}catch(IllegalArgumentException|IllegalAccessException e){
					System.err.println("Problème lors de la réinitialisation d'une variable");
				}
			}
		}
		for(BallType type : BallType.values()){
			type.setEnabled(true);
		}
		gameOver = true;
		gameOverTimer = 120;
	}
	
	/**
	 * Méthode qui permet de réinitialiser les propriétés de la balle active.
	 */
	public void resetBallObject() {
		if((int)nextData[DATA_INDEX_BALLTYPE] != BallType.INVALID_BALL.ordinal()){
			if(nextData[DATA_INDEX_DISTANCE] == null){
				nextData[DATA_INDEX_DISTANCE] = 0;
				lifes--;
			}
			saveData.add(nextData);
		}
		nextData = null;
		handler.removeObject(ball);
		ball = null;
		btnPlay.setVisible(true);
		btnPause.setVisible(true);
		cam.setX(0);
		cam.setY(0);
		isCamFixed = true;
		landed = false;
		if(lifes == 0){
			gameOver = true;
			btnPlay.setVisible(false);
			btnPause.setVisible(false);
		}
	}
	
	/**
	 * Retourner la structure de donnée qui détient tous les boutons Swing.
	 * @return La structure de donnée qui détient tous les boutons Swing.
	 */
	public ArrayList<Component> getComponents() {
		return components;
	}
	
	/**
	 * Permet d'initialiser la structure de sauvegarde. S'il n'y a pas de sauvegarde, la structure sera null.
	 * @param saveData La structure de sauvegarde.
	 */
	public void loadSaveData(ArrayList<Object[]> saveData){
		this.saveData = saveData;
	}
	
	/**
	 * Retourner l'état boolean des effets de son
	 * @return boolean
	 */
	public boolean isSFXEnabled() {
		return cfgSfxEnabled;
	}
	
	/**
	 * Modifier l'état boolean des effets de son
	 * @param enabled l'état boolean des effets de son
	 */
	public void setSFXEnabled(boolean enabled) {
		cfgSfxEnabled = enabled;
	}
	
	/**
	 * Retourner l'état boolean du déplacement de la caméra
	 * @return boolean
	 */
	public boolean isCamFixed() {
		return isCamFixed;
	}
	
	/**
	 * Modifier l'état boolean du déplacement de la caméra
	 * @param isCamFixed l'état boolean du déplacement de la caméra
	 */
	public void setCamFixed(boolean isCamFixed) {
		this.isCamFixed = isCamFixed;
	}
	
	/**
	 * Retourner l'état boolean de la tombée de la balle.
	 * @return boolean
	 */
	public boolean isLanded() {
		return landed;
	}
	
	/**
	 * Permet d'ajouter un écouteur pour les changements d'états.
	 * @param l Un écouteur d'état de jeu.
	 */
	public void addGameStateListener(GameStateListener l){
		REGISTERED_LISTENERS.add(GameStateListener.class, l);
	}
	
	/**
	 * Passer vers un autre menu
	 * @param state le menu qu'on désire y accéder
	 */
	private void requestChange(int state){
		for(GameStateListener listener : REGISTERED_LISTENERS.getListeners(GameStateListener.class)){
			listener.stateChangeRequested(id, state);
			listener.disableBat();
			listener.disableCamera();
			listener.disableResetBall();
		}
	}
	
	/**
	 * Signaler le besoin d'ajouter un ou plusieurs composants Swing
	 */
	private void notifyChange(){
		for(GameStateListener listener : REGISTERED_LISTENERS.getListeners(GameStateListener.class)){
			listener.stateChanged();
			listener.hitBat(worldMatrix, handler, cam);
			listener.moveCamera(cam);
			listener.resetBall();
		}
	}

}
