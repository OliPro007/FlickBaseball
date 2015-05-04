package drawable.gameobject.ball;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;

import util.BufferedImageLoader;
import application.Camera;
import drawable.gameobject.Handler;

/**
 * Classe qui crée la balle.
 * 
 * @author Alexandre Hua
 * @version 04-05-2015
 */
public class NormalBall extends AbstractBall{
	public static final BufferedImage IMG = new BufferedImageLoader().loadImage("/game/ball.png");
	public static final BufferedImage DISABLED_IMG = new BufferedImageLoader().loadImage("/game/ball_disabled.png");
	public static final int MAX_SPEED_X = 50;
	public static final int MAX_SPEED_Y = 30;
	
	/**
	 * <b>Constructeur</b>
	 * <p>Créer la balle.</p>
	 * @param x Position en x de la balle.
	 * @param y Position en y de la balle.
	 * @param diameter Diamètre de la balle.
	 * @param mass la masse de la balle en grammes.
	 * @param worldMatrix Matrice monde vers composant.
	 * @param handler Gestionnaire des objets de jeu.
	 * @param cam la caméra fixée à la balle
	 */
	public NormalBall(double x, double y, double diameter, double mass, AffineTransform worldMatrix, Handler handler, Camera cam) {
		super(x, y, diameter, mass, worldMatrix, handler, cam);
		this.bounds = new Ellipse2D.Double(x, y, this.getWidth(), this.getHeight());
		maxSpeedX = MAX_SPEED_X;
		maxSpeedY = MAX_SPEED_Y;
		
		super.createVector();
	}

	/**
	 * Mise à jour des différents paramètres de la balle.
	 */
	public void update() {
		super.calculateForce();		
		super.applyGravity();
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
