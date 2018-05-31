package com.incture.metrodata.entity;

import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.incture.metrodata.util.SortDHDoByDeliveryOrder;

import lombok.Data;
import lombok.ToString;

@Entity
@Data // for auto generation of getters and setters
@ToString
@Table(name = "TRIP_DETAILS")
public class TripDetailsDo implements BaseDo {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "TRIP_ID", length = 20)
	private String tripId;

	@Column(name = "STATUS")
	private String status;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATED_AT")
	private Date updatedAt;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_AT")
	private Date createdAt;

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Column(name = "UPDATED_BY")
	private String updatedBy;

	@Column(name = "START_TIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date startTime;

	@Column(name = "END_TIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date endTime;

	@Column(name = "REASON_FOR_CANCELLATION")
	private String reasonForCancellation;

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	//@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(name = "TRIP_DELIVERY_HEADER_MAPPING", joinColumns = {
			@JoinColumn(name = "TRIP_ID") }, inverseJoinColumns = { @JoinColumn(name = "DELIVERY_NOTE_ID") })
	private Set<DeliveryHeaderDo> deliveryHeader = new TreeSet<DeliveryHeaderDo>(new SortDHDoByDeliveryOrder()); 

	@Column(name = "TRACK_FREQ")
	private Long trackFreq;

	/*
	 * need to be clear with one to one or one to many btw driver to trips
	 */
	@OneToOne(targetEntity = UserDetailsDo.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private UserDetailsDo user;

	@Override
	public Object getPrimaryKey() {
		return tripId;
	}

}
