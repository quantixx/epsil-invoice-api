package io.epsil.invoice.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.epsil.invoice.service.QuotationService;
import io.epsil.invoice.web.rest.errors.BadRequestAlertException;
import io.epsil.invoice.web.rest.util.HeaderUtil;
import io.epsil.invoice.web.rest.util.PaginationUtil;
import io.epsil.invoice.service.dto.QuotationDTO;
import io.epsil.invoice.service.dto.QuotationCriteria;
import io.epsil.invoice.service.QuotationQueryService;
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
import java.util.stream.StreamSupport;

/**
 * REST controller for managing Quotation.
 */
@RestController
@RequestMapping("/api")
public class QuotationResource {

    private final Logger log = LoggerFactory.getLogger(QuotationResource.class);

    private static final String ENTITY_NAME = "quotation";

    private final QuotationService quotationService;

    private final QuotationQueryService quotationQueryService;

    public QuotationResource(QuotationService quotationService, QuotationQueryService quotationQueryService) {
        this.quotationService = quotationService;
        this.quotationQueryService = quotationQueryService;
    }

    /**
     * POST  /quotations : Create a new quotation.
     *
     * @param quotationDTO the quotationDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new quotationDTO, or with status 400 (Bad Request) if the quotation has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/quotations")
    @Timed
    public ResponseEntity<QuotationDTO> createQuotation(@Valid @RequestBody QuotationDTO quotationDTO) throws URISyntaxException {
        log.debug("REST request to save Quotation : {}", quotationDTO);
        if (quotationDTO.getId() != null) {
            throw new BadRequestAlertException("A new quotation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        QuotationDTO result = quotationService.save(quotationDTO);
        return ResponseEntity.created(new URI("/api/quotations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /quotations : Updates an existing quotation.
     *
     * @param quotationDTO the quotationDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated quotationDTO,
     * or with status 400 (Bad Request) if the quotationDTO is not valid,
     * or with status 500 (Internal Server Error) if the quotationDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/quotations")
    @Timed
    public ResponseEntity<QuotationDTO> updateQuotation(@Valid @RequestBody QuotationDTO quotationDTO) throws URISyntaxException {
        log.debug("REST request to update Quotation : {}", quotationDTO);
        if (quotationDTO.getId() == null) {
            return createQuotation(quotationDTO);
        }
        QuotationDTO result = quotationService.save(quotationDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, quotationDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /quotations : get all the quotations.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of quotations in body
     */
    @GetMapping("/quotations")
    @Timed
    public ResponseEntity<List<QuotationDTO>> getAllQuotations(QuotationCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Quotations by criteria: {}", criteria);
        Page<QuotationDTO> page = quotationQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/quotations");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /quotations/:id : get the "id" quotation.
     *
     * @param id the id of the quotationDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the quotationDTO, or with status 404 (Not Found)
     */
    @GetMapping("/quotations/{id}")
    @Timed
    public ResponseEntity<QuotationDTO> getQuotation(@PathVariable Long id) {
        log.debug("REST request to get Quotation : {}", id);
        QuotationDTO quotationDTO = quotationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(quotationDTO));
    }

    /**
     * DELETE  /quotations/:id : delete the "id" quotation.
     *
     * @param id the id of the quotationDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/quotations/{id}")
    @Timed
    public ResponseEntity<Void> deleteQuotation(@PathVariable Long id) {
        log.debug("REST request to delete Quotation : {}", id);
        quotationService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
