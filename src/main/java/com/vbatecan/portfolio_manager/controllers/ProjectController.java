package com.vbatecan.portfolio_manager.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vbatecan.portfolio_manager.models.dto.ProjectDTO;
import com.vbatecan.portfolio_manager.models.entities.Project;
import com.vbatecan.portfolio_manager.models.input.ProjectInput;
import com.vbatecan.portfolio_manager.models.output.MessageResponse;
import com.vbatecan.portfolio_manager.services.interfaces.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/projects")
@RequiredArgsConstructor
public class ProjectController {

	private final ProjectService projectService;
	private final ObjectMapper mapper = new ObjectMapper();

	@GetMapping("")
	public ResponseEntity<?> listAll(@NonNull @PageableDefault Pageable pageable) {
		return ResponseEntity.ok(
			new PagedModel<>(projectService.listAll(pageable).map(project -> mapper.convertValue(project, ProjectDTO.class)))
		);
	}

	@PostMapping("")
	public ResponseEntity<?> save(@NonNull @RequestBody ProjectInput projectInput) {
		Project project = mapper.convertValue(projectInput, Project.class);
		Optional<Project> projectOptional = projectService.save(project);

		if ( projectOptional.isPresent() ) {
			return ResponseEntity.ok(project);
		}

		return ResponseEntity.status(HttpStatus.CONFLICT).body(new MessageResponse("Project title already exists", false));
	}
}
