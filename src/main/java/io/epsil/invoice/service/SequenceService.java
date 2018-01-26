package io.epsil.invoice.service;

import io.epsil.invoice.domain.Sequence;
import io.epsil.invoice.repository.SequenceRepository;
import io.epsil.invoice.service.dto.SequenceDTO;
import io.epsil.invoice.service.mapper.SequenceMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing Sequence.
 */
@Service
@Transactional
public class SequenceService {

    private final Logger log = LoggerFactory.getLogger(SequenceService.class);

    private final SequenceRepository sequenceRepository;

    private final SequenceMapper sequenceMapper;

    public SequenceService(SequenceRepository sequenceRepository, SequenceMapper sequenceMapper) {
        this.sequenceRepository = sequenceRepository;
        this.sequenceMapper = sequenceMapper;
    }

    /**
     * Save a sequence.
     *
     * @param sequenceDTO the entity to save
     * @return the persisted entity
     */
    public SequenceDTO save(SequenceDTO sequenceDTO) {
        log.debug("Request to save Sequence : {}", sequenceDTO);
        Sequence sequence = sequenceMapper.toEntity(sequenceDTO);
        sequence = sequenceRepository.save(sequence);
        return sequenceMapper.toDto(sequence);
    }

    /**
     * Get all the sequences.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<SequenceDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Sequences");
        return sequenceRepository.findAll(pageable)
            .map(sequenceMapper::toDto);
    }

    /**
     * Get one sequence by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public SequenceDTO findOne(Long id) {
        log.debug("Request to get Sequence : {}", id);
        Sequence sequence = sequenceRepository.findOne(id);
        return sequenceMapper.toDto(sequence);
    }

    /**
     * Delete the sequence by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Sequence : {}", id);
        sequenceRepository.delete(id);
    }
}
