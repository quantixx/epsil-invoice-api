package io.epsil.invoice.service.dto;

import java.io.Serializable;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;


import io.github.jhipster.service.filter.LocalDateFilter;



/**
 * Criteria class for the Event entity. This class is used in EventResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /events?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class EventCriteria implements Serializable {
    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private StringFilter slug;

    private StringFilter name;

    private BooleanFilter virtual;

    private LocalDateFilter startsOn;

    private LocalDateFilter endsOn;

    private LongFilter tenantId;

    public EventCriteria() {
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

    public BooleanFilter getVirtual() {
        return virtual;
    }

    public void setVirtual(BooleanFilter virtual) {
        this.virtual = virtual;
    }

    public LocalDateFilter getStartsOn() {
        return startsOn;
    }

    public void setStartsOn(LocalDateFilter startsOn) {
        this.startsOn = startsOn;
    }

    public LocalDateFilter getEndsOn() {
        return endsOn;
    }

    public void setEndsOn(LocalDateFilter endsOn) {
        this.endsOn = endsOn;
    }

    public LongFilter getTenantId() {
        return tenantId;
    }

    public void setTenantId(LongFilter tenantId) {
        this.tenantId = tenantId;
    }

    @Override
    public String toString() {
        return "EventCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (slug != null ? "slug=" + slug + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (virtual != null ? "virtual=" + virtual + ", " : "") +
                (startsOn != null ? "startsOn=" + startsOn + ", " : "") +
                (endsOn != null ? "endsOn=" + endsOn + ", " : "") +
                (tenantId != null ? "tenantId=" + tenantId + ", " : "") +
            "}";
    }

}
