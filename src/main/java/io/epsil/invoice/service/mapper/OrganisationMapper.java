package io.epsil.invoice.service.mapper;

import io.epsil.invoice.domain.*;
import io.epsil.invoice.service.dto.OrganisationDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Organisation and its DTO OrganisationDTO.
 */
@Mapper(componentModel = "spring", uses = {AddressMapper.class, ContactMapper.class, TenantMapper.class})
public interface OrganisationMapper extends EntityMapper<OrganisationDTO, Organisation> {

    @Mapping(source = "address.id", target = "addressId")
    @Mapping(source = "contact.id", target = "contactId")
    @Mapping(source = "contact.lastName", target = "contactLastName")
    @Mapping(source = "tenant.id", target = "tenantId")
    @Mapping(source = "tenant.name", target = "tenantName")
    OrganisationDTO toDto(Organisation organisation);

    @Mapping(source = "addressId", target = "address")
    @Mapping(source = "contactId", target = "contact")
    @Mapping(source = "tenantId", target = "tenant")
    Organisation toEntity(OrganisationDTO organisationDTO);

    default Organisation fromId(Long id) {
        if (id == null) {
            return null;
        }
        Organisation organisation = new Organisation();
        organisation.setId(id);
        return organisation;
    }
}
