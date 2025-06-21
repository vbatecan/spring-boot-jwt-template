package com.vbatecan.portfolio_manager.web;

import com.vbatecan.portfolio_manager.models.dto.EducationDTO;
import com.vbatecan.portfolio_manager.models.filter.EducationFilterInput;
import com.vbatecan.portfolio_manager.models.input.EducationInput;
import com.vbatecan.portfolio_manager.services.interfaces.EducationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/educations")
@RequiredArgsConstructor
public class EducationController {

    private final EducationService educationService;

    @GetMapping
    public ResponseEntity<Page<EducationDTO>> listAll(@NonNull @PageableDefault(size = 10) Pageable pageable) {
        Page<EducationDTO> educations = educationService.listAll(pageable);
        return ResponseEntity.ok(educations);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EducationDTO> getById(@PathVariable UUID id) {
        return educationService.get(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<EducationDTO> create(@Valid @RequestBody EducationInput educationInput) {
        return educationService.save(educationInput)
                .map(educationDTO -> {
                    URI location = ServletUriComponentsBuilder
                            .fromCurrentRequest()
                            .path("/{id}")
                            .buildAndExpand(educationDTO.id())
                            .toUri();
                    return ResponseEntity.created(location).body(educationDTO);
                })
                .orElse(ResponseEntity.badRequest().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<EducationDTO> update(@PathVariable UUID id, @Valid @RequestBody EducationInput educationInput) {
        return educationService.update(id, educationInput)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        educationService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/filter")
    public ResponseEntity<Page<EducationDTO>> filter(
            @NonNull EducationFilterInput filter,
            @NonNull @PageableDefault(size = 10) Pageable pageable) {
        Page<EducationDTO> educations = educationService.filter(filter, pageable);
        return ResponseEntity.ok(educations);
    }
}
