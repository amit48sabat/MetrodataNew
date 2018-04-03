package com.incture.metrodata.service;

import org.apache.http.HttpStatus;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.incture.metrodata.constant.Message;
import com.incture.metrodata.dao.UserDAO;
import com.incture.metrodata.dto.ResponseDto;
import com.incture.metrodata.dto.UserDetailsDTO;

@Service("userService")
@Transactional
public class UserService implements UserServiceLocal {

	// private static final Logger LOGGER =
	// LoggerFactory.getLogger(DeliveryHeaderService.class);
	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private UserDAO userDAO;

	protected Session getSession() {
		return sessionFactory.getCurrentSession();
	}

	@Override
	public ResponseDto updateUserData(UserDetailsDTO userDetailsDTO) {
		// TODO Auto-generated method stub
		ResponseDto responseDto = new ResponseDto();
		/*try{
			 data  = userDAO.findById(userDetailsDTO);
		}catch (Exception e) {
			// TODO: handle exception
		}*/
		try {
			/*if(!ServicesUtil.isEmpty(data))
				if(ServicesUtil.isEmpty(data.getUserType())){
					userDetailsDTO.setUserType("Driver");
			}*/
			responseDto.setStatus(userDAO.updateUserData(userDetailsDTO));
			responseDto.setCode(HttpStatus.SC_OK);
			
		} catch (Exception e) {
			e.printStackTrace();
			responseDto.setStatus(false);
			responseDto.setCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
		}
		return responseDto;
	}

	@Override
	public ResponseDto findById(String userId) {
		ResponseDto responseDto = new ResponseDto();
		try {
			UserDetailsDTO dto = new UserDetailsDTO();
			dto.setUserId(userId);
			Object data  = userDAO.findById(dto);

			responseDto.setStatus(true);
			responseDto.setCode(200);
			responseDto.setData(data);
			responseDto.setMessage(Message.SUCCESS.toString());
		} catch (Exception e) {
			responseDto.setStatus(false);
			responseDto.setCode(417);
			responseDto.setMessage(Message.FAILED + " : " + e.getMessage());
		}
		return responseDto;
	}
}
