package com.incture.metrodata.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class DefaultUserDetailsVO {
	private String firstName;
	private String lastName;
	private String email;
	private String role;
}
