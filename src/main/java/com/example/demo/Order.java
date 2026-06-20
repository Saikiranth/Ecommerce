package com.example.demo;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private String products;

    private double totalAmount;

    private String status;
    
    private String trackingStatus;

    @Temporal(TemporalType.TIMESTAMP)
    private Date orderDate;
    
    public Order() {
    }
    

	public Order(Long id, Long userId, String products, double totalAmount, String status, Date orderDate) {
		super();
		this.id = id;
		this.userId = userId;
		this.products = products;
		this.totalAmount = totalAmount;
		this.status = status;
		this.orderDate = orderDate;
		this.trackingStatus=trackingStatus;
	}
	
	public String getTrackingStatus() {
	    return trackingStatus;
	}
	public void setTrackingStatus(String trackingStatus) {
	    this.trackingStatus = trackingStatus;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getProducts() {
		return products;
	}

	public void setProducts(String products) {
		this.products = products;
	}

	public double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

    // getters and setters
    
    
}