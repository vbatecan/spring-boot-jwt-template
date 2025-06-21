package com.vbatecan.portfolio_manager.services.impl;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vbatecan.portfolio_manager.mappers.ProjectInputMapper;
import com.vbatecan.portfolio_manager.mappers.ProjectMapper;
import com.vbatecan.portfolio_manager.models.dto.ProjectDTO;
import com.vbatecan.portfolio_manager.models.entities.Project;
import com.vbatecan.portfolio_manager.models.entities.User;
import com.vbatecan.portfolio_manager.models.filters.ProjectFilterInput;
import com.vbatecan.portfolio_manager.models.input.ProjectInput;
import com.vbatecan.portfolio_manager.repositories.ProjectRepository;
import com.vbatecan.portfolio_manager.services.interfaces.AuthService;
import com.vbatecan.portfolio_manager.services.interfaces.ProjectService;
import com.vbatecan.portfolio_manager.specifications.ProjectSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class ProjectServiceImpl implements ProjectService {

	private final ProjectRepository projectRepository;
	private final ProjectMapper projectMapper;
	private final ProjectInputMapper projectInputMapper;
	private final AuthService authService;
	private final ObjectMapper mapper = new ObjectMapper();

	@Override
	@Transactional(readOnly = true)
	public Page<ProjectDTO> listAll(@NonNull Pageable pageable) {
		User user = authService.getLoggedInUser();
		Page<Project> projects = projectRepository.findByUser_Id(user.getId(), pageable);

		List<ProjectDTO> dtos = projects.getContent().stream()
			.map(projectMapper::toDTO)
			.collect(Collectors.toList());

		return new PageImpl<>(dtos, pageable, projects.getTotalElements());
	}

	@Override
	@Transactional
	public Optional<ProjectDTO> save(@NonNull ProjectInput projectInput) {
		User user = authService.getLoggedInUser();
		if ( projectRepository.existsByTitleAndUser_Id(projectInput.title(), user.getId()) ) {
			return Optional.empty();
		}

		Project project = projectInputMapper.toEntity(projectInput);
		project.setUser(user);

		Project savedProject = projectRepository.save(project);
		return Optional.of(projectMapper.toDTO(savedProject));
	}

	@Override
	@Transactional
	public Optional<ProjectDTO> update(@NonNull UUID id, @NonNull ProjectInput projectInput) throws JsonMappingException {
		User user = authService.getLoggedInUser();
		Optional<Project> projectOptional = projectRepository.findByIdAndUser_Id(id, user.getId());

		if ( projectOptional.isEmpty() ) {
			return Optional.empty();
		}

		Project updatedProject = mapper.updateValue(
			projectOptional.get(),
			projectInputMapper.toEntity(projectInput)
		);
		Project savedProject = projectRepository.save(updatedProject);
		return Optional.of(projectMapper.toDTO(savedProject));
	}

	@Override
	@Transactional
	public Optional<ProjectDTO> delete(@NonNull UUID id) {
		User user = authService.getLoggedInUser();
		Optional<Project> projectOptional = projectRepository.findByIdAndUser_Id(id, user.getId());

		if ( projectOptional.isEmpty() ) {
			return Optional.empty();
		}

		Project project = projectOptional.get();
		ProjectDTO projectDTO = projectMapper.toDTO(project);
		projectRepository.delete(project);

		return Optional.of(projectDTO);
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<ProjectDTO> get(@NonNull UUID id) {
		User user = authService.getLoggedInUser();
		return projectRepository.findByIdAndUser_Id(id, user.getId())
			.map(projectMapper::toDTO);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<ProjectDTO> filter(@NonNull ProjectFilterInput filter, Pageable pageable) {
		User user = authService.getLoggedInUser();
		Page<Project> projects = projectRepository.findAll(ProjectSpecification.filter(filter, user), pageable);

		List<ProjectDTO> dtos = projects.getContent().stream()
			.map(projectMapper::toDTO)
			.collect(Collectors.toList());

		return new PageImpl<>(dtos, pageable, projects.getTotalElements());
	}
}
