package com.vbatecan.portfolio_manager.services.interfaces;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.vbatecan.portfolio_manager.exceptions.UserInvalidMatchException;
import com.vbatecan.portfolio_manager.models.entities.Project;
import com.vbatecan.portfolio_manager.models.filters.ProjectFilterInput;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;

import java.util.Optional;
import java.util.UUID;

public interface ProjectService {

	Page<Project> listAll(@NonNull Pageable pageable);

	Optional<Project> save(@NonNull Project project);

	Optional<Project> update(@NonNull UUID id, @NonNull Project updatedProject) throws JsonMappingException, UserInvalidMatchException;

	Optional<Project> delete(@NonNull UUID id) throws UserInvalidMatchException;

	Optional<Project> get(@NonNull UUID id) throws UserInvalidMatchException;

	Page<Project> filter(@NonNull ProjectFilterInput filter, Pageable pageable);
}
