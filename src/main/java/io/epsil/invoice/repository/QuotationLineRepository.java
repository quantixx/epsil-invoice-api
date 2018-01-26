package io.epsil.invoice.repository;

import io.epsil.invoice.domain.QuotationLine;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the QuotationLine entity.
 */
@SuppressWarnings("unused")
@Repository
public interface QuotationLineRepository extends JpaRepository<QuotationLine, Long>, JpaSpecificationExecutor<QuotationLine> {

}
