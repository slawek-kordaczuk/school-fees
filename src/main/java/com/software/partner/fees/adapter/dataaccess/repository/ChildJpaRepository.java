package com.software.partner.fees.adapter.dataaccess.repository;

import com.software.partner.fees.adapter.dataaccess.entity.ChildEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ChildJpaRepository extends JpaRepository<ChildEntity, UUID> {

    List<ChildEntity> findAllBySchoolId(UUID schoolId);

    List<ChildEntity> findAllByParentId(UUID parentId);

}
