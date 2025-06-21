package com.vbatecan.portfolio_manager.models.input;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CertificateInput(
    @NotBlank(message = "Title cannot be blank")
    @Size(max = 255, message = "Title should be less than 255 characters.")
    String title,

    @NotBlank(message = "Issuer cannot be blank")
    @Size(max = 255, message = "Issuer should be less than 255 characters.")
    String issuer,

    @NotBlank(message = "Credential ID cannot be blank")
    @Size(max = 255, message = "Credential ID should be less than 255 characters.")
    String credentialId,

    @NotBlank(message = "Credential URL cannot be blank")
    @Size(max = 255, message = "Credential URL should be less than 255 characters.")
    String credentialUrl,

    @NotBlank(message = "Image URL cannot be blank")
    String image,

    String description
) {}
