package com.mongodb.demo.entity;

import java.time.LocalDateTime;

public class MaxMinCreatedDate {
    private LocalDateTime minDate;
    private LocalDateTime maxDate;
    
    
    
	public LocalDateTime getMinDate() {
		return minDate;
	}
	public void setMinDate(LocalDateTime minDate) {
		this.minDate = minDate;
	}
	public LocalDateTime getMaxDate() {
		return maxDate;
	}
	public void setMaxDate(LocalDateTime maxDate) {
		this.maxDate = maxDate;
	}

    // constructor, getters, and setters
    
    
    
    
}
