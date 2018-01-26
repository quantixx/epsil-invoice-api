package io.epsil.invoice.service.mapper;

import io.epsil.invoice.domain.*;
import io.epsil.invoice.service.dto.TenantDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Tenant and its DTO TenantDTO.
 */
@Mapper(componentModel = "spring", uses = {AddressMapper.class, BankMapper.class, DocumentMapper.class, ContactMapper.class})
public interface TenantMapper extends EntityMapper<TenantDTO, Tenant> {

    @Mapping(source = "invoiceAddress.id", target = "invoiceAddressId")
    @Mapping(source = "bank.id", target = "bankId")
    @Mapping(source = "bank.bankName", target = "bankBankName")
    @Mapping(source = "logo.id", target = "logoId")
    @Mapping(source = "logo.name", target = "logoName")
    @Mapping(source = "contact.id", target = "contactId")
    @Mapping(source = "contact.lastName", target = "contactLastName")
    TenantDTO toDto(Tenant tenant);

    @Mapping(source = "invoiceAddressId", target = "invoiceAddress")
    @Mapping(source = "bankId", target = "bank")
    @Mapping(source = "logoId", target = "logo")
    @Mapping(source = "contactId", target = "contact")
    @Mapping(target = "events", ignore = true)
    @Mapping(target = "invoices", ignore = true)
    @Mapping(target = "invoiceDefinitions", ignore = true)
    @Mapping(target = "quotations", ignore = true)
    @Mapping(target = "quotationDefinitions", ignore = true)
    @Mapping(target = "families", ignore = true)
    Tenant toEntity(TenantDTO tenantDTO);

    default Tenant fromId(Long id) {
        if (id == null) {
            return null;
        }
        Tenant tenant = new Tenant();
        tenant.setId(id);
        return tenant;
    }
}
