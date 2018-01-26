package io.epsil.invoice.service.mapper;

import io.epsil.invoice.domain.*;
import io.epsil.invoice.service.dto.QuotationStateDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity QuotationState and its DTO QuotationStateDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface QuotationStateMapper extends EntityMapper<QuotationStateDTO, QuotationState> {



    default QuotationState fromId(Long id) {
        if (id == null) {
            return null;
        }
        QuotationState quotationState = new QuotationState();
        quotationState.setId(id);
        return quotationState;
    }
}
