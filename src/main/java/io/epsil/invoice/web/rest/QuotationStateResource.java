package io.epsil.invoice.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.epsil.invoice.service.QuotationStateService;
import io.epsil.invoice.web.rest.errors.BadRequestAlertException;
import io.epsil.invoice.web.rest.util.HeaderUtil;
import io.epsil.invoice.web.rest.util.PaginationUtil;
import io.epsil.invoice.service.dto.QuotationStateDTO;
import io.epsil.invoice.service.dto.QuotationStateCriteria;
import io.epsil.invoice.service.QuotationStateQueryService;
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
 * REST controller for managing QuotationState.
 */
@RestController
@RequestMapping("/api")
public class QuotationStateResource {

    private final Logger log = LoggerFactory.getLogger(QuotationStateResource.class);

    private static final String ENTITY_NAME = "quotationState";

    private final QuotationStateService quotationStateService;

    private final QuotationStateQueryService quotationStateQueryService;

    public QuotationStateResource(QuotationStateService quotationStateService, QuotationStateQueryService quotationStateQueryService) {
        this.quotationStateService = quotationStateService;
        this.quotationStateQueryService = quotationStateQueryService;
    }

    /**
     * POST  /quotation-states : Create a new quotationState.
     *
     * @param quotationStateDTO the quotationStateDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new quotationStateDTO, or with status 400 (Bad Request) if the quotationState has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/quotation-states")
    @Timed
    public ResponseEntity<QuotationStateDTO> createQuotationState(@Valid @RequestBody QuotationStateDTO quotationStateDTO) throws URISyntaxException {
        log.debug("REST request to save QuotationState : {}", quotationStateDTO);
        if (quotationStateDTO.getId() != null) {
            throw new BadRequestAlertException("A new quotationState cannot already have an ID", ENTITY_NAME, "idexists");
        }
        QuotationStateDTO result = quotationStateService.save(quotationStateDTO);
        return ResponseEntity.created(new URI("/api/quotation-states/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /quotation-states : Updates an existing quotationState.
     *
     * @param quotationStateDTO the quotationStateDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated quotationStateDTO,
     * or with status 400 (Bad Request) if the quotationStateDTO is not valid,
     * or with status 500 (Internal Server Error) if the quotationStateDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/quotation-states")
    @Timed
    public ResponseEntity<QuotationStateDTO> updateQuotationState(@Valid @RequestBody QuotationStateDTO quotationStateDTO) throws URISyntaxException {
        log.debug("REST request to update QuotationState : {}", quotationStateDTO);
        if (quotationStateDTO.getId() == null) {
            return createQuotationState(quotationStateDTO);
        }
        QuotationStateDTO result = quotationStateService.save(quotationStateDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, quotationStateDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /quotation-states : get all the quotationStates.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of quotationStates in body
     */
    @GetMapping("/quotation-states")
    @Timed
    public ResponseEntity<List<QuotationStateDTO>> getAllQuotationStates(QuotationStateCriteria criteria, Pageable pageable) {
        log.debug("REST request to get QuotationStates by criteria: {}", criteria);
        Page<QuotationStateDTO> page = quotationStateQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/quotation-states");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /quotation-states/:id : get the "id" quotationState.
     *
     * @param id the id of the quotationStateDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the quotationStateDTO, or with status 404 (Not Found)
     */
    @GetMapping("/quotation-states/{id}")
    @Timed
    public ResponseEntity<QuotationStateDTO> getQuotationState(@PathVariable Long id) {
        log.debug("REST request to get QuotationState : {}", id);
        QuotationStateDTO quotationStateDTO = quotationStateService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(quotationStateDTO));
    }

    /**
     * DELETE  /quotation-states/:id : delete the "id" quotationState.
     *
     * @param id the id of the quotationStateDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/quotation-states/{id}")
    @Timed
    public ResponseEntity<Void> deleteQuotationState(@PathVariable Long id) {
        log.debug("REST request to delete QuotationState : {}", id);
        quotationStateService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
