package org.hugo.customers.repositories;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

import org.hugo.customers.domain.Customer;

@ApplicationScoped
public class CustomerRepository implements PanacheRepository<Customer> {

    public boolean existsByEmail(String email) {
        return count("email = ?1", email) > 0;
    }

    public boolean existsByEmailAndIdNot(String email, long id) {
        return count("email = ?1 and id <> ?2", email, id) > 0;
    }

    public List<Customer> listPage(int page, int size) {
        return findAll().page(page, size).list();
    }

    public List<Customer> listActivePage(int page, int size) {
        return find("active = true").page(page, size).list();
    }
}
