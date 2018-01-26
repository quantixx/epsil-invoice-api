package io.epsil.invoice.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.epsil.invoice.service.InvoiceStateService;
import io.epsil.invoice.web.rest.errors.BadRequestAlertException;
import io.epsil.invoice.web.rest.util.HeaderUtil;
import io.epsil.invoice.web.rest.util.PaginationUtil;
import io.epsil.invoice.service.dto.InvoiceStateDTO;
import io.epsil.invoice.service.dto.InvoiceStateCriteria;
import io.epsil.invoice.service.InvoiceStateQueryService;
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
 * REST controller for managing InvoiceState.
 */
@RestController
@RequestMapping("/api")
public class InvoiceStateResource {

    private final Logger log = LoggerFactory.getLogger(InvoiceStateResource.class);

    private static final String ENTITY_NAME = "invoiceState";

    private final InvoiceStateService invoiceStateService;

    private final InvoiceStateQueryService invoiceStateQueryService;

    public InvoiceStateResource(InvoiceStateService invoiceStateService, InvoiceStateQueryService invoiceStateQueryService) {
        this.invoiceStateService = invoiceStateService;
        this.invoiceStateQueryService = invoiceStateQueryService;
    }

    /**
     * POST  /invoice-states : Create a new invoiceState.
     *
     * @param invoiceStateDTO the invoiceStateDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new invoiceStateDTO, or with status 400 (Bad Request) if the invoiceState has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/invoice-states")
    @Timed
    public ResponseEntity<InvoiceStateDTO> createInvoiceState(@Valid @RequestBody InvoiceStateDTO invoiceStateDTO) throws URISyntaxException {
        log.debug("REST request to save InvoiceState : {}", invoiceStateDTO);
        if (invoiceStateDTO.getId() != null) {
            throw new BadRequestAlertException("A new invoiceState cannot already have an ID", ENTITY_NAME, "idexists");
        }
        InvoiceStateDTO result = invoiceStateService.save(invoiceStateDTO);
        return ResponseEntity.created(new URI("/api/invoice-states/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /invoice-states : Updates an existing invoiceState.
     *
     * @param invoiceStateDTO the invoiceStateDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated invoiceStateDTO,
     * or with status 400 (Bad Request) if the invoiceStateDTO is not valid,
     * or with status 500 (Internal Server Error) if the invoiceStateDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/invoice-states")
    @Timed
    public ResponseEntity<InvoiceStateDTO> updateInvoiceState(@Valid @RequestBody InvoiceStateDTO invoiceStateDTO) throws URISyntaxException {
        log.debug("REST request to update InvoiceState : {}", invoiceStateDTO);
        if (invoiceStateDTO.getId() == null) {
            return createInvoiceState(invoiceStateDTO);
        }
        InvoiceStateDTO result = invoiceStateService.save(invoiceStateDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, invoiceStateDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /invoice-states : get all the invoiceStates.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of invoiceStates in body
     */
    @GetMapping("/invoice-states")
    @Timed
    public ResponseEntity<List<InvoiceStateDTO>> getAllInvoiceStates(InvoiceStateCriteria criteria, Pageable pageable) {
        log.debug("REST request to get InvoiceStates by criteria: {}", criteria);
        Page<InvoiceStateDTO> page = invoiceStateQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/invoice-states");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /invoice-states/:id : get the "id" invoiceState.
     *
     * @param id the id of the invoiceStateDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the invoiceStateDTO, or with status 404 (Not Found)
     */
    @GetMapping("/invoice-states/{id}")
    @Timed
    public ResponseEntity<InvoiceStateDTO> getInvoiceState(@PathVariable Long id) {
        log.debug("REST request to get InvoiceState : {}", id);
        InvoiceStateDTO invoiceStateDTO = invoiceStateService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(invoiceStateDTO));
    }

    /**
     * DELETE  /invoice-states/:id : delete the "id" invoiceState.
     *
     * @param id the id of the invoiceStateDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/invoice-states/{id}")
    @Timed
    public ResponseEntity<Void> deleteInvoiceState(@PathVariable Long id) {
        log.debug("REST request to delete InvoiceState : {}", id);
        invoiceStateService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
