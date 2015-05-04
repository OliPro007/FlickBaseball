package util;

import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.EventListener;

import application.Camera;
import drawable.gameobject.Handler;

/**
 * Interface écouteur qui régit le comportement des états lorsqu'ils sont initialisés.
 * 
 * @author Olivier St-Jean
 * @version 04-05-2015
 */
public interface GameStateListener extends EventListener{
	
	/**
	 * Méthode qui doit être exécutée lorsqu'on veut changer d'état principal.
	 * @param thisState L'état actuel.
	 * @param nextState Le prochain état.
	 */
	public abstract void stateChangeRequested(int thisState, int nextState);
	
	/**
	 * Méthode qui doit être exécutée lorsqu'on veut changer vers un mode de jeu.
	 * @param thisState L'état actuel.
	 * @param nextState Le prochain état.
	 * @param saveData Les données sauvegardées. Peut être null s'il n'y a pas de sauvegarde.
	 */
	public abstract void stateChangeRequested(int thisState, int nextState, ArrayList<Object[]> saveData);
	
	/**
	 * Méthode qui doit être exécutée lorsqu'on veut retourner dans l'état précédent.
	 */
	public abstract void stateReturnRequested();
	
	/**
	 * Méthode qui doit être exécutée lorsque l'état (principal ou secondaire) termine son initialisation.
	 */
	public abstract void stateChanged();
	
	/**
	 * Méthode qui doit être exécutée lorsqu'on veut réinitialiser l'état actuel.
	 */
	public abstract void resetRequested();	
	
	/**
	 * Méthode qui doit être exécutée lorsque la balle entre en contact avec la batte
	 * @param worldMatrix Matrice monde vers composant.
	 * @param handler Gestionnaire des objets de jeu.
	 * @param cam la caméra fixée à la balle
	 */
	public abstract void hitBat(AffineTransform worldMatrix, Handler handler, Camera cam);
	
	/**
	 * Méthode qui doit être exécutée lorsqu'on veut désactiver le mouseListener
	 */
	public abstract void disableBat();
	
	/**
	 * Méthode qui doit être exécutée lorsqu'on veut déplacer la caméra
	 * @param cam la caméra
	 */
	public abstract void moveCamera(Camera cam);
	
	/**
	 * Méthode qui doit être exécutée lorsqu'on veut désactiver le keyListener
	 */
	public abstract void disableCamera();
	
	/**
	 * Méthode qui doit être exécutée lorsqu'on veut réinitialiser le jeu après que la balle ait touché le sol.
	 */
	public abstract void resetBall();
	
	/**
	 * Méthode qui doit être exécutée lorsqu'on veut désactiver le keyListener de la balle
	 */
	public abstract void disableResetBall();

}
