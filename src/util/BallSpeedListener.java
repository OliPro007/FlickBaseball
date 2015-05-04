package util;

import java.util.EventListener;

import drawable.gameobject.ThrowController;

/**
 * Interface écouteur qui régit le comportement de l'application lorsque le controlleur de vitesse initiale de la balle est modifiée.
 * @author Alexandre Hua
 * @version 04-05-2015
 */
public interface BallSpeedListener extends EventListener{
	/**
	 * Signaler que la vitesse en x de départ de la balle est modifiée.
	 * @param forceX l'intensité de la vitesse initiale en x en fonction de la position du curseur du panneau {@link ThrowController}
	 */
	public abstract void setSpeedX(double forceX);
	/**
	 * Signaler que la vitesse en y de départ de la balle est modifiée.
	 * @param forceY l'intensité de la vitesse initiale en y en fonction de la position du curseur du panneau {@link ThrowController}
	 */
	public abstract void setSpeedY(double forceY);
}
