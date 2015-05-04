package drawable.gameobject;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import util.BufferedImageLoader;
import util.Constants;
import application.GamePanel;
import drawable.util.Vector2D;

/**
 * Classe qui créer une platforme avec un champ électrique du jeu sous la forme de bloc rectangulaire
 * 
 * @author Alexandre Hua
 * @version 04-05-2015
 */
public class ElectricPlatform extends GameObject{
	public static final double WIDTH = GamePanel.REAL_SIZE/5 + 5;
	public static final double HEIGHT = 3; //3 mètres
	public static final double MASS = 1;
	private static BufferedImage texture_positive = new BufferedImageLoader().loadImage("/game/Metal_Positive.png");
	private static BufferedImage texture_negative = new BufferedImageLoader().loadImage("/game/Metal_Negative.png");
	private BufferedImage texture;
	private Rectangle2D.Double rect;
	private Shape rectTransf;

	private double angle; //angle du bloc par rapport l'horizontal en radian
	private double fieldOrientation = angle+Math.PI/2-Math.toRadians(1); //Math.cos() semble donner des faux résultats pour les angle 90 et 270
	private double electricField;
	private double electricFieldX;
	private double electricFieldY;
	private double density;

	private Vector2D vectElectricField;

	/**
	 * <b>Constructeur</b>
	 * <p>Initialiser la position des blocs.</p>
	 * @param x La position en x du bloc.
	 * @param y La position en y du bloc.
	 * @param density la densité de la plaque.
	 * @param angle l'angle de plaque par rapport à l'horizontal
	 * @param worldMatrix Matrice monde vers composant.
	 */
	public ElectricPlatform(double x, double y, double density, double angle, AffineTransform worldMatrix) {
		super(x, y, WIDTH, HEIGHT, MASS, worldMatrix);
		this.density = density;
		if(density >= 0) {
			texture = texture_positive;
		} else {
			texture = texture_negative;
		}
		this.angle = angle;
		electricField = density/(2*Constants.C_ELECTRIQUE);
		electricFieldX = electricField*(Math.cos(fieldOrientation));
		electricFieldY = electricField*(Math.sin(fieldOrientation));
		this.rect = new Rectangle2D.Double(x, y, getWidth(), getHeight());
		
		vectElectricField = new Vector2D(new Point2D.Double(getX()+getWidth()/2, getY()+getHeight()/2), electricFieldX, electricFieldY);
	}

	/**
	 * Mise à jour des propriétés du bloc.
	 */
	public void update() {
		
	}

	/**
	 * Dessiner le bloc
	 * @param g2d ({@link Graphics2D}) Le contexte graphique sur lequel dessiner la plateforme.
	 * @see AffineTransform#createTransformedShape(Shape)
	 */
	public void render(Graphics2D g2d) {
		//g2d.setColor(Color.GREEN);
		rectTransf = getWorldMatrix().createTransformedShape(rect);
		Rectangle2D transfBounds = rectTransf.getBounds2D();
		//g2d.fill(rectTransf);
		g2d.drawImage(texture, (int)transfBounds.getX(), (int)transfBounds.getY(), (int)transfBounds.getWidth(), (int)transfBounds.getHeight(), null);
		//g2d.setColor(Color.BLACK);
		//g2d.draw(rectTransf);
		if(density < 0) {
			g2d.setColor(new Color(0,255,255,50));
		} else {
			g2d.setColor(new Color(255,0,0,50));
		}
		
		int colHeight = (int)(20*GamePanel.HEIGHT*getWorldMatrix().getScaleY());
		g2d.fillRect((int)(getX()*getWorldMatrix().getScaleX()), GamePanel.HEIGHT-colHeight, (int)(getWidth()*getWorldMatrix().getScaleX()), colHeight);
	}

	/**
	 * Retourner la zone de collision du bloc.
	 * @return L'aire de la zone de collision du bloc.
	 */
	public Area getBounds() {
		return new Area(rectTransf);
	}
	
	/**
	 * Méthode qui permet de vérifier si l'objet passé en paramètre est de la nature Platform
	 * @param rhs objet à comparer
	 * @return boolean 
	 */
	public boolean equals(Object rhs){
		return (this.getClass().getName().equals(rhs.getClass().getName()));
	}
	
	/**
	 * Retourner le vecteur du champ électrique de la plaque.
	 * @return le vecteur du champ électrique
	 */
	public Vector2D getVectElectricField() {
		return vectElectricField;
	}
}
