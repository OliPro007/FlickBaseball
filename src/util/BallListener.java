/**
 * 
 */
package util;

import java.util.EventListener;

/**
 * Interface écouteur qui régit le comportement de l'application lorsque la balle a besoin d'être réinitialisée.
 * 
 * @author Olivier St-Jean
 * @version 04-05-2015
 */
public interface BallListener extends EventListener{
	
	/**
	 * Signaler que la balle doit être réinitialisée.
	 */
	public abstract void resetBall();
	
	/**
	 * Signaler que la balle a touché le sol pour la première fois.
	 */
	public abstract void landed();
}
