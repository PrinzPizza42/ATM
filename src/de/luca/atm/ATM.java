package de.luca.atm;
import java.util.Scanner;

public class ATM {
    public static void main(String[] args) {
        //init Scanner
        Scanner sc = new Scanner(System.in);

        //init Bank
        Bank theBank = new Bank("Bank of Bremen");

        // add the Data of the User
        System.out.println("Adding User...");
        System.out.print("Please enter your first name: ");
        String firstName = sc.nextLine();
        System.out.print("\nPlease enter your last name: ");
        String lastName = sc.nextLine();
        System.out.print("\nPlease create a PIN: ");
        String pin = sc.nextLine();

        // add a user, which also creates a savings account
        User aUser = theBank.addUser(firstName, lastName, pin);

        //add a checking account for our user
        Account newAccount = new Account("Checking", aUser, theBank);
        aUser.addAccount(newAccount);
        theBank.addAccount(newAccount);

        User curUser;
        //noinspection InfiniteLoopStatement
        while (true) {
            //stay in the login prompt until successfull login
            curUser = ATM.mainMenuPrompt(theBank, sc);

            // stay in main menu until user quits
            ATM.printUserMenu(curUser, sc);
        }
    }

    /**
     * Print main menu before a user is logged-in
     * @param theBank   the bank the ATM from
     * @param sc        the Scanner object used for user input
     * @return          the user logged in
     */
    public static User mainMenuPrompt(Bank theBank, Scanner sc) {

        // inits
        String userID;
        String pin;
        User authUser;

        // prompt the user for user ID/pin combo until a correct one is reached
        do {

            System.out.println("\n\nWelcome to Bank of Bremen\n\n");
            System.out.print("Enter user ID: ");
            userID = sc.nextLine();
            System.out.print("\nEnter pin: ");
            pin = sc.nextLine();

            // try to get the user object corresponding to the ID and pin combo
            authUser = theBank.userLogin(userID, pin);
            if (authUser == null) {
                System.out.println("Incorrect user ID/pin combination. Please try again");
            }

        } while (authUser == null); // continue looping until successful login

        return authUser;

    }

    /**
     * Print the user menu to use the services from
     * @param theUser   the logged-in User object
     * @param sc        the Scanner object used for user input
     */
    public static void printUserMenu(User theUser, Scanner sc) {

        // print a summary of the users accounts
        theUser.printAccountsSummary();

        // init
        int choice;

        // user menu
        do {
            System.out.printf("Welcome %s, what would you like to do?\n", theUser.getFirstName());
            System.out.println(" 1) Show account transaction history");
            System.out.println(" 2) Withdraw");
            System.out.println(" 3) Deposit");
            System.out.println(" 4) Transfer");
            System.out.println(" 5) Quit");
            System.out.println();
            System.out.print("Enter your choice: ");
            choice = sc.nextInt();

            if (choice < 1 || choice > 5) {
                System.out.println("Invalid choice. Please choose 1-5");
            }
        }while (choice < 1 || choice >5);

        // process the choice
        switch (choice) {
            case 1:
                ATM.showTransHistory(theUser, sc);
                break;

            case 2:
                ATM.withdrawFunds(theUser, sc);
                break;

            case 3:
                ATM.depositFunds(theUser, sc);
                break;

            case 4:
                ATM.transferFunds(theUser, sc);
                break;
        }

        // redisplay this menu unless the user wants to quit
        if (choice != 5) {
            ATM.printUserMenu(theUser, sc);
        }

    }

    /**
     * Print the transfer history of a account
     * @param theUser   the logged-in User object
     * @param sc        the Scanner object used for user input
     */
    public static void showTransHistory(User theUser, Scanner sc) {

        int theAcct;

        //get account whoe transaction history to look at
        do {
            System.out.printf("Enter the number (1-%d) of the account whose transactions you want to see: ", theUser.numAccounts());
            theAcct = sc.nextInt()-1;
            if (theAcct < 0 || theAcct >= theUser.numAccounts()) {
                System.out.printf("Could not find the account, please enter a number between 1 and %d", theUser.numAccounts());
            }
        } while (theAcct < 0 || theAcct >= theUser.numAccounts());

        // print the transaction history
        theUser.printAcctTransHistory(theAcct);
    }

    /**
     * Process transferring funds from one account to another
     * @param theUser   the logging-in User object
     * @param sc        the Scanner object used for user input
     */
    public static void transferFunds(User theUser, Scanner sc) {

        //inits
        int fromAcct;
        int toAcct;
        double amount;
        double acctBal;

        // get the account to transfer from
        do {
            System.out.printf("Enter the number (1-%d) of the account to transfer from: ", theUser.numAccounts());
            fromAcct = sc.nextInt()-1;
            if (fromAcct < 0 || fromAcct >= theUser.numAccounts()) {
                System.out.printf("Could not find the account, please enter a number between 1 and %d\n", theUser.numAccounts());
            }
        } while (fromAcct < 0 || fromAcct >= theUser.numAccounts());
        acctBal = theUser.getAcctBal(fromAcct);

        // get the account to transfer to
        do {
            System.out.printf("Enter the number (1-%d) of the account to transfer to: ", theUser.numAccounts());
            toAcct = sc.nextInt()-1;
            if (toAcct < 0 || toAcct >= theUser.numAccounts()) {
                System.out.printf("Could not find the account, please enter a number between 1 and %d\n", theUser.numAccounts());
            }
        } while (toAcct < 0 || toAcct >= theUser.numAccounts());

        // get the amount to transfer
        do {
            System.out.printf("Enter the amount to transfer (max €%.02f): €", acctBal);
            amount = sc.nextDouble();
            if (amount < 0) {
                System.out.println("Amount must be greater than 0.");
            } else if (amount > acctBal) {
                System.out.printf("Amount cant be greater than the balance of €%.02f.\n", acctBal);
            }
        } while (amount < 0 || amount > acctBal);

        // finally, do the transfer
        theUser.addAccountTransaction(fromAcct, -1*amount, String.format("Transfer to account %s", theUser.getAccountUUID(toAcct)));
        theUser.addAccountTransaction(toAcct, amount, String.format("Transfer from account %s", theUser.getAccountUUID(fromAcct)));
    }

    /**
     * Process a fund withdraw from an account
     * @param theUser   the logged-in User object
     * @param sc        the Scanner object to use for user input
     */
    public static void withdrawFunds(User theUser, Scanner sc) {

        //inits
        int fromAcct;
        double amount;
        double acctBal;
        String memo;

        // get the account to transfer from
        do {
            System.out.printf("Enter the number (1-%d) of the account to withdraw from: ", theUser.numAccounts());
            fromAcct = sc.nextInt()-1;
            if (fromAcct < 0 || fromAcct >= theUser.numAccounts()) {
                System.out.printf("Could not find the account, please enter a number between 1 and %d\n", theUser.numAccounts());
            }
        } while (fromAcct < 0 || fromAcct >= theUser.numAccounts());
        acctBal = theUser.getAcctBal(fromAcct);

        // get the amount to transfer
        do {
            System.out.printf("Enter the amount to transfer (max €%.02f): €", acctBal);
            amount = sc.nextDouble();
            if (amount < 0) {
                System.out.println("Amount must be greater than 0.");
            } else if (amount > acctBal) {
                System.out.printf("Amount cant be greater than the balance of €%.02f.\n", acctBal);
            }
        } while (amount < 0 || amount > acctBal);

        // goble up rest of the previous input
        sc.nextLine();

        // get a memo
        System.out.print("Enter a memo: ");
        memo = sc.nextLine();

        // do the withdraw
        theUser.addAccountTransaction(fromAcct, -1*amount, memo);
    }

    /**
     * Process a fund deposit to an account
     * @param theUser   the logged-in User object
     * @param sc        the Scanner object used for user input
     */
    public static void depositFunds(User theUser, Scanner sc) {
        //inits
        int toAcct;
        double amount;
        double acctBal;
        String memo;

        // get the account to deposit to
        do {
            System.out.printf("Enter the number (1-%d) of the account to deposit to: ", theUser.numAccounts());
            toAcct = sc.nextInt()-1;
            if (toAcct < 0 || toAcct >= theUser.numAccounts()) {
                System.out.printf("Could not find the account, please enter a number between 1 and %d\n", theUser.numAccounts());
            }
        } while (toAcct < 0 || toAcct >= theUser.numAccounts());
        acctBal = theUser.getAcctBal(toAcct);

        // get the amount to transfer
        do {
            System.out.printf("Enter the amount to transfer (Balance right now: €%.02f): €", acctBal);
            amount = sc.nextDouble();
            if (amount < 0) {
                System.out.println("Amount must be greater than 0.");
            }
        } while (amount < 0);

        // goble up rest of the previous input
        sc.nextLine();

        // get a memo
        System.out.print("Enter a memo: ");
        memo = sc.nextLine();

        // do the deposit
        theUser.addAccountTransaction(toAcct, amount, memo);
    }
}
