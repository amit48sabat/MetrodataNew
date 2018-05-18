package com.incture.metrodata.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.DynamicUpdate;

import lombok.Data;
import lombok.ToString;

@Entity
@Data
@ToString
@Table(name = "WARE_HOUSE_DETAILS")
@DynamicUpdate(true)
public class WareHouseDetailsDo implements BaseDo {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "WARE_HOUSE_ID")
	private String wareHouseId;

	@Column(name = "WARE_HOUSE_NAME", length = 100)
	private String wareHouseName;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_AT")
	private Date createdAt;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATED_AT")
	private Date updatedAt;

	@Override
	public Object getPrimaryKey() {
		return wareHouseId;
	}

}
