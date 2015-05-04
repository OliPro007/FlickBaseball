package drawable.gameobject;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import util.BufferedImageLoader;
import application.GamePanel;

/**
 * Classe qui créer la platforme du jeu sous la forme de bloc carré
 * 
 * @author Alexandre Hua
 * @version 04-05-2015
 */
public class Platform extends GameObject{
	public static final double SIZE = GamePanel.REAL_SIZE/10;
	public static final double MASS = 1;
	private static BufferedImage texture = new BufferedImageLoader().loadImage("/game/Ground.png");
	private Rectangle2D.Double rect;
	private Shape rectTransf;

	/**
	 * <b>Constructeur</b>
	 * <p>Initialiser la position des blocs.</p>
	 * @param x La position en x du bloc.
	 * @param y La position en y du bloc.
	 * @param worldMatrix Matrice monde vers composant.
	 */
	public Platform(double x, double y, AffineTransform worldMatrix) {
		super(x, y, SIZE, SIZE, MASS, worldMatrix);
		this.rect = new Rectangle2D.Double(x, y, getWidth(), getHeight());
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
}
