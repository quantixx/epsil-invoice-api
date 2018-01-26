package io.epsil.invoice.service.mapper;

import io.epsil.invoice.domain.*;
import io.epsil.invoice.service.dto.BankDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Bank and its DTO BankDTO.
 */
@Mapper(componentModel = "spring", uses = {AddressMapper.class})
public interface BankMapper extends EntityMapper<BankDTO, Bank> {

    @Mapping(source = "address.id", target = "addressId")
    BankDTO toDto(Bank bank);

    @Mapping(source = "addressId", target = "address")
    Bank toEntity(BankDTO bankDTO);

    default Bank fromId(Long id) {
        if (id == null) {
            return null;
        }
        Bank bank = new Bank();
        bank.setId(id);
        return bank;
    }
}
