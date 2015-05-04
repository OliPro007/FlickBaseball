/**
 * 
 */
package drawable.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JCheckBox;

import util.BufferedImageLoader;

/**
 * Classe remplaçant le {@link JCheckBox} de Swing, pour le défi et
 * pour la facilité de personnalisation.
 * 
 * @author Olivier St-Jean
 * @version 04-05-2015
 */
public class Checkbox extends JCheckBox{

	private static final long serialVersionUID = 1L;
	
	private static final Font FONT = new Font("Tahoma", Font.PLAIN, 20);
	private static final String CHECKED_IMG_NAME = "checkbox_checked.png";
	private static final String UNCHECKED_IMG_NAME = "checkbox_unchecked.png";
	private final BufferedImage checkedImg;
	private final BufferedImage uncheckedImg;
	
	public Checkbox(String text, String imageLocation){
		super(text);
		this.setBorderPainted(false);
		this.setContentAreaFilled(false);
        this.setFocusPainted(false);
        this.setForeground(Color.BLACK);
        this.setFont(FONT);
        BufferedImageLoader bil = new BufferedImageLoader();
        this.checkedImg = bil.loadImage(imageLocation + CHECKED_IMG_NAME);
        this.uncheckedImg = bil.loadImage(imageLocation + UNCHECKED_IMG_NAME);
	}
	
	/**
	 * Méthode qui permet de dessiner le bouton.
	 * @param g Contexte graphique sur lequel dessiner.
	 */
	protected void paintComponent(Graphics g){
		if(isSelected())
			setIcon(new ImageIcon(checkedImg));
		else
			setIcon(new ImageIcon(uncheckedImg));
		
		super.paintComponent(g);
	}

}
