package org.homepoker.domain.user;

import lombok.Value;

@Value
public class UserLogin {
	String emailAddress;
	String password;
}
