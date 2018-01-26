package io.epsil.invoice.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A Organisation.
 */
@Entity
@Table(name = "inv_organisation")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Organisation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "slug", length = 15, nullable = false)
    private String slug;

    @NotNull
    @Size(max = 50)
    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @Column(name = "business_identification")
    private String businessIdentification;

    @Column(name = "corporate_name")
    private String corporateName;

    @Column(name = "vat_identification")
    private String vatIdentification;

    @Column(name = "activated")
    private Boolean activated;

    @Column(name = "created_on")
    private Instant createdOn;

    @OneToOne
    @JoinColumn(unique = true)
    private Address address;

    @OneToOne
    @JoinColumn(unique = true)
    private Contact contact;

    @ManyToOne(optional = false)
    @NotNull
    private Tenant tenant;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSlug() {
        return slug;
    }

    public Organisation slug(String slug) {
        this.slug = slug;
        return this;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getName() {
        return name;
    }

    public Organisation name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBusinessIdentification() {
        return businessIdentification;
    }

    public Organisation businessIdentification(String businessIdentification) {
        this.businessIdentification = businessIdentification;
        return this;
    }

    public void setBusinessIdentification(String businessIdentification) {
        this.businessIdentification = businessIdentification;
    }

    public String getCorporateName() {
        return corporateName;
    }

    public Organisation corporateName(String corporateName) {
        this.corporateName = corporateName;
        return this;
    }

    public void setCorporateName(String corporateName) {
        this.corporateName = corporateName;
    }

    public String getVatIdentification() {
        return vatIdentification;
    }

    public Organisation vatIdentification(String vatIdentification) {
        this.vatIdentification = vatIdentification;
        return this;
    }

    public void setVatIdentification(String vatIdentification) {
        this.vatIdentification = vatIdentification;
    }

    public Boolean isActivated() {
        return activated;
    }

    public Organisation activated(Boolean activated) {
        this.activated = activated;
        return this;
    }

    public void setActivated(Boolean activated) {
        this.activated = activated;
    }

    public Instant getCreatedOn() {
        return createdOn;
    }

    public Organisation createdOn(Instant createdOn) {
        this.createdOn = createdOn;
        return this;
    }

    public void setCreatedOn(Instant createdOn) {
        this.createdOn = createdOn;
    }

    public Address getAddress() {
        return address;
    }

    public Organisation address(Address address) {
        this.address = address;
        return this;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Contact getContact() {
        return contact;
    }

    public Organisation contact(Contact contact) {
        this.contact = contact;
        return this;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public Tenant getTenant() {
        return tenant;
    }

    public Organisation tenant(Tenant tenant) {
        this.tenant = tenant;
        return this;
    }

    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
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
        Organisation organisation = (Organisation) o;
        if (organisation.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), organisation.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Organisation{" +
            "id=" + getId() +
            ", slug='" + getSlug() + "'" +
            ", name='" + getName() + "'" +
            ", businessIdentification='" + getBusinessIdentification() + "'" +
            ", corporateName='" + getCorporateName() + "'" +
            ", vatIdentification='" + getVatIdentification() + "'" +
            ", activated='" + isActivated() + "'" +
            ", createdOn='" + getCreatedOn() + "'" +
            "}";
    }
}
