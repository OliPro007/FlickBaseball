package drawable.gameobject;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.LinkedList;

import util.BufferedImageLoader;

/**
 * Classe qui détient tous les objets de jeu
 * 
 * @author Alexandre Hua
 * @since 9-02-2015
 * @version 04-05-2015
 */
public class Handler {

	private LinkedList<GameObject> objects;
	private GameObject tempObject;
	private AffineTransform worldMatrix;
	private int lastPlatformIndex;

	/**
	 * <b>Contructeur</b>
	 * <p>Créer un structure de donnée où seront gardés en mémoire les objets de jeu</p>
	 * @param worldMatrix Matrice monde vers composant.
	 */
	public Handler(AffineTransform worldMatrix) {
		objects = new LinkedList<GameObject>();
		this.worldMatrix = worldMatrix;
	}

	/*private void createPlatforms(){
		//BufferedImageLoader loader = new BufferedImageLoader();
		platform = new BufferedImage(GamePanel.WIDTH*10, GamePanel.HEIGHT, BufferedImage.TYPE_INT_RGB);
		for(GameObject o : objects){
			if(o instanceof Platform){

			}
		}
		//this.worldMatrix = worldMatrix;
	}*/

	/**
	 * Charger les images en mémoire et créer les platformes.
	 * @see Handler#createPlatforms(BufferedImage)
	 * @see BufferedImageLoader
	 */
	public void loadImage(){
		BufferedImageLoader loader = new BufferedImageLoader();
		BufferedImage platform = loader.loadImage("/game/platform.png");
		createPlatforms(platform);
	}

	/**
	 * Retourner la structure de donnée qui détient les objets de jeu.
	 * @return ({@link LinkedList}) La structure de donnée qui détient les objets de jeu.
	 */
	public LinkedList<GameObject> getObjects() {
		return objects;
	}

	/**
	 * Mise à jour des propriétés de tous les objets de jeu.
	 */
	public void update() {
		//Les plateformes sont toujours créées en premier, mais leur méthode update
		//ne fait rien. Ça sert donc à rien de l'appeler et boucler toutes les plateformes.
		for(int i = lastPlatformIndex; i < objects.size(); i++) {
			try{
				tempObject = objects.get(i);
				tempObject.update();
			}catch(NullPointerException e){
				//System.err.println("Exception dans Handler#update(): " + e.getLocalizedMessage() + " pour l'index " + i);
				//System.err.println("Cette exception se produit lorsque la batte de baseball est effacée par le mouseListener pendant la boucle.");
			}
		}
	}

	/**
	 * Dessiner tous les objets de jeu.
	 * @param g2d ({@link Graphics2D}) Le contexte graphique sur lequel dessiner les objets de jeu.
	 */
	public void render(Graphics2D g2d) {
		for(int i = 0; i < objects.size(); i++) {
			tempObject = objects.get(i);
			tempObject.render(g2d);
		}
	}

	/**
	 * Ajouter un nouvel objet de jeu dans la structure de donnée.
	 * @param object Le nouvel objet de jeu à ajouter dans la structure de donnée.
	 */
	public void addObject(GameObject object) {
		this.objects.add(object);
	}

	/**
	 * Enlever un objet de jeu de la structure de donnée et l'effacer de la mémoire.
	 * @param object L'objet de jeu qui sera enlevé de la structure de donnée.
	 */
	public void removeObject(GameObject object) {
		this.objects.remove(object);
		object = null;
	}

	/**
	 * Créer les platformes selon un fichier image.
	 * @param pattern ({@link BufferedImage}) L'image contenant les pixels représentant les plateformes.
	 * @see Handler#addObject(GameObject)
	 * @see Platform#Platform(double, double, AffineTransform)
	 */
	private void createPlatforms(BufferedImage pattern) {
		//Cette méthode pour créer la platforme du jeu est tirée du tutorial de ce lien
		//https://www.youtube.com/watch?v=1TFDOT1HiBo&list=PLWms45O3n--54U-22GDqKMRGlXROOZtMx&index=11
		int w = pattern.getWidth();
		int h = pattern.getHeight();
		
		BufferedImageLoader loader = new BufferedImageLoader();
		//BufferedImage groundTexture = loader.loadImage("/game/ground.png");
		BufferedImage house1 = loader.loadImage("/game/House1.png");
		BufferedImage house2 = loader.loadImage("/game/House2.png");
		//ArrayList<Shape> ground = new ArrayList<Shape>();
		ArrayList<Shape> shapes1 = new ArrayList<Shape>();
		ArrayList<Shape> shapes2 = new ArrayList<Shape>();
		ArrayList<Shape> shapes3 = new ArrayList<Shape>();
		//shapes.add(new Rectangle2D.Double(2,2,2,2));

		for(int xx = 0; xx < w; xx++) {
			for(int yy = 0; yy < h; yy++) {
				int pixel = pattern.getRGB(xx, yy);
				int red = (pixel >> 16) & 0xff;
				int green = (pixel >> 8) & 0xff;
				int blue = (pixel) & 0xff;

				if(red == 0 && green == 255 && blue == 0) addObject(new Platform(xx*Platform.SIZE, yy*Platform.SIZE, worldMatrix));
				if(red == 0 && green == 0 && blue == 255) shapes1.add((new Area(new Rectangle2D.Double(xx*Platform.SIZE, yy*Platform.SIZE, Platform.SIZE, Platform.SIZE))));
				if(red == 255 && green == 0 && blue == 0) shapes2.add((new Area(new Rectangle2D.Double(xx*Platform.SIZE, yy*Platform.SIZE, Platform.SIZE, Platform.SIZE))));
				if(red == 0 && green == 255 && blue == 255) shapes3.add((new Area(new Rectangle2D.Double(xx*Platform.SIZE, yy*Platform.SIZE, Platform.SIZE, Platform.SIZE))));
				//if(red == 0 && green == 255 && blue == 0) ground.add((new Area(new Rectangle2D.Double(xx*Platform.SIZE, yy*Platform.SIZE, Platform.SIZE, Platform.SIZE))));
			}
		}
		
		//Obstacles groundO = new Obstacles(1000,groundTexture, worldMatrix, ground);
		Obstacles obstacle1 = new Obstacles(1000,house2, worldMatrix, shapes1);
		Obstacles obstacle2 = new Obstacles(1000,house1, worldMatrix, shapes2);
		Obstacles obstacle3 = new Obstacles(1000,house2, worldMatrix, shapes3);
		//addObject(groundO);
		addObject(obstacle1);
		addObject(obstacle2);
		addObject(obstacle3);
		
		//GameObject platTemp = new Platform(0, 0, new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB), new AffineTransform());
		GameObject platTemp = new Platform(0, 0, new AffineTransform());
		lastPlatformIndex = objects.lastIndexOf(platTemp);
		
		addObject(new ElectricPlatform(250+ElectricPlatform.WIDTH, 55, 9*Math.pow(10, -9), 0, worldMatrix));
		addObject(new ElectricPlatform(250, 55, -9*Math.pow(10, -9), 0, worldMatrix));
		
		addObject(new ElectricPlatform(700+2*ElectricPlatform.WIDTH, 55, 9*Math.pow(10, -9), 0, worldMatrix));
		addObject(new ElectricPlatform(700+ElectricPlatform.WIDTH, 55, 9*Math.pow(10, -9), 0, worldMatrix));
		addObject(new ElectricPlatform(700, 55, 9*Math.pow(10, -9), 0, worldMatrix));
		
		addObject(new ElectricPlatform(800+2*ElectricPlatform.WIDTH, 55, -9*Math.pow(10, -9), 0, worldMatrix));
		addObject(new ElectricPlatform(800+ElectricPlatform.WIDTH, 55, -9*Math.pow(10, -9), 0, worldMatrix));
		addObject(new ElectricPlatform(800, 55, -9*Math.pow(10, -9), 0, worldMatrix));
		
		//addObject(new ElectricPlatform(50+ElectricPlatform.WIDTH, 55, 9*Math.pow(10, -9), 0, worldMatrix));
		//addObject(new ElectricPlatform(20, 55, -9*Math.pow(10, -9), 0, worldMatrix));
	}

	/*public void createPlatforms(){
		for(int i=0; i<GamePanel.WIDTH*10/worldMatrix.getScaleX(); i+=Platform.SIZE){
			addObject(new Platform(i, GamePanel.HEIGHT/worldMatrix.getScaleY()-Platform.SIZE, worldMatrix));
		}
	}*/

	/*private void createCollisionBox(){
		for(int i=0; i<3; i++){
			addObject(new Platform(getObjects().indexOf(instanceof Platform)))
		}
	}*/
}
