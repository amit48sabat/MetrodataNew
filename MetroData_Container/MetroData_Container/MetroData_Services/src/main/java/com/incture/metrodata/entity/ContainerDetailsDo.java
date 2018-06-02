package com.incture.metrodata.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Where;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@Table(name = "CONTAINER_DETAILS")
@Where(clause = "DELETED = 0 ")
public class ContainerDetailsDo implements BaseDo {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6054555619829494683L;

	@TableGenerator(name = "id", table = "ID_GEN", pkColumnName = "GEN_KEY", valueColumnName = "GEN_VALUE", pkColumnValue = "DELIVNO",initialValue=1, allocationSize = 1)
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "id")
	private Long id;

	@Column(name = "DELIVNO")
	private Long DELIVNO;

	@Column(name = "CREATEDT")
	private String CREATEDT;

	@Column(name = "CREATETM")
	private String CREATETM;

	@Column(name = "SALESGRP")
	private String SALESGRP;

	@Column(name = "PURCHORD")
	private String PURCHORD;

	@Column(name = "REFNO")
	private Long REFNO;

	@Column(name = "SLOC")
	private String SLOC;

	@Column(name = "SHIPADD")
	private String SHIPADD;

	@Column(name = "CITY")
	private String CITY;

	@Column(name = "AREACODE")
	private String AREACODE;

	@Column(name = "TELP")
	private String TELP;

	@Column(name = "SOLDADD")
	private String SOLDADD;

	@Column(name = "SHIPTYP")
	private String SHIPTYP;

	@Column(name = "INSTDELV")
	private String INSTDELV;

	@Column(name = "SERNUM")
	private String SERNUM;

	@Column(name = "MAT")
	private String MAT;

	@Column(name = "BATCH")
	private String BATCH;

	@Lob
	@Column(name = "DESC")
	private String DESC;

	@Column(name = "QTY")
	private String QTY;

	@Column(name = "VOL")
	private String VOL;
	
	@Column(name = "STAT")
	private String STAT;

	@Column(name = "DELETED", columnDefinition="int default 0", nullable=false)
	private Integer deleted = 0;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_AT")
	private Date createdAt;
	
	@Override
	public Object getPrimaryKey() {
		return id;
	}

}
