package drawable.gameobject;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JToggleButton;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

import util.BallSpeedListener;
import util.BufferedImageLoader;
import util.OptionsListener;
import application.GamePanel;
import drawable.gameobject.ball.FastBall;
import drawable.gameobject.ball.NormalBall;
import drawable.gameobject.ball.SinusBall;
import drawable.util.Button;

/**
 * Panneau de contrôle des différents paramètres.
 * @author Alexandre Hua
 * @version 04-05-2015
 */
public class ParameterPane extends JPanel{
	
	private static final long serialVersionUID = 1L;
	private final int SCREEN_X = GamePanel.WIDTH/20;
	private final int SCREEN_Y = GamePanel.HEIGHT/10;
	private final int SCRREN_WIDTH = GamePanel.WIDTH*18/20;
	private final int SCREEN_HEIGHT = GamePanel.HEIGHT*8/10;
	private final EventListenerList REGISTERED_LISTENERS = new EventListenerList();
	private final int SPACING = 25;
	private final int TITLED_BORDER_SPACING = 20;
	private final int BTN_EXIT_SIZE = 50;
	private final int BTN_SIZE = 80;
	private final int LABEL_WIDTH = 2*ThrowController.SCREEN_SIZE;
	private final int LABEL_HEIGHT = ThrowController.SCREEN_SIZE/2;
	private final int SLIDER_WIDTH = 2*ThrowController.SCREEN_SIZE;
	private final int SLIDER_HEIGHT = LABEL_HEIGHT;
	private final int FIRST_POS = TITLED_BORDER_SPACING;
	
	private final int maxAmplitude = SinusBall.MAX_AMPLITUDE;

	private double intensityX = 0.5;
	private double intensityY = 0.5;
	private double lambda = 60; //longueur d'onde de la balle sinusoidale
	private double T = 3; //Période de la balle sinusoidale
	private double amplitude = maxAmplitude*0.5;
	private double mass = 0.500; //masse de la balle en kilogrammes
	private double chargeSign = 1;
	private double charge = 4*Math.pow(10, -3);
	private double otherSpdX = NormalBall.MAX_SPEED_X/2;
	private double otherSpdY = NormalBall.MAX_SPEED_Y/2;
	private double fastSpdX = FastBall.MAX_SPEED_X/2;
	private double fastSpdY = FastBall.MAX_SPEED_Y/2;
	
	
	/**
	 * <b>Constructeur</b>
	 * <p>Initialiser les différentes propriétés et composants du panneau<p>
	 * @param components structure de donnée qui détient tous les composants Swing
	 */
	public ParameterPane(ArrayList<Component> components) {
		this.setPreferredSize(new Dimension(SCRREN_WIDTH, SCREEN_HEIGHT));
		setBounds(SCREEN_X, SCREEN_Y, SCRREN_WIDTH, SCREEN_HEIGHT);
		setLayout(null);
		setBackground(new Color(0,0,0,200));
		
		Button exit;
		JLabel lblIntensityX; //afficheur de vitesse x de la balle
		JLabel lblIntensityY; //afficheur de vitesse y de la balle
		ThrowController controller; //joystick pour controller la direction et la force de la lancée
		JSlider sldLambda;
		JSlider sldPeriod;
		JSlider sldAmplitude;
		JSpinner spnMass;
		JLabel lblLambda;
		JLabel lblPeriod;
		JLabel lblMass;
		JLabel lblAmplitude;
		JToggleButton btnCharge;
		JSlider sldCharge;
		JLabel lblNormalSpdX;
		JLabel lblNormalSpdY;
		JLabel lblFastSpdX;
		JLabel lblFastSpdY;
		JLabel lblCharge;
		JPanel speedPane;
		JPanel sinusBallPane;
		JPanel massPane;
		JPanel normalBallPane;
		JPanel fastBallPane;
		JPanel electricBallPane;
		
		

		int speedPaneWidth = ThrowController.SCREEN_SIZE+LABEL_WIDTH+2*TITLED_BORDER_SPACING;
		int speedPaneHeight = ThrowController.SCREEN_SIZE+2*TITLED_BORDER_SPACING;
		int speedPaneX = SCRREN_WIDTH-speedPaneWidth-3*SPACING;
		int speedPaneY = 2*SPACING;
		int labelX = FIRST_POS + ThrowController.SCREEN_SIZE;
		Font lblFont = new Font("Time New Roman", Font.PLAIN, 30);
		Color textColor = Color.WHITE;
		Color bgc = new Color(0,0,0,150);
		
		
		lblIntensityX = new JLabel("", SwingConstants.CENTER);
		lblIntensityX.setBounds(labelX, FIRST_POS, LABEL_WIDTH, LABEL_HEIGHT);
		lblIntensityX.setBorder(LineBorder.createGrayLineBorder());
		lblIntensityX.setForeground(textColor);
		lblIntensityX.setText("X: %" + intensityX*100);
		lblIntensityX.setFont(lblFont);
		
		lblIntensityY = new JLabel("", SwingConstants.CENTER);
		lblIntensityY.setBounds(labelX, FIRST_POS+ThrowController.SCREEN_SIZE/2, LABEL_WIDTH, LABEL_HEIGHT);
		lblIntensityY.setBorder(LineBorder.createGrayLineBorder());
		lblIntensityY.setForeground(textColor);
		lblIntensityY.setText("Y: %" + intensityY*100);
		lblIntensityY.setFont(lblFont);
		
		
		

		int sldPeriodY = FIRST_POS+LABEL_HEIGHT;
		int sldAmplitudeY = sldPeriodY+LABEL_HEIGHT;
		int sinusBallPaneWidth = SLIDER_WIDTH+LABEL_WIDTH+2*TITLED_BORDER_SPACING;
		int sinusBallPaneHeight = 3*LABEL_HEIGHT+2*TITLED_BORDER_SPACING;
		int sinusBallPaneX = SCRREN_WIDTH-sinusBallPaneWidth-3*SPACING;
		int sinusBallPaneY = speedPaneY+speedPaneHeight+SPACING;
		
		lblLambda = new JLabel("", SwingConstants.CENTER);
		lblLambda.setBounds(FIRST_POS+SLIDER_WIDTH, FIRST_POS, LABEL_WIDTH, LABEL_HEIGHT);
		lblLambda.setBorder(LineBorder.createGrayLineBorder());
		lblLambda.setForeground(textColor);
		lblLambda.setText("lam: " + lambda + " m");
		lblLambda.setFont(lblFont);
		
		lblPeriod = new JLabel("", SwingConstants.CENTER);
		lblPeriod.setBounds(FIRST_POS+SLIDER_WIDTH, sldPeriodY, LABEL_WIDTH, LABEL_HEIGHT);
		lblPeriod.setBorder(LineBorder.createGrayLineBorder());
		lblPeriod.setForeground(textColor);
		lblPeriod.setText("T: " + T + " s");
		lblPeriod.setFont(lblFont);
		
		lblAmplitude = new JLabel("", SwingConstants.CENTER);
		lblAmplitude.setBounds(FIRST_POS+SLIDER_WIDTH, sldAmplitudeY, LABEL_WIDTH, LABEL_HEIGHT);
		lblAmplitude.setBorder(LineBorder.createGrayLineBorder());
		lblAmplitude.setForeground(textColor);
		lblAmplitude.setText(amplitude+" m");
		lblAmplitude.setFont(lblFont);
		
		
		sldLambda = new JSlider();
		sldLambda.setBounds(FIRST_POS, FIRST_POS, SLIDER_WIDTH, SLIDER_HEIGHT);
		sldLambda.setBackground(bgc);
		sldLambda.setValue((int)lambda);
		sldLambda.setMaximum((int)GamePanel.REAL_SIZE);
		sldLambda.setMinimum(20);
		sldLambda.setMajorTickSpacing(10);
		sldLambda.setPaintTicks(true);
		sldLambda.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				lambda = sldLambda.getValue();
				lblLambda.setText("lam: " + lambda + " m");
			}
		});
		
		sldPeriod = new JSlider();
		sldPeriod.setBounds(FIRST_POS, sldPeriodY, SLIDER_WIDTH, SLIDER_HEIGHT);
		sldPeriod.setBackground(bgc);
		sldPeriod.setValue((int)T);
		sldPeriod.setMaximum(10);
		sldPeriod.setMinimum(1);
		sldPeriod.setMajorTickSpacing(1);
		sldPeriod.setPaintTicks(true);
		sldPeriod.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				T = sldPeriod.getValue();
				lblPeriod.setText("T: " + T + " s");
			}
		});
		
		sldAmplitude = new JSlider();
		sldAmplitude.setBounds(FIRST_POS, sldAmplitudeY, SLIDER_WIDTH, SLIDER_HEIGHT);
		sldAmplitude.setBackground(bgc);
		sldAmplitude.setValue((int)amplitude);
		sldAmplitude.setMaximum(maxAmplitude);
		sldAmplitude.setMinimum(0);
		sldAmplitude.setMajorTickSpacing(1);
		sldAmplitude.setPaintTicks(true);
		sldAmplitude.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				amplitude = sldAmplitude.getValue();
				lblAmplitude.setText(amplitude+" m");
			}
		});
		
		sinusBallPane = new JPanel();
		sinusBallPane.setPreferredSize(new Dimension(sinusBallPaneWidth, sinusBallPaneHeight));
		sinusBallPane.setForeground(textColor);
		sinusBallPane.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Balle Sinusoidale", TitledBorder.CENTER, TitledBorder.TOP, null, textColor));
		sinusBallPane.setBounds(sinusBallPaneX, sinusBallPaneY, sinusBallPaneWidth, sinusBallPaneHeight);
		sinusBallPane.setBackground(new Color(0,0,0,0));
		sinusBallPane.setLayout(null);
		sinusBallPane.add(lblLambda);
		sinusBallPane.add(lblPeriod);
		sinusBallPane.add(lblAmplitude);
		sinusBallPane.add(sldLambda);
		sinusBallPane.add(sldPeriod);
		sinusBallPane.add(sldAmplitude);
		
		
		
		
		
		int spnMassWidth = SLIDER_WIDTH;
		int spnMassHeight = LABEL_HEIGHT;
		int massPaneWidth = SLIDER_WIDTH+LABEL_WIDTH+2*TITLED_BORDER_SPACING;
		int massPaneHeight = LABEL_HEIGHT+2*TITLED_BORDER_SPACING;
		int massPaneX = SCRREN_WIDTH-massPaneWidth-3*SPACING;
		int massPaneY = sinusBallPaneY+sinusBallPaneHeight+SPACING;
		
		lblMass = new JLabel("", SwingConstants.CENTER);
		lblMass.setBounds(FIRST_POS+SLIDER_WIDTH, FIRST_POS, LABEL_WIDTH, LABEL_HEIGHT);
		lblMass.setBorder(LineBorder.createGrayLineBorder());
		lblMass.setForeground(textColor);
		lblMass.setText("grammes");
		lblMass.setFont(lblFont);
		
		spnMass = new JSpinner();
		spnMass.setBounds(FIRST_POS, FIRST_POS, spnMassWidth, spnMassHeight);
		spnMass.setModel(new SpinnerNumberModel(mass*1000, 0.0, 2000.0, 1.0));
		spnMass.getEditor().getComponent(0).setBackground(Color.BLACK);
		spnMass.getEditor().getComponent(0).setFont(lblFont);
		spnMass.getEditor().getComponent(0).setForeground(textColor);
		spnMass.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				mass = (double)spnMass.getValue()/1000;
			}
		});
		
		massPane = new JPanel();
		massPane.setPreferredSize(new Dimension(massPaneWidth, massPaneHeight));
		massPane.setForeground(textColor);
		massPane.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Masse de Balle", TitledBorder.CENTER, TitledBorder.TOP, null, textColor));
		massPane.setBounds(massPaneX, massPaneY, massPaneWidth, massPaneHeight);
		massPane.setBackground(new Color(0,0,0,0));
		massPane.setLayout(null);
		massPane.add(lblMass);
		massPane.add(spnMass);
		
		
		
		
		int sldChargeY = FIRST_POS+BTN_SIZE;
		int ElectricBallPaneWidth = SLIDER_WIDTH+LABEL_WIDTH+2*TITLED_BORDER_SPACING;
		int ElectricBallPaneHeight = BTN_SIZE+LABEL_HEIGHT+2*TITLED_BORDER_SPACING;
		int ElectricBallPaneX = 3*SPACING;
		int ElectricBallPaneY = sinusBallPaneY;
		
		btnCharge = new JToggleButton();
		btnCharge.setBounds(FIRST_POS, FIRST_POS, BTN_SIZE, BTN_SIZE);
		btnCharge.setToolTipText("<html>Balle Normale<br>"
				+ "Trajectoire quadratique<br>"
				+ "Particularité : Aucune<br>"
				+ "Vitesse minimale: 50 m/s<br>"
				+ "Vitesse maximale: 50 m/s<html>");
		BufferedImage imgPositiveBall = new BufferedImageLoader().loadImage("/game/ElectricBall_Positive.png");
		BufferedImage imgNegativeBall = new BufferedImageLoader().loadImage("/game/ElectricBall_Negative.png");
		Image iconPositiveBall = imgPositiveBall.getScaledInstance(btnCharge.getWidth(), btnCharge.getHeight(), Image.SCALE_SMOOTH);
		Image iconNegativeBall = imgNegativeBall.getScaledInstance(btnCharge.getWidth(), btnCharge.getHeight(), Image.SCALE_SMOOTH);
		btnCharge.setIcon(new ImageIcon(iconPositiveBall));
		btnCharge.setBorderPainted(false);
		btnCharge.setContentAreaFilled(false);
		btnCharge.setFocusPainted(false);
		btnCharge.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(btnCharge.isSelected()) {
					chargeSign = -1;
					btnCharge.setIcon(new ImageIcon(iconNegativeBall));
				} else {
					chargeSign = 1;
					btnCharge.setIcon(new ImageIcon(iconPositiveBall));
				}
			}
		});
		
		lblCharge = new JLabel("", SwingConstants.CENTER);
		lblCharge.setBounds(FIRST_POS+SLIDER_WIDTH, sldChargeY, LABEL_WIDTH, LABEL_HEIGHT);
		lblCharge.setBorder(LineBorder.createGrayLineBorder());
		lblCharge.setForeground(textColor);
		lblCharge.setText(charge/Math.pow(10, -3)+"x10E-3 C");
		lblCharge.setFont(lblFont);
		
		sldCharge = new JSlider();
		sldCharge.setBounds(FIRST_POS, sldChargeY, SLIDER_WIDTH, SLIDER_HEIGHT);
		sldCharge.setBackground(bgc);
		sldCharge.setValue((int)(charge/Math.pow(10, -3)));
		sldCharge.setMaximum(10);
		sldCharge.setMinimum(1);
		sldCharge.setMajorTickSpacing(1);
		sldCharge.setPaintTicks(true);
		sldCharge.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				charge = sldCharge.getValue()*Math.pow(10, -3);
				lblCharge.setText(sldCharge.getValue()+"x10E-3 C");
			}
		});
		
		electricBallPane = new JPanel();
		electricBallPane.setPreferredSize(new Dimension(massPaneWidth, massPaneHeight));
		electricBallPane.setForeground(textColor);
		electricBallPane.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Balle Électrique", TitledBorder.CENTER, TitledBorder.TOP, null, textColor));
		electricBallPane.setBounds(ElectricBallPaneX, ElectricBallPaneY, ElectricBallPaneWidth, ElectricBallPaneHeight);
		electricBallPane.setBackground(new Color(0,0,0,0));
		electricBallPane.setLayout(null);
		electricBallPane.add(btnCharge);
		electricBallPane.add(sldCharge);
		electricBallPane.add(lblCharge);
		
		
		

		int NormalBallPaneWidth = LABEL_WIDTH+2*TITLED_BORDER_SPACING;
		int NormalBallPaneHeight = 2*LABEL_HEIGHT+2*TITLED_BORDER_SPACING;
		int NormalBallPaneX = 3*SPACING;
		int NormalBallPaneY = speedPaneY;
		
		lblNormalSpdX = new JLabel("", SwingConstants.CENTER);
		lblNormalSpdX.setBounds(FIRST_POS, FIRST_POS, LABEL_WIDTH, LABEL_HEIGHT);
		lblNormalSpdX.setBorder(LineBorder.createGrayLineBorder());
		lblNormalSpdX.setForeground(textColor);
		lblNormalSpdX.setText(otherSpdX+"m/s en X");
		lblNormalSpdX.setFont(lblFont);
		
		lblNormalSpdY = new JLabel("", SwingConstants.CENTER);
		lblNormalSpdY.setBounds(FIRST_POS, FIRST_POS+LABEL_HEIGHT, LABEL_WIDTH, LABEL_HEIGHT);
		lblNormalSpdY.setBorder(LineBorder.createGrayLineBorder());
		lblNormalSpdY.setForeground(textColor);
		lblNormalSpdY.setText(otherSpdY+"m/s en Y");
		lblNormalSpdY.setFont(lblFont);
		
		normalBallPane = new JPanel();
		normalBallPane.setPreferredSize(new Dimension(massPaneWidth, massPaneHeight));
		normalBallPane.setForeground(textColor);
		normalBallPane.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Balle Normale", TitledBorder.CENTER, TitledBorder.TOP, null, textColor));
		normalBallPane.setBounds(NormalBallPaneX, NormalBallPaneY, NormalBallPaneWidth, NormalBallPaneHeight);
		normalBallPane.setBackground(new Color(0,0,0,0));
		normalBallPane.setLayout(null);
		normalBallPane.add(lblNormalSpdX);
		normalBallPane.add(lblNormalSpdY);
		
		
		
		

		int fastBallPaneWidth = LABEL_WIDTH+2*TITLED_BORDER_SPACING;
		int fastBallPaneHeight = 2*LABEL_HEIGHT+2*TITLED_BORDER_SPACING;
		int fastBallPaneX = 3*SPACING+NormalBallPaneWidth+SPACING;
		int fastBallPaneY = speedPaneY;
		
		lblFastSpdX = new JLabel("", SwingConstants.CENTER);
		lblFastSpdX.setBounds(FIRST_POS, FIRST_POS, LABEL_WIDTH, LABEL_HEIGHT);
		lblFastSpdX.setBorder(LineBorder.createGrayLineBorder());
		lblFastSpdX.setForeground(textColor);
		lblFastSpdX.setText(fastSpdX+"m/s en X");
		lblFastSpdX.setFont(lblFont);
		
		lblFastSpdY = new JLabel("", SwingConstants.CENTER);
		lblFastSpdY.setBounds(FIRST_POS, FIRST_POS+LABEL_HEIGHT, LABEL_WIDTH, LABEL_HEIGHT);
		lblFastSpdY.setBorder(LineBorder.createGrayLineBorder());
		lblFastSpdY.setForeground(textColor);
		lblFastSpdY.setText(fastSpdY+"m/s en Y");
		lblFastSpdY.setFont(lblFont);
		
		fastBallPane = new JPanel();
		fastBallPane.setPreferredSize(new Dimension(massPaneWidth, massPaneHeight));
		fastBallPane.setForeground(textColor);
		fastBallPane.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Balle Rapide", TitledBorder.CENTER, TitledBorder.TOP, null, textColor));
		fastBallPane.setBounds(fastBallPaneX, fastBallPaneY, fastBallPaneWidth, fastBallPaneHeight);
		fastBallPane.setBackground(new Color(0,0,0,0));
		fastBallPane.setLayout(null);
		fastBallPane.add(lblFastSpdX);
		fastBallPane.add(lblFastSpdY);
		
		
		
		
		controller = new ThrowController(FIRST_POS, FIRST_POS);
		controller.addInitSpeedListener(new BallSpeedListener() {
			public void setSpeedX(double intensity) {
				intensityX = intensity;
				lblIntensityX.setText("X: %" + String.format("%.2f", intensityX*100));
				
				otherSpdX = NormalBall.MAX_SPEED_X*intensityX;
				lblNormalSpdX.setText(otherSpdX+"m/s en X");
				
				fastSpdX = FastBall.MAX_SPEED_X*intensityX;
				lblFastSpdX.setText(fastSpdX+"m/s en X");
			}
			
			public void setSpeedY(double intensity) {
				intensityY = intensity;
				lblIntensityY.setText("Y: %" + String.format("%.2f", intensityY*100));
				
				otherSpdY = NormalBall.MAX_SPEED_Y*intensityY;
				lblNormalSpdY.setText(otherSpdY+"m/s en Y");
				
				fastSpdY = FastBall.MAX_SPEED_Y*intensityY;
				lblFastSpdY.setText(fastSpdY+"m/s en Y");
			}
		});
		
		speedPane = new JPanel();
		speedPane.setPreferredSize(new Dimension(speedPaneWidth, speedPaneHeight));
		speedPane.setForeground(textColor);
		speedPane.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Vitesse de Balle", TitledBorder.CENTER, TitledBorder.TOP, null, textColor));
		speedPane.setBounds(speedPaneX, speedPaneY, speedPaneWidth, speedPaneHeight);
		speedPane.setBackground(new Color(0,0,0,0));
		speedPane.setLayout(null);
		speedPane.add(controller);
		speedPane.add(lblIntensityX);
		speedPane.add(lblIntensityY);
		
		
		
		exit = new Button("", "/game/exit_");
		exit.setBounds(SCRREN_WIDTH-BTN_EXIT_SIZE, 0, BTN_EXIT_SIZE, BTN_EXIT_SIZE);
		exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for(Component component : components) {
					if(component.isVisible() || component instanceof BallSelectorPane) {
						component.setVisible(false);
					} else {
						component.setVisible(true);
					}
				}
				setInfoEnabled(true);
			}
		});
		

		
		
		
		add(exit);
		add(speedPane);
		add(sinusBallPane);
		add(massPane);
		add(electricBallPane);
		add(normalBallPane);
		add(fastBallPane);
	}
	
	/**
	 * Retourner l'intensité de la vitesse en x de la balle
	 * @return l'intensité de la vitesse en x de la balle
	 */
	public double getIntensityX() {
		return intensityX;
	}
	
	/**
	 * Modifier l'intensité de la vitesse en x de la balle
	 * @param intensityX nouvelle intensité de la vitesse en x de la balle
	 */
	public void setIntensityX(double intensityX) {
		this.intensityX = intensityX;
	}
	
	/**
	 * Retourner l'intensité de la vitesse en y de la balle
	 * @return l'intensité de la vitesse en y de la balle
	 */
	public double getIntensityY() {
		return intensityY;
	}
	
	/**
	 * Modifier l'intensité de la vitesse en y de la balle
	 * @param intensityY nouvelle intensité de la vitesse en y de la balle
	 */
	public void setIntensityY(double intensityY) {
		this.intensityY = intensityY;
	}
	
	/**
	 * Retourner la vitesse en x de la balle autre que la balle rapide.
	 * @return la vitesse en x de la balle.
	 */
	public double getOtherSpdX() {
		return otherSpdX;
	}
	
	/**
	 * Modifier la vitesse en x de la balle autre que la balle rapide.
	 * @param otherSpdX la nouvelle vitesse en x de la balle.
	 */
	public void setOtherSpdX(double otherSpdX) {
		this.otherSpdX = otherSpdX;
	}
	
	/**
	 * Retourner la vitesse en y de la balle autre que la balle rapide.
	 * @return la vitesse en y de la balle.
	 */
	public double getOtherSpdY() {
		return otherSpdY;
	}
	
	/**
	 * Modifier la vitesse en y de la balle autre que la balle rapide.
	 * @param otherSpdY la nouvelle vitesse en y de la balle.
	 */
	public void setOtherSpdY(double otherSpdY) {
		this.otherSpdY = otherSpdY;
	}
	
	/**
	 * Retourner la vitesse en x de la balle rapide.
	 * @return la vitesse en x de la balle rapide.
	 */
	public double getFastSpdX() {
		return fastSpdX;
	}
	
	/**
	 * Modifier la vitesse en x de la balle rapide.
	 * @param fastSpdX la nouvelle vitesse en x de la balle rapide.
	 */
	public void setFastSpdX(double fastSpdX) {
		this.fastSpdX = fastSpdX;
	}
	
	/**
	 * Retourner la vitesse en y de la balle rapide.
	 * @return la vitesse en y de la balle rapide.
	 */
	public double getFastSpdY() {
		return fastSpdY;
	}
	
	/**
	 * Modifier la vitesse en y de la balle rapide.
	 * @param fastSpdY la nouvelle vitesse en y de la balle rapide.
	 */
	public void setFastSpdY(double fastSpdY) {
		this.fastSpdY = fastSpdY;
	}

	/**
	 * Retourner le lambda de la balle sinusoidale
	 * @return lambda
	 */
	public double getLambda() {
		return lambda;
	}
	
	/**
	 * Modifier le lambda de la balle sinusoidale
	 * @param lambda nouveau lambda
	 */
	public void setLambda(double lambda) {
		this.lambda = lambda;
	}
	
	/**
	 * Retourner la période de la balle sinusoidale
	 * @return période
	 */
	public double getT() {
		return T;
	}
	
	/**
	 * Modifier la période de la balle sinusoidale
	 * @param t nouvelle période
	 */
	public void setT(double t) {
		T = t;
	}
	
	/**
	 * Retourner l'amplitude de la balle sinusoidale
	 * @return l'amplitude
	 */
	public double getAmplitude() {
		return amplitude;
	}
	
	/**
	 * Modifier l'amplitude de la balle sinusoidale
	 * @param amplitude nouvelle amplitude
	 */
	public void setAmplitude(double amplitude) {
		this.amplitude = amplitude;
	}
	
	/**
	 * Retourner la masse de la balle
	 * @return la masse
	 */
	public double getMass() {
		return mass;
	}
	
	/**
	 * Modifier la masse de la balle.
	 * @param mass nouvelle masse
	 */
	public void setMass(double mass) {
		this.mass = mass;
	}
	
	/**
	 * Retourner la charge de la balle électrique.
	 * @return la charge de la balle électrique.
	 */
	public double getCharge() {
		return charge*chargeSign;
	}
	
	/**
	 * Modifier la charge de la balle électrique.
	 * @param charge la charge de la balle électrique.
	 */
	public void setCharge(double charge) {
		this.charge = charge;
	}

	/**
	 * Permet d'ajouter un écouteur pour les changements d'option.
	 * @param l Un écouteur d'options.
	 */
	public void addOptionsListener(OptionsListener l){
		REGISTERED_LISTENERS.add(OptionsListener.class, l);
	}
	
	/**
	 * Modifier l'état boolean du panneau d'informations scientifiques
	 * @param enabled l'état boolean du panneau d'informations scientifiques
	 */
	private void setInfoEnabled(boolean enabled){
		for(OptionsListener listener : REGISTERED_LISTENERS.getListeners(OptionsListener.class)){
			listener.setInfoEnabled(enabled);
		}
	}
	
	
}
