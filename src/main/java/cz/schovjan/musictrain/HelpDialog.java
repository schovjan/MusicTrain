/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.schovjan.musictrain;

import java.awt.Dimension;
import javax.swing.JDialog;

/**
 *
 * @author schovanek
 */
public class HelpDialog extends JDialog {

	public HelpDialog() {
		super();

		setSize(new Dimension(550, 400));
		setLocationRelativeTo(null);
		setModal(true);
		setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
		setTitle("Nápověda");
	}

}
