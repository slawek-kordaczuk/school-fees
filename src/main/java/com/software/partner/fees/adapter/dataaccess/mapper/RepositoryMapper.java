package com.software.partner.fees.adapter.dataaccess.mapper;

import com.software.partner.fees.adapter.dataaccess.entity.AttendanceEntity;
import com.software.partner.fees.adapter.dataaccess.entity.ChildEntity;
import com.software.partner.fees.adapter.dataaccess.entity.SchoolEntity;
import com.software.partner.fees.domain.entity.Attendance;
import com.software.partner.fees.domain.entity.Child;
import com.software.partner.fees.domain.entity.Parent;
import com.software.partner.fees.domain.entity.School;
import com.software.partner.fees.domain.valueobject.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class RepositoryMapper {

    public List<Attendance> attendanceEntityToAttendance(List<AttendanceEntity> attendanceEntities){
        List<Attendance> attendances = new ArrayList<>();
        attendanceEntities.forEach(attendanceEntity -> attendances.add(attendanceEntityToAttendance(attendanceEntity)));
        return attendances;
    }

    public List<Child> childEntityToChild(List<ChildEntity> childEntities) {
        List<Child> children = new ArrayList<>();
        childEntities.forEach(childEntity -> children.add(childEntityToChild(childEntity)));
        return children;
    }

    public School schoolEntityToSchool(SchoolEntity schoolEntity) {
        return School.builder()
                .schoolId(new SchoolId(schoolEntity.getId()))
                .hourPrice(new HourPrice(schoolEntity.getHourPrice()))
                .name(new Name(schoolEntity.getName()))
                .build();
    }

    private Attendance attendanceEntityToAttendance(AttendanceEntity attendanceEntity) {
        return Attendance.builder()
                .child(childEntityToChild(attendanceEntity.getChild()))
                .entryDate(new EntryDate(attendanceEntity.getEntryDate()))
                .exitDate(new ExitDate(attendanceEntity.getExitDate()))
                .build();
    }

    private Attendance attendanceEntityToAttendanceWithoutChild(AttendanceEntity attendanceEntity) {
        return Attendance.builder()
                .entryDate(new EntryDate(attendanceEntity.getEntryDate()))
                .exitDate(new ExitDate(attendanceEntity.getExitDate()))
                .build();
    }

    private Child childEntityToChild(ChildEntity childEntity) {
        Parent parent = Parent.builder()
                .parentId(new ParentId(childEntity.getParent().getId()))
                .firstName(new FirstName(childEntity.getParent().getFirstName()))
                .lastName(new LastName(childEntity.getParent().getLastName()))
                .build();
        School school = School.builder()
                .schoolId(new SchoolId(childEntity.getSchool().getId()))
                .name(new Name(childEntity.getSchool().getName()))
                .hourPrice(new HourPrice(childEntity.getSchool().getHourPrice()))
                .build();
        return Child.builder()
                .childId(new ChildId(childEntity.getId()))
                .school(school)
                .parent(parent)
                .firstName(new FirstName(childEntity.getFirstName()))
                .lastName(new LastName(childEntity.getLastName()))
                .attendance(attendanceEntityToAttendanceWithoutChild(childEntity.getAttendance()))
                .build();
    }

    private List<Attendance> attendanceEntityToAttendanceWithoutChild(List<AttendanceEntity> attendanceEntities){
        List<Attendance> attendances = new ArrayList<>();
        attendanceEntities.forEach(attendanceEntity -> attendances.add(attendanceEntityToAttendanceWithoutChild(attendanceEntity)));
        return attendances;
    }
}
