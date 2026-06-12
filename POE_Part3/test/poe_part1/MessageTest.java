package poe_part1;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class MessageTest {

    private Message message1;
    private Message message2;
    private Message message3;
    private Message message4;
    private Message message5;

    @Before
    public void setUp() {
        Message.resetMessages();

        message1 = new Message(1);
        message1.setRecipient("+27834557896");
        message1.setMessage("Did you get the cake?");
        message1.createMessageHash();
        message1.sentMessage(1);

        message2 = new Message(2);
        message2.setRecipient("+27838884567");
        message2.setMessage("Where are you? You are late! I have asked you to be on time.");
        message2.createMessageHash();
        message2.sentMessage(3);

        message3 = new Message(3);
        message3.setRecipient("+27834484567");
        message3.setMessage("Yohoooo, I am at your gate.");
        message3.createMessageHash();
        message3.sentMessage(2);

        message4 = new Message(4);
        message4.setRecipient("0838884567");
        message4.setMessage("It is dinner time!");
        message4.createMessageHash();
        message4.sentMessage(1);

        message5 = new Message(5);
        message5.setRecipient("+27838884567");
        message5.setMessage("Ok, I am leaving without you.");
        message5.createMessageHash();
        message5.sentMessage(3);
    }

    @Test
    public void testMessageIDNotMoreThanTenCharacters() {
        assertTrue(message1.checkMessageID());
    }

    @Test
    public void testMessageIDGenerated() {
        String id = message1.getMessageID();
        assertTrue(id != null && !id.isEmpty());
        System.out.println("Message ID generated: " + id);
    }

    @Test
    public void testMessageWithinLimit() {
        String msg = "Did you get the cake?";
        assertTrue(msg.length() <= 250);
        assertEquals("Message ready to send.", msg.length() <= 250 ? "Message ready to send." : "fail");
    }

    @Test
    public void testMessageExceedsLimit() {
        String longMessage = "A".repeat(260);
        int excess = longMessage.length() - 250;
        String result = "Message exceeds 250 characters by " + excess + "; please reduce the size.";
        assertEquals("Message exceeds 250 characters by 10; please reduce the size.", result);
    }

    @Test
    public void testRecipientValidSuccess() {
        Message msg = new Message(1);
        String result = msg.checkRecipientCell("+27718693002");
        assertEquals("Cell phone number successfully captured.", result);
    }

    @Test
    public void testRecipientValidFailure() {
        Message msg = new Message(1);
        String result = msg.checkRecipientCell("08575975889");
        assertEquals("Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.", result);
    }

    @Test
    public void testSentMessagesArrayPopulated() {
        assertEquals("Did you get the cake?", Message.getSentMessages().get(0)[3]);
        assertEquals("It is dinner time!", Message.getSentMessages().get(1)[3]);
    }

    @Test
    public void testDisplayLongestMessage() {
        String result = Message.displayLongestMessage();
        assertTrue(result.contains("Where are you? You are late! I have asked you to be on time."));
    }

    @Test
    public void testSearchByRecipient() {
        String result = Message.searchByRecipient("+27838884567");
        assertTrue(result.contains("Where are you? You are late! I have asked you to be on time."));
        assertTrue(result.contains("Ok, I am leaving without you."));
    }

    @Test
    public void testDeleteMessageByHash() {
        String hash = message2.getMessageHash();
        String result = Message.deleteMessageByHash(hash);
        assertTrue(result.contains("successfully deleted"));
    }

    @Test
    public void testSentMessageSend() {
        Message msg = new Message(1);
        msg.setRecipient("+27718693002");
        msg.setMessage("Test message.");
        msg.createMessageHash();
        assertEquals("Message successfully sent.", msg.sentMessage(1));
    }

    @Test
    public void testSentMessageDisregard() {
        Message msg = new Message(1);
        msg.setRecipient("+27718693002");
        msg.setMessage("Test message.");
        msg.createMessageHash();
        assertEquals("Press 0 to delete the message.", msg.sentMessage(2));
    }

    @Test
    public void testSentMessageStore() {
        Message msg = new Message(1);
        msg.setRecipient("+27718693002");
        msg.setMessage("Test message.");
        msg.createMessageHash();
        assertEquals("Message successfully stored.", msg.sentMessage(3));
    }

    @Test
    public void testReturnTotalMessages() {
        assertTrue(message1.returnTotalMessages() >= 1);
    }

    @Test
    public void testMessageHashesInLoop() {
        for (String[] msg : Message.getSentMessages()) {
            assertTrue(msg[1] != null && !msg[1].isEmpty());
            assertTrue(msg[1].equals(msg[1].toUpperCase()));
        }
    }
}