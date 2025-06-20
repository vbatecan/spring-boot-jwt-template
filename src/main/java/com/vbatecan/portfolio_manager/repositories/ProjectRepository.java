package com.vbatecan.portfolio_manager.repositories;

import com.vbatecan.portfolio_manager.models.entities.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProjectRepository extends JpaRepository<Project, UUID>, JpaSpecificationExecutor<Project> {
	boolean existsByTitle(@NonNull String title);

	Page<Project> findByUser_Id(Specification<Project> spec, UUID userId, Pageable pageable);

	Page<Project> findByUser_Id(UUID id, Pageable pageable);
}
