/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.schovjan.musictrain.manager;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.tika.Tika;

/**
 *
 * @author schovjan
 */
public class AudioManager {

	private static final Logger LOG = Logger.getLogger(AudioManager.class.getName());

	public AudioManager() {
	}

	/**
	 * Vyhleda ve slozce audio stopu a otevre ji v prehravaci
	 *
	 * @param selectedFile
	 * @return true pokud nalezne audio, jinak vraci false
	 * @throws IOException
	 */
	public boolean openAudio(File selectedFile) throws IOException {
		File audio = findAndLoadAudio(selectedFile);
		if (audio != null) {
			Desktop.getDesktop().open(audio);
			return true;
		}
		return false;
	}

	private File findAndLoadAudio(File folder) {
		Tika tika = new Tika();
		for (File f : folder.listFiles()) {
			try {
				if (tika.detect(f).contains("audio")) {
					return f;
				}
			} catch (IOException ex) {
				LOG.log(Level.SEVERE, null, ex);
			}
		}
		return null;
	}

	/**
	 * Spusti prohlizec na strance youtube a vysledky pro hledani nazvu skladby
	 *
	 * @param selectedFile
	 * @throws IOException
	 */
	public void findInYoutube(File selectedFile) throws IOException {
		//vyhledani na youtube
		try {
			Desktop.getDesktop().browse(new URI("https://www.youtube.com/results?q=" + selectedFile.getName()));
		} catch (URISyntaxException ex) {
			LOG.log(Level.SEVERE, null, ex);
		}
	}

}
