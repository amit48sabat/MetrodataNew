package com.incture.metrodata.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Where;

import lombok.Data;
import lombok.ToString;

@Entity
@Data
@ToString
@Table(name = "USER_DETAILS")
@DynamicUpdate(true)
@Where(clause = "DELETE = 0")
public class UserDetailsDo implements BaseDo {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "USER_ID",length=50)
	private String userId;
	
	@Column(name = "FIRST_NAME",length=100)
	private String firstName;

	@Column(name = "LAST_NAME",length=100)
	private String lastName;
	
	//@Formula(value = " concat(FIRST_NAME, ' ', LAST_NAME) ")
	@Column(name = "NAME",length=100)
	private String name;
	
	@ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
	private RoleDetailsDo role;
	
	@Column(name = "TELEPHONE",length=50)
	private String telephone;
	
	@Column(name = "CREATED_BY",length=50)
	private String createdBy;
	
	@Column(name = "UPDATED_BY",length=50)
	private String updatedBy;
	
	
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
	
	@Column(name = "EMAIL",length=100)
	private String email;
	
	@Column(name = "PARENT_ID",length=100)
	private String parentId;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "LAST_LOGIN_TIME")
	private Date lastLogedIn;
	
	@Column(name = "TRACK_FREQUENCY")
	@ColumnDefault("'30'")
	private Long trackFreq;
	
	@ManyToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
	@JoinTable(name = "USERS_WAREHOUSE_MAPPING", joinColumns = { @JoinColumn(name = "USER_ID") }, inverseJoinColumns = {
			@JoinColumn(name = "WARE_HOUSE_ID") })
	private Set<WareHouseDetailsDo> wareHouseDetails = new HashSet<WareHouseDetailsDo>(0);
	
	@ManyToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
	@JoinTable(name = "USERS_COURIER_MAPPING", joinColumns = { @JoinColumn(name = "USER_ID") }, inverseJoinColumns = {
			@JoinColumn(name = "COURIER_ID") })
	private Set<CourierDetailsDo> courierDetails = new HashSet<CourierDetailsDo>(0);
	
	@Column(name = "DELETE")
	@ColumnDefault("'0'")
	private Integer deleted = 0;

	public void setDeleted() {
		this.deleted = 1;
	}
	
	@Override
	public Object getPrimaryKey() {
		return userId;
	}

}
