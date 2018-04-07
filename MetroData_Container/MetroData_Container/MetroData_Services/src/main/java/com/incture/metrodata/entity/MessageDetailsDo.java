package com.incture.metrodata.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@Table(name = "MESSAGE_DETAILS")
public class MessageDetailsDo implements BaseDo {

	private static final long serialVersionUID = -2740507075202872523L;

	@Id
	@Column(name = "MESSAGE_ID")
	private String messageId;

	@Column(name = "TRIP_ID")
	private String tripId;

	@Column(name = "TITILE")
	private String title;

	@Column(name = "BODY")
	private String body;

	@Column(name = "TYPE")
	private String type;

	@Column(name = "ADDRESS")
	private String address;

	@Column(name = "DELIVERY_NOTE_ID")
	private Long deliveryNoteId;

	
	@Column(name = "CREATED_BY")
	private String createdBy;

	@Column(name = "UPDATED_BY")
	private String updatedBy;

	@Column(name = "LONGITUDE")
	private Double longitude;

	@Column(name = "LATITUDE")
	private Double latitude;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_AT")
	private Date createdAt;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATED_AT")
	private Date updatedAt;

	@OneToMany(targetEntity = CommentsDetailsDo.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@Fetch(FetchMode.SELECT)
	private List<CommentsDetailsDo> comments;

	@ManyToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
	@JoinTable(name = "MESSAGE_USERS", joinColumns = { @JoinColumn(name = "MESSAGE_ID") }, inverseJoinColumns = {
			@JoinColumn(name = "USER_ID") })
	private Set<UserDetailsDo> users=new HashSet<UserDetailsDo>(0);;

	@Override
	public Object getPrimaryKey() {
		return messageId;
	}

}
