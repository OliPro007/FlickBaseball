package gamestate;

import java.awt.Component;
import java.awt.Graphics2D;
import java.util.ArrayList;

import util.GameStateListener;

/**
 * Classe mod�le pour les �tats de jeu.
 * 
 * @author Olivier St-Jean
 * @version 04-05-2015
 */
public interface IGameState {
	
	/**
	 * M�thode d'initialisation de l'�tat. Permet g�n�ralement d'initialiser
	 * la liste des composants graphiques n�cessaires et des param�tres ou 
	 * r�glements n�cessaires pour la logique.
	 */
	public abstract void init();
	
	/**
	 * M�thode de mise � jour de la logique. Permet g�n�ralement de manipuler les interactions des �l�ments
	 * qui sont dynamiquement modifi�s pendant le fonctionnement de l'application.
	 */
	public abstract void update();
	
	/**
	 * M�thode de dessin de l'�tat. Permet de dessiner tout: l'arri�re-plan, les composants graphiques,
	 * les objets de jeu, etc.
	 * @param g2d ({@link Graphics2D}) Le contexte graphique sur lequel dessiner.
	 */
	public abstract void draw(Graphics2D g2d);
	
	/**
	 * M�thode d'acc�s qui permet de retourner les boutons cr��s dans la phase init.
	 * @return ({@link ArrayList}) La liste des boutons cr��s dans la phase init.
	 */
	public abstract ArrayList<Component> getComponents();
	
	/**
	 * M�thode qui permet d'ajouter un �couteur � l'�tat. L'�tat avertiera l'�couteur
	 * lorsque la phase init de l'�tat est compl�t�e.
	 * @param l ({@link GameStateListener}) L'objet �couteur.
	 * @see GameStateListener#stateChanged()
	 */
	public abstract void addGameStateListener(GameStateListener l);
}
