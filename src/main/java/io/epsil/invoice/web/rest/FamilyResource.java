package io.epsil.invoice.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.epsil.invoice.service.FamilyService;
import io.epsil.invoice.web.rest.errors.BadRequestAlertException;
import io.epsil.invoice.web.rest.util.HeaderUtil;
import io.epsil.invoice.web.rest.util.PaginationUtil;
import io.epsil.invoice.service.dto.FamilyDTO;
import io.epsil.invoice.service.dto.FamilyCriteria;
import io.epsil.invoice.service.FamilyQueryService;
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
 * REST controller for managing Family.
 */
@RestController
@RequestMapping("/api")
public class FamilyResource {

    private final Logger log = LoggerFactory.getLogger(FamilyResource.class);

    private static final String ENTITY_NAME = "family";

    private final FamilyService familyService;

    private final FamilyQueryService familyQueryService;

    public FamilyResource(FamilyService familyService, FamilyQueryService familyQueryService) {
        this.familyService = familyService;
        this.familyQueryService = familyQueryService;
    }

    /**
     * POST  /families : Create a new family.
     *
     * @param familyDTO the familyDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new familyDTO, or with status 400 (Bad Request) if the family has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/families")
    @Timed
    public ResponseEntity<FamilyDTO> createFamily(@Valid @RequestBody FamilyDTO familyDTO) throws URISyntaxException {
        log.debug("REST request to save Family : {}", familyDTO);
        if (familyDTO.getId() != null) {
            throw new BadRequestAlertException("A new family cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FamilyDTO result = familyService.save(familyDTO);
        return ResponseEntity.created(new URI("/api/families/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /families : Updates an existing family.
     *
     * @param familyDTO the familyDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated familyDTO,
     * or with status 400 (Bad Request) if the familyDTO is not valid,
     * or with status 500 (Internal Server Error) if the familyDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/families")
    @Timed
    public ResponseEntity<FamilyDTO> updateFamily(@Valid @RequestBody FamilyDTO familyDTO) throws URISyntaxException {
        log.debug("REST request to update Family : {}", familyDTO);
        if (familyDTO.getId() == null) {
            return createFamily(familyDTO);
        }
        FamilyDTO result = familyService.save(familyDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, familyDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /families : get all the families.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of families in body
     */
    @GetMapping("/families")
    @Timed
    public ResponseEntity<List<FamilyDTO>> getAllFamilies(FamilyCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Families by criteria: {}", criteria);
        Page<FamilyDTO> page = familyQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/families");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /families/:id : get the "id" family.
     *
     * @param id the id of the familyDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the familyDTO, or with status 404 (Not Found)
     */
    @GetMapping("/families/{id}")
    @Timed
    public ResponseEntity<FamilyDTO> getFamily(@PathVariable Long id) {
        log.debug("REST request to get Family : {}", id);
        FamilyDTO familyDTO = familyService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(familyDTO));
    }

    /**
     * DELETE  /families/:id : delete the "id" family.
     *
     * @param id the id of the familyDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/families/{id}")
    @Timed
    public ResponseEntity<Void> deleteFamily(@PathVariable Long id) {
        log.debug("REST request to delete Family : {}", id);
        familyService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
