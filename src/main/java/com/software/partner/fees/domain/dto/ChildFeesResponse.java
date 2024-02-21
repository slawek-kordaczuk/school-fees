package com.software.partner.fees.domain.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class ChildFeesResponse {
    private String firstName;
    private String lastName;
    private BigDecimal sumFees;
    private Long spentHours;
}
