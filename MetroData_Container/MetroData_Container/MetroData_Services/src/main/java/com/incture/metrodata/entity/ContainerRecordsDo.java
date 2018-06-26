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

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Where;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@Table(name = "CONTAINER_RECORDS_DETAILS")
@Where(clause = "DELETED = 0 ")
public class ContainerRecordsDo implements BaseDo{
  
	/*@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "hilo_sequence_generator")
    @GenericGenerator(
            name = "hilo_sequence_generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @Parameter(name = "sequence_name", value = "container_sequence"),
                    @Parameter(name = "initial_value", value = "1"),
                    @Parameter(name = "increment_size", value = "1"),
                    @Parameter(name = "optimizer", value = "hilo")
            })
    @Id*/
	@Id
	@TableGenerator(
            name="containerGen", 
            table="CONT_ID_GEN", 
            pkColumnName="GEN_KEY", 
            valueColumnName="GEN_VALUE", 
            pkColumnValue="GEN_ID", 
            allocationSize=1,
            initialValue=5000)
    @GeneratedValue(strategy=GenerationType.TABLE, generator="containerGen")
	private Long id;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_AT")
	private Date createdAt;
	
	@Lob
	@Column(name = "PAYLOAD")
	private String payload;
	
	@Column(name = "TOTAL_ITEMS")
	private Long totalItems;

	@Column(name = "TOTAL_DNS")
	private Long totalDns;
	
	@Column(name = "DELETED", columnDefinition="int default 0", nullable=false)
	private Integer deleted = 0;
	
	public void setDeleted(){
		this.deleted = 1;
	}

	@Override
	public Object getPrimaryKey() {
		// TODO Auto-generated method stub
		return id;
	}
}
