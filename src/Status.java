package src;
// John Chiero
// 4/20/2026
// Status class

public class Status {
    private String availability;
    private Member member;
    private String borrowDate;
    private String dueDate;
    private Member noMember = new Member(null, null, null, null, null, null, null);

    public Status() {
        this.availability = "Available";
        this.member = noMember;
        this.borrowDate = null;
        this.dueDate = null;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public String getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(String borrowDate) {
        this.borrowDate = borrowDate;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    @Override
    public String toString() {
        if(availability.equals("Available")) {
            return availability;
        } else {
            return availability + ", " + ", " + member + ", " + borrowDate + ", " + dueDate;
        }
    }
}