package src;
// John Chiero
// 4/21/2026
// Author class - Represents book author information

public class Author {
    private String firstName;
    private String lastName;
    private String middleInitial;

    public Author(String firstName, String lastName, String middleInitial) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleInitial = middleInitial;
    }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getMiddleInitial() { return middleInitial; }
    public void setMiddleInitial(String middleInitial) { this.middleInitial = middleInitial; }

    @Override
    public String toString() {
        if (middleInitial != null)
            return firstName + " " + middleInitial + ". " + lastName;
        return firstName + " " + lastName;
    }
}