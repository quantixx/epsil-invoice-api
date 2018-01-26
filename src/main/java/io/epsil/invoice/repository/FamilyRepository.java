package io.epsil.invoice.repository;

import io.epsil.invoice.domain.Family;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Family entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FamilyRepository extends JpaRepository<Family, Long>, JpaSpecificationExecutor<Family> {

}
