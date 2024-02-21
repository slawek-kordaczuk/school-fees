package com.software.partner.fees.domain.ports;

import com.software.partner.fees.domain.entity.Attendance;
import com.software.partner.fees.domain.entity.Child;
import com.software.partner.fees.domain.entity.School;

import java.util.List;
import java.util.UUID;

public interface FeesRepository {

    List<Attendance> findAllAttendanceBySchoolId(UUID schoolId);
    List<Child> findAllChildByParentId(UUID parentId);

    School findSchoolById(UUID schoolId);
}
