package main.java.com.jobpolicy;
import main.java.com.jobpolicy.user.UserDAO;
import main.java.com.jobpolicy.user.User;
import java.util.Date;

public class Main {

	public static void main(String[] args) {
		//�׽�Ʈ�� ������
		String userid = "user11";
		String password = "1234";
		int gender = 1;
		String birth = "2005-11-13";
		
		UserDAO dao = new UserDAO();
		//���̵� �ߺ� üũ �׽�Ʈ
		int idCheck = dao.userIdCheck(userid);
		System.out.println("���̵� ���� 1 / �������� �ʴ� ���̵� 0 / ���� -2");
		System.out.println("idCheck = "+idCheck);
		
		userid = "user100";
		User user = new User();
		
		//ȸ������ �׽�Ʈ
		user.setUserid(userid);
		user.setPassword(password);
		user.setGender(gender);
		user.setBirth(birth);
		dao.userRegister(user);
		System.out.println(user.toString());
		
		//�α��� �׽�Ʈ
		int login = dao.login(userid, password);
		System.out.println("�α��� ���� 1 / �������� �ʴ� ���̵� 0 / ��й�ȣ ����ġ -1 / ���� -2");
		System.out.println("login = "+login);
		
		//ȸ�� ���� ��ȸ �׽�Ʈ
		user = dao.userInfo(userid);
		System.out.println(user.toString());
		
		//ȸ��Ż�� �׽�Ʈ
		dao.userDelete(userid);
	}

}
