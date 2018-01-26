package io.epsil.invoice.service.dto;

import java.io.Serializable;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

import io.github.jhipster.service.filter.InstantFilter;




/**
 * Criteria class for the Organisation entity. This class is used in OrganisationResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /organisations?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class OrganisationCriteria implements Serializable {
    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private StringFilter slug;

    private StringFilter name;

    private StringFilter businessIdentification;

    private StringFilter corporateName;

    private StringFilter vatIdentification;

    private BooleanFilter activated;

    private InstantFilter createdOn;

    private LongFilter addressId;

    private LongFilter contactId;

    private LongFilter tenantId;

    public OrganisationCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getSlug() {
        return slug;
    }

    public void setSlug(StringFilter slug) {
        this.slug = slug;
    }

    public StringFilter getName() {
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getBusinessIdentification() {
        return businessIdentification;
    }

    public void setBusinessIdentification(StringFilter businessIdentification) {
        this.businessIdentification = businessIdentification;
    }

    public StringFilter getCorporateName() {
        return corporateName;
    }

    public void setCorporateName(StringFilter corporateName) {
        this.corporateName = corporateName;
    }

    public StringFilter getVatIdentification() {
        return vatIdentification;
    }

    public void setVatIdentification(StringFilter vatIdentification) {
        this.vatIdentification = vatIdentification;
    }

    public BooleanFilter getActivated() {
        return activated;
    }

    public void setActivated(BooleanFilter activated) {
        this.activated = activated;
    }

    public InstantFilter getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(InstantFilter createdOn) {
        this.createdOn = createdOn;
    }

    public LongFilter getAddressId() {
        return addressId;
    }

    public void setAddressId(LongFilter addressId) {
        this.addressId = addressId;
    }

    public LongFilter getContactId() {
        return contactId;
    }

    public void setContactId(LongFilter contactId) {
        this.contactId = contactId;
    }

    public LongFilter getTenantId() {
        return tenantId;
    }

    public void setTenantId(LongFilter tenantId) {
        this.tenantId = tenantId;
    }

    @Override
    public String toString() {
        return "OrganisationCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (slug != null ? "slug=" + slug + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (businessIdentification != null ? "businessIdentification=" + businessIdentification + ", " : "") +
                (corporateName != null ? "corporateName=" + corporateName + ", " : "") +
                (vatIdentification != null ? "vatIdentification=" + vatIdentification + ", " : "") +
                (activated != null ? "activated=" + activated + ", " : "") +
                (createdOn != null ? "createdOn=" + createdOn + ", " : "") +
                (addressId != null ? "addressId=" + addressId + ", " : "") +
                (contactId != null ? "contactId=" + contactId + ", " : "") +
                (tenantId != null ? "tenantId=" + tenantId + ", " : "") +
            "}";
    }

}
