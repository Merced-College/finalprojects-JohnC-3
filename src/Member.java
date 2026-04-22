package src;
// John Chiero
// 4/21/2026
// Member class - Represents a library member

public class Member {
    private String id;
    private String name;
    private String email;
    private String phone;
    private String address;
    private String membershipType;
    private String joinDate;
    private String expirationDate;
    private int maxBooks;
    private int currBooks;
    private Book reservation;
    private boolean hasReservation;

    public Member(String id, String name, String email, String phone,
                  String address, String membershipType, String joinDate) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.membershipType = membershipType;
        this.joinDate = joinDate;
        this.currBooks = 0;
        this.reservation = null;
        this.hasReservation = false;

        // Calculate expiration date (1 year after join date)
        if (joinDate != null) {
            String[] parts = joinDate.split(" ");
            parts[2] = String.valueOf(Integer.parseInt(parts[2]) + 1);
            this.expirationDate = parts[0] + " " + parts[1] + " " + parts[2];
        } else {
            this.expirationDate = null;
        }

        // Set maximum books allowed based on membership type
        if (membershipType == null) {
            this.maxBooks = 0;
        } else if (membershipType.equals("Standard")) {
            this.maxBooks = 2;
        } else if (membershipType.equals("Student")) {
            this.maxBooks = 3;
        } else if (membershipType.equals("Faculty")) {
            this.maxBooks = 4;
        } else if (membershipType.equals("Premium")) {
            this.maxBooks = 5;
        } else {
            this.maxBooks = 0;
        }
    }

    // Getters and Setters
    public String getID() { return id; }
    public void setID(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getMembershipType() { return membershipType; }
    public void setMembershipType(String membershipType) { this.membershipType = membershipType; }

    public String getJoinDate() { return joinDate; }
    public void setJoinDate(String joinDate) { this.joinDate = joinDate; }

    public String getExpirationDate() { return expirationDate; }
    public void setExpirationDate(String expirationDate) { this.expirationDate = expirationDate; }

    public int getMaxBooks() { return maxBooks; }
    public void setMaxBooks(int maxBooks) { this.maxBooks = maxBooks; }

    public int getCurrBooks() { return currBooks; }
    public void setCurrBooks(int currBooks) { this.currBooks = currBooks; }

    public Book getReservation() { return reservation; }
    public void setReservation(Book reservation) {
        this.reservation = reservation;
        setHasReservation(reservation != null);
    }

    public boolean getHasReservation() { return hasReservation; }
    public void setHasReservation(boolean hasReservation) { this.hasReservation = hasReservation; }

    @Override
    public String toString() {
        return id + ", " + name + ", " + email + ", " + phone + ", " + address +
               ", " + membershipType + ", " + joinDate + ", " + expirationDate +
               ", " + currBooks + ", " + maxBooks;
    }
}