package drawable.gameobject.ball;

import static util.Constants.COR_BAT;
import static util.Constants.COR_GRASS;
import static util.Constants.DT;
import static util.Constants.G;
import static util.Constants.KFC_GRASS;
import gamestate.FreeModeState;
import gamestate.GameStateManager;

import java.applet.AudioClip;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import javax.swing.event.EventListenerList;

import util.AudioLoader;
import util.BallListener;
import util.ScoreListener;
import application.Camera;
import application.GamePanel;
import drawable.gameobject.BaseBallBat;
import drawable.gameobject.ElectricPlatform;
import drawable.gameobject.GameObject;
import drawable.gameobject.Handler;
import drawable.gameobject.Obstacles;
import drawable.gameobject.Platform;
import drawable.util.Vector2D;

/**
 * Classe modèle d'une balle qui fournit aux classes d'hériter des propriétés protégées et des méthodes de ces objets.
 * Elle permet d'associer des propriétés uniques pour chaque type de balle.
 * @author Olivier St-Jean
 * @author Alexandre Hua
 * @version 04-05-2015
 */
public abstract class AbstractBall extends GameObject{
	public static final int NORMAL_BALL_TYPE = 0;
	public static final int FAST_BALL_TYPE = 1;
	public static final int SINUS_BALL_TYPE = 2;
	public static final int FRICTION_BALL_TYPE = 3;
	public static final int ELECTRIC_BALL_TYPE = 4;
	private boolean sfxIsEnabled = true;
	private final EventListenerList REGISTERED_LISTENERS = new EventListenerList();
	
	protected Handler handler;
	protected Camera cam;
	
	protected Ellipse2D.Double bounds;
	private static final double MAX_ROTATION_SPEED = 0.25;
	protected double maxRotation;
	protected double rotation;
	protected Shape boundsTransf;
	protected boolean falling = true;
	
	//protected double mass;
	protected double timeElapsed;
	
	protected boolean hitByBat = false; //boolean qui précise si la balle a été frappée pour la première fois
	protected boolean hitLanded = false; //boolean qui précise si la balle a touché le sol pour la première fois après avoir été frappée
	protected boolean landedFirst = true;
	protected boolean isOnGround = false; //boolean qui précise si la balle est sur le sol
	protected AudioClip hitSound;
	protected AudioClip groundSound;
	protected int maxSpeedX;
	protected int maxSpeedY;
	
	protected double forceNorm;
	protected double forceFric;
	protected double forceGrav;

	protected double forceNet;
	protected double forcePerp;
	protected double forcePar;
	protected double eCinX;
	protected double eCinY;
	protected double eCin;
	protected double ePot;
	protected double impulse;
	
	protected Vector2D vectGrav;
	protected Vector2D vectNet;
	protected Vector2D vectSpeed;
	protected Vector2D vectNorm;

	/**
	 * <b>Constructeur</b>
	 * <p>Initialiser les différentes propriétés de la balle<p>
	 * @param x la position initiale en x de la balle
	 * @param y la position initiale en y de la balle
	 * @param diameter la diamètre en x et en y de la balle
	 * @param mass la masse de la balle
	 * @param worldMatrix Matrice monde vers composant
	 * @param handler Gestionnaire des objets de jeu.
	 * @param cam la caméra fixée à la balle
	 */
	public AbstractBall(double x, double y, double diameter, double mass, AffineTransform worldMatrix, Handler handler, Camera cam) {
		super(x, y, diameter, diameter, mass, worldMatrix);
		this.handler = handler;
		this.cam = cam;
		//this.mass = mass;	
		this.maxRotation = MAX_ROTATION_SPEED;
		impulse = 0;
		
		AudioLoader audioLoader = new AudioLoader();
		hitSound = audioLoader.loadAudio("audio/BatHit.wav");
		groundSound = audioLoader.loadAudio("audio/grass.wav");
		//ballTypes.add(new NormalBall(x, y, diameter, worldMatrix, handler, cam));
	}
	
	/**
	 * <i>Mise à jour des différents paramètres de la balle.</i>
	 * <p>Le comportement de base gère l'horloge de la balle, pour appeler sa réinitialisation.</p>
	 */
	public void update(){
		timeElapsed += DT;
		if(timeElapsed >= 20){
			for(BallListener listener : REGISTERED_LISTENERS.getListeners(BallListener.class)){
				listener.resetBall();
			}
		}
		
		//Vérifier si la balle touche le sol
		if(getY()+getHeight() >= GameStateManager.GROUND_LEVEL) {
			isOnGround = true;
		} else {
			isOnGround = false;
		}
	}
	
	/**
	 * Dessiner la balle.
	 */
	public abstract void render(Graphics2D g2d);
	
	/**
	 * Retourner la zone de collision de la balle.
	 * @return L'aire de la forme en monde vers composant.
	 */
	public Area getBounds() {
		return new Area(boundsTransf);
	}
	
	/**
	 * Prédire la position de la prochaine zone de collision de la balle.
	 * @return La prochaine aire de la forme en monde vers composant.
	 */
	protected Area getNextBounds() {
		/*double yBeforeTemp = yBefore;
		double yNowTemp = yNow;
		double velYTemp = velY;
		if(falling) {
			yBeforeTemp = yNow;
			yNowTemp = 0.5*G*(deltaT+DT)*(deltaT+DT);
			velYTemp += yNowTemp-yBeforeTemp;
		}*/
		double nextVelY = getVelY()+G*DT;
		double nextY = getY()+nextVelY*DT;
		double nextX = getX()+getVelX()*DT;
		//return new Area(worldMatrix.createTransformedShape(new Ellipse2D.Double(x+velX/60, y+velYTemp/60, width, height)));
		return new Area(getWorldMatrix().createTransformedShape(new Ellipse2D.Double(nextX, nextY, getWidth(), getHeight())));
	}

	/**
	 * Retourner la zone de collision la plus proche de l'objet collisionné parmis les 4 zones de collision directionnels de la balle.
	 * @param targetBounds Objet collsionné avec la balle.
	 * @param areas Les 4 coins de zone de collision de la balle.
	 * @return Une des 4 zones de collsion de la balle.
	 */
	protected Area getNearestAreaToObject(Area targetBounds, Area ...areas) {
		ArrayList<Double> distance = new ArrayList<Double>();
		for(int i = 0; i<areas.length; i++) {
			double distanceX = Math.abs(targetBounds.getBounds().getX()+targetBounds.getBounds().getWidth()/2 - (areas[i].getBounds().getX()+areas[i].getBounds().getWidth()/2));
			double distanceY = Math.abs(targetBounds.getBounds().getY()+targetBounds.getBounds().getHeight()/2 - (areas[i].getBounds().getY()+areas[i].getBounds().getHeight()/2));
			distance.add(Math.sqrt(Math.pow(distanceX, 2) +	Math.pow(distanceY, 2)));
		}
		
		double smallest = Integer.MAX_VALUE;
		for(int i = 0; i < distance.size(); i++) {
			if(smallest > distance.get(i)) {
				smallest = distance.get(i);
			}
		}
		return areas[distance.indexOf(smallest)];
	}
	
	/**
	 * Retourner la zone de collision du côté haut de la balle.	
	 * @return La zone de collision du côté haut de la balle.
	 */
	protected Area getBoundsTop() {
		int areaX 		= (int)boundsTransf.getBounds().getX()+(int)boundsTransf.getBounds().getWidth()/2-(int)boundsTransf.getBounds().getWidth()/(2*10);
		int areaY 		= (int)(boundsTransf.getBounds().getY());
		int areaWidth 	= (int)boundsTransf.getBounds().getWidth()/10;
		int areaHeight 	= (int)boundsTransf.getBounds().getHeight()/10;
		Area areaTop = new Area(new Rectangle(areaX, areaY, areaWidth, areaHeight));
		return areaTop;
	}

	/**
	 * Retourner la zone de collision du côté bas de la balle.
	 * @return La zone de collision du côté bas de la balle.
	 */
	protected Area getBoundsBot() {
		int areaX 		= (int)boundsTransf.getBounds().getX()+(int)boundsTransf.getBounds().getWidth()/2-(int)boundsTransf.getBounds().getWidth()/(2*10);
		int areaY 		= (int)(boundsTransf.getBounds().getY()+boundsTransf.getBounds().getHeight()-(int)boundsTransf.getBounds().getHeight()/10);
		int areaWidth 	= (int)boundsTransf.getBounds().getWidth()/10;
		int areaHeight 	= (int)boundsTransf.getBounds().getHeight()/10;
		Area areaBot = new Area(new Rectangle(areaX, areaY, areaWidth, areaHeight));
		return areaBot;
	}

	/**
	 * Retourner la zone de collision du côté gauche de la balle.	
	 * @return La zone de collision du côté gauche de la balle.
	 */
	protected Area getBoundsLeft() {
		int areaX 		= (int)boundsTransf.getBounds().getX();
		int areaY 		= (int)(boundsTransf.getBounds().getY()+boundsTransf.getBounds().getHeight()/2-(int)boundsTransf.getBounds().getHeight()/(2*10));
		int areaWidth 	= (int)boundsTransf.getBounds().getWidth()/10;
		int areaHeight 	= (int)boundsTransf.getBounds().getHeight()/10;
		Area areaLeft = new Area(new Rectangle(areaX, areaY, areaWidth, areaHeight));
		return areaLeft;
	}

	/**
	 * Retourner la zone de collision du côté droit de la balle.	
	 * @return La zone de collision du côté droit de la balle.
	 */
	protected Area getBoundsRight() {
		int areaX = (int)boundsTransf.getBounds().getX()+(int)boundsTransf.getBounds().getWidth()-(int)boundsTransf.getBounds().getWidth()/10;
		int areaY = (int)(boundsTransf.getBounds().getY()+boundsTransf.getBounds().getHeight()/2-(int)boundsTransf.getBounds().getHeight()/(2*10));
		int areaWidth = (int)boundsTransf.getBounds().getWidth()/10;
		int areaHeight = (int)boundsTransf.getBounds().getHeight()/10;
		Area areaRight = new Area(new Rectangle(areaX, areaY, areaWidth, areaHeight));
		return areaRight;
	}
	
	/**
	 * Détecter s'il y a collision et gérer la collision selon le côté qui a touché en la faisant rebondir la balle.
	 * @see AbstractBall#getNextBounds()
	 * @see AbstractBall#getNearestAreaToObject(Area, Area...)
	 * @see AbstractBall#getBoundsBot()
	 * @see AbstractBall#getBoundsTop()
	 * @see AbstractBall#getBoundsLeft()
	 * @see AbstractBall#getBoundsRight()
	 * @see Area#intersect(Area)
	 * @see Area#isEmpty()
	 */
	protected void collision() {
		for(int i = 0; i < handler.getObjects().size(); i++) {
			GameObject go = null;
			try{
				go = handler.getObjects().get(i);
			}catch(NullPointerException e){
				System.err.println("Exception dans Ball#collision(): " + e.getLocalizedMessage());
			}
			
			if(go != null && (go instanceof Platform || go instanceof Obstacles || go instanceof ElectricPlatform || (go instanceof BaseBallBat && !hitByBat))) {
			//if(go != null && !(go instanceof AbstractBall) || (go instanceof BaseBallBat && !hitByBat)) {
				Area contact = getNextBounds();
				contact.intersect(go.getBounds());
				if(!contact.isEmpty()) {
					if(go instanceof BaseBallBat) { //le baton de baseball ne peut frapper la balle qu'une seule fois
						//System.out.println("Speed before: " + getVelX() + ", " + getVelY());
						cam.setPos(getX()*getWorldMatrix().getScaleX(), getY()*getWorldMatrix().getScaleY());
						
						applyImpulsion(go);
						
						hitByBat = true;
						if(sfxIsEnabled)
							hitSound.play();
					}else{
						//System.out.println("hit something");
						Area nearestArea = getNearestAreaToObject(go.getBounds(), getBoundsBot(), getBoundsTop(), getBoundsLeft(), getBoundsRight());
	
						//test pour savoir si la balle a attéri sur le sol et a fini de rebondir
						//si test est vrai, enlever la gravité et mettre la vélocité Y à 0
						//if(Math.abs(velY) < 2 && y > GamePanel.HEIGHT/worldMatrix.getScaleY()-Platform.SIZE-getHeight()-1) {
						/*if(ePot < 3 && eCinY == 0) {
							falling = false;
							setVelY(0);
						} else {
							falling = true;
						}*/
	
						if(nearestArea.equals(getBoundsBot())) {							
							if(falling) {
								if(!(this instanceof FrictionBall)) {
									setVelY(-getVelY()*COR_GRASS);
								} else {
									setVelY(0);
								}
								if(sfxIsEnabled)
									groundSound.play();
							} else {
								setY(getY() - 1*getWorldMatrix().getScaleY());
							}
							setY(go.getY() - getHeight());
							
							applyFriction();
							
							//lorsque la balle touche le sol pour la première fois après avoir été frappée par la batte, le point est entré
							if(getY() > GamePanel.HEIGHT/getWorldMatrix().getScaleY()-Platform.SIZE-getHeight()-1 && !hitLanded && hitByBat) {
								for(ScoreListener listener : REGISTERED_LISTENERS.getListeners(ScoreListener.class)){
									listener.getScore((int)getX());
								}
								hitLanded = true;
								if(landedFirst) {
									for(BallListener listener : REGISTERED_LISTENERS.getListeners(BallListener.class)){
										listener.landed();
									}
								}
								landedFirst = false;
							}
							//System.out.println("hit bot");
						}
	
						if(nearestArea.equals(getBoundsTop())) {
							setVelY(-getVelY()*COR_GRASS);
							setY(go.getY() + go.getHeight());
							//System.out.println("hit top");
						}
	
						if(nearestArea.equals(getBoundsLeft())) {
							setVelX(-getVelX()*COR_GRASS);
							setX(go.getX() + go.getWidth());
							//System.out.println("hit left");
						}
	
						if(nearestArea.equals(getBoundsRight())) {
							setVelX(-getVelX()*COR_GRASS);
							setX(go.getX() - getWidth());
							//System.out.println("hit right");
						}
					} //Fin if(BaseBallBat)
				} //Fin if(contact pas vide)
			} //Fin if(go)
		} //Fin for
	}
	
	/**
	 * Appliquer la force de gravité dans le mouvement ballistique de la balle
	 */
	protected void applyGravity() {
		setX(getX() + getVelX()*DT);
		setVelY(getVelY() + G*DT);
		setY(getY() + getVelY()*DT);
	}
	
	/**
	 * Appliquer une rotation à la balle. Cette rotation dépend de la vitesse en x de la balle.
	 */
	protected void applyRotation() {
		if(getVelX() >= maxSpeedX) {
			rotation += maxRotation;
		} else {
			rotation += getVelX()*maxRotation/maxSpeedX;
		}
	}
	
	/**
	 * Initialiser et calculer différentes forces.
	 */
	protected void calculateForce() {
		//Les forces
		forceGrav = getMass()*G;
		if(isOnGround) {
			forceNorm = forceGrav;
		} else {
			forceNorm = 0;
		}
		forceFric = KFC_GRASS*forceNorm;
		forcePerp = -forceGrav;
		forcePar = -forceFric;
		forceNet = Math.sqrt(forcePerp*forcePerp + forcePar*forcePar);
		double ballY = FreeModeState.straightWorldHeight - Platform.SIZE - (getY()+getHeight()/2); //position en y du centre de la balle
		//***PAS LE DESSOUS DE LA BALLE, DONC Y N'EST PAS 0 LORSQU'ELLE EST SUR LE SOL
		eCinX = 0.5*getMass()*Math.pow(getVelX(), 2);
		eCinY = 0.5*getMass()*Math.pow(Math.sqrt(getVelY()), 2);
		eCin = 0.5*getMass()*Math.pow(Math.sqrt(getVelX()*getVelX() + getVelY()*getVelY()), 2);
		ePot = getMass()*G*ballY;
	}
	
	/**
	 * Appliquer la force de frottement cinétique sur la balle au contact avec le sol.
	 */
	private void applyFriction() {
		//application de force de frottement
		double facing = getVelX()/Math.abs(getVelX()); //permet de savoir dans quel sens voyage la balle
		double acceleration = forceFric/getMass();
		
		//enlever force de friction lorsque la vitesse en x frôle la valeur 0
		if(getVelX() < 0.5 && getVelX() > -0.5) {
			setVelX(0);
		} else {
			setVelX(getVelX() - facing*acceleration*DT);
		}
	}
	
	/**
	 * Appliquer l'impulsion à la balle
	 * @param go la balle
	 */
	private void applyImpulsion(GameObject go) {
		Vector2D ballSpeed = new Vector2D(new Point2D.Double(this.getX()+this.getWidth()/2, this.getY()+this.getHeight()/2), this.getVelX(), this.getVelY());
		Vector2D batSpeed = new Vector2D(new Point2D.Double(go.getX()+go.getWidth(), go.getY()+go.getHeight()), go.getVelX(), go.getVelY());
		Vector2D normal = new Vector2D(new Point2D.Double(go.getX(), go.getY()), (this.getX()+this.getWidth()/2) - go.getX(), (this.getY()+this.getHeight()/2) - go.getY()).normalize();
		impulse = (-(1+COR_BAT) / (1/getMass() + 1/( go).getMass())) * normal.dot(ballSpeed.substract(batSpeed));
		Vector2D finalSpeed = ballSpeed.add(normal.multiply(impulse/getMass()));
		//System.out.println(finalSpeed);
		setVelX(finalSpeed.getX());
		setVelY(finalSpeed.getY());
		//System.out.println("Speed after: " + getVelX() + ", " + getVelY());
	}
	
	/**
	 * Initialiser les différents vecteurs de force.
	 */
	protected void createVector() {
		//Les vecteurs
		vectGrav = new Vector2D(new Point2D.Double(getX()+getWidth()/2, getY()+getHeight()/2), 0, -forceGrav);
		
		if(getVelX() != 0) {
			vectSpeed = new Vector2D(new Point2D.Double(getX()+getWidth()/2, getY()+getHeight()/2), getVelX(), -getVelY());
		} else {
			vectSpeed = null;
		}
		
		if(isOnGround) {
			vectNorm = new Vector2D(new Point2D.Double(getX()+getWidth()/2, getY()+getHeight()/2), 0, forceGrav);
		} else {
			vectNorm = null;
		}
	}
	
	/**
	 * La methode suivante génère une approximation polygonale de la forme, constituée de multiples segments de droite.
	 * On affiche ensuite à la console la suite de coordonnées formant cette approximation polygonale.
	 * 
	 * Notez ci-dessous que la methode getPathIterator qui permet de specifier une matrice de transformation ainsi qu'une precision desirée.
	 * 
	 * @author Caroline Houle
	 * @param oShape La forme que l'on doit approximer.
	 */
	/*protected ArrayList<Point2D> polygonalApprox(Shape oShape) {
		double[] coords = new double[2];
		
		//getPathIterator retourne un objet capable de traverser pas a pas toutes les coordonnees necessaires pour 
		//approximer la forme de facon polygonale.
		//Le premier parametre permet de specifier une matrice de transformation a appliquer a la forme (dasn cet exemple on utilise la matrice identite)
		//Le deuxieme parametre permet de specifier la distance maximale desiree entre tout segment de droite et la forme reele: plus
		//cette valeur est petite, plus l'approximation est constituee d'un grand nombre de petits segements de droite, donc plus
		//elle approxime bien la forme originale (mais plus long a generer). Dans cet exemple on utilise arbitrairement 1.5
		
		AffineTransform identity = new AffineTransform();
		PathIterator pi = oShape.getPathIterator(identity, 0.5);
		ArrayList<Point2D> points = new ArrayList<Point2D>();

		//visiter chacun des segments de l'approximation 
		while (pi.isDone() == false) {
			int type = pi.currentSegment(coords);
			switch (type) {
				case PathIterator.SEG_MOVETO:
					System.out.println("Move to " + coords[0] + ", " + coords[1]);
					points.add(new Point2D.Double(coords[0]*getWorldMatrix().getScaleX(), coords[1]*getWorldMatrix().getScaleY()));
					break;
				case PathIterator.SEG_LINETO:
					System.out.println("Line to " + coords[0] + ", " + coords[1]);
					points.add(new Point2D.Double(coords[0]*getWorldMatrix().getScaleX(), coords[1]*getWorldMatrix().getScaleY()));
					break;
				case PathIterator.SEG_CLOSE:
					System.out.println("Close");
					break;
			}//fin switch
			pi.next(); //on avance l'iterateur et on passe au prochain segment du path
		} // fin while
		
		return points;
	}*/ //fin methode
	
	/**
	 * Permet d'ajouter un écouteur pour la balle.
	 * @param l L'écouteur de balle.
	 */
	public void addBallListener(BallListener l){
		REGISTERED_LISTENERS.add(BallListener.class, l);
	}
	
	/**
	 * Permet d'ajouter un écouteur pour enregistrer le score.
	 * @param l L'écouteur de balle.
	 */
	public void addScoreListener(ScoreListener l){
		REGISTERED_LISTENERS.add(ScoreListener.class, l);
	}
	
	/**
	 * Retourner l'état boolean de la présence des effets de son.
	 * @return boolean
	 */
	public boolean isSFXEnabled() {
		return sfxIsEnabled;
	}
	
	/**
	 * Modifier l'état boolean de la présence des effets de son.
	 * @param enabled l'état boolean de la présence des effets de son
	 */
	public void setSFXEnabled(boolean enabled) {
		sfxIsEnabled = enabled;
	}
	
	/**
	 * Retourner la vitesse maximale en x de la balle.
	 * @return la vitesse maximale en x de la balle
	 */
	public int getMaxSpeedX() {
		return maxSpeedX;
	}
	
	/**
	 * Retourner la vitesse maximale en y de la balle.
	 * @return la vitesse maximale en y de la balle
	 */
	public int getMaxSpeedY() {
		return maxSpeedY;
	}
	
	/**
	 * Retourner la masse de la balle.
	 * @return la masse de la balle
	 */
	/*public double getMass() {
		return mass;
	}*/
	
	/**
	 * Retourner l'état boolean pour connaitre si la balle a déjà été frappée une fois.
	 * @return boolean
	 */
	public boolean getHitByBat() {
		return hitByBat;
	}
	
	/**
	 * Retourner les vecteurs de force
	 * @return structure de donnée contenant les vecteurs de force
	 */
	public Vector2D[] getVectors(){
		return new Vector2D[]{vectGrav, vectSpeed};
	}
	
	/**
	 * Retourner le vecteur de gravité
	 * @return vecteur de gravité
	 */
	public Vector2D getVectorGrav(){
		return vectGrav;
	}
	
	/**
	 * Retourner le vecteur de vitesse
	 * @return vecteur de vitesse
	 */
	public Vector2D getVectorSpd(){
		return vectSpeed;
	}
	
	/**
	 * Retourner le vecteur de la normale
	 * @return vecteur de la normale
	 */
	public Vector2D getVectorNorm(){
		return vectNorm;
	}
	
	/**
	 * Modifier le vecteur de la normale
	 * @param vectNorm nouveau vecteur de la normale
	 */
	public void setVectorNorm(Vector2D vectNorm){
		this.vectNorm = vectNorm;
	}
	
	/**
	 * Retourner l'énergie cinétique de la balle
	 * @return l'énergie cinétique de la balle
	 */
	public double getECin(){
		return eCin;
	}
	
	/**
	 * Retourner l'énergie potentielle de la balle
	 * @return l'énergie potentielle de la balle
	 */
	public double getEPot(){
		return ePot;
	}
	
	/**
	 * Retourner la force normale appliquée sur la balle
	 * @return la force normale
	 */
	public double getForceNorm() {
		return forceNorm;
	}
	
	/**
	 * Modifier la force normale appliquée sur la balle
	 * @param forceNorm la nouvelle force normale
	 */
	public void setForceNorm(double forceNorm) {
		this.forceNorm = forceNorm;
	}
	
	/**
	 * Retourner la force de frottement appliquée sur la balle
	 * @return la force de frottement
	 */
	public double getForceFric() {
		return forceFric;
	}
	
	/**
	 * Retourner la force gravitationnelle appliquée sur la balle
	 * @return la force gravitationnelle
	 */
	public double getForceGrav() {
		return forceGrav;
	}
	
	/**
	 * Retourner la force d'impulsion appliquée sur la balle
	 * @return la force d'implusion
	 */
	public double getImpulse() {
		return impulse;
	}
	
	/**
	 * Retourner la valeur de la masse et des bornes de la balle
	 * @return String
	 */
	public String toString(){
		String str = this.getClass().getSimpleName() + "\n"
				+ "Masse: " + getMass() + " Bornes: " + this.bounds;
		return str;
	}
	
}
