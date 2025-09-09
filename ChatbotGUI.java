import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple AI Chatbot with a graphical user interface using Java Swing.
 *
 * This class combines a rule-based logic engine with a GUI for real-time interaction.
 * The chatbot is "trained" on a set of predefined questions and answers.
 */
public class ChatbotGUI extends JFrame {

    // GUI Components
    private final JTextArea chatArea;
    private final JTextField inputField;
    
    // Chatbot Logic
    private final Map<String, String> knowledgeBase = new HashMap<>();

    public ChatbotGUI() {
        // --- Frame Setup ---
        setTitle("AI Chatbot");
        setSize(500, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        setLocationRelativeTo(null); // Center the window

        // --- Knowledge Base Training ---
        trainBot();

        // --- GUI Components Initialization ---
        // Chat Area
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);
        chatArea.setFont(new Font("Arial", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(chatArea);

        // Input Panel
        JPanel inputPanel = new JPanel(new BorderLayout(10, 10));
        inputField = new JTextField();
        JButton sendButton = new JButton("Send");
        
        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Add components to the frame
        add(scrollPane, BorderLayout.CENTER);
        add(inputPanel, BorderLayout.SOUTH);

        // --- Action Listeners ---
        // Send button action
        sendButton.addActionListener(e -> sendMessage());
        
        // Allow sending message by pressing Enter in the text field
        inputField.addActionListener(e -> sendMessage());
        
        // Initial greeting
        appendMessage("Bot: Hello! How can I help you today?", false);
    }
    
    /**
     * Populates the knowledge base with predefined questions and answers.
     * This simulates the "training" of the chatbot.
     */
    private void trainBot() {
        // Greetings
        knowledgeBase.put("hello", "Hi there! How can I assist you?");
        knowledgeBase.put("hi", "Hello! What can I do for you?");
        knowledgeBase.put("hey", "Hey! How's it going?");

        // Common Questions
        knowledgeBase.put("how are you", "I'm just a program, but I'm doing great! Thanks for asking.");
        knowledgeBase.put("what is your name", "I am a simple AI Chatbot created in Java.");
        knowledgeBase.put("what can you do", "I can answer some frequently asked questions. Try asking me about my name or the time.");

        // Dynamic Responses
        knowledgeBase.put("time", "The current time is " + LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")) + ".");
        
        // Farewells
        knowledgeBase.put("bye", "Goodbye! Have a great day.");
        knowledgeBase.put("exit", "Farewell! Feel free to chat again anytime.");
    }
    
    /**
     * Handles the logic of sending and receiving messages.
     */
    private void sendMessage() {
        String userInput = inputField.getText().trim();
        if (!userInput.isEmpty()) {
            appendMessage("You: " + userInput, true);
            inputField.setText("");
            
            // Generate and display the bot's response
            String botResponse = getBotResponse(userInput);
            appendMessage("Bot: " + botResponse, false);
        }
    }
    
    /**
     * Appends a message to the chat area and ensures it's visible.
     * @param message The message to display.
     * @param isUser  True if the message is from the user, false if from the bot.
     */
    private void appendMessage(String message, boolean isUser) {
        // A simple way to distinguish user messages, e.g., by adding a prefix or color
        chatArea.append(message + "\n\n");
        // Auto-scroll to the bottom
        chatArea.setCaretPosition(chatArea.getDocument().getLength());
    }

    /**
     * Generates a response from the bot based on user input.
     * This is the core of the rule-based NLP engine.
     * @param userInput The user's message.
     * @return The bot's response as a String.
     */
    private String getBotResponse(String userInput) {
        String processedInput = userInput.toLowerCase();
        
        // Check for an exact match in the knowledge base first
        if (knowledgeBase.containsKey(processedInput)) {
            // Handle dynamic responses like time
            if (processedInput.equals("time")) {
                 return "The current time is " + LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")) + ".";
            }
            return knowledgeBase.get(processedInput);
        }
        
        // If no exact match, check for keywords
        for (String key : knowledgeBase.keySet()) {
            if (processedInput.contains(key)) {
                return knowledgeBase.get(key);
            }
        }
        
        // Default response if no match is found
        return "I'm sorry, I don't understand that. Can you please rephrase?";
    }

    /**
     * Main method to run the application.
     */
    public static void main(String[] args) {
        // Ensure the GUI is created on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> new ChatbotGUI().setVisible(true));
    }
}