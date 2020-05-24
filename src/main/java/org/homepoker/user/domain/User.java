package org.homepoker.user.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {

	/**
	 * Internally assigned unique key used for persistence.
	 */
	String id;

	/**
	 * User's email, also used as the login ID.
	 */
	String email;

	/**
	 * User's password, always encrypted.
	 */
	String loginPassword;

	/**
	 * User's preferred alias when in a game or at a table.
	 */
	String alias;

	/**
	 * User's "real" name.
	 */
	String name;

	/**
	 * Phone number can be useful when organizing a remote game.
	 */
	String phone;


}
