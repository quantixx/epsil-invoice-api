package io.epsil.invoice.service.mapper;

import io.epsil.invoice.domain.*;
import io.epsil.invoice.service.dto.FamilyDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Family and its DTO FamilyDTO.
 */
@Mapper(componentModel = "spring", uses = {TenantMapper.class})
public interface FamilyMapper extends EntityMapper<FamilyDTO, Family> {

    @Mapping(source = "tenant.id", target = "tenantId")
    @Mapping(source = "tenant.name", target = "tenantName")
    FamilyDTO toDto(Family family);

    @Mapping(source = "tenantId", target = "tenant")
    Family toEntity(FamilyDTO familyDTO);

    default Family fromId(Long id) {
        if (id == null) {
            return null;
        }
        Family family = new Family();
        family.setId(id);
        return family;
    }
}
