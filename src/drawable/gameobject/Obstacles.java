package drawable.gameobject;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import application.GamePanel;

/**
 * Classe qui créer les obstacles de jeu
 * 
 * @author Alexandre Hua
 * @version 04-05-2015
 */
public class Obstacles extends GameObject{
	public static final double SIZE = GamePanel.REAL_SIZE/10;
	private Area shape;
	//private Shape shape;
	private Shape shapeTransf;
	private BufferedImage texture;
	
	/**
	 * <b>Constructeur</b>
	 * <p>Initialiser la position des blocs.</p>
	 * @param mass la masse du bloc.
	 * @param texture La texture de la plateforme.
	 * @param worldMatrix Matrice monde vers composant.
	 * @param shapes la structure de donnée qui contient les objets Shape
	 */
	public Obstacles(double mass, BufferedImage texture, AffineTransform worldMatrix, ArrayList<Shape> shapes) {
		super(0, 0, 0, 0, mass, worldMatrix);
		boolean first = true;
		for(int i = 0; i < shapes.size(); i++) {
			if(first) {
				shape = new Area(shapes.get(i));
				first = false;
			} else {
				Area areaTemp = new Area(shapes.get(i));
				shape.add(areaTemp);
			}
		}
		
		setX(shape.getBounds2D().getX());
		setY(shape.getBounds2D().getY());
		setWidth(shape.getBounds2D().getWidth());
		setHeight(shape.getBounds2D().getHeight());
		this.texture = texture;
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
		g2d.setColor(Color.BLUE);
		shapeTransf = getWorldMatrix().createTransformedShape(shape);
		Rectangle2D transfBounds = shapeTransf.getBounds2D();
		//Rectangle2D transfBounds = shapeTransf.getBounds2D();
		//g2d.drawImage(texture, (int)transfBounds.getX(), (int)transfBounds.getY(), (int)transfBounds.getWidth(), (int)transfBounds.getHeight(), null);
		//g2d.setColor(Color.BLACK);
		g2d.draw(shapeTransf);
		g2d.drawImage(texture, (int)transfBounds.getX(), (int)transfBounds.getY(), (int)transfBounds.getWidth(), (int)transfBounds.getHeight(), null);
	}

	/**
	 * Retourner la zone de collision du bloc.
	 * @return L'aire de la zone de collision du bloc.
	 */
	public Area getBounds() {
		return new Area(shapeTransf);
	}
}
