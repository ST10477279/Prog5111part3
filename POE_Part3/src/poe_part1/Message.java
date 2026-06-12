package poe_part1;

import java.util.ArrayList;
import java.util.Random;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Message {

    private static ArrayList<String[]> sentMessages = new ArrayList<>();
    private static ArrayList<String[]> disregardedMessages = new ArrayList<>();
    private static ArrayList<String[]> storedMessages = new ArrayList<>();
    private static ArrayList<String> messageHashes = new ArrayList<>();
    private static ArrayList<String> messageIDs = new ArrayList<>();
    private static int totalMessagesSent = 0;

    private String messageID;
    private String messageHash;
    private String recipient;
    private String message;
    private int messageNumber;

    public Message(int messageNumber) {
        this.messageNumber = messageNumber;
        this.messageID = generateMessageID();
    }

    private String generateMessageID() {
        Random random = new Random();
        long id = (long) (random.nextDouble() * 9000000000L) + 1000000000L;
        return String.valueOf(id);
    }

    public boolean checkMessageID() {
        return messageID != null && messageID.length() <= 10;
    }

    public String checkRecipientCell(String number) {
        if (number == null || number.isEmpty()) {
            return "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
        }
        if (!number.startsWith("+")) {
            return "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
        }
        this.recipient = number;
        return "Cell phone number successfully captured.";
    }

    public String createMessageHash() {
        String firstTwo = messageID.substring(0, 2);
        String[] words = message.trim().split("\\s+");
        String firstWord = words[0];
        String lastWord = words[words.length - 1];
        messageHash = (firstTwo + ":" + messageNumber + ":" + firstWord + lastWord).toUpperCase();
        messageHashes.add(messageHash);
        messageIDs.add(messageID);
        return messageHash;
    }

    public String sentMessage(int choice) {
        if (choice == 1) {
            totalMessagesSent++;
            String[] details = {messageID, messageHash, recipient, message};
            sentMessages.add(details);
            return "Message successfully sent.";
        } else if (choice == 2) {
            String[] details = {messageID, messageHash, recipient, message};
            disregardedMessages.add(details);
            return "Press 0 to delete the message.";
        } else if (choice == 3) {
            storeMessage();
            return "Message successfully stored.";
        }
        return "Invalid option.";
    }

    public String printMessages() {
        if (sentMessages.isEmpty()) {
            return "No messages sent.";
        }
        StringBuilder sb = new StringBuilder();
        for (String[] msg : sentMessages) {
            sb.append("Message ID: ").append(msg[0]).append("\n");
            sb.append("Message Hash: ").append(msg[1]).append("\n");
            sb.append("Recipient: ").append(msg[2]).append("\n");
            sb.append("Message: ").append(msg[3]).append("\n");
            sb.append("----------------------------\n");
        }
        return sb.toString();
    }

    public int returnTotalMessages() {
        return totalMessagesSent;
    }

    public void storeMessage() {
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("messageID", messageID);
        jsonObject.put("messageHash", messageHash);
        jsonObject.put("recipient", recipient);
        jsonObject.put("message", message);
        jsonArray.add(jsonObject);

        try (FileWriter file = new FileWriter("messages.json", true)) {
            file.write(jsonArray.toJSONString());
            file.write("\n");
        } catch (IOException e) {
            System.out.println("Error storing message: " + e.getMessage());
        }
    }

    public static void loadStoredMessages() {
        storedMessages.clear();
        JSONParser parser = new JSONParser();
        try (FileReader reader = new FileReader("messages.json")) {
            String line;
            java.io.BufferedReader br = new java.io.BufferedReader(reader);
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    Object obj = parser.parse(line);
                    JSONArray array = (JSONArray) obj;
                    for (Object o : array) {
                        JSONObject jo = (JSONObject) o;
                        String[] details = {
                            (String) jo.get("messageID"),
                            (String) jo.get("messageHash"),
                            (String) jo.get("recipient"),
                            (String) jo.get("message")
                        };
                        storedMessages.add(details);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("No stored messages found.");
        }
    }

    public static String displayAllStoredMessages() {
        loadStoredMessages();
        if (storedMessages.isEmpty()) {
            return "No stored messages.";
        }
        StringBuilder sb = new StringBuilder();
        for (String[] msg : storedMessages) {
            sb.append("Recipient: ").append(msg[2]).append("\n");
            sb.append("Message: ").append(msg[3]).append("\n");
            sb.append("----------------------------\n");
        }
        return sb.toString();
    }

    public static String displayLongestMessage() {
        ArrayList<String[]> allMessages = new ArrayList<>();
        allMessages.addAll(sentMessages);
        allMessages.addAll(storedMessages);

        if (allMessages.isEmpty()) {
            return "No messages available.";
        }

        String longest = "";
        String longestRecipient = "";
        for (String[] msg : allMessages) {
            if (msg[3].length() > longest.length()) {
                longest = msg[3];
                longestRecipient = msg[2];
            }
        }
        return "Recipient: " + longestRecipient + "\nMessage: " + longest;
    }

    public static String searchByMessageID(String id) {
        ArrayList<String[]> allMessages = new ArrayList<>();
        allMessages.addAll(sentMessages);
        allMessages.addAll(storedMessages);

        for (String[] msg : allMessages) {
            if (msg[0].equals(id)) {
                return "Recipient: " + msg[2] + "\nMessage: " + msg[3];
            }
        }
        return "Message ID not found.";
    }

    public static String searchByRecipient(String recipient) {
        ArrayList<String[]> allMessages = new ArrayList<>();
        allMessages.addAll(sentMessages);
        allMessages.addAll(storedMessages);

        StringBuilder sb = new StringBuilder();
        for (String[] msg : allMessages) {
            if (msg[2].equals(recipient)) {
                sb.append("Message: ").append(msg[3]).append("\n");
            }
        }
        if (sb.length() == 0) {
            return "No messages found for recipient.";
        }
        return sb.toString();
    }

    public static String deleteMessageByHash(String hash) {
        for (int i = 0; i < sentMessages.size(); i++) {
            if (sentMessages.get(i)[1].equals(hash)) {
                String msg = sentMessages.get(i)[3];
                sentMessages.remove(i);
                return "Message: \"" + msg + "\" successfully deleted.";
            }
        }
        for (int i = 0; i < disregardedMessages.size(); i++) {
            if (disregardedMessages.get(i)[1].equals(hash)) {
                String msg = disregardedMessages.get(i)[3];
                disregardedMessages.remove(i);
                return "Message: \"" + msg + "\" successfully deleted.";
            }
        }
        return "Message hash not found.";
    }

    public static String displayReport() {
        if (sentMessages.isEmpty() && disregardedMessages.isEmpty() && storedMessages.isEmpty()) {
            return "No messages to display.";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("===== MESSAGE REPORT =====\n");
        sb.append("Total messages sent: ").append(totalMessagesSent).append("\n\n");

        sb.append("--- Sent Messages ---\n");
        for (String[] msg : sentMessages) {
            sb.append("ID: ").append(msg[0]).append("\n");
            sb.append("Hash: ").append(msg[1]).append("\n");
            sb.append("Recipient: ").append(msg[2]).append("\n");
            sb.append("Message: ").append(msg[3]).append("\n\n");
        }

        sb.append("--- Disregarded Messages ---\n");
        for (String[] msg : disregardedMessages) {
            sb.append("Recipient: ").append(msg[2]).append("\n");
            sb.append("Message: ").append(msg[3]).append("\n\n");
        }

        sb.append("--- Stored Messages ---\n");
        for (String[] msg : storedMessages) {
            sb.append("Recipient: ").append(msg[2]).append("\n");
            sb.append("Message: ").append(msg[3]).append("\n\n");
        }
        return sb.toString();
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getMessageID() {
        return messageID;
    }

    public String getMessageHash() {
        return messageHash;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getMessage() {
        return message;
    }

    public static ArrayList<String[]> getSentMessages() {
        return sentMessages;
    }

    public static ArrayList<String[]> getDisregardedMessages() {
        return disregardedMessages;
    }

    public static ArrayList<String[]> getStoredMessages() {
        return storedMessages;
    }

    public static void resetMessages() {
        sentMessages.clear();
        disregardedMessages.clear();
        storedMessages.clear();
        messageHashes.clear();
        messageIDs.clear();
        totalMessagesSent = 0;
    }
}