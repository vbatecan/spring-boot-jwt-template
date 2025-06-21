package com.vbatecan.portfolio_manager.repositories;

import com.vbatecan.portfolio_manager.models.entities.Certificate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CertificateRepository extends JpaRepository<Certificate, UUID>, JpaSpecificationExecutor<Certificate> {

    /**
     * Checks if a certificate with the given title already exists for a specific user.
     * This is used to prevent duplicate entries.
     *
     * @param title  the title of the certificate to check.
     * @param userId the ID of the user.
     * @return true if a certificate with the same title exists for the user, false otherwise.
     */
    boolean existsByTitleAndUser_Id(String title, UUID userId);

    /**
     * Finds a certificate by its ID, but only if it belongs to the specified user.
     * This is the primary method for securely fetching a single entity.
     *
     * @param id     the ID of the certificate.
     * @param userId the ID of the owner.
     * @return an Optional containing the certificate if found and owned by the user, otherwise an empty Optional.
     */
    Optional<Certificate> findByIdAndUser_Id(UUID id, UUID userId);

    /**
     * Retrieves a paginated list of all certificates owned by a specific user.
     *
     * @param userId   the ID of the user.
     * @param pageable the pagination information.
     * @return a Page of certificates for the user.
     */
    Page<Certificate> findByUser_Id(UUID userId, Pageable pageable);
}
