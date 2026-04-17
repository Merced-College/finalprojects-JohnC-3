// John Chiero
// 4/16/2026
// Member class

public class Member {
    private int id;
    private String name;
    private String email;
    private String phone;
    private String address;
    private String membershipType;
    private int joinDate;
    private int expirationDate;
    private int maxBooks;
    private int currBooks;

    public Member(int id, String name, String email, String phone, String address, String membershipType, int joinDate, int expirationDate) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.membershipType = membershipType;
        this.joinDate = joinDate;
        this.expirationDate = expirationDate;
        this.currBooks = 0;

        if(membershipType.equals("Standard")) {
            this.maxBooks = 2;
        } else if(membershipType.equals("Student")) {
            this.maxBooks = 3;
        } else if(membershipType.equals("Faculty")) {
            this.maxBooks = 4;
        } else if(membershipType.equals("Premium")) {
            this.maxBooks = 5;
        } else {
            this.maxBooks = 0;
        }
    }

    public int getID() {
        return id;
    }

    public void setID(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMembershipType() {
        return membershipType;
    }

    public void setMembershipType(String membershipType) {
        this.membershipType = membershipType;
    }

    public int getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(int joinDate) {
        this.joinDate = joinDate;
    }

    public int getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(int expirationDate) {
        this.expirationDate = expirationDate;
    }

    public int getMaxBooks() {
        return maxBooks;
    }

    public void setMaxBooks(int maxBooks) {
        this.maxBooks = maxBooks;
    }

    public int getCurrBooks() {
        return currBooks;
    }

    public void setCurrBooks(int currBooks) {
        this.currBooks = currBooks;
    }
}