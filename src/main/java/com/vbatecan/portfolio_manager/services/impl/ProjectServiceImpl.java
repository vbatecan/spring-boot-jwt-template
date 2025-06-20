package com.vbatecan.portfolio_manager.services.impl;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vbatecan.portfolio_manager.exceptions.UserInvalidMatchException;
import com.vbatecan.portfolio_manager.models.dto.UserDTO;
import com.vbatecan.portfolio_manager.models.entities.Project;
import com.vbatecan.portfolio_manager.models.entities.User;
import com.vbatecan.portfolio_manager.models.filters.ProjectFilterInput;
import com.vbatecan.portfolio_manager.repositories.ProjectRepository;
import com.vbatecan.portfolio_manager.services.interfaces.AuthService;
import com.vbatecan.portfolio_manager.services.interfaces.ProjectService;
import com.vbatecan.portfolio_manager.specifications.ProjectSpecification;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

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
	@Transactional
	public Page<Project> listAll(@NonNull Pageable pageable) {
		User user = authService.getLoggedInUser();
		return projectRepository.findByUser_Id(user.getId(), pageable);
	}

	@Override
	public Optional<Project> save(@NonNull Project project) {
		if ( projectRepository.existsByTitle(project.getTitle()) ) return Optional.empty();
		project.setUser(authService.getLoggedInUser());
		return Optional.of(projectRepository.save(project));
	}

	@Override
	public Optional<Project> delete(@NonNull UUID id) throws UserInvalidMatchException {
		Optional<Project> projectOptional = projectRepository.findById(id);
		if ( projectOptional.isEmpty() ) {
			return Optional.empty();
		}

		Project project = projectOptional.get();
		if ( project.getUser().getId().equals(authService.getLoggedInUser().getId()) ) {
			projectRepository.delete(project);
		}

		throw new UserInvalidMatchException("The user of the project id from the input did not match from the currently logged in user.");
	}

	@Override
	public Optional<Project> update(@NonNull UUID id, @NonNull Project updatedProject) throws JsonMappingException, UserInvalidMatchException {
		Optional<Project> projectOptional = projectRepository.findById(id);

		if ( projectOptional.isEmpty() ) {
			return Optional.empty();
		}

		Project project = projectOptional.get();

		if ( project.getUser().getId().equals(authService.getLoggedInUser().getId()) ) {
			project = mapper.updateValue(project, updatedProject);
			return Optional.of(projectRepository.save(project));
		}

		throw new UserInvalidMatchException("The user of the project id from the input did not match from the currently logged in user id.");
	}

	@Override
	public Optional<Project> get(@NonNull UUID id) throws UserInvalidMatchException {
		Optional<Project> projectOptional = projectRepository.findById(id);

		if ( projectOptional.isEmpty() ) {
			return Optional.empty();
		}

		Project project = projectOptional.get();
		if ( project.getUser().getId().equals(authService.getLoggedInUser().getId()) ) {
			return Optional.of(project);
		}

		throw new UserInvalidMatchException("The user of the project id from the input did not match from the currently logged in user id.");
	}

	@Override
	public Page<Project> filter(@NonNull ProjectFilterInput filter, Pageable pageable) {
		User user = authService.getLoggedInUser();
		return projectRepository.findByUser_Id(ProjectSpecification.filter(filter), user.getId(), pageable);
	}
}
