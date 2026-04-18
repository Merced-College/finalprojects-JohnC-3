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

        ArrayList<Book> testList = insertionSort("author", books);
        System.out.println("\nSorted books by author:");
        for(int i = 0; i < testList.size(); i++) {
            System.out.println(testList.get(i));
        }
    }

    // Modified code from Assessment: Car Data Analyzer
    private static void loadBooks(String filename) {
        // Try to open and read the file
        try(Scanner scnr = new Scanner(new File(filename))) {
            int count = 0;
            // Skip the header line
            scnr.nextLine();

            // Read each line of the file
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

    // Modified code from Assessment: Car Data Analyzer
    private static ArrayList<Book> insertionSort(String sortBy, ArrayList<Book> books) {
        // Outer loop for each element in the list
        for(int i = 0; i < books.size(); i++) {
            if(sortBy.equalsIgnoreCase("author")) {
                // Inner loop to sort by author
                for(int j = i; j > 0 && (books.get(j).getAuthor().getLastName() + books.get(j).getAuthor().getFirstName()).compareTo(books.get(j - 1).getAuthor().getLastName() + books.get(j - 1).getAuthor().getFirstName()) < 0; j--) {
                    // Swap elements if out of order
                    Book temp = books.get(j);
                    books.set(j, books.get(j - 1));
                    books.set(j - 1, temp);
                }
            } else if(sortBy.equalsIgnoreCase("ISBN")) {
                // Inner loop to sort by ISBN
                for(int j = i; j > 0 && books.get(j).getISBN().compareTo(books.get(j - 1).getISBN()) < 0; j--) {
                    // Swap elements if out of order
                    Book temp = books.get(j);
                    books.set(j, books.get(j - 1));
                    books.set(j - 1, temp);
                }
            } else if(sortBy.equalsIgnoreCase("publication year") || sortBy.equalsIgnoreCase("publicationyear")) {
                // Inner loop to sort by publication year
                for(int j = i; j > 0 && books.get(j).getPublicationYear() < books.get(j - 1).getPublicationYear(); j--) {
                    // Swap elements if out of order
                    Book temp = books.get(j);
                    books.set(j, books.get(j - 1));
                    books.set(j - 1, temp);
                }
            } else if(sortBy.equalsIgnoreCase("genre")) {
                // Inner loop to sort by genre
                for(int j = i; j > 0 && books.get(j).getGenre().compareTo(books.get(j - 1).getGenre()) < 0; j--) {
                    // Swap elements if out of order
                    Book temp = books.get(j);
                    books.set(j, books.get(j - 1));
                    books.set(j - 1, temp);
                }
            } else if(sortBy.equalsIgnoreCase("language")) {
                // Inner loop to sort by language
                for(int j = i; j > 0 && books.get(j).getLanguage().compareTo(books.get(j - 1).getLanguage()) < 0; j--) {
                    // Swap elements if out of order
                    Book temp = books.get(j);
                    books.set(j, books.get(j - 1));
                    books.set(j - 1, temp);
                }
            } else {
                // Inner loop to sort by title
                for(int j = i; j > 0 && books.get(j).getTitle().compareTo(books.get(j - 1).getTitle()) < 0; j--) {
                    // Swap elements if out of order
                    Book temp = books.get(j);
                    books.set(j, books.get(j - 1));
                    books.set(j - 1, temp);
                }
            }
        }
        return books;
    }
}