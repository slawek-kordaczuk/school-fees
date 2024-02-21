package com.software.partner.fees.domain.entity;

import com.software.partner.fees.domain.valueobject.AttendanceId;
import com.software.partner.fees.domain.valueobject.EntryDate;
import com.software.partner.fees.domain.valueobject.ExitDate;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Attendance extends Entity<AttendanceId>{

    private Child child;
    private EntryDate entryDate;
    private ExitDate exitDate;
}
