package com.software.partner.fees.domain.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class SchoolQuery {
    private UUID schoolId;
}
