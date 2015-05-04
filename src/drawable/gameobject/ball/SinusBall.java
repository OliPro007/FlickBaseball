package drawable.gameobject.ball;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;

import util.BufferedImageLoader;
import util.Constants;
import application.Camera;
import drawable.gameobject.Handler;

/**
 * Classe qui crée la balle qui représente la trajectoire d'une onde progrssif.
 * 
 * @author Alexandre Hua
 * @version 04-05-2015
 */
public class SinusBall extends AbstractBall{
	public static final BufferedImage IMG = new BufferedImageLoader().loadImage("/game/SinusBall.png");
	public static final BufferedImage DISABLED_IMG = new BufferedImageLoader().loadImage("/game/SinusBall_disabled.png");
	public static final int MAX_SPEED_X = 50;
	public static final int MAX_AMPLITUDE = 15;
	
	//private double A; //amplitude de l'onde
	//private double lambda; //longueur d'onde (mètre) --> distance pour effectuer un cycle complet
	//private double T; //période (seconde) --> temps pour effectuer un cycle complet
	private double k; //nombre d'onde (mètre)
	private double w; //fréquence angulaire (radian)
	private double b;; //l'axe central de la fonction sinus, position initiale (mètre)
	
	/**
	 * <b>Constructeur</b>
	 * <p>Créer la balle.</p>
	 * @param x Position en x de la balle.
	 * @param y Position en y de la balle.
	 * @param diameter Diamètre de la balle.
	 * @param mass la masse de la balle en grammes.
	 * @param lambda Longueur d'onde de la trajectoire sinoidale.
	 * @param T Période de la trajectoire sinusoidale.
	 * @param worldMatrix Matrice monde vers composant.
	 * @param handler Gestionnaire des objets de jeu.
	 * @param cam la caméra fixée à la balle
	 */
	public SinusBall(double x, double y, double diameter, double mass, double lambda, double T, AffineTransform worldMatrix, Handler handler, Camera cam) {
		super(x, y, diameter, mass, worldMatrix, handler, cam);
		//this.lambda = lambda;
		//this.T = T;
		k = 2*Math.PI/lambda;
		w = 2*Math.PI/T;
		b = getY();
		this.bounds = new Ellipse2D.Double(x, y, this.getWidth(), this.getHeight());
		maxSpeedX = MAX_SPEED_X;
		maxSpeedY = MAX_AMPLITUDE;
		
		super.createVector();
	}

	/**
	 * Mise à jour des différents paramètres de la balle.
	 */
	public void update() {
		if(hitByBat) {
			super.calculateForce();
			super.applyGravity();
			super.applyRotation();
		} else {
			//application de la trajectoire d'une onde
			//y(x,t) = Asin(kx + wt + phi) voir onde progressive chap.1.9a
			//k = 2*pi/lambda
			//w = 2*pi/T
			setX(getX() + getVelX()*Constants.DT);
			//setY(15*Math.sin(k*getX())+b); //test lambda
			//setY(15*Math.sin(w*timeElapsed)+b); //test T
			setY(getVelY()*Math.sin(k*getX() - w*timeElapsed) + b);
		}
		super.createVector();
		bounds = new Ellipse2D.Double(getX(), getY(), getWidth(), getHeight());
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
		g2d.drawImage(IMG, (int)boundsTransf.getBounds().getX(), (int)boundsTransf.getBounds().getY(), (int)boundsTransf.getBounds().getWidth(), (int)boundsTransf.getBounds().getHeight(), null);
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
	}
	
}
