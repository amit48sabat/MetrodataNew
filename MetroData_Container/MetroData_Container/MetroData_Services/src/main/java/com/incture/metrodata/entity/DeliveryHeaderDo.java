package com.incture.metrodata.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Where;

import lombok.Data;
import lombok.ToString;

/**
 * @author Lucky.Barkane
 *
 */
@Entity
@Data
@ToString
@Table(name = "DELIVERY_HEADER")
@Where(clause = "DELETED = 0")
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

	@Column(name = "VALIDATION_STATUS")
	@ColumnDefault("'false'")
	private String validationStatus;

	@Column(name = "ASSIGNED_USER")
	private String assignedUser;

	@Column(name = "IS_AWB_VALIDATE")
	@ColumnDefault("'false'")
	private String awbValidated;

	// expected delivery date
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "STARTED_AT")
	private Date startedAt;

	// actual delivery date
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ENDED_AT")
	private Date endedAt;

	@Column(name = "REASON_FOR_CANCELLATION")
	private String reasonForCancellation;

	@Column(name = "CUST_COMMENT")
	private String custComment;

	@Column(name = "AIR_WAY_BILL_NO")
	private String airwayBillNo;

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

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DELIVERY_DATE")
	private Date deliveryDate;
	
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private WareHouseDetailsDo wareHouseDetails;

	@Column(name = "DELETED")
	private Integer deleted = 0;

	// getters and setters

	public void setDeleted() {
		this.deleted = 1;
	}

	@Override
	public Object getPrimaryKey() {
		// TODO Auto-generated method stub
		return deliveryNoteId;
	}

}
