package lms;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class FileManager {

    private static final String DATA_FOLDER = "../data/";

    public static List<Book> loadBooks(String fileName) {
        List<Book> books = new ArrayList<>();
        Path path = Paths.get(DATA_FOLDER + fileName);
        try (BufferedReader br = new BufferedReader(new FileReader(path.toFile()))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] data = line.split(",", -1);
                if (data.length != 7) {
                    System.out.println("Skipping invalid book record (wrong column count): " + line);
                    continue;
                }
                try {
                    Book book = new Book();
                    book.setBookId(data[0].trim());
                    book.setTitle(data[1].trim());
                    book.setIsbn(data[2].trim());
                    book.setAuthor(data[3].trim());
                    book.setCopies(Integer.parseInt(data[4].trim()));
                    book.setAvailability(Integer.parseInt(data[5].trim()));
                    book.setPrice(Double.parseDouble(data[6].trim()));
                    books.add(book);
                } catch (Exception e) {
                    System.out.println("Error parsing book: " + line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading books file: " + e.getMessage());
        }
        return books;
    }

    public static List<Student> loadStudents(String fileName) {
        List<Student> students = new ArrayList<>();
        Path path = Paths.get(DATA_FOLDER + fileName);
        try (BufferedReader br = new BufferedReader(new FileReader(path.toFile()))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] data = line.split(",", -1);
                if (data.length != 2) {
                    System.out.println("Skipping invalid student record: " + line);
                    continue;
                }
                try {
                    Student student = new Student();
                    student.setStudentID(data[0].trim());
                    student.setFirstName(data[1].trim());
                    students.add(student);
                } catch (Exception e) {
                    System.out.println("Error parsing student: " + line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading books file: " + e.getMessage());
        }
        return students;
    }

    public static List<Transaction> loadTransactions(String fileName) {
        List<Transaction> transactions = new ArrayList<>();
        Path path = Paths.get(DATA_FOLDER + fileName);
        try (BufferedReader br = new BufferedReader(new FileReader(path.toFile()))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] data = line.split(",", -1);
                if (data.length != 4) {
                    System.out.println("Skipping invalid transaction record: " + line);
                    continue;
                }
                try {
                    Transaction t = new Transaction();
                    t.setDate(data[0].trim());
                    t.setBookId(data[1].trim());
                    t.setStudentId(data[2].trim());
                    t.setType(Integer.parseInt(data[3].trim()));
                    transactions.add(t);
                } catch (Exception e) {
                    System.out.println("Error parsing transaction: " + line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading books file: " + e.getMessage());
        }
        return transactions;
    }

    public static void saveBooks(String fileName, List<Book> books) {
        Path path = Paths.get(DATA_FOLDER + fileName);
        try (PrintWriter pw = new PrintWriter(new FileWriter(path.toFile()))) {
            for (Book b : books) pw.println(b.toCsv());
        } catch (Exception e) {
            System.out.println("Error saving books: " + e.getMessage());
        }
    }

    public static void saveStudents(String fileName, List<Student> students) {
        Path path = Paths.get(DATA_FOLDER + fileName);
        try (PrintWriter pw = new PrintWriter(new FileWriter(path.toFile()))) {
            for (Student s : students) pw.println(s.toCsv());
        } catch (Exception e) {
            System.out.println("Error saving students: " + e.getMessage());
        }
    }

    public static void saveTransactions(String fileName, List<Transaction> transactions) {
        Path path = Paths.get(DATA_FOLDER + fileName);
        try (PrintWriter pw = new PrintWriter(new FileWriter(path.toFile()))) {
            for (Transaction t : transactions) pw.println(t.toCsv());
        } catch (Exception e) {
            System.out.println("Error saving transactions: " + e.getMessage());
        }
    }

    public static void exportToFile(String filePath, String content) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filePath))) {
            pw.print(content);
        } catch (Exception e) {
            System.out.println("Error exporting file: " + e.getMessage());
        }
    }
}
