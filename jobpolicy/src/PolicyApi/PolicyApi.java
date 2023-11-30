package PolicyApi;

public class PolicyApi {
    private int policyAId;
    private String title;
    private String institution;
    private String beginDate;
    private String endDate;
    private String URL;
    private String category;

    public PolicyApi(int policyAId, String title, String institution, String beginDate, String endDate, String URL, String category) {
        this.policyAId = policyAId;
        this.title = title;
        this.institution = institution;
        this.beginDate = beginDate;
        this.endDate = endDate;
        this.URL = URL;
        this.category = category;
    }

    public int getPolicyAId() {
        return policyAId;
    }

    public void setPolicyAId(int policyAId) {
        this.policyAId = policyAId;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "jobpolicy [" + "policyAId=" + policyAId + ", title='" + title + ", institution='" + institution +
                ", beginDate='" + beginDate + ", endDate='" + endDate + ", URL='" + URL + ", category='" + category + ']';
    }
}
