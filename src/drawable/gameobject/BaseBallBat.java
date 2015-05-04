package drawable.gameobject;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;

/**
 * Classe qui crée la batte de baseball pour frapper la balle.
 * @author Alexandre Hua
 * @version 04-05-2015
 */
public class BaseBallBat extends GameObject{
	private static final double DIAMETER = 5;
	private Ellipse2D.Double bounds;
	private Shape boundsTransf;
	//private double mass;
	
	/**
	 * <b>Constructeur</b>
	 * <p>Initialiser les différentes propriétés de la batte.<p>
	 * @param x La position en x du centre de la batte.
	 * @param y La position en y du centre de la batte.
	 * @param mass la masse de la batte.
	 * @param worldMatrix Matrice monde vers composant.
	 */
	public BaseBallBat(double x, double y, double mass, AffineTransform worldMatrix) {
		super(x, y, DIAMETER, DIAMETER, mass, worldMatrix); //pourquoi on a besoin que le DIAMETER soit static pour fonctionner?
		setVelX(x - getX());
		setVelY(y - getY());
		//this.mass = 1.0; //kg
	}
	
	/**
	 * Mise à jour des différents paramètres de la batte.
	 */
	public void update() {
		this.bounds = new Ellipse2D.Double(getX()-getWidth()/2, getY()-getHeight()/2, getWidth(), getHeight());
		this.boundsTransf = this.getWorldMatrix().createTransformedShape(this.bounds);
	}
	
	/**
	 * Dessiner la batte.
	 * @param g2d contexte graphique 2D
	 */
	public void render(Graphics2D g2d) {		
		g2d.setColor(Color.YELLOW);
		if(boundsTransf != null)
		g2d.fill(this.boundsTransf);
	}
	
	/**
	 * Retourner la zone de collision de la batte.
	 * @return L'aire de la forme en monde vers composant.
	 */
	public Area getBounds() {
		return new Area(this.boundsTransf);
	}
	
	/**
	 * Modifier la position en x du centre de la batte et calculer la vitesse selon le déplacement.
	 * @param x La nouvelle position en x de la batte.
	 */
	@Override
	public void setX(double x) {
		setVelX((x - getX()) * 10);
		super.setX(x);
	}
	
	/**
	 * Modifier la position en y du centre de la batte et calculer la vitesse selon le déplacement.
	 * @param y La nouvelle position en y de la batte.
	 */
	@Override
	public void setY(double y) {
		setVelY((y - getY()) * 10);
		super.setY(y);
	}
	
}
