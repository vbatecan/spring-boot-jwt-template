package com.vbatecan.portfolio_manager.controllers;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.vbatecan.portfolio_manager.mappers.ProjectMapper;
import com.vbatecan.portfolio_manager.models.dto.ProjectDTO;
import com.vbatecan.portfolio_manager.models.entities.Project;
import com.vbatecan.portfolio_manager.models.input.ProjectInput;
import com.vbatecan.portfolio_manager.models.output.MessageResponse;
import com.vbatecan.portfolio_manager.services.interfaces.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/projects")
@RequiredArgsConstructor
@Log4j2
public class ProjectController {
	private final ProjectMapper projectMapper;
	private final ProjectService projectService;

	@GetMapping("")
	public ResponseEntity<?> listAll(@NonNull @PageableDefault Pageable pageable) {
		return ResponseEntity.ok(
			new PagedModel<>(projectService.listAll(pageable).map(projectMapper::toDTO))
		);
	}

	@PostMapping("")
	public ResponseEntity<?> save(@NonNull @RequestBody @Valid ProjectInput projectInput) {
		Project project = projectMapper.toEntity(projectInput);
		Optional<ProjectDTO> projectOptional = projectService.save(
			project
		);

		if ( projectOptional.isPresent() ) {
			return ResponseEntity.ok(projectMapper.toDTO(projectOptional.get()));
		}

		return ResponseEntity.status(HttpStatus.CONFLICT).body(new MessageResponse("Project title already exists", false));
	}

	@PutMapping("")
	public ResponseEntity<?> update(@NonNull @RequestBody @Valid ProjectInput projectInput, @RequestParam UUID id) throws JsonMappingException {
		Project project = projectMapper.toEntity(projectInput);

		Optional<Project> projectOptional = projectService.update(id, project);

		if ( projectOptional.isPresent() ) {
			return ResponseEntity.ok(projectMapper.toDTO(projectOptional.get()));
		}

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Project " + id + " not found", false));
	}

	@DeleteMapping("")
	public ResponseEntity<?> delete(@NonNull @RequestParam UUID id) {
		Optional<Project> projectOptional = projectService.delete(id);
		if ( projectOptional.isPresent() ) {
			return ResponseEntity.ok(projectMapper.toDTO(projectOptional.get()));
		}

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Project " + id + " not found", false));
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> get(@NonNull @PathVariable("id") UUID id) {
		Optional<Project> projectOptional = projectService.get(id);
		if ( projectOptional.isPresent() ) {
			return ResponseEntity.ok(projectMapper.toDTO(projectOptional.get()));
		}

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Project " + id + " not found", false));
	}
}
