package src;
// John Chiero
// 4/20/2026
// Main class

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
    private static ArrayList<Book> books = new ArrayList<Book>();
    private static ArrayList<Member> members = new ArrayList<Member>();
    private static ArrayList<Scene> scenes = new ArrayList<Scene>();

    public static void main(String[] args ) {
        setup();
        libraryManager();
    }

    private static void setup() {
        loadBooks("docs/Books.txt");
        for(int i = 0; i < books.size(); i++) {
            System.out.println(books.get(i));
        }

        System.out.println("");
        loadMembers("docs/Members.txt");
        for(int i = 0; i < members.size(); i++) {
            System.out.println(members.get(i));
        }

        System.out.println("");
        loadScenes("docs/Scenes.txt");

        System.out.println("\n\n\n------------------------------\n\n\n");
    }

    private static void libraryManager() {
        Scanner scnr = new Scanner(System.in);
        int input;
        String writtenInput;
        int currSceneID = 0;
        Scene currScene;
        String sortBy = "";
        ArrayList<Book> bookResults;
        ArrayList<Member> memberResults = new ArrayList<Member>();

        System.out.println("Welcome to the Library Manager!\n");
        while(true) {
            if(currSceneID != -1) {
                currScene = scenes.get(currSceneID);
            } else {
                break;
            }

            if(currSceneID == 2) {
                System.out.print(currScene.getPrompt());
                scnr.nextLine();
                writtenInput = scnr.nextLine();
                bookResults = bookBinarySearch(sortBy, writtenInput, bookInsertionSort(sortBy, books));
                System.out.println("\nSearch results:");
                for(int i = 0; i < bookResults.size(); i++) {
                    System.out.println(bookResults.get(i));
                }
                currSceneID = 0;
            } else if(currSceneID == 4) {
                System.out.println(currScene.getPrompt());
                bookResults = bookInsertionSort(sortBy, books);
                for(int i = 0; i < bookResults.size(); i++) {
                    System.out.println(bookResults.get(i));
                }
                currSceneID = 0;
            } else if(currSceneID == 6) {
                System.out.print(currScene.getPrompt());
                scnr.nextLine();
                writtenInput = scnr.nextLine();
                memberResults = memberBinarySearch(sortBy, writtenInput, memberInsertionSort(sortBy, members));
                System.out.println("\nSearch results:");
                for(int i = 0; i < memberResults.size(); i++) {
                    System.out.println(memberResults.get(i));
                }
                currSceneID = 0;
            } else if(currSceneID == 8) {
                System.out.println(currScene.getPrompt());
                memberResults = memberInsertionSort(sortBy, members);
                for(int i = 0; i < memberResults.size(); i++) {
                    System.out.println(memberResults.get(i));
                }
                currSceneID = 0;
            } else if(currSceneID == 9) {
                System.out.print(currScene.getPrompt());
                scnr.nextLine();
                writtenInput = scnr.nextLine();
                sortBy = "ID";
                memberResults = memberBinarySearch(sortBy, writtenInput, memberInsertionSort(sortBy, members));
                if(memberResults.size() == 0) {
                    System.out.println("Failed to find member.");
                    currSceneID = 0;
                } else if(memberResults.get(0).getCurrBooks() == memberResults.get(0).getMaxBooks()) {
                    System.out.println("Member has too many books checked out.");
                    currSceneID = 0;
                } else {
                    currSceneID = 10;
                }
            } else if(currSceneID ==10) {
                System.out.print(currScene.getPrompt());
                writtenInput = scnr.nextLine();
                sortBy = "ISBN";
                bookResults = bookBinarySearch(sortBy, writtenInput, bookInsertionSort(sortBy, books));
                if(bookResults.size() == 0) {
                    System.out.println("Failed to find book.");
                } else if(!bookResults.get(0).getStatus().getAvailability().equals("Available")) {
                    System.out.println("Book is not available.");
                } else {
                    System.out.print("Enter current date (e.g. 1 Jan 2020): ");
                    String currDate = scnr.nextLine();
                    System.out.print("Enter due date (e.g. 1 Jan 2020): ");
                    String dueDate = scnr.nextLine();
                    bookResults.get(0).setStatus("Checked out", memberResults.get(0), currDate, dueDate);
                    memberResults.get(0).setCurrBooks(memberResults.get(0).getCurrBooks() + 1);
                    System.out.println("Book checked out successfully!");
                }
                currSceneID = 0;
            } else {
                System.out.println(currScene.getPrompt());
                for(int i = 0; i < currScene.getChoices().length; i++) {
                    if(i < 10) {
                        System.out.print(" ");
                    }
                    System.out.println(i + ": " + currScene.getChoices()[i]);
                }
                System.out.println("");
                input = scnr.nextInt();
                if(currSceneID == 1 || currSceneID == 3 || currSceneID == 5 || currSceneID == 7) {
                    sortBy = currScene.getChoices()[input];
                }
                currSceneID = currScene.getNextIDs()[input];
            }
            System.out.println("\n\n\n------------------------------\n\n\n");
        }
        scnr.close();
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

                // Create a new Book object and add it to the list
                Book book = new Book(title, author, isbn, publicationYear, genre, language);
                books.add(book);
                count++;
            }

            // Display the total number of books loaded
            System.out.println("Total books loaded: " + count);
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + filename);
            return;
        }
    }

    // Modified code from Assessment: Car Data Analyzer
    private static void loadMembers(String filename) {
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
                String id = parts[0];
                String name = parts[1];
                String email = parts[2];
                String phone = parts[3];
                String address = parts[4];
                String membershipType = parts[5];
                String joinDate = parts[6];

                // Create a new Member object and add it to the list
                Member member = new Member(id, name, email, phone, address, membershipType, joinDate);
                members.add(member);
                count++;
            }

            // Display the total number of members loaded
            System.out.println("Total members loaded: " + count);
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + filename);
            return;
        }
    }

    // Modified code from Assessment: Car Data Analyzer
    private static void loadScenes(String filename) {
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
                int sceneID = Integer.parseInt(parts[0]);
                String prompt = parts[1];
                String[] choices = parts[2].split(";");
                int[] nextIDs = Arrays.stream(parts[3].split(";")).mapToInt(Integer::parseInt).toArray();

                // Create a new Member object and add it to the list
                Scene scene = new Scene(sceneID, prompt, choices, nextIDs);
                scenes.add(scene);
                count++;
            }

            // Display the total number of scenes loaded
            System.out.println("Total scenes loaded: " + count);
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + filename);
            return;
        }
    }

    // Modified code from Assessment: Car Data Analyzer
    // Method to sort the list of books using insertion sort by title, author, ISBN, publication year, genre or language
    private static ArrayList<Book> bookInsertionSort(String sortBy, ArrayList<Book> books) {
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

    // Modified code from Assessment: Car Data Analyzer
    // Method to perform binary search on the sorted list for matching books
    private static ArrayList<Book> bookBinarySearch(String sortBy, String search, ArrayList<Book> books) {
        // List to store matching results
        ArrayList<Book> results = new ArrayList<Book>();
        // Working copy of the list
        ArrayList<Book> list = new ArrayList<Book>(books);
        int low = 0;
        int high = list.size() - 1;

        // Binary search loop
        while(high >= low) {
            int mid = (low + high) / 2;
            if(sortBy.equalsIgnoreCase("author")) {
                // Search by author
                if(search.compareToIgnoreCase(list.get(mid).getAuthor().getLastName()) < 0) {
                    high = mid - 1;
                } else if(search.equalsIgnoreCase(list.get(mid).getAuthor().getLastName())) {
                    // Found a match, add to results and remove from list to find all occurrences
                    results.add(list.get(mid));
                    list.remove(mid);
                    // Reset search bounds
                    low = 0;
                    high = list.size() - 1;
                } else {
                    low = mid + 1;
                }
            } else if(sortBy.equalsIgnoreCase("ISBN")) {
                // Search by ISBN
                if(search.compareToIgnoreCase(list.get(mid).getISBN()) < 0) {
                    high = mid - 1;
                } else if(search.equalsIgnoreCase(list.get(mid).getISBN())) {
                    // Found a match, add to results and remove from list to find all occurrences
                    results.add(list.get(mid));
                    list.remove(mid);
                    // Reset search bounds
                    low = 0;
                    high = list.size() - 1;
                } else {
                    low = mid + 1;
                }
            } else if(sortBy.equalsIgnoreCase("publication year") || sortBy.equalsIgnoreCase("publicationyear")) {
                // Search by publication year
                if(Integer.parseInt(search) < list.get(mid).getPublicationYear()) {
                    high = mid - 1;
                } else if(Integer.parseInt(search) == list.get(mid).getPublicationYear()) {
                    // Found a match, add to results and remove from list to find all occurrences
                    results.add(list.get(mid));
                    list.remove(mid);
                    // Reset search bounds
                    low = 0;
                    high = list.size() - 1;
                } else {
                    low = mid + 1;
                }
            } else if(sortBy.equalsIgnoreCase("genre")) {
                // Search by genre
                if(search.compareToIgnoreCase(list.get(mid).getGenre()) < 0) {
                    high = mid - 1;
                } else if(search.equalsIgnoreCase(list.get(mid).getGenre())) {
                    // Found a match, add to results and remove from list to find all occurrences
                    results.add(list.get(mid));
                    list.remove(mid);
                    // Reset search bounds
                    low = 0;
                    high = list.size() - 1;
                } else {
                    low = mid + 1;
                }
            } else if(sortBy.equalsIgnoreCase("language")) {
                // Search by language
                if(search.compareToIgnoreCase(list.get(mid).getLanguage()) < 0) {
                    high = mid - 1;
                } else if(search.equalsIgnoreCase(list.get(mid).getLanguage())) {
                    // Found a match, add to results and remove from list to find all occurrences
                    results.add(list.get(mid));
                    list.remove(mid);
                    // Reset search bounds
                    low = 0;
                    high = list.size() - 1;
                } else {
                    low = mid + 1;
                }
            } else {
                // Search by title
                if(search.compareToIgnoreCase(list.get(mid).getTitle()) < 0) {
                    high = mid - 1;
                } else if(search.equalsIgnoreCase(list.get(mid).getTitle())) {
                    // Found a match, add to results and remove from list
                    results.add(list.get(mid));
                    list.remove(mid);
                    // Reset search bounds
                    low = 0;
                    high = list.size() - 1;
                } else {
                    low = mid + 1;
                }
            }
        }
        return results;
    }

    // Modified code from Assessment: Car Data Analyzer
    // Method to sort the list of members using insertion sort by ID, name, email, phone, address or join date
    private static ArrayList<Member> memberInsertionSort(String sortBy, ArrayList<Member> members) {
        // Outer loop for each element in the list
        for(int i = 0; i < members.size(); i++) {
            if(sortBy.equalsIgnoreCase("name")) {
                // Inner loop to sort by name
                for(int j = i; j > 0 && members.get(j).getName().compareTo(members.get(j - 1).getName()) < 0; j--) {
                    // Swap elements if out of order
                    Member temp = members.get(j);
                    members.set(j, members.get(j - 1));
                    members.set(j - 1, temp);
                }
            } else if(sortBy.equalsIgnoreCase("email")) {
                // Inner loop to sort by email
                for(int j = i; j > 0 && members.get(j).getEmail().compareTo(members.get(j - 1).getEmail()) < 0; j--) {
                    // Swap elements if out of order
                    Member temp = members.get(j);
                    members.set(j, members.get(j - 1));
                    members.set(j - 1, temp);
                }
            } else if(sortBy.equalsIgnoreCase("phone")) {
                // Inner loop to sort by phone
                for(int j = i; j > 0 && members.get(j).getPhone().compareTo(members.get(j - 1).getPhone()) < 0; j--) {
                    // Swap elements if out of order
                    Member temp = members.get(j);
                    members.set(j, members.get(j - 1));
                    members.set(j - 1, temp);
                }
            } else if(sortBy.equalsIgnoreCase("address")) {
                // Inner loop to sort by address
                for(int j = i; j > 0 && members.get(j).getAddress().compareTo(members.get(j - 1).getAddress()) < 0; j--) {
                    // Swap elements if out of order
                    Member temp = members.get(j);
                    members.set(j, members.get(j - 1));
                    members.set(j - 1, temp);
                }
            } else if(sortBy.equalsIgnoreCase("join date")) {
                // Inner loop to sort by join date
                for(int j = i; j > 0 && members.get(j).getJoinDate().compareTo(members.get(j - 1).getJoinDate()) < 0; j--) {
                    // Swap elements if out of order
                    Member temp = members.get(j);
                    members.set(j, members.get(j - 1));
                    members.set(j - 1, temp);
                }
            } else {
                // Inner loop to sort by ID
                for(int j = i; j > 0 && members.get(j).getID().compareTo(members.get(j - 1).getID()) < 0; j--) {
                    // Swap elements if out of order
                    Member temp = members.get(j);
                    members.set(j, members.get(j - 1));
                    members.set(j - 1, temp);
                }
            }
        }
        return members;
    }

    // Modified code from Assessment: Car Data Analyzer
    // Method to perform binary search on the sorted list for matching members
    private static ArrayList<Member> memberBinarySearch(String sortBy, String search, ArrayList<Member> members) {
        // List to store matching results
        ArrayList<Member> results = new ArrayList<Member>();
        // Working copy of the list
        ArrayList<Member> list = new ArrayList<Member>(members);
        int low = 0;
        int high = list.size() - 1;

        // Binary search loop
        while(high >= low) {
            int mid = (low + high) / 2;
            if(sortBy.equalsIgnoreCase("name")) {
                // Search by name
                if(search.compareToIgnoreCase(list.get(mid).getName()) < 0) {
                    high = mid - 1;
                } else if(search.equalsIgnoreCase(list.get(mid).getName())) {
                    // Found a match, add to results and remove from list to find all occurrences
                    results.add(list.get(mid));
                    list.remove(mid);
                    // Reset search bounds
                    low = 0;
                    high = list.size() - 1;
                } else {
                    low = mid + 1;
                }
            } else if(sortBy.equalsIgnoreCase("email")) {
                // Search by email
                if(search.compareToIgnoreCase(list.get(mid).getEmail()) < 0) {
                    high = mid - 1;
                } else if(search.equalsIgnoreCase(list.get(mid).getEmail())) {
                    // Found a match, add to results and remove from list to find all occurrences
                    results.add(list.get(mid));
                    list.remove(mid);
                    // Reset search bounds
                    low = 0;
                    high = list.size() - 1;
                } else {
                    low = mid + 1;
                }
            } else if(sortBy.equalsIgnoreCase("phone")) {
                // Search by phone
                if(search.compareToIgnoreCase(list.get(mid).getPhone()) < 0) {
                    high = mid - 1;
                } else if(search.equalsIgnoreCase(list.get(mid).getPhone())) {
                    // Found a match, add to results and remove from list to find all occurrences
                    results.add(list.get(mid));
                    list.remove(mid);
                    // Reset search bounds
                    low = 0;
                    high = list.size() - 1;
                } else {
                    low = mid + 1;
                }
            } else if(sortBy.equalsIgnoreCase("address")) {
                // Search by address
                if(search.compareToIgnoreCase(list.get(mid).getAddress()) < 0) {
                    high = mid - 1;
                } else if(search.equalsIgnoreCase(list.get(mid).getAddress())) {
                    // Found a match, add to results and remove from list to find all occurrences
                    results.add(list.get(mid));
                    list.remove(mid);
                    // Reset search bounds
                    low = 0;
                    high = list.size() - 1;
                } else {
                    low = mid + 1;
                }
            } else if(sortBy.equalsIgnoreCase("join date")) {
                // Search by join date
                if(search.compareToIgnoreCase(list.get(mid).getJoinDate()) < 0) {
                    high = mid - 1;
                } else if(search.equalsIgnoreCase(list.get(mid).getJoinDate())) {
                    // Found a match, add to results and remove from list to find all occurrences
                    results.add(list.get(mid));
                    list.remove(mid);
                    // Reset search bounds
                    low = 0;
                    high = list.size() - 1;
                } else {
                    low = mid + 1;
                }
            } else {
                // Search by ID
                if(search.compareToIgnoreCase(list.get(mid).getID()) < 0) {
                    high = mid - 1;
                } else if(search.equalsIgnoreCase(list.get(mid).getID())) {
                    // Found a match, add to results and remove from list
                    results.add(list.get(mid));
                    list.remove(mid);
                    // Reset search bounds
                    low = 0;
                    high = list.size() - 1;
                } else {
                    low = mid + 1;
                }
            }
        }
        return results;
    }
}