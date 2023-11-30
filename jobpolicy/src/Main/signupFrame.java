package Main;

import User.UserDAO;
import User.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.SQLException;

public class signupFrame extends JFrame implements ActionListener {
    Connection conn = null;
    JTextField idText, pwText, birthText;
    JButton signUpButton, signInButton;
    JPanel panel;
    JComboBox<String> genderBox;
    JLabel status;
    
    signupFrame() {
        setTitle("ȸ������");

        //â ���� �� DB ���� �����ϵ��� ����
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                closeDBConnection();
                System.exit(0);
            }
        });

        Container contentPane = getContentPane();

        // table
        status = new JLabel();

        // panel
        JPanel bottomPanel = new JPanel();
        JPanel TopPanel1 = new JPanel();
        JPanel TopPanel2 = new JPanel();
        JPanel TopPanel3 = new JPanel();
        JPanel TopPanel4 = new JPanel();
        JPanel TopPanel5 = new JPanel();

        idText = new JTextField(6);
        pwText = new JTextField(6);
        birthText = new JTextField(10);
        signInButton = new JButton("�ڷ�");
        signUpButton = new JButton("�����ϱ�");
        genderBox = new JComboBox<String>();
        genderBox.setModel(new DefaultComboBoxModel<String>(new String[] {"����", "����"}));
        bottomPanel.add(signInButton);
        bottomPanel.add(signUpButton);

        TopPanel2.add(new JLabel("���̵�"));
        TopPanel2.add(idText);

        TopPanel3.add(new JLabel("��й�ȣ"));
        TopPanel3.add(pwText);

        TopPanel4.add(new JLabel("����"));
        TopPanel4.add(genderBox);

        TopPanel5.add(new JLabel("�������"));
        TopPanel5.add(birthText);

        TopPanel1.add(TopPanel2);
        TopPanel1.add(TopPanel3);
        TopPanel1.add(TopPanel4);
        TopPanel1.add(TopPanel5);

        panel = new JPanel(new BorderLayout());
        panel.add(TopPanel1, BorderLayout.NORTH);
        panel.add(bottomPanel, BorderLayout.CENTER);
        panel.add(status, BorderLayout.SOUTH);

        contentPane.add(panel, BorderLayout.SOUTH);

        // action listener
        signUpButton.addActionListener(this);
        signInButton.addActionListener(this);
        setPreferredSize(new Dimension(600, 150));
        setLocationRelativeTo(null);
        pack();
        setVisible(true);
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

    public void actionPerformed(ActionEvent ae) {
        Object src = ae.getSource();
        if (src == signInButton) {
            dispose();
            new signinFrame();
        }
        else if (src == signUpButton){
        	UserDAO dao = new UserDAO();
            String idStr = idText.getText().trim();
            String pwStr = pwText.getText().trim();
            String genderStr = genderBox.getSelectedItem().toString();
            String birthStr = birthText.getText().trim();
            User user = new User(idStr, pwStr, genderStr.equals("����") ? 1 : 0, birthStr, "");

            if(idStr.equals(""))
                setStatus("���̵� �Է����ּ���.");
            else if(dao.userIdCheck(idStr) == 1)
                setStatus("�ߺ��Ǵ� ���̵� �Դϴ�.");
            else{
                try{
                    dao.userRegister(user);
                }catch (Exception e){
                    setStatus("���� ������ �����߽��ϴ�.");
                }
                finally {
                    setStatus("���� ������ �����߽��ϴ�.");
                }
            }
        }
    }

    public void setStatus(String s) {
        status.setText(s);
        this.validate();
    }
}