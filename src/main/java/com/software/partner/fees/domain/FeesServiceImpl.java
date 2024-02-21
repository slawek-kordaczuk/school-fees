package com.software.partner.fees.domain;

import com.software.partner.fees.domain.dto.*;
import com.software.partner.fees.domain.entity.Attendance;
import com.software.partner.fees.domain.entity.Child;
import com.software.partner.fees.domain.entity.Parent;
import com.software.partner.fees.domain.entity.School;
import com.software.partner.fees.domain.exception.DomainException;
import com.software.partner.fees.domain.ports.FeesRepository;
import com.software.partner.fees.domain.ports.FeesService;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.stream.Collectors.groupingBy;

@Component
public class FeesServiceImpl implements FeesService {

    private final FeesRepository feesRepository;

    public FeesServiceImpl(FeesRepository feesRepository) {
        this.feesRepository = feesRepository;
    }

    @Override
    public SchoolFeesResponse schoolFees(SchoolQuery schoolQuery) {
        School school = feesRepository.findSchoolById(schoolQuery.getSchoolId());
        List<Attendance> attendancesBySchool = feesRepository.findAllAttendanceBySchoolId(schoolQuery.getSchoolId());
        return getSchoolFeesResponse(school, attendancesBySchool);
    }

    @Override
    public ParentFeesResponse parentFees(ParentQuery parentQuery) {
        List<Child> children = feesRepository.findAllChildByParentId(parentQuery.getParentId());
        School school = getSchool(children);
        Parent parent = getParent(children);
        List<Attendance> parentAttendance = getParentAttendance(children);
        long spentHours = calculateSpentPayedHours(parentAttendance);
        BigDecimal sumFees = calculateFees(school.getHourPrice().getPrice(), spentHours);
        return getParentFeesResponse(parent, sumFees, childFeesResponses(children, school.getHourPrice().getPrice()));
    }

    private Parent getParent(List<Child> children) {
        Optional<Parent> parent = children.stream()
                .map(Child::getParent)
                .findAny();
        if (parent.isEmpty()) {
            throw new DomainException("There is no any parent for children");
        }
        return parent.get();
    }

    private School getSchool(List<Child> children) {
        Optional<School> school = children.stream()
                .map(Child::getSchool)
                .findAny();
        if (school.isEmpty()) {
            throw new DomainException("There is no any school for children");
        }
        return school.get();
    }

    private static List<Attendance> getParentAttendance(List<Child> children) {
        return children.stream()
                        .map(Child::getAttendance)
                        .toList()
                        .stream()
                        .flatMap(List::stream)
                        .toList();
    }

    private ParentFeesResponse getParentFeesResponse(Parent parent, BigDecimal sumFees, List<ChildFeesResponse> children) {
        return ParentFeesResponse.builder()
                .firstName(parent.getFirstName().getValue())
                .lastName(parent.getLastName().getValue())
                .sumFees(sumFees)
                .children(children)
                .build();
    }

    private SchoolFeesResponse getSchoolFeesResponse(School school, List<Attendance> attendancesBySchool) {
        return SchoolFeesResponse.builder()
                .sumFees(calculateFees(school
                                .getHourPrice()
                                .getPrice(),
                        calculateSpentPayedHours(attendancesBySchool)))
                .parents(parentFeesResponse(attendancesBySchool
                                .stream()
                                .collect(groupingBy(attendance -> attendance
                                        .getChild()
                                        .getParent())),
                        school.getHourPrice()
                                .getPrice()))
                .build();
    }

    private List<ChildFeesResponse> childFeesResponses(List<Child> children, BigDecimal feeRate) {
        List<ChildFeesResponse> childFeesRespons = new ArrayList<>();
        children.forEach(child -> {
            List<Attendance> attendances = child.getAttendance();
            long spentHours = calculateSpentPayedHours(attendances);
            BigDecimal sumFees = calculateFees(feeRate, spentHours);
            childFeesRespons.add(ChildFeesResponse.builder()
                    .sumFees(sumFees)
                    .spentHours(spentHours)
                    .lastName(child.getLastName().getValue())
                    .firstName(child.getFirstName().getValue())
                    .build());
        });
        return childFeesRespons;
    }

    private List<ParentFeesResponse> parentFeesResponse(Map<Parent, List<Attendance>> groupedByParent, BigDecimal feeRate) {
        List<ParentFeesResponse> parentResponses = new ArrayList<>();
        groupedByParent.forEach((k, v) -> {
            long spentHours = calculateSpentPayedHours(v);
            BigDecimal sumFees = calculateFees(feeRate, spentHours);
            Map<Child, List<Attendance>> groupedByChild = v
                    .stream()
                    .collect(groupingBy(Attendance::getChild));
            parentResponses.add(getParentFeesResponse(k, sumFees, childFeesResponses(groupedByChild, feeRate)));
        });
        return parentResponses;
    }

    private List<ChildFeesResponse> childFeesResponses(Map<Child, List<Attendance>> groupedByChild, BigDecimal feeRate) {
        List<ChildFeesResponse> childFeesRespons = new ArrayList<>();
        groupedByChild.forEach((k, v) -> {
            long spentHours = calculateSpentPayedHours(v);
            BigDecimal sumFees = calculateFees(feeRate, spentHours);
            childFeesRespons.add(ChildFeesResponse
                    .builder()
                    .firstName(k.getFirstName().getValue())
                    .lastName(k.getLastName().getValue())
                    .sumFees(sumFees)
                    .spentHours(spentHours)
                    .build());
        });
        return childFeesRespons;
    }

    private long calculateSpentPayedHours(List<Attendance> attendances) {
        long hoursSum = 0L;
        LocalTime startFreeHour = LocalTime.parse("07:00");
        LocalTime endFreeHour = LocalTime.parse("12:00");
        for (Attendance attendance : attendances) {
            LocalTime enterTime = attendance.getEntryDate().getDateTime().toLocalTime();
            LocalTime exitTime = attendance.getExitDate().getDateTime().toLocalTime();
            if (enterTime.isBefore(startFreeHour)) {
                hoursSum += sumPayedHours(enterTime, startFreeHour);
            }
            if (exitTime.isAfter(endFreeHour)) {
                hoursSum += sumPayedHours(endFreeHour, exitTime);
            }
        }
        return hoursSum;
    }

    private long sumPayedHours(LocalTime startHour, LocalTime endHour) {
        long minutes = Duration.between(startHour,
                        endHour)
                .toMinutes();
        if (minutes < 60) {
            return 1L;
        }
        return minutes / 60;
    }

    private BigDecimal calculateFees(BigDecimal pricePerHour, long spentHour) {
        return pricePerHour.multiply(BigDecimal.valueOf(spentHour)).setScale(2, RoundingMode.HALF_EVEN);
    }
}


