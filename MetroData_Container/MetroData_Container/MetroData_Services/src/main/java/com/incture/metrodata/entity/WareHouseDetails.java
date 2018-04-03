package com.incture.metrodata.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;

import lombok.Data;

@Entity
@Data
@Table(name = "WARE_HOUSE_DETAILS")
@DynamicUpdate(true)
public class WareHouseDetails implements BaseDo {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "WARE_HOUSE_ID")
	private String wareHouseId;
	
	@Column(name = "WARE_HOUSE_NAME",length=50)
	private String firstName;

	
	@Override
	public Object getPrimaryKey() {
		return wareHouseId;
	}

}
