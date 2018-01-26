package io.epsil.invoice.service.mapper;

import io.epsil.invoice.domain.*;
import io.epsil.invoice.service.dto.InvoiceStateDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity InvoiceState and its DTO InvoiceStateDTO.
 */
@Mapper(componentModel = "spring", uses = {InvoiceMapper.class})
public interface InvoiceStateMapper extends EntityMapper<InvoiceStateDTO, InvoiceState> {

    @Mapping(source = "invoice.id", target = "invoiceId")
    @Mapping(source = "invoice.number", target = "invoiceNumber")
    InvoiceStateDTO toDto(InvoiceState invoiceState);

    @Mapping(source = "invoiceId", target = "invoice")
    InvoiceState toEntity(InvoiceStateDTO invoiceStateDTO);

    default InvoiceState fromId(Long id) {
        if (id == null) {
            return null;
        }
        InvoiceState invoiceState = new InvoiceState();
        invoiceState.setId(id);
        return invoiceState;
    }
}
