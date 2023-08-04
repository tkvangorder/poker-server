package org.homepoker.rsocket;

import org.homepoker.domain.user.User;
import org.homepoker.domain.user.UserCriteria;
import org.homepoker.domain.user.UserInformationUpdate;
import org.homepoker.domain.user.UserPasswordChangeRequest;
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
