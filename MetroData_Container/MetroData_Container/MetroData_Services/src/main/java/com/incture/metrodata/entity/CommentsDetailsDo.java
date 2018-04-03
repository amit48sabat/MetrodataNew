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

import lombok.Getter;
import lombok.Setter;
@Entity
@Getter
@Setter
@Table(name = "COMMENT_DETAILS")
public class CommentsDetailsDo implements BaseDo{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@TableGenerator(name = "id", table = "ID_GEN_COMMENTS", pkColumnName = "GEN_KEY", valueColumnName = "GEN_VALUE", pkColumnValue = "commentsId",initialValue=1, allocationSize = 1)
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "id")
	private Long id;
	
	@Lob
	private String comment;
	
	@Column(name = "CREATED_BY")
	private String createdBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_AT")
	private Date createdAt;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATED_AT")
	private Date updatedAt;
	
	/*@Column(name="MSG_ID")
	private Long msg_id;*/

	@Override
	public Object getPrimaryKey() {
		// TODO Auto-generated method stub
		return id;
	}

}
