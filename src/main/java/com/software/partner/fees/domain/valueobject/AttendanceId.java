package com.software.partner.fees.domain.valueobject;

import java.util.UUID;

public class AttendanceId extends BaseId<UUID>{
    protected AttendanceId(UUID value) {
        super(value);
    }
}
