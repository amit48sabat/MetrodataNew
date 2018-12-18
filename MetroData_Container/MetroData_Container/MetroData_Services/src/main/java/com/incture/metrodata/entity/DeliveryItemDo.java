package com.incture.metrodata.entity;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import lombok.Data;
import lombok.ToString;

@Entity
@Data
@ToString
@Table(name = "DELIVERY_ITEM")
@Cacheable
@Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)  
public class DeliveryItemDo implements BaseDo {
	
	private static final long serialVersionUID = 1L;
  
	/*@Id
	@Column(name = "DELIVERY_ITEM_ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "hilo_sequence_generator")
    @GenericGenerator(
            name = "hilo_sequence_generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @Parameter(name = "sequence_name", value = "delivery_item_sequence"),
                    @Parameter(name = "initial_value", value = "1"),
                    @Parameter(name = "increment_size", value = "1"),
                    @Parameter(name = "optimizer", value = "hilo")
            })*/
	@Id
	@TableGenerator(
            name="itemGen", 
            table="ID_GEN_TBL", 
            pkColumnName="GEN_KEY", 
            valueColumnName="GEN_VALUE", 
            pkColumnValue="GEN_ID", 
            allocationSize=1,
            initialValue=60000)
    @GeneratedValue(strategy=GenerationType.TABLE, generator="itemGen")
	@Column(name = "DELIVERY_ITEM_ID")
	private Long deliveryItemId;

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
