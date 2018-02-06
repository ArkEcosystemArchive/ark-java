package io.ark.core.exception;

import lombok.Getter;

public class ArkNodeException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3602886103244998849L;

	@Getter
	private String request;
	
	public ArkNodeException(String request, String message) {
		super(message);
		this.request = request;
	}
	
}
