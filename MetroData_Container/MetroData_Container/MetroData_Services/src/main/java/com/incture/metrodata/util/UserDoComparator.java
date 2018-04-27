package com.incture.metrodata.util;

import java.util.Comparator;

import com.incture.metrodata.entity.UserDetailsDo;

public class UserDoComparator implements Comparator<UserDetailsDo> {

	@Override
	public int compare(UserDetailsDo o1, UserDetailsDo o2) {
		// TODO Auto-generated method stub
		return o1.getUserId().compareTo(o2.getUserId());
	}

}
