package com.pluralsight;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;
public class FinancialTracker {

    // Lists store transactions
    private static ArrayList<Transaction> transactions = new ArrayList<Transaction>();

    // csv file to store data
    private static final String FILE_NAME = "transactions.csv";
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String TIME_FORMAT = "HH:mm:ss";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT);
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern(TIME_FORMAT);

    public static void main(String[] args) {
        //to load existing transactions from the csv file
        loadTransactions(FILE_NAME);
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        //main program loop
        while (running) {
            System.out.println("Welcome to TransactionApp");
            System.out.println("Choose an option:");
            System.out.println("D) Add Deposit");
            System.out.println("P) Make Payment (Debit)");
            System.out.println("L) Ledger");
            System.out.println("X) Exit");

            String input = scanner.nextLine().trim();

            //handle user input to select a feature
            switch (input.toUpperCase()) {
                case "D":
                    addDeposit(scanner);
                    break;
                case "P":
                    addPayment(scanner);
                    break;
                case "L":
                    ledgerMenu(scanner); //show ledger menu
                    break;
                case "X":
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option");
                    break;
            }
        }

        scanner.close();
    }

    // load existing transactions from the csv file
    public static void loadTransactions(String fileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] details = line.split("\\|");
                LocalDate date = LocalDate.parse(details[0].trim(), DATE_FORMATTER);
                LocalTime time = LocalTime.parse(details[1].trim(), TIME_FORMATTER);
                String description = details[2].trim();
                String vendor = details[3].trim();
                double amount = Double.parseDouble(details[4].trim());
                transactions.add(new Transaction(date, time, description, vendor, amount)); // add to the list
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found, starting fresh.");
        } catch (IOException e) {
            System.out.println("Error reading the file.");
        }
    }

    //save all transactions to the csv file
    private static void saveTransactions() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Transaction transaction : transactions) {
                bw.write(transaction.toString()); //writing each transaction to the file
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error writing to the file.");
        }
    }
        // This method should load transactions from a file with the given file name.
        // If the file does not exist, it should be created.
        // The transactions should be stored in the `transactions` ArrayList.
        // Each line of the file represents a single transaction in the following format:
        // <date>|<time>|<description>|<vendor>|<amount>
        // For example: 2023-04-15|10:13:25|ergonomic keyboard|Amazon|-89.50
        // After reading all the transactions, the file should be closed.
        // If any errors occur, an appropriate error message should be displayed.


    private static void addDeposit(Scanner scanner) {
        System.out.println("Enter date (yyyy-MM-dd): ");
        LocalDate date = LocalDate.parse(scanner.nextLine().trim(), DATE_FORMATTER);
        System.out.println("Enter time (HH:mm:ss): ");
        LocalTime time = LocalTime.parse(scanner.nextLine().trim(), TIME_FORMATTER);
        System.out.println("Enter description: ");
        String description = scanner.nextLine().trim();
        System.out.println("Enter vendor: ");
        String vendor = scanner.nextLine().trim();
        System.out.println("Enter amount (positive number): ");
        double amount = Double.parseDouble(scanner.nextLine().trim());

        Transaction deposit = new Transaction(date, time, description, vendor, amount);
        transactions.add(deposit);

        // Save the transaction to the CSV file
        saveTransactionToFile(deposit);

        System.out.println("Deposit added successfully.");

        // This method should prompt the user to enter the date, time, description, vendor, and amount of a deposit.
        // The user should enter the date and time in the following format: yyyy-MM-dd HH:mm:ss
        // The amount should be a positive number.
        // After validating the input, a new `Transaction` object should be created with the entered values.
        // The new deposit should be added to the `transactions` ArrayList.
    }

    private static void addPayment(Scanner scanner) {
        System.out.println("Enter date (yyyy-MM-dd): ");
        LocalDate date = LocalDate.parse(scanner.nextLine().trim(), DATE_FORMATTER);
        System.out.println("Enter time (HH:mm:ss): ");
        LocalTime time = LocalTime.parse(scanner.nextLine().trim(), TIME_FORMATTER);
        System.out.println("Enter description: ");
        String description = scanner.nextLine().trim();
        System.out.println("Enter vendor: ");
        String vendor = scanner.nextLine().trim();
        System.out.println("Enter amount (positive number): ");
        double amount = Double.parseDouble(scanner.nextLine().trim()) * -1;  // Make it a negative value for payment

        Transaction payment = new Transaction(date, time, description, vendor, amount);
        transactions.add(payment);

        // Save the transaction to the CSV file,
        saveTransactionToFile(payment);

        System.out.println("Payment added successfully.");

        // This method should prompt the user to enter the date, time, description, vendor, and amount of a payment.
        // The user should enter the date and time in the following format: yyyy-MM-dd HH:mm:ss
        // The amount received should be a positive number than transformed to a negative number.
        // After validating the input, a new `Transaction` object should be created with the entered values.
        // The new payment should be added to the `transactions` ArrayList.
    }

    // saving individual transactions to csv file (from addDeposit and addPayment)
    private static void saveTransactionToFile(Transaction transaction) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            bw.write(transaction.toString()); //appending new transaction to file
            bw.newLine();
        } catch (IOException e) {
            System.out.println("Error writing to the file: " + e.getMessage());
        }
    }

    private static void ledgerMenu(Scanner scanner) {
        boolean running = true;
        while (running) {
            System.out.println("Ledger");
            System.out.println("Choose an option:");
            System.out.println("A) A`ll");
            System.out.println("D) Deposits");
            System.out.println("P) Payments");
            System.out.println("R) Reports");
            System.out.println("H) Home");

            String input = scanner.nextLine().trim();

            switch (input.toUpperCase()) {
                case "A":
                    displayLedger();
                    break;
                case "D":
                    displayDeposits();
                    break;
                case "P":
                    displayPayments();
                    break;
                case "R":
                    reportsMenu(scanner);
                    break;
                case "H":
                    running = false;
                default:
                    System.out.println("Invalid option");
                    break;
            }
        }
    }

    private static void displayLedger() {
        System.out.println("Date | Time | Description | Vendor | Amount");
        for (Transaction transaction : transactions) {
            System.out.println(transaction);
        }
        // This method should display a table of all transactions in the `transactions` ArrayList.
        // The table should have columns for date, time, description, vendor, and amount.
    }

    private static void displayDeposits() {
        System.out.println("Date | Time | Description | Vendor | Amount");
        for (Transaction transaction : transactions) {
            if (transaction.getAmount() > 0) {
                System.out.println(transaction);
            }
        }
        // This method should display a table of all deposits in the `transactions` ArrayList.
        // The table should have columns for date, time, description, vendor, and amount.
    }

    private static void displayPayments() {
        System.out.println("Date | Time | Description | Vendor | Amount");
        for (Transaction transaction : transactions) {
            if (transaction.getAmount() < 0) {
                System.out.println(transaction);
            }
        }
        // This method should display a table of all payments in the `transactions` ArrayList.
        // The table should have columns for date, time, description, vendor, and amount.
    }

    private static void reportsMenu(Scanner scanner) {
        boolean running = true;
        while (running) {
            System.out.println("Reports");
            System.out.println("Choose an option:");
            System.out.println("1) Month To Date");
            System.out.println("2) Previous Month");
            System.out.println("3) Year To Date");
            System.out.println("4) Previous Year");
            System.out.println("5) Search by Vendor");
            System.out.println("6) Custom Search");
            System.out.println("0) Back");

            String input = scanner.nextLine().trim();

            switch (input) {
                case "1":
                    filterByMonth(LocalDate.now());
                    break;
                    // Generate a report for all transactions within the current month,
                    // including the date, time, description, vendor, and amount for each transaction.
                case "2":
                    filterByPreviousMonth();
                    break;
                    // Generate a report for all transactions within the previous month,
                    // including the date, time, description, vendor, and amount for each transaction.
                case "3":
                    filterByYear(LocalDate.now().getYear());
                    break;
                    // Generate a report for all transactions within the current year,
                    // including the date, time, description, vendor, and amount for each transaction.
                case "4":
                    filterByYear(LocalDate.now().getYear() - 1);
                    break;
                    // Generate a report for all transactions within the previous year,
                    // including the date, time, description, vendor, and amount for each transaction.
                case "5":
                    System.out.println("Enter vendor name: ");
                    String vendor = scanner.nextLine().trim();
                    filterTransactionsByVendor(vendor);
                    break;
                    // Prompt the user to enter a vendor name, then generate a report for all transactions
                    // with that vendor, including the date, time, description, vendor, and amount for each transaction.
                case "6":
                    customSearch(scanner);
                    break;
                    //performing a custom search based on multiple criteria
                case "0":
                    running = false;
                    //to return to previous menu
                default:
                    System.out.println("Invalid option");
                    break;
            }
        }
    }


    private static void filterTransactionsByDate(LocalDate startDate, LocalDate endDate) {
        System.out.println("Date | Time | Description | Vendor | Amount");
        for (Transaction transaction : transactions) {
            //check if the transaction date falls within the start and end dates
            if (!transaction.getDate().isBefore(startDate) && !transaction.getDate().isAfter(endDate)) {
                System.out.println(transaction); //print matching transactions
            }
        }
        // This method filters the transactions by date and prints a report to the console.
        // It takes two parameters: startDate and endDate, which represent the range of dates to filter by.
        // The method loops through the transactions list and checks each transaction's date against the date range.
        // Transactions that fall within the date range are printed to the console.
        // If no transactions fall within the date range, the method prints a message indicating that there are no results.
    }

    private static void filterTransactionsByVendor(String vendor) {
        System.out.println("Date | Time | Description | Vendor | Amount");
        for (Transaction transaction : transactions) {
            if (transaction.getVendor().equalsIgnoreCase(vendor)) {
                System.out.println(transaction);
            }
        }
        // This method filters the transactions by vendor and prints a report to the console.
        // It takes one parameter: vendor, which represents the name of the vendor to filter by.
        // The method loops through the transactions list and checks each transaction's vendor name against the specified vendor name.
        // Transactions with a matching vendor name are printed to the console.
        // If no transactions match the specified vendor name, the method prints a message indicating that there are no results.
    }

    private static void filterByMonth(LocalDate date) {
        YearMonth currentMonth = YearMonth.of(date.getYear(), date.getMonth());
        LocalDate start = currentMonth.atDay(1);
        LocalDate end = currentMonth.atEndOfMonth();
        filterTransactionsByDate(start, end); // calling filter to display transactions
    }

    private static void filterByPreviousMonth() {
        YearMonth previousMonth = YearMonth.now().minusMonths(1);
        LocalDate start = previousMonth.atDay(1);
        LocalDate end = previousMonth.atEndOfMonth();
        filterTransactionsByDate(start, end); // calling filter to display transactions from previous month
    }

    private static void filterByYear(int year) {
        LocalDate start = LocalDate.of(year, 1, 1);
        LocalDate end = LocalDate.of(year, 12, 31);
        filterTransactionsByDate(start, end);
    }
    // performing a custom search based on multiple criteria (date, description, vendor, and amount)
    private static void customSearch(Scanner scanner) {
        System.out.println("Enter start date (yyyy-MM-dd) or press Enter to skip: ");
        String startDateInput = scanner.nextLine().trim();
        LocalDate startDate = startDateInput.isEmpty() ? null : LocalDate.parse(startDateInput, DATE_FORMATTER);

        System.out.println("Enter end date (yyyy-MM-dd) or press Enter to skip: ");
        String endDateInput = scanner.nextLine().trim();
        LocalDate endDate = endDateInput.isEmpty() ? null : LocalDate.parse(endDateInput, DATE_FORMATTER);

        System.out.println("Enter description or press Enter to skip: ");
        String description = scanner.nextLine().trim();

        System.out.println("Enter vendor or press Enter to skip: ");
        String vendor = scanner.nextLine().trim();

        System.out.println("Enter amount or press Enter to skip: ");
        String amountInput = scanner.nextLine().trim();
        Double amount = amountInput.isEmpty() ? null : Double.parseDouble(amountInput);

        //call the method to filter transactions based on the entered criteria
        filterTransactionsCustom(startDate, endDate, description, vendor, amount);
    }

    //filter transactions based on the custom criteria provided by the user
    private static void filterTransactionsCustom(LocalDate startDate, LocalDate endDate, String description, String vendor, Double amount) {
        System.out.println("Date | Time | Description | Vendor | Amount");

        for (Transaction transaction : transactions) {
            boolean matches = true;

            //checking if the transaction date is within the date range
            if (startDate != null && transaction.getDate().isBefore(startDate)) {
                matches = false;
            }
            if (endDate != null && transaction.getDate().isAfter(endDate)) {
                matches = false;
            }
            // if the description contains the input
            if (description != null && !description.isEmpty() && !transaction.getDescription().contains(description)) {
                matches = false;
            }
            // if the vendor matches the input
            if (vendor != null && !vendor.isEmpty() && !transaction.getVendor().equalsIgnoreCase(vendor)) {
                matches = false;
            }
            // if the amount matches the input
            if (amount != null && transaction.getAmount() != amount) {
                matches = false;
            }

            // print the transaction if all criteria match
            if (matches) {
                System.out.println(transaction);
            }
        }
    }
}