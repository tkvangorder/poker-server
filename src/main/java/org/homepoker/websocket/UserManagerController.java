package org.homepoker.websocket;

import org.homepoker.user.User;
import org.homepoker.user.UserCriteria;
import org.homepoker.user.UserInformationUpdate;
import org.homepoker.user.UserPasswordChangeRequest;
import org.homepoker.user.UserManager;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class UserManagerController {

  private final UserManager userManager;

  public UserManagerController(UserManager userManager) {
    this.userManager = userManager;
  }

  @MessageMapping("/user/register")
  User registerUser(User user) {
    return userManager.registerUser(user);
  }

  @MessageMapping("/user/get")
  User getUser(String loginId) {
    return userManager.getUser(loginId);
  }

  @MessageMapping("/user/find")
  List<User> findUsers(UserCriteria criteria) {
    return userManager.findUsers(criteria);
  }

  @MessageMapping("/user/update")
  User updateUser(UserInformationUpdate update) {
    return userManager.updateUserInformation(update);
  }

  @MessageMapping("/user/update-password")
  void updateUserPassword(UserPasswordChangeRequest passwordRequest) {
    userManager.updateUserPassword(passwordRequest);
  }

  @MessageMapping("/user/delete")
  void deleteUser(String loginId) {
    userManager.deleteUser(loginId);
  }
}
