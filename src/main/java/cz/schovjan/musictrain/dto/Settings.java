/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.schovjan.musictrain.dto;

/**
 *
 * @author schovanek
 */
public class Settings {

	//defaultni hodnoty
	private String folderPath = System.getProperty("user.home");

	public Settings() {
	}

	public String getFolderPath() {
		return folderPath;
	}

	public void setFolderPath(String folderPath) {
		this.folderPath = folderPath;
	}

}
