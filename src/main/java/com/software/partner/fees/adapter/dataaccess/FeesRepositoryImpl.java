package com.software.partner.fees.adapter.dataaccess;

import com.software.partner.fees.adapter.dataaccess.entity.AttendanceEntity;
import com.software.partner.fees.adapter.dataaccess.entity.ChildEntity;
import com.software.partner.fees.adapter.dataaccess.entity.SchoolEntity;
import com.software.partner.fees.adapter.dataaccess.mapper.RepositoryMapper;
import com.software.partner.fees.adapter.dataaccess.repository.ChildJpaRepository;
import com.software.partner.fees.adapter.dataaccess.repository.SchoolJpaRepository;
import com.software.partner.fees.adapter.exception.RepositoryException;
import com.software.partner.fees.domain.entity.Attendance;
import com.software.partner.fees.domain.entity.Child;
import com.software.partner.fees.domain.entity.School;
import com.software.partner.fees.domain.ports.FeesRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class FeesRepositoryImpl implements FeesRepository {

    private final ChildJpaRepository childJpaRepository;

    private final SchoolJpaRepository schoolJpaRepository;

    private final RepositoryMapper mapper;

    public FeesRepositoryImpl(ChildJpaRepository childJpaRepository, SchoolJpaRepository schoolJpaRepository, RepositoryMapper mapper) {
        this.childJpaRepository = childJpaRepository;
        this.schoolJpaRepository = schoolJpaRepository;
        this.mapper = mapper;
    }

    @Override
    public List<Attendance> findAllAttendanceBySchoolId(UUID schoolId) {
        List<ChildEntity> childEntities = childJpaRepository
                .findAllBySchoolId(schoolId);
        if (childEntities == null || childEntities.isEmpty()) {
            throw new RepositoryException("Can't find children for school id " + schoolId);
        }
        return mapper.attendanceEntityToAttendance(
                getAttendancesFromChildEntities(childEntities));
    }

    @Override
    public List<Child> findAllChildByParentId(UUID parentId) {
        List<ChildEntity> childEntities = childJpaRepository
                .findAllByParentId(parentId);
        if (childEntities == null || childEntities.isEmpty()) {
            throw new RepositoryException("Can't find children for parent id " + parentId);
        }
        return mapper.childEntityToChild(childEntities);
    }

    @Override
    public School findSchoolById(UUID schoolId) {
        Optional<SchoolEntity> schoolEntity = schoolJpaRepository.findById(schoolId);
        if (schoolEntity.isEmpty()) {
            throw new RepositoryException("Can't find school with id " + schoolId);
        }
        return mapper.schoolEntityToSchool(schoolEntity.get());
    }

    private List<AttendanceEntity> getAttendancesFromChildEntities(List<ChildEntity> childEntities) {
        return childEntities
                .stream()
                .map(ChildEntity::getAttendance)
                .toList()
                .stream()
                .flatMap(List::stream)
                .toList();
    }
}
