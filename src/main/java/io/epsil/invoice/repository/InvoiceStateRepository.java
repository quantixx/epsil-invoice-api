package io.epsil.invoice.repository;

import io.epsil.invoice.domain.InvoiceState;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the InvoiceState entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InvoiceStateRepository extends JpaRepository<InvoiceState, Long>, JpaSpecificationExecutor<InvoiceState> {

}
