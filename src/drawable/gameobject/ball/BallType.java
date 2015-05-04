package drawable.gameobject.ball;

import java.awt.image.BufferedImage;

/**
 * Classe qui distingue les types de balle.
 * @author Olivier St-Jean
 * @since 01-05-2015
 * @version 04-05-2015
 */
public enum BallType {

	INVALID_BALL(null, null),
	NORMAL_BALL(NormalBall.IMG, NormalBall.DISABLED_IMG),
	FAST_BALL(FastBall.IMG, FastBall.DISABLED_IMG),
	SINUS_BALL(SinusBall.IMG, SinusBall.DISABLED_IMG),
	FRICTION_BALL(FrictionBall.IMG, FrictionBall.DISABLED_IMG),
	ELECTRIC_BALL(ElectricBall.IMG, ElectricBall.DISABLED_IMG);
	
	private BufferedImage img;
	private BufferedImage disabledImg;
	private boolean enabled;
	
	/**
	 * Initialiser les différentes propriétés du type de balle.
	 * @param img texture de la balle.
	 * @param disabledImg texture de la balle désactivée.
	 */
	private BallType(BufferedImage img, BufferedImage disabledImg){
		this.img = img;
		this.disabledImg = disabledImg;
		this.enabled = true;
	}
	
	/**
	 * Retourner le texture de la balle.
	 * @return texture de la balle.
	 */
	public BufferedImage getImage(){
		return this.img;
	}
	
	/**
	 * Retourner le texture de la balle désactivée.
	 * @return texture de la balle désactivée.
	 */
	public BufferedImage getDisabledImage(){
		return this.disabledImg;
	}
	
	/**
	 * Retourner l'état boolean de l'activation du type de balle.
	 * @return boolean
	 */
	public boolean isEnabled(){
		return this.enabled;
	}
	
	/**
	 * Modifier l'état boolean de l'activation du type de balle.
	 * @param enabled boolean
	 */
	public void setEnabled(boolean enabled){
		this.enabled = enabled;
	}
	
}
