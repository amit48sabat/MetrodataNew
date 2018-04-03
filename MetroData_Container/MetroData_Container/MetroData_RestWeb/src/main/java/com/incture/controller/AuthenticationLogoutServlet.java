package com.incture.controller;

import java.io.IOException;

import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.security.auth.login.LoginContextFactory;

/**
 * Servlet implementing logout for SAP Cloud Platform.
 */
public class AuthenticationLogoutServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationLogoutServlet.class);

    /** {@inheritDoc} */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {

        	// Check if a user is logged in
            if (request.getUserPrincipal() != null) {
                logout();
                response.reset();
                response.setHeader("Cache-Control","no-cache");
                response.setHeader("Pragma","no-cache");
                response.setHeader("Cache-Control","no-store");
                response.setHeader("Cache-Control","must-revalidate");
                response.setDateHeader("Expires",0); 
                /*Cookie[] cookies = request.getCookies();
                
                // Delete all the cookies
                if (cookies != null) {
             
                    for (int i = 0; i < cookies.length; i++) {
             
                        Cookie cookie = cookies[i];
                        cookies[i].setValue(null);
                        cookies[i].setMaxAge(0);
                        response.addCookie(cookie);
                    }
                }*/
            }
            /*request =null;
            response =null;*/

        } catch (Exception e) {
            // Logout operation failed
            response.getWriter().println("Logout operation failed with reason: " + e.getMessage());
            LOGGER.error("Logout operation failed", e);
        }
    }

    /**
     * Logout user.
     */
    private void logout() throws LoginException {
        LoginContext loginContext = LoginContextFactory.createLoginContext("BASIC");
        
        loginContext.logout();
    }
}
