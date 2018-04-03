package com.incture.metrodata.exceptions;

import com.incture.metrodata.dto.ResponseDto;

/**
 * <code>InvalidInputFault</code> is to indicate application that the parameters
 * passed to the method is invalid w.r.t its implementation.
 * 
 * @version 2.0, 21-June-2012
 * @since CR8313
 * @author VINU
 */
public class InvalidInputFault extends Exception {
	private static final long serialVersionUID = 2680366978983580640L;
	private ResponseDto faultInfo;

	public InvalidInputFault() {
		// TODO Auto-generated constructor stub
	}

	public InvalidInputFault(String faultMessage) {
		super(faultMessage);
		faultInfo = new ResponseDto();
		faultInfo.setMessage(faultMessage);
	}
	
	public InvalidInputFault(String message, ResponseDto faultInfo) {
		super(message);
		this.faultInfo = faultInfo;
	}

	public InvalidInputFault(String message, ResponseDto faultInfo,
			Throwable cause) {
		super(message, cause);
		this.faultInfo = faultInfo;
	}

	public ResponseDto getFaultInfo() {
		return faultInfo;
	}
}