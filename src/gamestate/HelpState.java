package gamestate;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.EventListenerList;

import util.BufferedImageLoader;
import util.GameStateListener;
import application.GamePanel;
import drawable.util.Button;
/**
 * Le Panneau où sont inscrits les instructions pour le jeu d'essai
 * 
 * @author Alexandre Hua
 * @version 04-05-2015
 */
public class HelpState implements IGameState{
	private final int BTN_WIDTH = 400;
	private final int BTN_HEIGHT = 50;
	private final int BTN_SPACING_INDEX = 50;
	private final int NB_BTNS_INDEX = 6;
	private final int NB_SPACES_INDEX = NB_BTNS_INDEX - 1;
	private final int BTN_SPACING = 20;
	private final int BORDER_SPACING = 50;
	private final EventListenerList REGISTERED_LISTENERS = new EventListenerList();
	//private final int id = GameStateManager.HELP_STATE; //Inutile, état de plus haut niveau.
	
	private ArrayList<Component> components;
	private BufferedImage titleImg;
	private String sectionTitle;
	
	/**
	 * Initialise les différentes boutons du menu de principal d'aide qui servira d'index pour les suivants
	 */
	public void init() {
		sectionTitle = "Aide";
		components = new ArrayList<Component>();
		
		int btnX = (GamePanel.WIDTH - BTN_WIDTH) / 2;		
		int btnTestCasesY = (GamePanel.HEIGHT - (BTN_SPACING_INDEX*NB_SPACES_INDEX + BTN_HEIGHT*NB_BTNS_INDEX))/2;
		int btnInstructionsY = btnTestCasesY + BTN_HEIGHT + BTN_SPACING_INDEX;
		int btnConceptsY = btnInstructionsY + BTN_HEIGHT + BTN_SPACING_INDEX;
		int btnSourcesY = btnConceptsY + BTN_HEIGHT + BTN_SPACING_INDEX;
		int btnAboutY = btnSourcesY + BTN_HEIGHT + BTN_SPACING_INDEX;
		int btnReturnY = btnAboutY + BTN_HEIGHT + BTN_SPACING_INDEX;
		
		Button btnTestCases = new Button("Jeux d'essai", "/menu/");
		btnTestCases.setBounds(btnX, btnTestCasesY, BTN_WIDTH, BTN_HEIGHT);
		btnTestCases.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				initTestCases();
			}
		});
		Button btnInstructions = new Button("Instructions complètes", "/menu/");
		btnInstructions.setBounds(btnX, btnInstructionsY, BTN_WIDTH, BTN_HEIGHT);
		btnInstructions.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				initInstructions();
			}
		});
		Button btnConcepts = new Button("Concepts scientifiques", "/menu/");
		btnConcepts.setBounds(btnX, btnConceptsY, BTN_WIDTH, BTN_HEIGHT);
		btnConcepts.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				initConcepts();
			}
		});
		Button btnSources = new Button("Sources", "/menu/");
		btnSources.setBounds(btnX, btnSourcesY, BTN_WIDTH, BTN_HEIGHT);
		btnSources.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				initSources();
			}
		});
		Button btnAbout = new Button("À propos", "/menu/");
		btnAbout.setBounds(btnX, btnAboutY, BTN_WIDTH, BTN_HEIGHT);
		btnAbout.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				initAbout();
			}
		});
		Button btnReturn = new Button("Revenir au menu précédent", "/menu/");
		btnReturn.setBounds(btnX, btnReturnY, BTN_WIDTH, BTN_HEIGHT);
		btnReturn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				requestReturn();
			}
		});
		
		components.add(btnTestCases);
		components.add(btnInstructions);
		components.add(btnConcepts);
		components.add(btnSources);
		components.add(btnAbout);
		components.add(btnReturn);
		
		BufferedImageLoader bil = new BufferedImageLoader();
		titleImg = bil.loadImage("/menu/title_help.png");	
		
		notifyChange();
	}
	
	/**
	 * Initialise les différentes propriétés du menu de jeu d'essai
	 */
	private void initTestCases(){
		sectionTitle = "Jeu d'essai";
		components = new ArrayList<Component>();
		Button btnReturn = new Button("Revenir au menu précédent", "/menu/");
		btnReturn.setBounds(GamePanel.WIDTH-BTN_WIDTH-BTN_SPACING, GamePanel.HEIGHT-BTN_HEIGHT-BTN_SPACING, BTN_WIDTH, BTN_HEIGHT);
		btnReturn.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				requestReturn();
			}
		});
		components.add(btnReturn);
		
		Button btnBack = new Button("Retour à l'index", "/menu/");
		btnBack.setBounds(BTN_SPACING, GamePanel.HEIGHT-BTN_HEIGHT-BTN_SPACING, BTN_WIDTH, BTN_HEIGHT);
		btnBack.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				init();
			}
		});
		components.add(btnBack);
		
		JTextArea textBox = new JTextArea();
		//textBox.setBounds(BORDER_SPACING, 2*BORDER_SPACING, GamePanel.WIDTH-2*BORDER_SPACING, GamePanel.HEIGHT-3*BORDER_SPACING-BUTTON_SPACING-BUTTON_HEIGHT);
		textBox.setLineWrap(true);
		textBox.setBackground(new Color(255,255,255,100));
		textBox.setWrapStyleWord(true);
		textBox.setEditable(false);
		textBox.setMargin(new Insets(BORDER_SPACING,BORDER_SPACING,BORDER_SPACING,BORDER_SPACING));
		//textBox.setLayout(null);
		//Font fontTemp = textBox.getFont();
		textBox.setFont(new Font("Time New Roman", Font.BOLD, 20));
		textBox.setText(readText("/textDoc/jeu_essai.txt"));
		textBox.setCaretPosition(0);
		//components.add(textBox);
		
		//il y a un petit problème de dessin au moment qu'on bouge le slider
		JScrollPane scroll = new JScrollPane(textBox, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.setBounds(BORDER_SPACING, 2*BORDER_SPACING, GamePanel.WIDTH-2*BORDER_SPACING, GamePanel.HEIGHT-3*BORDER_SPACING-BTN_SPACING-BTN_HEIGHT);
		scroll.setBackground(new Color(255,255,255,100));
		components.add(scroll);
		notifyChange();
	}
	
	/**
	 * Initialise les différentes propriétés du menu d'instruction
	 */
	private void initInstructions(){
		sectionTitle = "Instructions complètes";
		components = new ArrayList<Component>();
		Button btnReturn = new Button("Revenir au menu précédent", "/menu/");
		btnReturn.setBounds(GamePanel.WIDTH-BTN_WIDTH-BTN_SPACING, GamePanel.HEIGHT-BTN_HEIGHT-BTN_SPACING, BTN_WIDTH, BTN_HEIGHT);
		btnReturn.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				requestReturn();
			}
		});
		components.add(btnReturn);
		
		Button btnBack = new Button("Retour à l'index", "/menu/");
		btnBack.setBounds(BTN_SPACING, GamePanel.HEIGHT-BTN_HEIGHT-BTN_SPACING, BTN_WIDTH, BTN_HEIGHT);
		btnBack.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				init();
			}
		});
		components.add(btnBack);
		
		JTextArea textBox = new JTextArea();
		//textBox.setBounds(BORDER_SPACING, 2*BORDER_SPACING, GamePanel.WIDTH-2*BORDER_SPACING, GamePanel.HEIGHT-3*BORDER_SPACING-BUTTON_SPACING-BUTTON_HEIGHT);
		textBox.setLineWrap(true);
		textBox.setBackground(new Color(255,255,255,100));
		textBox.setWrapStyleWord(true);
		textBox.setEditable(false);
		textBox.setMargin(new Insets(BORDER_SPACING,BORDER_SPACING,BORDER_SPACING,BORDER_SPACING));
		//textBox.setLayout(null);
		//Font fontTemp = textBox.getFont();
		textBox.setFont(new Font("Time New Roman", Font.BOLD, 20));
		/*textBox.setText("Ce projet s’agit d’une épreuve synthèse du cours dont l’objectif est de créer une application qui utilise des principes physiques et mathématiques."
				+ "Inspirée par des comportements réalistes du monde réel, notre application vous offre une expérience absolument merveilleuse, vous permettant de redécouvrir "
				+ "le monde dans un plan virtuel.");*/
		textBox.setText(readText("/textDoc/instructions.txt"));
		textBox.setCaretPosition(0);
		JScrollPane scroll = new JScrollPane(textBox, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.setBounds(BORDER_SPACING, 2*BORDER_SPACING, GamePanel.WIDTH-2*BORDER_SPACING, GamePanel.HEIGHT-3*BORDER_SPACING-BTN_SPACING-BTN_HEIGHT);
		scroll.setBackground(new Color(255,255,255,100));
		components.add(scroll);
		notifyChange();
	}
	
	/**
	 * Initialise les différentes propriétés du menu de concepts scientifiques
	 */
	private void initConcepts(){
		sectionTitle = "Concepts scientifiques";
		components = new ArrayList<Component>();
		Button btnReturn = new Button("Revenir au menu précédent", "/menu/");
		btnReturn.setBounds(GamePanel.WIDTH-BTN_WIDTH-BTN_SPACING, GamePanel.HEIGHT-BTN_HEIGHT-BTN_SPACING, BTN_WIDTH, BTN_HEIGHT);
		btnReturn.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				requestReturn();
			}
		});
		components.add(btnReturn);
		
		Button btnBack = new Button("Retour à l'index", "/menu/");
		btnBack.setBounds(BTN_SPACING, GamePanel.HEIGHT-BTN_HEIGHT-BTN_SPACING, BTN_WIDTH, BTN_HEIGHT);
		btnBack.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				init();
			}
		});
		components.add(btnBack);
		
		JTextArea textBox = new JTextArea();
		//textBox.setBounds(BORDER_SPACING, 2*BORDER_SPACING, GamePanel.WIDTH-2*BORDER_SPACING, GamePanel.HEIGHT-3*BORDER_SPACING-BUTTON_SPACING-BUTTON_HEIGHT);
		textBox.setLineWrap(true);
		textBox.setBackground(new Color(255,255,255,100));
		textBox.setWrapStyleWord(true);
		textBox.setEditable(false);
		textBox.setMargin(new Insets(BORDER_SPACING,BORDER_SPACING,BORDER_SPACING,BORDER_SPACING));
		//textBox.setLayout(null);
		//Font fontTemp = textBox.getFont();
		textBox.setFont(new Font("Time New Roman", Font.BOLD, 20));
		textBox.setText(readText("/textDoc/concepts_scientifiques.txt"));
		textBox.setCaretPosition(0);
		JScrollPane scroll = new JScrollPane(textBox, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.setBounds(BORDER_SPACING, 2*BORDER_SPACING, GamePanel.WIDTH-2*BORDER_SPACING, GamePanel.HEIGHT-3*BORDER_SPACING-BTN_SPACING-BTN_HEIGHT);
		scroll.setBackground(new Color(255,255,255,100));
		components.add(scroll);
		notifyChange();
	}
	
	/**
	 * Initialise les différentes propriétés du menu de sources
	 */
	private void initSources(){
		sectionTitle = "Sources";
		components = new ArrayList<Component>();
		Button btnReturn = new Button("Revenir au menu précédent", "/menu/");
		btnReturn.setBounds(GamePanel.WIDTH-BTN_WIDTH-BTN_SPACING, GamePanel.HEIGHT-BTN_HEIGHT-BTN_SPACING, BTN_WIDTH, BTN_HEIGHT);
		btnReturn.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				requestReturn();
			}
		});
		components.add(btnReturn);
		
		Button btnBack = new Button("Retour à l'index", "/menu/");
		btnBack.setBounds(BTN_SPACING, GamePanel.HEIGHT-BTN_HEIGHT-BTN_SPACING, BTN_WIDTH, BTN_HEIGHT);
		btnBack.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				init();
			}
		});
		components.add(btnBack);
		
		JTextArea textBox = new JTextArea();
		//textBox.setBounds(BORDER_SPACING, 2*BORDER_SPACING, GamePanel.WIDTH-2*BORDER_SPACING, GamePanel.HEIGHT-3*BORDER_SPACING-BUTTON_SPACING-BUTTON_HEIGHT);
		textBox.setLineWrap(true);
		textBox.setBackground(new Color(255,255,255,100));
		textBox.setWrapStyleWord(true);
		textBox.setEditable(false);
		textBox.setMargin(new Insets(BORDER_SPACING,BORDER_SPACING,BORDER_SPACING,BORDER_SPACING));
		//textBox.setLayout(null);
		//Font fontTemp = textBox.getFont();
		textBox.setFont(new Font("Time New Roman", Font.BOLD, 20));
		textBox.setText(readText("/textDoc/sources.txt"));
		textBox.setCaretPosition(0);
		JScrollPane scroll = new JScrollPane(textBox, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.setBounds(BORDER_SPACING, 2*BORDER_SPACING, GamePanel.WIDTH-2*BORDER_SPACING, GamePanel.HEIGHT-3*BORDER_SPACING-BTN_SPACING-BTN_HEIGHT);
		scroll.setBackground(new Color(255,255,255,100));
		components.add(scroll);
		notifyChange();
	}
	
	/**
	 * Initialise les différentes propriétés du menu d'à propos
	 */
	private void initAbout(){
		sectionTitle = "À propos";
		components = new ArrayList<Component>();
		Button btnReturn = new Button("Revenir au menu précédent", "/menu/");
		btnReturn.setBounds(GamePanel.WIDTH-BTN_WIDTH-BTN_SPACING, GamePanel.HEIGHT-BTN_HEIGHT-BTN_SPACING, BTN_WIDTH, BTN_HEIGHT);
		btnReturn.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				requestReturn();
			}
		});
		components.add(btnReturn);
		
		Button btnBack = new Button("Retour à l'index", "/menu/");
		btnBack.setBounds(BTN_SPACING, GamePanel.HEIGHT-BTN_HEIGHT-BTN_SPACING, BTN_WIDTH, BTN_HEIGHT);
		btnBack.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				init();
			}
		});
		components.add(btnBack);
		
		JTextArea textBox = new JTextArea();
		//textBox.setBounds(BORDER_SPACING, 2*BORDER_SPACING, GamePanel.WIDTH-2*BORDER_SPACING, GamePanel.HEIGHT-3*BORDER_SPACING-BUTTON_SPACING-BUTTON_HEIGHT);
		textBox.setLineWrap(true);
		textBox.setBackground(new Color(255,255,255,100));
		textBox.setWrapStyleWord(true);
		textBox.setEditable(false);
		textBox.setMargin(new Insets(BORDER_SPACING,BORDER_SPACING,BORDER_SPACING,BORDER_SPACING));
		//textBox.setLayout(null);
		//Font fontTemp = textBox.getFont();
		textBox.setFont(new Font("Time New Roman", Font.BOLD, 25));
		textBox.setText("\n\nFlick Baseball v1.0.0\n\n"
					  + "(\u0254) Olivier St-Jean, Alexandre Hua, 2015\n\n"
					  + "420-SCD: Intégration des apprentissages en sciences informatiques et mathématiques\n"
					  + "Collège de Maisonneuve");
		textBox.setCaretPosition(0);
		JScrollPane scroll = new JScrollPane(textBox, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.setBounds(BORDER_SPACING, 2*BORDER_SPACING, GamePanel.WIDTH-2*BORDER_SPACING, GamePanel.HEIGHT-3*BORDER_SPACING-BTN_SPACING-BTN_HEIGHT);
		scroll.setBackground(new Color(255,255,255,100));
		components.add(scroll);
		notifyChange();
	}

	/**
	 * Mise à jour des propriétés des différents composants du menu d'aide
	 */
	public void update() {
		
	}

	/**
	 * Mise à jour des dessins des différents composants du menu d'aide
	 * @param g2d ({@link Graphics2D}) le contexte graphique 2D du menu d'aide
	 */
	public void draw(Graphics2D g2d) {
		g2d.setColor(Color.ORANGE);
		g2d.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
		g2d.drawImage(titleImg, 50, 50, null);
		
		//Merci à stackoverflow pour ce truc
		//http://stackoverflow.com/questions/1055851/how-do-you-draw-a-string-centered-vertically-in-java
		Font font = new Font("Time New Roman", Font.BOLD, 40);
		FontRenderContext context = g2d.getFontRenderContext();
		TextLayout txt = new TextLayout(sectionTitle, font, context);
		g2d.setColor(Color.BLACK);
		g2d.setFont(font);
		g2d.drawString(sectionTitle, (int)(GamePanel.WIDTH-txt.getBounds().getWidth())/2, (int)(BORDER_SPACING));
	}

	/**
	 * Retourner la structure de donnée qui détient tous les boutons Swing
	 * @return la structure de donnée qui détient tous les boutons Swing
	 */
	public ArrayList<Component> getComponents() {
		return components;
	}
	
	/**
	 * Méthode qui permet de lire les fichiers .txt
	 * @param path l'URL du fichier .txt
	 * @return le contenu de texte en String du fichier .txt
	 */
	private String readText(String path) {
		String string = new String();
		try {
			InputStream stream = getClass().getResourceAsStream(path);
			InputStreamReader reader = new InputStreamReader(stream);
			int charTemp;
			do {
				charTemp = reader.read();
				if(charTemp > 0)
					string += (char)charTemp;			
			} while(charTemp != -1);
			stream.close();
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return string.toString();
	}

	/**
	 * Permet d'ajouter un écouteur pour les changements d'états.
	 */
	public void addGameStateListener(GameStateListener l){
		REGISTERED_LISTENERS.add(GameStateListener.class, l);
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
	
}
