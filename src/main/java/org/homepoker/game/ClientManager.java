package org.homepoker.game;

public interface ClientManager {
	boolean registerClient(Client cilent);
	void disconnectClient(Client client);
}
