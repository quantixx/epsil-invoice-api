package io.epsil.invoice.service.mapper;

import io.epsil.invoice.domain.*;
import io.epsil.invoice.service.dto.QuotationDefinitionDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity QuotationDefinition and its DTO QuotationDefinitionDTO.
 */
@Mapper(componentModel = "spring", uses = {FamilyMapper.class, LanguageMapper.class, TenantMapper.class})
public interface QuotationDefinitionMapper extends EntityMapper<QuotationDefinitionDTO, QuotationDefinition> {

    @Mapping(source = "family.id", target = "familyId")
    @Mapping(source = "family.name", target = "familyName")
    @Mapping(source = "language.id", target = "languageId")
    @Mapping(source = "language.name", target = "languageName")
    @Mapping(source = "tenant.id", target = "tenantId")
    @Mapping(source = "tenant.name", target = "tenantName")
    QuotationDefinitionDTO toDto(QuotationDefinition quotationDefinition);

    @Mapping(source = "familyId", target = "family")
    @Mapping(source = "languageId", target = "language")
    @Mapping(source = "tenantId", target = "tenant")
    QuotationDefinition toEntity(QuotationDefinitionDTO quotationDefinitionDTO);

    default QuotationDefinition fromId(Long id) {
        if (id == null) {
            return null;
        }
        QuotationDefinition quotationDefinition = new QuotationDefinition();
        quotationDefinition.setId(id);
        return quotationDefinition;
    }
}
