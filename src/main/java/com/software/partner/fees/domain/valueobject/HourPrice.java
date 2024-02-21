package com.software.partner.fees.domain.valueobject;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class HourPrice {
    private BigDecimal price;
}
