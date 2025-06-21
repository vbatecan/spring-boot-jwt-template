package com.vbatecan.portfolio_manager.repositories;

import com.vbatecan.portfolio_manager.models.entities.Education;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface EducationRepository extends JpaRepository<Education, UUID>, JpaSpecificationExecutor<Education> {

    Optional<Education> findByIdAndUser_Id(UUID id, UUID userId);

    Page<Education> findByUser_Id(UUID userId, Pageable pageable);

    boolean existsByTitleAndInstitutionAndUser_Id(String title, String institution, UUID userId);
}
