package io.epsil.invoice.service.mapper;

import io.epsil.invoice.domain.*;
import io.epsil.invoice.service.dto.InvoiceDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Invoice and its DTO InvoiceDTO.
 */
@Mapper(componentModel = "spring", uses = {OrganisationMapper.class, QuotationMapper.class, DocumentMapper.class, LanguageMapper.class, CurrencyMapper.class, FamilyMapper.class, TenantMapper.class})
public interface InvoiceMapper extends EntityMapper<InvoiceDTO, Invoice> {

    @Mapping(source = "linked.id", target = "linkedId")
    @Mapping(source = "linked.number", target = "linkedNumber")
    @Mapping(source = "organisation.id", target = "organisationId")
    @Mapping(source = "organisation.name", target = "organisationName")
    @Mapping(source = "quotation.id", target = "quotationId")
    @Mapping(source = "quotation.number", target = "quotationNumber")
    @Mapping(source = "document.id", target = "documentId")
    @Mapping(source = "document.name", target = "documentName")
    @Mapping(source = "language.id", target = "languageId")
    @Mapping(source = "language.name", target = "languageName")
    @Mapping(source = "currency.id", target = "currencyId")
    @Mapping(source = "currency.currency", target = "currencyCurrency")
    @Mapping(source = "family.id", target = "familyId")
    @Mapping(source = "family.name", target = "familyName")
    @Mapping(source = "tenant.id", target = "tenantId")
    @Mapping(source = "tenant.name", target = "tenantName")
    InvoiceDTO toDto(Invoice invoice);

    @Mapping(source = "linkedId", target = "linked")
    @Mapping(source = "organisationId", target = "organisation")
    @Mapping(source = "quotationId", target = "quotation")
    @Mapping(source = "documentId", target = "document")
    @Mapping(target = "states", ignore = true)
    @Mapping(target = "lines", ignore = true)
    @Mapping(source = "languageId", target = "language")
    @Mapping(source = "currencyId", target = "currency")
    @Mapping(source = "familyId", target = "family")
    @Mapping(source = "tenantId", target = "tenant")
    Invoice toEntity(InvoiceDTO invoiceDTO);

    default Invoice fromId(Long id) {
        if (id == null) {
            return null;
        }
        Invoice invoice = new Invoice();
        invoice.setId(id);
        return invoice;
    }
}
