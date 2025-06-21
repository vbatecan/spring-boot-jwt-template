package com.vbatecan.portfolio_manager.models.input;

public record CertificateFilterInput(
    String title,
    String issuer,
    String description
) {}
