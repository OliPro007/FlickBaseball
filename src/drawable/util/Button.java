/**
 * 
 */
package drawable.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JButton;

import util.BufferedImageLoader;

/**
 * Classe remplaçant le {@link JButton} de Swing, pour le défi et
 * pour la facilité de personnalisation.
 * 
 * @author Olivier St-Jean
 * @version 04-05-2015
 */
public class Button extends JButton{
	
	private static final long serialVersionUID = 1L;
	
	private static final String UNSELECTED_IMG_NAME = "button_unselected.png";
	private static final String SELECTED_IMG_NAME = "button_selected.png";
	private static final String CLICKED_IMG_NAME = "button_clicked.png";
	private final BufferedImage unselectedImg;
	private final BufferedImage selectedImg;
	private final BufferedImage clickedImg;
	private static final Font FONT = new Font("Tahoma", Font.PLAIN, 20);
	
	/**
	 * <b>Constructeur</b>
	 * <p>Permet de créer un bouton.</p>
	 * @param text Le texte du bouton.
	 * @param imageLocation L'emplacement du fichier image à lier au bouton.
	 */
	public Button(String text, String imageLocation) {
		super(text);
		this.setBorderPainted(false);
		this.setContentAreaFilled(false);
        this.setFocusPainted(false);
        this.setForeground(Color.BLACK);
        this.setFont(FONT);
        BufferedImageLoader bil = new BufferedImageLoader();
        this.unselectedImg = bil.loadImage(imageLocation + UNSELECTED_IMG_NAME);
        this.selectedImg = bil.loadImage(imageLocation + SELECTED_IMG_NAME);
        this.clickedImg = bil.loadImage(imageLocation + CLICKED_IMG_NAME);
	}
	
	/**
	 * Méthode qui permet de dessiner le bouton.
	 * @param g Contexte graphique sur lequel dessiner.
	 */
	protected void paintComponent(Graphics g){
		Graphics2D g2d = (Graphics2D)g;
		
		if(getModel().isPressed())
			g2d.drawImage(clickedImg, 0, 0, this.getWidth(), this.getHeight(), null);
		else if(getModel().isRollover())
			g2d.drawImage(selectedImg, 0, 0, this.getWidth(), this.getHeight(), null);
		else
			g2d.drawImage(unselectedImg, 0, 0, this.getWidth(), this.getHeight(), null);
		
		super.paintComponent(g);
	}
	
}
