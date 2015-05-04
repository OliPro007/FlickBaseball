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
import util.OptionsListener;
import util.ScoreListener;
import application.App05FlickBaseball;
import application.Camera;
import application.GamePanel;
import drawable.gameobject.BallSelectorPane;
import drawable.gameobject.GameObject;
import drawable.gameobject.Handler;
import drawable.gameobject.ParameterPane;
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
 * Le panneau o� se d�roule le jeu dans le mode de jeu libre.
 * Dans ce mode, les param�tres sont disponibles � l'utilisateur.
 * 
 * @author Alexandre Hua
 * @author Olivier St-Jean
 * @since 12-02-2015
 * @version 04-05-2015
 */
public class FreeModeState implements IGameState{
	
	/********************** propri�t� finale ******************************/
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
	private final Font INFO_FONT = new Font("Time New Roman", Font.PLAIN, 15);
	private final EventListenerList REGISTERED_LISTENERS = new EventListenerList();
	private final int id = GameStateManager.FREE_MODE_STATE;
	
	/********************* tableaux ***************************************/
	private ArrayList<Ellipse2D.Double> ballTraj1;
	private ArrayList<Ellipse2D.Double> ballTraj2;
	private ArrayList<Component> components;
	private ArrayList<Object[]> saveData;
	private Object[] nextData;
	
	/********************* propri�t� GameObject ***************************/
	private Camera cam;
	private boolean isCamFixed;
	private Handler handler;
	private AffineTransform worldMatrix;
	private boolean cfgSfxEnabled;
	private boolean cfgInfoEnabled;
	private boolean cfgPrintInfo = cfgInfoEnabled;
	public static double straightWorldHeight;
	
	/********************* propri�t� Balle ********************************/
	private boolean loaded;
	private boolean landed;
	private double pixelsByUnitX, pixelsByUnitY;
	private int score;
	private int ballPosCount;
	private int ballsThrown;
	//private double intensityX = 0.5;
	//private double intensityY = 0.5;
	//private double lambda = 60; //longueur d'onde de la balle sinusoidale
	//private double T = 3; //P�riode de la balle sinusoidale
	private double spdX, spdY;
	private double ballX, ballY;
	private double eCin, ePot;
	private AbstractBall ball;
	private final Color vectGravColor = Color.GREEN.darker();
	private final Color vectSpdColor = Color.BLACK;
	private final Color vectNormColor = Color.BLUE;
	private final Color vectElecColor = Color.ORANGE;
	
	/********************** fichiers ***************************************/
	private AudioClip audio;
	private BufferedImage backgroundImg;
	private TexturePaint background;
	
	/*********************** composants *************************************/
	private Button btnPause;
	private Button btnPlay;
	private Button btnBallType;
	private Button btnParameters;	
	private BallSelectorPane optionPanel; //afficheur du menu d'option des types de balle
	private ParameterPane parameterPanel; //afficheur du menu d'option des param�tres
	
	/**
	 * Initialise les diff�rentes propri�t�s du jeu.
	 */
	public void init() {
		if(!loaded){
			/********************************** initialisation **************************************/
			calculerMatriceMondeVersComposant(GamePanel.REAL_SIZE);
			isCamFixed = true;
			landed = false;
			ballPosCount = 0;
			
			if(saveData == null){
				saveData = new ArrayList<Object[]>();
				score = 0;
				ballsThrown = 0;
			}else{
				for(int i=0; i<saveData.size(); i++){
					score += (int) saveData.get(i)[DATA_INDEX_DISTANCE];
				}
				ballsThrown = saveData.size();
			}
	
			handler = new Handler(worldMatrix);
			handler.loadImage();
			cam = new Camera(0,0,0,1000*worldMatrix.getScaleX(),worldMatrix); //Le monde mesure 1000m, mais la cam�ra ne se d�place pas dans le monde r�el.
			
			components = new ArrayList<Component>();
			btnPause = new Button("", "/game/pause_");
			btnPlay = new Button("Lancer", "/game/launch_");
			btnBallType = new Button("", "/game/type_");
			btnParameters = new Button("", "/game/tool_");
			
			ballTraj1 = new ArrayList<Ellipse2D.Double>();
			ballTraj2 = new ArrayList<Ellipse2D.Double>();
			
			/********************************** corps *********************************************/
			btnPause.setBounds(GamePanel.WIDTH-BTN_SIZE*2, 10, BTN_SIZE/2, BTN_SIZE/2);
			btnPause.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					requestChange(GameStateManager.PAUSE_STATE);
				}
			});
						
			optionPanel = new BallSelectorPane(components);
			optionPanel.setVisible(false);
			
			parameterPanel = new ParameterPane(components);
			parameterPanel.setVisible(false);

			optionPanel.addOptionsListener(new OptionsListener() {
				@Override
				public void setInfoEnabled(boolean enabled) {
					cfgPrintInfo = enabled && cfgInfoEnabled;
				}

				@Override
				public void setSFXEnabled(boolean enabled) {}

				@Override
				public void setBGMEnabled(boolean enabled) {}
			});
			
			parameterPanel.addOptionsListener(new OptionsListener() {
				@Override
				public void setInfoEnabled(boolean enabled) {
					cfgPrintInfo = enabled && cfgInfoEnabled;
				}

				@Override
				public void setSFXEnabled(boolean enabled) {}

				@Override
				public void setBGMEnabled(boolean enabled) {}
			});
			
			btnPlay.setBounds(BTN_SIZE, GamePanel.HEIGHT-BTN_SIZE*2, BTN_SIZE, BTN_SIZE);
			btnPlay.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(cfgSfxEnabled) {
						audio.play();
					}
					
					ball = createBall(); //cr�er le type de balle � lancer al�atoirement
					
					try {
						Thread.sleep(2000); //attendre une 1second pour laisser le temps de se pr�parer pour frapper la balle
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
					
					isCamFixed = true;
					
					/************************* cr�er la balle *******************************************/			
					ball.addScoreListener(new ScoreListener() { //ajouter score lorsque la balle att�rit au sol
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

					if(ball instanceof SinusBall){
						cam.setPos(ball.getX()*worldMatrix.getScaleX(), 0);
						ball.setVelX(-ball.getMaxSpeedX()*parameterPanel.getIntensityX()); // m�tre/seconde
						ball.setVelY(-parameterPanel.getAmplitude());
						nextData[DATA_INDEX_INCOMINGSPEED] = new ArrayList<Double>(Arrays.asList(ball.getVelX(), ball.getVelY(), parameterPanel.getLambda(), parameterPanel.getT(), parameterPanel.getAmplitude()));
					}else{
						cam.setPos(ball.getX()*worldMatrix.getScaleX(), 0);						
						double spdX;
						double spdY;
						if(ball instanceof FastBall) {
							spdX = -parameterPanel.getFastSpdX();
							spdY = -parameterPanel.getFastSpdY();
						} else {
							spdX = -parameterPanel.getOtherSpdX();
							spdY = -parameterPanel.getOtherSpdY();
						}
						
						ball.setVelX(spdX); // m�tre/seconde
						ball.setVelY(spdY);
						nextData[DATA_INDEX_INCOMINGSPEED] = new ArrayList<Double>(Arrays.asList(ball.getVelX(), ball.getVelY()));
					}
					handler.addObject(ball);
					ballsThrown++;
					/************************************************************************************/
					
					ballTraj1.removeAll(ballTraj1);
					ballTraj2.removeAll(ballTraj2);
					
					btnPlay.setVisible(false);
					btnBallType.setVisible(false);
					btnParameters.setVisible(false);
				}
			});
			
			btnBallType.setBounds(GamePanel.WIDTH-BTN_SIZE*2/3-SPACING, GamePanel.HEIGHT*4/10+BTN_SIZE/3, BTN_SIZE*2/3, BTN_SIZE*2/3);
			btnBallType.setToolTipText("Choisir un type de balle ici");
			btnBallType.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					optionPanel.setVisible(true);
					btnPlay.setVisible(false);
					btnPause.setVisible(false);
					btnBallType.setVisible(false);
					btnParameters.setVisible(false);
					cfgPrintInfo = false;
				}
			});
			
			btnParameters.setBounds(GamePanel.WIDTH-BTN_SIZE*2/3-SPACING, GamePanel.HEIGHT*5/10+BTN_SIZE/3, BTN_SIZE*2/3, BTN_SIZE*2/3);
			btnParameters.setToolTipText("Acc�der aux param�tres de jeu ici");
			btnParameters.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					parameterPanel.setVisible(true);
					btnPlay.setVisible(false);
					btnPause.setVisible(false);
					btnParameters.setVisible(false);
					btnBallType.setVisible(false);
					cfgPrintInfo = false;
				}
			});
			
			/********************************** Ajout Swing ***************************************/			
			components.add(btnPlay);
			components.add(btnPause);
			components.add(btnBallType);
			components.add(btnParameters);
			components.add(optionPanel);
			components.add(parameterPanel);
			
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
	 * Mise � jour des propri�t�s de diff�rents objets de jeu et les comportements du jeu.
	 * Appelle la m�thode {@link Handler#update()} pour mettre � jour les objets de jeu, puis met � jour la
	 * cam�ra gr�ce � sa m�thode {@link Camera#tick(GameObject)} et une r�f�rence � la balle active.
	 */
	public void update() {
		handler.update();
		
		//Nous sommes oblig�s de garder une r�f�rence � la balle � cause de l'�couteur du bouton "Lancer".
		//Puisqu'il ne s'agit que d'une r�f�rence, nous ne somme pas oblig�s d'obtenir celle du gestionnaire.
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
	}

	/**
	 * Mise � jour des dessins des diff�rents objets de jeu et le dessin du jeu.
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
		g2d.drawString("Balles lanc�es "+ballsThrown, SPACING, SPACING); //balles lanc�es
		FontRenderContext context = g2d.getFontRenderContext();
		TextLayout txt = new TextLayout("Pointage: "+score, GAME_FONT, context);
		g2d.drawString("Pointage: "+score, (int)(GamePanel.WIDTH-txt.getBounds().getWidth())/2, 30); //pointage
		g2d.setFont(oFont);
		/*********************************************/
		
		//translation de la cam�ra
		AffineTransform oTransform = g2d.getTransform();
		g2d.translate(cam.getX(), cam.getY());
			if(ballTraj1 != null) {
				g2d.setColor(Color.BLACK);
				for(int i=0; i<ballTraj1.size(); i+=2) { //�viter les erreurs de concurrence en r�duisant le nombre de points dessin�s en jeu
					g2d.fill(worldMatrix.createTransformedShape(ballTraj1.get(i)));
				}
			}		
			if(ballTraj2 != null) {
				g2d.setColor(Color.WHITE);
				for(int i=0; i<ballTraj2.size(); i+=2) { //�viter les erreurs de concurrence en r�duisant le nombre de points dessin�s en jeu
					g2d.fill(worldMatrix.createTransformedShape(ballTraj2.get(i)));
				}
			}
			//dessin des lignes du zone de frappe
			g2d.setColor(Color.BLACK);
			for(int y = (int)(-5*GamePanel.HEIGHT); y < GamePanel.HEIGHT; y+=LINE_LENGTH+LINE_SPACE) {
				g2d.fillRect(GamePanel.WIDTH/2, y, 5, LINE_LENGTH);
			}
			handler.render(g2d);		
			//dessin vecteur
			if(cfgPrintInfo && ball != null && !(ball instanceof TrollfaceBall)){
				ball.getVectorGrav().draw(g2d, vectGravColor, worldMatrix);
				
				if(ball.getVectorSpd() != null) {
					ball.getVectorSpd().draw(g2d, vectSpdColor, worldMatrix);
				}
				
				if(ball.getVectorNorm() != null) {
					ball.getVectorNorm().draw(g2d, vectNormColor, worldMatrix);	
				}
			}
		g2d.setTransform(oTransform);
		
		if(!isCamFixed) {
			g2d.setColor(Color.BLACK);
			//traits du cam�ra horizontals
			g2d.fillRect(SPACING, SPACING, CAMERA_LOCK_WIDTH, CAMERA_LOCK_HEIGHT);
			g2d.fillRect(GamePanel.WIDTH-SPACING-CAMERA_LOCK_WIDTH, SPACING, CAMERA_LOCK_WIDTH, CAMERA_LOCK_HEIGHT);
			g2d.fillRect(SPACING, GamePanel.HEIGHT-SPACING-CAMERA_LOCK_HEIGHT, CAMERA_LOCK_WIDTH, CAMERA_LOCK_HEIGHT);
			g2d.fillRect(GamePanel.WIDTH-SPACING-CAMERA_LOCK_WIDTH, GamePanel.HEIGHT-SPACING-CAMERA_LOCK_HEIGHT, CAMERA_LOCK_WIDTH, CAMERA_LOCK_HEIGHT);
			
			//traits du cam�ra verticals
			g2d.fillRect(SPACING, SPACING, CAMERA_LOCK_HEIGHT, CAMERA_LOCK_WIDTH);
			g2d.fillRect(GamePanel.WIDTH-SPACING-CAMERA_LOCK_HEIGHT, SPACING, CAMERA_LOCK_HEIGHT, CAMERA_LOCK_WIDTH);
			g2d.fillRect(SPACING, GamePanel.HEIGHT-CAMERA_LOCK_WIDTH-SPACING, CAMERA_LOCK_HEIGHT, CAMERA_LOCK_WIDTH);
			g2d.fillRect(GamePanel.WIDTH-SPACING-CAMERA_LOCK_HEIGHT, GamePanel.HEIGHT-CAMERA_LOCK_WIDTH-SPACING, CAMERA_LOCK_HEIGHT, CAMERA_LOCK_WIDTH);
		}		
		
		if(cfgPrintInfo) {
			displayInfo(g2d);
		}		
		
	}
	
	/**
	 * M�thode qui dessine le panneau d'informations scientifiques
	 * @param g2d ({@link Graphics2D}) le contexte graphique 2D du jeu.
	 */
	private void displayInfo(Graphics2D g2d) {
		int screenX = GamePanel.WIDTH*3/4;
		int screenY = GamePanel.HEIGHT*3/5;
		int margin = 20;
		int lineSpacing = 20;
		g2d.setColor(new Color(0,0,0, 200));
		g2d.fillRect(screenX, screenY, GamePanel.WIDTH/4, GamePanel.HEIGHT/2);
		
		double forceGrav;
		double forceNorm;
		double forceElec;
		double impulse;
		
		if(ball == null) {
			spdX = 0;
			spdY = 0;
			ballX = 0;
			ballY = 0;
			eCin = 0;
			ePot = 0;
			forceGrav = 0;
			forceNorm = 0;
			forceElec = 0;
			impulse = 0;
		} else {
			ballX = ball.getX()+ball.getWidth()/2 - GamePanel.REAL_SIZE/2;
			ballY = FreeModeState.straightWorldHeight - Platform.SIZE - (ball.getY()+ball.getHeight()/2);
			spdX = ball.getVelX();
			spdY = -ball.getVelY();
			eCin = ball.getECin();
			ePot = ball.getEPot();
			forceGrav = ball.getForceGrav();
			forceNorm = ball.getForceNorm();
			if(ball instanceof ElectricBall) {
				forceElec = ((ElectricBall)ball).getForceElec();
			} else {
				forceElec = 0;
			}
			impulse = Math.abs(ball.getImpulse());
		}
		
		Font oFont = g2d.getFont();
		g2d.setColor(Color.WHITE);
		g2d.setFont(INFO_FONT);
		g2d.drawString("position de la balle en x : " + String.format("%.2f", ballX) + " m", screenX+margin, screenY+margin);
		g2d.drawString("position de la balle en y : " + String.format("%.2f", ballY)+ " m", screenX+margin, screenY+margin+lineSpacing);
		g2d.drawString("vitesse de la balle en x : " + String.format("%.2f", spdX)+ " m/s", screenX+margin, screenY+margin+2*lineSpacing);
		g2d.drawString("vitesse de la balle en y : " + String.format("%.2f", spdY)+ " m/s", screenX+margin, screenY+margin+3*lineSpacing);
		g2d.setColor(vectGravColor);
		g2d.drawString("force gravitationnelle: " + String.format("%.2f", forceGrav)+ " N", screenX+margin, screenY+margin+4*lineSpacing);
		g2d.setColor(Color.CYAN);
		g2d.drawString("force normale : " + String.format("%.2f", forceNorm)+ " N", screenX+margin, screenY+margin+5*lineSpacing);
		g2d.setColor(vectElecColor);
		g2d.drawString("force �lectrique : " + String.format("%.2f", forceElec)+ " N", screenX+margin, screenY+margin+6*lineSpacing);
		g2d.setColor(Color.WHITE);
		g2d.drawString("�nergie cin�tique de la balle : " + String.format("%.2f", eCin)+ " J", screenX+margin, screenY+margin+7*lineSpacing);
		g2d.drawString("�nergie potentielle de la balle : " + String.format("%.2f", ePot)+ " J", screenX+margin, screenY+margin+8*lineSpacing);
		g2d.drawString("force d'impulsion : " + String.format("%.2f", impulse)+ " N/s", screenX+margin, screenY+margin+9*lineSpacing);
		g2d.setFont(oFont);
	}
	
	/**
	 * Calculer la matrice monde vers composant qui sera appliqu�e sur les dimensions de tous les objets de jeu. 
	 * @param worldSize L'indice de grandeur du monde r�el.
	 */
	private void calculerMatriceMondeVersComposant(double worldSize) {
		worldMatrix = new AffineTransform();  //donne une matrice identit�

		pixelsByUnitX =  GamePanel.WIDTH / worldSize;
		double ratio = GamePanel.HEIGHT / (double)GamePanel.WIDTH;
		straightWorldHeight = worldSize * ratio;
		pixelsByUnitY = GamePanel.HEIGHT / straightWorldHeight ;

		worldMatrix.scale( pixelsByUnitX, pixelsByUnitY );
		GameStateManager.GROUND_LEVEL = straightWorldHeight-Platform.SIZE;
	}
	
	/**
	 * G�n�rer une balle associ�e � un type de balle al�atoirement
	 * @return la balle d'un certain type
	 */
	private AbstractBall createBall() {
		AbstractBall ball;
		BallType type = BallType.INVALID_BALL;
		nextData = new Object[NB_DATA_INDEX];
		
		Random random = new Random();
		ArrayList<Integer> enabledTypes = new ArrayList<Integer>();
		for(int i=1; i<BallType.values().length; i++){
			if(BallType.values()[i].isEnabled()){
				enabledTypes.add(i);
			}
		}
		if(enabledTypes.size() > 0) {
			int temp = random.nextInt(enabledTypes.size());
			type = BallType.values()[enabledTypes.get(temp)];
		}
		
		switch (type) {
			case NORMAL_BALL :
				ball = new NormalBall(GamePanel.REAL_SIZE, (GamePanel.HEIGHT-200)/pixelsByUnitX, 50/pixelsByUnitX, parameterPanel.getMass(), worldMatrix, handler, cam);
				break;
			case FAST_BALL :
				ball = new FastBall(GamePanel.REAL_SIZE, (GamePanel.HEIGHT-200)/pixelsByUnitX, 50/pixelsByUnitX, parameterPanel.getMass(), worldMatrix, handler, cam);
				break;
			case SINUS_BALL :
				ball = new SinusBall(GamePanel.REAL_SIZE, (GamePanel.HEIGHT/2)/pixelsByUnitX, 50/pixelsByUnitX, parameterPanel.getMass(), parameterPanel.getLambda(), parameterPanel.getT(), worldMatrix, handler, cam);
				break;
			case FRICTION_BALL :
				ball = new FrictionBall(GamePanel.REAL_SIZE, (GamePanel.HEIGHT-200)/pixelsByUnitX, 50/pixelsByUnitX, parameterPanel.getMass(), worldMatrix, handler, cam);
				break;
			case ELECTRIC_BALL :
				ball = new ElectricBall(GamePanel.REAL_SIZE, (GamePanel.HEIGHT-200)/pixelsByUnitX, 50/pixelsByUnitX, parameterPanel.getMass(), parameterPanel.getCharge(), worldMatrix, handler, cam);
				nextData = new Object[NB_DATA_INDEX+1];
				nextData[DATA_INDEX_ELECTRIC_CHARGE] = parameterPanel.getCharge();
				break;
			default :
				ball = new TrollfaceBall(GamePanel.REAL_SIZE, (GamePanel.HEIGHT-500)/pixelsByUnitX, 300/pixelsByUnitX, worldMatrix, handler, cam);
				ballsThrown--;
				break;
		}
		ball.setSFXEnabled(cfgSfxEnabled);
		
		nextData[DATA_INDEX_BALLTYPE] = type.ordinal();
		
		return ball;
	}
	
	/**
	 * M�thode qui permet de r�initialiser les propri�t�s et les composants du jeu
	 */
	public void reset(){
		/*System.out.println("SaveData: ");
		for(Object[] array : saveData){
			System.out.println(Arrays.toString(array));
		}*/
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(App05FlickBaseball.FM_SAVE_NAME))){
			oos.writeObject(saveData);
		} catch (Exception e) {
			e.printStackTrace();
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
					System.err.println("Probl�me lors de la r�initialisation d'une variable");
				}
			}
		}
		for(BallType type : BallType.values()){
			type.setEnabled(true);
		}
	}
	
	/**
	 * M�thode qui permet de r�initialiser les propri�t�s de la balle active.
	 */
	public void resetBallObject() {
		if((int)nextData[DATA_INDEX_BALLTYPE] != BallType.INVALID_BALL.ordinal()){
			if(nextData[DATA_INDEX_DISTANCE] == null)
				nextData[DATA_INDEX_DISTANCE] = 0;
			saveData.add(nextData);
		}
		nextData = null;
		handler.removeObject(ball);
		ball = null;
		btnPlay.setVisible(true);
		btnPause.setVisible(true);
		btnBallType.setVisible(true);
		btnParameters.setVisible(true);
		cam.setX(0);
		cam.setY(0);
		isCamFixed = true;
		landed = false;
	}
	
	/**
	 * Retourner la structure de donn�e qui d�tient tous les boutons Swing.
	 * @return La structure de donn�e qui d�tient tous les boutons Swing.
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
	 * Retourner l'�tat boolean des effets de son
	 * @return boolean
	 */
	public boolean isSFXEnabled() {
		return cfgSfxEnabled;
	}
	
	/**
	 * Modifier l'�tat boolean des effets de son
	 * @param enabled l'�tat boolean des effets de son
	 */
	public void setSFXEnabled(boolean enabled) {
		cfgSfxEnabled = enabled;
	}
	
	/**
	 * Retourner l'�tat boolean du panneau d'informations scientifiques
	 * @return boolean
	 */
	public boolean isInfoIsEnabled() {
		return cfgInfoEnabled;
	}
	
	/**
	 * Modifier l'�tat boolean du panneau d'informations scientifiques
	 * @param enabled l'�tat boolean du panneau d'informations scientifiques
	 */
	public void setInfoEnabled(boolean enabled) {
		cfgInfoEnabled = enabled;
		cfgPrintInfo = cfgInfoEnabled;
	}
	
	/**
	 * Retourner l'�tat boolean du d�placement de la cam�ra
	 * @return boolean
	 */
	public boolean isCamFixed() {
		return isCamFixed;
	}
	
	/**
	 * Modifier l'�tat boolean du d�placement de la cam�ra
	 * @param isCamFixed l'�tat boolean du d�placement de la cam�ra
	 */
	public void setCamFixed(boolean isCamFixed) {
		this.isCamFixed = isCamFixed;
	}
	
	/**
	 * Retourner l'�tat boolean de la tomb�e de la balle.
	 * @return boolean
	 */
	public boolean isLanded() {
		return landed;
	}
	
	/**
	 * Permet d'ajouter un �couteur pour les changements d'�tats.
	 * @param l Un �couteur d'�tat de jeu.
	 */
	public void addGameStateListener(GameStateListener l){
		REGISTERED_LISTENERS.add(GameStateListener.class, l);
	}
	
	/**
	 * Passer vers un autre menu
	 * @param state le menu qu'on d�sire y acc�der
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