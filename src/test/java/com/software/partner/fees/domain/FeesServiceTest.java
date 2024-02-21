package com.software.partner.fees.domain;

import com.software.partner.fees.domain.dto.ParentQuery;
import com.software.partner.fees.domain.dto.ParentFeesResponse;
import com.software.partner.fees.domain.dto.SchoolQuery;
import com.software.partner.fees.domain.dto.SchoolFeesResponse;
import com.software.partner.fees.domain.entity.Attendance;
import com.software.partner.fees.domain.entity.Child;
import com.software.partner.fees.domain.entity.Parent;
import com.software.partner.fees.domain.entity.School;
import com.software.partner.fees.domain.ports.FeesRepository;
import com.software.partner.fees.domain.ports.FeesService;
import com.software.partner.fees.domain.valueobject.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class FeesServiceTest {

    private FeesRepository feesRepository;

    private FeesService feesService;

    @BeforeEach
    public void setUp() {
        feesRepository = Mockito.mock();
        feesService = new FeesServiceImpl(feesRepository);
    }

    @Test
    public void testCalculateSchoolFees() {
        //Given
        School school = School.builder()
                .hourPrice(new HourPrice(new BigDecimal("45.00")))
                .build();

        Child child = getChild(school);

        List<Attendance> attendances = getAttendances(child);

        when(feesRepository.findSchoolById(any(UUID.class))).thenReturn(school);
        when(feesRepository.findAllAttendanceBySchoolId(any(UUID.class))).thenReturn(attendances);

        //When
        SchoolFeesResponse response = feesService.schoolFees(SchoolQuery.builder()
                .schoolId(UUID.fromString("f0b45dd0-d812-499b-97e1-9133b9c9e776"))
                .build());

        //Then
        assertSchoolResponse(response);
    }


    @Test
    public void testCalculateParentFees() {
        //Given
        School school = School.builder()
                .hourPrice(new HourPrice(new BigDecimal("45.00")))
                .build();

        List<Attendance> attendances_one = List.of(Attendance.builder()
                .exitDate(new ExitDate(initLocalDateTime("2024-02-08 12:30")))
                .entryDate(new EntryDate(initLocalDateTime("2024-02-08 09:30")))
                .build());

        List<Attendance> attendances_two = List.of(Attendance.builder()
                .exitDate(new ExitDate(initLocalDateTime("2024-02-08 11:30")))
                .entryDate(new EntryDate(initLocalDateTime("2024-02-08 06:30")))
                .build());

        List<Child> children = getChildren(school, attendances_one, attendances_two);

        when(feesRepository.findAllChildByParentId(any(UUID.class))).thenReturn(children);

        //When
        ParentFeesResponse response = feesService.parentFees(ParentQuery.builder()
                .parentId(UUID.fromString("f0b45dd0-d812-499b-97e1-9133b9c9e776"))
                .build());

        //Then
        assertParentFeesResponse(response);
    }

    private static void assertSchoolResponse(SchoolFeesResponse response) {
        response.getParents().forEach(parent -> {
            assertEquals("parent first name", parent.getFirstName());
            assertEquals("parent last name", parent.getLastName());
            assertEquals(new BigDecimal("90.00"), parent.getSumFees());
            parent.getChildren().forEach(responseChild -> {
                assertEquals(new BigDecimal("90.00"), responseChild.getSumFees());
                assertEquals(2L, responseChild.getSpentHours());
            });
        });
        assertEquals(new BigDecimal("90.00"), response.getSumFees());
    }

    private List<Attendance> getAttendances(Child child) {
        List<Attendance> attendances = List.of(Attendance.builder()
                        .child(child)
                        .exitDate(new ExitDate(initLocalDateTime("2024-02-08 12:30")))
                        .entryDate(new EntryDate(initLocalDateTime("2024-02-08 09:30")))
                        .build(),
                Attendance.builder()
                        .child(child)
                        .exitDate(new ExitDate(initLocalDateTime("2024-02-08 11:30")))
                        .entryDate(new EntryDate(initLocalDateTime("2024-02-08 06:30")))
                        .build());
        return attendances;
    }

    private static Child getChild(School school) {
        Child child = Child.builder()
                .school(school)
                .parent(Parent.builder()
                        .firstName(new FirstName("parent first name"))
                        .lastName(new LastName("parent last name"))
                        .build())
                .lastName(new LastName("child last name"))
                .firstName(new FirstName("child first name"))
                .build();
        return child;
    }

    private static void assertParentFeesResponse(ParentFeesResponse response) {
        response.getChildren().forEach(child -> {
            assertEquals(1L, child.getSpentHours());
            assertEquals(new BigDecimal("45.00"), child.getSumFees());
        });
        assertEquals(new BigDecimal("90.00"), response.getSumFees());
    }

    private static List<Child> getChildren(School school, List<Attendance> attendances_one, List<Attendance> attendances_two) {
        List<Child> children = List.of(Child.builder()
                        .school(school)
                        .parent(Parent.builder()
                                .firstName(new FirstName("parent first name"))
                                .lastName(new LastName("parent last name"))
                                .build())
                        .lastName(new LastName("child last name"))
                        .firstName(new FirstName("child first name"))
                        .attendance(attendances_one)
                        .build(),
                Child.builder()
                        .school(school)
                        .parent(Parent.builder()
                                .firstName(new FirstName("parent first name"))
                                .lastName(new LastName("parent last name"))
                                .build())
                        .lastName(new LastName("child last name"))
                        .firstName(new FirstName("child first name"))
                        .attendance(attendances_two)
                        .build());
        return children;
    }

    private LocalDateTime initLocalDateTime(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return LocalDateTime.parse(date, formatter);
    }
}
