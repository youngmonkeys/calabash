package com.tvd12.calabash.client.exception;

public class CalabaseRpcException extends RuntimeException {
	private static final long serialVersionUID = 7996223029074587833L;

	public CalabaseRpcException(String message, Throwable e) {
		super(message, e);
	}
	
}
