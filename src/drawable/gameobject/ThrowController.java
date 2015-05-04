package drawable.gameobject;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;

import javax.swing.JPanel;
import javax.swing.event.EventListenerList;

import util.BallSpeedListener;

/**
 * Panneau de contrôle qui permet de modifier la vitesse initiale et la direction de projection de la balle.
 * @author Alexandre
 * @version 04-05-2015
 */
public class ThrowController extends JPanel{
	private static final long serialVersionUID = 1L;
	public static final int SCREEN_SIZE = 100;
	private final double DOT_SIZE = 15;
	private Ellipse2D.Double dot;
	
	private boolean selected = false;
	private double translateX, translateY;
	private double lastX, lastY;
	private Shape dotTransf;
	
	private final EventListenerList REGISTERED_LISTENERS = new EventListenerList();
	
	/**
	 * <b>Constructeur</b>
	 * <p>Créer le panneau<p>
	 * @param screenX la position en x du panneau
	 * @param screenY la position en y du panneau
	 */
	public ThrowController(int screenX, int screenY) {
		this.setPreferredSize(new Dimension(SCREEN_SIZE, SCREEN_SIZE));
		setBounds(screenX, screenY, SCREEN_SIZE, SCREEN_SIZE);
		setBackground(new Color(0,0,0,200));
		
		dot = new Ellipse2D.Double(SCREEN_SIZE/2-DOT_SIZE/2, SCREEN_SIZE/2-DOT_SIZE/2, DOT_SIZE, DOT_SIZE);
		
		addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				if (selected) {  // de petite amélioration à faire
					if(isInBoundX()) {
						if(e.getX() > 0 && e.getX() < SCREEN_SIZE){
							translateX += e.getX() - lastX;
							lastX = e.getX();
						}
						for(BallSpeedListener listener : REGISTERED_LISTENERS.getListeners(BallSpeedListener.class)){
							listener.setSpeedX(Math.abs((SCREEN_SIZE-(dotTransf.getBounds2D().getX()+dotTransf.getBounds2D().getWidth()/2))/SCREEN_SIZE));;
						}
					} else {
						if(e.getX() > SCREEN_SIZE/2) {
							translateX = SCREEN_SIZE/2;
						} else {
							translateX = -SCREEN_SIZE/2;
						}
					}

					if(isInBoundY()) {
						if(e.getY() > 0 && e.getY() < SCREEN_SIZE){
							translateY += e.getY() - lastY;
							lastY = e.getY();
						}
						for(BallSpeedListener listener : REGISTERED_LISTENERS.getListeners(BallSpeedListener.class)){
							listener.setSpeedY((Math.abs(SCREEN_SIZE-(dotTransf.getBounds2D().getY()+dotTransf.getBounds2D().getHeight()/2)))/SCREEN_SIZE);;
						}
					} else {
						if(e.getY() > SCREEN_SIZE/2) {
							translateY = SCREEN_SIZE/2;
						} else {
							translateY = -SCREEN_SIZE/2;
						}
					}
					repaint();
				}
			
			}
		});	
		
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (dotTransf.contains(e.getX(), e.getY())){
					selected = true;
					lastX = e.getX();
					lastY = e.getY();
				}
			}
			
			@Override
			public void mouseReleased(MouseEvent arg0) {
				selected = false;
			}
		});
	}
	
	/**
	 * Méthode de dessin du panneau
	 * @param g ({@link Graphics}) Le contexte graphique du panneau.
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		g2d.setColor(Color.cyan);
		AffineTransform affLos = new AffineTransform();
		affLos.translate(translateX, translateY);
		
		dotTransf = affLos.createTransformedShape(dot);
		g2d.fill(dotTransf);
	}
	
	/**
	 * Méthode qui permet de vérifier si le composant de curseur se situe entre les limites en x.
	 * @return boolean
	 */
	public boolean isInBoundX() {
		return dotTransf.getBounds2D().getX()+dotTransf.getBounds2D().getWidth()/2 >= 0 && 
				dotTransf.getBounds2D().getX()+dotTransf.getBounds2D().getWidth()/2 <= SCREEN_SIZE;
				
	}
	
	/**
	 * Méthode qui permet de vérifier si le composant de curseur se situe entre les limites en y.
	 * @return boolean
	 */
	public boolean isInBoundY() {
		return dotTransf.getBounds2D().getY()+dotTransf.getBounds2D().getHeight()/2 >= 0 &&
				dotTransf.getBounds2D().getY()+dotTransf.getBounds2D().getHeight()/2 <= SCREEN_SIZE;
	}
	
	/**
	 * Permet d'ajouter un écouteur pour modifier la vitesse initiale de la balle.
	 * @param l L'écouteur de balle.
	 */
	public void addInitSpeedListener(BallSpeedListener l) {
		REGISTERED_LISTENERS.add(BallSpeedListener.class, l);
	}
}
