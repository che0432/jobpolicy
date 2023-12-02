package Bookmark;

public class BookmarkInfo {
    private int bookmarkId;
    private String title;
    private String institution;
    private String beginDate;
    private String endDate;
    private String URL;
    private String createAt;

    public BookmarkInfo(int bookmarkId, String title, String institution, String beginDate, String endDate, String URL, String createAt) {
        this.bookmarkId = bookmarkId;
        this.title = title;
        this.institution = institution;
        this.beginDate = beginDate;
        this.endDate = endDate;
        this.URL = URL;
        this.createAt = createAt;
    }

    public int getBookmarkId() {
        return bookmarkId;
    }

    public void setBookmarkId(int bookmarkId) {
        this.bookmarkId = bookmarkId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public String getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }
    @Override
    public String toString() {
        return "BookmarkInfo{" +
                "bookmarkId=" + bookmarkId +
                ", title='" + title + '\'' +
                ", institution='" + institution + '\'' +
                ", beginDate='" + beginDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", URL='" + URL + '\'' +
                ", createAt='" + createAt + '\'' +
                '}';
    }
}