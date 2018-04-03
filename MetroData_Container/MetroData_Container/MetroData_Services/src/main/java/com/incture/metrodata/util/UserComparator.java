package com.incture.metrodata.util;

import java.util.Comparator;

import com.incture.metrodata.dto.UserDetailsDTO;

public class UserComparator implements Comparator<UserDetailsDTO>{

	@Override
	public int compare(UserDetailsDTO o1, UserDetailsDTO o2) {
		// TODO Auto-generated method stub
		return o1.getUserId().compareTo(o2.getUserId());
	}

}
