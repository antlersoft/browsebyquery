/**
 * 
 */
package com.antlersoft.ilanalyze.ui;

import java.awt.Container;
import java.awt.Frame;
import java.awt.GridBagConstraints;

import javax.swing.JDialog;

/**
 * @author Michael A. MacDonald
 *
 */
class ConfigurationDialog extends JDialog {

	/**
	 * @param owner
	 * @param title
	 * @param modal
	 */
	ConfigurationDialog(Frame owner, String title, boolean modal) {
		super(owner, title, modal);
		Container container = getContentPane();
		GridBagConstraints c = new GridBagConstraints();
	}

}
