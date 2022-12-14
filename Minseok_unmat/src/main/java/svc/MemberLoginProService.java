package svc;

import static db.JdbcUtil.*;

import java.sql.*;

import dao.*;
import vo.*;

public class MemberLoginProService {

	public boolean loginMember(MemberDTO member) {
		boolean isLoginSuccess = false;
		
		Connection con = getConnection();
		MemberDAO dao = MemberDAO.getInstance();
		dao.setConnection(con);
		
		isLoginSuccess = dao.selectMember(member);
		
		close(con);
		
		return isLoginSuccess;
	}

}
