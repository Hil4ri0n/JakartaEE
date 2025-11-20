package com.hil4ri0n.carrental.repository;

import com.hil4ri0n.carrental.model.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class UserRepository {

    @PersistenceContext
    private EntityManager em;

    public List<User> findAll() {
        return em.createQuery("SELECT u FROM User u", User.class)
                .getResultList();
    }

    public Optional<User> findById(UUID id) {
        return Optional.ofNullable(em.find(User.class, id));
    }

    public Optional<User> findByLogin(String login) {
        if (login == null) {
            return Optional.empty();
        }
        var result = em.createQuery(
                        "SELECT u FROM User u WHERE UPPER(u.login) = UPPER(:login)",
                        User.class)
                .setParameter("login", login)
                .getResultList();
        return result.stream().findFirst();
    }

    public Optional<User> findByEmail(String email) {
        if (email == null) {
            return Optional.empty();
        }
        var result = em.createQuery(
                        "SELECT u FROM User u WHERE UPPER(u.email) = UPPER(:email)",
                        User.class)
                .setParameter("email", email)
                .getResultList();
        return result.stream().findFirst();
    }

    @Transactional
    public void save(User user) {
        if (user.getId() == null) {
            user.setId(UUID.randomUUID());
            if (user.getJoinedAt() == null) {
                user.setJoinedAt(LocalDate.now());
            }
            em.persist(user);
        } else {
            em.merge(user);
        }
    }
}
