package Bookmark;

public class Bookmark {
    private int bookmarkId;
    private int policyAId;
    private String userId;
    private int jobId;
    private String createAt;

    public Bookmark(int bookmarkId, int policyAId, String userId, int jobId, String createAt) {
        this.bookmarkId = bookmarkId;
        this.policyAId = policyAId;
        this.userId = userId;
        this.jobId = jobId;
        this.createAt = createAt;
    }

    public int getBookmarkId() {
        return bookmarkId;
    }

    public void setBookmarkId(int bookmarkId) {
        this.bookmarkId = bookmarkId;
    }

    public int getPolicyAId() {
        return policyAId;
    }

    public void setPolicyAId(int policyAId) {
        this.policyAId = policyAId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getJobId() {
        return jobId;
    }

    public void setJobId(int jobId) {
        this.jobId = jobId;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    @Override
    public String toString() {
        return "Bookmark{" +
                "bookmarkId=" + bookmarkId +
                ", policyAId=" + policyAId +
                ", userId='" + userId + '\'' +
                ", jobId=" + jobId +
                ", createAt='" + createAt + '\'' +
                '}';
    }
}
