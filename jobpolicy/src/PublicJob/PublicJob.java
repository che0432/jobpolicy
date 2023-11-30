package PublicJob;

public class PublicJob {
    private int jobId;
    private String title;
    private String institution;
    private String beginDate;
    private String endDate;
    private String URL;

    public PublicJob(int jobId, String title, String institution, String beginDate, String endDate, String URL) {
        this.jobId = jobId;
        this.title = title;
        this.institution = institution;
        this.beginDate = beginDate;
        this.endDate = endDate;
        this.URL = URL;
    }

    public int getjobId() {
        return jobId;
    }

    public void setjobId(int policyAId) {
        this.jobId = policyAId;
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

    public String getbeginDate() {
        return beginDate;
    }

    public void setbeginDate(String beginDate) {
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

    @Override
    public String toString() {
        return "publicjob [" + "jobId=" + jobId + ", title='" + title + ", institution='" + institution +
                ", beginDate='" + beginDate + ", endDate='" + endDate + ", URL='" + URL + ']';
    }
}
