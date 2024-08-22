package de.luca.atm;

import java.sql.Timestamp;
import java.util.Date;

public class Transaction {

    private double amount;
    private Date timestamp;
    private String memo;
    private Account inAccount;

    public Transaction(double amount, Account inAccount) {

        this.amount = amount;
        this.inAccount = inAccount;
        this.timestamp = new Date();
        this.memo = "";

    }

    public Transaction(double amount, String memo, Account inAccount) {

        this(amount, inAccount);

        this.memo = memo;

    }

    /**
     * get the amount of the transaction
     * @return  the amount
     */
    public double getAmount() {
        return this.amount;
    }

    /**
     * get a string summarizing the transaction
     * @return  the summary string
     */
    public String getSummaryLine() {
        if (this.amount >= 0) {
            return String.format("%s : €%.02f : %s", this.timestamp.toString(), this.amount, this.memo);
        } else {
            return String.format("%s : €(%.02f) : %s", this.timestamp.toString(), this.amount, this.memo);
        }
    }
}
