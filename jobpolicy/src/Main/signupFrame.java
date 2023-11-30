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
        setTitle("회원가입");

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
        JPanel TopPanel1 = new JPanel();
        JPanel TopPanel2 = new JPanel();
        JPanel TopPanel3 = new JPanel();
        JPanel TopPanel4 = new JPanel();
        JPanel TopPanel5 = new JPanel();

        idText = new JTextField(6);
        pwText = new JTextField(6);
        birthText = new JTextField(10);
        signInButton = new JButton("뒤로");
        signUpButton = new JButton("가입하기");
        genderBox = new JComboBox<String>();
        genderBox.setModel(new DefaultComboBoxModel<String>(new String[] {"남성", "여성"}));
        bottomPanel.add(signInButton);
        bottomPanel.add(signUpButton);

        TopPanel2.add(new JLabel("아이디"));
        TopPanel2.add(idText);

        TopPanel3.add(new JLabel("비밀번호"));
        TopPanel3.add(pwText);

        TopPanel4.add(new JLabel("성별"));
        TopPanel4.add(genderBox);

        TopPanel5.add(new JLabel("생년월일"));
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
            User user = new User(idStr, pwStr, genderStr.equals("남성") ? 1 : 0, birthStr, "");

            if(idStr.equals(""))
                setStatus("아이디를 입력해주세요.");
            else if(dao.userIdCheck(idStr) == 1)
                setStatus("중복되는 아이디 입니다.");
            else{
                try{
                    dao.userRegister(user);
                }catch (Exception e){
                    setStatus("계정 생성에 실패했습니다.");
                }
                finally {
                    setStatus("계정 생성에 성공했습니다.");
                }
            }
        }
    }

    public void setStatus(String s) {
        status.setText(s);
        this.validate();
    }
}