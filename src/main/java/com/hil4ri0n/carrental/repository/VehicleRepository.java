package com.hil4ri0n.carrental.repository;

import com.hil4ri0n.carrental.model.Vehicle;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.util.*;

@ApplicationScoped
public class VehicleRepository {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void save(Vehicle vehicle) {
        if (vehicle.getId() == null) {
            vehicle.setId(UUID.randomUUID());
            em.persist(vehicle);
        } else {
            em.merge(vehicle);
        }
    }

    public List<Vehicle> findAll() {
        return em.createQuery("SELECT v FROM Vehicle v", Vehicle.class)
                .getResultList();
    }

    public Optional<Vehicle> findById(UUID id) {
        return Optional.ofNullable(em.find(Vehicle.class, id));
    }

    public Optional<Vehicle> findByVin(String vin) {
        if (vin == null) {
            return Optional.empty();
        }

        List<Vehicle> result = em.createQuery(
                        "SELECT v FROM Vehicle v WHERE UPPER(v.vin) = UPPER(:vin)",
                        Vehicle.class)
                .setParameter("vin", vin)
                .getResultList();

        return result.stream().findFirst();
    }

    @Transactional
    public void deleteById(UUID id) {
        Vehicle managed = em.find(Vehicle.class, id);
        if (managed != null) {
            em.remove(managed);
        }
    }

    @Transactional
    public void deleteByVin(String vin) {
        findByVin(vin).ifPresent(v -> {
            Vehicle managed = em.contains(v) ? v : em.merge(v);
            em.remove(managed);
        });
    }
}
