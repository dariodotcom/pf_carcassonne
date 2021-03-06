package it.polimi.dei.provafinale.carcassonne.controller.client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JTextField;

import it.polimi.dei.provafinale.carcassonne.controller.Message;
import it.polimi.dei.provafinale.carcassonne.controller.MessageType;
import it.polimi.dei.provafinale.carcassonne.model.SidePosition;

/**
 * Class MessageSenderListener implements an ActionListener in order to handle
 * user actions during a match with Swing UI.
 * 
 */
public class MessageSenderListener implements ActionListener {

	private MessageType type;
	private JComponent payloadSource;

	/**
	 * MessageSender constructorListener. Creates a new instance of class
	 * MessageSenderListener.
	 * 
	 * @param type
	 *            the type of the message to be sent.
	 * @param payloadSource
	 *            the JComponent to take the payload from.
	 */
	public MessageSenderListener(MessageType type, JComponent payloadSource) {
		this.type = type;
		this.payloadSource = payloadSource;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String payload;
		MessageType newType;

		if (payloadSource == null) {
			payload = null;
		}

		else if (payloadSource instanceof JTextField) {
			JTextField source = (JTextField) payloadSource;
			payload = source.getText();
		}

		else if (payloadSource instanceof JComboBox) {
			JComboBox source = (JComboBox) payloadSource;
			int selectedIndex = source.getSelectedIndex();
			payload = SidePosition.valueOf(selectedIndex).toString();
		}

		else {
			throw new RuntimeException("Can't handle given JComponent");
		}
		/* Case the passed message is not a correct coordinate. */
		if (this.type == MessageType.PLACE
				&& !payload.matches("[-]??[0-9]+,[-]??[0-9]+")) {
			newType = MessageType.INVALID_MOVE;
			payload = null;
		}
		/* Other cases. */
		else {
			newType = type;
		}

		Message msg = new Message(newType, payload);
		ClientController.getCurrentMatchController().sendMessage(msg);
	}

}
