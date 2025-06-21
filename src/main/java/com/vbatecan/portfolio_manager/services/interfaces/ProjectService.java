package com.vbatecan.portfolio_manager.services.interfaces;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.vbatecan.portfolio_manager.models.dto.ProjectDTO;
import com.vbatecan.portfolio_manager.models.input.ProjectInput;
import com.vbatecan.portfolio_manager.models.filters.ProjectFilterInput;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

public interface ProjectService {

    Page<ProjectDTO> listAll(@NonNull Pageable pageable);

    @Transactional
    Optional<ProjectDTO> save(@NonNull ProjectInput projectInput);

    @Transactional
    Optional<ProjectDTO> update(@NonNull UUID id, @NonNull ProjectInput projectInput) throws JsonMappingException;

    @Transactional
    Optional<ProjectDTO> delete(@NonNull UUID id);

    @Transactional(readOnly = true)
    Optional<ProjectDTO> get(@NonNull UUID id);

    @Transactional(readOnly = true)
    Page<ProjectDTO> filter(@NonNull ProjectFilterInput filter, Pageable pageable);
}
