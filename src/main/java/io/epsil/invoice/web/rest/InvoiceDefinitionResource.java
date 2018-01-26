package io.epsil.invoice.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.epsil.invoice.service.InvoiceDefinitionService;
import io.epsil.invoice.web.rest.errors.BadRequestAlertException;
import io.epsil.invoice.web.rest.util.HeaderUtil;
import io.epsil.invoice.web.rest.util.PaginationUtil;
import io.epsil.invoice.service.dto.InvoiceDefinitionDTO;
import io.epsil.invoice.service.dto.InvoiceDefinitionCriteria;
import io.epsil.invoice.service.InvoiceDefinitionQueryService;
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
 * REST controller for managing InvoiceDefinition.
 */
@RestController
@RequestMapping("/api")
public class InvoiceDefinitionResource {

    private final Logger log = LoggerFactory.getLogger(InvoiceDefinitionResource.class);

    private static final String ENTITY_NAME = "invoiceDefinition";

    private final InvoiceDefinitionService invoiceDefinitionService;

    private final InvoiceDefinitionQueryService invoiceDefinitionQueryService;

    public InvoiceDefinitionResource(InvoiceDefinitionService invoiceDefinitionService, InvoiceDefinitionQueryService invoiceDefinitionQueryService) {
        this.invoiceDefinitionService = invoiceDefinitionService;
        this.invoiceDefinitionQueryService = invoiceDefinitionQueryService;
    }

    /**
     * POST  /invoice-definitions : Create a new invoiceDefinition.
     *
     * @param invoiceDefinitionDTO the invoiceDefinitionDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new invoiceDefinitionDTO, or with status 400 (Bad Request) if the invoiceDefinition has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/invoice-definitions")
    @Timed
    public ResponseEntity<InvoiceDefinitionDTO> createInvoiceDefinition(@Valid @RequestBody InvoiceDefinitionDTO invoiceDefinitionDTO) throws URISyntaxException {
        log.debug("REST request to save InvoiceDefinition : {}", invoiceDefinitionDTO);
        if (invoiceDefinitionDTO.getId() != null) {
            throw new BadRequestAlertException("A new invoiceDefinition cannot already have an ID", ENTITY_NAME, "idexists");
        }
        InvoiceDefinitionDTO result = invoiceDefinitionService.save(invoiceDefinitionDTO);
        return ResponseEntity.created(new URI("/api/invoice-definitions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /invoice-definitions : Updates an existing invoiceDefinition.
     *
     * @param invoiceDefinitionDTO the invoiceDefinitionDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated invoiceDefinitionDTO,
     * or with status 400 (Bad Request) if the invoiceDefinitionDTO is not valid,
     * or with status 500 (Internal Server Error) if the invoiceDefinitionDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/invoice-definitions")
    @Timed
    public ResponseEntity<InvoiceDefinitionDTO> updateInvoiceDefinition(@Valid @RequestBody InvoiceDefinitionDTO invoiceDefinitionDTO) throws URISyntaxException {
        log.debug("REST request to update InvoiceDefinition : {}", invoiceDefinitionDTO);
        if (invoiceDefinitionDTO.getId() == null) {
            return createInvoiceDefinition(invoiceDefinitionDTO);
        }
        InvoiceDefinitionDTO result = invoiceDefinitionService.save(invoiceDefinitionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, invoiceDefinitionDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /invoice-definitions : get all the invoiceDefinitions.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of invoiceDefinitions in body
     */
    @GetMapping("/invoice-definitions")
    @Timed
    public ResponseEntity<List<InvoiceDefinitionDTO>> getAllInvoiceDefinitions(InvoiceDefinitionCriteria criteria, Pageable pageable) {
        log.debug("REST request to get InvoiceDefinitions by criteria: {}", criteria);
        Page<InvoiceDefinitionDTO> page = invoiceDefinitionQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/invoice-definitions");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /invoice-definitions/:id : get the "id" invoiceDefinition.
     *
     * @param id the id of the invoiceDefinitionDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the invoiceDefinitionDTO, or with status 404 (Not Found)
     */
    @GetMapping("/invoice-definitions/{id}")
    @Timed
    public ResponseEntity<InvoiceDefinitionDTO> getInvoiceDefinition(@PathVariable Long id) {
        log.debug("REST request to get InvoiceDefinition : {}", id);
        InvoiceDefinitionDTO invoiceDefinitionDTO = invoiceDefinitionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(invoiceDefinitionDTO));
    }

    /**
     * DELETE  /invoice-definitions/:id : delete the "id" invoiceDefinition.
     *
     * @param id the id of the invoiceDefinitionDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/invoice-definitions/{id}")
    @Timed
    public ResponseEntity<Void> deleteInvoiceDefinition(@PathVariable Long id) {
        log.debug("REST request to delete InvoiceDefinition : {}", id);
        invoiceDefinitionService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
