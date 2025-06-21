package com.vbatecan.portfolio_manager.services.impl;

import com.vbatecan.portfolio_manager.mappers.CertificateInputMapper;
import com.vbatecan.portfolio_manager.mappers.CertificateMapper;
import com.vbatecan.portfolio_manager.models.dto.CertificateDTO;
import com.vbatecan.portfolio_manager.models.entities.Certificate;
import com.vbatecan.portfolio_manager.models.entities.User;
import com.vbatecan.portfolio_manager.models.filters.CertificateFilterInput;
import com.vbatecan.portfolio_manager.models.input.CertificateInput;
import com.vbatecan.portfolio_manager.repositories.CertificateRepository;
import com.vbatecan.portfolio_manager.services.interfaces.AuthService;
import com.vbatecan.portfolio_manager.services.interfaces.CertificateService;
import com.vbatecan.portfolio_manager.specs.CertificateSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CertificateServiceImpl implements CertificateService {

    private final CertificateRepository certificateRepository;
    private final AuthService authService;
    private final CertificateMapper certificateMapper;
    private final CertificateInputMapper certificateInputMapper;

    @Override
    public Page<CertificateDTO> listAll(@NonNull Pageable pageable) {
        User user = authService.getLoggedInUser();
        Page<Certificate> certificates = certificateRepository.findByUser_Id(user.getId(), pageable);
        List<CertificateDTO> dtos = certificates.getContent().stream()
                .map(certificateMapper::toDTO)
                .collect(Collectors.toList());
        return new PageImpl<>(dtos, pageable, certificates.getTotalElements());
    }

    @Override
    public Optional<CertificateDTO> get(@NonNull UUID id) {
        User user = authService.getLoggedInUser();
        return certificateRepository.findByIdAndUser_Id(id, user.getId())
                .map(certificateMapper::toDTO);
    }

    @Override
    public Optional<CertificateDTO> save(@NonNull CertificateInput certificateInput) {
        User user = authService.getLoggedInUser();
        if (certificateRepository.existsByTitleAndUser_Id(certificateInput.title(), user.getId())) {
            return Optional.empty(); // Conflict: title already exists for this user
        }
        Certificate certificate = certificateInputMapper.toEntity(certificateInput);
        certificate.setUser(user);
        Certificate savedCertificate = certificateRepository.save(certificate);
        return Optional.of(certificateMapper.toDTO(savedCertificate));
    }

    @Override
    public Optional<CertificateDTO> update(@NonNull UUID id, @NonNull CertificateInput certificateInput) {
        User user = authService.getLoggedInUser();
        return certificateRepository.findByIdAndUser_Id(id, user.getId())
                .map(existingCertificate -> {
                    certificateInputMapper.updateEntityFromInput(certificateInput, existingCertificate);
                    Certificate updatedCertificate = certificateRepository.save(existingCertificate);
                    return certificateMapper.toDTO(updatedCertificate);
                });
    }

    @Override
    public Optional<CertificateDTO> delete(@NonNull UUID id) {
        User user = authService.getLoggedInUser();
        Optional<Certificate> certificateOpt = certificateRepository.findByIdAndUser_Id(id, user.getId());
        if (certificateOpt.isEmpty()) {
            return Optional.empty();
        }
        Certificate certificate = certificateOpt.get();
        CertificateDTO dto = certificateMapper.toDTO(certificate);
        certificateRepository.delete(certificate);
        return Optional.of(dto);
    }

    @Override
    public Page<CertificateDTO> filter(@NonNull CertificateFilterInput filter, @NonNull Pageable pageable) {
        User user = authService.getLoggedInUser();
        Specification<Certificate> spec = CertificateSpecification.filter(filter, user);
        Page<Certificate> certificates = certificateRepository.findAll(spec, pageable);
        List<CertificateDTO> dtos = certificates.getContent().stream()
                .map(certificateMapper::toDTO)
                .collect(Collectors.toList());
        return new PageImpl<>(dtos, pageable, certificates.getTotalElements());
    }
}
