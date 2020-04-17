package com.ypitta.auctionsite.model;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;

@Entity
@Table(name = "auctions")
public class Auction {
	
	@Id
	private int id;
	
	@Column(name = "price")
	private double price;
	
	@OneToOne
	@MapsId
	@JoinColumn(name = "id")
	@Cascade(org.hibernate.annotations.CascadeType.ALL)
	private Product product;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "start_time")
	private Date start_time;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "end_time")
	private Date end_time;
	
	@Transient
	private boolean isActive;
	
	@OneToMany(mappedBy = "auction", orphanRemoval = true)
	@Cascade(value = org.hibernate.annotations.CascadeType.ALL)
	private List<Bid> bids;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public List<Bid> getBids() {
		return bids;
	}

	public void setBids(List<Bid> bids) {
		this.bids = bids;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	
	
	public Date getStart_time() {
		return start_time;
	}

	public void setStart_time(Date start_time) {
		this.start_time = start_time;
	}

	public Date getEnd_time() {
		return end_time;
	}

	public void setEnd_time(Date end_time) {
		this.end_time = end_time;
	}

	public boolean isActive() {
		return getStart_time().before(Calendar.getInstance().getTime()) && getEnd_time().after(Calendar.getInstance().getTime());
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
	
	
	
}
