package io.epsil.invoice.domain;

import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

import io.epsil.invoice.domain.enumeration.Continent;

/**
 * iso2 codes are two-letter country codes defined in ISO 3166-1
 * iso3 codes are three-letter country codes defined in ISO 3166-1
 * M.49 is a standard for area codes used by the United Nations for statistical purposes
 * dial number to dial for phone numbers
 * tld  top-level domains
 */
@ApiModel(description = "iso2 codes are two-letter country codes defined in ISO 3166-1 iso3 codes are three-letter country codes defined in ISO 3166-1 M.49 is a standard for area codes used by the United Nations for statistical purposes dial number to dial for phone numbers tld  top-level domains")
@Entity
@Table(name = "inv_country")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Country implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(max = 2)
    @Column(name = "iso_2", length = 2, nullable = false)
    private String iso2;

    @NotNull
    @Size(max = 3)
    @Column(name = "iso_3", length = 3, nullable = false)
    private String iso3;

    @NotNull
    @Column(name = "m_49", nullable = false)
    private Integer m49;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "official_name_en")
    private String officialNameEn;

    @Column(name = "official_name_fr")
    private String officialNameFr;

    @Size(max = 5)
    @Column(name = "dial", length = 5)
    private String dial;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "continent", nullable = false)
    private Continent continent;

    @Size(max = 3)
    @Column(name = "tld", length = 3)
    private String tld;

    @Column(name = "flag_32")
    private String flag32;

    @Column(name = "flag_128")
    private String flag128;

    @Column(name = "latitude")
    private String latitude;

    @Column(name = "longitude")
    private String longitude;

    @Column(name = "zoom")
    private Integer zoom;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIso2() {
        return iso2;
    }

    public Country iso2(String iso2) {
        this.iso2 = iso2;
        return this;
    }

    public void setIso2(String iso2) {
        this.iso2 = iso2;
    }

    public String getIso3() {
        return iso3;
    }

    public Country iso3(String iso3) {
        this.iso3 = iso3;
        return this;
    }

    public void setIso3(String iso3) {
        this.iso3 = iso3;
    }

    public Integer getm49() {
        return m49;
    }

    public Country m49(Integer m49) {
        this.m49 = m49;
        return this;
    }

    public void setm49(Integer m49) {
        this.m49 = m49;
    }

    public String getName() {
        return name;
    }

    public Country name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOfficialNameEn() {
        return officialNameEn;
    }

    public Country officialNameEn(String officialNameEn) {
        this.officialNameEn = officialNameEn;
        return this;
    }

    public void setOfficialNameEn(String officialNameEn) {
        this.officialNameEn = officialNameEn;
    }

    public String getOfficialNameFr() {
        return officialNameFr;
    }

    public Country officialNameFr(String officialNameFr) {
        this.officialNameFr = officialNameFr;
        return this;
    }

    public void setOfficialNameFr(String officialNameFr) {
        this.officialNameFr = officialNameFr;
    }

    public String getDial() {
        return dial;
    }

    public Country dial(String dial) {
        this.dial = dial;
        return this;
    }

    public void setDial(String dial) {
        this.dial = dial;
    }

    public Continent getContinent() {
        return continent;
    }

    public Country continent(Continent continent) {
        this.continent = continent;
        return this;
    }

    public void setContinent(Continent continent) {
        this.continent = continent;
    }

    public String getTld() {
        return tld;
    }

    public Country tld(String tld) {
        this.tld = tld;
        return this;
    }

    public void setTld(String tld) {
        this.tld = tld;
    }

    public String getFlag32() {
        return flag32;
    }

    public Country flag32(String flag32) {
        this.flag32 = flag32;
        return this;
    }

    public void setFlag32(String flag32) {
        this.flag32 = flag32;
    }

    public String getFlag128() {
        return flag128;
    }

    public Country flag128(String flag128) {
        this.flag128 = flag128;
        return this;
    }

    public void setFlag128(String flag128) {
        this.flag128 = flag128;
    }

    public String getLatitude() {
        return latitude;
    }

    public Country latitude(String latitude) {
        this.latitude = latitude;
        return this;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public Country longitude(String longitude) {
        this.longitude = longitude;
        return this;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public Integer getZoom() {
        return zoom;
    }

    public Country zoom(Integer zoom) {
        this.zoom = zoom;
        return this;
    }

    public void setZoom(Integer zoom) {
        this.zoom = zoom;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Country country = (Country) o;
        if (country.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), country.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Country{" +
            "id=" + getId() +
            ", iso2='" + getIso2() + "'" +
            ", iso3='" + getIso3() + "'" +
            ", m49=" + getm49() +
            ", name='" + getName() + "'" +
            ", officialNameEn='" + getOfficialNameEn() + "'" +
            ", officialNameFr='" + getOfficialNameFr() + "'" +
            ", dial='" + getDial() + "'" +
            ", continent='" + getContinent() + "'" +
            ", tld='" + getTld() + "'" +
            ", flag32='" + getFlag32() + "'" +
            ", flag128='" + getFlag128() + "'" +
            ", latitude='" + getLatitude() + "'" +
            ", longitude='" + getLongitude() + "'" +
            ", zoom=" + getZoom() +
            "}";
    }
}
