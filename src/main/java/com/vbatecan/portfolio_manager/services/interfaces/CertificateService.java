package com.vbatecan.portfolio_manager.services.interfaces;

import com.vbatecan.portfolio_manager.models.dto.CertificateDTO;
import com.vbatecan.portfolio_manager.models.input.CertificateFilterInput;
import com.vbatecan.portfolio_manager.models.input.CertificateInput;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

public interface CertificateService {

    @Transactional(readOnly = true)
    Page<CertificateDTO> listAll(@NonNull Pageable pageable);

    @Transactional(readOnly = true)
    Optional<CertificateDTO> get(@NonNull UUID id);

    @Transactional
    Optional<CertificateDTO> save(@NonNull CertificateInput certificateInput);

    @Transactional
    Optional<CertificateDTO> update(@NonNull UUID id, @NonNull CertificateInput certificateInput);

    @Transactional
    Optional<CertificateDTO> delete(@NonNull UUID id);

    @Transactional(readOnly = true)
    Page<CertificateDTO> filter(@NonNull CertificateFilterInput filter, @NonNull Pageable pageable);
}
