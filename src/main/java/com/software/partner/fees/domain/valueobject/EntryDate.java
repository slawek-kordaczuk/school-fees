package com.software.partner.fees.domain.valueobject;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class EntryDate {
    private LocalDateTime dateTime;
}
