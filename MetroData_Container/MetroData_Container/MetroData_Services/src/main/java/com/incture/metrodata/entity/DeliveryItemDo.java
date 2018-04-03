package com.incture.metrodata.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import lombok.Data;

@Entity
@Data
@Table(name = "DELIVERY_ITEM")
public class DeliveryItemDo implements BaseDo {
	
	private static final long serialVersionUID = 1L;

	@Id
	@TableGenerator(
            name="itemGen", 
            table="ID_GEN_TBL", 
            pkColumnName="GEN_KEY", 
            valueColumnName="GEN_VALUE", 
            pkColumnValue="GEN_ID", 
            allocationSize=1)
    @GeneratedValue(strategy=GenerationType.TABLE, generator="itemGen")
	@Column(name = "DELIVERY_ITEM_ID")
	private long deliveryItemId;

	@Column(name = "SERIAL_NUM")
	private String serialNum;

	@Column(name = "MATERIAL")
	private String material;
	
	@Column
	@Lob
	private String description;

	@Column(name = "BATCH")
	private String batch;

	@Column(name = "QUANTITY")
	private String quantity;

	@Column(name = "VOLUME")
	private String volume;

	@Override
	public Object getPrimaryKey() {
		return deliveryItemId;
	}
}
