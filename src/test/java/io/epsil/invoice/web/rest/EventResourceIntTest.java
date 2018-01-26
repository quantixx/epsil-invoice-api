package io.epsil.invoice.web.rest;

import io.epsil.invoice.InvoiceapiApp;

import io.epsil.invoice.domain.Event;
import io.epsil.invoice.domain.Tenant;
import io.epsil.invoice.repository.EventRepository;
import io.epsil.invoice.service.EventService;
import io.epsil.invoice.service.dto.EventDTO;
import io.epsil.invoice.service.mapper.EventMapper;
import io.epsil.invoice.web.rest.errors.ExceptionTranslator;
import io.epsil.invoice.service.dto.EventCriteria;
import io.epsil.invoice.service.EventQueryService;

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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static io.epsil.invoice.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the EventResource REST controller.
 *
 * @see EventResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = InvoiceapiApp.class)
public class EventResourceIntTest {

    private static final String DEFAULT_SLUG = "AAAAAAAAAA";
    private static final String UPDATED_SLUG = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_VIRTUAL = false;
    private static final Boolean UPDATED_VIRTUAL = true;

    private static final LocalDate DEFAULT_STARTS_ON = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_STARTS_ON = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_ENDS_ON = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_ENDS_ON = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private EventMapper eventMapper;

    @Autowired
    private EventService eventService;

    @Autowired
    private EventQueryService eventQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restEventMockMvc;

    private Event event;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final EventResource eventResource = new EventResource(eventService, eventQueryService);
        this.restEventMockMvc = MockMvcBuilders.standaloneSetup(eventResource)
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
    public static Event createEntity(EntityManager em) {
        Event event = new Event()
            .slug(DEFAULT_SLUG)
            .name(DEFAULT_NAME)
            .virtual(DEFAULT_VIRTUAL)
            .startsOn(DEFAULT_STARTS_ON)
            .endsOn(DEFAULT_ENDS_ON);
        // Add required entity
        Tenant tenant = TenantResourceIntTest.createEntity(em);
        em.persist(tenant);
        em.flush();
        event.setTenant(tenant);
        return event;
    }

    @Before
    public void initTest() {
        event = createEntity(em);
    }

    @Test
    @Transactional
    public void createEvent() throws Exception {
        int databaseSizeBeforeCreate = eventRepository.findAll().size();

        // Create the Event
        EventDTO eventDTO = eventMapper.toDto(event);
        restEventMockMvc.perform(post("/api/events")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(eventDTO)))
            .andExpect(status().isCreated());

        // Validate the Event in the database
        List<Event> eventList = eventRepository.findAll();
        assertThat(eventList).hasSize(databaseSizeBeforeCreate + 1);
        Event testEvent = eventList.get(eventList.size() - 1);
        assertThat(testEvent.getSlug()).isEqualTo(DEFAULT_SLUG);
        assertThat(testEvent.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testEvent.isVirtual()).isEqualTo(DEFAULT_VIRTUAL);
        assertThat(testEvent.getStartsOn()).isEqualTo(DEFAULT_STARTS_ON);
        assertThat(testEvent.getEndsOn()).isEqualTo(DEFAULT_ENDS_ON);
    }

    @Test
    @Transactional
    public void createEventWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = eventRepository.findAll().size();

        // Create the Event with an existing ID
        event.setId(1L);
        EventDTO eventDTO = eventMapper.toDto(event);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEventMockMvc.perform(post("/api/events")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(eventDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Event in the database
        List<Event> eventList = eventRepository.findAll();
        assertThat(eventList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkSlugIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventRepository.findAll().size();
        // set the field null
        event.setSlug(null);

        // Create the Event, which fails.
        EventDTO eventDTO = eventMapper.toDto(event);

        restEventMockMvc.perform(post("/api/events")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(eventDTO)))
            .andExpect(status().isBadRequest());

        List<Event> eventList = eventRepository.findAll();
        assertThat(eventList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventRepository.findAll().size();
        // set the field null
        event.setName(null);

        // Create the Event, which fails.
        EventDTO eventDTO = eventMapper.toDto(event);

        restEventMockMvc.perform(post("/api/events")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(eventDTO)))
            .andExpect(status().isBadRequest());

        List<Event> eventList = eventRepository.findAll();
        assertThat(eventList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllEvents() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList
        restEventMockMvc.perform(get("/api/events?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(event.getId().intValue())))
            .andExpect(jsonPath("$.[*].slug").value(hasItem(DEFAULT_SLUG.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].virtual").value(hasItem(DEFAULT_VIRTUAL.booleanValue())))
            .andExpect(jsonPath("$.[*].startsOn").value(hasItem(DEFAULT_STARTS_ON.toString())))
            .andExpect(jsonPath("$.[*].endsOn").value(hasItem(DEFAULT_ENDS_ON.toString())));
    }

    @Test
    @Transactional
    public void getEvent() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get the event
        restEventMockMvc.perform(get("/api/events/{id}", event.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(event.getId().intValue()))
            .andExpect(jsonPath("$.slug").value(DEFAULT_SLUG.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.virtual").value(DEFAULT_VIRTUAL.booleanValue()))
            .andExpect(jsonPath("$.startsOn").value(DEFAULT_STARTS_ON.toString()))
            .andExpect(jsonPath("$.endsOn").value(DEFAULT_ENDS_ON.toString()));
    }

    @Test
    @Transactional
    public void getAllEventsBySlugIsEqualToSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where slug equals to DEFAULT_SLUG
        defaultEventShouldBeFound("slug.equals=" + DEFAULT_SLUG);

        // Get all the eventList where slug equals to UPDATED_SLUG
        defaultEventShouldNotBeFound("slug.equals=" + UPDATED_SLUG);
    }

    @Test
    @Transactional
    public void getAllEventsBySlugIsInShouldWork() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where slug in DEFAULT_SLUG or UPDATED_SLUG
        defaultEventShouldBeFound("slug.in=" + DEFAULT_SLUG + "," + UPDATED_SLUG);

        // Get all the eventList where slug equals to UPDATED_SLUG
        defaultEventShouldNotBeFound("slug.in=" + UPDATED_SLUG);
    }

    @Test
    @Transactional
    public void getAllEventsBySlugIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where slug is not null
        defaultEventShouldBeFound("slug.specified=true");

        // Get all the eventList where slug is null
        defaultEventShouldNotBeFound("slug.specified=false");
    }

    @Test
    @Transactional
    public void getAllEventsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where name equals to DEFAULT_NAME
        defaultEventShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the eventList where name equals to UPDATED_NAME
        defaultEventShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllEventsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where name in DEFAULT_NAME or UPDATED_NAME
        defaultEventShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the eventList where name equals to UPDATED_NAME
        defaultEventShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllEventsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where name is not null
        defaultEventShouldBeFound("name.specified=true");

        // Get all the eventList where name is null
        defaultEventShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllEventsByVirtualIsEqualToSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where virtual equals to DEFAULT_VIRTUAL
        defaultEventShouldBeFound("virtual.equals=" + DEFAULT_VIRTUAL);

        // Get all the eventList where virtual equals to UPDATED_VIRTUAL
        defaultEventShouldNotBeFound("virtual.equals=" + UPDATED_VIRTUAL);
    }

    @Test
    @Transactional
    public void getAllEventsByVirtualIsInShouldWork() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where virtual in DEFAULT_VIRTUAL or UPDATED_VIRTUAL
        defaultEventShouldBeFound("virtual.in=" + DEFAULT_VIRTUAL + "," + UPDATED_VIRTUAL);

        // Get all the eventList where virtual equals to UPDATED_VIRTUAL
        defaultEventShouldNotBeFound("virtual.in=" + UPDATED_VIRTUAL);
    }

    @Test
    @Transactional
    public void getAllEventsByVirtualIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where virtual is not null
        defaultEventShouldBeFound("virtual.specified=true");

        // Get all the eventList where virtual is null
        defaultEventShouldNotBeFound("virtual.specified=false");
    }

    @Test
    @Transactional
    public void getAllEventsByStartsOnIsEqualToSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where startsOn equals to DEFAULT_STARTS_ON
        defaultEventShouldBeFound("startsOn.equals=" + DEFAULT_STARTS_ON);

        // Get all the eventList where startsOn equals to UPDATED_STARTS_ON
        defaultEventShouldNotBeFound("startsOn.equals=" + UPDATED_STARTS_ON);
    }

    @Test
    @Transactional
    public void getAllEventsByStartsOnIsInShouldWork() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where startsOn in DEFAULT_STARTS_ON or UPDATED_STARTS_ON
        defaultEventShouldBeFound("startsOn.in=" + DEFAULT_STARTS_ON + "," + UPDATED_STARTS_ON);

        // Get all the eventList where startsOn equals to UPDATED_STARTS_ON
        defaultEventShouldNotBeFound("startsOn.in=" + UPDATED_STARTS_ON);
    }

    @Test
    @Transactional
    public void getAllEventsByStartsOnIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where startsOn is not null
        defaultEventShouldBeFound("startsOn.specified=true");

        // Get all the eventList where startsOn is null
        defaultEventShouldNotBeFound("startsOn.specified=false");
    }

    @Test
    @Transactional
    public void getAllEventsByStartsOnIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where startsOn greater than or equals to DEFAULT_STARTS_ON
        defaultEventShouldBeFound("startsOn.greaterOrEqualThan=" + DEFAULT_STARTS_ON);

        // Get all the eventList where startsOn greater than or equals to UPDATED_STARTS_ON
        defaultEventShouldNotBeFound("startsOn.greaterOrEqualThan=" + UPDATED_STARTS_ON);
    }

    @Test
    @Transactional
    public void getAllEventsByStartsOnIsLessThanSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where startsOn less than or equals to DEFAULT_STARTS_ON
        defaultEventShouldNotBeFound("startsOn.lessThan=" + DEFAULT_STARTS_ON);

        // Get all the eventList where startsOn less than or equals to UPDATED_STARTS_ON
        defaultEventShouldBeFound("startsOn.lessThan=" + UPDATED_STARTS_ON);
    }


    @Test
    @Transactional
    public void getAllEventsByEndsOnIsEqualToSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where endsOn equals to DEFAULT_ENDS_ON
        defaultEventShouldBeFound("endsOn.equals=" + DEFAULT_ENDS_ON);

        // Get all the eventList where endsOn equals to UPDATED_ENDS_ON
        defaultEventShouldNotBeFound("endsOn.equals=" + UPDATED_ENDS_ON);
    }

    @Test
    @Transactional
    public void getAllEventsByEndsOnIsInShouldWork() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where endsOn in DEFAULT_ENDS_ON or UPDATED_ENDS_ON
        defaultEventShouldBeFound("endsOn.in=" + DEFAULT_ENDS_ON + "," + UPDATED_ENDS_ON);

        // Get all the eventList where endsOn equals to UPDATED_ENDS_ON
        defaultEventShouldNotBeFound("endsOn.in=" + UPDATED_ENDS_ON);
    }

    @Test
    @Transactional
    public void getAllEventsByEndsOnIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where endsOn is not null
        defaultEventShouldBeFound("endsOn.specified=true");

        // Get all the eventList where endsOn is null
        defaultEventShouldNotBeFound("endsOn.specified=false");
    }

    @Test
    @Transactional
    public void getAllEventsByEndsOnIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where endsOn greater than or equals to DEFAULT_ENDS_ON
        defaultEventShouldBeFound("endsOn.greaterOrEqualThan=" + DEFAULT_ENDS_ON);

        // Get all the eventList where endsOn greater than or equals to UPDATED_ENDS_ON
        defaultEventShouldNotBeFound("endsOn.greaterOrEqualThan=" + UPDATED_ENDS_ON);
    }

    @Test
    @Transactional
    public void getAllEventsByEndsOnIsLessThanSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where endsOn less than or equals to DEFAULT_ENDS_ON
        defaultEventShouldNotBeFound("endsOn.lessThan=" + DEFAULT_ENDS_ON);

        // Get all the eventList where endsOn less than or equals to UPDATED_ENDS_ON
        defaultEventShouldBeFound("endsOn.lessThan=" + UPDATED_ENDS_ON);
    }


    @Test
    @Transactional
    public void getAllEventsByTenantIsEqualToSomething() throws Exception {
        // Initialize the database
        Tenant tenant = TenantResourceIntTest.createEntity(em);
        em.persist(tenant);
        em.flush();
        event.setTenant(tenant);
        eventRepository.saveAndFlush(event);
        Long tenantId = tenant.getId();

        // Get all the eventList where tenant equals to tenantId
        defaultEventShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the eventList where tenant equals to tenantId + 1
        defaultEventShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultEventShouldBeFound(String filter) throws Exception {
        restEventMockMvc.perform(get("/api/events?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(event.getId().intValue())))
            .andExpect(jsonPath("$.[*].slug").value(hasItem(DEFAULT_SLUG.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].virtual").value(hasItem(DEFAULT_VIRTUAL.booleanValue())))
            .andExpect(jsonPath("$.[*].startsOn").value(hasItem(DEFAULT_STARTS_ON.toString())))
            .andExpect(jsonPath("$.[*].endsOn").value(hasItem(DEFAULT_ENDS_ON.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultEventShouldNotBeFound(String filter) throws Exception {
        restEventMockMvc.perform(get("/api/events?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingEvent() throws Exception {
        // Get the event
        restEventMockMvc.perform(get("/api/events/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEvent() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);
        int databaseSizeBeforeUpdate = eventRepository.findAll().size();

        // Update the event
        Event updatedEvent = eventRepository.findOne(event.getId());
        // Disconnect from session so that the updates on updatedEvent are not directly saved in db
        em.detach(updatedEvent);
        updatedEvent
            .slug(UPDATED_SLUG)
            .name(UPDATED_NAME)
            .virtual(UPDATED_VIRTUAL)
            .startsOn(UPDATED_STARTS_ON)
            .endsOn(UPDATED_ENDS_ON);
        EventDTO eventDTO = eventMapper.toDto(updatedEvent);

        restEventMockMvc.perform(put("/api/events")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(eventDTO)))
            .andExpect(status().isOk());

        // Validate the Event in the database
        List<Event> eventList = eventRepository.findAll();
        assertThat(eventList).hasSize(databaseSizeBeforeUpdate);
        Event testEvent = eventList.get(eventList.size() - 1);
        assertThat(testEvent.getSlug()).isEqualTo(UPDATED_SLUG);
        assertThat(testEvent.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testEvent.isVirtual()).isEqualTo(UPDATED_VIRTUAL);
        assertThat(testEvent.getStartsOn()).isEqualTo(UPDATED_STARTS_ON);
        assertThat(testEvent.getEndsOn()).isEqualTo(UPDATED_ENDS_ON);
    }

    @Test
    @Transactional
    public void updateNonExistingEvent() throws Exception {
        int databaseSizeBeforeUpdate = eventRepository.findAll().size();

        // Create the Event
        EventDTO eventDTO = eventMapper.toDto(event);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restEventMockMvc.perform(put("/api/events")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(eventDTO)))
            .andExpect(status().isCreated());

        // Validate the Event in the database
        List<Event> eventList = eventRepository.findAll();
        assertThat(eventList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteEvent() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);
        int databaseSizeBeforeDelete = eventRepository.findAll().size();

        // Get the event
        restEventMockMvc.perform(delete("/api/events/{id}", event.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Event> eventList = eventRepository.findAll();
        assertThat(eventList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Event.class);
        Event event1 = new Event();
        event1.setId(1L);
        Event event2 = new Event();
        event2.setId(event1.getId());
        assertThat(event1).isEqualTo(event2);
        event2.setId(2L);
        assertThat(event1).isNotEqualTo(event2);
        event1.setId(null);
        assertThat(event1).isNotEqualTo(event2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EventDTO.class);
        EventDTO eventDTO1 = new EventDTO();
        eventDTO1.setId(1L);
        EventDTO eventDTO2 = new EventDTO();
        assertThat(eventDTO1).isNotEqualTo(eventDTO2);
        eventDTO2.setId(eventDTO1.getId());
        assertThat(eventDTO1).isEqualTo(eventDTO2);
        eventDTO2.setId(2L);
        assertThat(eventDTO1).isNotEqualTo(eventDTO2);
        eventDTO1.setId(null);
        assertThat(eventDTO1).isNotEqualTo(eventDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(eventMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(eventMapper.fromId(null)).isNull();
    }
}
