package com.uburakcakmak.shared.domain;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
public abstract class AggregateRoot<ID> {
    private ID id;
    private final List<Object> domainEvents = new ArrayList<>();
    public ID getId() { return id; }
    protected void setId(ID id) { this.id = id; }
    protected void registerEvent(Object event) { this.domainEvents.add(event); }
    public List<Object> getDomainEvents() { return Collections.unmodifiableList(domainEvents); }
    public void clearDomainEvents() { this.domainEvents.clear(); }
}
