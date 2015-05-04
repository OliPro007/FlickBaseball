package gamestate;

import java.awt.Component;
import java.awt.Graphics2D;
import java.util.ArrayList;

import util.GameStateListener;

/**
 * Classe modèle pour les états de jeu.
 * 
 * @author Olivier St-Jean
 * @version 04-05-2015
 */
public interface IGameState {
	
	/**
	 * Méthode d'initialisation de l'état. Permet généralement d'initialiser
	 * la liste des composants graphiques nécessaires et des paramètres ou 
	 * règlements nécessaires pour la logique.
	 */
	public abstract void init();
	
	/**
	 * Méthode de mise à jour de la logique. Permet généralement de manipuler les interactions des éléments
	 * qui sont dynamiquement modifiés pendant le fonctionnement de l'application.
	 */
	public abstract void update();
	
	/**
	 * Méthode de dessin de l'état. Permet de dessiner tout: l'arrière-plan, les composants graphiques,
	 * les objets de jeu, etc.
	 * @param g2d ({@link Graphics2D}) Le contexte graphique sur lequel dessiner.
	 */
	public abstract void draw(Graphics2D g2d);
	
	/**
	 * Méthode d'accès qui permet de retourner les boutons créés dans la phase init.
	 * @return ({@link ArrayList}) La liste des boutons créés dans la phase init.
	 */
	public abstract ArrayList<Component> getComponents();
	
	/**
	 * Méthode qui permet d'ajouter un écouteur à l'état. L'état avertiera l'écouteur
	 * lorsque la phase init de l'état est complétée.
	 * @param l ({@link GameStateListener}) L'objet écouteur.
	 * @see GameStateListener#stateChanged()
	 */
	public abstract void addGameStateListener(GameStateListener l);
}
