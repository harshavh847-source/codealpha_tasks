
package studentgradetracker;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class StudentManager {

    private final List<Student> students = new ArrayList<>();

    // Add student
    public boolean addStudent(Student student) {
        if (findStudent(student.getRollNumber()) != null) {
            return false;
        }

        students.add(student);
        return true;
    }

    // Find student by roll number
    public Student findStudent(String rollNumber) {
        for (Student student : students) {
            if (student.getRollNumber().equalsIgnoreCase(rollNumber)) {
                return student;
            }
        }

        return null;
    }

    // Update existing student
    public boolean updateStudent(String rollNumber, String name,
                                 double[] marks) {

        Student oldStudent = findStudent(rollNumber);

        if (oldStudent == null) {
            return false;
        }

        students.remove(oldStudent);
        students.add(new Student(name, rollNumber, marks));

        return true;
    }

    // Delete student
    public boolean deleteStudent(String rollNumber) {
        Student student = findStudent(rollNumber);

        if (student == null) {
            return false;
        }

        students.remove(student);
        return true;
    }

    // Get all students
    public List<Student> getStudents() {
        return new ArrayList<>(students);
    }

    // Number of students
    public int getStudentCount() {
        return students.size();
    }

    // Class average
    public double getClassAverage() {
        if (students.isEmpty()) {
            return 0;
        }

        double total = 0;

        for (Student student : students) {
            total += student.getAverage();
        }

        return total / students.size();
    }

    // Top student
    public Student getTopStudent() {
        return students.stream()
                .max(Comparator.comparingDouble(Student::getAverage))
                .orElse(null);
    }

    // Lowest student
    public Student getLowestStudent() {
        return students.stream()
                .min(Comparator.comparingDouble(Student::getAverage))
                .orElse(null);
    }

    // Passed students
    public int getPassedCount() {
        int count = 0;

        for (Student student : students) {
            if (student.getStatus().equals("PASS")) {
                count++;
            }
        }

        return count;
    }

    // Failed students
    public int getFailedCount() {
        return students.size() - getPassedCount();
    }

    // Clear all students
    public void clearAll() {
        students.clear();
    }
}