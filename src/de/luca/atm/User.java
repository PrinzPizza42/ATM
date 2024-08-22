package de.luca.atm;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.security.MessageDigest;

public class User {

    private String firstName;

    private String lastName;

    private String uuid;

    private byte pinHash[];

    private ArrayList<Account> accounts;

    public User(String firstName, String lastName, String pin, Bank theBank) {
        this.firstName = firstName;
        this.lastName = lastName;

        // strore the pins MD5 hash, rather than the original value

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            this.pinHash = md.digest(pin.getBytes());
        } catch (NoSuchAlgorithmException e) {
            System.err.println("error, caught NoSuchAlgorithmException");
            e.printStackTrace();
            System.exit(1);
        }

        this.uuid = theBank.getNewUserUUID();

        this.accounts = new ArrayList<>();

        System.out.printf("\nNew user %s, %s with ID %s created. %n", lastName, firstName, this.uuid);
        System.out.println("Please remember your ID, you will need it to log into this ATM later.");
    }

    /**
     * Ad an account to the user
     * @param anAcct    name of the account to add
     */
    public void addAccount(Account anAcct) {
        this.accounts.add(anAcct);
    }

    /**
     * Get the UUID of the user
     * @return  the UUID of the user
     */
    public String getUUID() {
        return this.uuid;
    }

    /**
     * Check wether a given pin matches the true User pin
     * @param aPin  the oin to check
     * @return      wether the pin is valid or not
     */
    public boolean validatePin(String aPin) {

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            return MessageDigest.isEqual(md.digest(aPin.getBytes()), this.pinHash);
        } catch (NoSuchAlgorithmException e) {
            System.err.println("error, caught NoSuchAlgorithmException");
            e.printStackTrace();
            System.exit(1);
        }
        return false;
    }

    /**
     * return users first name
     * @return
     */
    public String getFirstName() {
        return this.firstName;
    }

    /**
     * Prints a summary of the accounts of the user
     */
    public void printAccountsSummary() {

        System.out.printf("\n%s's accounts summary\n", this.firstName);
        for(int a = 0; a < this.accounts.size(); a++) {
            System.out.printf("  %d) %s\n", a+1, this.accounts.get(a).getSummaryLine());
        }
        System.out.println();

    }

    /**
     * counts the number of accounts the user has
     * @return  number of accounts
     */
    public int numAccounts(){

        return this.accounts.size();
    }

    /**
     * Print transaction history for a particular account.
     * @param acctIdx   the index of the account to use
     */
    public void printAcctTransHistory(int acctIdx) {
        this.accounts.get(acctIdx).printTransHistory();
    }

    /**
     * get the balance of the account as a double
     * @param acctIdx   the index of the account
     * @return          a double of the balance
     */
    public double getAcctBal(int acctIdx) {
        return this.accounts.get(acctIdx).getBalance();
    }

    /**
     * Get the UUID of a particular account
     * @param acctIdx   the index of the account to use
     * @return          the UUID of the account
     */
    public String getAccountUUID(int acctIdx) {
        return this.accounts.get(acctIdx).getUUID();
    }

    /**
     * Add a transaction to a specific account of the user
     * @param acctIdx   the Index of the account
     * @param amount    the amount of the transaction
     * @param memo      the memo of the transaction
     */
    public void addAccountTransaction(int acctIdx, double amount, String memo) {
        this.accounts.get(acctIdx).addTransaction(amount, memo);
    }
}
