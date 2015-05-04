package util;

import java.util.EventListener;

/**
 * Écouteur permettant de gérer l'activation ou la désactivation de certains paramètres
 * de jeu, tels que la musique de fond, l'affichage des informations scientifiques, etc.
 * 
 * @author Olivier St-Jean
 * @since 20-04-2015
 * @version 04-05-2015
 */
public interface OptionsListener extends EventListener {

	/**
	 * Évènement qui permet de modifier la condition d'affichage des informations scientifiques.
	 * @param enabled La nouvelle condition d'affichage.
	 */
	public abstract void setInfoEnabled(boolean enabled);
	
	/**
	 * Évènement qui permet de modifier la condition d'exécution des effects sonores.
	 * @param enabled La nouvelle condition d'exécution.
	 */
	public abstract void setSFXEnabled(boolean enabled);
	
	/**
	 * Évènement qui permet de modifier la condition d'exécution de la musique de fond.
	 * @param enabled La nouvelle condition d'exécution.
	 */
	public abstract void setBGMEnabled(boolean enabled);
	
}
