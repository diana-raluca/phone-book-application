package com.github.dianaraluca.phonebook.service;

import com.github.dianaraluca.phonebook.model.Contact;
import com.github.dianaraluca.phonebook.repository.ContactRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContactService {

    private final ContactRepository contactRepository;

    public ContactService(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    public Contact saveContact(Contact contact){

        Contact savedContact = contactRepository.save(contact);

        return savedContact;
    }

    public long getContactsCount() {

        return contactRepository.count();
    }

    public List<Contact> getAllContacts() {
        List<Contact> contactList =  contactRepository.findAll();

        return contactList;
    }

    public Contact getContactByPhoneNumber(String phoneNumber) {

        Contact contactByPhoneNumber = contactRepository.findByPhoneNumber(phoneNumber);

        return contactByPhoneNumber;
    }

    public Contact getContactByFirstName(String firstName) {
        Contact contactByFirstName = contactRepository.findByFirstName(firstName);

        return contactByFirstName;
    }

    public List<Contact> searchByName(String firstName) {

        return contactRepository.findByFirstNameContainingIgnoringCaseOrLastNameContainingIgnoringCase(firstName, firstName);
    }

    public List<Contact> searchByFirstNameAndLastName(String firstName, String lastName) {

        return contactRepository.findByFirstNameContainingIgnoringCaseAndLastNameContainingIgnoringCase(firstName, lastName);
    }


}
