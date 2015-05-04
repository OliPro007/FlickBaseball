package gamestate;

import java.awt.Component;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Stack;

import util.GameStateListener;
import util.OptionsListener;
import drawable.util.Button;

/**
 * Gestionnaire des états de jeu. Permet de gérer les
 * règles de jeu et les éléments qui seront dessinés.
 * 
 * @author Olivier St-Jean
 * @since 9-02-2015
 * @version 04-05-2015
 */
public class GameStateManager {

	public static final int INVALID_STATE = -1;
	public static final int MENU_STATE = 0;
	public static final int NORMAL_MODE_STATE = 1;
	public static final int FREE_MODE_STATE = 2;
	public static final int HELP_STATE = 3;
	public static final int PAUSE_STATE = 4;
	public static final int OPTION_STATE = 5;
	
	public static double GROUND_LEVEL;	
	private ArrayList<IGameState> gameStates = new ArrayList<IGameState>();
	private Stack<Integer> previousStates = new Stack<Integer>();
	private int currentState;
	
	/**
	 * <pre>Constructeur</pre>
	 * Ajoute les états de jeu à une liste et initialise le
	 * panneau de jeu.
	 * @see IGameState#init()
	 */
	public GameStateManager(){
		gameStates.add(new MenuState());
		gameStates.add(new NormalGameState());
		gameStates.add(new FreeModeState());
		gameStates.add(new HelpState());
		gameStates.add(new PauseState());
		gameStates.add(new OptionState());
		gameStates.trimToSize();
		
		setState(MENU_STATE);
	}
	
	/**
	 * Permet de changer l'état en cours d'utilisation.
	 * @param state Le numéro correspondant au prochain état.
	 * @see IGameState#init()
	 */
	public synchronized void setState(int state){
		currentState = state;
		gameStates.get(currentState).init();
	}
	
	/**
	 * Permet de changer l'état en cours d'utilisation pour un mode de jeu.
	 * @param state Le numéro correspondant au prochain état.
	 * @param saveData Les données sauvegardées. Peut être null s'il n'y a pas de sauvegarde.
	 * @see IGameState#init()
	 */
	public synchronized void setGameMode(int state, ArrayList<Object[]> saveData){
		currentState = state;
		if(state == NORMAL_MODE_STATE)
			((NormalGameState) gameStates.get(currentState)).loadSaveData(saveData);
		else if(state == FREE_MODE_STATE)
			((FreeModeState) gameStates.get(currentState)).loadSaveData(saveData);
		gameStates.get(currentState).init();
	}
	
	/**
	 * Élément de la boucle logique, appelant le procédé
	 * logique associé à l'état actuel.
	 * @see IGameState#update()
	 */
	public synchronized void update(){
		gameStates.get(currentState).update();
	}
	
	/**
	 * Permet de passer les instructions de dessin à l'état.
	 * @param g2d ({@link Graphics2D}) Le contexte graphique sur lequel dessiner.
	 * @see IGameState#draw(Graphics2D)
	 */
	public void draw(Graphics2D g2d){
		gameStates.get(currentState).draw(g2d);
	}
	
	/**
	 * Permet d'obtenir les boutons en lien avec l'état actuel.
	 * @return ({@link ArrayList}) Une liste des boutons de l'état.
	 * @see Button
	 */
	public ArrayList<Component> getComponents(){
		return gameStates.get(currentState).getComponents();
	}
	
	/**
	 * Permet d'ajouter un écouteur pour les changements d'états.
	 * @param l ({@link GameStateListener}) L'objet écouteur.
	 * @see IGameState#addGameStateListener(GameStateListener)
	 */
	public void addGameStateListener(GameStateListener l){
		for(IGameState state : gameStates){
			state.addGameStateListener(l);
		}
	}
	
	/**
	 * Permet d'ajouter un écouteur pour les paramètres de jeu.
	 * @param l ({@link OptionsListener}) L'objet écouteur.
	 * @see OptionState#addOptionsListener(OptionsListener)
	 */
	public void addOptionsListener(OptionsListener l){
		((OptionState) gameStates.get(OPTION_STATE)).addOptionsListener(l);
	}
	
	/**
	 * Permet d'activer ou de désactiver l'affichage des informations scientifiques.
	 * <p><b><i><u>DISPONIBLE DANS LE MODE DE JEU LIBRE UNIQUEMENT.</u></i></b></p>
	 * @param enabled Le nouvel état de l'affichage.
	 */
	public void setInfoEnabled(boolean enabled){
		((FreeModeState) gameStates.get(FREE_MODE_STATE)).setInfoEnabled(enabled);
	}
	
	/**
	 * Permet d'activer ou de désactiver les effets sonores dans les modes de jeu.
	 * @param enabled Le nouvel état d'activation.
	 */
	public void setSFXEnabled(boolean enabled){
		((NormalGameState) gameStates.get(NORMAL_MODE_STATE)).setSFXEnabled(enabled);
		((FreeModeState) gameStates.get(FREE_MODE_STATE)).setSFXEnabled(enabled);
	}
	
	/**
	 * Permet d'activer ou de désactiver le contrôle manuel de la caméra.
	 * @param enabled Le nouvel état de la caméra
	 */
	public void setCamFixed(boolean enabled) {
		if(currentState == NORMAL_MODE_STATE)
			((NormalGameState) gameStates.get(currentState)).setCamFixed(enabled);
		else
			((FreeModeState) gameStates.get(currentState)).setCamFixed(enabled);
	}
	
	/**
	 * Retourner l'état boolean de la caméra.
	 * @return l'état boolean de la caméra
	 */
	public boolean getCamFixed() {
		if(currentState == NORMAL_MODE_STATE)
			return ((NormalGameState) gameStates.get(currentState)).isCamFixed();
		else
			return ((FreeModeState) gameStates.get(currentState)).isCamFixed();
	}
	
	/**
	 * Retourner l'état boolean de la première tombée de la balle.
	 * @return l'état boolean de la tombée de la balle
	 */
	public boolean getlanded() {
		if(currentState == NORMAL_MODE_STATE)
			return ((NormalGameState) gameStates.get(currentState)).isLanded();
		else
			return ((FreeModeState) gameStates.get(currentState)).isLanded();
	}
	
	/**
	 * Mettre le menu passé en paramètre au début de la queue pour identifier le menu précédent.
	 * @param id Le menu précédent.
	 */
	public void addPreviousState(int id){
		previousStates.push(id);
	}
	
	/**
	 * Retourner l'état précédent.
	 * @return L'état précédent.
	 */
	public int getPreviousState(){
		return previousStates.pop();
	}
	
	/**
	 * Effacer les progrès du jeu pour tout recommencer.
	 */
	public void resetGame(){
		IGameState gs = gameStates.get(previousStates.pop());
		if(gs instanceof NormalGameState)
			((NormalGameState) gs).reset();
		else if(gs instanceof FreeModeState)
			((FreeModeState) gs).reset();
	}
	
	/**
	 * Réinitilialiser la balle.
	 */
	public void resetBall() {
		if(currentState == NORMAL_MODE_STATE)
			((NormalGameState) gameStates.get(currentState)).resetBallObject();
		else
			((FreeModeState) gameStates.get(currentState)).resetBallObject();
	}
	
}
