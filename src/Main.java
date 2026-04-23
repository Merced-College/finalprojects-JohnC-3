package src;
// John Chiero
// 4/22/2026
// Main class - Entry point of the Library Management System
/**
 * All code written by John Chiero or recycled from previous assignments, with some minor help from GitHub Copilot and Grok.
 * Nearly all comments written by Grok.
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Main {

    // Static lists to hold all data in memory
    private static ArrayList<Book> books = new ArrayList<Book>();
    private static ArrayList<Member> members = new ArrayList<Member>();
    private static ArrayList<Scene> scenes = new ArrayList<Scene>();

    public static void main(String[] args) {
        setup();           // Load all data from files and display initial info
        libraryManager();  // Start the main interactive menu system
    }

    /**
     * Loads books, members, and scenes from their respective text files.
     * Prints the loaded data for verification.
     */
    private static void setup() {
        loadBooks("docs/Books.txt");
        for (int i = 0; i < books.size(); i++) {
            System.out.println(books.get(i));
        }
        System.out.println("");

        loadMembers("docs/Members.txt");
        for (int i = 0; i < members.size(); i++) {
            System.out.println(members.get(i));
        }
        System.out.println("");

        loadScenes("docs/Scenes.txt");
        System.out.println("\n\n\n------------------------------\n\n\n");
    }

    /**
     * Core loop of the library management system.
     * Uses a scene-based navigation system (like a state machine) to handle
     * different menus and operations such as searching, checking out/in books,
     * adding/removing items, etc.
     */
    private static void libraryManager() {
        Scanner scnr = new Scanner(System.in);
        int input;
        String writtenInput;
        int currSceneID = 0;           // Current menu/scene being displayed
        Scene currScene;
        String sortBy = "";            // Field to sort or search by
        ArrayList<Book> bookResults;
        ArrayList<Member> memberResults = new ArrayList<Member>();

        System.out.println("Welcome to the Library Manager!\n");

        while (true) {
            if (currSceneID != -1) {
                currScene = scenes.get(currSceneID);
            } else {
                break;                 // Exit condition
            }

            // === Special handling for different scenes (menus/actions) ===

            if (currSceneID == 2) { // Search Books
                System.out.print(currScene.getPrompt());
                scnr.nextLine(); // Consume leftover newline
                writtenInput = scnr.nextLine();
                // Sort then perform binary search
                bookResults = bookBinarySearch(sortBy, writtenInput,
                        bookInsertionSort(sortBy, books));

                System.out.println("\nSearch results:");
                for (int i = 0; i < bookResults.size(); i++) {
                    System.out.println(bookResults.get(i));
                }
                currSceneID = 0; // Return to main menu
            }
            else if (currSceneID == 4) { // List All Books (sorted)
                System.out.println(currScene.getPrompt());
                bookResults = bookInsertionSort(sortBy, books);
                for (int i = 0; i < bookResults.size(); i++) {
                    System.out.println(bookResults.get(i));
                }
                currSceneID = 0;
            }
            else if (currSceneID == 6) { // Search Members
                System.out.print(currScene.getPrompt());
                scnr.nextLine();
                writtenInput = scnr.nextLine();
                memberResults = memberBinarySearch(sortBy, writtenInput,
                        memberInsertionSort(sortBy, members));

                System.out.println("\nSearch results:");
                for (int i = 0; i < memberResults.size(); i++) {
                    System.out.println(memberResults.get(i));
                }
                currSceneID = 0;
            }
            else if (currSceneID == 8) { // List All Members (sorted)
                System.out.println(currScene.getPrompt());
                memberResults = memberInsertionSort(sortBy, members);
                for (int i = 0; i < memberResults.size(); i++) {
                    System.out.println(memberResults.get(i));
                }
                currSceneID = 0;
            }
            else if (currSceneID == 9) { // Prepare to Check Out a Book - Find Member
                System.out.print(currScene.getPrompt());
                scnr.nextLine();
                writtenInput = scnr.nextLine();
                sortBy = "ID";
                memberResults = memberBinarySearch(sortBy, writtenInput,
                        memberInsertionSort(sortBy, members));

                if (memberResults.size() == 0) {
                    System.out.println("Failed to find member.");
                    currSceneID = 0;
                } else if (memberResults.get(0).getCurrBooks() == memberResults.get(0).getMaxBooks()) {
                    System.out.println("Member has too many books checked out.");
                    currSceneID = 0;
                } else {
                    currSceneID = 10; // Proceed to book selection
                }
            }
            else if (currSceneID == 10) { // Check Out Book - Find Book and Process
                System.out.print(currScene.getPrompt());
                writtenInput = scnr.nextLine();
                sortBy = "ISBN";
                bookResults = bookBinarySearch(sortBy, writtenInput,
                        bookInsertionSort(sortBy, books));

                if (bookResults.size() == 0) {
                    System.out.println("Failed to find book.");
                }
                else if (!bookResults.get(0).getStatus().getAvailability().equals("Available")) {
                    // Handle reserved books (only the reserving member can check out)
                    if (bookResults.get(0).getStatus().getAvailability().equals("Reserved")) {
                        if (bookResults.get(0).getStatus().getMember().getID().equals(memberResults.get(0).getID())) {
                            System.out.print("Enter current date (e.g. 1 Jan 2020): ");
                            String currDate = scnr.nextLine();
                            System.out.print("Enter due date (e.g. 1 Jan 2020): ");
                            String dueDate = scnr.nextLine();
                            bookResults.get(0).setStatus("Checked out", memberResults.get(0), currDate, dueDate);
                            memberResults.get(0).setCurrBooks(memberResults.get(0).getCurrBooks() + 1);
                            System.out.println("Book checked out successfully!");
                            currSceneID = 0;
                            continue;
                        }
                    }
                    System.out.println("Book is not available.");
                }
                else {
                    // Book is available - check it out
                    System.out.print("Enter current date (e.g. 1 Jan 2020): ");
                    String currDate = scnr.nextLine();
                    System.out.print("Enter due date (e.g. 1 Jan 2020): ");
                    String dueDate = scnr.nextLine();
                    bookResults.get(0).setStatus("Checked out", memberResults.get(0), currDate, dueDate);
                    memberResults.get(0).setCurrBooks(memberResults.get(0).getCurrBooks() + 1);
                    System.out.println("Book checked out successfully!");
                }
                currSceneID = 0;
            }
            else if (currSceneID == 11) { // Prepare to Check In a Book - Find Member
                System.out.print(currScene.getPrompt());
                scnr.nextLine();
                writtenInput = scnr.nextLine();
                sortBy = "ID";
                memberResults = memberBinarySearch(sortBy, writtenInput,
                        memberInsertionSort(sortBy, members));

                if (memberResults.size() == 0) {
                    System.out.println("Failed to find member.");
                    currSceneID = 0;
                } else if (memberResults.get(0).getCurrBooks() == 0) {
                    System.out.println("Member has no books checked out.");
                    currSceneID = 0;
                } else {
                    currSceneID = 12;
                }
            }
            else if (currSceneID == 12) { // Check In Book
                System.out.print(currScene.getPrompt());
                writtenInput = scnr.nextLine();
                sortBy = "ISBN";
                bookResults = bookBinarySearch(sortBy, writtenInput,
                        bookInsertionSort(sortBy, books));

                if (bookResults.size() == 0) {
                    System.out.println("Failed to find book.");
                }
                else if (!bookResults.get(0).getStatus().getAvailability().equals("Checked out")) {
                    System.out.println("Member does not have this book checked out.");
                }
                else if (!bookResults.get(0).getStatus().getMember().getID().equals(memberResults.get(0).getID())) {
                    System.out.println("Member does not have this book checked out.");
                }
                else {
                    // Return book - if someone is waiting in queue, reserve it for them
                    if (bookResults.get(0).getQueue().size() > 0) {
                        bookResults.get(0).setStatus("Reserved",
                                bookResults.get(0).getQueue().peek(), null, null);
                        bookResults.get(0).removeFromQueue();
                    } else {
                        bookResults.get(0).setStatus("Available", null, null, null);
                    }
                    memberResults.get(0).setCurrBooks(memberResults.get(0).getCurrBooks() - 1);
                    System.out.println("Book checked in successfully!");
                }
                currSceneID = 0;
            }
            else if (currSceneID == 13) { // Prepare to Reserve a Book - Find Member
                System.out.print(currScene.getPrompt());
                scnr.nextLine();
                writtenInput = scnr.nextLine();
                sortBy = "ID";
                memberResults = memberBinarySearch(sortBy, writtenInput,
                        memberInsertionSort(sortBy, members));

                if (memberResults.size() == 0) {
                    System.out.println("Failed to find member.");
                    currSceneID = 0;
                } else if (memberResults.get(0).getHasReservation()) {
                    System.out.println("Member already has a reservation.");
                    currSceneID = 0;
                } else {
                    currSceneID = 14;
                }
            }
            else if (currSceneID == 14) { // Reserve a Book
                System.out.print(currScene.getPrompt());
                writtenInput = scnr.nextLine();
                sortBy = "ISBN";
                bookResults = bookBinarySearch(sortBy, writtenInput,
                        bookInsertionSort(sortBy, books));

                if (bookResults.size() == 0) {
                    System.out.println("Failed to find book.");
                }
                else if (!(bookResults.get(0).getStatus().getAvailability().equals("Checked out") ||
                           bookResults.get(0).getStatus().getAvailability().equals("Reserved"))) {
                    System.out.println("Book is available, no need to reserve.");
                }
                else if (bookResults.get(0).getStatus().getMember().getID().equals(memberResults.get(0).getID())) {
                    System.out.println("Member already has this book checked out.");
                }
                else {
                    bookResults.get(0).addToQueue(memberResults.get(0));
                    memberResults.get(0).setReservation(bookResults.get(0));
                    System.out.println("Book reserved/queued successfully!");
                }
                currSceneID = 0;
            }
            else if (currSceneID == 15) { // Add New Book
                System.out.println(currScene.getPrompt());
                scnr.nextLine();
                System.out.print("Title: ");
                String title = scnr.nextLine();
                System.out.print("Author (First Last): ");
                Author author = new Author(scnr.next(), scnr.next(), null);
                scnr.nextLine();
                System.out.print("ISBN: ");
                String isbn = scnr.nextLine();
                System.out.print("Publication Year: ");
                int publicationYear = scnr.nextInt();
                scnr.nextLine();
                System.out.print("Genre: ");
                String genre = scnr.nextLine();
                System.out.print("Language: ");
                String language = scnr.nextLine();

                Book newBook = new Book(title, author, isbn, publicationYear, genre, language);
                books.add(newBook);
                System.out.println("Book added successfully!");
                currSceneID = 0;
            }
            else if (currSceneID == 16) { // Remove Book
                System.out.print(currScene.getPrompt());
                scnr.nextLine();
                sortBy = "ISBN";
                bookResults = bookBinarySearch(sortBy, scnr.nextLine(),
                        bookInsertionSort(sortBy, books));

                if (bookResults.size() == 0) {
                    System.out.println("Failed to find book.");
                } else {
                    System.out.print("Are you sure you want to remove " +
                            bookResults.get(0).getTitle() + " by " +
                            bookResults.get(0).getAuthor() + "? (Y/N): ");
                    String confirmation = scnr.nextLine();
                    if (confirmation.equalsIgnoreCase("Y")) {
                        books.remove(bookResults.get(0));
                        System.out.println("Book removed successfully!");
                    } else {
                        System.out.println("Book removal cancelled.");
                    }
                }
                currSceneID = 0;
            }
            else if (currSceneID == 17) { // Add New Member
                System.out.println(currScene.getPrompt());
                scnr.nextLine();
                System.out.print("ID: ");
                String id = scnr.nextLine();
                System.out.print("Name (First Last): ");
                String name = scnr.nextLine();
                System.out.print("Email: ");
                String email = scnr.nextLine();
                System.out.print("Phone: ");
                String phone = scnr.nextLine();
                System.out.print("Address: ");
                String address = scnr.nextLine();
                System.out.print("Membership type: ");
                String membershipType = scnr.nextLine();
                System.out.print("Join date (e.g. 1 Jan 2020): ");
                String joinDate = scnr.nextLine();

                Member newMember = new Member(id, name, email, phone, address, membershipType, joinDate);
                members.add(newMember);
                System.out.println("Member added successfully!");
                currSceneID = 0;
            }
            else if (currSceneID == 18) { // Remove Member
                System.out.print(currScene.getPrompt());
                scnr.nextLine();
                sortBy = "ID";
                memberResults = memberBinarySearch(sortBy, scnr.nextLine(),
                        memberInsertionSort(sortBy, members));

                if (memberResults.size() == 0) {
                    System.out.println("Failed to find member.");
                } else {
                    System.out.print("Are you sure you want to remove " +
                            memberResults.get(0).getName() + "? (Y/N): ");
                    String confirmation = scnr.nextLine();
                    if (confirmation.equalsIgnoreCase("Y")) {
                        members.remove(memberResults.get(0));
                        System.out.println("Member removed successfully!");
                    } else {
                        System.out.println("Member removal cancelled.");
                    }
                }
                currSceneID = 0;
            }
            else {
                // Default scene: Show menu choices
                System.out.println(currScene.getPrompt());
                for (int i = 0; i < currScene.getChoices().length; i++) {
                    if (i < 10) System.out.print(" ");
                    System.out.println(i + ": " + currScene.getChoices()[i]);
                }
                System.out.println("");

                input = scnr.nextInt();
                // Remember the sort field for sorting scenes
                if (currSceneID == 1 || currSceneID == 3 || currSceneID == 5 || currSceneID == 7) {
                    sortBy = currScene.getChoices()[input];
                }
                currSceneID = currScene.getNextIDs()[input]; // Move to next scene
            }

            System.out.println("\n\n\n------------------------------\n\n\n");
        }
        scnr.close();
    }

    // ===================================================================
    // Data Loading Methods
    // ===================================================================

    /**
     * Loads book data from a text file and populates the books list.
     * Skips header and empty lines.
     */
    private static void loadBooks(String filename) {
        try (Scanner scnr = new Scanner(new File(filename))) {
            int count = 0;
            scnr.nextLine(); // Skip header

            while (scnr.hasNextLine()) {
                String line = scnr.nextLine();
                if (line.isEmpty()) continue;

                String[] parts = line.split(",");
                String title = parts[0];
                String isbn = parts[2];
                int publicationYear = Integer.parseInt(parts[3]);
                String genre = parts[4];
                String language = parts[5];

                // Parse author (First [Middle] Last)
                String[] authorParts = parts[1].split(" ");
                String firstName = authorParts[0];
                String middleInitial = null;
                String lastName;
                if (authorParts.length == 2) {
                    lastName = authorParts[1];
                } else {
                    middleInitial = authorParts[1].substring(0, 1);
                    lastName = authorParts[2];
                }

                Author author = new Author(firstName, lastName, middleInitial);
                Book book = new Book(title, author, isbn, publicationYear, genre, language);
                books.add(book);
                count++;
            }
            System.out.println("Total books loaded: " + count);
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + filename);
        }
    }

    /**
     * Loads member data from a text file and populates the members list.
     */
    private static void loadMembers(String filename) {
        try (Scanner scnr = new Scanner(new File(filename))) {
            int count = 0;
            scnr.nextLine(); // Skip header

            while (scnr.hasNextLine()) {
                String line = scnr.nextLine();
                if (line.isEmpty()) continue;

                String[] parts = line.split(",");
                String id = parts[0];
                String name = parts[1];
                String email = parts[2];
                String phone = parts[3];
                String address = parts[4];
                String membershipType = parts[5];
                String joinDate = parts[6];

                Member member = new Member(id, name, email, phone, address, membershipType, joinDate);
                members.add(member);
                count++;
            }
            System.out.println("Total members loaded: " + count);
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + filename);
        }
    }

    /**
     * Loads scene (menu) definitions from a text file.
     * Each scene defines prompt, choices, and next scene IDs.
     */
    private static void loadScenes(String filename) {
        try (Scanner scnr = new Scanner(new File(filename))) {
            int count = 0;
            scnr.nextLine(); // Skip header

            while (scnr.hasNextLine()) {
                String line = scnr.nextLine();
                if (line.isEmpty()) continue;

                String[] parts = line.split(",");
                int sceneID = Integer.parseInt(parts[0]);
                String prompt = parts[1];
                String[] choices = parts[2].split(";");
                int[] nextIDs = Arrays.stream(parts[3].split(";"))
                                      .mapToInt(Integer::parseInt)
                                      .toArray();

                Scene scene = new Scene(sceneID, prompt, choices, nextIDs);
                scenes.add(scene);
                count++;
            }
            System.out.println("Total scenes loaded: " + count);
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + filename);
        }
    }

    // ===================================================================
    // Sorting and Searching Methods (Modified from Car Data Analyzer)
    // ===================================================================

    /**
     * Sorts books using Insertion Sort based on the specified field.
     * Returns the (modified) sorted list.
     */
    private static ArrayList<Book> bookInsertionSort(String sortBy, ArrayList<Book> books) {
        for (int i = 0; i < books.size(); i++) {
            if (sortBy.equalsIgnoreCase("author")) {
                for (int j = i; j > 0 &&
                     (books.get(j).getAuthor().getLastName() + books.get(j).getAuthor().getFirstName())
                             .compareTo(books.get(j - 1).getAuthor().getLastName() +
                                        books.get(j - 1).getAuthor().getFirstName()) < 0; j--) {
                    Book temp = books.get(j);
                    books.set(j, books.get(j - 1));
                    books.set(j - 1, temp);
                }
            } else if (sortBy.equalsIgnoreCase("ISBN")) {
                for (int j = i; j > 0 &&
                     books.get(j).getISBN().compareTo(books.get(j - 1).getISBN()) < 0; j--) {
                    Book temp = books.get(j);
                    books.set(j, books.get(j - 1));
                    books.set(j - 1, temp);
                }
            } else if (sortBy.equalsIgnoreCase("publication year") ||
                       sortBy.equalsIgnoreCase("publicationyear")) {
                for (int j = i; j > 0 &&
                     books.get(j).getPublicationYear() < books.get(j - 1).getPublicationYear(); j--) {
                    Book temp = books.get(j);
                    books.set(j, books.get(j - 1));
                    books.set(j - 1, temp);
                }
            } else if (sortBy.equalsIgnoreCase("genre")) {
                for (int j = i; j > 0 &&
                     books.get(j).getGenre().compareTo(books.get(j - 1).getGenre()) < 0; j--) {
                    Book temp = books.get(j);
                    books.set(j, books.get(j - 1));
                    books.set(j - 1, temp);
                }
            } else if (sortBy.equalsIgnoreCase("language")) {
                for (int j = i; j > 0 &&
                     books.get(j).getLanguage().compareTo(books.get(j - 1).getLanguage()) < 0; j--) {
                    Book temp = books.get(j);
                    books.set(j, books.get(j - 1));
                    books.set(j - 1, temp);
                }
            } else { // Default: sort by title
                for (int j = i; j > 0 &&
                     books.get(j).getTitle().compareTo(books.get(j - 1).getTitle()) < 0; j--) {
                    Book temp = books.get(j);
                    books.set(j, books.get(j - 1));
                    books.set(j - 1, temp);
                }
            }
        }
        return books;
    }

    /**
     * Performs a modified binary search on a sorted list of books.
     * Finds ALL matches for the given search term and returns them.
     */
    private static ArrayList<Book> bookBinarySearch(String sortBy, String search, ArrayList<Book> books) {
        ArrayList<Book> results = new ArrayList<Book>();
        ArrayList<Book> list = new ArrayList<Book>(books); // working copy
        int low = 0;
        int high = list.size() - 1;

        while (high >= low) {
            int mid = (low + high) / 2;

            if (sortBy.equalsIgnoreCase("author")) {
                if (search.compareToIgnoreCase(list.get(mid).getAuthor().getLastName()) < 0) {
                    high = mid - 1;
                } else if (search.equalsIgnoreCase(list.get(mid).getAuthor().getLastName())) {
                    results.add(list.get(mid));
                    list.remove(mid);
                    low = 0;
                    high = list.size() - 1;
                } else {
                    low = mid + 1;
                }
            } else if (sortBy.equalsIgnoreCase("ISBN")) {
                if (search.compareToIgnoreCase(list.get(mid).getISBN()) < 0) {
                    high = mid - 1;
                } else if (search.equalsIgnoreCase(list.get(mid).getISBN())) {
                    results.add(list.get(mid));
                    list.remove(mid);
                    low = 0;
                    high = list.size() - 1;
                } else {
                    low = mid + 1;
                }
            } else if (sortBy.equalsIgnoreCase("publication year") ||
                       sortBy.equalsIgnoreCase("publicationyear")) {
                int searchYear = Integer.parseInt(search);
                if (searchYear < list.get(mid).getPublicationYear()) {
                    high = mid - 1;
                } else if (searchYear == list.get(mid).getPublicationYear()) {
                    results.add(list.get(mid));
                    list.remove(mid);
                    low = 0;
                    high = list.size() - 1;
                } else {
                    low = mid + 1;
                }
            } else if (sortBy.equalsIgnoreCase("genre")) {
                if (search.compareToIgnoreCase(list.get(mid).getGenre()) < 0) {
                    high = mid - 1;
                } else if (search.equalsIgnoreCase(list.get(mid).getGenre())) {
                    results.add(list.get(mid));
                    list.remove(mid);
                    low = 0;
                    high = list.size() - 1;
                } else {
                    low = mid + 1;
                }
            } else if (sortBy.equalsIgnoreCase("language")) {
                if (search.compareToIgnoreCase(list.get(mid).getLanguage()) < 0) {
                    high = mid - 1;
                } else if (search.equalsIgnoreCase(list.get(mid).getLanguage())) {
                    results.add(list.get(mid));
                    list.remove(mid);
                    low = 0;
                    high = list.size() - 1;
                } else {
                    low = mid + 1;
                }
            } else { // Default: title
                if (search.compareToIgnoreCase(list.get(mid).getTitle()) < 0) {
                    high = mid - 1;
                } else if (search.equalsIgnoreCase(list.get(mid).getTitle())) {
                    results.add(list.get(mid));
                    list.remove(mid);
                    low = 0;
                    high = list.size() - 1;
                } else {
                    low = mid + 1;
                }
            }
        }
        return results;
    }

    /**
     * Sorts members using Insertion Sort based on the specified field.
     */
    private static ArrayList<Member> memberInsertionSort(String sortBy, ArrayList<Member> members) {
        for (int i = 0; i < members.size(); i++) {
            if (sortBy.equalsIgnoreCase("name")) {
                for (int j = i; j > 0 &&
                     members.get(j).getName().compareTo(members.get(j - 1).getName()) < 0; j--) {
                    Member temp = members.get(j);
                    members.set(j, members.get(j - 1));
                    members.set(j - 1, temp);
                }
            } else if (sortBy.equalsIgnoreCase("email")) {
                for (int j = i; j > 0 &&
                     members.get(j).getEmail().compareTo(members.get(j - 1).getEmail()) < 0; j--) {
                    Member temp = members.get(j);
                    members.set(j, members.get(j - 1));
                    members.set(j - 1, temp);
                }
            } else if (sortBy.equalsIgnoreCase("phone")) {
                for (int j = i; j > 0 &&
                     members.get(j).getPhone().compareTo(members.get(j - 1).getPhone()) < 0; j--) {
                    Member temp = members.get(j);
                    members.set(j, members.get(j - 1));
                    members.set(j - 1, temp);
                }
            } else if (sortBy.equalsIgnoreCase("address")) {
                for (int j = i; j > 0 &&
                     members.get(j).getAddress().compareTo(members.get(j - 1).getAddress()) < 0; j--) {
                    Member temp = members.get(j);
                    members.set(j, members.get(j - 1));
                    members.set(j - 1, temp);
                }
            } else if (sortBy.equalsIgnoreCase("join date")) {
                for (int j = i; j > 0 &&
                     members.get(j).getJoinDate().compareTo(members.get(j - 1).getJoinDate()) < 0; j--) {
                    Member temp = members.get(j);
                    members.set(j, members.get(j - 1));
                    members.set(j - 1, temp);
                }
            } else { // Default: ID
                for (int j = i; j > 0 &&
                     members.get(j).getID().compareTo(members.get(j - 1).getID()) < 0; j--) {
                    Member temp = members.get(j);
                    members.set(j, members.get(j - 1));
                    members.set(j - 1, temp);
                }
            }
        }
        return members;
    }

    /**
     * Performs a modified binary search on a sorted list of members.
     * Finds ALL matches for the given search term.
     */
    private static ArrayList<Member> memberBinarySearch(String sortBy, String search, ArrayList<Member> members) {
        ArrayList<Member> results = new ArrayList<Member>();
        ArrayList<Member> list = new ArrayList<Member>(members);
        int low = 0;
        int high = list.size() - 1;

        while (high >= low) {
            int mid = (low + high) / 2;

            if (sortBy.equalsIgnoreCase("name")) {
                if (search.compareToIgnoreCase(list.get(mid).getName()) < 0) high = mid - 1;
                else if (search.equalsIgnoreCase(list.get(mid).getName())) {
                    results.add(list.get(mid));
                    list.remove(mid);
                    low = 0; high = list.size() - 1;
                } else low = mid + 1;
            } else if (sortBy.equalsIgnoreCase("email")) {
                if (search.compareToIgnoreCase(list.get(mid).getEmail()) < 0) high = mid - 1;
                else if (search.equalsIgnoreCase(list.get(mid).getEmail())) {
                    results.add(list.get(mid));
                    list.remove(mid);
                    low = 0; high = list.size() - 1;
                } else low = mid + 1;
            } else if (sortBy.equalsIgnoreCase("phone")) {
                if (search.compareToIgnoreCase(list.get(mid).getPhone()) < 0) high = mid - 1;
                else if (search.equalsIgnoreCase(list.get(mid).getPhone())) {
                    results.add(list.get(mid));
                    list.remove(mid);
                    low = 0; high = list.size() - 1;
                } else low = mid + 1;
            } else if (sortBy.equalsIgnoreCase("address")) {
                if (search.compareToIgnoreCase(list.get(mid).getAddress()) < 0) high = mid - 1;
                else if (search.equalsIgnoreCase(list.get(mid).getAddress())) {
                    results.add(list.get(mid));
                    list.remove(mid);
                    low = 0; high = list.size() - 1;
                } else low = mid + 1;
            } else if (sortBy.equalsIgnoreCase("join date")) {
                if (search.compareToIgnoreCase(list.get(mid).getJoinDate()) < 0) high = mid - 1;
                else if (search.equalsIgnoreCase(list.get(mid).getJoinDate())) {
                    results.add(list.get(mid));
                    list.remove(mid);
                    low = 0; high = list.size() - 1;
                } else low = mid + 1;
            } else { // Default: ID
                if (search.compareToIgnoreCase(list.get(mid).getID()) < 0) high = mid - 1;
                else if (search.equalsIgnoreCase(list.get(mid).getID())) {
                    results.add(list.get(mid));
                    list.remove(mid);
                    low = 0; high = list.size() - 1;
                } else low = mid + 1;
            }
        }
        return results;
    }
}