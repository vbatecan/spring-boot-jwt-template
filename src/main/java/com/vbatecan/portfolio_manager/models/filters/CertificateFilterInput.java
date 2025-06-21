package com.vbatecan.portfolio_manager.models.filters;

public record CertificateFilterInput(
    String title,
    String issuer,
    String description
) {}
