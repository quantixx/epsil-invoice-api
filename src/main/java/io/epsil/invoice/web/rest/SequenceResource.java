package io.epsil.invoice.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.epsil.invoice.service.SequenceService;
import io.epsil.invoice.web.rest.errors.BadRequestAlertException;
import io.epsil.invoice.web.rest.util.HeaderUtil;
import io.epsil.invoice.web.rest.util.PaginationUtil;
import io.epsil.invoice.service.dto.SequenceDTO;
import io.epsil.invoice.service.dto.SequenceCriteria;
import io.epsil.invoice.service.SequenceQueryService;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Sequence.
 */
@RestController
@RequestMapping("/api")
public class SequenceResource {

    private final Logger log = LoggerFactory.getLogger(SequenceResource.class);

    private static final String ENTITY_NAME = "sequence";

    private final SequenceService sequenceService;

    private final SequenceQueryService sequenceQueryService;

    public SequenceResource(SequenceService sequenceService, SequenceQueryService sequenceQueryService) {
        this.sequenceService = sequenceService;
        this.sequenceQueryService = sequenceQueryService;
    }

    /**
     * POST  /sequences : Create a new sequence.
     *
     * @param sequenceDTO the sequenceDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new sequenceDTO, or with status 400 (Bad Request) if the sequence has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/sequences")
    @Timed
    public ResponseEntity<SequenceDTO> createSequence(@Valid @RequestBody SequenceDTO sequenceDTO) throws URISyntaxException {
        log.debug("REST request to save Sequence : {}", sequenceDTO);
        if (sequenceDTO.getId() != null) {
            throw new BadRequestAlertException("A new sequence cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SequenceDTO result = sequenceService.save(sequenceDTO);
        return ResponseEntity.created(new URI("/api/sequences/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /sequences : Updates an existing sequence.
     *
     * @param sequenceDTO the sequenceDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated sequenceDTO,
     * or with status 400 (Bad Request) if the sequenceDTO is not valid,
     * or with status 500 (Internal Server Error) if the sequenceDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/sequences")
    @Timed
    public ResponseEntity<SequenceDTO> updateSequence(@Valid @RequestBody SequenceDTO sequenceDTO) throws URISyntaxException {
        log.debug("REST request to update Sequence : {}", sequenceDTO);
        if (sequenceDTO.getId() == null) {
            return createSequence(sequenceDTO);
        }
        SequenceDTO result = sequenceService.save(sequenceDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, sequenceDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /sequences : get all the sequences.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of sequences in body
     */
    @GetMapping("/sequences")
    @Timed
    public ResponseEntity<List<SequenceDTO>> getAllSequences(SequenceCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Sequences by criteria: {}", criteria);
        Page<SequenceDTO> page = sequenceQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/sequences");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /sequences/:id : get the "id" sequence.
     *
     * @param id the id of the sequenceDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the sequenceDTO, or with status 404 (Not Found)
     */
    @GetMapping("/sequences/{id}")
    @Timed
    public ResponseEntity<SequenceDTO> getSequence(@PathVariable Long id) {
        log.debug("REST request to get Sequence : {}", id);
        SequenceDTO sequenceDTO = sequenceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(sequenceDTO));
    }

    /**
     * DELETE  /sequences/:id : delete the "id" sequence.
     *
     * @param id the id of the sequenceDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/sequences/{id}")
    @Timed
    public ResponseEntity<Void> deleteSequence(@PathVariable Long id) {
        log.debug("REST request to delete Sequence : {}", id);
        sequenceService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
