package com.incture;

import java.util.Date;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Scope;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.incture.metrodata.constant.RoleConstant;
import com.incture.metrodata.dto.DefaultUserDetailsVO;
import com.incture.metrodata.dto.RoleDetailsDTO;
import com.incture.metrodata.dto.UserDetailsDTO;
import com.incture.metrodata.service.RoleServiceLocal;
import com.incture.metrodata.service.UserServiceLocal;
import com.incture.metrodata.util.ServicesUtil;

@Component
@Scope(value = "prototype")
@ComponentScan("com.incture")
public class AllStrategiesExampleBean {

	private static final Logger LOG = Logger.getLogger(AllStrategiesExampleBean.class);

	@Autowired
	DefaultUserDetailsVO dto;

	@Autowired
	UserServiceLocal userService;

	@Autowired
	RoleServiceLocal roleService;

	@EventListener(ContextRefreshedEvent.class)
	void contextRefreshedEvent() {
		LOG.error("Setting up Default Metrodata user with Email id " + dto.getEmail());
		UserDetailsDTO userDto = new UserDetailsDTO();
		userDto.setEmail(dto.getEmail());
		userDto.setFirstName(dto.getFirstName());
		userDto.setLastName(dto.getLastName());
		Date currDate = new Date();
		RoleDetailsDTO roleDto = roleService.getRoleByRoleName(RoleConstant.SUPER_ADMIN.getValue());
		if (!ServicesUtil.isEmpty(roleDto.getRoleName())) {
			userDto.setRole(roleDto);
		} else {
			roleDto = new RoleDetailsDTO();

			roleDto.setCreatedAt(currDate);
			roleDto.setUpdatedAt(currDate);
			roleDto.setRoleName(RoleConstant.SUPER_ADMIN.getValue());

			userDto.setRole(roleDto);

		}

		userService.createDefaultUser(userDto);
	}

}
