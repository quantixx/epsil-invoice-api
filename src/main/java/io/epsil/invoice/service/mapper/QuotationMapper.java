package io.epsil.invoice.service.mapper;

import io.epsil.invoice.domain.*;
import io.epsil.invoice.service.dto.QuotationDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Quotation and its DTO QuotationDTO.
 */
@Mapper(componentModel = "spring", uses = {FamilyMapper.class, TenantMapper.class})
public interface QuotationMapper extends EntityMapper<QuotationDTO, Quotation> {

    @Mapping(source = "family.id", target = "familyId")
    @Mapping(source = "family.name", target = "familyName")
    @Mapping(source = "tenant.id", target = "tenantId")
    @Mapping(source = "tenant.name", target = "tenantName")
    QuotationDTO toDto(Quotation quotation);

    @Mapping(source = "familyId", target = "family")
    @Mapping(target = "invoice", ignore = true)
    @Mapping(source = "tenantId", target = "tenant")
    Quotation toEntity(QuotationDTO quotationDTO);

    default Quotation fromId(Long id) {
        if (id == null) {
            return null;
        }
        Quotation quotation = new Quotation();
        quotation.setId(id);
        return quotation;
    }
}
