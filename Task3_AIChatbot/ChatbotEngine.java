package aichatbot;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class ChatbotEngine {

    public String getResponse(String input) {

        if (input == null || input.trim().isEmpty()) {
            return "Please type something so I can help you.";
        }

        String message = input.toLowerCase().trim();

        if (message.contains("hello") ||
                message.contains("hi") ||
                message.contains("hey")) {

            return "Hello! I'm your AI Study Assistant. How can I help you?";
        }

        if (message.contains("how are you")) {
            return "I'm doing great! Ready to help you learn.";
        }

        if (message.contains("your name") ||
                message.contains("who are you")) {

            return "I'm an AI Chatbot developed using Java.";
        }

        if (message.contains("java")) {
            return "Java is an object-oriented programming language widely used for desktop, web and enterprise applications.";
        }

        if (message.contains("oops") ||
                message.contains("oop")) {

            return "OOP stands for Object-Oriented Programming. Its main concepts are Encapsulation, Inheritance, Polymorphism and Abstraction.";
        }

        if (message.contains("arraylist")) {
            return "ArrayList is a resizable array implementation in Java. It belongs to the java.util package.";
        }

        if (message.contains("time")) {
            return "Current time: " +
                    LocalTime.now().format(
                            DateTimeFormatter.ofPattern("hh:mm a"));
        }

        if (message.contains("date") ||
                message.contains("today")) {

            return "Today's date is: " + LocalDate.now();
        }

        if (message.contains("help")) {
            return "You can ask me about Java, OOP, ArrayList, today's date, current time, or simply say hello.";
        }

        if (message.contains("thank")) {
            return "You're welcome!";
        }

        if (message.contains("bye") ||
                message.contains("exit")) {

            return "Goodbye! Keep learning and keep coding.";
        }

        return "I'm not sure about that yet. Try asking me about Java, OOP, ArrayList, date, time, or type 'help'.";
    }
}
