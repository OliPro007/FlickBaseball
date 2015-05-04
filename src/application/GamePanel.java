package application;

import gamestate.GameStateManager;

import java.applet.AudioClip;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.JPanel;

import util.AudioLoader;
import util.GameStateListener;
import util.OptionsListener;
import drawable.gameobject.BaseBallBat;
import drawable.gameobject.Handler;

/**
 * Seul et unique panneau contenant le jeu.
 * Il gère l'ensemble des évènements (dessin, logique, etc)
 * dans son propre processus.
 * 
 * @author Olivier St-Jean
 * @since 9-02-2015
 * @version 04-05-2015
 */
public class GamePanel extends JPanel implements Runnable{
	
	public static final int WIDTH = 1280;
	public static final int HEIGHT = 768;
	public static final double REAL_SIZE = 100; // 100 mètres
	private static final long serialVersionUID = -2201226009478254554L;

	private GameStateManager gsm;
	private ArrayList<Component> components;
	private Thread gameLoop;
	private boolean running;
	private int FPS = 60;
	private long targetTime = 1000 / FPS;
	private boolean lmbPressed = false;	
	private AudioClip BGM;
	
	/**
	 * Initialise les différentes propriétés du composant
	 * et démarre le thread.
	 * @see GameStateManager#addGameStateListener(GameStateListener)
	 * @see GameStateManager#getComponents()
	 */
	public void init(){
		this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setFocusable(true);
		AudioLoader audioLoader = new AudioLoader();
		BGM = audioLoader.loadAudio("audio/Kuroko_no_Basket.wav");
		gsm = new GameStateManager();
		gsm.addGameStateListener(new ThisGameStateListener());
		gsm.addOptionsListener(new OptionsListener() {	
			public void setSFXEnabled(boolean enabled) {
				gsm.setSFXEnabled(enabled);
			}
			
			public void setInfoEnabled(boolean enabled) {
				gsm.setInfoEnabled(enabled);
			}
			
			public void setBGMEnabled(boolean enabled) {
				if(enabled)
					playBGM();
				else
					stopBGM();
			}
		});
		
		components = gsm.getComponents();
		for(Component component : components){
			add(component);
		}
		validate();
		
		running = true;
		gameLoop = new Thread(this);
		gameLoop.setName("Game Loop");
		gameLoop.start();
	}

	/**
	 * Méthode de dessin principale.
	 * @param g ({@link Graphics}) Le contexte graphique du panneau.
	 * @see GameStateManager#draw(Graphics2D)
	 * @see JComponent#paintComponents(Graphics)
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D)g;
		AffineTransform oTransf = g2d.getTransform();
		gsm.draw(g2d);
		g2d.setTransform(oTransf);
	}

	/**
	 * Boucle de jeu principale.
	 * Appel à {@link Component#repaint()} pour éviter
	 * les problèmes de dessins intensif lorsque ce n'est
	 * pas nécessaire.
	 * @see GameStateManager#update()
	 */
	public void run() {		
		while(running){
			gsm.update();
			repaint();
			try {
				Thread.sleep(targetTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	/**
	 * Interface écouteur qui régit le comportement des états lorsqu'ils sont initialisés.
	 * @author Olivier St-Jean
	 * @version 2.0.0
	 */
	private class ThisGameStateListener implements GameStateListener{
		
		private int batIndex = -1;
		private KeyAdapter keyCamera;
		private KeyAdapter keyBallReset;
		
		public void stateChangeRequested(int thisState, int nextState) {
			gsm.addPreviousState(thisState);
			gsm.setState(nextState);
		}
		
		public void stateChangeRequested(int thisState, int nextState, ArrayList<Object[]> saveData){
			gsm.addPreviousState(thisState);
			gsm.setGameMode(nextState, saveData);
		}
		
		public void stateReturnRequested(){
			gsm.setState(gsm.getPreviousState());
		}
		
		public void stateChanged() {
			invalidate();
			for(Component component : components){
				remove(component);
			}
			components = gsm.getComponents();
			for(Component component : components){
				add(component);
			}
			validate();
		}
		
		public void resetRequested(){
			gsm.resetGame();
		}
		
		public void hitBat(AffineTransform worldMatrix, Handler handler, Camera cam) {
			addMouseListener(new MouseAdapter() {
				public void mousePressed(MouseEvent e) {
					if(e.getButton() == MouseEvent.BUTTON1){
						//limiter la zone d'interaction de la batte
						double mouseX = e.getX()-cam.getX(); 
						if(mouseX > GamePanel.WIDTH/2) {
							mouseX = GamePanel.WIDTH/2;
						}
						
						BaseBallBat bat = new BaseBallBat((mouseX)/worldMatrix.getScaleX(), e.getY()/worldMatrix.getScaleY(), 1, worldMatrix);
						handler.getObjects().add(bat);
						batIndex = handler.getObjects().indexOf(bat);
						lmbPressed = true;
					}
				}
				
				public void mouseReleased(MouseEvent e) {
					if(e.getButton() == MouseEvent.BUTTON1){
						for(int i = 0; i < handler.getObjects().size(); i++) {
							if(handler.getObjects().get(i) instanceof BaseBallBat) {
								handler.getObjects().remove(i);
							}
						}
						lmbPressed = false;
					}
				}
			});
			
			addMouseMotionListener(new MouseMotionAdapter() {
				@Override
				public void mouseDragged(MouseEvent e) {
					if(lmbPressed){
						BaseBallBat bat = (BaseBallBat) handler.getObjects().get(batIndex);
						double mouseX = e.getX()-cam.getX();
						if(mouseX > GamePanel.WIDTH/2) {
							mouseX = GamePanel.WIDTH/2;
						}
						bat.setX((mouseX)/worldMatrix.getScaleX());
						bat.setY((e.getY()-cam.getY())/worldMatrix.getScaleY());
					}
				}
			});
		}
		
		public void disableBat(){
			if(getMouseListeners().length != 0 && getMouseMotionListeners().length != 0){
				removeMouseListener(getMouseListeners()[0]);
				removeMouseMotionListener(getMouseMotionListeners()[0]);				
			}
		}
		
		public void moveCamera(Camera cam) {
			requestFocus();
			
			keyCamera = new KeyAdapter() {
				boolean enabledCam;
				public void keyPressed(KeyEvent e) {
					enabledCam  = gsm.getCamFixed();
					if (e.getKeyCode() == KeyEvent.VK_C) {
						if(enabledCam) {
							gsm.setCamFixed(false);
						} else {
							gsm.setCamFixed(true);
							cam.setX(0);
							cam.setY(0);
						}
		            }
					
					if (e.getKeyCode() == KeyEvent.VK_LEFT) {
						if(!enabledCam) {
							cam.moveX(50);
						}
					}

					if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
						if(!enabledCam) {
							cam.moveX(-50);
						}
		            }
					
					if (e.getKeyCode() == KeyEvent.VK_UP) {
						if(!enabledCam) {
							cam.moveY(50);
						}
					}

					if (e.getKeyCode() == KeyEvent.VK_DOWN) {
						if(!enabledCam) {
							cam.moveY(-50);
						}
		            }
				}
			};
			
			addKeyListener(keyCamera);
		}
		
		public void disableCamera(){
			removeKeyListener(keyCamera);
		}
		
		public void resetBall() {
			requestFocus();
			
			keyBallReset = new KeyAdapter() {
				public void keyPressed(KeyEvent e) {
					if (e.getKeyCode() == KeyEvent.VK_SPACE) {
						if(gsm.getlanded()) {
							gsm.resetBall();
						}
					}
				}
			};
			
			addKeyListener(keyBallReset);
		}
		
		public void disableResetBall() {
			removeKeyListener(keyBallReset);		
		}
	}
	
	/**
	 * Méthode qui permet de jouer et répéter la musique de fond
	 */
	private void playBGM() {
		BGM.loop();
		BGM.play();
	}
	
	/**
	 * Méthode qui permet d'interrompre la musique de fond
	 */
	private void stopBGM() {
		BGM.stop();
	}

}
