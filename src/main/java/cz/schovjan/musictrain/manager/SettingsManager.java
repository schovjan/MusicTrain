/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.schovjan.musictrain.manager;

import com.google.gson.Gson;
import cz.schovjan.musictrain.dto.Settings;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author schovanek
 */
public class SettingsManager {

	private static final Logger LOG = Logger.getLogger(SettingsManager.class.getName());

	private static final String APP_HOME_DIR_PATH = System.getProperty("user.home") + "/.music-train/";
	private static final String APP_SETTINGS_FILE_PATHNAME = APP_HOME_DIR_PATH + "application-settings";

	private Settings settings = null;

	public SettingsManager() {
	}

	/**
	 * Vraci existujici nastaveni, pokud neexistuje tak vraci defaulni
	 *
	 * @return
	 */
	private Settings loadSettings() {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(APP_SETTINGS_FILE_PATHNAME));
			Gson gson = new Gson();
			return gson.fromJson(br, Settings.class);
		} catch (FileNotFoundException ex) {
			return new Settings();
		} finally {
			try {
				if (br != null) {
					br.close();
				}
			} catch (IOException ex) {
				LOG.log(Level.SEVERE, null, ex);
			}
		}
	}

	public void saveSettings() {
		Gson gson = new Gson();
		String json = gson.toJson(settings);

		File folder = new File(APP_HOME_DIR_PATH);
		if (!folder.exists()) {
			//slozka s nastavenim neexistuje, vytvori se
			folder.mkdirs();
		}

		try {
			FileWriter writer = new FileWriter(APP_SETTINGS_FILE_PATHNAME);
			writer.write(json);
			writer.close();
		} catch (IOException e) {
			LOG.log(Level.SEVERE, null, e);
		}
	}

	public Settings getSettings() {
		if (settings == null) {
			settings = loadSettings();
		}
		return settings;
	}

}
