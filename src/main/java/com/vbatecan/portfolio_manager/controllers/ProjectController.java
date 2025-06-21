package com.vbatecan.portfolio_manager.controllers;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.vbatecan.portfolio_manager.models.dto.ProjectDTO;
import com.vbatecan.portfolio_manager.models.input.ProjectInput;
import com.vbatecan.portfolio_manager.models.output.MessageResponse;
import com.vbatecan.portfolio_manager.services.interfaces.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/projects")
@RequiredArgsConstructor
@Log4j2
public class ProjectController {
	private final ProjectService projectService;

	@GetMapping("")
	public ResponseEntity<PagedModel<ProjectDTO>> listAll(@NonNull @PageableDefault Pageable pageable) {
		Page<ProjectDTO> projects = projectService.listAll(pageable);
		return ResponseEntity.ok(new PagedModel<>(projects));
	}

	@PostMapping("")
	public ResponseEntity<?> save(@NonNull @RequestBody @Valid ProjectInput projectInput) {
		Optional<ProjectDTO> projectOptional = projectService.save(projectInput);
		if ( projectOptional.isPresent() ) {
			return ResponseEntity.status(HttpStatus.CREATED).body(projectOptional.get());
		}

		return ResponseEntity.status(HttpStatus.CONFLICT).body(new MessageResponse("Project already exists", false));
	}

	@PutMapping("")
	public ResponseEntity<?> update(
		@NonNull @RequestBody @Valid ProjectInput projectInput,
		@RequestParam UUID id) throws JsonMappingException {

		Optional<ProjectDTO> projectOptional = projectService.update(id, projectInput);
		if ( projectOptional.isPresent() ) {
			return ResponseEntity.ok(projectOptional.get());
		}

		return ResponseEntity.status(HttpStatus.NOT_FOUND)
			.body(new MessageResponse("Project " + id + " not found", false));
	}

	@DeleteMapping("")
	public ResponseEntity<?> delete(@NonNull @RequestParam UUID id) {
		Optional<ProjectDTO> projectOptional = projectService.delete(id);
		if ( projectOptional.isPresent() ) {
			return ResponseEntity.ok(projectOptional.get());
		}

		return ResponseEntity.status(HttpStatus.NOT_FOUND)
			.body(new MessageResponse("Project " + id + " not found", false));
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> get(@NonNull @PathVariable("id") UUID id) {
		Optional<ProjectDTO> projectOptional = projectService.get(id);
		if ( projectOptional.isPresent() ) {
			return ResponseEntity.ok(projectOptional.get());
		}

		return ResponseEntity.status(HttpStatus.NOT_FOUND)
			.body(new MessageResponse("Project " + id + " not found", false));
	}
}
