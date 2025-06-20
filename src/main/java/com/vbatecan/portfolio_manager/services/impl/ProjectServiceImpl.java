package com.vbatecan.portfolio_manager.services.impl;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vbatecan.portfolio_manager.models.entities.Project;
import com.vbatecan.portfolio_manager.models.entities.User;
import com.vbatecan.portfolio_manager.models.filters.ProjectFilterInput;
import com.vbatecan.portfolio_manager.repositories.ProjectRepository;
import com.vbatecan.portfolio_manager.services.interfaces.AuthService;
import com.vbatecan.portfolio_manager.services.interfaces.ProjectService;
import com.vbatecan.portfolio_manager.specifications.ProjectSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Log4j2
public class ProjectServiceImpl implements ProjectService {

	private final ProjectRepository projectRepository;
	private final ObjectMapper mapper = new ObjectMapper();
	private final AuthService authService;

	@Override
	public Page<Project> listAll(@NonNull Pageable pageable) {
		User user = authService.getLoggedInUser();
		return projectRepository.findByUser_Id(user.getId(), pageable);
	}

	@Override
	public Optional<Project> save(@NonNull Project project) {
		User user = authService.getLoggedInUser();
		if ( projectRepository.existsByTitleAndUser_Id(project.getTitle(), user.getId()) ) return Optional.empty();
		project.setUser(user);
		return Optional.of(projectRepository.save(project));
	}

	@Override
	public Optional<Project> delete(@NonNull UUID id) {
		User user = authService.getLoggedInUser();

		Optional<Project> projectOptional = projectRepository.findByIdAndUser_Id(id, user.getId());
		if ( projectOptional.isEmpty() ) {
			return Optional.empty();
		}

		Project project = projectOptional.get();
		projectRepository.delete(project);
		return projectOptional;
	}

	@Override
	public Optional<Project> update(@NonNull UUID id, @NonNull Project updatedProject) throws JsonMappingException {
		User user = authService.getLoggedInUser();
		Optional<Project> projectOptional = projectRepository.findByIdAndUser_Id(id, user.getId());

		if ( projectOptional.isEmpty() ) {
			return Optional.empty();
		}

		Project project = projectOptional.get();
		project = mapper.updateValue(project, updatedProject);
		return Optional.of(projectRepository.save(project));
	}

	@Override
	public Optional<Project> get(@NonNull UUID id) {
		User user = authService.getLoggedInUser();
		Optional<Project> projectOptional = projectRepository.findByIdAndUser_Id(id, user.getId());

		if ( projectOptional.isEmpty() ) {
			return Optional.empty();
		}

		Project project = projectOptional.get();
		return Optional.of(project);
	}

	@Override
	public Page<Project> filter(@NonNull ProjectFilterInput filter, Pageable pageable) {
		User user = authService.getLoggedInUser();
		return projectRepository.findAll(ProjectSpecification.filter(filter, user), pageable);
	}
}
