package util;

import java.util.EventListener;

/**
 * Interface �couteur qui r�git le comportement de l'�tat de jeu lorsque le jeu a besoin d'accorder des points
 * @author Alexandre Hua
 * @version 04-05-2015
 */
public interface ScoreListener extends EventListener{
	
	/**
	 * Signaler que le jeu doit accorder des points.
	 * @param score le score qui repr�sente la distance parcourue par la balle
	 */
	public abstract void getScore(int score);
}
