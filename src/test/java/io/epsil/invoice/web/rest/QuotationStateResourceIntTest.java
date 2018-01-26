package io.epsil.invoice.web.rest;

import io.epsil.invoice.InvoiceapiApp;

import io.epsil.invoice.domain.QuotationState;
import io.epsil.invoice.repository.QuotationStateRepository;
import io.epsil.invoice.service.QuotationStateService;
import io.epsil.invoice.service.dto.QuotationStateDTO;
import io.epsil.invoice.service.mapper.QuotationStateMapper;
import io.epsil.invoice.web.rest.errors.ExceptionTranslator;
import io.epsil.invoice.service.dto.QuotationStateCriteria;
import io.epsil.invoice.service.QuotationStateQueryService;

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

import io.epsil.invoice.domain.enumeration.QuotationStatus;
/**
 * Test class for the QuotationStateResource REST controller.
 *
 * @see QuotationStateResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = InvoiceapiApp.class)
public class QuotationStateResourceIntTest {

    private static final QuotationStatus DEFAULT_STATUS = QuotationStatus.CREATED;
    private static final QuotationStatus UPDATED_STATUS = QuotationStatus.COMMITTED;

    private static final Instant DEFAULT_STATUS_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_STATUS_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private QuotationStateRepository quotationStateRepository;

    @Autowired
    private QuotationStateMapper quotationStateMapper;

    @Autowired
    private QuotationStateService quotationStateService;

    @Autowired
    private QuotationStateQueryService quotationStateQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restQuotationStateMockMvc;

    private QuotationState quotationState;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final QuotationStateResource quotationStateResource = new QuotationStateResource(quotationStateService, quotationStateQueryService);
        this.restQuotationStateMockMvc = MockMvcBuilders.standaloneSetup(quotationStateResource)
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
    public static QuotationState createEntity(EntityManager em) {
        QuotationState quotationState = new QuotationState()
            .status(DEFAULT_STATUS)
            .statusDate(DEFAULT_STATUS_DATE);
        return quotationState;
    }

    @Before
    public void initTest() {
        quotationState = createEntity(em);
    }

    @Test
    @Transactional
    public void createQuotationState() throws Exception {
        int databaseSizeBeforeCreate = quotationStateRepository.findAll().size();

        // Create the QuotationState
        QuotationStateDTO quotationStateDTO = quotationStateMapper.toDto(quotationState);
        restQuotationStateMockMvc.perform(post("/api/quotation-states")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(quotationStateDTO)))
            .andExpect(status().isCreated());

        // Validate the QuotationState in the database
        List<QuotationState> quotationStateList = quotationStateRepository.findAll();
        assertThat(quotationStateList).hasSize(databaseSizeBeforeCreate + 1);
        QuotationState testQuotationState = quotationStateList.get(quotationStateList.size() - 1);
        assertThat(testQuotationState.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testQuotationState.getStatusDate()).isEqualTo(DEFAULT_STATUS_DATE);
    }

    @Test
    @Transactional
    public void createQuotationStateWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = quotationStateRepository.findAll().size();

        // Create the QuotationState with an existing ID
        quotationState.setId(1L);
        QuotationStateDTO quotationStateDTO = quotationStateMapper.toDto(quotationState);

        // An entity with an existing ID cannot be created, so this API call must fail
        restQuotationStateMockMvc.perform(post("/api/quotation-states")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(quotationStateDTO)))
            .andExpect(status().isBadRequest());

        // Validate the QuotationState in the database
        List<QuotationState> quotationStateList = quotationStateRepository.findAll();
        assertThat(quotationStateList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = quotationStateRepository.findAll().size();
        // set the field null
        quotationState.setStatus(null);

        // Create the QuotationState, which fails.
        QuotationStateDTO quotationStateDTO = quotationStateMapper.toDto(quotationState);

        restQuotationStateMockMvc.perform(post("/api/quotation-states")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(quotationStateDTO)))
            .andExpect(status().isBadRequest());

        List<QuotationState> quotationStateList = quotationStateRepository.findAll();
        assertThat(quotationStateList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatusDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = quotationStateRepository.findAll().size();
        // set the field null
        quotationState.setStatusDate(null);

        // Create the QuotationState, which fails.
        QuotationStateDTO quotationStateDTO = quotationStateMapper.toDto(quotationState);

        restQuotationStateMockMvc.perform(post("/api/quotation-states")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(quotationStateDTO)))
            .andExpect(status().isBadRequest());

        List<QuotationState> quotationStateList = quotationStateRepository.findAll();
        assertThat(quotationStateList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllQuotationStates() throws Exception {
        // Initialize the database
        quotationStateRepository.saveAndFlush(quotationState);

        // Get all the quotationStateList
        restQuotationStateMockMvc.perform(get("/api/quotation-states?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(quotationState.getId().intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].statusDate").value(hasItem(DEFAULT_STATUS_DATE.toString())));
    }

    @Test
    @Transactional
    public void getQuotationState() throws Exception {
        // Initialize the database
        quotationStateRepository.saveAndFlush(quotationState);

        // Get the quotationState
        restQuotationStateMockMvc.perform(get("/api/quotation-states/{id}", quotationState.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(quotationState.getId().intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.statusDate").value(DEFAULT_STATUS_DATE.toString()));
    }

    @Test
    @Transactional
    public void getAllQuotationStatesByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        quotationStateRepository.saveAndFlush(quotationState);

        // Get all the quotationStateList where status equals to DEFAULT_STATUS
        defaultQuotationStateShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the quotationStateList where status equals to UPDATED_STATUS
        defaultQuotationStateShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllQuotationStatesByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        quotationStateRepository.saveAndFlush(quotationState);

        // Get all the quotationStateList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultQuotationStateShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the quotationStateList where status equals to UPDATED_STATUS
        defaultQuotationStateShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllQuotationStatesByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        quotationStateRepository.saveAndFlush(quotationState);

        // Get all the quotationStateList where status is not null
        defaultQuotationStateShouldBeFound("status.specified=true");

        // Get all the quotationStateList where status is null
        defaultQuotationStateShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    public void getAllQuotationStatesByStatusDateIsEqualToSomething() throws Exception {
        // Initialize the database
        quotationStateRepository.saveAndFlush(quotationState);

        // Get all the quotationStateList where statusDate equals to DEFAULT_STATUS_DATE
        defaultQuotationStateShouldBeFound("statusDate.equals=" + DEFAULT_STATUS_DATE);

        // Get all the quotationStateList where statusDate equals to UPDATED_STATUS_DATE
        defaultQuotationStateShouldNotBeFound("statusDate.equals=" + UPDATED_STATUS_DATE);
    }

    @Test
    @Transactional
    public void getAllQuotationStatesByStatusDateIsInShouldWork() throws Exception {
        // Initialize the database
        quotationStateRepository.saveAndFlush(quotationState);

        // Get all the quotationStateList where statusDate in DEFAULT_STATUS_DATE or UPDATED_STATUS_DATE
        defaultQuotationStateShouldBeFound("statusDate.in=" + DEFAULT_STATUS_DATE + "," + UPDATED_STATUS_DATE);

        // Get all the quotationStateList where statusDate equals to UPDATED_STATUS_DATE
        defaultQuotationStateShouldNotBeFound("statusDate.in=" + UPDATED_STATUS_DATE);
    }

    @Test
    @Transactional
    public void getAllQuotationStatesByStatusDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        quotationStateRepository.saveAndFlush(quotationState);

        // Get all the quotationStateList where statusDate is not null
        defaultQuotationStateShouldBeFound("statusDate.specified=true");

        // Get all the quotationStateList where statusDate is null
        defaultQuotationStateShouldNotBeFound("statusDate.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultQuotationStateShouldBeFound(String filter) throws Exception {
        restQuotationStateMockMvc.perform(get("/api/quotation-states?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(quotationState.getId().intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].statusDate").value(hasItem(DEFAULT_STATUS_DATE.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultQuotationStateShouldNotBeFound(String filter) throws Exception {
        restQuotationStateMockMvc.perform(get("/api/quotation-states?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingQuotationState() throws Exception {
        // Get the quotationState
        restQuotationStateMockMvc.perform(get("/api/quotation-states/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateQuotationState() throws Exception {
        // Initialize the database
        quotationStateRepository.saveAndFlush(quotationState);
        int databaseSizeBeforeUpdate = quotationStateRepository.findAll().size();

        // Update the quotationState
        QuotationState updatedQuotationState = quotationStateRepository.findOne(quotationState.getId());
        // Disconnect from session so that the updates on updatedQuotationState are not directly saved in db
        em.detach(updatedQuotationState);
        updatedQuotationState
            .status(UPDATED_STATUS)
            .statusDate(UPDATED_STATUS_DATE);
        QuotationStateDTO quotationStateDTO = quotationStateMapper.toDto(updatedQuotationState);

        restQuotationStateMockMvc.perform(put("/api/quotation-states")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(quotationStateDTO)))
            .andExpect(status().isOk());

        // Validate the QuotationState in the database
        List<QuotationState> quotationStateList = quotationStateRepository.findAll();
        assertThat(quotationStateList).hasSize(databaseSizeBeforeUpdate);
        QuotationState testQuotationState = quotationStateList.get(quotationStateList.size() - 1);
        assertThat(testQuotationState.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testQuotationState.getStatusDate()).isEqualTo(UPDATED_STATUS_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingQuotationState() throws Exception {
        int databaseSizeBeforeUpdate = quotationStateRepository.findAll().size();

        // Create the QuotationState
        QuotationStateDTO quotationStateDTO = quotationStateMapper.toDto(quotationState);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restQuotationStateMockMvc.perform(put("/api/quotation-states")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(quotationStateDTO)))
            .andExpect(status().isCreated());

        // Validate the QuotationState in the database
        List<QuotationState> quotationStateList = quotationStateRepository.findAll();
        assertThat(quotationStateList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteQuotationState() throws Exception {
        // Initialize the database
        quotationStateRepository.saveAndFlush(quotationState);
        int databaseSizeBeforeDelete = quotationStateRepository.findAll().size();

        // Get the quotationState
        restQuotationStateMockMvc.perform(delete("/api/quotation-states/{id}", quotationState.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<QuotationState> quotationStateList = quotationStateRepository.findAll();
        assertThat(quotationStateList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(QuotationState.class);
        QuotationState quotationState1 = new QuotationState();
        quotationState1.setId(1L);
        QuotationState quotationState2 = new QuotationState();
        quotationState2.setId(quotationState1.getId());
        assertThat(quotationState1).isEqualTo(quotationState2);
        quotationState2.setId(2L);
        assertThat(quotationState1).isNotEqualTo(quotationState2);
        quotationState1.setId(null);
        assertThat(quotationState1).isNotEqualTo(quotationState2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(QuotationStateDTO.class);
        QuotationStateDTO quotationStateDTO1 = new QuotationStateDTO();
        quotationStateDTO1.setId(1L);
        QuotationStateDTO quotationStateDTO2 = new QuotationStateDTO();
        assertThat(quotationStateDTO1).isNotEqualTo(quotationStateDTO2);
        quotationStateDTO2.setId(quotationStateDTO1.getId());
        assertThat(quotationStateDTO1).isEqualTo(quotationStateDTO2);
        quotationStateDTO2.setId(2L);
        assertThat(quotationStateDTO1).isNotEqualTo(quotationStateDTO2);
        quotationStateDTO1.setId(null);
        assertThat(quotationStateDTO1).isNotEqualTo(quotationStateDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(quotationStateMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(quotationStateMapper.fromId(null)).isNull();
    }
}
