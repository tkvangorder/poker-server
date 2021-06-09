package org.homepoker.game;

import java.util.Map;
import java.util.Optional;

import org.homepoker.common.Command;
import org.homepoker.domain.game.Game;
import org.homepoker.domain.user.User;
import org.jctools.maps.NonBlockingHashMap;
import org.jctools.queues.MessagePassingQueue;
import org.jctools.queues.MpscLinkedQueue;

public abstract class AbstractGameManagerImpl implements GameManager {

				/**
				 * This is a non-blocking queue that allows multiple threads to add commands to the queue and a single thread (the game loop thread) that will drain those commands.
				 * The contract guarantee (multi-producers, one consumer) is defined by the implementation MpscLinkedQueue and the capacity is unbounded.
				 * 
				 * The game loop thread is the only thread that will  drain the commands and manipulate the game state.
				 */
				MessagePassingQueue<Command> pendingCommands = new MpscLinkedQueue<>();

				/**
				 * This is a map of user ID -> game listener registered for that user.
				 */
				private final Map<String, UserGameListener> userGameListeners = new NonBlockingHashMap<>();

				private final 
	
	@Override
								public Optional<UserGameListener> getUserGameListener(User user) {
												return Optional.ofNullable(userGameListeners.get(user.getLoginId()));
								}

				@Override
				public void addGameListener(GameListener listener) {
				}

				@Override
				public void removeGameListener(GameListener listener) {
					// TODO Auto-generated method stub
				}

				@Override
				public void submitCommand(Command command) {
								pendingCommands.offer(command);
				}

				public void processGameTick() {
								//Process queued Commands
								pendingCommands.drain(this::applyCommand);

					//Top Level Game Processing
					//ProcessEachTable
					//Optionally Save Game/State
				}

				protected final void applyCommand(Command command) {

								switch (command.getCommandId()) {
												case REGISTER_USER:
												case CONFIRM_USER:
												case LEAVE_GAME:
												case START_GAME:
												case PAUSE_GAME:
												case END_GAME:
								}
				}


				protected abstract Game getGame();

	//protected abstract void do();
	
}
