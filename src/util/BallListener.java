/**
 * 
 */
package util;

import java.util.EventListener;

/**
 * Interface �couteur qui r�git le comportement de l'application lorsque la balle a besoin d'�tre r�initialis�e.
 * 
 * @author Olivier St-Jean
 * @version 04-05-2015
 */
public interface BallListener extends EventListener{
	
	/**
	 * Signaler que la balle doit �tre r�initialis�e.
	 */
	public abstract void resetBall();
	
	/**
	 * Signaler que la balle a touch� le sol pour la premi�re fois.
	 */
	public abstract void landed();
}
