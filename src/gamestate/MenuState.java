package gamestate;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import javax.swing.event.EventListenerList;

import util.BufferedImageLoader;
import util.GameStateListener;
import application.App05FlickBaseball;
import application.GamePanel;
import drawable.util.Button;

/**
 * Le panneau du menu principal du jeu.
 * 
 * @author Olivier St-Jean
 * @since 09-02-2015
 * @version 04-05-2015
 */
public class MenuState implements IGameState {
	
	private final int BTN_WIDTH = 400;
	private final int BTN_HEIGHT = 50;
	private final int SPACING = 75;
	private final int NB_BTNS = 5;
	private final int NB_SPACES = NB_BTNS - 1;
	private final EventListenerList REGISTERED_LISTENERS = new EventListenerList();
	private final int id = GameStateManager.MENU_STATE;
	
	private ArrayList<Component> components;
	private BufferedImage title;
	private ArrayList<Object[]> saveData;
	
	/**
	 * Initialise les différentes propriétés du menu principal.
	 */
	public void init(){
		components = new ArrayList<Component>();
		saveData = new ArrayList<Object[]>();
		
		int btnX = GamePanel.WIDTH - SPACING - BTN_WIDTH;		
		int btnNormalGameY = (GamePanel.HEIGHT - (SPACING*NB_SPACES + BTN_HEIGHT*NB_BTNS))/2;
		int btnFreeGameY = btnNormalGameY + BTN_HEIGHT + SPACING;
		int btnOptionsY = btnFreeGameY + BTN_HEIGHT + SPACING;
		int btnHelpY = btnOptionsY + BTN_HEIGHT + SPACING;
		int btnExitY = btnHelpY + BTN_HEIGHT + SPACING;
		
		Button btnNormalGame = new Button("Partie standard", "/menu/");
		btnNormalGame.setBounds(btnX, btnNormalGameY, BTN_WIDTH, BTN_HEIGHT);
		btnNormalGame.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				newGameOrContinue(GameStateManager.NORMAL_MODE_STATE);
			}
		});
		Button btnFreeGame = new Button("Mode libre", "/menu/");
		btnFreeGame.setBounds(btnX, btnFreeGameY, BTN_WIDTH, BTN_HEIGHT);
		btnFreeGame.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				newGameOrContinue(GameStateManager.FREE_MODE_STATE);
			}
		});
		Button btnOptions = new Button("Options", "/menu/");
		btnOptions.setBounds(btnX, btnOptionsY, BTN_WIDTH, BTN_HEIGHT);
		btnOptions.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				requestChange(GameStateManager.OPTION_STATE);
			}
		});
		Button btnHelp = new Button("Aide", "/menu/");
		btnHelp.setBounds(btnX, btnHelpY, BTN_WIDTH, BTN_HEIGHT);
		btnHelp.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				requestChange(GameStateManager.HELP_STATE);
			}
		});
		Button btnExit = new Button("Quitter", "/menu/");
		btnExit.setBounds(btnX, btnExitY, BTN_WIDTH, BTN_HEIGHT);
		btnExit.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		
		components.add(btnNormalGame);
		components.add(btnFreeGame);
		components.add(btnOptions);
		components.add(btnHelp);
		components.add(btnExit);
		
		notifyChange();
		
		BufferedImageLoader bil = new BufferedImageLoader();
		title = bil.loadImage("/menu/title.png");
	}
	
	//À cause du lecteur d'objet, il va envoyer une erreur parce qu'on a une collection, pas un objet.
	@SuppressWarnings("unchecked")
	private void newGameOrContinue(int nextState){
		components = new ArrayList<Component>();
		
		int nbButtons = 3;
		int nbSpaces = nbButtons - 1;
		int btnX = (GamePanel.WIDTH - BTN_WIDTH)/2;		
		int btnResumeY = (GamePanel.HEIGHT - (SPACING*nbSpaces + BTN_HEIGHT*nbButtons))/2;
		int btnNewGameY = btnResumeY + BTN_HEIGHT + SPACING;
		int btnReturnY = btnNewGameY + BTN_HEIGHT + SPACING;
		
		Button btnResume = new Button("Continuer la partie", "/menu/");
		btnResume.setBounds(btnX, btnResumeY, BTN_WIDTH, BTN_HEIGHT);
		btnResume.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				requestChange(nextState, saveData);
			}
		});
		Button btnNewGame = new Button("Nouvelle partie", "/menu/");
		btnNewGame.setBounds(btnX, btnNewGameY, BTN_WIDTH, BTN_HEIGHT);
		btnNewGame.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				requestChange(nextState, null);
			}
		});
		Button btnReturn = new Button("Revenir au menu principal", "/menu/");
		btnReturn.setBounds(btnX, btnReturnY, BTN_WIDTH, BTN_HEIGHT);
		btnReturn.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				init();
			}
		});
		
		String fileName = (nextState == GameStateManager.NORMAL_MODE_STATE) ? App05FlickBaseball.NM_SAVE_NAME : App05FlickBaseball.FM_SAVE_NAME;
		try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))){			
			saveData = saveData.getClass().cast(ois.readObject());
		}catch(IOException|ClassNotFoundException e){
			System.err.println("Aucune sauvegarde pour se mode de jeu. Chargement désactivé!");
			btnResume.setEnabled(false);
			saveData = null;
		}
		
		components.add(btnResume);
		components.add(btnNewGame);
		components.add(btnReturn);
		
		notifyChange();
	}

	/**
	 * Mise à jour des propriétés des différents composants du menu principal.
	 */
	public void update() {}

	/**
	 * Mise à jour des dessins des différents composant du menu principal.
	 * @param g2d ({@link Graphics2D}) le contexte graphique 2D du menu principal
	 */
	public void draw(Graphics2D g2d) {
		g2d.setColor(Color.WHITE);
		g2d.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
		g2d.drawImage(title, 50, 50, null);
	}
	
	/**
	 * Retourner la structure de donnée qui détient tous les boutons Swing.
	 * @return La structure de donnée qui détient tous les boutons Swing.
	 */
	public ArrayList<Component> getComponents(){
		return this.components;
	}
	
	/**
	 * Permet d'ajouter un écouteur pour les changements d'états.
	 */
	public void addGameStateListener(GameStateListener l){
		REGISTERED_LISTENERS.add(GameStateListener.class, l);
	}
	
	/**
	 * Passer vers un autre menu
	 * @param state Le menu qu'on désire accéder.
	 */
	private void requestChange(int state){
		for(GameStateListener listener : REGISTERED_LISTENERS.getListeners(GameStateListener.class)){
			listener.stateChangeRequested(id, state);
		}
	}
	
	/**
	 * Passer vers un mode de jeu.
	 * @param state Le mode de jeu qu'on désire accéder.
	 * @param saveData structure de donnée qui contient les informations sur une partie sauvegardée
	 */
	private void requestChange(int state, ArrayList<Object[]> saveData){
		for(GameStateListener listener : REGISTERED_LISTENERS.getListeners(GameStateListener.class)){
			listener.stateChangeRequested(id, state, saveData);
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
