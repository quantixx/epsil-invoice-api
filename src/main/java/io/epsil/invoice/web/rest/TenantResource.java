package io.epsil.invoice.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.epsil.invoice.service.TenantService;
import io.epsil.invoice.web.rest.errors.BadRequestAlertException;
import io.epsil.invoice.web.rest.util.HeaderUtil;
import io.epsil.invoice.web.rest.util.PaginationUtil;
import io.epsil.invoice.service.dto.TenantDTO;
import io.epsil.invoice.service.dto.TenantCriteria;
import io.epsil.invoice.service.TenantQueryService;
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
 * REST controller for managing Tenant.
 */
@RestController
@RequestMapping("/api")
public class TenantResource {

    private final Logger log = LoggerFactory.getLogger(TenantResource.class);

    private static final String ENTITY_NAME = "tenant";

    private final TenantService tenantService;

    private final TenantQueryService tenantQueryService;

    public TenantResource(TenantService tenantService, TenantQueryService tenantQueryService) {
        this.tenantService = tenantService;
        this.tenantQueryService = tenantQueryService;
    }

    /**
     * POST  /tenants : Create a new tenant.
     *
     * @param tenantDTO the tenantDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new tenantDTO, or with status 400 (Bad Request) if the tenant has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/tenants")
    @Timed
    public ResponseEntity<TenantDTO> createTenant(@Valid @RequestBody TenantDTO tenantDTO) throws URISyntaxException {
        log.debug("REST request to save Tenant : {}", tenantDTO);
        if (tenantDTO.getId() != null) {
            throw new BadRequestAlertException("A new tenant cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TenantDTO result = tenantService.save(tenantDTO);
        return ResponseEntity.created(new URI("/api/tenants/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /tenants : Updates an existing tenant.
     *
     * @param tenantDTO the tenantDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated tenantDTO,
     * or with status 400 (Bad Request) if the tenantDTO is not valid,
     * or with status 500 (Internal Server Error) if the tenantDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/tenants")
    @Timed
    public ResponseEntity<TenantDTO> updateTenant(@Valid @RequestBody TenantDTO tenantDTO) throws URISyntaxException {
        log.debug("REST request to update Tenant : {}", tenantDTO);
        if (tenantDTO.getId() == null) {
            return createTenant(tenantDTO);
        }
        TenantDTO result = tenantService.save(tenantDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, tenantDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /tenants : get all the tenants.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of tenants in body
     */
    @GetMapping("/tenants")
    @Timed
    public ResponseEntity<List<TenantDTO>> getAllTenants(TenantCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Tenants by criteria: {}", criteria);
        Page<TenantDTO> page = tenantQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/tenants");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /tenants/:id : get the "id" tenant.
     *
     * @param id the id of the tenantDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the tenantDTO, or with status 404 (Not Found)
     */
    @GetMapping("/tenants/{id}")
    @Timed
    public ResponseEntity<TenantDTO> getTenant(@PathVariable Long id) {
        log.debug("REST request to get Tenant : {}", id);
        TenantDTO tenantDTO = tenantService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(tenantDTO));
    }

    /**
     * DELETE  /tenants/:id : delete the "id" tenant.
     *
     * @param id the id of the tenantDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/tenants/{id}")
    @Timed
    public ResponseEntity<Void> deleteTenant(@PathVariable Long id) {
        log.debug("REST request to delete Tenant : {}", id);
        tenantService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
