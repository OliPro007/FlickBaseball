/**
 * 
 */
package drawable.gameobject.ball;

import static util.Constants.DT;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;

import util.BufferedImageLoader;
import application.Camera;
import drawable.gameobject.Handler;

/**
 * @author Olivier St-Jean
 * @version 04-05-2015
 */
public class TrollfaceBall extends AbstractBall {
	private static BufferedImage NO_PROBLEM = new BufferedImageLoader().loadImage("/game/trollface_no_problem.png");
	private static BufferedImage PROBLEM = new BufferedImageLoader().loadImage("/game/trollface.png");

	private int trollCounter = 0;
	private BufferedImage currImg;
	
	/**
	 * @param x la position initiale en x de la balle
	 * @param y la position initiale en y de la balle
	 * @param diameter la diamètre en x et en y de la balle
	 * @param worldMatrix Matrice monde vers composant
	 * @param handler Gestionnaire des objets de jeu.
	 * @param cam la caméra fixée à la balle
	 */
	public TrollfaceBall(double x, double y, double diameter, AffineTransform worldMatrix, Handler handler, Camera cam) {
		super(x, y, diameter, Double.MAX_VALUE,worldMatrix, handler, cam);
		this.bounds = new Ellipse2D.Double(x, y, this.getWidth(), this.getHeight());
		currImg = NO_PROBLEM;
	}

	/* (non-Javadoc)
	 * @see gameobject.AbstractBall#update()
	 */
	@Override
	public void update() {
		if(getX() >= 30){
			setX(getX() - 1);
		}else{
			if(trollCounter < 50){
				trollCounter++;
			}else{
				currImg = PROBLEM;
				timeElapsed += 4*DT;
			}
		}
		
		bounds = new Ellipse2D.Double(getX(), getY(), getWidth(), getHeight());
		super.update();
	}

	/* (non-Javadoc)
	 * @see gameobject.AbstractBall#render(java.awt.Graphics2D)
	 */
	@Override
	public void render(Graphics2D g2d) {
		boundsTransf = getWorldMatrix().createTransformedShape(bounds);
		g2d.drawImage(currImg, (int)boundsTransf.getBounds().getX(), (int)boundsTransf.getBounds().getY(), (int)boundsTransf.getBounds().getWidth(), (int)boundsTransf.getBounds().getHeight(), null);
	}
	
	/**
	 * Retourner l'image de texture de la balle.
	 * @return l'image de texture de la balle
	 */
	public BufferedImage getImage(){
		return currImg;
	}

}
