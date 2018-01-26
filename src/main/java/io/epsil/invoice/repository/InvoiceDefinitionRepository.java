package io.epsil.invoice.repository;

import io.epsil.invoice.domain.InvoiceDefinition;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the InvoiceDefinition entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InvoiceDefinitionRepository extends JpaRepository<InvoiceDefinition, Long>, JpaSpecificationExecutor<InvoiceDefinition> {

}
