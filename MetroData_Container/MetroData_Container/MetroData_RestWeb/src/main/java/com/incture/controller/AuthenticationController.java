package com.incture.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.incture.metrodata.dao.UserDAO;
import com.incture.metrodata.dto.ResponseDto;
import com.incture.metrodata.dto.UserDetailsDTO;
import com.incture.metrodata.service.UserServiceLocal;
import com.incture.metrodata.util.RESTInvoker;

@RestController
@CrossOrigin
public class AuthenticationController {

	/*
	 * @RequestMapping(value = "/logout") public @ResponseBody ResponseVo
	 * logout(HttpServletRequest request) { ResponseVo responseVo = new
	 * ResponseVo();
	 * 
	 * if (request.getUserPrincipal() != null) { LoginContext loginContext; try
	 * { loginContext = LoginContextFactory.createLoginContext();
	 * loginContext.logout(); request.getSession().invalidate();
	 * responseVo.setMessage("logout successful"); } catch (LoginException e) {
	 * e.printStackTrace();
	 * responseVo.setMessage("logout failed due to server error"); }
	 * 
	 * }
	 * 
	 * return responseVo; }
	 */

	@Autowired
	UserServiceLocal userService;

	@Autowired
	UserDAO userDAO;

	@Autowired
	RESTInvoker restInvoker;

	@RequestMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseDto login(HttpServletRequest request, HttpServletResponse httpServletResponse) {

		ResponseDto responseDto = new ResponseDto();
		if (request.getUserPrincipal() != null) {
			String userId = request.getUserPrincipal().getName();

			/*
			 * String userData = restInvoker.getDataFromServer("/Users/" +
			 * userId);
			 * 
			 * 
			 * JSONArray data = jsonResult.getJSONArray("data"); if(data !=
			 * null) { String[] names = new String[data.length()]; String[]
			 * birthdays = new String[data.length()]; for(int i = 0 ; i <
			 * data.length() ; i++) { birthdays[i] = data.getString("birthday");
			 * names[i] = data.getString("name"); } }
			 * 
			 * JSONObject jsonResult = new JSONObject(userData); JSONObject
			 * userDetails = new JSONObject(jsonResult.get("name").toString());
			 * String name = userDetails.get("givenName") + " " +
			 * userDetails.get("familyName");
			 * 
			 * String emailDetailsJson = jsonResult.get("emails").toString();
			 * Gson googleJson = new Gson(); ArrayList javaArrayListFromGSON =
			 * googleJson.fromJson(emailDetailsJson, ArrayList.class); String
			 * email =""; Map m; for (Object o : javaArrayListFromGSON) { m =
			 * googleJson.fromJson(o.toString(), Map.class); if ((boolean)
			 * m.get("primary")) { email= (String) m.get("value"); } }
			 */

			responseDto.setMessage("Login Success");
			UserDetailsDTO user = new UserDetailsDTO();

			user.setUserId(userId);
			/*
			 * user.setName(name); // user.setEmail((String)
			 * userDetails.get("email")); user.setFirstName((String)
			 * userDetails.get("givenName")); user.setLastName((String)
			 * userDetails.get("familyName")); user.setEmail(email);
			 */
			// updating user in database
			
			ResponseDto dto = userService.find(user);
			if (dto.isStatus()) {
				responseDto.setData(dto.getData());
				responseDto.setStatus(true);
				responseDto.setCode(HttpStatus.OK.value());
			} else {
				responseDto.setStatus(false);
				responseDto.setCode(HttpStatus.UNAUTHORIZED.value());
			}
		} else {
			responseDto.setStatus(false);
			responseDto.setMessage("Login Failed");
			responseDto.setCode(HttpStatus.UNAUTHORIZED.value());
		}
		return responseDto;
	}

}