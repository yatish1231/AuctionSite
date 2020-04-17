package com.ypitta.auctionsite.util;

import java.util.Date;

import org.springframework.stereotype.Component;

@Component
public class BidForm {
	
	private int id;
	
	private double price;
	
	private Date time;

	public int getId() {
		return id;
	}
	
	
	public BidForm() {
		super();
		// TODO Auto-generated constructor stub
	}


	public BidForm(int id, double price, Date time) {
		super();
		this.id = id;
		this.price = price;
		this.time = time;
	}


	public void setId(int id) {
		this.id = id;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}
	
	
}
