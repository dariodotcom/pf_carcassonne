package it.polimi.dei.provafinale.carcassonne.controller.client;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import it.polimi.dei.provafinale.carcassonne.Constants;
import it.polimi.dei.provafinale.carcassonne.controller.server.CarcassonneRMIServer;
import it.polimi.dei.provafinale.carcassonne.logger.Logger;
import it.polimi.dei.provafinale.carcassonne.logger.LoggerService;
import it.polimi.dei.provafinale.carcassonne.model.gameinterface.Message;
import it.polimi.dei.provafinale.carcassonne.model.gameinterface.MessageType;
import it.polimi.dei.provafinale.carcassonne.model.gamelogic.player.PlayerColor;

public class ClientRMIInterface implements ClientInterface,
		CarcassonneRMIClient {

	private int POLL_INTERVAL;
	private String host;
	private Message serverBuffer, clientBuffer;
	private CarcassonneRMIServer server;
	private Logger logger;

	public ClientRMIInterface(String host) {
		this.host = host;
		this.logger = LoggerService.getService().register("RMI Interface");
	}

	/* ClientInterface methods */
	@Override
	public void connect() throws ConnectionLostException {
		Message request = new Message(MessageType.CONNECT, null);
		connectToRMIServer(request);
	}

	@Override
	public synchronized void sendMessage(Message msg)
			throws ConnectionLostException {
		while (clientBuffer != null) {
			try {
				wait(POLL_INTERVAL);
				server.poll();
			} catch (InterruptedException ie) {

			} catch (RemoteException re) {
				throw new ConnectionLostException();
			}
		}

		clientBuffer = msg;
		notifyAll();
		logger.log("RMI|CLIENT|WRITE: " + msg);
		return;
	}

	@Override
	public synchronized Message readMessage() throws ConnectionLostException {
		while (serverBuffer == null) {
			try {
				wait(POLL_INTERVAL);
				server.poll();
			} catch (InterruptedException ie) {

			} catch (RemoteException re) {
				throw new ConnectionLostException();
			}
		}

		Message msg = serverBuffer;
		serverBuffer = null;
		notifyAll();
		logger.log("RMI|CLIENT|READ: " + msg);
		return msg;
	}

	@Override
	public void reconnect(String matchName, PlayerColor color)
			throws ConnectionLostException {
		String payload = String.format("reconnect: %s, %s", color, matchName);
		Message request = new Message(MessageType.RECONNECT, payload);
		connectToRMIServer(request);
	}

	/* ClientRMIInterface methods */
	@Override
	public synchronized void sendMessageToPlayer(Message msg) {
		while (serverBuffer != null) {
			try {
				wait();
			} catch (InterruptedException ie) {

			}
		}

		serverBuffer = msg;
		logger.log("RMI|SERVER|WRITE: " + msg);
		notifyAll();
		return;
	}

	@Override
	public synchronized Message readMessageFromPlayer() {
		while (clientBuffer == null) {
			try {
				wait();
			} catch (InterruptedException ie) {

			}
		}

		Message msg = clientBuffer;
		clientBuffer = null;
		notifyAll();
		logger.log("RMI|SERVER|READ: " + msg);
		return msg;
	}

	// Helpers
	private void connectToRMIServer(Message request)
			throws ConnectionLostException {
		try {
			UnicastRemoteObject.exportObject(this, 0);
			Registry registry = LocateRegistry.getRegistry(host);
			server = (CarcassonneRMIServer) registry
					.lookup(Constants.RMI_SERVER_NAME);
			server.register(this, request);
		} catch (Exception re) {
			re.printStackTrace();
			throw new ConnectionLostException();
		}
	}
}