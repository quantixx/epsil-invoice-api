package io.epsil.invoice.repository;

import io.epsil.invoice.domain.QuotationState;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the QuotationState entity.
 */
@SuppressWarnings("unused")
@Repository
public interface QuotationStateRepository extends JpaRepository<QuotationState, Long>, JpaSpecificationExecutor<QuotationState> {

}
