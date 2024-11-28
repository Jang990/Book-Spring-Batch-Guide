package io.spring.batch.hello_world.chapter7._9;

import io.spring.batch.hello_world.chapter7.domain.Chapter7_JPA_Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Chapter7_CustomerRepository extends JpaRepository<Chapter7_JPA_Customer, Long> {
    Page<Chapter7_JPA_Customer> findByCity(String city, Pageable pageable);
}
