package com.software.partner.fees.domain.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Builder
public class ParentFeesResponse {
    private String firstName;
    private String lastName;
    private BigDecimal sumFees;
    private List<ChildFeesResponse> children;
}
