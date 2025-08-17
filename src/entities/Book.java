package entities;

public class Book {
    private int bookId;
    private String title;
    private int authorId;
    private int languageId;
    private String genre;
    private float rating;
    private boolean available; 

    public Book() {}

    public Book(int bookId, String title, int authorId, int languageId, String genre, float rating, boolean available) {
        this.bookId = bookId;
        this.title = title;
        this.authorId = authorId;
        this.languageId = languageId;
        this.genre = genre;
        this.rating = rating;
        this.available = available;
    }

    public int getBookId() { return bookId; }
    public void setBookId(int bookId) { this.bookId = bookId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public int getAuthorId() { return authorId; }
    public void setAuthorId(int authorId) { this.authorId = authorId; }

    public int getLanguageId() { return languageId; }
    public void setLanguageId(int languageId) { this.languageId = languageId; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

    public float getRating() { return rating; }
    public void setRating(float rating) { this.rating = rating; }

    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }

    public boolean isBookAvailable() {
        return available;
    }
}
