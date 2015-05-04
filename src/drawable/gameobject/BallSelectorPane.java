package drawable.gameobject;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.event.EventListenerList;

import util.OptionsListener;
import application.GamePanel;
import drawable.gameobject.ball.BallType;
import drawable.util.Button;

/**
 * Panneau de contrôle du type de balle qui permet d'activer ou de désactiver un type de balle.
 * @author Alexandre Hua
 * @since 30-03-2015
 * @version 04-05-2015
 */
public class BallSelectorPane extends JPanel{
	
	private static final long serialVersionUID = 1L;
	private final int SCREEN_X = GamePanel.WIDTH/20;
	private final int SCREEN_Y = GamePanel.HEIGHT/10;
	private final int SCRREN_WIDTH = GamePanel.WIDTH*18/20;
	private final int SCREEN_HEIGHT = GamePanel.HEIGHT*8/10;	
	private final int SPACING = 50;
	private final int BTN_WIDTH = 400;
	private final int BTN_HEIGHT = 50;
	private final EventListenerList REGISTERED_LISTENERS = new EventListenerList();
	
	private final int BTN_EXIT_SIZE = 50;
	private final int BTN_SIZE = 100;
	
	/**
	 * <b>Constructeur</b>
	 * <p>Initialiser les différentes propriétés et composants du panneau<p>
	 * @param components structure de donnée qui détient tous les composants Swing
	 */
	public BallSelectorPane(ArrayList<Component> components) {
		this.setPreferredSize(new Dimension(SCRREN_WIDTH, SCREEN_HEIGHT));
		setBounds(SCREEN_X, SCREEN_Y, SCRREN_WIDTH, SCREEN_HEIGHT);
		setLayout(null);
		setBackground(new Color(0,0,0,200));
		
		JToggleButton btnNormalBall;
		JToggleButton btnFastBall;
		JToggleButton btnSinusBall;
		JToggleButton btnFrictionBall;
		JToggleButton btnElectricBall;
		Button btnEnabled;
		Button btnDisabled;
		Button exit;
		
		btnNormalBall = new JToggleButton();
		btnNormalBall.setBounds(SCRREN_WIDTH/5-BTN_SIZE/2, SCREEN_HEIGHT/4, BTN_SIZE, BTN_SIZE);
		btnNormalBall.setToolTipText("<html>Balle Normale<br>"
				+ "Trajectoire quadratique<br>"
				+ "Particularité : Aucune<br>"
				+ "Vitesse minimale: 50 m/s<br>"
				+ "Vitesse maximale: 50 m/s<html>");
		Image iconNormalBall = BallType.NORMAL_BALL.getImage().getScaledInstance(btnNormalBall.getWidth(), btnNormalBall.getHeight(), Image.SCALE_SMOOTH);
		Image iconNormalDisabledBall = BallType.NORMAL_BALL.getDisabledImage().getScaledInstance(btnNormalBall.getWidth(), btnNormalBall.getHeight(), Image.SCALE_SMOOTH);
		btnNormalBall.setIcon(new ImageIcon(iconNormalBall));
		btnNormalBall.setBorderPainted(false);
		btnNormalBall.setContentAreaFilled(false);
        btnNormalBall.setFocusPainted(false);
		btnNormalBall.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(btnNormalBall.isSelected()) {
					BallType.NORMAL_BALL.setEnabled(false);
					btnNormalBall.setIcon(new ImageIcon(iconNormalDisabledBall));
				} else {
					BallType.NORMAL_BALL.setEnabled(true);
					btnNormalBall.setIcon(new ImageIcon(iconNormalBall));
				}
			}
		});
		
		btnFastBall = new JToggleButton();
		btnFastBall.setBounds(SCRREN_WIDTH*2/5-BTN_SIZE/2, SCREEN_HEIGHT/4, BTN_SIZE, BTN_SIZE);
		btnFastBall.setToolTipText("<html>Balle Rapide<br>"
				+ "Trajectoire quadratique<br>"
				+ "Particularité : vitesse maximale plus grande<br>"
				+ "Vitesse minimale: 50 m/s<br>"
				+ "Vitesse maximale: 80 m/s<html>");
		Image iconFastBall = BallType.FAST_BALL.getImage().getScaledInstance(btnNormalBall.getWidth(), btnFastBall.getHeight(), Image.SCALE_SMOOTH);
		Image iconFastDisabledBall = BallType.FAST_BALL.getDisabledImage().getScaledInstance(btnNormalBall.getWidth(), btnFastBall.getHeight(), Image.SCALE_SMOOTH);
		btnFastBall.setIcon(new ImageIcon(iconFastBall));
		btnFastBall.setBorderPainted(false);
		btnFastBall.setContentAreaFilled(false);
        btnFastBall.setFocusPainted(false);
		btnFastBall.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(btnFastBall.isSelected()) {
					BallType.FAST_BALL.setEnabled(false);
					btnFastBall.setIcon(new ImageIcon(iconFastDisabledBall));
				} else {
					BallType.FAST_BALL.setEnabled(true);
					btnFastBall.setIcon(new ImageIcon(iconFastBall));
				}
			}
		});
		
		
		btnSinusBall = new JToggleButton();
		btnSinusBall.setBounds(SCRREN_WIDTH*3/5-BTN_SIZE/2, SCREEN_HEIGHT/4, BTN_SIZE, BTN_SIZE);
		btnSinusBall.setToolTipText("<html>Balle Sinusoidale<br>"
				+ "Trajectoire sinusoidale<br>"
				+ "Particularité : la trajectoire de la lancée dérive du comportement d'une onde<br>"
				+ "Vitesse minimale: 50 m/s<br>"
				+ "Vitesse maximale: 50 m/s<html>");
		Image iconSinusBall = BallType.SINUS_BALL.getImage().getScaledInstance(btnSinusBall.getWidth(), btnSinusBall.getHeight(), Image.SCALE_SMOOTH);
		Image iconSinusDisabledBall = BallType.SINUS_BALL.getDisabledImage().getScaledInstance(btnSinusBall.getWidth(), btnSinusBall.getHeight(), Image.SCALE_SMOOTH);
		btnSinusBall.setIcon(new ImageIcon(iconSinusBall));
		btnSinusBall.setBorderPainted(false);
		btnSinusBall.setContentAreaFilled(false);
		btnSinusBall.setFocusPainted(false);		
		btnSinusBall.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(btnSinusBall.isSelected()) {
					BallType.SINUS_BALL.setEnabled(false);
					btnSinusBall.setIcon(new ImageIcon(iconSinusDisabledBall));
				} else {
					BallType.SINUS_BALL.setEnabled(true);
					btnSinusBall.setIcon(new ImageIcon(iconSinusBall));
				}
			}
		});
		
		
		btnFrictionBall = new JToggleButton();
		btnFrictionBall.setBounds(SCRREN_WIDTH*4/5-BTN_SIZE/2, SCREEN_HEIGHT/4, BTN_SIZE, BTN_SIZE);
		btnFrictionBall.setToolTipText("<html>Balle de Friction<br>"
				+ "Trajectoire quadratique<br>"
				+ "Particularité : aucun rebond<br>"
				+ "Vitesse minimale: 50 m/s<br>"
				+ "Vitesse maximale: 50 m/s<html>");
		Image iconFrictionBall = BallType.FRICTION_BALL.getImage().getScaledInstance(btnFrictionBall.getWidth(), btnFrictionBall.getHeight(), Image.SCALE_SMOOTH);
		Image iconFrictionDisabledBall = BallType.FRICTION_BALL.getDisabledImage().getScaledInstance(btnFrictionBall.getWidth(), btnFrictionBall.getHeight(), Image.SCALE_SMOOTH);
		btnFrictionBall.setIcon(new ImageIcon(iconFrictionBall));
		btnFrictionBall.setBorderPainted(false);
		btnFrictionBall.setContentAreaFilled(false);
		btnFrictionBall.setFocusPainted(false);
		btnFrictionBall.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(btnFrictionBall.isSelected()) {
					BallType.FRICTION_BALL.setEnabled(false);
					btnFrictionBall.setIcon(new ImageIcon(iconFrictionDisabledBall));
				} else {
					BallType.FRICTION_BALL.setEnabled(true);
					btnFrictionBall.setIcon(new ImageIcon(iconFrictionBall));
				}
			}
		});
		
		
		btnElectricBall = new JToggleButton();
		btnElectricBall.setBounds(SCRREN_WIDTH/5-BTN_SIZE/2, SCREEN_HEIGHT*2/4, BTN_SIZE, BTN_SIZE);
		btnElectricBall.setToolTipText("<html>Balle Électrique<br>"
				+ "Trajectoire quadratique<br>"
				+ "Particularité : possède une charge et réagit avec une plaque électrique<br>"
				+ "Vitesse minimale: 50 m/s<br>"
				+ "Vitesse maximale: 50 m/s<html>");
		Image iconElectricBall = BallType.ELECTRIC_BALL.getImage().getScaledInstance(btnElectricBall.getWidth(), btnElectricBall.getHeight(), Image.SCALE_SMOOTH);
		Image iconElectricDisabledBall = BallType.ELECTRIC_BALL.getDisabledImage().getScaledInstance(btnElectricBall.getWidth(), btnElectricBall.getHeight(), Image.SCALE_SMOOTH);
		btnElectricBall.setIcon(new ImageIcon(iconElectricBall));
		btnElectricBall.setBorderPainted(false);
		btnElectricBall.setContentAreaFilled(false);
		btnElectricBall.setFocusPainted(false);
		btnElectricBall.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(btnElectricBall.isSelected()) {
					BallType.ELECTRIC_BALL.setEnabled(false);
					btnElectricBall.setIcon(new ImageIcon(iconElectricDisabledBall));
				} else {
					BallType.ELECTRIC_BALL.setEnabled(true);
					btnElectricBall.setIcon(new ImageIcon(iconElectricBall));
				}
			}
		});
		
		
		int btnEnabledX = SPACING;		
		int btnDisabledX = SCRREN_WIDTH-BTN_WIDTH-SPACING;
		int btnEnabledY = SPACING;
		
		btnEnabled = new Button("Activer tout", "/menu/"); //BUG : lorsqu'on désactive toutes les balles sauf une, après
		//avoir cliqué sur ce bouton, il ne fait que renvoyer toujours cette même balle
		btnEnabled.setBounds(btnEnabledX, btnEnabledY, BTN_WIDTH, BTN_HEIGHT);
		btnEnabled.addActionListener(new ActionListener() {		
			public void actionPerformed(ActionEvent e) {
				btnNormalBall.setSelected(false);
				BallType.NORMAL_BALL.setEnabled(true);
				btnNormalBall.setIcon(new ImageIcon(iconNormalBall));
				
				btnFastBall.setSelected(false);
				BallType.FAST_BALL.setEnabled(true);
				btnFastBall.setIcon(new ImageIcon(iconFastBall));
				
				btnSinusBall.setSelected(false);
				BallType.SINUS_BALL.setEnabled(true);
				btnSinusBall.setIcon(new ImageIcon(iconSinusBall));
				
				btnFrictionBall.setSelected(false);
				BallType.FRICTION_BALL.setEnabled(true);
				btnFrictionBall.setIcon(new ImageIcon(iconFrictionBall));
				
				btnElectricBall.setSelected(false);
				BallType.ELECTRIC_BALL.setEnabled(true);
				btnElectricBall.setIcon(new ImageIcon(iconElectricBall));
			}
		});
		
		btnDisabled = new Button("Désactiver tout", "/menu/");
		btnDisabled.setBounds(btnDisabledX, btnEnabledY, BTN_WIDTH, BTN_HEIGHT);
		btnDisabled.addActionListener(new ActionListener() {		
			public void actionPerformed(ActionEvent e) {
				btnNormalBall.setSelected(true);
				BallType.NORMAL_BALL.setEnabled(false);
				btnNormalBall.setIcon(new ImageIcon(iconNormalDisabledBall));
				
				btnFastBall.setSelected(true);
				BallType.FAST_BALL.setEnabled(false);
				btnFastBall.setIcon(new ImageIcon(iconFastDisabledBall));
				
				btnSinusBall.setSelected(true);
				BallType.SINUS_BALL.setEnabled(false);
				btnSinusBall.setIcon(new ImageIcon(iconSinusDisabledBall));
				
				btnFrictionBall.setSelected(true);
				BallType.FRICTION_BALL.setEnabled(false);
				btnFrictionBall.setIcon(new ImageIcon(iconFrictionDisabledBall));
				
				btnElectricBall.setSelected(true);
				BallType.ELECTRIC_BALL.setEnabled(false);
				btnElectricBall.setIcon(new ImageIcon(iconElectricDisabledBall));
			}
		});
		
		
		exit = new Button("", "/game/exit_");
		exit.setBounds(SCRREN_WIDTH-BTN_EXIT_SIZE, 0, BTN_EXIT_SIZE, BTN_EXIT_SIZE);
		exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for(Component component : components) {
					if(component.isVisible() || component instanceof ParameterPane) {
						component.setVisible(false);
					} else {
						component.setVisible(true);
					}
				}
				setInfoEnabled(true);
			}
		});
		
		add(btnNormalBall);
		add(btnFastBall);
		add(btnSinusBall);
		add(btnFrictionBall);
		add(btnElectricBall);
		add(exit);
		add(btnEnabled);
		add(btnDisabled);
	}
	
	/**
	 * Permet d'ajouter un écouteur pour les changements d'option.
	 * @param l Un écouteur d'options.
	 */
	public void addOptionsListener(OptionsListener l){
		REGISTERED_LISTENERS.add(OptionsListener.class, l);
	}
	
	/**
	 * Permet de réinitialiser la condition d'affichage des informations scientifiques, puisqu'elles 
	 * ont été masquées si elles étaient activées.
	 * @param enabled La nouvelle condition d'affichage.
	 */
	private void setInfoEnabled(boolean enabled){
		for(OptionsListener listener : REGISTERED_LISTENERS.getListeners(OptionsListener.class)){
			listener.setInfoEnabled(enabled);
		}
	}
	
}
