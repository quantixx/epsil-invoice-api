package io.epsil.invoice.service.mapper;

import io.epsil.invoice.domain.*;
import io.epsil.invoice.service.dto.InvoiceLineDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity InvoiceLine and its DTO InvoiceLineDTO.
 */
@Mapper(componentModel = "spring", uses = {InvoiceMapper.class})
public interface InvoiceLineMapper extends EntityMapper<InvoiceLineDTO, InvoiceLine> {

    @Mapping(source = "invoice.id", target = "invoiceId")
    @Mapping(source = "invoice.number", target = "invoiceNumber")
    InvoiceLineDTO toDto(InvoiceLine invoiceLine);

    @Mapping(source = "invoiceId", target = "invoice")
    InvoiceLine toEntity(InvoiceLineDTO invoiceLineDTO);

    default InvoiceLine fromId(Long id) {
        if (id == null) {
            return null;
        }
        InvoiceLine invoiceLine = new InvoiceLine();
        invoiceLine.setId(id);
        return invoiceLine;
    }
}
