package com.incture.controller;

import java.io.IOException;

import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.sap.security.auth.login.LoginContextFactory;

public class MyFilter implements Filter {

	public void init(FilterConfig arg0) throws ServletException {
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		LoginContext loginContext;
		try {
			loginContext = LoginContextFactory.createLoginContext("BASIC");
			loginContext.login();

		} catch (LoginException e) {
			e.printStackTrace();
		}
		chain.doFilter(request, response);
	}
	public void destroy() {
	}
}
