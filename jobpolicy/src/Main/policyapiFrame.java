package Main;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.SQLException;
import PolicyApi.PolicyApi;
import PolicyApi.PolicyApiDAO;
import Bookmark.BookmarkDAO;
import Bookmark.Bookmark;

public class policyapiFrame extends JFrame implements ActionListener {
    String userId;
    Connection conn = null;
    JTable table;
    JTextField policyAIdText;
    JButton nextButton, preButton, publicjobFrameButton, policyapiFrameButton, bookmarkFrameButton, recentButton, oldButton, addBookMarkButton;
    JPanel panel;
    JComboBox<String> categoryBox;
    JLabel status, pageLabel;
    int currentPage = 1;
    String sortedBy = "";

    BookmarkDAO bdao = new BookmarkDAO();
    PolicyApiDAO pdao = new PolicyApiDAO();

    policyapiFrame(String userId) {
        setTitle("공공 일자리 관련 정책");

        //창 닫을 때 DB 연결 해제하도록 설정
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                closeDBConnection();
                System.exit(0);
            }
        });

        Container contentPane = getContentPane();

        // table
        String colNames[] = {"번호", "제목", "기관명", "시작일", "종료일", "카테고리", "상세주소"};
        DefaultTableModel model = new DefaultTableModel(colNames, 0) {
            public boolean isCellEditable(int row, int col) {
                if (col == 0) return false;
                else return true;
            }
        };
        table = new JTable(model);
        resizeColumnWidth(table);
        status = new JLabel();

        contentPane.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel topPanel = new JPanel();

        publicjobFrameButton = new JButton("공공 일자리 정보");
        policyapiFrameButton = new JButton("공공 일자리 관련 정책");
        bookmarkFrameButton = new JButton("즐겨찾기");
        recentButton = new JButton("최신순");
        oldButton = new JButton("등록순");
        addBookMarkButton = new JButton("추가");
        policyAIdText = new JTextField(5);
        categoryBox = new JComboBox<String>();
        categoryBox.setModel(new DefaultComboBoxModel<String>(new String[] {"", "기업지원", "생활지원", "구직활동", "주거지원", "재직지원"}));

        topPanel.add(policyapiFrameButton);
        topPanel.add(publicjobFrameButton);
        topPanel.add(bookmarkFrameButton);
        topPanel.add(recentButton);
        topPanel.add(oldButton);
        topPanel.add(new JLabel("카테고리"));
        topPanel.add(categoryBox);
        topPanel.add(new JLabel("번호"));
        topPanel.add(policyAIdText);
        topPanel.add(addBookMarkButton);

        JPanel bottomPanel = new JPanel();

        preButton = new JButton("이전");
        nextButton = new JButton("다음");
        pageLabel = new JLabel();
        bottomPanel.add(preButton);
        bottomPanel.add(pageLabel);
        bottomPanel.add(nextButton);

        panel = new JPanel(new BorderLayout());
        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(bottomPanel, BorderLayout.CENTER);
        panel.add(status, BorderLayout.SOUTH);

        contentPane.add(panel, BorderLayout.SOUTH);

        // action listener
        preButton.addActionListener(this);
        nextButton.addActionListener(this);
        policyapiFrameButton.addActionListener(this);
        publicjobFrameButton.addActionListener(this);
        bookmarkFrameButton.addActionListener(this);
        recentButton.addActionListener(this);
        oldButton.addActionListener(this);
        categoryBox.addActionListener(this);
        policyAIdText.addActionListener(this);
        addBookMarkButton.addActionListener(this);

        setPreferredSize(new Dimension(1300, 500));
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        initPolicyApi();
        this.userId = userId;
    }

    //DB 연결 해제
    void closeDBConnection() {
        try {
            DBConnManager.closeConnection(conn);
            System.err.println("DB 연결이 해제되었습니다.");
        }
        catch (SQLException e) {
            System.err.println("DB 연결 해제 중 에러!");
            e.printStackTrace();
        }
    }

    // 테이블 초기화 (모든 레코드)
    public int initPolicyApi() {

        DefaultTableModel model = (DefaultTableModel) table.getModel();
        PolicyApi[] list = pdao.selectPolicyApi(1);
        int count = 0;
        for(int i = 0; i < 20; i++) {
            String [] arr = new String[7];
            arr[0] = Integer.toString(list[i].getPolicyAId());
            arr[1] = list[i].getTitle();
            arr[2] = list[i].getInstitution();
            arr[3] = list[i].getbeginDate();
            arr[4] = list[i].getEndDate();
            arr[5] = list[i].getCategory();
            arr[6] = list[i].getURL();

            model.addRow(arr);
        }
        resizeColumnWidth(table);
        setPageLabel();
        return count;
    }

    // 목록
    public int selectPolicyApi(int page) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setNumRows(0);
        PolicyApi[] list = pdao.selectPolicyApi(page);
        int count = 0;
        for(int i = 0; i < list.length; i++) {
            String [] arr = new String[7];
            arr[0] = Integer.toString(list[i].getPolicyAId());
            arr[1] = list[i].getTitle();
            arr[2] = list[i].getInstitution();
            arr[3] = list[i].getbeginDate();
            arr[4] = list[i].getEndDate();
            arr[5] = list[i].getCategory();
            arr[6] = list[i].getURL();

            model.addRow(arr);
        }
        resizeColumnWidth(table);
        setPageLabel();
        return count;
    }

    // 목록 최신, 등록순 정렬
    public int seletPolicyApiSortedByDate(String sortOrder, int page) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setNumRows(0);
        PolicyApi[] list = pdao.sortedByDate(sortOrder, page);
        int count = 0;

        for(int i = 0; i < list.length; i++) {
            String [] arr = new String[7];
            arr[0] = Integer.toString(list[i].getPolicyAId());
            arr[1] = list[i].getTitle();
            arr[2] = list[i].getInstitution();
            arr[3] = list[i].getbeginDate();
            arr[4] = list[i].getEndDate();
            arr[5] = list[i].getCategory();
            arr[6] = list[i].getURL();

            model.addRow(arr);
        }

        resizeColumnWidth(table);
        setPageLabel();
        setStatus("목록이 " + sortOrder + "으로 정렬 되었습니다.");
        return count;
    }

    // 목록 카테고리 필터링
    public int seletPolicyApiGetCategory(String category, int page) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setNumRows(0);
        PolicyApi[] list = pdao.getPolicyByCategory(category, page);
        int count = 0;

        for(int i = 0; i < list.length; i++) {
            String [] arr = new String[7];
            arr[0] = Integer.toString(list[i].getPolicyAId());
            arr[1] = list[i].getTitle();
            arr[2] = list[i].getInstitution();
            arr[3] = list[i].getbeginDate();
            arr[4] = list[i].getEndDate();
            arr[5] = list[i].getCategory();
            arr[6] = list[i].getURL();

            model.addRow(arr);
        }

        resizeColumnWidth(table);
        setPageLabel();
        setStatus(category + "로 필터링 되었습니다.");
        return count;
    }

    public void addPolicyBookmark(String policyAId) {

        Bookmark newBookmark = new Bookmark(0, Integer.parseInt(policyAId), userId, 0 , "");
        bdao.addBookmarkPolicyapi(newBookmark);
        setStatus("즐겨찾기가 추가되었습니다.");

        setPageLabel();
    }

    // 삽입/삭제/수정/검색 처리
    public void actionPerformed(ActionEvent ae) {
        Object src = ae.getSource();
        if (src == nextButton) {
            if(sortedBy.isEmpty()){
                if(categoryBox.getSelectedItem().toString().isEmpty())
                    selectPolicyApi(++currentPage);
                else
                    seletPolicyApiGetCategory(categoryBox.getSelectedItem().toString(), ++currentPage);
            }
            else
                seletPolicyApiSortedByDate(sortedBy, ++currentPage);
        }
        else if (src == preButton){
            if(currentPage > 1)
                if(sortedBy.isEmpty()){
                    if(categoryBox.getSelectedItem().toString().isEmpty())
                        selectPolicyApi(--currentPage);
                    else
                        seletPolicyApiGetCategory(categoryBox.getSelectedItem().toString(), --currentPage);
                }
                else
                    seletPolicyApiSortedByDate(sortedBy, --currentPage);
        }
        else if(src == oldButton){
            sortedBy = oldButton.getText().trim();
            seletPolicyApiSortedByDate(sortedBy, currentPage);
        }
        else if(src == recentButton){
            sortedBy = recentButton.getText().trim();
            seletPolicyApiSortedByDate(sortedBy, currentPage);
        }
        else if(src == categoryBox){
            sortedBy = "";
            String category = categoryBox.getSelectedItem().toString();
            seletPolicyApiGetCategory(category, currentPage);
        }
        else if(src == publicjobFrameButton){
            dispose();
            new publicjobFrame(userId);
        }
        else if(src == addBookMarkButton){
            addPolicyBookmark(policyAIdText.getText().trim());
        }
        else if(src == bookmarkFrameButton){
            dispose();
            new bookmarkFrame(userId);
        }
    }

    public void setPageLabel() {
        pageLabel.setText(Integer.toString(currentPage));
        this.validate();
    }

    public void setStatus(String s) {
        status.setText(s);
        this.validate();
    }

    public void resizeColumnWidth(JTable table) {
        final TableColumnModel columnModel = table.getColumnModel();
        for (int column = 0; column < table.getColumnCount(); column++) {
            int width = 50; // Min width
            for (int row = 0; row < table.getRowCount(); row++) {
                TableCellRenderer renderer = table.getCellRenderer(row, column);
                Component comp = table.prepareRenderer(renderer, row, column);
                width = Math.max(comp.getPreferredSize().width +1 , width);
            }
            columnModel.getColumn(column).setPreferredWidth(width);
        }
    }
}