package com.software.partner.fees.adapter.dataaccess;

import com.software.partner.fees.adapter.dataaccess.entity.AttendanceEntity;
import com.software.partner.fees.adapter.dataaccess.entity.ChildEntity;
import com.software.partner.fees.adapter.dataaccess.entity.ParentEntity;
import com.software.partner.fees.adapter.dataaccess.entity.SchoolEntity;
import com.software.partner.fees.adapter.dataaccess.mapper.RepositoryMapper;
import com.software.partner.fees.adapter.dataaccess.repository.ChildJpaRepository;
import com.software.partner.fees.adapter.dataaccess.repository.SchoolJpaRepository;
import com.software.partner.fees.adapter.exception.RepositoryException;
import com.software.partner.fees.domain.entity.Attendance;
import com.software.partner.fees.domain.entity.Child;
import com.software.partner.fees.domain.entity.School;
import com.software.partner.fees.domain.ports.FeesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class RepositoryTest {

    private FeesRepository feesRepository;

    private ChildJpaRepository childJpaRepository;

    private SchoolJpaRepository schoolJpaRepository;

    @BeforeEach
    public void setUp() {
        childJpaRepository = Mockito.mock();
        schoolJpaRepository = Mockito.mock();
        RepositoryMapper repositoryMapper = new RepositoryMapper();
        feesRepository = new FeesRepositoryImpl(childJpaRepository, schoolJpaRepository, repositoryMapper);
    }

    @Test
    void testFindAllAttendanceBySchoolId(){
        //Given
        SchoolEntity school = SchoolEntity.builder()
                .hourPrice(new BigDecimal("45.00"))
                .build();

        ChildEntity child = getChildEntity(school);

        List<AttendanceEntity> attendances = getAttendanceEntities(child);

        List<ChildEntity> children = getChildEntities(school, attendances);

        when(schoolJpaRepository.findById(any(UUID.class))).thenReturn(Optional.of(school));
        when(childJpaRepository.findAllBySchoolId(any(UUID.class))).thenReturn(children);

        //When
        List<Attendance> response = feesRepository.findAllAttendanceBySchoolId(UUID.fromString("dd8bc325-c14c-453c-a8bb-7608410f5cea"));

        //Then
        assertBtSchoolIdResponse(response);
    }

    @Test
    void testFindAllChildrenByParentId(){
        //Given
        SchoolEntity school = SchoolEntity.builder()
                .hourPrice(new BigDecimal("45.00"))
                .build();

        ChildEntity childEntity = getChildEntity(school);

        List<AttendanceEntity> attendances = getAttendanceEntities(childEntity);

        List<ChildEntity> children = getChildEntities(school, attendances);
        when(childJpaRepository.findAllByParentId(any(UUID.class))).thenReturn(children);

        //When
        List<Child> response = feesRepository.findAllChildByParentId(UUID.fromString("dd8bc325-c14c-453c-a8bb-7608410f5cea"));

        //Then
        assertByParentResponse(response);
    }

    @Test
    void testFindSchoolById(){
        //Given
        SchoolEntity school = SchoolEntity.builder()
                .name("school name")
                .hourPrice(new BigDecimal("45.00"))
                .build();

        when(schoolJpaRepository.findById(any(UUID.class))).thenReturn(Optional.of(school));

        //When
        School schoolResponse = feesRepository.findSchoolById(UUID.fromString("dd8bc325-c14c-453c-a8bb-7608410f5cea"));

        //Then
        assertEquals("school name", schoolResponse.getName().getName());
        assertEquals(new BigDecimal("45.00"), schoolResponse.getHourPrice().getPrice());
    }

    @Test
    void testFindAllAttendanceBySchoolIdFailure(){
        //Given
        when(childJpaRepository.findAllBySchoolId(any(UUID.class))).thenReturn(null);

        //When&Then
        RepositoryException repositoryException = assertThrows(RepositoryException.class,
                () -> feesRepository.findAllAttendanceBySchoolId(UUID.fromString("dd8bc325-c14c-453c-a8bb-7608410f5cea")));
        assertEquals("Can't find children for school id dd8bc325-c14c-453c-a8bb-7608410f5cea",
                repositoryException.getMessage());
    }

    @Test
    void testFindAllAttendanceByParentIdFailure(){
        //Given
        when(childJpaRepository.findAllByParentId(any(UUID.class))).thenReturn(null);

        //When&Then
        RepositoryException repositoryException = assertThrows(RepositoryException.class,
                () -> feesRepository.findAllChildByParentId(UUID.fromString("dd8bc325-c14c-453c-a8bb-7608410f5cea")));
        assertEquals("Can't find children for parent id dd8bc325-c14c-453c-a8bb-7608410f5cea",
                repositoryException.getMessage());
    }

    @Test
    void testFindSchoolByIdFailure(){
        //Given
        when(childJpaRepository.findById(any(UUID.class))).thenReturn(null);

        //When&Then
        RepositoryException repositoryException = assertThrows(RepositoryException.class,
                () -> feesRepository.findSchoolById(UUID.fromString("dd8bc325-c14c-453c-a8bb-7608410f5cea")));
        assertEquals("Can't find school with id dd8bc325-c14c-453c-a8bb-7608410f5cea",
                repositoryException.getMessage());
    }

    private void assertBtSchoolIdResponse(List<Attendance> response) {
        response.forEach(attendance -> {
            assertEquals("child first name", attendance.getChild().getFirstName().getValue());
            assertEquals("child last name", attendance.getChild().getLastName().getValue());
            assertEquals(initLocalDateTime("2024-02-08 11:30"), attendance.getExitDate().getDateTime());
            assertEquals(initLocalDateTime("2024-02-08 06:30"), attendance.getEntryDate().getDateTime());
            assertEquals("parent last name", attendance.getChild().getParent().getLastName().getValue());
            assertEquals("parent first name", attendance.getChild().getParent().getFirstName().getValue());
        });
    }

    private void assertByParentResponse(List<Child> response) {
        response.forEach(child -> {
            assertEquals("child first name", child.getFirstName().getValue());
            assertEquals("child last name", child.getLastName().getValue());
            child.getAttendance().forEach(attendance -> {
                assertEquals(initLocalDateTime("2024-02-08 11:30"), attendance.getExitDate().getDateTime());
                assertEquals(initLocalDateTime("2024-02-08 06:30"), attendance.getEntryDate().getDateTime());
            });
            assertEquals("parent last name", child.getParent().getLastName().getValue());
            assertEquals("parent first name", child.getParent().getFirstName().getValue());
        });
    }

    private static List<ChildEntity> getChildEntities(SchoolEntity school, List<AttendanceEntity> attendances) {
        List<ChildEntity> children = List.of(ChildEntity.builder()
                        .school(school)
                        .parent(ParentEntity.builder()
                                .firstName("parent first name")
                                .lastName("parent last name")
                                .build())
                        .lastName("child last name")
                        .firstName("child first name")
                        .attendance(attendances)
                        .build(),
                ChildEntity.builder()
                        .school(school)
                        .parent(ParentEntity.builder()
                                .firstName("parent first name")
                                .lastName("parent last name")
                                .build())
                        .lastName("child last name")
                        .firstName("child first name")
                        .attendance(attendances)
                        .build());
        return children;
    }

    private List<AttendanceEntity> getAttendanceEntities(ChildEntity child) {
        List<AttendanceEntity> attendances = List.of(AttendanceEntity.builder()
                        .exitDate(initLocalDateTime("2024-02-08 11:30"))
                        .entryDate(initLocalDateTime("2024-02-08 06:30"))
                        .child(child)
                        .build(),
                AttendanceEntity.builder()
                        .exitDate(initLocalDateTime("2024-02-08 11:30"))
                        .entryDate(initLocalDateTime("2024-02-08 06:30"))
                        .child(child)
                        .build());
        return attendances;
    }

    private ChildEntity getChildEntity(SchoolEntity school) {
        ChildEntity child = ChildEntity.builder()
                .school(school)
                .parent(ParentEntity.builder()
                        .firstName("parent first name")
                        .lastName("parent last name")
                        .build())
                .lastName("child last name")
                .firstName("child first name")
                .attendance(List.of(AttendanceEntity.builder()
                        .exitDate(initLocalDateTime("2024-02-08 11:30"))
                        .entryDate(initLocalDateTime("2024-02-08 06:30"))
                        .build()))
                .build();
        return child;
    }

    private LocalDateTime initLocalDateTime(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return LocalDateTime.parse(date, formatter);
    }

}
