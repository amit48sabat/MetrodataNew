package com.incture.metrodata.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("testService")
@Transactional
public class TestService implements TestServiceLocal{

	@Autowired
	AsyncCompLocal comp;
	
	@Override
	public void get() {
		Map<String,Object> map = new HashMap<>();
		map.put("test","Test");
		comp.backgroudDnProcessing(map);
	}

}
