package it.polimi.dei.provafinale.carcassonne.controller.client;

import it.polimi.dei.provafinale.carcassonne.Constants;
import it.polimi.dei.provafinale.carcassonne.controller.ClientLocalInterface;
import it.polimi.dei.provafinale.carcassonne.controller.server.MatchHandler;
import it.polimi.dei.provafinale.carcassonne.view.CarcassonneFrame;
import it.polimi.dei.provafinale.carcassonne.view.ViewManager;
import it.polimi.dei.provafinale.carcassonne.view.game.GamePanel;
import it.polimi.dei.provafinale.carcassonne.view.game.SwingGamePanel;
import it.polimi.dei.provafinale.carcassonne.view.game.TextualGamePanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;

/**
 * Class StartLocalGameListener implements an ActionListener in order to manage the
 * beginning of a new local game.
 * 
 */
public class StartLocalGameListener implements ActionListener {

	private final int[] numPlayerValues = { 2, 3, 4, 5 };

	private JComboBox numPlayerBox;
	private JComboBox viewTypeBox;

	/**
	 * StartLocalGameListener constructor. Creates a new instance of class
	 * StartLocalGameListener.
	 * 
	 * @param numPlayerBox
	 *            the number of players selected in the JComboBox.
	 * @param viewType
	 *            the view type selected in the JComboBox.
	 */
	public StartLocalGameListener(JComboBox numPlayerBox, JComboBox viewType) {
		this.numPlayerBox = numPlayerBox;
		this.viewTypeBox = viewType;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		int numPlayers = numPlayerValues[numPlayerBox.getSelectedIndex()];
		
		/* Create duplex interface for Client - Match controller communication. */
		ClientLocalInterface cli = new ClientLocalInterface(numPlayers);
	
		MatchHandler mh = new MatchHandler(cli);
		Thread th = new Thread(mh);
		th.start();
		int viewType = viewTypeBox.getSelectedIndex();
		GamePanel panel;
		
		/* Case Swing selection. */
		if (viewType == Constants.VIEW_TYPE_GUI) {
			panel = new SwingGamePanel();
		}
		/* Case textual selection. */
		else if (viewType == Constants.VIEW_TYPE_TEXTUAL) {
			panel = new TextualGamePanel();
		}
		else {
			System.out.println("Error in view type selection: value "
					+ viewType);
			return;
		}
		
		CarcassonneFrame frame = ViewManager.getInstance().getFrame();
		frame.setGamePanel(panel);
		frame.changeMainPanel(CarcassonneFrame.GAMEPANEL);
		
		ClientController.startNewMatchController(cli, panel);
	}

}