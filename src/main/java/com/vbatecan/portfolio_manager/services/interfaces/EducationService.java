package com.vbatecan.portfolio_manager.services.interfaces;

import com.vbatecan.portfolio_manager.models.dto.EducationDTO;
import com.vbatecan.portfolio_manager.models.filter.EducationFilterInput;
import com.vbatecan.portfolio_manager.models.input.EducationInput;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

public interface EducationService {

    @Transactional(readOnly = true)
    Page<EducationDTO> listAll(@NonNull Pageable pageable);

    @Transactional(readOnly = true)
    Optional<EducationDTO> get(@NonNull UUID id);

    @Transactional
    Optional<EducationDTO> save(@NonNull EducationInput educationInput);

    @Transactional
    Optional<EducationDTO> update(@NonNull UUID id, @NonNull EducationInput educationInput);

    @Transactional
    void delete(@NonNull UUID id);

    @Transactional(readOnly = true)
    Page<EducationDTO> filter(@NonNull EducationFilterInput filter, @NonNull Pageable pageable);
}
