package com.incture.controller;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import com.incture.metrodata.dto.ResponseDto;
import com.incture.metrodata.dto.UserDetailsDTO;
import com.incture.metrodata.service.UserServiceLocal;

public class MyFilter implements Filter {
	
	@Autowired
	UserServiceLocal userService;

	public void init(FilterConfig arg0) throws ServletException {
	}

	
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		String userId = ((HttpServletRequest)request).getUserPrincipal().getName();
		UserDetailsDTO user = new UserDetailsDTO();
		user.setUserId(userId);
		ResponseDto dto = userService.find(user);
		if (dto.isStatus()) {
			((HttpServletResponse)response).setStatus(403);
			return;
		}
		chain.doFilter(request, response);
		
	}

	
}
