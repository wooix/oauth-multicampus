package com.multi.oauth20server.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import com.multi.oauth20server.domain.Contact;

public interface ContactRepository extends JpaRepository<Contact, Long>{
}
