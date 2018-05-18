package com.incture.metrodata.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;
import lombok.ToString;

@Entity
@Data
@ToString
@Table(name = "ORDER_TRACKING_DETAILS")
public class OrderTrackingDo implements BaseDo{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3444813924990881919L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;

	@Column(name = "TRIP_ID")
	private String tripId;
	
	@Column(name = "DELIVERY_NOTE_ID")
	private String deliveryNoteId;
	
	@Column(name = "DRIVER_ID")
	private String driverId;
	
	@Column(name = "LONGITUDE")
	private Double longitude;
	
	@Column(name = "LATITUDE")
	private Double latitude;
	
	 @Temporal(TemporalType.TIMESTAMP)
	 @Column(name = "CREATED_AT", nullable = false)
	 private Date createdAt;

	@Override
	public Object getPrimaryKey() {
		return id;
	}
}
