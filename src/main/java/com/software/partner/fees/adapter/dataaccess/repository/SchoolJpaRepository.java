package com.software.partner.fees.adapter.dataaccess.repository;

import com.software.partner.fees.adapter.dataaccess.entity.SchoolEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SchoolJpaRepository extends JpaRepository<SchoolEntity, UUID> {
}
