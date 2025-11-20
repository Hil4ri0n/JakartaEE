package com.hil4ri0n.carrental.repository;

import com.hil4ri0n.carrental.model.Rental;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class RentalRepository {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void save(Rental rental) {
        if (rental.getId() == null) {
            rental.setId(UUID.randomUUID());
            em.persist(rental);
        } else {
            em.merge(rental);
        }
    }

    public List<Rental> findAll() {
        return em.createQuery("SELECT r FROM Rental r", Rental.class)
                .getResultList();
    }

    public Optional<Rental> findById(UUID id) {
        return Optional.ofNullable(em.find(Rental.class, id));
    }

    @Transactional
    public void deleteById(UUID id) {
        Rental managed = em.find(Rental.class, id);
        if (managed != null) {
            em.remove(managed);
        }
    }

    @Transactional
    public void update(Rental rental) {
        em.merge(rental);
    }

    public List<Rental> findByVin(String vin) {
        if (vin == null) {
            return List.of();
        }

        return em.createQuery(
                        "SELECT r FROM Rental r " +
                                "WHERE UPPER(r.vehicle.vin) = UPPER(:vin)",
                        Rental.class)
                .setParameter("vin", vin)
                .getResultList();
    }

    public Optional<Rental> findByIdAndVin(UUID id, String vin) {
        if (id == null || vin == null) {
            return Optional.empty();
        }

        List<Rental> result = em.createQuery(
                        "SELECT r FROM Rental r " +
                                "WHERE r.id = :id AND UPPER(r.vehicle.vin) = UPPER(:vin)",
                        Rental.class)
                .setParameter("id", id)
                .setParameter("vin", vin)
                .getResultList();

        return result.stream().findFirst();
    }

    @Transactional
    public int deleteByVehicleVin(String vin) {
        if (vin == null) {
            return 0;
        }

        return em.createQuery(
                        "DELETE FROM Rental r WHERE UPPER(r.vehicle.vin) = UPPER(:vin)")
                .setParameter("vin", vin)
                .executeUpdate();
    }

    public List<Rental> findByVinAndUserLogin(String vin, String login) {
        if (vin == null || login == null) {
            return List.of();
        }

        return em.createQuery(
                        "SELECT r FROM Rental r " +
                                "WHERE UPPER(r.vehicle.vin) = UPPER(:vin) " +
                                "AND UPPER(r.user.login) = UPPER(:login)",
                        Rental.class)
                .setParameter("vin", vin)
                .setParameter("login", login)
                .getResultList();
    }

    public Optional<Rental> findByIdVinAndUserLogin(UUID id, String vin, String login) {
        if (id == null || vin == null || login == null) {
            return Optional.empty();
        }

        List<Rental> result = em.createQuery(
                        "SELECT r FROM Rental r " +
                                "WHERE r.id = :id " +
                                "AND UPPER(r.vehicle.vin) = UPPER(:vin) " +
                                "AND UPPER(r.user.login) = UPPER(:login)",
                        Rental.class)
                .setParameter("id", id)
                .setParameter("vin", vin)
                .setParameter("login", login)
                .getResultList();

        return result.stream().findFirst();
    }

    public List<Rental> findByUserLogin(String login) {
        if (login == null) {
            return List.of();
        }
        return em.createQuery(
                        "SELECT r FROM Rental r " +
                                "WHERE UPPER(r.user.login) = UPPER(:login)",
                        Rental.class)
                .setParameter("login", login)
                .getResultList();
    }
}
