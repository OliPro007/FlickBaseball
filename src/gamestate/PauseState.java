package gamestate;

import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.event.EventListenerList;

import util.GameStateListener;
import application.GamePanel;
import drawable.util.Button;

/**
 * Le panneau qui s'affiche lorsque l'utilisateur pause le jeu et qui permet de modifier des param�tres du panneau de jeu.
 * 
 * @author Olivier St-Jean
 * @version 04-05-2015
 */
public class PauseState implements IGameState {

	private final int BTN_WIDTH = 400;
	private final int BTN_HEIGHT = 50;
	private final int SPACING = 75;
	private final int NB_BTNS = 4;
	private final int NB_SPACES = NB_BTNS - 1;
	private final EventListenerList REGISTERED_LISTENERS = new EventListenerList();
	private final int id = GameStateManager.PAUSE_STATE;
	
	private ArrayList<Component> components;
	
	/**
	 * Initialise les diff�rentes propri�t�s du jeu.
	 */
	public void init() {
		Button btnContinue;
		Button btnHelp;
		Button btnQuit;
		Button btnOption;
		components = new ArrayList<Component>();
		
		int btnX = (GamePanel.WIDTH-BTN_WIDTH)/2;
		int btnContinueY = (GamePanel.HEIGHT - (SPACING*NB_SPACES + BTN_HEIGHT*NB_BTNS))/2;
		int btnOptionY = btnContinueY + BTN_HEIGHT + SPACING;
		int btnHelpY = btnOptionY + BTN_HEIGHT + SPACING;		
		int btnQuitY = btnHelpY + BTN_HEIGHT + SPACING;
		
		btnContinue = new Button("Reprendre", "/menu/");
		btnContinue.setBounds(btnX, btnContinueY, BTN_WIDTH, BTN_HEIGHT);
		btnContinue.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				requestReturn();
			}
		});
		
		btnOption = new Button("Option", "/menu/");
		btnOption.setBounds(btnX, btnOptionY, BTN_WIDTH, BTN_HEIGHT);
		btnOption.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				requestChange(GameStateManager.OPTION_STATE);
			}
		});
		
		btnHelp = new Button("Aide", "/menu/");
		btnHelp.setBounds(btnX, btnHelpY, BTN_WIDTH, BTN_HEIGHT);
		btnHelp.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				requestChange(GameStateManager.HELP_STATE);
			}
		});
		
		btnQuit = new Button("Abandonner la partie", "/menu/");
		btnQuit.setBounds(btnX, btnQuitY, BTN_WIDTH, BTN_HEIGHT);
		btnQuit.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				requestReset();
				requestChange(GameStateManager.MENU_STATE);
			}
		});
		
		
		
		components.add(btnContinue);
		components.add(btnHelp);
		components.add(btnOption);
		components.add(btnQuit);
		
		notifyChange();
	}

	/**
	 * Mise � jour des propri�t�s de diff�rents objets de jeu et les comportements du jeu.
	 */
	public void update() {
		
	}

	/**
	 * Mise � jour des dessins des diff�rents objets de jeu et le dessin du jeu.
	 * @param g2d ({@link Graphics2D}) le contexte graphique 2D du menu de pause
	 */
	public void draw(Graphics2D g2d) {
		
	}

	/**
	 * Retourner la structure de donn�e qui d�tient tous les boutons Swing.
	 * @return La structure de donn�e qui d�tient tous les boutons Swing.
	 */
	public ArrayList<Component> getComponents(){
		return this.components;
	}
	
	/**
	 * Permet d'ajouter un �couteur pour les changements d'�tats.
	 */
	public void addGameStateListener(GameStateListener l){
		REGISTERED_LISTENERS.add(GameStateListener.class, l);
	}
	
	/**
	 * Passer vers un autre menu
	 * @param state le menu qu'on d�sire y acc�der
	 */
	private void requestChange(int state){
		for(GameStateListener listener : REGISTERED_LISTENERS.getListeners(GameStateListener.class)){
			listener.stateChangeRequested(id, state);
		}
	}
	
	/**
	 * M�thode qui permet de passer vers le menu pr�c�dent de la liste
	 */
	private void requestReturn(){
		for(GameStateListener listener : REGISTERED_LISTENERS.getListeners(GameStateListener.class)){
			listener.stateReturnRequested();
		}
	}
	
	/**
	 * M�thode qui permet de r�initialiser le menu actuel
	 */
	private void requestReset(){
		for(GameStateListener listener : REGISTERED_LISTENERS.getListeners(GameStateListener.class)){
			listener.resetRequested();
		}
	}
	
	/**
	 * Signaler le besoin d'ajouter un ou plusieurs composants Swing
	 */
	private void notifyChange(){
		for(GameStateListener listener : REGISTERED_LISTENERS.getListeners(GameStateListener.class)){
			listener.stateChanged();
		}
	}

}
