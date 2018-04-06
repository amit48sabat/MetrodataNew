package com.incture.metrodata.dto;

import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.incture.metrodata.exceptions.InvalidInputFault;
import com.incture.metrodata.util.DB_Operation;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Lucky.Barkane
 *
 */
@XmlRootElement
@Getter
@Setter
public class DeliveryHeaderDTO extends BaseDto {
	private Long deliveryNoteId;
	private Date createdDate;
	private Date createdAt;
	private Date updatedAt;
	private String salesGroup;
	private String purchaseOrder;
	private long refNo;
	private String storageLocation;
	private String city;
	private String areaCode;
	private String telephone;
	private String soldToAddress;
	private String shipToAddress;
	private Double longitude;
	private Double latitude;
	private String shippingType;
	private String instructionForDelivery;
	private Boolean tripped;
	private String status;
	private Date startedAt;
	private Date endedAt;
	private String reasonForCancellation;
	private List<DeliveryItemDTO> deliveryItems;
	private Long deliveryOrder;
	private String signatureDocId;
	private String invalidateIds;
	private String fileContent;
	private String fileType;
	private String fileName;
	private Integer rating;
	private String receiverName;
	private String assignedUser;
	private String airwayBillNo;

	@Override
	public void validate(DB_Operation enOperation) throws InvalidInputFault {
		// TODO Auto-generated method stub

	}

	@Override
	public Object getPrimaryKey() {
		// TODO Auto-generated method stub
		return deliveryNoteId;
	}

}
