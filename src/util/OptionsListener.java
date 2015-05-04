package util;

import java.util.EventListener;

/**
 * �couteur permettant de g�rer l'activation ou la d�sactivation de certains param�tres
 * de jeu, tels que la musique de fond, l'affichage des informations scientifiques, etc.
 * 
 * @author Olivier St-Jean
 * @since 20-04-2015
 * @version 04-05-2015
 */
public interface OptionsListener extends EventListener {

	/**
	 * �v�nement qui permet de modifier la condition d'affichage des informations scientifiques.
	 * @param enabled La nouvelle condition d'affichage.
	 */
	public abstract void setInfoEnabled(boolean enabled);
	
	/**
	 * �v�nement qui permet de modifier la condition d'ex�cution des effects sonores.
	 * @param enabled La nouvelle condition d'ex�cution.
	 */
	public abstract void setSFXEnabled(boolean enabled);
	
	/**
	 * �v�nement qui permet de modifier la condition d'ex�cution de la musique de fond.
	 * @param enabled La nouvelle condition d'ex�cution.
	 */
	public abstract void setBGMEnabled(boolean enabled);
	
}
