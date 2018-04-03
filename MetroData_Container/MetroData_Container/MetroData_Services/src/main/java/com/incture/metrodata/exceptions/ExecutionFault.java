package com.incture.metrodata.exceptions;

import com.incture.metrodata.dto.ResponseDto;

/**
 * <code>ExecutionException</code> is wrapper over different kinds of
 * exceptions, generally indicate a fatal error eg: resource missing, data
 * source connection failed
 * 
 * 
 */
public class ExecutionFault extends Exception {
	private static final long serialVersionUID = -5972920879141164722L;
	/**
	 * Java type that goes as soapenv:Fault detail element.
	 */
	private ResponseDto faultInfo;

	public ExecutionFault() {
		// TODO Auto-generated constructor stub
	}

	public ExecutionFault(String faultMessage) {
		super(faultMessage);
		// super("Execution on Server failed, please retry later");
		faultInfo = new ResponseDto();
		faultInfo.setMessage(faultMessage);
	}

	public ExecutionFault(String message, ResponseDto faultInfo) {
		super(message);
		this.faultInfo = faultInfo;
	}

	public ExecutionFault(String message, ResponseDto faultInfo, Throwable cause) {
		super(message, cause);
		this.faultInfo = faultInfo;
	}

	public ResponseDto getFaultInfo() {
		return faultInfo;
	}
}