package com.incture.metrodata.dto;

import com.incture.metrodata.exceptions.InvalidInputFault;
import com.incture.metrodata.util.DB_Operation;
import com.incture.metrodata.util.ServicesUtil;

public abstract class BaseDto {
	
	
	public abstract void validate(DB_Operation enOperation)
			throws InvalidInputFault;

	protected void enforceMandatory(String field, Object value)
			throws InvalidInputFault {
		// TODO: Handle empty String case?? [ROHIT]
		if (ServicesUtil.isEmpty(value)) {
			String message = "Field=" + field + " can't be empty";
			throw new InvalidInputFault(message, null);
		}
	}

	protected String checkStringSize(String field, String value, int allowedSize)
			throws InvalidInputFault {
		if (!ServicesUtil.isEmpty(value)) {// check size
			value = value.trim();
			int sizeOfInput = value.length();
			if (sizeOfInput > allowedSize) {
				String message = "Exceeding size for[" + field
						+ "] allowed size is[" + allowedSize
						+ "], input value[" + value + "] is of size[ "
						+ sizeOfInput + "]";
				throw new InvalidInputFault(message, null);
			}
			return value;
		}
		return null;
	}
	
	public abstract Object getPrimaryKey();
}
