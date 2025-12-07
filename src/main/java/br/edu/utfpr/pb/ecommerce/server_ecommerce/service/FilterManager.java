package br.edu.utfpr.pb.ecommerce.server_ecommerce.service;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FilterManager {

    private final EntityManager entityManager;

    public void enableActiveFilter(boolean isActive) {
        Session session = entityManager.unwrap(Session.class);
        session.enableFilter("activeFilter").setParameter("isActive", isActive);
    }

    public void disableActiveFilter() {
        Session session = entityManager.unwrap(Session.class);
        session.disableFilter("activeFilter");
    }
}
