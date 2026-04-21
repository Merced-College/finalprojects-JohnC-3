package src;
// John Chiero
// 4/20/2026
// Book class

public class Book {
    private String title;
    private Author author;
    private String isbn;
    private int publicationYear;
    private String genre;
    private String language;
    private Status status;

    public Book(String title, Author author, String isbn, int publicationYear, String genre, String language) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.publicationYear = publicationYear;
        this.genre = genre;
        this.language = language;
        this.status = new Status();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public String getISBN() {
        return isbn;
    }

    public void setISBN(String isbn) {
        this.isbn = isbn;
    }

    public int getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(int publicationYear) {
        this.publicationYear = publicationYear;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(String availability, Member member, String borrowDate, String dueDate) {
        this.status.setAvailability(availability);
        this.status.setMember(member);
        this.status.setBorrowDate(borrowDate);
        this.status.setDueDate(dueDate);
    }

    @Override
    public String toString() {
        return title + ", " + author + ", " + isbn + ", " + publicationYear + ", " + genre + ", " + language + ", " + status;
    }
}