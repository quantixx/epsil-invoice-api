package io.epsil.invoice.service.mapper;

import io.epsil.invoice.domain.*;
import io.epsil.invoice.service.dto.QuotationLineDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity QuotationLine and its DTO QuotationLineDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface QuotationLineMapper extends EntityMapper<QuotationLineDTO, QuotationLine> {



    default QuotationLine fromId(Long id) {
        if (id == null) {
            return null;
        }
        QuotationLine quotationLine = new QuotationLine();
        quotationLine.setId(id);
        return quotationLine;
    }
}
