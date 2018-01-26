package io.epsil.invoice.web.rest;

import io.epsil.invoice.InvoiceapiApp;

import io.epsil.invoice.domain.InvoiceState;
import io.epsil.invoice.domain.Invoice;
import io.epsil.invoice.repository.InvoiceStateRepository;
import io.epsil.invoice.service.InvoiceStateService;
import io.epsil.invoice.service.dto.InvoiceStateDTO;
import io.epsil.invoice.service.mapper.InvoiceStateMapper;
import io.epsil.invoice.web.rest.errors.ExceptionTranslator;
import io.epsil.invoice.service.dto.InvoiceStateCriteria;
import io.epsil.invoice.service.InvoiceStateQueryService;

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
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static io.epsil.invoice.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import io.epsil.invoice.domain.enumeration.InvoiceStatus;
/**
 * Test class for the InvoiceStateResource REST controller.
 *
 * @see InvoiceStateResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = InvoiceapiApp.class)
public class InvoiceStateResourceIntTest {

    private static final InvoiceStatus DEFAULT_STATUS = InvoiceStatus.CREATED;
    private static final InvoiceStatus UPDATED_STATUS = InvoiceStatus.COMMITTED;

    private static final Instant DEFAULT_STATUS_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_STATUS_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private InvoiceStateRepository invoiceStateRepository;

    @Autowired
    private InvoiceStateMapper invoiceStateMapper;

    @Autowired
    private InvoiceStateService invoiceStateService;

    @Autowired
    private InvoiceStateQueryService invoiceStateQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restInvoiceStateMockMvc;

    private InvoiceState invoiceState;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final InvoiceStateResource invoiceStateResource = new InvoiceStateResource(invoiceStateService, invoiceStateQueryService);
        this.restInvoiceStateMockMvc = MockMvcBuilders.standaloneSetup(invoiceStateResource)
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
    public static InvoiceState createEntity(EntityManager em) {
        InvoiceState invoiceState = new InvoiceState()
            .status(DEFAULT_STATUS)
            .statusDate(DEFAULT_STATUS_DATE);
        // Add required entity
        Invoice invoice = InvoiceResourceIntTest.createEntity(em);
        em.persist(invoice);
        em.flush();
        invoiceState.setInvoice(invoice);
        return invoiceState;
    }

    @Before
    public void initTest() {
        invoiceState = createEntity(em);
    }

    @Test
    @Transactional
    public void createInvoiceState() throws Exception {
        int databaseSizeBeforeCreate = invoiceStateRepository.findAll().size();

        // Create the InvoiceState
        InvoiceStateDTO invoiceStateDTO = invoiceStateMapper.toDto(invoiceState);
        restInvoiceStateMockMvc.perform(post("/api/invoice-states")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoiceStateDTO)))
            .andExpect(status().isCreated());

        // Validate the InvoiceState in the database
        List<InvoiceState> invoiceStateList = invoiceStateRepository.findAll();
        assertThat(invoiceStateList).hasSize(databaseSizeBeforeCreate + 1);
        InvoiceState testInvoiceState = invoiceStateList.get(invoiceStateList.size() - 1);
        assertThat(testInvoiceState.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testInvoiceState.getStatusDate()).isEqualTo(DEFAULT_STATUS_DATE);
    }

    @Test
    @Transactional
    public void createInvoiceStateWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = invoiceStateRepository.findAll().size();

        // Create the InvoiceState with an existing ID
        invoiceState.setId(1L);
        InvoiceStateDTO invoiceStateDTO = invoiceStateMapper.toDto(invoiceState);

        // An entity with an existing ID cannot be created, so this API call must fail
        restInvoiceStateMockMvc.perform(post("/api/invoice-states")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoiceStateDTO)))
            .andExpect(status().isBadRequest());

        // Validate the InvoiceState in the database
        List<InvoiceState> invoiceStateList = invoiceStateRepository.findAll();
        assertThat(invoiceStateList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = invoiceStateRepository.findAll().size();
        // set the field null
        invoiceState.setStatus(null);

        // Create the InvoiceState, which fails.
        InvoiceStateDTO invoiceStateDTO = invoiceStateMapper.toDto(invoiceState);

        restInvoiceStateMockMvc.perform(post("/api/invoice-states")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoiceStateDTO)))
            .andExpect(status().isBadRequest());

        List<InvoiceState> invoiceStateList = invoiceStateRepository.findAll();
        assertThat(invoiceStateList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatusDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = invoiceStateRepository.findAll().size();
        // set the field null
        invoiceState.setStatusDate(null);

        // Create the InvoiceState, which fails.
        InvoiceStateDTO invoiceStateDTO = invoiceStateMapper.toDto(invoiceState);

        restInvoiceStateMockMvc.perform(post("/api/invoice-states")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoiceStateDTO)))
            .andExpect(status().isBadRequest());

        List<InvoiceState> invoiceStateList = invoiceStateRepository.findAll();
        assertThat(invoiceStateList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllInvoiceStates() throws Exception {
        // Initialize the database
        invoiceStateRepository.saveAndFlush(invoiceState);

        // Get all the invoiceStateList
        restInvoiceStateMockMvc.perform(get("/api/invoice-states?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(invoiceState.getId().intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].statusDate").value(hasItem(DEFAULT_STATUS_DATE.toString())));
    }

    @Test
    @Transactional
    public void getInvoiceState() throws Exception {
        // Initialize the database
        invoiceStateRepository.saveAndFlush(invoiceState);

        // Get the invoiceState
        restInvoiceStateMockMvc.perform(get("/api/invoice-states/{id}", invoiceState.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(invoiceState.getId().intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.statusDate").value(DEFAULT_STATUS_DATE.toString()));
    }

    @Test
    @Transactional
    public void getAllInvoiceStatesByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        invoiceStateRepository.saveAndFlush(invoiceState);

        // Get all the invoiceStateList where status equals to DEFAULT_STATUS
        defaultInvoiceStateShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the invoiceStateList where status equals to UPDATED_STATUS
        defaultInvoiceStateShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllInvoiceStatesByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        invoiceStateRepository.saveAndFlush(invoiceState);

        // Get all the invoiceStateList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultInvoiceStateShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the invoiceStateList where status equals to UPDATED_STATUS
        defaultInvoiceStateShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllInvoiceStatesByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        invoiceStateRepository.saveAndFlush(invoiceState);

        // Get all the invoiceStateList where status is not null
        defaultInvoiceStateShouldBeFound("status.specified=true");

        // Get all the invoiceStateList where status is null
        defaultInvoiceStateShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    public void getAllInvoiceStatesByStatusDateIsEqualToSomething() throws Exception {
        // Initialize the database
        invoiceStateRepository.saveAndFlush(invoiceState);

        // Get all the invoiceStateList where statusDate equals to DEFAULT_STATUS_DATE
        defaultInvoiceStateShouldBeFound("statusDate.equals=" + DEFAULT_STATUS_DATE);

        // Get all the invoiceStateList where statusDate equals to UPDATED_STATUS_DATE
        defaultInvoiceStateShouldNotBeFound("statusDate.equals=" + UPDATED_STATUS_DATE);
    }

    @Test
    @Transactional
    public void getAllInvoiceStatesByStatusDateIsInShouldWork() throws Exception {
        // Initialize the database
        invoiceStateRepository.saveAndFlush(invoiceState);

        // Get all the invoiceStateList where statusDate in DEFAULT_STATUS_DATE or UPDATED_STATUS_DATE
        defaultInvoiceStateShouldBeFound("statusDate.in=" + DEFAULT_STATUS_DATE + "," + UPDATED_STATUS_DATE);

        // Get all the invoiceStateList where statusDate equals to UPDATED_STATUS_DATE
        defaultInvoiceStateShouldNotBeFound("statusDate.in=" + UPDATED_STATUS_DATE);
    }

    @Test
    @Transactional
    public void getAllInvoiceStatesByStatusDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        invoiceStateRepository.saveAndFlush(invoiceState);

        // Get all the invoiceStateList where statusDate is not null
        defaultInvoiceStateShouldBeFound("statusDate.specified=true");

        // Get all the invoiceStateList where statusDate is null
        defaultInvoiceStateShouldNotBeFound("statusDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllInvoiceStatesByInvoiceIsEqualToSomething() throws Exception {
        // Initialize the database
        Invoice invoice = InvoiceResourceIntTest.createEntity(em);
        em.persist(invoice);
        em.flush();
        invoiceState.setInvoice(invoice);
        invoiceStateRepository.saveAndFlush(invoiceState);
        Long invoiceId = invoice.getId();

        // Get all the invoiceStateList where invoice equals to invoiceId
        defaultInvoiceStateShouldBeFound("invoiceId.equals=" + invoiceId);

        // Get all the invoiceStateList where invoice equals to invoiceId + 1
        defaultInvoiceStateShouldNotBeFound("invoiceId.equals=" + (invoiceId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultInvoiceStateShouldBeFound(String filter) throws Exception {
        restInvoiceStateMockMvc.perform(get("/api/invoice-states?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(invoiceState.getId().intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].statusDate").value(hasItem(DEFAULT_STATUS_DATE.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultInvoiceStateShouldNotBeFound(String filter) throws Exception {
        restInvoiceStateMockMvc.perform(get("/api/invoice-states?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingInvoiceState() throws Exception {
        // Get the invoiceState
        restInvoiceStateMockMvc.perform(get("/api/invoice-states/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateInvoiceState() throws Exception {
        // Initialize the database
        invoiceStateRepository.saveAndFlush(invoiceState);
        int databaseSizeBeforeUpdate = invoiceStateRepository.findAll().size();

        // Update the invoiceState
        InvoiceState updatedInvoiceState = invoiceStateRepository.findOne(invoiceState.getId());
        // Disconnect from session so that the updates on updatedInvoiceState are not directly saved in db
        em.detach(updatedInvoiceState);
        updatedInvoiceState
            .status(UPDATED_STATUS)
            .statusDate(UPDATED_STATUS_DATE);
        InvoiceStateDTO invoiceStateDTO = invoiceStateMapper.toDto(updatedInvoiceState);

        restInvoiceStateMockMvc.perform(put("/api/invoice-states")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoiceStateDTO)))
            .andExpect(status().isOk());

        // Validate the InvoiceState in the database
        List<InvoiceState> invoiceStateList = invoiceStateRepository.findAll();
        assertThat(invoiceStateList).hasSize(databaseSizeBeforeUpdate);
        InvoiceState testInvoiceState = invoiceStateList.get(invoiceStateList.size() - 1);
        assertThat(testInvoiceState.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testInvoiceState.getStatusDate()).isEqualTo(UPDATED_STATUS_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingInvoiceState() throws Exception {
        int databaseSizeBeforeUpdate = invoiceStateRepository.findAll().size();

        // Create the InvoiceState
        InvoiceStateDTO invoiceStateDTO = invoiceStateMapper.toDto(invoiceState);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restInvoiceStateMockMvc.perform(put("/api/invoice-states")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invoiceStateDTO)))
            .andExpect(status().isCreated());

        // Validate the InvoiceState in the database
        List<InvoiceState> invoiceStateList = invoiceStateRepository.findAll();
        assertThat(invoiceStateList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteInvoiceState() throws Exception {
        // Initialize the database
        invoiceStateRepository.saveAndFlush(invoiceState);
        int databaseSizeBeforeDelete = invoiceStateRepository.findAll().size();

        // Get the invoiceState
        restInvoiceStateMockMvc.perform(delete("/api/invoice-states/{id}", invoiceState.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<InvoiceState> invoiceStateList = invoiceStateRepository.findAll();
        assertThat(invoiceStateList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(InvoiceState.class);
        InvoiceState invoiceState1 = new InvoiceState();
        invoiceState1.setId(1L);
        InvoiceState invoiceState2 = new InvoiceState();
        invoiceState2.setId(invoiceState1.getId());
        assertThat(invoiceState1).isEqualTo(invoiceState2);
        invoiceState2.setId(2L);
        assertThat(invoiceState1).isNotEqualTo(invoiceState2);
        invoiceState1.setId(null);
        assertThat(invoiceState1).isNotEqualTo(invoiceState2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(InvoiceStateDTO.class);
        InvoiceStateDTO invoiceStateDTO1 = new InvoiceStateDTO();
        invoiceStateDTO1.setId(1L);
        InvoiceStateDTO invoiceStateDTO2 = new InvoiceStateDTO();
        assertThat(invoiceStateDTO1).isNotEqualTo(invoiceStateDTO2);
        invoiceStateDTO2.setId(invoiceStateDTO1.getId());
        assertThat(invoiceStateDTO1).isEqualTo(invoiceStateDTO2);
        invoiceStateDTO2.setId(2L);
        assertThat(invoiceStateDTO1).isNotEqualTo(invoiceStateDTO2);
        invoiceStateDTO1.setId(null);
        assertThat(invoiceStateDTO1).isNotEqualTo(invoiceStateDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(invoiceStateMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(invoiceStateMapper.fromId(null)).isNull();
    }
}
