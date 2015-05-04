package drawable.gameobject.ball;

import static util.Constants.DT;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import util.BufferedImageLoader;
import application.Camera;
import drawable.gameobject.ElectricPlatform;
import drawable.gameobject.GameObject;
import drawable.gameobject.Handler;
import drawable.util.Vector2D;

/**
 * Classe qui crée la balle électrique qui réagit lorsqu'elle approche d'une plaque chargée.
 * 
 * @author Alexandre Hua
 * @version 04-05-2015
 */
public class ElectricBall extends AbstractBall{
	public static final BufferedImage IMG = new BufferedImageLoader().loadImage("/game/ElectricBall.png");
	public static final BufferedImage DISABLED_IMG = new BufferedImageLoader().loadImage("/game/ElectricBall_disabled.png");
	private static final BufferedImage IMG_P = new BufferedImageLoader().loadImage("/game/ElectricBall_Positive.png");
	private static final BufferedImage IMG_N = new BufferedImageLoader().loadImage("/game/ElectricBall_Negative.png");
	private BufferedImage texture;
	
	public static final int MAX_SPEED_X = 50;
	public static final int MAX_SPEED_Y = 30;
	
	private double q; //charge de la ablle en coulomb
	private ArrayList<ElectricPlatform> elecPlat;
	private Color vectElecColor = Color.ORANGE.darker();
	
	private boolean isInZone = false;
	
	private Vector2D vectElecForce;
	
	private double forceElec;
	
	/**
	 * <b>Constructeur</b>
	 * <p>Créer la balle.</p>
	 * @param x Position en x de la balle.
	 * @param y Position en y de la balle.
	 * @param diameter Diamètre de la balle.
	 * @param mass la masse de la balle en grammes.
	 * @param charge la charge électrique de la balle
	 * @param worldMatrix Matrice monde vers composant.
	 * @param handler Gestionnaire des objets de jeu.
	 * @param cam la caméra fixée à la balle
	 */
	public ElectricBall(double x, double y, double diameter, double mass, double charge, AffineTransform worldMatrix, Handler handler, Camera cam) {
		super(x, y, diameter, mass, worldMatrix, handler, cam);
		q = charge;
		if(charge >= 0) {
			texture = IMG_P;
		} else {
			texture = IMG_N;
		}
		this.bounds = new Ellipse2D.Double(x, y, this.getWidth(), this.getHeight());
		maxSpeedX = MAX_SPEED_X;
		maxSpeedY = MAX_SPEED_Y;
		
		super.createVector();
		vectElecForce = new Vector2D(new Point2D.Double(getX()+getWidth()/2, getY()+getHeight()/2), 0,0);
		
		elecPlat = new ArrayList<ElectricPlatform>();
		for(int i = 0; i < handler.getObjects().size(); i++) {
			GameObject go = handler.getObjects().get(i);
			if(go instanceof ElectricPlatform) {
				elecPlat.add((ElectricPlatform) go);
			}
		}
	}

	/**
	 * Mise à jour des différents paramètres de la balle.
	 */
	public void update() {
		super.calculateForce();
		if(getVectorNorm() != null) {
			setVectorNorm(getVectorNorm().add(vectElecForce.multiply(-1)));
			setForceNorm(getVectorNorm().modulus());
		}
		super.applyGravity();
		applyElectricField();
		if(!isInZone) {
			vectElecForce = new Vector2D(new Point2D.Double(getX()+getWidth()/2, getY()+getHeight()/2), 0,0);
		}
		forceElec = vectElecForce.modulus();
		super.createVector();
		bounds = new Ellipse2D.Double(getX(), getY(), getWidth(), getHeight());
		super.applyRotation();
		super.collision();
		super.update();
	}

	/**
	 * Dessiner la balle.
	 * @param g2d ({@link Graphics2D}) Le contexte graphique sur lequel dessiner la balle.
	 * @see AffineTransform#createTransformedShape(Shape)
	 */
	public void render(Graphics2D g2d) {
		boundsTransf = getWorldMatrix().createTransformedShape(bounds);
		AffineTransform oTransf = g2d.getTransform();
		g2d.rotate(rotation, boundsTransf.getBounds().getX()+boundsTransf.getBounds().getWidth()/2, boundsTransf.getBounds().getY()+boundsTransf.getBounds().getHeight()/2);
		g2d.drawImage(texture, (int)boundsTransf.getBounds().getX(), (int)boundsTransf.getBounds().getY(), (int)boundsTransf.getBounds().getWidth(), (int)boundsTransf.getBounds().getHeight(), null);
		g2d.setTransform(oTransf);
		/*g2d.setColor(Color.GREEN);
		g2d.fill(getBoundsTop());
		g2d.fill(getBoundsBot());
		g2d.fill(getBoundsLeft());
		g2d.fill(getBoundsRight());*/
		
		/*if(FreeModeState.infoIsEnabled) {
			vectGrav.draw(g2d, getWorldMatrix());
			vectSpeed.draw(g2d, getWorldMatrix());
		}*/
		
		if(isInZone) {
			vectElecForce.draw(g2d, vectElecColor, getWorldMatrix());
			isInZone = false;
		}
	}
	
	/**
	 * Retourner l'image de texture de la balle.
	 * @return l'image de texture de la balle
	 */
	public BufferedImage getImage() {
		return null;
	}
	
	/**
	 * Appliquer la force électrique sur la balle par une plaque électrique chargée. Cette force ne s'applique seulement lorsque la balle passe proche de la plaque.
	 */
	private void applyElectricField() { //éviter le loop
		for(int i = 0; i < elecPlat.size(); i++) {
			ElectricPlatform ep = null;
			try{
				ep = elecPlat.get(i);
					//le champ électrique ne s'applique seulement lorsque la balle se trouve dans la zone du bloc électrique
					//if(getX()+getWidth()/2 >= go.getX()-go.getWidth()/2 && getX()+getWidth()/2 <= go.getX()+go.getWidth()+go.getWidth()/2) {
					if((getX()+getWidth()/2) >= ep.getX() && (getX()+getWidth()/2) <= (ep.getX()+ep.getWidth())) {
						isInZone = true;
						Vector2D vectTemp = new Vector2D(((ElectricPlatform) ep).getVectElectricField());
						
						if(getY() < ep.getY()) {
							vectElecForce = vectTemp.multiply(q);
						} else {
							vectElecForce = vectTemp.multiply(-q);
						}
						vectElecForce.setPosition(new Point2D.Double(getX()+getWidth()/2, getY()+getHeight()/2));


						double accelerationX = vectElecForce.getX()/getMass();
						double accelerationY = -vectElecForce.getY()/getMass();
						setVelX(getVelX() + accelerationX*DT);
						setVelY(getVelY() + accelerationY*DT);
						
						break;
					}
				
			}catch(NullPointerException e){
				System.err.println("Exception dans Ball#collision(): " + e.getLocalizedMessage());
			}
		}
	}
	
	/**
	 * Retourner la force électrique appliquée sur la balle.
	 * @return la force électrique
	 */
	public double getForceElec() {
		return forceElec;
	}
	
}