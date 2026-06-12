package poe_part1;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        Login login = new Login();

        System.out.println("========================================");
        System.out.println("   Welcome to the Chat Application");
        System.out.println("========================================");
        System.out.println();

        System.out.print("Enter your first name: ");
        String firstName = scanner.nextLine().trim();

        System.out.print("Enter your last name: ");
        String lastName = scanner.nextLine().trim();

        login.setFirstName(firstName);
        login.setLastName(lastName);

        System.out.println();
        System.out.println("--- REGISTRATION ---");
        System.out.println();

        System.out.print("Enter username: ");
        String username = scanner.nextLine().trim();

        System.out.print("Enter password: ");
        String password = scanner.nextLine().trim();

        System.out.print("Enter cell phone number (e.g. +27838968976): ");
        String cellPhone = scanner.nextLine().trim();

        System.out.println();
        String registrationResult = login.registerUser(username, password, cellPhone);
        System.out.println(registrationResult);

        System.out.println();
        System.out.println("--- LOGIN ---");
        System.out.println();

        System.out.print("Enter username: ");
        String loginUsername = scanner.nextLine().trim();

        System.out.print("Enter password: ");
        String loginPassword = scanner.nextLine().trim();

        boolean loginSuccess = login.loginUser(loginUsername, loginPassword);

        System.out.println();
        System.out.println(login.returnLoginStatus(loginSuccess));

        if (!loginSuccess) {
            System.out.println("Exiting application.");
            scanner.close();
            return;
        }

        System.out.println();
        System.out.println("Welcome to Quickchat.");
        System.out.println();

        System.out.print("How many messages would you like to send? ");
        int numMessages = Integer.parseInt(scanner.nextLine().trim());

        int menuChoice = 0;

        while (menuChoice != 3) {
            System.out.println();
            System.out.println("1) Send Messages");
            System.out.println("2) Show recently sent messages");
            System.out.println("3) Quit");
            System.out.println("4) Stored Messages");
            System.out.print("Enter choice: ");
            menuChoice = Integer.parseInt(scanner.nextLine().trim());

            if (menuChoice == 1) {
                for (int i = 1; i <= numMessages; i++) {
                    System.out.println();
                    System.out.println("--- Message " + i + " of " + numMessages + " ---");

                    Message msg = new Message(i);

                    System.out.print("Enter recipient number: ");
                    String recipientInput = scanner.nextLine().trim();
                    String recipientResult = msg.checkRecipientCell(recipientInput);
                    System.out.println(recipientResult);

                    if (!recipientResult.equals("Cell phone number successfully captured.")) {
                        System.out.println("Skipping this message.");
                        continue;
                    }

                    msg.setRecipient(recipientInput);

                    System.out.print("Enter message (max 250 characters): ");
                    String messageInput = scanner.nextLine().trim();

                    if (messageInput.length() > 250) {
                        int excess = messageInput.length() - 250;
                        System.out.println("Message exceeds 250 characters by " + excess + "; please reduce the size.");
                        continue;
                    }

                    System.out.println("Message ready to send.");
                    msg.setMessage(messageInput);

                    String hash = msg.createMessageHash();

                    System.out.println();
                    System.out.println("Message ID: " + msg.getMessageID());
                    System.out.println("Message Hash: " + hash);
                    System.out.println("Recipient: " + msg.getRecipient());
                    System.out.println("Message: " + msg.getMessage());
                    System.out.println();

                    System.out.println("1) Send Message");
                    System.out.println("2) Disregard Message");
                    System.out.println("3) Store Message");
                    System.out.print("Enter choice: ");
                    int sendChoice = Integer.parseInt(scanner.nextLine().trim());

                    System.out.println(msg.sentMessage(sendChoice));
                }

                System.out.println();
                System.out.println("Total messages sent: " + new Message(0).returnTotalMessages());

            } else if (menuChoice == 2) {
                System.out.println("Coming Soon.");

            } else if (menuChoice == 3) {
                System.out.println(Message.displayReport());
                System.out.println("Goodbye!");

            } else if (menuChoice == 4) {
                System.out.println();
                System.out.println("1) Display all stored messages");
                System.out.println("2) Display longest message");
                System.out.println("3) Search by Message ID");
                System.out.println("4) Search by Recipient");
                System.out.println("5) Delete message by Hash");
                System.out.print("Enter choice: ");
                int subChoice = Integer.parseInt(scanner.nextLine().trim());

                if (subChoice == 1) {
                    System.out.println(Message.displayAllStoredMessages());

                } else if (subChoice == 2) {
                    System.out.println(Message.displayLongestMessage());

                } else if (subChoice == 3) {
                    System.out.print("Enter Message ID: ");
                    String searchID = scanner.nextLine().trim();
                    System.out.println(Message.searchByMessageID(searchID));

                } else if (subChoice == 4) {
                    System.out.print("Enter recipient number: ");
                    String searchRecipient = scanner.nextLine().trim();
                    System.out.println(Message.searchByRecipient(searchRecipient));

                } else if (subChoice == 5) {
                    System.out.print("Enter Message Hash: ");
                    String searchHash = scanner.nextLine().trim();
                    System.out.println(Message.deleteMessageByHash(searchHash));
                }
            }
        }

        scanner.close();
    }
}