import java.util.ArrayList;
import java.util.Scanner;

class User {
    String username;
    String password;
    Account account;

    User(String username, String password, Account account) {
        this.username = username;
        this.password = password;
        this.account = account;
    }
}

class Account {
    double balance;
    ArrayList<String> transactionHistory;

    Account(double initialBalance) {
        this.balance = initialBalance;
        this.transactionHistory = new ArrayList<>();
    }

    void deposit(double amount) {
        balance += amount;
        transactionHistory.add("Deposit: $" + amount + ", New Balance: $" + balance);
    }

    boolean withdraw(double amount) {
        if (amount <= balance) {
            balance -= amount;
            transactionHistory.add("Withdraw: $" + amount + ", New Balance: $" + balance);
            return true;
        } else {
            return false;
        }
    }

    void transfer(Account recipient, double amount) {
        if (withdraw(amount)) {
            recipient.deposit(amount);
            transactionHistory.add("Transfer: $" + amount + " to " + recipient + ", New Balance: $" + balance);
        }
    }

    @Override
    public String toString() {
        return "Account{" +
                "balance=" + balance +
                '}';
    }
}

public class BankingSystem {
    static ArrayList<User> users = new ArrayList<>();
    static User currentUser = null;
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    login();
                    break;
                case 2:
                    register();
                    break;
                case 3:
                    System.out.println("Thank you for using the online banking system.");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }

            if (currentUser != null) {
                userMenu();
            }
        }
    }

    static void login() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        for (User user : users) {
            if (user.username.equals(username) && user.password.equals(password)) {
                currentUser = user;
                System.out.println("Welcome, " + currentUser.username + "!");
                return;
            }
        }

        System.out.println("Invalid username or password. Please try again.");
    }

    static void register() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        System.out.print("Enter initial deposit: ");
        double initialDeposit = scanner.nextDouble();
        scanner.nextLine();

        Account account = new Account(initialDeposit);
        User user = new User(username, password, account);
        users.add(user);

        System.out.println("Registration successful for " + username + ". You can now login.");
    }

    static void userMenu() {
        while (true) {
            System.out.println("1. Check Balance");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Transfer Funds");
            System.out.println("5. View Transaction History");
            System.out.println("6. Logout");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    checkBalance();
                    break;
                case 2:
                    deposit();
                    break;
                case 3:
                    withdraw();
                    break;
                case 4:
                    transferFunds();
                    break;
                case 5:
                    viewTransactionHistory();
                    break;
                case 6:
                    currentUser = null;
                    System.out.println("You have logged out.");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    static void checkBalance() {
        System.out.println("Current Balance: $" + currentUser.account.balance);
    }

    static void deposit() {
        System.out.print("Enter amount to deposit: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();

        currentUser.account.deposit(amount);
        System.out.println("Deposit of $" + amount + " successful. Current Balance: $" + currentUser.account.balance);
    }

    static void withdraw() {
        System.out.print("Enter amount to withdraw: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();

        if (currentUser.account.withdraw(amount)) {
            System.out.println("Withdrawal of $" + amount + " successful. Current Balance: $" + currentUser.account.balance);
        } else {
            System.out.println("Insufficient balance.");
        }
    }

    static void transferFunds() {
        System.out.print("Enter recipient username: ");
        String recipientUsername = scanner.nextLine();
        System.out.print("Enter amount to transfer: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();

        User recipient = null;
        for (User user : users) {
            if (user.username.equals(recipientUsername)) {
                recipient = user;
                break;
            }
        }

        if (recipient == null) {
            System.out.println("Recipient " + recipientUsername + " not found.");
            return;
        }

        currentUser.account.transfer(recipient.account, amount);
        System.out.println("Transfer of $" + amount + " to " + recipientUsername + " successful. Current Balance: $" + currentUser.account.balance);
    }

    static void viewTransactionHistory() {
        if (currentUser.account.transactionHistory.isEmpty()) {
            System.out.println("No transactions found.");
        } else {
            System.out.println("\nTransaction History:");
            for (String transaction : currentUser.account.transactionHistory) {
                System.out.println(transaction);
            }
        }
    }
}