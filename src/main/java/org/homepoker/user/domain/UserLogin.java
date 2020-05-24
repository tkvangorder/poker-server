package org.homepoker.user.domain;

import lombok.Value;

@Value
public class UserLogin {
	String emailAddress;
	String password;
}
