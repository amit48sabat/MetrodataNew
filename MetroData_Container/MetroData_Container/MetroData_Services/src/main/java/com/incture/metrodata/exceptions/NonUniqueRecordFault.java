package com.incture.metrodata.exceptions;

import com.incture.metrodata.dto.ResponseDto;

/**
 * <code>NonUniqueRecordFault</code> is to indicate application that the query
 * returns more than one record, sometimes its indicates an unique constraint
 * violation in the data source
 * 
 * @version 2.0, 21-June-2012
 * @since CR8313
 * @author VINU
 */
// @WebFault(name = "NonUniqueRecordFault", faultBean =
// "com.incture.u2k2.mmw.exception.MessageUIDto")
public class NonUniqueRecordFault extends Exception {
	private static final long serialVersionUID = 7172941851377952669L;
	/**
	 * Java type that goes as soapenv:Fault detail element.
	 */
	private ResponseDto faultInfo;

	public NonUniqueRecordFault() {
		// TODO Auto-generated constructor stub
	}

	public NonUniqueRecordFault(String faultMessage) {
		super("Failed due to corrupt data, please contact db admin ");
		faultInfo = new ResponseDto();
		faultInfo.setMessage(faultMessage);
	}

	public NonUniqueRecordFault(String message, ResponseDto faultInfo) {
		super(message);
		this.faultInfo = faultInfo;
	}

	public NonUniqueRecordFault(String message, ResponseDto faultInfo, Throwable cause) {
		super(message, cause);
		this.faultInfo = faultInfo;
	}

	public ResponseDto getFaultInfo() {
		return faultInfo;
	}
}