package org.homepoker.rsocket;

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

  User registerUser(User user) {
    return userManager.registerUser(user);
  }

  User getUser(String loginId) {
    return userManager.getUser(loginId);
  }

  List<User> findUsers(UserCriteria criteria) {
    return userManager.findUsers(criteria);
  }

  User updateUser(UserInformationUpdate update) {
    return userManager.updateUserInformation(update);
  }

  void updateUserPassword(UserPasswordChangeRequest passwordRequest) {
    userManager.updateUserPassword(passwordRequest);
  }

  @MessageMapping(MessageRoutes.ROUTE_USER_MANAGER_DELETE_USER)
  void deleteUser(String loginId) {
    userManager.deleteUser(loginId);
  }
}
