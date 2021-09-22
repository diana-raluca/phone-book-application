package com.github.dianaraluca.phonebook.repository;

import com.github.dianaraluca.phonebook.model.Contact;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ContactRepository extends CrudRepository<Contact, Long> {

        List<Contact> findByLastName(String lastName);

        Contact findById(long id);

        List<Contact> findAll();

        Contact findByPhoneNumber(String phoneNumber);

        Contact findByFirstName(String firstName);

        List<Contact> findByFirstNameContainingIgnoringCaseOrLastNameContainingIgnoringCase(String firstName, String lastName);

        List<Contact> findByFirstNameContainingIgnoringCaseAndLastNameContainingIgnoringCase(String firstName, String lastName);

}
