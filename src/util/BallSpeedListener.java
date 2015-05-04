package util;

import java.util.EventListener;

import drawable.gameobject.ThrowController;

/**
 * Interface �couteur qui r�git le comportement de l'application lorsque le controlleur de vitesse initiale de la balle est modifi�e.
 * @author Alexandre Hua
 * @version 04-05-2015
 */
public interface BallSpeedListener extends EventListener{
	/**
	 * Signaler que la vitesse en x de d�part de la balle est modifi�e.
	 * @param forceX l'intensit� de la vitesse initiale en x en fonction de la position du curseur du panneau {@link ThrowController}
	 */
	public abstract void setSpeedX(double forceX);
	/**
	 * Signaler que la vitesse en y de d�part de la balle est modifi�e.
	 * @param forceY l'intensit� de la vitesse initiale en y en fonction de la position du curseur du panneau {@link ThrowController}
	 */
	public abstract void setSpeedY(double forceY);
}
