package com.incture.metrodata.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.DynamicUpdate;

import lombok.Data;

@Entity
@Data
@Table(name = "USER_DETAILS")
@DynamicUpdate(true)
public class UserDetailsDo implements BaseDo {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "USER_ID")
	private String userId;
	
	@Column(name = "FIRST_NAME",length=50)
	private String firstName;

	@Column(name = "LAST_NAME",length=50)
	private String lastName;
	
	@Column(name = "NAME",length=50)
	private String name;
	
	@Column(name = "USER_TYPE",length=50)
	private String userType;
	
	
	@Column(name = "MOBILE_TOKEN")
	@Lob
	private String mobileToken;

	@Column(name = "WEB_TOKEN")
	@Lob
	private String webToken;

	@Column(name = "LONGITUDE")
	private Double longitude;
	
	@Column(name = "LATITUDE")
	private Double latitude;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DATE")
	private Date createdDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_AT")
	private Date createdAt;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATED_AT")
	private Date updateAt;
	
	@Column(name = "EMAIL",length=50)
	private String email;
	
	@Column(name = "PARENT_ID",length=50)
	private String parentId;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "LAST_LOGIN_TIME")
	private Date lastLogedIn;
	
	@Override
	public Object getPrimaryKey() {
		return userId;
	}

}
