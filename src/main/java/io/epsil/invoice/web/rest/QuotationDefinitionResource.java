package io.epsil.invoice.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.epsil.invoice.service.QuotationDefinitionService;
import io.epsil.invoice.web.rest.errors.BadRequestAlertException;
import io.epsil.invoice.web.rest.util.HeaderUtil;
import io.epsil.invoice.web.rest.util.PaginationUtil;
import io.epsil.invoice.service.dto.QuotationDefinitionDTO;
import io.epsil.invoice.service.dto.QuotationDefinitionCriteria;
import io.epsil.invoice.service.QuotationDefinitionQueryService;
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
 * REST controller for managing QuotationDefinition.
 */
@RestController
@RequestMapping("/api")
public class QuotationDefinitionResource {

    private final Logger log = LoggerFactory.getLogger(QuotationDefinitionResource.class);

    private static final String ENTITY_NAME = "quotationDefinition";

    private final QuotationDefinitionService quotationDefinitionService;

    private final QuotationDefinitionQueryService quotationDefinitionQueryService;

    public QuotationDefinitionResource(QuotationDefinitionService quotationDefinitionService, QuotationDefinitionQueryService quotationDefinitionQueryService) {
        this.quotationDefinitionService = quotationDefinitionService;
        this.quotationDefinitionQueryService = quotationDefinitionQueryService;
    }

    /**
     * POST  /quotation-definitions : Create a new quotationDefinition.
     *
     * @param quotationDefinitionDTO the quotationDefinitionDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new quotationDefinitionDTO, or with status 400 (Bad Request) if the quotationDefinition has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/quotation-definitions")
    @Timed
    public ResponseEntity<QuotationDefinitionDTO> createQuotationDefinition(@Valid @RequestBody QuotationDefinitionDTO quotationDefinitionDTO) throws URISyntaxException {
        log.debug("REST request to save QuotationDefinition : {}", quotationDefinitionDTO);
        if (quotationDefinitionDTO.getId() != null) {
            throw new BadRequestAlertException("A new quotationDefinition cannot already have an ID", ENTITY_NAME, "idexists");
        }
        QuotationDefinitionDTO result = quotationDefinitionService.save(quotationDefinitionDTO);
        return ResponseEntity.created(new URI("/api/quotation-definitions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /quotation-definitions : Updates an existing quotationDefinition.
     *
     * @param quotationDefinitionDTO the quotationDefinitionDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated quotationDefinitionDTO,
     * or with status 400 (Bad Request) if the quotationDefinitionDTO is not valid,
     * or with status 500 (Internal Server Error) if the quotationDefinitionDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/quotation-definitions")
    @Timed
    public ResponseEntity<QuotationDefinitionDTO> updateQuotationDefinition(@Valid @RequestBody QuotationDefinitionDTO quotationDefinitionDTO) throws URISyntaxException {
        log.debug("REST request to update QuotationDefinition : {}", quotationDefinitionDTO);
        if (quotationDefinitionDTO.getId() == null) {
            return createQuotationDefinition(quotationDefinitionDTO);
        }
        QuotationDefinitionDTO result = quotationDefinitionService.save(quotationDefinitionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, quotationDefinitionDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /quotation-definitions : get all the quotationDefinitions.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of quotationDefinitions in body
     */
    @GetMapping("/quotation-definitions")
    @Timed
    public ResponseEntity<List<QuotationDefinitionDTO>> getAllQuotationDefinitions(QuotationDefinitionCriteria criteria, Pageable pageable) {
        log.debug("REST request to get QuotationDefinitions by criteria: {}", criteria);
        Page<QuotationDefinitionDTO> page = quotationDefinitionQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/quotation-definitions");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /quotation-definitions/:id : get the "id" quotationDefinition.
     *
     * @param id the id of the quotationDefinitionDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the quotationDefinitionDTO, or with status 404 (Not Found)
     */
    @GetMapping("/quotation-definitions/{id}")
    @Timed
    public ResponseEntity<QuotationDefinitionDTO> getQuotationDefinition(@PathVariable Long id) {
        log.debug("REST request to get QuotationDefinition : {}", id);
        QuotationDefinitionDTO quotationDefinitionDTO = quotationDefinitionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(quotationDefinitionDTO));
    }

    /**
     * DELETE  /quotation-definitions/:id : delete the "id" quotationDefinition.
     *
     * @param id the id of the quotationDefinitionDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/quotation-definitions/{id}")
    @Timed
    public ResponseEntity<Void> deleteQuotationDefinition(@PathVariable Long id) {
        log.debug("REST request to delete QuotationDefinition : {}", id);
        quotationDefinitionService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
