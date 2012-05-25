package it.polimi.dei.provafinale.carcassonne.view;

import java.awt.Graphics;

public class ViewManager {

	private static ViewManager instance = null;
	private CarcassonneFrame frame;

	private ViewManager() {
		frame = new CarcassonneFrame();
	}

	/**
	 * Gives an instance of ViewManager. If it doesn't exist, the method creates
	 * a new one.
	 * 
	 * @return an instance of ViewManager.
	 */
	public static ViewManager getInstance() {

		/* There are no instances of ViewManager. */
		if (instance == null) {
			instance = new ViewManager();
		}

		return instance;
	}

	/**
	 * Manages the change of menuPanel (eg. from HomePanel to LocalGamePanel,
	 * from HomePanel to InternetGamePanel) in ViewManager.
	 * 
	 * @param panelNamea
	 *            String representing the name of the panel we want to put in
	 *            frame.
	 */
	public void changeMenuPanel(String panelName) {
		frame.changeMenuPanel(panelName);
	}

	/**
	 * 
	 * @return the current instance of frame.
	 */
	public CarcassonneFrame getFrame() {
		return frame;
	}

	/**
	 * Updates the current view of frame.
	 */
	public void updateView() {
		Graphics g = frame.getGraphics();
		if (g != null) {
			frame.paint(g);
		}
	}

}
