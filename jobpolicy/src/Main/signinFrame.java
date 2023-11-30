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
        setTitle("로그인");

        //창 닫을 때 DB 연결 해제하도록 설정
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
        signUpButton = new JButton("가입하기");
        signInButton = new JButton("로그인");
        bottomPanel.add(signUpButton);
        bottomPanel.add(signInButton);

        TopPanel2.add(new JLabel("아이디"));
        TopPanel2.add(idText);

        TopPanel3.add(new JLabel("비밀번호"));
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
                setStatus("아이디를 입력해주세요.");
            else if(dao.userIdCheck(idStr) == 0)
                setStatus("아이디가 존재하지 않습니다.");
            else{
                if(login == -1)
                    setStatus("비밀번호가 일치하지 않습니다.");
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
