package org.thinker.oauth.util;

public class OAuthException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private final String status;
	
	public OAuthException(String message, String status) {
		super(message);
		this.status = status;
	}
	
	public OAuthException(String message) {
		this(message, "E000");
	}
	
	public String getStatus(){
		return this.status;
	}

}
