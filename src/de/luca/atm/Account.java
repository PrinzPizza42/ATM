package de.luca.atm;
import java.util.ArrayList;

public class Account {

    private String name;

    private String uuid;

    private User holder;

    private ArrayList<Transaction> transactions;

    public Account(String name, User holder, Bank theBank) {
        this.name = name;
        this.holder = holder;

        this.uuid = theBank.getNewAccountUUID();

        this.transactions = new ArrayList<Transaction>();


    }

    /**
     * Get the UUID of the account
     * @return  the UUID of the account
     */
    public String getUUID() {
        return this.uuid;
    }

    /**
     * Get summary line for the account
     * @return  the string summary
     */
    public String getSummaryLine() {

        // get the accounts balance
        double balance = this.getBalance();

        // format the summary line, depending on wether teh balance is negativ
        if (balance >= 0) {
            return String.format("%s : €%.02f : %s", this.uuid, balance, this.name);
        } else {
            return String.format("%s : €(%.02f) : %s", this.uuid, balance, this.name);
        }
    }

    /**
     * get the balance of the account
     * @return  the balance of the account
     */
    public double getBalance() {

        double balance = 0;
        for (Transaction t : this.transactions) {
            balance += t.getAmount();
        }
        return balance;
    }


    /**
     * Print the transaction history of the account
     */
    public void printTransHistory() {

        System.out.printf("\nTransaction history for account %s\n", this.uuid);
        for (int t = (this.transactions.size()-1); t >= 0; t--) {
            System.out.println(transactions.get(t).getSummaryLine());
        }
        System.out.println();
    }

    /**
     * Add a new transaction in this account
     * @param amount    the amount transacted
     * @param memo      the transaction memo
     */
    public void addTransaction(Double amount, String memo) {

        // create new transaction object and add it to our list
        Transaction newTrans = new Transaction(amount, memo, this);
        this.transactions.add(newTrans);
    }
}
