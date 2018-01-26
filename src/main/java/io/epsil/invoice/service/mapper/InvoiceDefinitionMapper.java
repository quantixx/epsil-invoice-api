package io.epsil.invoice.service.mapper;

import io.epsil.invoice.domain.*;
import io.epsil.invoice.service.dto.InvoiceDefinitionDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity InvoiceDefinition and its DTO InvoiceDefinitionDTO.
 */
@Mapper(componentModel = "spring", uses = {FamilyMapper.class, LanguageMapper.class, TenantMapper.class})
public interface InvoiceDefinitionMapper extends EntityMapper<InvoiceDefinitionDTO, InvoiceDefinition> {

    @Mapping(source = "family.id", target = "familyId")
    @Mapping(source = "family.name", target = "familyName")
    @Mapping(source = "language.id", target = "languageId")
    @Mapping(source = "language.name", target = "languageName")
    @Mapping(source = "tenant.id", target = "tenantId")
    @Mapping(source = "tenant.name", target = "tenantName")
    InvoiceDefinitionDTO toDto(InvoiceDefinition invoiceDefinition);

    @Mapping(source = "familyId", target = "family")
    @Mapping(source = "languageId", target = "language")
    @Mapping(source = "tenantId", target = "tenant")
    InvoiceDefinition toEntity(InvoiceDefinitionDTO invoiceDefinitionDTO);

    default InvoiceDefinition fromId(Long id) {
        if (id == null) {
            return null;
        }
        InvoiceDefinition invoiceDefinition = new InvoiceDefinition();
        invoiceDefinition.setId(id);
        return invoiceDefinition;
    }
}
