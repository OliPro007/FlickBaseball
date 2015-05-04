package application;

import gamestate.FreeModeState;
import gamestate.NormalGameState;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

/**
 * Classe d'entrée de l'application.
 * 
 * @author Olivier St-Jean
 * @since 9-02-2015
 * @version 02-05-2015
 */
public class App05FlickBaseball extends JFrame {
	
	public static final String NM_SAVE_NAME = NormalGameState.class.getSimpleName() + ".sav";
	public static final String FM_SAVE_NAME = FreeModeState.class.getSimpleName() + ".sav";

	private static final long serialVersionUID = 6673268957041734599L;
	private JPanel contentPane;

	/**
	 * Lancer l'application.
	 * @param args Les arguments à la ligne de commande.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					App05FlickBaseball frame = new App05FlickBaseball();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * <b>Constructeur</b>
	 * <p>Créer le frame et attribuer le conteneur.</p>
	 * @see GamePanel#init()
	 */
	public App05FlickBaseball() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Flick Baseball");
		setResizable(false);
		//http://stackoverflow.com/questions/11570356/jframe-in-full-screen-java
		//Ceci permet de mettre le jeu en plein écran! (Seulement sur Windows)
		/*setUndecorated(true);
		setExtendedState(JFrame.MAXIMIZED_BOTH);*/
		//http://stackoverflow.com/questions/16364487/java-rendering-loop-and-logic-loop
		//Plein écran sur multiplatforme mais cause des problèmes avec aero de Windows 7
		/*GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice device = env.getDefaultScreenDevice();
        device.setFullScreenWindow(this);*/
		contentPane = new GamePanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		setContentPane(contentPane);
		((GamePanel) contentPane).init();
		pack(); //il y a une interférence avec le full screen
		setLocationRelativeTo(null);
	}

}
