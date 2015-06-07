package util;

/**
 * Classe de d�clarations publiques.
 * 
 * @author Olivier St-Jean
 * @version 07-06-2015
 */
public final class Constants {

	/**
	 * Constante de gravit�.
	 */
	public static final double G = 9.8;
	
	/**
	 * Rythme de rafraichissement des frames.
	 */
	public static final double DT = 1.0/60.0;
	
	/**
	 * Coefficient de restitution du gazon.
	 */
	public static final double COR_GRASS = 0.42328;
	
	/**
	 * Coefficient de restitution de la batte.
	 */
	public static final double COR_BAT = 0.70;
	
	/**
	 * Coefficient de frottement cin�tique du gazon.
	 */
	public static final double KFC_GRASS = 0.35;
	
	/**
	 * Coefficient de coulomb.
	 */
	public static final double C_COULOMB = 9*Math.pow(10, 9);
	
	/**
	 * Coefficient de Faraday
	 */
	public static final double C_ELECTRIQUE = 8.85*Math.pow(10, -12);
	
}
