// John Chiero
// 4/16/2026
// Main class

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    private static ArrayList<Book> books = new ArrayList<Book>();

    public static void main(String[] args ) {
        loadBooks("Books.txt");
        for(int i = 0; i < books.size(); i++) {
            System.out.println(books.get(i));
        }
    }

    // Modified code from Assessment: Car Data Analyzer
    private static void loadBooks(String filename) {
        // Try to open and read the file
        try(Scanner scnr = new Scanner(new File(filename))) {
            int count = 0;
            // Skip the header line
            scnr.nextLine();

            // Read each line of the CSV
            while(scnr.hasNextLine()) {
                String line = scnr.nextLine();
                // Skip empty lines
                if (line.isEmpty()) continue;

                // Split the line by commas to get individual fields
                String[] parts = line.split(",");

                // Parse each field
                String title = parts[0];
                String isbn = parts[2];
                int publicationYear = Integer.parseInt(parts[3]);
                String genre = parts[4];
                String language = parts[5];

                // Parse author field
                String[] authorParts = parts[1].split(" ");
                String firstName = authorParts[0];
                String middleInitial;
                String lastName;
                if(authorParts.length == 2) {
                    middleInitial = null;
                    lastName = authorParts[1];
                } else {
                    middleInitial = authorParts[1].substring(0, 1);
                    lastName = authorParts[2];
                }
                Author author = new Author(firstName, lastName, middleInitial);

                // Create a new Car object and add it to the list
                Book book = new Book(title, author, isbn, publicationYear, genre, language);
                books.add(book);
                count++;
            }

            // Display the total number of cars loaded
            System.out.println("Total books loaded: " + count);
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + filename);
            return;
        }
    }
}