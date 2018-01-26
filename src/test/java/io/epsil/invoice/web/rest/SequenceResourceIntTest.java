package io.epsil.invoice.web.rest;

import io.epsil.invoice.InvoiceapiApp;

import io.epsil.invoice.domain.Sequence;
import io.epsil.invoice.domain.Tenant;
import io.epsil.invoice.domain.Family;
import io.epsil.invoice.repository.SequenceRepository;
import io.epsil.invoice.service.SequenceService;
import io.epsil.invoice.service.dto.SequenceDTO;
import io.epsil.invoice.service.mapper.SequenceMapper;
import io.epsil.invoice.web.rest.errors.ExceptionTranslator;
import io.epsil.invoice.service.dto.SequenceCriteria;
import io.epsil.invoice.service.SequenceQueryService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static io.epsil.invoice.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import io.epsil.invoice.domain.enumeration.DocumentType;
/**
 * Test class for the SequenceResource REST controller.
 *
 * @see SequenceResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = InvoiceapiApp.class)
public class SequenceResourceIntTest {

    private static final DocumentType DEFAULT_DOC_TYPE = DocumentType.TENANT_LOGO;
    private static final DocumentType UPDATED_DOC_TYPE = DocumentType.INVOICE;

    private static final Integer DEFAULT_NEXT = 1;
    private static final Integer UPDATED_NEXT = 2;

    @Autowired
    private SequenceRepository sequenceRepository;

    @Autowired
    private SequenceMapper sequenceMapper;

    @Autowired
    private SequenceService sequenceService;

    @Autowired
    private SequenceQueryService sequenceQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSequenceMockMvc;

    private Sequence sequence;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SequenceResource sequenceResource = new SequenceResource(sequenceService, sequenceQueryService);
        this.restSequenceMockMvc = MockMvcBuilders.standaloneSetup(sequenceResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Sequence createEntity(EntityManager em) {
        Sequence sequence = new Sequence()
            .docType(DEFAULT_DOC_TYPE)
            .next(DEFAULT_NEXT);
        // Add required entity
        Tenant tenant = TenantResourceIntTest.createEntity(em);
        em.persist(tenant);
        em.flush();
        sequence.setTenant(tenant);
        // Add required entity
        Family family = FamilyResourceIntTest.createEntity(em);
        em.persist(family);
        em.flush();
        sequence.setFamily(family);
        return sequence;
    }

    @Before
    public void initTest() {
        sequence = createEntity(em);
    }

    @Test
    @Transactional
    public void createSequence() throws Exception {
        int databaseSizeBeforeCreate = sequenceRepository.findAll().size();

        // Create the Sequence
        SequenceDTO sequenceDTO = sequenceMapper.toDto(sequence);
        restSequenceMockMvc.perform(post("/api/sequences")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sequenceDTO)))
            .andExpect(status().isCreated());

        // Validate the Sequence in the database
        List<Sequence> sequenceList = sequenceRepository.findAll();
        assertThat(sequenceList).hasSize(databaseSizeBeforeCreate + 1);
        Sequence testSequence = sequenceList.get(sequenceList.size() - 1);
        assertThat(testSequence.getDocType()).isEqualTo(DEFAULT_DOC_TYPE);
        assertThat(testSequence.getNext()).isEqualTo(DEFAULT_NEXT);
    }

    @Test
    @Transactional
    public void createSequenceWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = sequenceRepository.findAll().size();

        // Create the Sequence with an existing ID
        sequence.setId(1L);
        SequenceDTO sequenceDTO = sequenceMapper.toDto(sequence);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSequenceMockMvc.perform(post("/api/sequences")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sequenceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Sequence in the database
        List<Sequence> sequenceList = sequenceRepository.findAll();
        assertThat(sequenceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkDocTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = sequenceRepository.findAll().size();
        // set the field null
        sequence.setDocType(null);

        // Create the Sequence, which fails.
        SequenceDTO sequenceDTO = sequenceMapper.toDto(sequence);

        restSequenceMockMvc.perform(post("/api/sequences")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sequenceDTO)))
            .andExpect(status().isBadRequest());

        List<Sequence> sequenceList = sequenceRepository.findAll();
        assertThat(sequenceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNextIsRequired() throws Exception {
        int databaseSizeBeforeTest = sequenceRepository.findAll().size();
        // set the field null
        sequence.setNext(null);

        // Create the Sequence, which fails.
        SequenceDTO sequenceDTO = sequenceMapper.toDto(sequence);

        restSequenceMockMvc.perform(post("/api/sequences")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sequenceDTO)))
            .andExpect(status().isBadRequest());

        List<Sequence> sequenceList = sequenceRepository.findAll();
        assertThat(sequenceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSequences() throws Exception {
        // Initialize the database
        sequenceRepository.saveAndFlush(sequence);

        // Get all the sequenceList
        restSequenceMockMvc.perform(get("/api/sequences?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sequence.getId().intValue())))
            .andExpect(jsonPath("$.[*].docType").value(hasItem(DEFAULT_DOC_TYPE.toString())))
            .andExpect(jsonPath("$.[*].next").value(hasItem(DEFAULT_NEXT)));
    }

    @Test
    @Transactional
    public void getSequence() throws Exception {
        // Initialize the database
        sequenceRepository.saveAndFlush(sequence);

        // Get the sequence
        restSequenceMockMvc.perform(get("/api/sequences/{id}", sequence.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(sequence.getId().intValue()))
            .andExpect(jsonPath("$.docType").value(DEFAULT_DOC_TYPE.toString()))
            .andExpect(jsonPath("$.next").value(DEFAULT_NEXT));
    }

    @Test
    @Transactional
    public void getAllSequencesByDocTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        sequenceRepository.saveAndFlush(sequence);

        // Get all the sequenceList where docType equals to DEFAULT_DOC_TYPE
        defaultSequenceShouldBeFound("docType.equals=" + DEFAULT_DOC_TYPE);

        // Get all the sequenceList where docType equals to UPDATED_DOC_TYPE
        defaultSequenceShouldNotBeFound("docType.equals=" + UPDATED_DOC_TYPE);
    }

    @Test
    @Transactional
    public void getAllSequencesByDocTypeIsInShouldWork() throws Exception {
        // Initialize the database
        sequenceRepository.saveAndFlush(sequence);

        // Get all the sequenceList where docType in DEFAULT_DOC_TYPE or UPDATED_DOC_TYPE
        defaultSequenceShouldBeFound("docType.in=" + DEFAULT_DOC_TYPE + "," + UPDATED_DOC_TYPE);

        // Get all the sequenceList where docType equals to UPDATED_DOC_TYPE
        defaultSequenceShouldNotBeFound("docType.in=" + UPDATED_DOC_TYPE);
    }

    @Test
    @Transactional
    public void getAllSequencesByDocTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        sequenceRepository.saveAndFlush(sequence);

        // Get all the sequenceList where docType is not null
        defaultSequenceShouldBeFound("docType.specified=true");

        // Get all the sequenceList where docType is null
        defaultSequenceShouldNotBeFound("docType.specified=false");
    }

    @Test
    @Transactional
    public void getAllSequencesByNextIsEqualToSomething() throws Exception {
        // Initialize the database
        sequenceRepository.saveAndFlush(sequence);

        // Get all the sequenceList where next equals to DEFAULT_NEXT
        defaultSequenceShouldBeFound("next.equals=" + DEFAULT_NEXT);

        // Get all the sequenceList where next equals to UPDATED_NEXT
        defaultSequenceShouldNotBeFound("next.equals=" + UPDATED_NEXT);
    }

    @Test
    @Transactional
    public void getAllSequencesByNextIsInShouldWork() throws Exception {
        // Initialize the database
        sequenceRepository.saveAndFlush(sequence);

        // Get all the sequenceList where next in DEFAULT_NEXT or UPDATED_NEXT
        defaultSequenceShouldBeFound("next.in=" + DEFAULT_NEXT + "," + UPDATED_NEXT);

        // Get all the sequenceList where next equals to UPDATED_NEXT
        defaultSequenceShouldNotBeFound("next.in=" + UPDATED_NEXT);
    }

    @Test
    @Transactional
    public void getAllSequencesByNextIsNullOrNotNull() throws Exception {
        // Initialize the database
        sequenceRepository.saveAndFlush(sequence);

        // Get all the sequenceList where next is not null
        defaultSequenceShouldBeFound("next.specified=true");

        // Get all the sequenceList where next is null
        defaultSequenceShouldNotBeFound("next.specified=false");
    }

    @Test
    @Transactional
    public void getAllSequencesByNextIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        sequenceRepository.saveAndFlush(sequence);

        // Get all the sequenceList where next greater than or equals to DEFAULT_NEXT
        defaultSequenceShouldBeFound("next.greaterOrEqualThan=" + DEFAULT_NEXT);

        // Get all the sequenceList where next greater than or equals to UPDATED_NEXT
        defaultSequenceShouldNotBeFound("next.greaterOrEqualThan=" + UPDATED_NEXT);
    }

    @Test
    @Transactional
    public void getAllSequencesByNextIsLessThanSomething() throws Exception {
        // Initialize the database
        sequenceRepository.saveAndFlush(sequence);

        // Get all the sequenceList where next less than or equals to DEFAULT_NEXT
        defaultSequenceShouldNotBeFound("next.lessThan=" + DEFAULT_NEXT);

        // Get all the sequenceList where next less than or equals to UPDATED_NEXT
        defaultSequenceShouldBeFound("next.lessThan=" + UPDATED_NEXT);
    }


    @Test
    @Transactional
    public void getAllSequencesByTenantIsEqualToSomething() throws Exception {
        // Initialize the database
        Tenant tenant = TenantResourceIntTest.createEntity(em);
        em.persist(tenant);
        em.flush();
        sequence.setTenant(tenant);
        sequenceRepository.saveAndFlush(sequence);
        Long tenantId = tenant.getId();

        // Get all the sequenceList where tenant equals to tenantId
        defaultSequenceShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the sequenceList where tenant equals to tenantId + 1
        defaultSequenceShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }


    @Test
    @Transactional
    public void getAllSequencesByFamilyIsEqualToSomething() throws Exception {
        // Initialize the database
        Family family = FamilyResourceIntTest.createEntity(em);
        em.persist(family);
        em.flush();
        sequence.setFamily(family);
        sequenceRepository.saveAndFlush(sequence);
        Long familyId = family.getId();

        // Get all the sequenceList where family equals to familyId
        defaultSequenceShouldBeFound("familyId.equals=" + familyId);

        // Get all the sequenceList where family equals to familyId + 1
        defaultSequenceShouldNotBeFound("familyId.equals=" + (familyId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultSequenceShouldBeFound(String filter) throws Exception {
        restSequenceMockMvc.perform(get("/api/sequences?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sequence.getId().intValue())))
            .andExpect(jsonPath("$.[*].docType").value(hasItem(DEFAULT_DOC_TYPE.toString())))
            .andExpect(jsonPath("$.[*].next").value(hasItem(DEFAULT_NEXT)));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultSequenceShouldNotBeFound(String filter) throws Exception {
        restSequenceMockMvc.perform(get("/api/sequences?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingSequence() throws Exception {
        // Get the sequence
        restSequenceMockMvc.perform(get("/api/sequences/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSequence() throws Exception {
        // Initialize the database
        sequenceRepository.saveAndFlush(sequence);
        int databaseSizeBeforeUpdate = sequenceRepository.findAll().size();

        // Update the sequence
        Sequence updatedSequence = sequenceRepository.findOne(sequence.getId());
        // Disconnect from session so that the updates on updatedSequence are not directly saved in db
        em.detach(updatedSequence);
        updatedSequence
            .docType(UPDATED_DOC_TYPE)
            .next(UPDATED_NEXT);
        SequenceDTO sequenceDTO = sequenceMapper.toDto(updatedSequence);

        restSequenceMockMvc.perform(put("/api/sequences")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sequenceDTO)))
            .andExpect(status().isOk());

        // Validate the Sequence in the database
        List<Sequence> sequenceList = sequenceRepository.findAll();
        assertThat(sequenceList).hasSize(databaseSizeBeforeUpdate);
        Sequence testSequence = sequenceList.get(sequenceList.size() - 1);
        assertThat(testSequence.getDocType()).isEqualTo(UPDATED_DOC_TYPE);
        assertThat(testSequence.getNext()).isEqualTo(UPDATED_NEXT);
    }

    @Test
    @Transactional
    public void updateNonExistingSequence() throws Exception {
        int databaseSizeBeforeUpdate = sequenceRepository.findAll().size();

        // Create the Sequence
        SequenceDTO sequenceDTO = sequenceMapper.toDto(sequence);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restSequenceMockMvc.perform(put("/api/sequences")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sequenceDTO)))
            .andExpect(status().isCreated());

        // Validate the Sequence in the database
        List<Sequence> sequenceList = sequenceRepository.findAll();
        assertThat(sequenceList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteSequence() throws Exception {
        // Initialize the database
        sequenceRepository.saveAndFlush(sequence);
        int databaseSizeBeforeDelete = sequenceRepository.findAll().size();

        // Get the sequence
        restSequenceMockMvc.perform(delete("/api/sequences/{id}", sequence.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Sequence> sequenceList = sequenceRepository.findAll();
        assertThat(sequenceList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Sequence.class);
        Sequence sequence1 = new Sequence();
        sequence1.setId(1L);
        Sequence sequence2 = new Sequence();
        sequence2.setId(sequence1.getId());
        assertThat(sequence1).isEqualTo(sequence2);
        sequence2.setId(2L);
        assertThat(sequence1).isNotEqualTo(sequence2);
        sequence1.setId(null);
        assertThat(sequence1).isNotEqualTo(sequence2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SequenceDTO.class);
        SequenceDTO sequenceDTO1 = new SequenceDTO();
        sequenceDTO1.setId(1L);
        SequenceDTO sequenceDTO2 = new SequenceDTO();
        assertThat(sequenceDTO1).isNotEqualTo(sequenceDTO2);
        sequenceDTO2.setId(sequenceDTO1.getId());
        assertThat(sequenceDTO1).isEqualTo(sequenceDTO2);
        sequenceDTO2.setId(2L);
        assertThat(sequenceDTO1).isNotEqualTo(sequenceDTO2);
        sequenceDTO1.setId(null);
        assertThat(sequenceDTO1).isNotEqualTo(sequenceDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(sequenceMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(sequenceMapper.fromId(null)).isNull();
    }
}
