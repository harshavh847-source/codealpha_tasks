package studentgradetracker;

public class Student {

    private String name;
    private String rollNumber;
    private double[] marks;

    public Student(String name, String rollNumber, double[] marks) {
        this.name = name;
        this.rollNumber = rollNumber;
        this.marks = marks;
    }

    public String getName() {
        return name;
    }

    public String getRollNumber() {
        return rollNumber;
    }

    public double[] getMarks() {
        return marks;
    }

    public double getAverage() {
        double total = 0;

        for (double mark : marks) {
            total += mark;
        }

        return total / marks.length;
    }

    public double getHighestMark() {
        double highest = marks[0];

        for (double mark : marks) {
            if (mark > highest) {
                highest = mark;
            }
        }

        return highest;
    }

    public double getLowestMark() {
        double lowest = marks[0];

        for (double mark : marks) {
            if (mark < lowest) {
                lowest = mark;
            }
        }

        return lowest;
    }

    public String getGrade() {
        double average = getAverage();

        if (average >= 90) return "A+";
        if (average >= 80) return "A";
        if (average >= 70) return "B";
        if (average >= 60) return "C";
        if (average >= 50) return "D";

        return "F";
    }

    public String getStatus() {
        return getAverage() >= 40 ? "PASS" : "FAIL";
    }
}