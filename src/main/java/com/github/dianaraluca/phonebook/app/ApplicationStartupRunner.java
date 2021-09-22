package com.github.dianaraluca.phonebook.app;

import com.github.dianaraluca.phonebook.PhoneBookApplication;
import com.github.dianaraluca.phonebook.model.Contact;
import com.github.dianaraluca.phonebook.service.ContactService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class ApplicationStartupRunner implements CommandLineRunner {

    private static final Logger LOG = LoggerFactory.getLogger(PhoneBookApplication.class);

    private final ContactService contactService;

    public ApplicationStartupRunner(ContactService contactService) {
        this.contactService = contactService;
    }

    @Override
    public void run(String... args) {
        LOG.info("EXECUTING : command line runner");

        boolean shouldExit = false;

        while (!shouldExit) {
            Scanner in = new Scanner(System.in);
            printMenu();

            int choice = getChoice(in);

            switch (choice) {
                case 0:
                    shouldExit = true;
                    break;
                case 1:
                    addOrEditContact();
                    break;
                case 2:
                    viewAllContacts();
                    break;
                case 3:
                    findContactByPhoneNumber();
                    break;
                case 4:
                    findContactByName();
                    break;
                default:
                    System.out.println("Invalid choice");
                    break;
            }

            if(!shouldExit){
                System.out.println("Press \"ENTER\" to continue...");
                String test = in.nextLine();
            }

        }

    }

    private int getChoice(Scanner scanner) {
        int choice = 0;
        boolean isValidChoice = false;

        while (!isValidChoice) {
            try {
                String choiceString = scanner.nextLine();
                choice = Integer.parseInt(choiceString);

                isValidChoice = true;
            } catch (NumberFormatException numberFormatException) {
                System.out.println("Invalid input type (must be an integer)");
            }
        }

        return choice;
    }

    private void printMenu() {
        System.out.println("Phone Book");
        System.out.println("There are currently " + contactService.getContactsCount() + " contacts in the phone book.");
        System.out.println("1. Add or edit a contact.");
        System.out.println("2. View all contacts.");
        System.out.println("3. Find a contact by phone number.");
        System.out.println("4. Find a contact by name.");
        System.out.println("0. Exit");

        System.out.println("Select an option: ");
    }

    private void addOrEditContact() {
        Scanner input = new Scanner(System.in);

        System.out.println("Add or edit a contact.");
        System.out.println("Enter a phone number: ");

        String phoneNumber = getValidatedPhoneNumber(input);

        Contact contact = contactService.getContactByPhoneNumber(phoneNumber);
        boolean contactAlreadyExists = contact != null;

        if (contactAlreadyExists) {
            System.out.println("This phone number already exists. Editing an existing entry.");
        } else {
            contact = new Contact();
            System.out.println("This phone number is new. Adding a new entry to the phone book.");
        }

        System.out.println("First Name: ");
        String firstName = getValidatedName(input);
        System.out.println("Last Name: ");
        String lastName = getValidatedName(input);
        System.out.println("Email (optional): ");
        String email = input.nextLine();
        System.out.println("Address (optional): ");
        String address = input.nextLine();

        System.out.println("Phone book was updated successfully.");

        contact.setAddress(address);
        contact.setEmail(email);
        contact.setFirstName(firstName);
        contact.setLastName(lastName);
        contact.setPhoneNumber(phoneNumber);

        contactService.saveContact(contact);
    }

    private String getValidatedName(Scanner input) {
        String name = input.nextLine();

        boolean isNameEmpty = name.isEmpty();

        while (isNameEmpty) {
            System.out.println("The entered name is invalid");
            System.out.println("Enter a name: ");

            name = input.nextLine();
            isNameEmpty = name.isEmpty();
        }

        return name;
    }




    private String getValidatedPhoneNumber(Scanner input) {
        String phoneNumber = input.nextLine();
        boolean isValidPhone = validatePhoneNumber(phoneNumber);

        while (!isValidPhone) {
            System.out.println("The Phone Number is invalid");
            System.out.println("Enter a phone number: ");

            phoneNumber = input.nextLine();
            isValidPhone = validatePhoneNumber(phoneNumber);
        }
        return phoneNumber;
    }

    private static boolean validatePhoneNumber(String phoneNumber) {
        // regex pattern source https://www.baeldung.com/java-regex-validate-phone-numbers
        String patterns
                = "^(\\+\\d{1,3}( )?)?((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$"
                + "|^(\\+\\d{1,3}( )?)?(\\d{3}[ ]?){2}\\d{3}$"
                + "|^(\\+\\d{1,3}( )?)?(\\d{3}[ ]?)(\\d{2}[ ]?){2}\\d{2}$";

        String strippedPhoneNumber = phoneNumber.replaceAll("\\s+", "");

        Pattern pattern = Pattern.compile(patterns);
        Matcher matcher = pattern.matcher(strippedPhoneNumber);

        boolean isValidPhone = false;
        if (matcher.matches()) {
            isValidPhone = true;
            System.out.println(phoneNumber + " = Valid Phone Number");
        }
        return isValidPhone;
    }

    private void viewAllContacts() {
        System.out.println("Contact list.\n");

        List<Contact> contactList = contactService.getAllContacts();

//        for (int i = 0; i < contactList.size(); i++) {
//            Contact contact = contactList.get(i);
//
//            System.out.println(contact.getPhoneNumber() + " " + contact.getFirstName() + " " + contact.getLastName() + " " + contact.getEmail());
//        }

        for (Contact contact : contactList) {
            String leftAlignFormat = "%-20.20s %-15.20s %-15.20s %-20s %n";
            System.out.format(leftAlignFormat, contact.getPhoneNumber(), contact.getFirstName(), contact.getLastName(), contact.getEmail());
        }

    }

    private void findContactByPhoneNumber() {
        Scanner input = new Scanner(System.in);

        System.out.println("Find a contact by phone number:");

        String phoneNumber = input.nextLine();
        boolean isValidPhone = validatePhoneNumber(phoneNumber);

        while (!isValidPhone) {
            System.out.println("The inserted text is: " + phoneNumber);
            System.out.println("The Phone Number is invalid");
            System.out.println("Enter a phone number: ");

            phoneNumber = input.nextLine();
            isValidPhone = validatePhoneNumber(phoneNumber);
        }

        Contact contact = contactService.getContactByPhoneNumber(phoneNumber);

        if (contact == null) {
            System.out.println("The phone number could not be found in the address book.");
        } else {
            String leftAlignFormat = "%-20.20s %-20s %n";

            System.out.format(leftAlignFormat, "Phone number:", contact.getPhoneNumber());
            System.out.format(leftAlignFormat, "First Name:", contact.getFirstName());
            System.out.format(leftAlignFormat, "Last Name:", contact.getLastName());
            System.out.format(leftAlignFormat, "E-mail:", contact.getEmail());
            System.out.format(leftAlignFormat, "Address:", contact.getAddress());

        }

    }

    private void findContactByName() {
        System.out.println("Search by name");
        Scanner input = new Scanner(System.in);

        System.out.println("Enter a name (first name, last name or both):");

        String name = input.nextLine();
        String[] names = name.split("\\s+");

        List<Contact> contactList = null;

        if(names.length == 1){
            contactList = contactService.searchByName(names[0]);
        } else if(names.length == 2){
            contactList = contactService.searchByFirstNameAndLastName(names[0], names[1]);

            if(contactList.isEmpty()){
                contactList = contactService.searchByFirstNameAndLastName(names[1], names[0]);
            }
        }

        if (contactList != null && !contactList.isEmpty()) {
            for (Contact contact : contactList) {
                String leftAlignFormat = "%-20.20s %-15.20s %-15.20s %-20s %n";
                System.out.format(leftAlignFormat, contact.getPhoneNumber(), contact.getFirstName(), contact.getLastName(), contact.getEmail());
            }

        } else {
            System.out.println("The name could not be found in the address book.");
        }

    }




}