package com.incture.metrodata.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;

import lombok.Data;

@Entity
@Data
@Table(name = "COURIER_DETAILS")
@DynamicUpdate(true)
public class CourierDetails implements BaseDo {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "courier_id")
	private String courierId;
	
	@Column(name = "COURIER_NAME",length=50)
	private String courierName;

	
	@Override
	public Object getPrimaryKey() {
		return courierId;
	}

}
