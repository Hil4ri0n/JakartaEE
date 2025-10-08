package com.hil4ri0n.carrental.model;

import com.hil4ri0n.carrental.model.enums.RentalStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Rental {
    private Vehicle vehicle;
    private User user;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private RentalStatus status;
    private BigDecimal price;
}
