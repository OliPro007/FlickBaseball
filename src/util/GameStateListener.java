package util;

import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.EventListener;

import application.Camera;
import drawable.gameobject.Handler;

/**
 * Interface �couteur qui r�git le comportement des �tats lorsqu'ils sont initialis�s.
 * 
 * @author Olivier St-Jean
 * @version 04-05-2015
 */
public interface GameStateListener extends EventListener{
	
	/**
	 * M�thode qui doit �tre ex�cut�e lorsqu'on veut changer d'�tat principal.
	 * @param thisState L'�tat actuel.
	 * @param nextState Le prochain �tat.
	 */
	public abstract void stateChangeRequested(int thisState, int nextState);
	
	/**
	 * M�thode qui doit �tre ex�cut�e lorsqu'on veut changer vers un mode de jeu.
	 * @param thisState L'�tat actuel.
	 * @param nextState Le prochain �tat.
	 * @param saveData Les donn�es sauvegard�es. Peut �tre null s'il n'y a pas de sauvegarde.
	 */
	public abstract void stateChangeRequested(int thisState, int nextState, ArrayList<Object[]> saveData);
	
	/**
	 * M�thode qui doit �tre ex�cut�e lorsqu'on veut retourner dans l'�tat pr�c�dent.
	 */
	public abstract void stateReturnRequested();
	
	/**
	 * M�thode qui doit �tre ex�cut�e lorsque l'�tat (principal ou secondaire) termine son initialisation.
	 */
	public abstract void stateChanged();
	
	/**
	 * M�thode qui doit �tre ex�cut�e lorsqu'on veut r�initialiser l'�tat actuel.
	 */
	public abstract void resetRequested();	
	
	/**
	 * M�thode qui doit �tre ex�cut�e lorsque la balle entre en contact avec la batte
	 * @param worldMatrix Matrice monde vers composant.
	 * @param handler Gestionnaire des objets de jeu.
	 * @param cam la cam�ra fix�e � la balle
	 */
	public abstract void hitBat(AffineTransform worldMatrix, Handler handler, Camera cam);
	
	/**
	 * M�thode qui doit �tre ex�cut�e lorsqu'on veut d�sactiver le mouseListener
	 */
	public abstract void disableBat();
	
	/**
	 * M�thode qui doit �tre ex�cut�e lorsqu'on veut d�placer la cam�ra
	 * @param cam la cam�ra
	 */
	public abstract void moveCamera(Camera cam);
	
	/**
	 * M�thode qui doit �tre ex�cut�e lorsqu'on veut d�sactiver le keyListener
	 */
	public abstract void disableCamera();
	
	/**
	 * M�thode qui doit �tre ex�cut�e lorsqu'on veut r�initialiser le jeu apr�s que la balle ait touch� le sol.
	 */
	public abstract void resetBall();
	
	/**
	 * M�thode qui doit �tre ex�cut�e lorsqu'on veut d�sactiver le keyListener de la balle
	 */
	public abstract void disableResetBall();

}
