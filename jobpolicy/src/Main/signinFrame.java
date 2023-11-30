package Main;

import User.UserDAO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.SQLException;

public class signinFrame extends JFrame implements ActionListener {
    Connection conn = null;
    JTextField idText, pwText;
    JButton signUpButton, signInButton;
    JPanel panel;
    JLabel status;
    
    
    signinFrame() {
        setTitle("�α���");

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
        JPanel TopPanel1 = new JPanel(new BorderLayout());
        JPanel TopPanel2 = new JPanel();
        JPanel TopPanel3 = new JPanel();

        idText = new JTextField(6);
        pwText = new JTextField(6);
        signUpButton = new JButton("�����ϱ�");
        signInButton = new JButton("�α���");
        bottomPanel.add(signUpButton);
        bottomPanel.add(signInButton);

        TopPanel2.add(new JLabel("���̵�"));
        TopPanel2.add(idText);

        TopPanel3.add(new JLabel("��й�ȣ"));
        TopPanel3.add(pwText);

        TopPanel1.add(TopPanel2, BorderLayout.NORTH);
        TopPanel1.add(TopPanel3, BorderLayout.CENTER);


        panel = new JPanel(new BorderLayout());
        panel.add(TopPanel1, BorderLayout.NORTH);
        panel.add(bottomPanel, BorderLayout.CENTER);
        panel.add(status, BorderLayout.SOUTH);

        contentPane.add(panel, BorderLayout.SOUTH);

        // action listener
        signUpButton.addActionListener(this);
        signInButton.addActionListener(this);
        setPreferredSize(new Dimension(400, 150));
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
        if (src == signUpButton) {
            dispose();
            new signupFrame();
        }
        else if (src == signInButton){
        	UserDAO dao = new UserDAO();
            String idStr = idText.getText().trim();
            String pwStr = pwText.getText().trim();
            int login = dao.login(idStr, pwStr);

            if(idStr.equals(""))
                setStatus("���̵� �Է����ּ���.");
            else if(dao.userIdCheck(idStr) == 0)
                setStatus("���̵� �������� �ʽ��ϴ�.");
            else{
                if(login == -1)
                    setStatus("��й�ȣ�� ��ġ���� �ʽ��ϴ�.");
                else if(login == 1) {
                    dispose();
                    new policyapiFrame(idStr);
                }
            }
        }
    }

    public void setStatus(String s) {
        status.setText(s);
        this.validate();
    }
}
