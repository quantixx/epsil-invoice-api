package io.epsil.invoice.service.mapper;

import io.epsil.invoice.domain.*;
import io.epsil.invoice.service.dto.SequenceDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Sequence and its DTO SequenceDTO.
 */
@Mapper(componentModel = "spring", uses = {TenantMapper.class, FamilyMapper.class})
public interface SequenceMapper extends EntityMapper<SequenceDTO, Sequence> {

    @Mapping(source = "tenant.id", target = "tenantId")
    @Mapping(source = "tenant.name", target = "tenantName")
    @Mapping(source = "family.id", target = "familyId")
    @Mapping(source = "family.name", target = "familyName")
    SequenceDTO toDto(Sequence sequence);

    @Mapping(source = "tenantId", target = "tenant")
    @Mapping(source = "familyId", target = "family")
    Sequence toEntity(SequenceDTO sequenceDTO);

    default Sequence fromId(Long id) {
        if (id == null) {
            return null;
        }
        Sequence sequence = new Sequence();
        sequence.setId(id);
        return sequence;
    }
}
