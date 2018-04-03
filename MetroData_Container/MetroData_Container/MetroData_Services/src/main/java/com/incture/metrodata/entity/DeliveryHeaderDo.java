package com.incture.metrodata.entity;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import lombok.Data;

/**
 * @author Lucky.Barkane
 *
 */
@Entity
@Data
@Table(name = "DELIVERY_HEADER")
public class DeliveryHeaderDo implements BaseDo {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "DELIVERY_NOTE_ID")
	private Long deliveryNoteId;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DATE")
	private Date createdDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATED_AT")
	private Date updatedAt;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_AT")
	private Date createdAt;

	@Column(name = "SALES_GROUP")
	private String salesGroup;

	@Column(name = "PURCHASE_ORDER")
	private String purchaseOrder;

	@Column(name = "REF_NUM")
	private long refNo;

	@Column(name = "STORAGE_LOC")
	private String storageLocation;

	@Column(name = "CITY")
	private String city;

	@Column(name = "AREA_CODE")
	private String areaCode;

	@Column(name = "TELEPHONE")
	private String telephone;

	@Column(name = "SOLD_TO")
	private String soldToAddress;
	
	@Column(name = "SHIP_TO")
	private String shipToAddress;

	@Column(name = "LONGITUDE")
	private Double longitude;
	
	@Column(name = "LATITUDE")
	private Double latitude;
	
	@Column(name = "SHIPPING_TYPE")
	private String shippingType;

	@Column(name = "INS_FOR_DEL")
	private String instructionForDelivery;

	@Column(name = "TRIPPED")
	private Boolean tripped;

	@Column(name = "STATUS")
	private String status;

	// expected delivery date
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "STARTED_AT")
	private Date startedAt;
	
	// actual delivery date
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ENDED_AT")
	private Date endedAt;
	
	@Column(name ="REASON_FOR_CANCELLATION")
	private String reasonForCancellation;
	
	@OneToMany(targetEntity = DeliveryItemDo.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@Fetch(value = FetchMode.SUBSELECT)
	private List<DeliveryItemDo> deliveryItems = new ArrayList<DeliveryItemDo>();

	@Column(name = "DELIVERY_ORDER")
	private Long deliveryOrder;
	
	@Column(name = "SIGN_DOC_ID")
	private String signatureDocId;
	
	@Column(name = "INVALIDATE_IDS")
	private String invalidateIds;
	
	@Column(name = "CUST_RATING")
	private Integer rating;
	
	@Column(name = "REC_NAME")
	private String receiverName;
	
	
	@Override
	public Object getPrimaryKey() {
		// TODO Auto-generated method stub
		return deliveryNoteId;
	}

}
