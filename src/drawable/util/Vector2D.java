package drawable.util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

/**
 * Classe permettant de créer un vecteur en deux dimension.
 * Elle se base sur la classe SVector3d du projet de raytracing
 * SIM créé par Simon Vézina.
 * 
 * @author Simon Vézina
 * @author Olivier St-Jean
 * @since 2-03-2015
 * @version 04-05-2015
 */
public class Vector2D {
	
    private Point2D.Double position;
	private double x, y;
	private double orientation;
	private double modulus;
	
	private final double ARROW_SEGMENT_ROTATION = 0.3;
	private double arrowLength = 20;

    /**
     * <b>Constructeur</b>
     * Créer un vecteur unitaire à la position donnée.
     * @param position La position de départ du vecteur.
     * @see java.awt.geom.Point2D.Double
     */
    public Vector2D(Point2D.Double position) {
        this.position = position;
        x = 1;
        y = 1;

    	orientation = Math.atan2(-y, x);
    	modulus = Math.sqrt(this.x*this.x + this.y*this.y);
    }
    
    /**
     * <b>Constructeur</b>
     * Créer un vecteur de composantes x et y à la postion donnée.
     * @param position La position de départ du vecteur.
     * @param x La composante en x du vecteur.
     * @param y La composante en y du vecteur.
     * @see java.awt.geom.Point2D.Double
     */
    public Vector2D(Point2D.Double position, double x, double y){
    	this.position = position;
    	this.x = x;
    	this.y = y;

    	orientation = Math.atan2(-y, x);
    	modulus = Math.sqrt(this.x*this.x + this.y*this.y);
    }
    
    /**
     * <b>Constructeur de copie</b>
     * @param v Le vecteur à copier.
     */
    public Vector2D(Vector2D v) {
        this.position = v.position;
        this.x = v.x;
        this.y = v.y;

    	orientation = Math.atan2(y, x);
    	modulus = Math.sqrt(this.x*this.x + this.y*this.y);

    }
    
    /**
     * Dessiner le vecteur de force
     * @param g2d Contexte graphique 2D sur lequel dessiner.
     * @param vectColor la couleur du vecteur.
     * @param worldMatrix matrice monde vers composant.
     */
    public void draw(Graphics2D g2d, Color vectColor, AffineTransform worldMatrix){
    	AffineTransform localTransform = g2d.getTransform();
    	
    	Color colorTemp = g2d.getColor();
    	
    	g2d.setColor(vectColor);
    	double origX = position.getX()*worldMatrix.getScaleX();
    	double origY = position.getY()*worldMatrix.getScaleY();
    	double lineX = origX + modulus*worldMatrix.getScaleX();
    	double lineY = origY;
    	double arrowX = lineX - arrowLength;
    	double arrowY = origY;
    	
    	Line2D.Double vectorLineTransf = new Line2D.Double(origX, origY, lineX, lineY);
    	Line2D.Double vectorHalfArrowTransf = new Line2D.Double(lineX, lineY, arrowX, arrowY);

		g2d.rotate(orientation, origX, origY);
		g2d.draw(vectorLineTransf);
		
		g2d.rotate(ARROW_SEGMENT_ROTATION, lineX, lineY);
		g2d.draw(vectorHalfArrowTransf);
		g2d.rotate(-(ARROW_SEGMENT_ROTATION*2), lineX, lineY);
		g2d.draw(vectorHalfArrowTransf);
	
		g2d.setTransform(localTransform);
		
		g2d.setColor(colorTemp);
    }
    
    /**
     * Permet d'obtenir le module du vecteur.
     * @return Le module du vecteur.
     */
    public double modulus(){
    	return modulus;
    }
    
    /**
     * Permet d'obtenir la version unitaire de ce vecteur (module = 1).
     * Si ce vecteur est nul, le vecteur retourné sera nul.
     * @return Le vecteur normalisé.
     */
    public Vector2D normalize(){
    	//double modulus = this.modulus();
    	if(modulus > 0.0)
    		return new Vector2D(this.position, this.x/modulus, this.y/modulus);
    	else
    		return this;
    }
    
    /**
     * Permet d'additionner un autre vecteur à la position de ce vecteur.
     * @param v Le vecteur à additionner.
     * @return Le vecteur résultant.
     */
    public Vector2D add(Vector2D v){
    	return new Vector2D(this.position, this.x + v.x, this.y + v.y);
    }
    
    /**
     * Permet de soustraire un autre vecteur à la position de ce vecteur.
     * @param v Le vecteur à soustraire.
     * @return Le vecteur résultant.
     */
    public Vector2D substract(Vector2D v){
    	return new Vector2D(this.position, this.x - v.x, this.y - v.y);
    }
    
    /**
     * Permet de multiplier ce vecteur par un scalaire.
     * @param scalar Le scalaire utilisé pour la multiplication.
     * @return Le vecteur résultant.
     */
    public Vector2D multiply(double scalar){
    	Vector2D vector = new Vector2D(this.position, this.x * scalar, this.y * scalar);
    	this.x = this.x * scalar;
    	this.y = this.y * scalar;
    	modulus = Math.sqrt(this.x*this.x + this.y*this.y);
    	return vector;
    }
    
    /**
     * Permet d'obtenir le produit scalaire de ce vecteur avec un autre vecteur.
     * @param v L'autre vecteur.
     * @return La valeur du produit scalaire.
     */
    public double dot(Vector2D v){
    	return (this.x*v.x + this.y*v.y);
    }

    /**
     * Permet d'obtenir la position du vecteur.
     * @return La position du vecteur.
     * @see java.awt.geom.Point2D.Double
     */
    public Point2D.Double getPosition() {
		return position;
	}

    /**
     * Permet de modifier la position du vecteur.
     * @param position La nouvelle position du vecteur.
     * @see java.awt.geom.Point2D.Double
     */
	public void setPosition(Point2D.Double position) {
		this.position = position;
	}

	/**
	 * Permet d'obtenir la composante x du vecteur.
	 * @return La composante x du vecteur.
	 */
	public double getX() {
		return x;
	}

	/**
	 * Permet de modifier la composante x du vecteur.
	 * @param x La nouvelle composante x du vecteur.
	 */
	public void setX(double x) {
		this.x = x;
	}

	/**
	 * Permet d'obtenir la composante y du vecteur.
	 * @return La composante y du vecteur.
	 */
	public double getY() {
		return y;
	}

	/**
	 * Permet de modifier la composante y du vecteur.
	 * @param y La nouvelle composante y du vecteur.
	 */
	public void setY(double y) {
		this.y = y;
	}

	/**
     * Retourner la position et les composantes du vecteur en chaîne de caractères.
     * @return Représentation en chaîne de caractères du vecteur: Position: [posX, posY] Composantes: (x, y).
     */
    public String toString() {
        return "Position: " + position.toString() + " Composantes: <" + this.x + ", " + this.y + ">";
    }
	
}