package de.luca.atm;

import java.util.ArrayList;
import java.util.Random;

public class Bank {

    private String name;

    private ArrayList<User> users;

    private ArrayList<Account> accounts;

    /**
     * Create a new bank object with emty lists of users and accounts
     * @param name  the name of the bank
     */
    public Bank(String name) {
        this.name = name;
        this.users = new ArrayList<User>();
        this.accounts = new ArrayList<Account>();
    }

    /**
     * Generate the UUID of a new user
     * @return  the UUID of the user
     */
    public String getNewUserUUID() {

        String uuid;
        Random rng = new Random();
        int len = 6;
        boolean nonUnique;

        do {

            uuid = "";
            for (int counter = 0; counter < len; counter++) {
                uuid += ((Integer) rng.nextInt(10)).toString();
            }

            nonUnique = false;
            for (User u : this.users) {
                if (uuid.compareTo(u.getUUID()) == 0) {
                    nonUnique = true;
                    break;
                }
            }

        } while (nonUnique);

        return uuid;

    }

    /**
     * Generate the UUID of the new account
     * @return  the UUID of the account
     */
    public String getNewAccountUUID() {
        String uuid;
        Random rng = new Random();
        int len = 10;
        boolean nonUnique;

        do {

            uuid = "";
            for (int counter = 0; counter < len; counter++) {
                uuid += ((Integer) rng.nextInt(10)).toString();
            }

            nonUnique = false;
            for (Account a : this.accounts) {
                if (uuid.compareTo(a.getUUID()) == 0) {
                    nonUnique = true;
                    break;
                }
            }

        } while (nonUnique);

        return uuid;

    }

    /**
     * Add an account to the list of the bank
     * @param anAcct    the name of the account to add
     */
    public void addAccount(Account anAcct) {
        this.accounts.add(anAcct);
    }

    /**
     * Add a user to the list users of the bank
     * @param firstName the first name of the user
     * @param lastName  the last name of the user
     * @param pin       the pin of the user
     * @return          newUser object
     */
    public User addUser(String firstName, String lastName, String pin) {
        User newUser = new User(firstName, lastName, pin, this);
        this.users.add(newUser);

        Account newAccount = new Account("Savings", newUser, this);

        newUser.addAccount(newAccount);
        this.addAccount(newAccount);

        return newUser;
    }

    /**
     * Log-in a user if given the right userID and pin
     * @param userID    ID of the user to log-in
     * @param pin       pin of the user to log-in
     * @return          null if wrong combination / User object u if right combination
     */
    public User userLogin(String userID, String pin) {
        //search through list of users
        for (User u : this.users) {

            //check if user ID is correct
            if (u.getUUID().compareTo(userID) == 0 && u.validatePin(pin)) {
                return u;
            }
        }
        //if we havent found the user or have an incorrect pin
        return null;
    }

    /**
     * get the name of the bank
     * @return
     */
    public String getName() {
        return this.name;
    }
}