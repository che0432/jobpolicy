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
        setTitle("���� ���ڸ� ���� ��å");

        //â ���� �� DB ���� �����ϵ��� ����
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                closeDBConnection();
                System.exit(0);
            }
        });

        Container contentPane = getContentPane();

        // table
        String colNames[] = {"��ȣ", "����", "�����", "������", "������", "ī�װ�", "���ּ�"};
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
        recentButton = new JButton("�ֽż�");
        oldButton = new JButton("��ϼ�");
        addBookMarkButton = new JButton("�߰�");
        policyAIdText = new JTextField(5);
        categoryBox = new JComboBox<String>();
        categoryBox.setModel(new DefaultComboBoxModel<String>(new String[] {"", "�������", "��Ȱ����", "����Ȱ��", "�ְ�����", "��������"}));

        topPanel.add(policyapiFrameButton);
        topPanel.add(publicjobFrameButton);
        topPanel.add(bookmarkFrameButton);
        topPanel.add(recentButton);
        topPanel.add(oldButton);
        topPanel.add(new JLabel("ī�װ�"));
        topPanel.add(categoryBox);
        topPanel.add(new JLabel("��ȣ"));
        topPanel.add(policyAIdText);
        topPanel.add(addBookMarkButton);

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

    // ���
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

    // ��� �ֽ�, ��ϼ� ����
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
        setStatus("����� " + sortOrder + "���� ���� �Ǿ����ϴ�.");
        return count;
    }

    // ��� ī�װ� ���͸�
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
        setStatus(category + "�� ���͸� �Ǿ����ϴ�.");
        return count;
    }

    public void addPolicyBookmark(String policyAId) {

        Bookmark newBookmark = new Bookmark(0, Integer.parseInt(policyAId), userId, 0 , "");
        bdao.addBookmarkPolicyapi(newBookmark);
        setStatus("���ã�Ⱑ �߰��Ǿ����ϴ�.");

        setPageLabel();
    }

    // ����/����/����/�˻� ó��
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