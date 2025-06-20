package com.vbatecan.portfolio_manager.services.interfaces;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.vbatecan.portfolio_manager.models.entities.Project;
import com.vbatecan.portfolio_manager.models.filters.ProjectFilterInput;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

public interface ProjectService {

	@Transactional(readOnly = true)
	Page<Project> listAll(@NonNull Pageable pageable);

	@Transactional
	Optional<Project> save(@NonNull Project project);

	@Transactional
	Optional<Project> update(@NonNull UUID id, @NonNull Project updatedProject) throws JsonMappingException;

	@Transactional
	Optional<Project> delete(@NonNull UUID id);

	@Transactional(readOnly = true)
	Optional<Project> get(@NonNull UUID id);

	@Transactional(readOnly = true)
	Page<Project> filter(@NonNull ProjectFilterInput filter, Pageable pageable);
}
