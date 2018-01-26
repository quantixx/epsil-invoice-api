package io.epsil.invoice.repository;

import io.epsil.invoice.domain.InvoiceLine;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the InvoiceLine entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InvoiceLineRepository extends JpaRepository<InvoiceLine, Long>, JpaSpecificationExecutor<InvoiceLine> {

}
