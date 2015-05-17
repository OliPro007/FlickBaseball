package application;

import java.awt.geom.AffineTransform;

import drawable.gameobject.GameObject;
/**
 * Cette classe permet de suivre l'objet ciblé dans son déplacement.
 * 
 * @author Alexandre Hua
 * @since 23-02-2015
 * @version 04-05-2015
 */
public class Camera {
	private double x, y;
	private AffineTransform worldMatrix;
	private double posIX = 0;
	private double posIY = 0;
	private double endOfWorld = 0;
	
	/**
	 * <b>Constructeur</b>
	 * <p>Créer la caméra qui suit l'objet cible selon une position initiale.</p>
	 * @param x Position en x de la caméra.
	 * @param y Position en y de la caméra.
	 * @param posI Position initiale en x de l'objet cible par rapport au côté gauche de l'écran.
	 * @param endOfWorld la limite du monde en x.
	 * @param worldMatrix ({@link AffineTransform}) Matrice monde vers composant.
	 */
	public Camera(double x, double y, double posI, double endOfWorld, AffineTransform worldMatrix) {
		this.x = x;
		this.y = y;
		this.worldMatrix = worldMatrix;
		this.posIX = posI;
		this.posIY = posI;
		this.endOfWorld = -endOfWorld+GamePanel.WIDTH;
	}
	
	/**
	 * Recalculer la position de la caméra.
	 * @param target Objet de référence pour la caméra.
	 */
	public void tick(GameObject target) {
		x = posIX - target.getX()*worldMatrix.getScaleX();
		y = posIY - target.getY()*worldMatrix.getScaleY();
		if(x>0) x=0;
		if(x<endOfWorld) x=endOfWorld;
		if(y<0) y=0;
	}
	
	/**
	 * Retourner la position en x de la caméra.
	 * @return Position en x de la caméra.
	 */
	public double getX() {
		return x;
	}
	
	/**
	 * Retourner la position en y de la caméra.
	 * @return position en y de la caméra.
	 */
	public double getY() {
		return y;
	}
	
	/**
	 * Modifier la position en x de la caméra.
	 * @param x La nouvelle position en x de la caméra.
	 */
	public void setX(double x) {
		this.x = x;
	}
	
	/**
	 * Modifier la position en y de la caméra.
	 * @param y La nouvelle position en y de la caméra.
	 */
	public void setY(double y) {
		this.y = y;
	}
	
	/**
	 * Méthode qui permet de déplacer la caméra en x.
	 * @param dx la distance de déplacement en x
	 */
	public void moveX(double dx) {
		x += dx;
		if(x>0) x=0;
		if(x<endOfWorld) x=endOfWorld;
	}
	
	/**
	 * Méthode qui permet de déplacer la caméra en y.
	 * @param dy la distance de déplacement en y
	 */
	public void moveY(double dy) {
		y += dy;
		if(y<0) y=0;
	}
	
	/**
	 * Modifier la position initiale de la caméra.
	 * @param posIX La nouvelle position initiale en x de la caméra.
	 * @param posIY La nouvelle position initiale en y de la caméra.
	 */
	public void setPos(double posIX, double posIY) {
		this.posIX = posIX;
		this.posIY = posIY;
	}
	
	/**
	 * Retourner la position initiale de l'objet cible de la caméra.
	 * @return position initiale de la caméra.
	 */
	public double getPos() {
		return posIX;
	}
	
	/**
	 * Retourner la limite du monde en x.
	 * @return la limite du monde en x.
	 */
	public double getEndOfWorld() {
		return endOfWorld-GamePanel.WIDTH;
	}
}
