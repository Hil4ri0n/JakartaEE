package com.hil4ri0n.carrental.model;

import com.hil4ri0n.carrental.model.enums.RentalStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Rental {
    private UUID id;
    private Vehicle vehicle;
    private User user;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private RentalStatus status;
    private BigDecimal price;
}
