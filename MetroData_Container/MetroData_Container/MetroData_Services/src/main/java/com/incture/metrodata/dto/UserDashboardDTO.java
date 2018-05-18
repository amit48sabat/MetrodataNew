package com.incture.metrodata.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserDashboardDTO {
	 String status;
	 Long count;
	 UserDetailsDTO userInfo;
}
