package com.vbatecan.portfolio_manager.controllers;

import com.vbatecan.portfolio_manager.models.common.MessageResponse;
import com.vbatecan.portfolio_manager.models.common.PagedModel;
import com.vbatecan.portfolio_manager.models.dto.CertificateDTO;
import com.vbatecan.portfolio_manager.models.input.CertificateFilterInput;
import com.vbatecan.portfolio_manager.models.input.CertificateInput;
import com.vbatecan.portfolio_manager.services.interfaces.CertificateService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/certificates")
@RequiredArgsConstructor
public class CertificateController {

    private final CertificateService certificateService;

    @GetMapping
    public ResponseEntity<PagedModel<CertificateDTO>> listAll(@NonNull @PageableDefault(sort = "updatedAt") Pageable pageable) {
        Page<CertificateDTO> certificates = certificateService.listAll(pageable);
        return ResponseEntity.ok(new PagedModel<>(certificates));
    }

    @GetMapping("/filter")
    public ResponseEntity<PagedModel<CertificateDTO>> filter(
        @NonNull CertificateFilterInput filter,
        @NonNull @PageableDefault(sort = "updatedAt") Pageable pageable) {
        Page<CertificateDTO> certificates = certificateService.filter(filter, pageable);
        return ResponseEntity.ok(new PagedModel<>(certificates));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CertificateDTO> getById(@PathVariable UUID id) {
        return ResponseEntity.of(certificateService.get(id));
    }

    @PostMapping
    public ResponseEntity<?> save(@Valid @RequestBody CertificateInput certificateInput) {
        return certificateService.save(certificateInput)
                .<ResponseEntity<?>>map(dto -> ResponseEntity.status(HttpStatus.CREATED).body(dto))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(new MessageResponse("A certificate with this title already exists.")));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CertificateDTO> update(@PathVariable UUID id, @Valid @RequestBody CertificateInput certificateInput) {
        return ResponseEntity.of(certificateService.update(id, certificateInput));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CertificateDTO> delete(@PathVariable UUID id) {
        return ResponseEntity.of(certificateService.delete(id));
    }
}
