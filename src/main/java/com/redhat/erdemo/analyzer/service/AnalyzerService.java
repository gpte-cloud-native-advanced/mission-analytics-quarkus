package com.redhat.erdemo.analyzer.service;

import java.util.List;
import java.util.stream.Collectors;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import com.redhat.erdemo.responder.model.Responder;
import com.redhat.erdemo.responder.model.ResponderStats;
import com.redhat.erdemo.responder.repository.ResponderEntity;
import com.redhat.erdemo.responder.repository.ResponderRepository;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Triple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class AnalyzerService {

    private static final Logger log = LoggerFactory.getLogger(ResponderService.class);

    @Inject
    ResponderRepository repository;

    @Inject
    EventPublisher eventPublisher;

    @Transactional
    public ResponderStats getResponderStats() {
        return new ResponderStats(repository.activeRespondersCount(), repository.enrolledRespondersCount());
    }

    @Transactional
    public Responder getResponder(long id) {
        return toResponder(repository.findById(id));
    }

    @Transactional
    public Responder getResponderByName(String name) {
        return toResponder(repository.findByName(name));
    }

    @Transactional
    public List<Responder> availableResponders() {
        return repository.availableResponders().stream().map(this::toResponder)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<Responder> availableResponders(int limit, int offset) {
        return repository.availableResponders(limit, offset).stream().map(this::toResponder)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<Responder> allResponders() {
        return repository.allResponders().stream().map(this::toResponder)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<Responder> allResponders(int limit, int offset) {
        return repository.allResponders(limit, offset).stream().map(this::toResponder)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<Responder> personResponders() {
        return repository.personResponders().stream().map(this::toResponder)
                .collect(Collectors.toList());
    }

    public List<Responder> personResponders(int limit, int offset) {
        return repository.personResponders(limit, offset).stream().map(this::toResponder)
                .collect(Collectors.toList());
    }

    @Transactional
    public Responder createResponder(Responder responder) {

        ResponderEntity entity = fromResponder(responder);
        repository.create(entity);
        eventPublisher.responderCreated(entity.getId());
        return toResponder(entity);
    }

    @Transactional
    public void createResponders(List<Responder> responders) {
        List<Long> responderIds = responders.stream()
                .map(this::fromResponder)
                .map(re -> repository.create(re).getId())
                .collect(Collectors.toList());

        eventPublisher.respondersCreated(responderIds);
    }

    @Transactional
    public Triple<Boolean, String, Responder> updateResponder(Responder updateTo) {

        ResponderEntity entity = fromResponder(updateTo);
        Triple<Boolean, String, ResponderEntity> result = repository.update(entity);
        return ImmutableTriple.of(result.getLeft(), result.getMiddle(), toResponder(result.getRight()));

    }

    @Transactional
    public Triple<Boolean, String, Responder> updateResponderLocation(Responder updateTo) {

        // only update location for responders during a mission
        Responder current = getResponder(Long.parseLong(updateTo.getId()));
        if (current == null) {
            log.warn("Responder with id '" + updateTo.getId() + "' not found in the database");
            return ImmutableTriple.of(false, "Responder with id + " + updateTo.getId() + " not found.", null);
        }
        if (current.isAvailable()) {
            log.warn("Responder with id '" + updateTo.getId() + "' is available. Ignoring location update");
            return ImmutableTriple.of(false, "Responder with id + " + updateTo.getId() + " is available.", current);
        }
        return updateResponder(updateTo);
    }

    private Responder toResponder(ResponderEntity entity) {

        if (entity == null) {
            return null;
        }

        return new Responder.Builder(Long.toString(entity.getId()))
                .name(entity.getName())
                .phoneNumber(entity.getPhoneNumber())
                .latitude(entity.getCurrentPositionLatitude())
                .longitude(entity.getCurrentPositionLongitude())
                .boatCapacity(entity.getBoatCapacity())
                .medicalKit(entity.getMedicalKit())
                .available(entity.isAvailable())
                .person(entity.isPerson())
                .enrolled(entity.isEnrolled())
                .build();
    }

    private ResponderEntity fromResponder(Responder responder) {

        if (responder == null) {
            return null;
        }

        return new ResponderEntity.Builder(responder.getId() == null ? 0 : Long.parseLong(responder.getId()), 0L)
                .name(responder.getName())
                .phoneNumber(responder.getPhoneNumber())
                .currentPositionLatitude(responder.getLatitude())
                .currentPositionLongitude(responder.getLongitude())
                .boatCapacity(responder.getBoatCapacity())
                .medicalKit(responder.isMedicalKit())
                .available(responder.isAvailable())
                .enrolled(responder.isEnrolled())
                .person(responder.isPerson())
                .build();
    }

    @Transactional
    public void reset() {
        log.info("Reset called");
        repository.reset();
    }


    @Transactional
    public void clear() {
        log.info("Clear called");
        List<Long> responderIds = repository.nonPersonResponders().stream().map(ResponderEntity::getId).collect(Collectors.toList());
        repository.clear();

        eventPublisher.respondersDeleted(responderIds);
    }

}
