package com.vbatecan.portfolio_manager.specs;

import com.vbatecan.portfolio_manager.models.entities.Education;
import com.vbatecan.portfolio_manager.models.entities.User;
import com.vbatecan.portfolio_manager.models.filter.EducationFilterInput;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public final class EducationSpecification {

    private EducationSpecification() {}

    public static Specification<Education> filter(EducationFilterInput filter, User user) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter.title() != null && !filter.title().isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("title")), "%" + filter.title().toLowerCase() + "%"));
            }

            if (filter.institution() != null && !filter.institution().isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("institution")), "%" + filter.institution().toLowerCase() + "%"));
            }

            // CRITICAL SECURITY RULE: Always filter by user ID
            predicates.add(cb.equal(root.get("user").get("id"), user.getId()));

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
