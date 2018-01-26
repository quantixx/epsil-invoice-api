package io.epsil.invoice.web.rest;

import io.epsil.invoice.InvoiceapiApp;

import io.epsil.invoice.domain.Country;
import io.epsil.invoice.repository.CountryRepository;
import io.epsil.invoice.service.CountryService;
import io.epsil.invoice.service.dto.CountryDTO;
import io.epsil.invoice.service.mapper.CountryMapper;
import io.epsil.invoice.web.rest.errors.ExceptionTranslator;
import io.epsil.invoice.service.dto.CountryCriteria;
import io.epsil.invoice.service.CountryQueryService;

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

import io.epsil.invoice.domain.enumeration.Continent;
/**
 * Test class for the CountryResource REST controller.
 *
 * @see CountryResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = InvoiceapiApp.class)
public class CountryResourceIntTest {

    private static final String DEFAULT_ISO_2 = "AA";
    private static final String UPDATED_ISO_2 = "BB";

    private static final String DEFAULT_ISO_3 = "AAA";
    private static final String UPDATED_ISO_3 = "BBB";

    private static final Integer DEFAULT_M_49 = 1;
    private static final Integer UPDATED_M_49 = 2;

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_OFFICIAL_NAME_EN = "AAAAAAAAAA";
    private static final String UPDATED_OFFICIAL_NAME_EN = "BBBBBBBBBB";

    private static final String DEFAULT_OFFICIAL_NAME_FR = "AAAAAAAAAA";
    private static final String UPDATED_OFFICIAL_NAME_FR = "BBBBBBBBBB";

    private static final String DEFAULT_DIAL = "AAAAA";
    private static final String UPDATED_DIAL = "BBBBB";

    private static final Continent DEFAULT_CONTINENT = Continent.AF;
    private static final Continent UPDATED_CONTINENT = Continent.AN;

    private static final String DEFAULT_TLD = "AAA";
    private static final String UPDATED_TLD = "BBB";

    private static final String DEFAULT_FLAG_32 = "AAAAAAAAAA";
    private static final String UPDATED_FLAG_32 = "BBBBBBBBBB";

    private static final String DEFAULT_FLAG_128 = "AAAAAAAAAA";
    private static final String UPDATED_FLAG_128 = "BBBBBBBBBB";

    private static final String DEFAULT_LATITUDE = "AAAAAAAAAA";
    private static final String UPDATED_LATITUDE = "BBBBBBBBBB";

    private static final String DEFAULT_LONGITUDE = "AAAAAAAAAA";
    private static final String UPDATED_LONGITUDE = "BBBBBBBBBB";

    private static final Integer DEFAULT_ZOOM = 1;
    private static final Integer UPDATED_ZOOM = 2;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private CountryMapper countryMapper;

    @Autowired
    private CountryService countryService;

    @Autowired
    private CountryQueryService countryQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCountryMockMvc;

    private Country country;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CountryResource countryResource = new CountryResource(countryService, countryQueryService);
        this.restCountryMockMvc = MockMvcBuilders.standaloneSetup(countryResource)
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
    public static Country createEntity(EntityManager em) {
        Country country = new Country()
            .iso2(DEFAULT_ISO_2)
            .iso3(DEFAULT_ISO_3)
            .m49(DEFAULT_M_49)
            .name(DEFAULT_NAME)
            .officialNameEn(DEFAULT_OFFICIAL_NAME_EN)
            .officialNameFr(DEFAULT_OFFICIAL_NAME_FR)
            .dial(DEFAULT_DIAL)
            .continent(DEFAULT_CONTINENT)
            .tld(DEFAULT_TLD)
            .flag32(DEFAULT_FLAG_32)
            .flag128(DEFAULT_FLAG_128)
            .latitude(DEFAULT_LATITUDE)
            .longitude(DEFAULT_LONGITUDE)
            .zoom(DEFAULT_ZOOM);
        return country;
    }

    @Before
    public void initTest() {
        country = createEntity(em);
    }

    @Test
    @Transactional
    public void createCountry() throws Exception {
        int databaseSizeBeforeCreate = countryRepository.findAll().size();

        // Create the Country
        CountryDTO countryDTO = countryMapper.toDto(country);
        restCountryMockMvc.perform(post("/api/countries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(countryDTO)))
            .andExpect(status().isCreated());

        // Validate the Country in the database
        List<Country> countryList = countryRepository.findAll();
        assertThat(countryList).hasSize(databaseSizeBeforeCreate + 1);
        Country testCountry = countryList.get(countryList.size() - 1);
        assertThat(testCountry.getIso2()).isEqualTo(DEFAULT_ISO_2);
        assertThat(testCountry.getIso3()).isEqualTo(DEFAULT_ISO_3);
        assertThat(testCountry.getm49()).isEqualTo(DEFAULT_M_49);
        assertThat(testCountry.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCountry.getOfficialNameEn()).isEqualTo(DEFAULT_OFFICIAL_NAME_EN);
        assertThat(testCountry.getOfficialNameFr()).isEqualTo(DEFAULT_OFFICIAL_NAME_FR);
        assertThat(testCountry.getDial()).isEqualTo(DEFAULT_DIAL);
        assertThat(testCountry.getContinent()).isEqualTo(DEFAULT_CONTINENT);
        assertThat(testCountry.getTld()).isEqualTo(DEFAULT_TLD);
        assertThat(testCountry.getFlag32()).isEqualTo(DEFAULT_FLAG_32);
        assertThat(testCountry.getFlag128()).isEqualTo(DEFAULT_FLAG_128);
        assertThat(testCountry.getLatitude()).isEqualTo(DEFAULT_LATITUDE);
        assertThat(testCountry.getLongitude()).isEqualTo(DEFAULT_LONGITUDE);
        assertThat(testCountry.getZoom()).isEqualTo(DEFAULT_ZOOM);
    }

    @Test
    @Transactional
    public void createCountryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = countryRepository.findAll().size();

        // Create the Country with an existing ID
        country.setId(1L);
        CountryDTO countryDTO = countryMapper.toDto(country);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCountryMockMvc.perform(post("/api/countries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(countryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Country in the database
        List<Country> countryList = countryRepository.findAll();
        assertThat(countryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkIso2IsRequired() throws Exception {
        int databaseSizeBeforeTest = countryRepository.findAll().size();
        // set the field null
        country.setIso2(null);

        // Create the Country, which fails.
        CountryDTO countryDTO = countryMapper.toDto(country);

        restCountryMockMvc.perform(post("/api/countries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(countryDTO)))
            .andExpect(status().isBadRequest());

        List<Country> countryList = countryRepository.findAll();
        assertThat(countryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkIso3IsRequired() throws Exception {
        int databaseSizeBeforeTest = countryRepository.findAll().size();
        // set the field null
        country.setIso3(null);

        // Create the Country, which fails.
        CountryDTO countryDTO = countryMapper.toDto(country);

        restCountryMockMvc.perform(post("/api/countries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(countryDTO)))
            .andExpect(status().isBadRequest());

        List<Country> countryList = countryRepository.findAll();
        assertThat(countryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkm49IsRequired() throws Exception {
        int databaseSizeBeforeTest = countryRepository.findAll().size();
        // set the field null
        country.setm49(null);

        // Create the Country, which fails.
        CountryDTO countryDTO = countryMapper.toDto(country);

        restCountryMockMvc.perform(post("/api/countries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(countryDTO)))
            .andExpect(status().isBadRequest());

        List<Country> countryList = countryRepository.findAll();
        assertThat(countryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = countryRepository.findAll().size();
        // set the field null
        country.setName(null);

        // Create the Country, which fails.
        CountryDTO countryDTO = countryMapper.toDto(country);

        restCountryMockMvc.perform(post("/api/countries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(countryDTO)))
            .andExpect(status().isBadRequest());

        List<Country> countryList = countryRepository.findAll();
        assertThat(countryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkContinentIsRequired() throws Exception {
        int databaseSizeBeforeTest = countryRepository.findAll().size();
        // set the field null
        country.setContinent(null);

        // Create the Country, which fails.
        CountryDTO countryDTO = countryMapper.toDto(country);

        restCountryMockMvc.perform(post("/api/countries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(countryDTO)))
            .andExpect(status().isBadRequest());

        List<Country> countryList = countryRepository.findAll();
        assertThat(countryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCountries() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList
        restCountryMockMvc.perform(get("/api/countries?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(country.getId().intValue())))
            .andExpect(jsonPath("$.[*].iso2").value(hasItem(DEFAULT_ISO_2.toString())))
            .andExpect(jsonPath("$.[*].iso3").value(hasItem(DEFAULT_ISO_3.toString())))
            .andExpect(jsonPath("$.[*].m49").value(hasItem(DEFAULT_M_49)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].officialNameEn").value(hasItem(DEFAULT_OFFICIAL_NAME_EN.toString())))
            .andExpect(jsonPath("$.[*].officialNameFr").value(hasItem(DEFAULT_OFFICIAL_NAME_FR.toString())))
            .andExpect(jsonPath("$.[*].dial").value(hasItem(DEFAULT_DIAL.toString())))
            .andExpect(jsonPath("$.[*].continent").value(hasItem(DEFAULT_CONTINENT.toString())))
            .andExpect(jsonPath("$.[*].tld").value(hasItem(DEFAULT_TLD.toString())))
            .andExpect(jsonPath("$.[*].flag32").value(hasItem(DEFAULT_FLAG_32.toString())))
            .andExpect(jsonPath("$.[*].flag128").value(hasItem(DEFAULT_FLAG_128.toString())))
            .andExpect(jsonPath("$.[*].latitude").value(hasItem(DEFAULT_LATITUDE.toString())))
            .andExpect(jsonPath("$.[*].longitude").value(hasItem(DEFAULT_LONGITUDE.toString())))
            .andExpect(jsonPath("$.[*].zoom").value(hasItem(DEFAULT_ZOOM)));
    }

    @Test
    @Transactional
    public void getCountry() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get the country
        restCountryMockMvc.perform(get("/api/countries/{id}", country.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(country.getId().intValue()))
            .andExpect(jsonPath("$.iso2").value(DEFAULT_ISO_2.toString()))
            .andExpect(jsonPath("$.iso3").value(DEFAULT_ISO_3.toString()))
            .andExpect(jsonPath("$.m49").value(DEFAULT_M_49))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.officialNameEn").value(DEFAULT_OFFICIAL_NAME_EN.toString()))
            .andExpect(jsonPath("$.officialNameFr").value(DEFAULT_OFFICIAL_NAME_FR.toString()))
            .andExpect(jsonPath("$.dial").value(DEFAULT_DIAL.toString()))
            .andExpect(jsonPath("$.continent").value(DEFAULT_CONTINENT.toString()))
            .andExpect(jsonPath("$.tld").value(DEFAULT_TLD.toString()))
            .andExpect(jsonPath("$.flag32").value(DEFAULT_FLAG_32.toString()))
            .andExpect(jsonPath("$.flag128").value(DEFAULT_FLAG_128.toString()))
            .andExpect(jsonPath("$.latitude").value(DEFAULT_LATITUDE.toString()))
            .andExpect(jsonPath("$.longitude").value(DEFAULT_LONGITUDE.toString()))
            .andExpect(jsonPath("$.zoom").value(DEFAULT_ZOOM));
    }

    @Test
    @Transactional
    public void getAllCountriesByIso2IsEqualToSomething() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where iso2 equals to DEFAULT_ISO_2
        defaultCountryShouldBeFound("iso2.equals=" + DEFAULT_ISO_2);

        // Get all the countryList where iso2 equals to UPDATED_ISO_2
        defaultCountryShouldNotBeFound("iso2.equals=" + UPDATED_ISO_2);
    }

    @Test
    @Transactional
    public void getAllCountriesByIso2IsInShouldWork() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where iso2 in DEFAULT_ISO_2 or UPDATED_ISO_2
        defaultCountryShouldBeFound("iso2.in=" + DEFAULT_ISO_2 + "," + UPDATED_ISO_2);

        // Get all the countryList where iso2 equals to UPDATED_ISO_2
        defaultCountryShouldNotBeFound("iso2.in=" + UPDATED_ISO_2);
    }

    @Test
    @Transactional
    public void getAllCountriesByIso2IsNullOrNotNull() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where iso2 is not null
        defaultCountryShouldBeFound("iso2.specified=true");

        // Get all the countryList where iso2 is null
        defaultCountryShouldNotBeFound("iso2.specified=false");
    }

    @Test
    @Transactional
    public void getAllCountriesByIso3IsEqualToSomething() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where iso3 equals to DEFAULT_ISO_3
        defaultCountryShouldBeFound("iso3.equals=" + DEFAULT_ISO_3);

        // Get all the countryList where iso3 equals to UPDATED_ISO_3
        defaultCountryShouldNotBeFound("iso3.equals=" + UPDATED_ISO_3);
    }

    @Test
    @Transactional
    public void getAllCountriesByIso3IsInShouldWork() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where iso3 in DEFAULT_ISO_3 or UPDATED_ISO_3
        defaultCountryShouldBeFound("iso3.in=" + DEFAULT_ISO_3 + "," + UPDATED_ISO_3);

        // Get all the countryList where iso3 equals to UPDATED_ISO_3
        defaultCountryShouldNotBeFound("iso3.in=" + UPDATED_ISO_3);
    }

    @Test
    @Transactional
    public void getAllCountriesByIso3IsNullOrNotNull() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where iso3 is not null
        defaultCountryShouldBeFound("iso3.specified=true");

        // Get all the countryList where iso3 is null
        defaultCountryShouldNotBeFound("iso3.specified=false");
    }

    @Test
    @Transactional
    public void getAllCountriesBym49IsEqualToSomething() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where m49 equals to DEFAULT_M_49
        defaultCountryShouldBeFound("m49.equals=" + DEFAULT_M_49);

        // Get all the countryList where m49 equals to UPDATED_M_49
        defaultCountryShouldNotBeFound("m49.equals=" + UPDATED_M_49);
    }

    @Test
    @Transactional
    public void getAllCountriesBym49IsInShouldWork() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where m49 in DEFAULT_M_49 or UPDATED_M_49
        defaultCountryShouldBeFound("m49.in=" + DEFAULT_M_49 + "," + UPDATED_M_49);

        // Get all the countryList where m49 equals to UPDATED_M_49
        defaultCountryShouldNotBeFound("m49.in=" + UPDATED_M_49);
    }

    @Test
    @Transactional
    public void getAllCountriesBym49IsNullOrNotNull() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where m49 is not null
        defaultCountryShouldBeFound("m49.specified=true");

        // Get all the countryList where m49 is null
        defaultCountryShouldNotBeFound("m49.specified=false");
    }

    @Test
    @Transactional
    public void getAllCountriesBym49IsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where m49 greater than or equals to DEFAULT_M_49
        defaultCountryShouldBeFound("m49.greaterOrEqualThan=" + DEFAULT_M_49);

        // Get all the countryList where m49 greater than or equals to UPDATED_M_49
        defaultCountryShouldNotBeFound("m49.greaterOrEqualThan=" + UPDATED_M_49);
    }

    @Test
    @Transactional
    public void getAllCountriesBym49IsLessThanSomething() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where m49 less than or equals to DEFAULT_M_49
        defaultCountryShouldNotBeFound("m49.lessThan=" + DEFAULT_M_49);

        // Get all the countryList where m49 less than or equals to UPDATED_M_49
        defaultCountryShouldBeFound("m49.lessThan=" + UPDATED_M_49);
    }


    @Test
    @Transactional
    public void getAllCountriesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where name equals to DEFAULT_NAME
        defaultCountryShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the countryList where name equals to UPDATED_NAME
        defaultCountryShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllCountriesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where name in DEFAULT_NAME or UPDATED_NAME
        defaultCountryShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the countryList where name equals to UPDATED_NAME
        defaultCountryShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllCountriesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where name is not null
        defaultCountryShouldBeFound("name.specified=true");

        // Get all the countryList where name is null
        defaultCountryShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllCountriesByOfficialNameEnIsEqualToSomething() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where officialNameEn equals to DEFAULT_OFFICIAL_NAME_EN
        defaultCountryShouldBeFound("officialNameEn.equals=" + DEFAULT_OFFICIAL_NAME_EN);

        // Get all the countryList where officialNameEn equals to UPDATED_OFFICIAL_NAME_EN
        defaultCountryShouldNotBeFound("officialNameEn.equals=" + UPDATED_OFFICIAL_NAME_EN);
    }

    @Test
    @Transactional
    public void getAllCountriesByOfficialNameEnIsInShouldWork() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where officialNameEn in DEFAULT_OFFICIAL_NAME_EN or UPDATED_OFFICIAL_NAME_EN
        defaultCountryShouldBeFound("officialNameEn.in=" + DEFAULT_OFFICIAL_NAME_EN + "," + UPDATED_OFFICIAL_NAME_EN);

        // Get all the countryList where officialNameEn equals to UPDATED_OFFICIAL_NAME_EN
        defaultCountryShouldNotBeFound("officialNameEn.in=" + UPDATED_OFFICIAL_NAME_EN);
    }

    @Test
    @Transactional
    public void getAllCountriesByOfficialNameEnIsNullOrNotNull() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where officialNameEn is not null
        defaultCountryShouldBeFound("officialNameEn.specified=true");

        // Get all the countryList where officialNameEn is null
        defaultCountryShouldNotBeFound("officialNameEn.specified=false");
    }

    @Test
    @Transactional
    public void getAllCountriesByOfficialNameFrIsEqualToSomething() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where officialNameFr equals to DEFAULT_OFFICIAL_NAME_FR
        defaultCountryShouldBeFound("officialNameFr.equals=" + DEFAULT_OFFICIAL_NAME_FR);

        // Get all the countryList where officialNameFr equals to UPDATED_OFFICIAL_NAME_FR
        defaultCountryShouldNotBeFound("officialNameFr.equals=" + UPDATED_OFFICIAL_NAME_FR);
    }

    @Test
    @Transactional
    public void getAllCountriesByOfficialNameFrIsInShouldWork() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where officialNameFr in DEFAULT_OFFICIAL_NAME_FR or UPDATED_OFFICIAL_NAME_FR
        defaultCountryShouldBeFound("officialNameFr.in=" + DEFAULT_OFFICIAL_NAME_FR + "," + UPDATED_OFFICIAL_NAME_FR);

        // Get all the countryList where officialNameFr equals to UPDATED_OFFICIAL_NAME_FR
        defaultCountryShouldNotBeFound("officialNameFr.in=" + UPDATED_OFFICIAL_NAME_FR);
    }

    @Test
    @Transactional
    public void getAllCountriesByOfficialNameFrIsNullOrNotNull() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where officialNameFr is not null
        defaultCountryShouldBeFound("officialNameFr.specified=true");

        // Get all the countryList where officialNameFr is null
        defaultCountryShouldNotBeFound("officialNameFr.specified=false");
    }

    @Test
    @Transactional
    public void getAllCountriesByDialIsEqualToSomething() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where dial equals to DEFAULT_DIAL
        defaultCountryShouldBeFound("dial.equals=" + DEFAULT_DIAL);

        // Get all the countryList where dial equals to UPDATED_DIAL
        defaultCountryShouldNotBeFound("dial.equals=" + UPDATED_DIAL);
    }

    @Test
    @Transactional
    public void getAllCountriesByDialIsInShouldWork() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where dial in DEFAULT_DIAL or UPDATED_DIAL
        defaultCountryShouldBeFound("dial.in=" + DEFAULT_DIAL + "," + UPDATED_DIAL);

        // Get all the countryList where dial equals to UPDATED_DIAL
        defaultCountryShouldNotBeFound("dial.in=" + UPDATED_DIAL);
    }

    @Test
    @Transactional
    public void getAllCountriesByDialIsNullOrNotNull() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where dial is not null
        defaultCountryShouldBeFound("dial.specified=true");

        // Get all the countryList where dial is null
        defaultCountryShouldNotBeFound("dial.specified=false");
    }

    @Test
    @Transactional
    public void getAllCountriesByContinentIsEqualToSomething() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where continent equals to DEFAULT_CONTINENT
        defaultCountryShouldBeFound("continent.equals=" + DEFAULT_CONTINENT);

        // Get all the countryList where continent equals to UPDATED_CONTINENT
        defaultCountryShouldNotBeFound("continent.equals=" + UPDATED_CONTINENT);
    }

    @Test
    @Transactional
    public void getAllCountriesByContinentIsInShouldWork() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where continent in DEFAULT_CONTINENT or UPDATED_CONTINENT
        defaultCountryShouldBeFound("continent.in=" + DEFAULT_CONTINENT + "," + UPDATED_CONTINENT);

        // Get all the countryList where continent equals to UPDATED_CONTINENT
        defaultCountryShouldNotBeFound("continent.in=" + UPDATED_CONTINENT);
    }

    @Test
    @Transactional
    public void getAllCountriesByContinentIsNullOrNotNull() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where continent is not null
        defaultCountryShouldBeFound("continent.specified=true");

        // Get all the countryList where continent is null
        defaultCountryShouldNotBeFound("continent.specified=false");
    }

    @Test
    @Transactional
    public void getAllCountriesByTldIsEqualToSomething() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where tld equals to DEFAULT_TLD
        defaultCountryShouldBeFound("tld.equals=" + DEFAULT_TLD);

        // Get all the countryList where tld equals to UPDATED_TLD
        defaultCountryShouldNotBeFound("tld.equals=" + UPDATED_TLD);
    }

    @Test
    @Transactional
    public void getAllCountriesByTldIsInShouldWork() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where tld in DEFAULT_TLD or UPDATED_TLD
        defaultCountryShouldBeFound("tld.in=" + DEFAULT_TLD + "," + UPDATED_TLD);

        // Get all the countryList where tld equals to UPDATED_TLD
        defaultCountryShouldNotBeFound("tld.in=" + UPDATED_TLD);
    }

    @Test
    @Transactional
    public void getAllCountriesByTldIsNullOrNotNull() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where tld is not null
        defaultCountryShouldBeFound("tld.specified=true");

        // Get all the countryList where tld is null
        defaultCountryShouldNotBeFound("tld.specified=false");
    }

    @Test
    @Transactional
    public void getAllCountriesByFlag32IsEqualToSomething() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where flag32 equals to DEFAULT_FLAG_32
        defaultCountryShouldBeFound("flag32.equals=" + DEFAULT_FLAG_32);

        // Get all the countryList where flag32 equals to UPDATED_FLAG_32
        defaultCountryShouldNotBeFound("flag32.equals=" + UPDATED_FLAG_32);
    }

    @Test
    @Transactional
    public void getAllCountriesByFlag32IsInShouldWork() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where flag32 in DEFAULT_FLAG_32 or UPDATED_FLAG_32
        defaultCountryShouldBeFound("flag32.in=" + DEFAULT_FLAG_32 + "," + UPDATED_FLAG_32);

        // Get all the countryList where flag32 equals to UPDATED_FLAG_32
        defaultCountryShouldNotBeFound("flag32.in=" + UPDATED_FLAG_32);
    }

    @Test
    @Transactional
    public void getAllCountriesByFlag32IsNullOrNotNull() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where flag32 is not null
        defaultCountryShouldBeFound("flag32.specified=true");

        // Get all the countryList where flag32 is null
        defaultCountryShouldNotBeFound("flag32.specified=false");
    }

    @Test
    @Transactional
    public void getAllCountriesByFlag128IsEqualToSomething() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where flag128 equals to DEFAULT_FLAG_128
        defaultCountryShouldBeFound("flag128.equals=" + DEFAULT_FLAG_128);

        // Get all the countryList where flag128 equals to UPDATED_FLAG_128
        defaultCountryShouldNotBeFound("flag128.equals=" + UPDATED_FLAG_128);
    }

    @Test
    @Transactional
    public void getAllCountriesByFlag128IsInShouldWork() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where flag128 in DEFAULT_FLAG_128 or UPDATED_FLAG_128
        defaultCountryShouldBeFound("flag128.in=" + DEFAULT_FLAG_128 + "," + UPDATED_FLAG_128);

        // Get all the countryList where flag128 equals to UPDATED_FLAG_128
        defaultCountryShouldNotBeFound("flag128.in=" + UPDATED_FLAG_128);
    }

    @Test
    @Transactional
    public void getAllCountriesByFlag128IsNullOrNotNull() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where flag128 is not null
        defaultCountryShouldBeFound("flag128.specified=true");

        // Get all the countryList where flag128 is null
        defaultCountryShouldNotBeFound("flag128.specified=false");
    }

    @Test
    @Transactional
    public void getAllCountriesByLatitudeIsEqualToSomething() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where latitude equals to DEFAULT_LATITUDE
        defaultCountryShouldBeFound("latitude.equals=" + DEFAULT_LATITUDE);

        // Get all the countryList where latitude equals to UPDATED_LATITUDE
        defaultCountryShouldNotBeFound("latitude.equals=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    public void getAllCountriesByLatitudeIsInShouldWork() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where latitude in DEFAULT_LATITUDE or UPDATED_LATITUDE
        defaultCountryShouldBeFound("latitude.in=" + DEFAULT_LATITUDE + "," + UPDATED_LATITUDE);

        // Get all the countryList where latitude equals to UPDATED_LATITUDE
        defaultCountryShouldNotBeFound("latitude.in=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    public void getAllCountriesByLatitudeIsNullOrNotNull() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where latitude is not null
        defaultCountryShouldBeFound("latitude.specified=true");

        // Get all the countryList where latitude is null
        defaultCountryShouldNotBeFound("latitude.specified=false");
    }

    @Test
    @Transactional
    public void getAllCountriesByLongitudeIsEqualToSomething() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where longitude equals to DEFAULT_LONGITUDE
        defaultCountryShouldBeFound("longitude.equals=" + DEFAULT_LONGITUDE);

        // Get all the countryList where longitude equals to UPDATED_LONGITUDE
        defaultCountryShouldNotBeFound("longitude.equals=" + UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    public void getAllCountriesByLongitudeIsInShouldWork() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where longitude in DEFAULT_LONGITUDE or UPDATED_LONGITUDE
        defaultCountryShouldBeFound("longitude.in=" + DEFAULT_LONGITUDE + "," + UPDATED_LONGITUDE);

        // Get all the countryList where longitude equals to UPDATED_LONGITUDE
        defaultCountryShouldNotBeFound("longitude.in=" + UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    public void getAllCountriesByLongitudeIsNullOrNotNull() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where longitude is not null
        defaultCountryShouldBeFound("longitude.specified=true");

        // Get all the countryList where longitude is null
        defaultCountryShouldNotBeFound("longitude.specified=false");
    }

    @Test
    @Transactional
    public void getAllCountriesByZoomIsEqualToSomething() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where zoom equals to DEFAULT_ZOOM
        defaultCountryShouldBeFound("zoom.equals=" + DEFAULT_ZOOM);

        // Get all the countryList where zoom equals to UPDATED_ZOOM
        defaultCountryShouldNotBeFound("zoom.equals=" + UPDATED_ZOOM);
    }

    @Test
    @Transactional
    public void getAllCountriesByZoomIsInShouldWork() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where zoom in DEFAULT_ZOOM or UPDATED_ZOOM
        defaultCountryShouldBeFound("zoom.in=" + DEFAULT_ZOOM + "," + UPDATED_ZOOM);

        // Get all the countryList where zoom equals to UPDATED_ZOOM
        defaultCountryShouldNotBeFound("zoom.in=" + UPDATED_ZOOM);
    }

    @Test
    @Transactional
    public void getAllCountriesByZoomIsNullOrNotNull() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where zoom is not null
        defaultCountryShouldBeFound("zoom.specified=true");

        // Get all the countryList where zoom is null
        defaultCountryShouldNotBeFound("zoom.specified=false");
    }

    @Test
    @Transactional
    public void getAllCountriesByZoomIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where zoom greater than or equals to DEFAULT_ZOOM
        defaultCountryShouldBeFound("zoom.greaterOrEqualThan=" + DEFAULT_ZOOM);

        // Get all the countryList where zoom greater than or equals to UPDATED_ZOOM
        defaultCountryShouldNotBeFound("zoom.greaterOrEqualThan=" + UPDATED_ZOOM);
    }

    @Test
    @Transactional
    public void getAllCountriesByZoomIsLessThanSomething() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where zoom less than or equals to DEFAULT_ZOOM
        defaultCountryShouldNotBeFound("zoom.lessThan=" + DEFAULT_ZOOM);

        // Get all the countryList where zoom less than or equals to UPDATED_ZOOM
        defaultCountryShouldBeFound("zoom.lessThan=" + UPDATED_ZOOM);
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultCountryShouldBeFound(String filter) throws Exception {
        restCountryMockMvc.perform(get("/api/countries?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(country.getId().intValue())))
            .andExpect(jsonPath("$.[*].iso2").value(hasItem(DEFAULT_ISO_2.toString())))
            .andExpect(jsonPath("$.[*].iso3").value(hasItem(DEFAULT_ISO_3.toString())))
            .andExpect(jsonPath("$.[*].m49").value(hasItem(DEFAULT_M_49)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].officialNameEn").value(hasItem(DEFAULT_OFFICIAL_NAME_EN.toString())))
            .andExpect(jsonPath("$.[*].officialNameFr").value(hasItem(DEFAULT_OFFICIAL_NAME_FR.toString())))
            .andExpect(jsonPath("$.[*].dial").value(hasItem(DEFAULT_DIAL.toString())))
            .andExpect(jsonPath("$.[*].continent").value(hasItem(DEFAULT_CONTINENT.toString())))
            .andExpect(jsonPath("$.[*].tld").value(hasItem(DEFAULT_TLD.toString())))
            .andExpect(jsonPath("$.[*].flag32").value(hasItem(DEFAULT_FLAG_32.toString())))
            .andExpect(jsonPath("$.[*].flag128").value(hasItem(DEFAULT_FLAG_128.toString())))
            .andExpect(jsonPath("$.[*].latitude").value(hasItem(DEFAULT_LATITUDE.toString())))
            .andExpect(jsonPath("$.[*].longitude").value(hasItem(DEFAULT_LONGITUDE.toString())))
            .andExpect(jsonPath("$.[*].zoom").value(hasItem(DEFAULT_ZOOM)));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultCountryShouldNotBeFound(String filter) throws Exception {
        restCountryMockMvc.perform(get("/api/countries?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingCountry() throws Exception {
        // Get the country
        restCountryMockMvc.perform(get("/api/countries/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCountry() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);
        int databaseSizeBeforeUpdate = countryRepository.findAll().size();

        // Update the country
        Country updatedCountry = countryRepository.findOne(country.getId());
        // Disconnect from session so that the updates on updatedCountry are not directly saved in db
        em.detach(updatedCountry);
        updatedCountry
            .iso2(UPDATED_ISO_2)
            .iso3(UPDATED_ISO_3)
            .m49(UPDATED_M_49)
            .name(UPDATED_NAME)
            .officialNameEn(UPDATED_OFFICIAL_NAME_EN)
            .officialNameFr(UPDATED_OFFICIAL_NAME_FR)
            .dial(UPDATED_DIAL)
            .continent(UPDATED_CONTINENT)
            .tld(UPDATED_TLD)
            .flag32(UPDATED_FLAG_32)
            .flag128(UPDATED_FLAG_128)
            .latitude(UPDATED_LATITUDE)
            .longitude(UPDATED_LONGITUDE)
            .zoom(UPDATED_ZOOM);
        CountryDTO countryDTO = countryMapper.toDto(updatedCountry);

        restCountryMockMvc.perform(put("/api/countries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(countryDTO)))
            .andExpect(status().isOk());

        // Validate the Country in the database
        List<Country> countryList = countryRepository.findAll();
        assertThat(countryList).hasSize(databaseSizeBeforeUpdate);
        Country testCountry = countryList.get(countryList.size() - 1);
        assertThat(testCountry.getIso2()).isEqualTo(UPDATED_ISO_2);
        assertThat(testCountry.getIso3()).isEqualTo(UPDATED_ISO_3);
        assertThat(testCountry.getm49()).isEqualTo(UPDATED_M_49);
        assertThat(testCountry.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCountry.getOfficialNameEn()).isEqualTo(UPDATED_OFFICIAL_NAME_EN);
        assertThat(testCountry.getOfficialNameFr()).isEqualTo(UPDATED_OFFICIAL_NAME_FR);
        assertThat(testCountry.getDial()).isEqualTo(UPDATED_DIAL);
        assertThat(testCountry.getContinent()).isEqualTo(UPDATED_CONTINENT);
        assertThat(testCountry.getTld()).isEqualTo(UPDATED_TLD);
        assertThat(testCountry.getFlag32()).isEqualTo(UPDATED_FLAG_32);
        assertThat(testCountry.getFlag128()).isEqualTo(UPDATED_FLAG_128);
        assertThat(testCountry.getLatitude()).isEqualTo(UPDATED_LATITUDE);
        assertThat(testCountry.getLongitude()).isEqualTo(UPDATED_LONGITUDE);
        assertThat(testCountry.getZoom()).isEqualTo(UPDATED_ZOOM);
    }

    @Test
    @Transactional
    public void updateNonExistingCountry() throws Exception {
        int databaseSizeBeforeUpdate = countryRepository.findAll().size();

        // Create the Country
        CountryDTO countryDTO = countryMapper.toDto(country);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCountryMockMvc.perform(put("/api/countries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(countryDTO)))
            .andExpect(status().isCreated());

        // Validate the Country in the database
        List<Country> countryList = countryRepository.findAll();
        assertThat(countryList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCountry() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);
        int databaseSizeBeforeDelete = countryRepository.findAll().size();

        // Get the country
        restCountryMockMvc.perform(delete("/api/countries/{id}", country.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Country> countryList = countryRepository.findAll();
        assertThat(countryList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Country.class);
        Country country1 = new Country();
        country1.setId(1L);
        Country country2 = new Country();
        country2.setId(country1.getId());
        assertThat(country1).isEqualTo(country2);
        country2.setId(2L);
        assertThat(country1).isNotEqualTo(country2);
        country1.setId(null);
        assertThat(country1).isNotEqualTo(country2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CountryDTO.class);
        CountryDTO countryDTO1 = new CountryDTO();
        countryDTO1.setId(1L);
        CountryDTO countryDTO2 = new CountryDTO();
        assertThat(countryDTO1).isNotEqualTo(countryDTO2);
        countryDTO2.setId(countryDTO1.getId());
        assertThat(countryDTO1).isEqualTo(countryDTO2);
        countryDTO2.setId(2L);
        assertThat(countryDTO1).isNotEqualTo(countryDTO2);
        countryDTO1.setId(null);
        assertThat(countryDTO1).isNotEqualTo(countryDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(countryMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(countryMapper.fromId(null)).isNull();
    }
}
