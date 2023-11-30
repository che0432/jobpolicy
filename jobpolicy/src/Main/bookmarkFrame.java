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
import Bookmark.BookmarkDAO;
import Bookmark.Bookmark;

public class bookmarkFrame extends JFrame implements ActionListener {
    String userId;
    Connection conn = null;
    JTable table;
    JTextField bookmarkIdText;
    JButton nextButton, preButton, publicjobFrameButton, policyapiFrameButton, bookmarkFrameButton, deleteBookMarkButton;
    JPanel panel;
    JComboBox<String> categoryBox;
    JLabel status, pageLabel;
    int currentPage = 1;
    String sortedBy = "";
    
    BookmarkDAO dao = new BookmarkDAO();

    bookmarkFrame(String userId) {
        setTitle("���ã��");

        //â ���� �� DB ���� �����ϵ��� ����
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                closeDBConnection();
                System.exit(0);
            }
        });

        Container contentPane = getContentPane();

        // table
        String colNames[] = {"��ȣ", "��å��ȣ", "���ڸ���ȣ", "������"};
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

        publicjobFrameButton = new JButton("���� ���ڸ� ����");
        policyapiFrameButton = new JButton("���� ���ڸ� ���� ��å");
        bookmarkFrameButton = new JButton("���ã��");
        deleteBookMarkButton = new JButton("����");
        bookmarkIdText = new JTextField(5);

        topPanel.add(policyapiFrameButton);
        topPanel.add(publicjobFrameButton);
        topPanel.add(bookmarkFrameButton);
        topPanel.add(new JLabel("��ȣ"));
        topPanel.add(bookmarkIdText);
        topPanel.add(deleteBookMarkButton);

        JPanel bottomPanel = new JPanel();

        preButton = new JButton("����");
        nextButton = new JButton("����");
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
        bookmarkIdText.addActionListener(this);
        deleteBookMarkButton.addActionListener(this);

        setPreferredSize(new Dimension(1300, 500));
        setLocationRelativeTo(null);
        pack();
        setVisible(true);
        this.userId = userId;
        initBookmark();
    }

    //DB ���� ����
    void closeDBConnection() {
        try {
            DBConnManager.closeConnection(conn);
            System.err.println("DB ������ �����Ǿ����ϴ�.");
        }
        catch (SQLException e) {
            System.err.println("DB ���� ���� �� ����!");
            e.printStackTrace();
        }
    }

    // ���̺� �ʱ�ȭ (��� ���ڵ�)
    public int initBookmark() {

        DefaultTableModel model = (DefaultTableModel) table.getModel();
        Bookmark[] list = dao.checkBookmark(userId,1);
        int count = 0;
        for(int i = 0; i < 20; i++) {
            String [] arr = new String[4];
            arr[0] = Integer.toString(list[i].getBookmarkId());
            arr[1] = Integer.toString(list[i].getPolicyAId());
            arr[2] = Integer.toString(list[i].getJobId());
            arr[3] = list[i].getCreateAt();

            model.addRow(arr);
        }
        resizeColumnWidth(table);
        setPageLabel();
        return count;
    }

    // ���
    public int selectPolicyApi(int page) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setNumRows(0);
        
        Bookmark[] list = dao.checkBookmark(userId,page);
        int count = 0;
        for(int i = 0; i < 20; i++) {
            String [] arr = new String[4];
            arr[0] = Integer.toString(list[i].getBookmarkId());
            arr[1] = Integer.toString(list[i].getPolicyAId());
            arr[2] = Integer.toString(list[i].getJobId());
            arr[3] = list[i].getCreateAt();

            model.addRow(arr);
        }
        resizeColumnWidth(table);
        setPageLabel();
        return count;
    }

    public void deletePolicyBookmark(String bookmakrId) {
        dao.delBookmark(Integer.parseInt(bookmakrId));
        selectPolicyApi(currentPage);
        setStatus("�����Ǿ����ϴ�.");

        setPageLabel();
    }

    // ����/����/����/�˻� ó��
    public void actionPerformed(ActionEvent ae) {
        Object src = ae.getSource();
        if (src == nextButton) {
            selectPolicyApi(++currentPage);
        }
        else if (src == preButton){
            if(currentPage > 1)
                selectPolicyApi(--currentPage);
        }
        else if(src == deleteBookMarkButton){
            deletePolicyBookmark(bookmarkIdText.getText().trim());
        }
        else if(src == policyapiFrameButton){
            dispose();
            new policyapiFrame(userId);
        }
        else if(src == publicjobFrameButton){
            dispose();
            new publicjobFrame(userId);
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