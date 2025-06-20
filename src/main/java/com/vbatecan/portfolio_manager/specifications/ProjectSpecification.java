package com.vbatecan.portfolio_manager.specifications;

import com.vbatecan.portfolio_manager.models.entities.Project;
import com.vbatecan.portfolio_manager.models.entities.User;
import com.vbatecan.portfolio_manager.models.filters.ProjectFilterInput;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ProjectSpecification {

	public static Specification<Project> filter(ProjectFilterInput filter, User user) {
		return ((root, query, cb) -> {
			List<Predicate> predicates = new ArrayList<>();

			if ( filter.title() != null && !filter.title().isBlank() ) {
				predicates.add(cb.like(
					cb.lower(root.get("title")),
					"%" + filter.title().toLowerCase() + "%"
				));
			}

			if ( filter.description() != null && !filter.description().isBlank() ) {
				predicates.add(cb.like(
					cb.lower(root.get("description")),
					"%" + filter.description().toLowerCase() + "%"
				));
			}

			// ! Check if the project is owned by the current user to prevent other users access.
			predicates.add(cb.equal(root.get("user").get("id"), user.getId()));
			return cb.and(predicates.toArray(new Predicate[0]));
		});
	}
}
