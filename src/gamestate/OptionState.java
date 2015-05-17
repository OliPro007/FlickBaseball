package gamestate;

import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;

import javax.swing.event.EventListenerList;

import util.GameStateListener;
import util.OptionsListener;
import application.GamePanel;
import drawable.util.Button;
import drawable.util.Checkbox;

/**
 * Le panneau qui s'affiche lorsque l'utilisateur pause le jeu et qui permet de modifier des paramètres du panneau de jeu.
 * 
 * @author Olivier St-Jean
 * @version 04-05-2015
 */
public class OptionState implements IGameState {

	private final int BTN_WIDTH = 500;
	private final int BTN_HEIGHT = 50;
	private final int BTN_SPACING = 20;
	private final int SPACING = 75;
	private final int NB_BOX = 5;
	private final int NB_SPACES = NB_BOX - 1;
	private final int CHECK_BOX_WIDTH = 500;
	private final int CHECK_BOX_HEIGHT = 50;
	
	private final EventListenerList REGISTERED_LISTENERS = new EventListenerList();
	
	private ArrayList<Component> components;
	
	private boolean sfxEnabled;
	private boolean bgmEnabled;
	private boolean infoEnabled;
	
	public OptionState(){
		try(BufferedReader br = new BufferedReader(new FileReader("FlickBaseball.cfg"))){
			String str;
			while((str = br.readLine()) != null){
				String var = str.substring(0, str.indexOf('='));
				String val = str.substring(str.indexOf('=')+1);
				Field field = this.getClass().getDeclaredField(var);
				field.set(this, Boolean.parseBoolean(val));
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	
	/**
	 * Initialise les différentes propriétés du jeu.
	 */
	public void init() {
		Checkbox infoCheck;
		Checkbox bgmCheck;
		Checkbox sfxCheck;
		//JToggleButton btnResolution;
		Button btnReturn;
		components = new ArrayList<Component>();
		
		int ChkBoxX = (GamePanel.WIDTH-CHECK_BOX_WIDTH)/2;
		int infoCheckY = (GamePanel.HEIGHT - (SPACING*NB_SPACES + CHECK_BOX_HEIGHT*NB_BOX))/2;
		int bgmCheckY = infoCheckY + SPACING + CHECK_BOX_HEIGHT;
		int sfxCheckY = bgmCheckY + SPACING + CHECK_BOX_HEIGHT;
		//int btnResolutionY = sfxCheckY + SPACING + CHECK_BOX_HEIGHT;
		
		Font font = new Font("Time New Roman", Font.PLAIN, 30);
		
		infoCheck = new Checkbox("Informations Scientifiques", "/menu/");
		infoCheck.setFont(font);
		infoCheck.setBounds(ChkBoxX, infoCheckY, CHECK_BOX_WIDTH, CHECK_BOX_HEIGHT);
		infoCheck.setSelected(infoEnabled); //False par défaut
		infoCheck.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if(infoCheck.isSelected()) {
					setInfoEnabled(true);
					infoEnabled = true;
					//FreeModeState.setInfoIsEnabled(true);
				} else {
					setInfoEnabled(false);
					infoEnabled = false;
					//FreeModeState.setInfoIsEnabled(false);
				}
			}
		});
		
		
		bgmCheck = new Checkbox("Music de fond", "/menu/");
		bgmCheck.setFont(font);
		bgmCheck.setBounds(ChkBoxX, bgmCheckY, CHECK_BOX_WIDTH, CHECK_BOX_HEIGHT);
		bgmCheck.setSelected(bgmEnabled); //True par défaut
		bgmCheck.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if(bgmCheck.isSelected()) {
					setBGMEnabled(true);
					bgmEnabled = true;
					//GamePanel.playBGM();
					//GamePanel.setBgmIsEnabled(true);
				} else {
					setBGMEnabled(false);
					bgmEnabled = false;
					//GamePanel.stopBGM();
					//GamePanel.setBgmIsEnabled(false);
				}
			}
		});
		
		
		sfxCheck = new Checkbox("Effets de son", "/menu/");
		sfxCheck.setFont(font);
		sfxCheck.setBounds(ChkBoxX, sfxCheckY, CHECK_BOX_WIDTH, CHECK_BOX_HEIGHT);
		sfxCheck.setSelected(sfxEnabled); //True par défaut
		sfxCheck.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if(sfxCheck.isSelected()) {
					setSFXEnabled(true);
					sfxEnabled = true;
					//FreeModeState.setSfxIsEnabled(true);
					//AbstractBall.setSfxIsEnabled(true);
				} else {
					setSFXEnabled(false);
					sfxEnabled = false;
					//FreeModeState.setSfxIsEnabled(false);
					//AbstractBall.setSfxIsEnabled(false);
				}
			}
		});
		
		
		/*btnResolution = new JToggleButton("Plein Écran");
		btnResolution.setBounds(ChkBoxX, btnResolutionY, BTN_WIDTH, BTN_HEIGHT);
		btnResolution.setBorderPainted(false);
		btnResolution.setContentAreaFilled(false);
        btnResolution.setFocusPainted(false);
		BufferedImage imgButton = new BufferedImageLoader().loadImage("/menu/button_unselected.png");
		Image iconButton = imgButton.getScaledInstance(btnResolution.getWidth(), btnResolution.getHeight(), Image.SCALE_SMOOTH);
		btnResolution.setIcon(new ImageIcon(iconButton));
		btnResolution.setFont(new Font("Time New Roman", Font.PLAIN, 20));
		btnResolution.setForeground(Color.BLACK);
		btnResolution.setHorizontalTextPosition(JToggleButton.CENTER);
		btnResolution.setVerticalTextPosition(JToggleButton.CENTER);
		btnResolution.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(btnResolution.isSelected()) {
					btnResolution.setText("Taille Originale");
				} else {
					btnResolution.setText("Plein Écran");
				}
			}
		});*/
		
		
		btnReturn = new Button("Revenir au menu précédent", "/menu/");
		btnReturn.setBounds(GamePanel.WIDTH-BTN_WIDTH-BTN_SPACING, GamePanel.HEIGHT-BTN_HEIGHT-BTN_SPACING, BTN_WIDTH, BTN_HEIGHT);
		btnReturn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try(BufferedWriter bw = new BufferedWriter(new FileWriter("FlickBaseball.cfg"))){
					bw.write("sfxEnabled="+sfxEnabled+"\n");
					bw.write("bgmEnabled="+bgmEnabled+"\n");
					bw.write("infoEnabled="+infoEnabled);
				}catch(Exception e2){
					e2.printStackTrace();
				}
				requestReturn();
			}
		});
		
		components.add(infoCheck);
		components.add(bgmCheck);
		components.add(sfxCheck);
		//components.add(btnResolution);
		components.add(btnReturn);
		
		notifyChange();
	}

	/**
	 * Mise à jour des propriétés de différents objets de jeu et les comportements du jeu.
	 */
	public void update() {
		
	}

	/**
	 * Mise à jour des dessins des différents objets de jeu et le dessin du jeu.
	 * @param g2d ({@link Graphics2D}) le contexte graphique 2D du menu d'option
	 */
	public void draw(Graphics2D g2d) {
		
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
	 * @param l Un écouteur d'état de jeu.
	 */
	public void addGameStateListener(GameStateListener l){
		REGISTERED_LISTENERS.add(GameStateListener.class, l);
	}
	
	/**
	 * Permet d'ajouter un écouteur pour les changements d'option.
	 * @param l Un écouteur d'options.
	 */
	public void addOptionsListener(OptionsListener l){
		REGISTERED_LISTENERS.add(OptionsListener.class, l);
		setInfoEnabled(infoEnabled);
		setSFXEnabled(sfxEnabled);
		setBGMEnabled(bgmEnabled);
	}
	
	/**
	 * Méthode qui permet de passer vers le menu précédent de la liste
	 */
	private void requestReturn(){
		for(GameStateListener listener : REGISTERED_LISTENERS.getListeners(GameStateListener.class)){
			listener.stateReturnRequested();
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
	 /**
	  * Signaler le besoin de modifier l'état boolean de l'affichage des informations scientifiques.
	  * @param enabled le nouveau état boolean
	  */
	private void setInfoEnabled(boolean enabled){
		for(OptionsListener listener : REGISTERED_LISTENERS.getListeners(OptionsListener.class)){
			listener.setInfoEnabled(enabled);
		}
	}
	
	/**
	  * Signaler le besoin de modifier l'état boolean de l'effet de son.
	  * @param enabled le nouveau état boolean
	  */
	private void setSFXEnabled(boolean enabled){
		for(OptionsListener listener : REGISTERED_LISTENERS.getListeners(OptionsListener.class)){
			listener.setSFXEnabled(enabled);
		}
	}
	
	/**
	  * Signaler le besoin de modifier l'état boolean de la musique de fond.
	  * @param enabled le nouveau état boolean
	  */
	private void setBGMEnabled(boolean enabled){
		for(OptionsListener listener : REGISTERED_LISTENERS.getListeners(OptionsListener.class)){
			listener.setBGMEnabled(enabled);
		}
	}

}