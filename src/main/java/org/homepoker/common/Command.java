package org.homepoker.common;

import lombok.Value;
import lombok.With;
import org.homepoker.domain.user.User;
import org.springframework.util.Assert;

import java.util.Map;

/**
 * The command is meant to be serialized over the wire and therefore, we avoid polymorphic types to make serialization easier.
 * A command will always have an ID and most commands will be associated with a user that has initiated that command.
 * <p>
 * The payload is a generic key/value structure that will be populated with elements that are specific to that command.
 *
 * @author tyler.vangorder
 */
@Value
@With
public class Command {

  CommandId commandId;
  User user;
  Map<String, String> payload;

  /**
   * Create a {@link CommandId#CONFIRM_USER} command.
   * <p>
   * The self-initiated command allows a user to register themselves with the game.
   * <p>
   * Payload:
   * loginId : The login ID of the user registering for the game.
   *
   * @param user The user that is registering the player.
   * @return command
   */
  public static Command asRegisterUser(User user) {
    Assert.notNull(user, "The user is required when registering for a game.");
    Assert.hasText(user.getLoginId(), "The user's loginId is required when registering for a game.");
    return new Command(
        CommandId.REGISTER_USER,
        user,
        Map.of("loginId", user.getLoginId()));
  }

  /**
   * Create a {@link CommandId#CONFIRM_USER} command.
   * <p>
   * Payload:
   * userId : The persistent ID of the user being confirmed for the game.
   *
   * @param user The user that is being confirmed for the game.
   * @return Confirm player command;
   */
  public static Command confirmUser(User user, String loginId) {
    Assert.notNull(user, "The user that is confirming the login ID is required.");
    Assert.hasText(user.getLoginId(), "The user's that is confirming the login ID is required.");
    Assert.hasText(loginId, "The login ID for the user being confirmed is required.");

    return new Command(
        CommandId.CONFIRM_USER,
        user,
        Map.of("loginId", loginId));
  }
}
