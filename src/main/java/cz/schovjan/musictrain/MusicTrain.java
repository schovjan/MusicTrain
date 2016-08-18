/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.schovjan.musictrain;

import com.sun.glass.events.KeyEvent;
import cz.schovjan.musictrain.manager.AudioManager;
import cz.schovjan.musictrain.manager.ImageManager;
import cz.schovjan.musictrain.manager.SettingsManager;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import org.imgscalr.Scalr;

/**
 *
 * http://stackoverflow.com/questions/11959758/java-maintaining-aspect-ratio-of-jpanel-background-image/11959928#11959928
 *
 * @author schovanek
 */
public class MusicTrain {

	private static final String ACTION_EXIT = "EXIT";
	private static final String ACTION_HELP = "HELP";
	private static final String ACTION_OPEN = "OPEN";
	private static final String ACTION_NEXT = "NEXT";
	private static final String ACTION_PREVIOUS = "PREVIOUS";

	private final SettingsManager settingsManager;
	private final AudioManager audioManager;
	private final ImageManager imageManager;
	private JFrame frame;
	private int imgIndexLeft = -2;
	private int imgIndexRight = -1;
	private List<BufferedImage> images;
	private JComponent imgComponent;

	public MusicTrain() throws IOException {
		settingsManager = new SettingsManager();
		imageManager = new ImageManager();
		audioManager = new AudioManager();
		showDialogToFindFolder();
//	showHelpDialog();
	}

	private void createAndShowGui() {
		frame = new JFrame("MusicTrain");
		frame.setUndecorated(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setBackground(Color.BLACK);

		addBindings();

		imgComponent = new JComponent() {
		};
		frame.add(imgComponent);

		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice gs = ge.getDefaultScreenDevice();
		gs.setFullScreenWindow(frame);
		frame.validate();

		imgIndexLeft = -2;
		imgIndexRight = -1;
	}

	/**
	 * Pridani udalosti klaves
	 */
	private void addBindings() {
		InputMap inputMap = frame.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		ActionMap actionMap = frame.getRootPane().getActionMap();

		//help - napoveda
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_H, 0), ACTION_HELP);
		actionMap.put(ACTION_HELP, new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showHelpDialog();
			}
		});

		//konec - escape
		inputMap.put(KeyStroke.getKeyStroke("ESCAPE"), ACTION_EXIT);
		actionMap.put(ACTION_EXIT, new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});

		//otevreni dialogu pro nalezeni slozky s obrazky
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_O, 0), ACTION_OPEN);
		actionMap.put(ACTION_OPEN, new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					showDialogToFindFolder();
				} catch (IOException ex) {
					Logger.getLogger(MusicTrain.class.getName()).log(Level.SEVERE, null, ex);
				}
			}
		});

		//dalsi obrazek - mezernik, vpravo
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), ACTION_NEXT);
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), ACTION_NEXT);
		actionMap.put(ACTION_NEXT, new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				loadNextImage();
			}
		});

		//predchozi obrazek - vlevo
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), ACTION_PREVIOUS);
		actionMap.put(ACTION_PREVIOUS, new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				loadPreviousImage();
			}
		});
	}

	private void showHelpDialog() {

	}

	private void showDialogToFindFolder() throws IOException {
		if (frame != null) {
			frame.setVisible(false);
			frame = null;
		}
		images = null;

		JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(new File(settingsManager.getSettings().getFolderPath()));
		chooser.setDialogTitle("Vyhledat slozku s obrazky");
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		chooser.setAcceptAllFileFilterUsed(false);

		if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			settingsManager.getSettings().setFolderPath(chooser.getCurrentDirectory().getCanonicalPath());
			settingsManager.saveSettings();
			images = imageManager.loadImages(chooser.getSelectedFile());
			createAndShowGui();

			if (Desktop.isDesktopSupported()) {
				boolean audioExists = audioManager.openAudio(chooser.getSelectedFile());
				if (!audioExists) {
					audioManager.findInYoutube(chooser.getSelectedFile());
				}
			}
		} else {
			System.exit(0);
		}
	}

	private void loadNextImage() {
		if (Math.min(imgIndexLeft + 2, imgIndexRight + 2) >= images.size()) {
			return;
		}
		if (imgIndexLeft > imgIndexRight) {
			//zmena vpravo
			imgIndexRight = imgIndexRight + 2;
			paintImage(images.get(imgIndexRight), imgComponent.getWidth() / 2, 0);
		} else {
			//zmena vlevo
			imgIndexLeft = imgIndexLeft + 2;
			paintImage(images.get(imgIndexLeft), 0, 0);
		}
	}

	private void loadPreviousImage() {
		if (Math.max(imgIndexLeft - 2, imgIndexRight - 2) < 0) {
			return;
		}
		if (imgIndexLeft > imgIndexRight) {
			//zmena vlevo
			imgIndexLeft = imgIndexLeft - 2;
			paintImage(images.get(imgIndexLeft), 0, 0);
		} else {
			//zmena vpravo
			imgIndexRight = imgIndexRight - 2;
			paintImage(images.get(imgIndexRight), imgComponent.getWidth() / 2, 0);
		}
	}

	private void paintImage(BufferedImage img, int x, int y) {
		Dimension scaleSize = imageManager.getScaleSize(img, imgComponent.getSize());
		img = Scalr.resize(img, scaleSize.width, scaleSize.height); // Scale image
		imgComponent.getGraphics().clearRect(x, y, java.awt.Toolkit.getDefaultToolkit().getScreenSize().width / 2, java.awt.Toolkit.getDefaultToolkit().getScreenSize().height);
		imgComponent.getGraphics().drawImage(
						img,
						x,
						y,
						scaleSize.width,
						scaleSize.height,
						imgComponent);
	}

	public static void main(String[] args) throws IOException {
		// Use the event dispatch thread to build the UI for thread-safety.
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					new MusicTrain();
				} catch (IOException ex) {
					Logger.getLogger(MusicTrain.class.getName()).log(Level.SEVERE, null, ex);
				}
			}
		});
	}

}
