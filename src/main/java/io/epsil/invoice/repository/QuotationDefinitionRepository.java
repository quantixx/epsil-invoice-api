package io.epsil.invoice.repository;

import io.epsil.invoice.domain.QuotationDefinition;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the QuotationDefinition entity.
 */
@SuppressWarnings("unused")
@Repository
public interface QuotationDefinitionRepository extends JpaRepository<QuotationDefinition, Long>, JpaSpecificationExecutor<QuotationDefinition> {

}
