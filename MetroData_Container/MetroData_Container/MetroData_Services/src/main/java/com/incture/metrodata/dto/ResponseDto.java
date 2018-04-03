package com.incture.metrodata.dto;

import javax.xml.bind.annotation.XmlRootElement;

import lombok.Getter;
import lombok.Setter;

@XmlRootElement
@Getter
@Setter
public class ResponseDto {
	private int code;
	private boolean status;
	private String Message;
	Object data;
}
