package drawable.gameobject;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
/**
 * Classe mod�le qui fournit aux classes d'h�riter des propri�t�s prot�g�es et des m�thodes de ces objets.
 * 
 * @author Alexandre Hua
 * @version 04-05-2015
 */
public abstract class GameObject {
	private double x;
	private double y;
	private double velX;
	private double velY;
	private double height;
	private double width;
	private double mass;
	private AffineTransform worldMatrix;
	
	/**
	 * <b>Constructeur</b>
	 * <p>Cr�er un objet de jeu.<p>
	 * @param x Position en x de l'objet.
	 * @param y Position en y de l'objet.
	 * @param width la largeur de l'objet
	 * @param height la hauteur de l'objet
	 * @param mass la masse de l'objet
	 * @param worldMatrix Matrice monde vers composant.
	 */
	public GameObject(double x, double y, double width, double height, double mass, AffineTransform worldMatrix) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.mass = mass;
		this.worldMatrix = worldMatrix;
	}
	
	/**
	 * Mise � jour des diff�rentes propri�t�s de l'objet.
	 */
	public abstract void update();
	
	/**
	 * Dessiner l'objet.
	 * @param g2d ({@link Graphics2D}) Le contexte graphique 2D sur lequel dessiner l'objet.
	 */
	public abstract void render(Graphics2D g2d);
	
	/**
	 * retourner la zone de collision de l'objet
	 * @return la forme de collision de l'objet
	 */
	public abstract Area getBounds();
	
	/**
	 * Retourner la hauteur de l'objet en taille r�elle.
	 * @return La hauteur de l'objet.
	 */
	public double getHeight() {
		return height;
	}
	
	/**
	 * Modifier la hauteur de l'objet en taille r�elle.
	 * @param height La nouvelle hauteur de l'objet.
	 */
	public void setHeight(double height){
		this.height = height;
	}
	
	/**
	 * Retourner la longueur de l'objet en taille r�elle.
	 * @return La longueur de l'objet.
	 */
	public double getWidth() {
		return width;
	}
	
	/**
	 * Modifier la longueur de l'objet en taille r�elle.
	 * @param width La nouvelle longueur de l'objet.
	 */
	public void setWidth(double width){
		this.width = width;
	}
	
	/**
	 * Retourner la position en x de l'objet en taille r�elle.
	 * @return La position en x de l'objet.
	 */
	public double getX() {
		return x;
	}
	
	/**
	 * Modifier la position en x de l'objet en taille r�elle.
	 * @param x La nouvelle position en x de l'objet.
	 */
	public void setX(double x) {
		this.x = x;
	}

	/**
	 * Retourner la position en y de l'objet en taille r�elle.
	 * @return la position en y de l'objet
	 */
	public double getY() {
		return y;
	}
	
	/**
	 * Modifier la position en y de l'objet en taille r�elle.
	 * @param y La nouvelle position en y de l'objet.
	 */
	public void setY(double y) {
		this.y = y;
	}
	
	/**
	 * Retourner la v�locit� en x de l'objet.
	 * @return La v�locit� en x de l'objet.
	 */
	public double getVelX() {
		return velX;
	}

	/**
	 * Retourner la v�locit� en y de l'objet.
	 * @return La v�locit� en y de l'objet.
	 */
	public double getVelY() {
		return velY;
	}
	
	/**
	 * Modifier la v�locit� en x de l'objet.
	 * @param velX La nouvelle v�locit� en x de l'objet.
	 */
	public void setVelX(double velX) {
		this.velX = velX;
	}
	
	/**
	 * Modifier la v�locit� en x de l'objet.
	 * @param velY La nouvelle v�locit� en x de l'objet.
	 */
	public void setVelY(double velY) {
		this.velY = velY;
	}
	
	/**
	 * Retourner la masse de l'objet.
	 * @return La masse de l'objet.
	 */
	public double getMass() {
		return mass;
	}
	
	/**
	 * Modifier la masse de l'objet.
	 * @param mass La masse de l'objet.
	 */
	public void setMass(double mass) {
		this.mass = mass;
	}
	
	/**
	 * Retourner la matrice monde vers composant de l'objet.
	 * @return la matrice monde vers composant
	 */
	public AffineTransform getWorldMatrix(){
		return this.worldMatrix;
	}
	
}
