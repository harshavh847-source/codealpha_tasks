package studentgradetracker;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class StudentGradeTracker extends JFrame {

    private final StudentManager manager = new StudentManager();

    private final JTextField nameField = new JTextField();
    private final JTextField rollField = new JTextField();
    private final JTextField[] markFields = {
            new JTextField(), new JTextField(),
            new JTextField(), new JTextField(),
            new JTextField()
    };

    private final JLabel studentCount = new JLabel("Students: 0");
    private final JLabel classAverage = new JLabel("Class Average: 0.00");

    private final DefaultTableModel tableModel =
            new DefaultTableModel(
                    new String[]{"Name", "Roll No", "Average", "Grade", "Status"}, 0) {

                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

    private final JTable table = new JTable(tableModel);

    public StudentGradeTracker() {

        setTitle("Student Grade Tracker");
        setSize(1000, 650);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        createGUI();
    }

    private void createGUI() {

        // ===== HEADER =====
        JPanel header = new JPanel(new BorderLayout());
        header.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel title = new JLabel("STUDENT GRADE TRACKER");
        title.setFont(new Font("Arial", Font.BOLD, 26));

        JLabel subtitle =
                new JLabel("Student Performance Management System");

        JPanel titlePanel = new JPanel(new GridLayout(2, 1));
        titlePanel.add(title);
        titlePanel.add(subtitle);

        JPanel summary = new JPanel(new GridLayout(2, 1));
        summary.add(studentCount);
        summary.add(classAverage);

        header.add(titlePanel, BorderLayout.WEST);
        header.add(summary, BorderLayout.EAST);

        // ===== INPUT =====
        JPanel input = new JPanel(new GridLayout(3, 4, 10, 10));
        input.setBorder(
                BorderFactory.createTitledBorder("Student Information"));

        input.add(new JLabel("Student Name"));
        input.add(nameField);

        input.add(new JLabel("Roll Number"));
        input.add(rollField);

        for (int i = 0; i < 5; i++) {
            input.add(new JLabel("Subject " + (i + 1)));
            input.add(markFields[i]);
        }

        // ===== BUTTONS =====
        JButton addButton = new JButton("Add Student");
        JButton updateButton = new JButton("Update");
        JButton clearButton = new JButton("Clear");

        input.add(addButton);
        input.add(updateButton);

        JPanel actions = new JPanel();

        JButton searchButton = new JButton("Search");
        JButton deleteButton = new JButton("Delete");
        JButton statsButton = new JButton("Class Statistics");
        JButton topButton = new JButton("Top Student");

        actions.add(searchButton);
        actions.add(deleteButton);
        actions.add(statsButton);
        actions.add(topButton);
        actions.add(clearButton);

        // ===== TABLE =====
        table.setRowHeight(28);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setFont(
                new Font("Arial", Font.BOLD, 14));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(
                BorderFactory.createTitledBorder("Student Records"));

        JPanel center = new JPanel(new BorderLayout(10, 10));
        center.setBorder(BorderFactory.createEmptyBorder(5, 15, 15, 15));
        center.add(input, BorderLayout.NORTH);
        center.add(actions, BorderLayout.CENTER);

        add(header, BorderLayout.NORTH);
        add(center, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.SOUTH);

        scrollPane.setPreferredSize(new Dimension(950, 300));

        // ===== ADD =====
        addButton.addActionListener(e -> addStudent());

        // ===== UPDATE =====
        updateButton.addActionListener(e -> updateStudent());

        // ===== CLEAR =====
        clearButton.addActionListener(e -> clearFields());

        // ===== DELETE =====
        deleteButton.addActionListener(e -> deleteStudent());

        // ===== SEARCH =====
        searchButton.addActionListener(e -> searchStudent());

        // ===== STATISTICS =====
        statsButton.addActionListener(e -> showStatistics());

        // ===== TOP STUDENT =====
        topButton.addActionListener(e -> showTopStudent());

        // Select table row
        table.getSelectionModel().addListSelectionListener(e -> {

            if (!e.getValueIsAdjusting()
                    && table.getSelectedRow() >= 0) {

                int row = table.getSelectedRow();

                nameField.setText(
                        tableModel.getValueAt(row, 0).toString());

                rollField.setText(
                        tableModel.getValueAt(row, 1).toString());

                Student student =
                        manager.findStudent(rollField.getText());

                if (student != null) {

                    double[] marks = student.getMarks();

                    for (int i = 0; i < marks.length; i++) {
                        markFields[i].setText(
                                String.valueOf(marks[i]));
                    }
                }
            }
        });
    }

    // ===== ADD STUDENT =====
    private void addStudent() {

        try {

            String name = nameField.getText().trim();
            String roll = rollField.getText().trim();

            if (name.isEmpty() || roll.isEmpty()) {
                showError("Name and Roll Number are required.");
                return;
            }

            if (manager.findStudent(roll) != null) {
                showError("Roll Number already exists.");
                return;
            }

            double[] marks = readMarks();

            Student student =
                    new Student(name, roll, marks);

            manager.addStudent(student);

            refreshTable();
            clearFields();

            JOptionPane.showMessageDialog(this,
                    "Student added successfully!");

        } catch (NumberFormatException ex) {

            showError("Enter valid marks between 0 and 100.");
        }
    }

    // ===== UPDATE STUDENT =====
    private void updateStudent() {

        try {

            String roll = rollField.getText().trim();

            if (manager.findStudent(roll) == null) {
                showError("Student not found.");
                return;
            }

            String name = nameField.getText().trim();

            if (name.isEmpty()) {
                showError("Name cannot be empty.");
                return;
            }

            double[] marks = readMarks();

            manager.updateStudent(roll, name, marks);

            refreshTable();

            JOptionPane.showMessageDialog(this,
                    "Student updated successfully!");

        } catch (NumberFormatException ex) {

            showError("Enter valid marks between 0 and 100.");
        }
    }

    // ===== DELETE =====
    private void deleteStudent() {

        int row = table.getSelectedRow();

        if (row < 0) {
            showError("Select a student first.");
            return;
        }

        String roll =
                tableModel.getValueAt(row, 1).toString();

        int choice = JOptionPane.showConfirmDialog(
                this,
                "Permanently delete this student?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION);

        if (choice == JOptionPane.YES_OPTION) {

            manager.deleteStudent(roll);

            refreshTable();
            clearFields();

            JOptionPane.showMessageDialog(this,
                    "Student deleted successfully.");
        }
    }

    // ===== SEARCH =====
    private void searchStudent() {

        String roll = JOptionPane.showInputDialog(
                this, "Enter Roll Number:");

        if (roll == null || roll.trim().isEmpty())
            return;

        Student student = manager.findStudent(roll.trim());

        if (student == null) {
            showError("Student not found.");
            return;
        }

        nameField.setText(student.getName());
        rollField.setText(student.getRollNumber());

        double[] marks = student.getMarks();

        for (int i = 0; i < marks.length; i++)
            markFields[i].setText(String.valueOf(marks[i]));

        for (int i = 0; i < tableModel.getRowCount(); i++) {

            if (tableModel.getValueAt(i, 1)
                    .toString().equalsIgnoreCase(roll.trim())) {

                table.setRowSelectionInterval(i, i);
                break;
            }
        }
    }

    // ===== STATISTICS =====
    private void showStatistics() {

        if (manager.getStudentCount() == 0) {
            showError("No students available.");
            return;
        }

        Student top = manager.getTopStudent();
        Student lowest = manager.getLowestStudent();

        JOptionPane.showMessageDialog(this,
                "CLASS STATISTICS\n\n" +
                        "Total Students : " +
                        manager.getStudentCount() +

                        "\nClass Average  : " +
                        String.format("%.2f",
                                manager.getClassAverage()) +

                        "\nPassed         : " +
                        manager.getPassedCount() +

                        "\nFailed         : " +
                        manager.getFailedCount() +

                        "\n\nTop Student    : " +
                        top.getName() +

                        "\nTop Average    : " +
                        String.format("%.2f",
                                top.getAverage()) +

                        "\n\nLowest Student : " +
                        lowest.getName() +

                        "\nLowest Average : " +
                        String.format("%.2f",
                                lowest.getAverage()));
    }

    // ===== TOP STUDENT =====
    private void showTopStudent() {

        Student top = manager.getTopStudent();

        if (top == null) {
            showError("No students available.");
            return;
        }

        JOptionPane.showMessageDialog(this,
                "TOP PERFORMER\n\n" +
                        "Name: " + top.getName() +
                        "\nRoll No: " + top.getRollNumber() +
                        "\nAverage: " +
                        String.format("%.2f", top.getAverage()) +
                        "\nGrade: " + top.getGrade());
    }

    // ===== READ MARKS =====
    private double[] readMarks()
            throws NumberFormatException {

        double[] marks = new double[5];

        for (int i = 0; i < 5; i++) {

            marks[i] =
                    Double.parseDouble(
                            markFields[i].getText());

            if (marks[i] < 0 || marks[i] > 100)
                throw new NumberFormatException();
        }

        return marks;
    }

    // ===== REFRESH TABLE =====
    private void refreshTable() {

        tableModel.setRowCount(0);

        for (Student student : manager.getStudents()) {

            tableModel.addRow(new Object[]{
                    student.getName(),
                    student.getRollNumber(),
                    String.format("%.2f",
                            student.getAverage()),
                    student.getGrade(),
                    student.getStatus()
            });
        }

        studentCount.setText(
                "Students: " + manager.getStudentCount());

        classAverage.setText(
                "Class Average: " +
                        String.format("%.2f",
                                manager.getClassAverage()));
    }

    // ===== CLEAR =====
    private void clearFields() {

        nameField.setText("");
        rollField.setText("");

        for (JTextField field : markFields)
            field.setText("");

        table.clearSelection();
        nameField.requestFocus();
    }

    // ===== ERROR =====
    private void showError(String message) {

        JOptionPane.showMessageDialog(
                this,
                message,
                "Input Error",
                JOptionPane.WARNING_MESSAGE);
    }

    // ===== MAIN =====
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() ->
                new StudentGradeTracker().setVisible(true));
    }
}