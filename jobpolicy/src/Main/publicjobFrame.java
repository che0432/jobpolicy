package Main;

import Bookmark.Bookmark;
import Bookmark.BookmarkDAO;
import PublicJob.PublicJob;
import PublicJob.PublicJobDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.SQLException;

public class publicjobFrame extends JFrame implements ActionListener {
    String userId;
    Connection conn = null;
    JTable table;
    JTextField JobIdText;
    JButton nextButton, preButton, policyapiFrameButton, publicjobFrameButton, bookmarkFrameButton, recentButton, oldButton, addBookMarkButton;
    JPanel panel;
    JLabel status, pageLabel;
    int currentPage = 1;
    String sortedBy = "";

    BookmarkDAO bdao = new BookmarkDAO();
    PublicJobDAO jdao = new PublicJobDAO();

    publicjobFrame(String userId) {
        setTitle("공공 일자리 정보");

        //창 닫을 때 DB 연결 해제하도록 설정
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                closeDBConnection();
                System.exit(0);
            }
        });

        Container contentPane = getContentPane();

        // table
        String colNames[] = {"번호", "제목", "기관명", "시작일", "종료일", "상세주소"};
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
        JobIdText = new JTextField(5);

        topPanel.add(policyapiFrameButton);
        topPanel.add(publicjobFrameButton);
        topPanel.add(bookmarkFrameButton);
        topPanel.add(recentButton);
        topPanel.add(oldButton);
        topPanel.add(new JLabel("번호"));
        topPanel.add(JobIdText);
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
        JobIdText.addActionListener(this);
        addBookMarkButton.addActionListener(this);

        setPreferredSize(new Dimension(1500, 500));
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        initPublicjob();
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
    public int initPublicjob() {

        DefaultTableModel model = (DefaultTableModel) table.getModel();

        PublicJob[] list = jdao.selectPublicJob(1);
        int count = 0;
        for(int i = 0; i < list.length; i++) {
            String [] arr = new String[6];
            arr[0] = Integer.toString(list[i].getjobId());
            arr[1] = list[i].getTitle();
            arr[2] = list[i].getInstitution();
            arr[3] = list[i].getbeginDate();
            arr[4] = list[i].getEndDate();
            arr[5] = list[i].getURL();

            model.addRow(arr);
        }
        resizeColumnWidth(table);
        setPageLabel();
        return count;
    }

    public int selectPublicjob(int page) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setNumRows(0);
        PublicJob[] list = jdao.selectPublicJob(page);
        int count = 0;
        for(int i = 0; i < list.length; i++) {
            String [] arr = new String[7];
            arr[0] = Integer.toString(list[i].getjobId());
            arr[1] = list[i].getTitle();
            arr[2] = list[i].getInstitution();
            arr[3] = list[i].getbeginDate();
            arr[4] = list[i].getEndDate();
            arr[5] = list[i].getURL();

            model.addRow(arr);
        }
        resizeColumnWidth(table);
        setPageLabel();
        return count;
    }

    public int seletPublicJobSortedByDate(String sortOrder, int page) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setNumRows(0);
        PublicJob[] list = jdao.sortedByDate(sortOrder, page);
        int count = 0;

        for(int i = 0; i < list.length; i++) {
            String [] arr = new String[6];
            arr[0] = Integer.toString(list[i].getjobId());
            arr[1] = list[i].getTitle();
            arr[2] = list[i].getInstitution();
            arr[3] = list[i].getbeginDate();
            arr[4] = list[i].getEndDate();
            arr[5] = list[i].getURL();

            model.addRow(arr);
        }

        resizeColumnWidth(table);
        setPageLabel();
        return count;
    }

    public void addJobBookmark(String JobId) {
        Bookmark newBookmark = new Bookmark(0, 0, userId, Integer.parseInt(JobId), "");
        bdao.addBookmarkPublicjob(newBookmark);
        setStatus("즐겨찾기가 추가되었습니다.");

        setPageLabel();
    }

    // 삽입/삭제/수정/검색 처리
    public void actionPerformed(ActionEvent ae) {
        Object src = ae.getSource();
        if (src == nextButton) {
            if(sortedBy.isEmpty())
                selectPublicjob(++currentPage);
            else
                seletPublicJobSortedByDate(sortedBy, ++currentPage);
        }
        else if (src == preButton){
            if(currentPage > 1)
                if(sortedBy.isEmpty())
                    selectPublicjob(--currentPage);
                else
                    seletPublicJobSortedByDate(sortedBy, --currentPage);
        }
        else if(src == oldButton){
            sortedBy = oldButton.getText().trim();
            seletPublicJobSortedByDate(sortedBy, currentPage);
        }
        else if(src == recentButton){
            sortedBy = recentButton.getText().trim();
            seletPublicJobSortedByDate(sortedBy, currentPage);
        }
        else if(src == policyapiFrameButton){
            dispose();
            new policyapiFrame(userId);
        }
        else if(src == addBookMarkButton){
            addJobBookmark(JobIdText.getText().trim());
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