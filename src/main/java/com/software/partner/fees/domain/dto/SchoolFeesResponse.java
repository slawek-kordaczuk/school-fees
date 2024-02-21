package com.software.partner.fees.domain.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Builder
public class SchoolFeesResponse {
    private BigDecimal sumFees;
    private List<ParentFeesResponse> parents;
}
