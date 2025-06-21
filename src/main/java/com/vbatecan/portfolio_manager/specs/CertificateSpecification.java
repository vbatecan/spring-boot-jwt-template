package com.vbatecan.portfolio_manager.specs;

import com.vbatecan.portfolio_manager.models.entities.Certificate;
import com.vbatecan.portfolio_manager.models.entities.User;
import com.vbatecan.portfolio_manager.models.filters.CertificateFilterInput;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;

import java.util.ArrayList;
import java.util.List;

public final class CertificateSpecification {

    private CertificateSpecification() {}

    public static Specification<Certificate> filter(@NonNull CertificateFilterInput filter, @NonNull User user) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // CRITICAL SECURITY RULE: Always add a predicate to enforce data ownership.
            predicates.add(cb.equal(root.get("user").get("id"), user.getId()));

            if (filter.title() != null && !filter.title().isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("title")), "%" + filter.title().toLowerCase() + "%"));
            }

            if (filter.issuer() != null && !filter.issuer().isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("issuer")), "%" + filter.issuer().toLowerCase() + "%"));
            }

            if (filter.description() != null && !filter.description().isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("description")), "%" + filter.description().toLowerCase() + "%"));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
