package com.collibra.dgc.core.security;

import org.apache.shiro.authc.AuthenticationException;

/**
 * Exception thrown when the system has too many concurrent users
 * @author GKDAI63
 * 
 */
// Because apache shiro only puts the name of a class on the request scope to check what went wrong
// request. request.getAttribute("shiroLoginFailure").equals(TooManyConcurrentUsersException.class.getName()) == true
// because of this a uniquely named exception is required to check why exactly the user can't log in.
// Using this method a clear error message can be shown to the user
public class TooManyConcurrentUsersException extends AuthenticationException {

}
