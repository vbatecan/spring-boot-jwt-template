package com.vbatecan.portfolio_manager.mappers;

import com.vbatecan.portfolio_manager.models.dto.CertificateDTO;
import com.vbatecan.portfolio_manager.models.entities.Certificate;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CertificateMapper {
    CertificateDTO toDto(Certificate certificate);
}
