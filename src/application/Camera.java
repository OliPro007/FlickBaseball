package application;

import java.awt.geom.AffineTransform;

import drawable.gameobject.GameObject;
/**
 * Cette classe permet de suivre l'objet cibl� dans son d�placement.
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
	 * <p>Cr�er la cam�ra qui suit l'objet cible selon une position initiale.</p>
	 * @param x Position en x de la cam�ra.
	 * @param y Position en y de la cam�ra.
	 * @param posI Position initiale en x de l'objet cible par rapport au c�t� gauche de l'�cran.
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
	 * Recalculer la position de la cam�ra.
	 * @param target Objet de r�f�rence pour la cam�ra.
	 */
	public void tick(GameObject target) {
		x = posIX - target.getX()*worldMatrix.getScaleX();
		y = posIY - target.getY()*worldMatrix.getScaleY();
		if(x>0) x=0;
		if(x<endOfWorld) x=endOfWorld;
		if(y<0) y=0;
	}
	
	/**
	 * Retourner la position en x de la cam�ra.
	 * @return Position en x de la cam�ra.
	 */
	public double getX() {
		return x;
	}
	
	/**
	 * Retourner la position en y de la cam�ra.
	 * @return position en y de la cam�ra.
	 */
	public double getY() {
		return y;
	}
	
	/**
	 * Modifier la position en x de la cam�ra.
	 * @param x La nouvelle position en x de la cam�ra.
	 */
	public void setX(double x) {
		this.x = x;
	}
	
	/**
	 * Modifier la position en y de la cam�ra.
	 * @param y La nouvelle position en y de la cam�ra.
	 */
	public void setY(double y) {
		this.y = y;
	}
	
	/**
	 * M�thode qui permet de d�placer la cam�ra en x.
	 * @param dx la distance de d�placement en x
	 */
	public void moveX(double dx) {
		x += dx;
		if(x>0) x=0;
		if(x<endOfWorld) x=endOfWorld;
	}
	
	/**
	 * M�thode qui permet de d�placer la cam�ra en y.
	 * @param dy la distance de d�placement en y
	 */
	public void moveY(double dy) {
		y += dy;
		if(y<0) y=0;
	}
	
	/**
	 * Modifier la position initiale de la cam�ra.
	 * @param posIX La nouvelle position initiale en x de la cam�ra.
	 * @param posIY La nouvelle position initiale en y de la cam�ra.
	 */
	public void setPos(double posIX, double posIY) {
		this.posIX = posIX;
		this.posIY = posIY;
	}
	
	/**
	 * Retourner la position initiale de l'objet cible de la cam�ra.
	 * @return position initiale de la cam�ra.
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
