package com.incture.metrodata.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDashboardDTO {
	 String status;
	 Long count;
	 UserDetailsDTO userInfo;
}
