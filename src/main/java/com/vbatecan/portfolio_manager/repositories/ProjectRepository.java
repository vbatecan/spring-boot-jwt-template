package com.vbatecan.portfolio_manager.repositories;

import com.vbatecan.portfolio_manager.models.entities.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProjectRepository extends JpaRepository<Project, UUID>, JpaSpecificationExecutor<Project> {
	boolean existsByTitleAndUser_Id(String title, UUID userId);

	Optional<Project> findByIdAndUser_Id(UUID id, UUID userId);

	Page<Project> findByUser_Id(UUID id, Pageable pageable);
}
