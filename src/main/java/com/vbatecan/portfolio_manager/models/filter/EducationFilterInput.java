package com.vbatecan.portfolio_manager.models.filter;

import com.fasterxml.jackson.annotation.JsonProperty;

public record EducationFilterInput(
    @JsonProperty("title")
    String title,

    @JsonProperty("institution")
    String institution
) {
}
