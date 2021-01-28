package com.eciccone.bpt.model;

import java.time.LocalDate;

/**
 * Bitcoin class model to hold price of Bitcoin on a specific date.
 * 
 * @author eddie ciccone
 *
 */
public class BitcoinPrice {
	
	private double price;
	private LocalDate date;
	
	public BitcoinPrice() {
		this(0.0, null);
	}
	
	public BitcoinPrice(double price, LocalDate date) {
		this.price = price;
		this.date = date;
	}
	
	public void setPrice(double price) {
		this.price = price;
	}
	
	public double getPrice() {
		return price;
	}
	
	public void setDate(LocalDate date) {
		this.date = date;
	}
	
	public LocalDate getDate() {
		return date;
	}
}
