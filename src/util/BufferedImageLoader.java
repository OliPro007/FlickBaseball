package util;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * Classe qui permet de lire les fichiers d'image.
 * 
 * @author Alexandre Hua
 * @version 04-05-2015
 */
public class BufferedImageLoader {
	private BufferedImage image;
	
	/**
	 * Lire l'image qui est passé en paramètre
	 * @param path le nom de l'image qui est gardé en mémoire dans le dossier ressources
	 * @return l'image
	 */
	public BufferedImage loadImage(String path) {
		try {
			image = ImageIO.read(getClass().getResource(path));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return image;
	}
}
