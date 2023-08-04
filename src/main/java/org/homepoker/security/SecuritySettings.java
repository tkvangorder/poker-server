package org.homepoker.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties("home-poker.security")
public class SecuritySettings {

  List<String> adminUsers;

  public List<String> getAdminUsers() {
    return adminUsers;
  }

  public void setAdminUsers(List<String> adminUsers) {
    this.adminUsers = adminUsers;
  }
}
