package dao;

import static db.JdbcUtil.close;

import java.sql.*;


import vo.*;


import vo.*;

public class MemberDAO {
	private static MemberDAO instance = new MemberDAO();
	private static Connection con;

	public void setConnection(Connection con) {
		this.con = con;
	}

	private MemberDAO() {
	}

	public static MemberDAO getInstance() {
		return instance;
	}

	public boolean selectMember(MemberDTO member) {
		boolean isLoginSuccess = false;

		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			String sql = "SELECT * FROM member WHERE email=? AND passwd=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, member.getEmail());
			pstmt.setString(2, member.getPasswd());

			rs = pstmt.executeQuery();

			if (rs.next()) {
				isLoginSuccess = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("MemberDAO - selectMember() 메서드 오류 : " + e.getMessage());
		} finally {
			close(pstmt);
			close(rs);
		}

		return isLoginSuccess;
	}

	public boolean selectApiMember(MemberDTO member) {
		boolean isLoginSuccess = false;

		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			String sql = "SELECT * FROM member WHERE email=? AND api_id=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, member.getEmail());
			pstmt.setString(2, member.getApi_id());

			rs = pstmt.executeQuery();

			if (rs.next()) {
				isLoginSuccess = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("MemberDAO - selectMember() 메서드 오류 : " + e.getMessage());
		} finally {
			close(pstmt);
			close(rs);
		}

		return isLoginSuccess;
	}

	public int insertMember(MemberDTO dto) {
		PreparedStatement pstmt = null;

		System.out.println("insertMember");
		int insertCount = 0;
		String sql = "";

		try {
			sql = "INSERT INTO member VALUES (?,?,?,?,?,?,?,?,null,?,?)";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, dto.getNickName());
			pstmt.setString(2, dto.getName());
			pstmt.setString(3, dto.getEmail());
			pstmt.setString(4, dto.getPasswd());
			pstmt.setDate(5, dto.getBirthdate());
			pstmt.setString(6, dto.getPostCode());
			pstmt.setString(7, dto.getAddress1());
			pstmt.setString(8, dto.getAddress2());
			pstmt.setString(9, dto.getAuth_status());
			pstmt.setString(10, dto.getApi_id());

			insertCount = pstmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

			close(pstmt);
		}

		return insertCount;

	}

	public boolean checkUser(MemberDTO dto) throws Exception {
		boolean isLoginSuccess = false;

		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			// 3단계. SQL 구문 작성 및 전달
			// => 전달받은 아이디, 패스워드를 사용하여 일치하는 레코드 조회
			String sql = "SELECT * FROM auth_member WHERE email=? AND passwd=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, dto.getEmail());
			pstmt.setString(2, dto.getPasswd());

			// 4단계. SQL 구문 실행 및 결과 처리
			rs = pstmt.executeQuery();

			// ResultSet 객체의 다음 레코드가 존재할 경우 = 로그인 성공
			if (rs.next()) {
				// 조회 성공 시(= 로그인 성공) isLoginSuccess 값을 true 로 변경
				isLoginSuccess = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// 자원 반환
			close(rs);
			close(pstmt);
		}

		return isLoginSuccess;
	}

	public int registAuthCode(String email, String authCode) {
		int registCount = 0;

		PreparedStatement pstmt = null, pstmt2 = null;
		ResultSet rs = null;

		try {
			// 전달받은 ID 에 대한 기존 인증코드가 존재하는지 확인 후
			// 만약, 기존 인증코드가 존재하면 새 인증코드로 덮어쓰고(UPDATE),
			// 아니면, 아이디와 인증코드를 새로 등록(INSERT)
			String sql = "SELECT auth_code FROM auth_info WHERE email=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, email);
			rs = pstmt.executeQuery();

			if (rs.next()) { // 기존 인증코드가 이미 존재하는 경우
				// UPDATE 구문을 사용하여 인증코드 갱신(새로운 인증코드로 덮어씀)
				sql = "UPDATE auth_info SET auth_code=? WHERE email=?";
				pstmt2 = con.prepareStatement(sql);
				pstmt2.setString(1, authCode);
				pstmt2.setString(2, email);
				registCount = pstmt2.executeUpdate();
				System.out.println("인증코드 갱신 성공!");
			} else { // 기존 인증코드가 존재하지 않는 경우
				// INSERT 구문을 사용하여 아이디와 인증코드 추가
				sql = "INSERT INTO auth_info VALUES (?,?)";
				pstmt2 = con.prepareStatement(sql);
				pstmt2.setString(1, email);
				pstmt2.setString(2, authCode);
				registCount = pstmt2.executeUpdate();
				System.out.println("인증코드 등록 성공!");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(rs);
			close(pstmt);
			close(pstmt2);
		}

		return registCount;
	}

	public boolean selectDuplicateNickName(String nickName) {
		boolean isDuplicate = false;

		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			String sql = "SELECT nickName FROM member WHERE nickName=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, nickName);
			rs = pstmt.executeQuery();

			if (rs.next()) { // 아이디가 이미 존재할 경우
				isDuplicate = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("MemberDAO - selectDuplicateNickName() 메서드 오류 : " + e.getMessage());
		} finally {
			close(rs);
			close(pstmt);
		}

		return isDuplicate;
	}

	public boolean selectDuplicateEmail(String email) {
		boolean isDuplicate = false;

		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			String sql = "SELECT email FROM member WHERE email=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, email);

			rs = pstmt.executeQuery();

			if (rs.next()) { // 아이디가 이미 존재할 경우
				isDuplicate = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("MemberDAO - selectDuplicateEmail() 메서드 오류 : " + e.getMessage());
		} finally {
			close(rs);
			close(pstmt);
		}

		return isDuplicate;
	}

	public boolean isAuthenticatedUser(MemberDTO member) {
		System.out.println("isAuthenticatedUser_DAO");
		boolean isAuthenticatedUserSuccess = false;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			String sql = "SELECT auth_status FROM member WHERE email=? AND passwd=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, member.getEmail());
			pstmt.setString(2, member.getPasswd());

			rs = pstmt.executeQuery();
			if (rs.next()) {
				if (rs.getString(1).equals("Y")) {
					isAuthenticatedUserSuccess = true;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("MemberDAO - selectMember() 메서드 오류 : " + e.getMessage());
		} finally {
			close(pstmt);
			close(rs);
		}

		return isAuthenticatedUserSuccess;
	}

	public boolean checkApiId(MemberDTO member) {
		System.out.println("checkApiId");
		boolean isApiUserSuccess = false;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			String sql = "SELECT api_id FROM member WHERE email=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, member.getEmail());

			rs = pstmt.executeQuery();
			if (rs.next()) {

				isApiUserSuccess = true;

			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("MemberDAO - selectMember() 메서드 오류 : " + e.getMessage());
		} finally {
			close(pstmt);
			close(rs);
		}

		return isApiUserSuccess;
	}

	public int newPassword(String email, String passwd) {
		int updateCount = 0;

		PreparedStatement pstmt = null;

		try {
			String sql = "UPDATE member SET passwd=? WHERE email=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, passwd);
			pstmt.setString(2, email);
			updateCount = pstmt.executeUpdate();
			System.out.println("패스워드 갱신 성공!");
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {

			close(pstmt);

		}

		return updateCount;
	}

	public MemberDTO selectMember(String email) {
		System.out.println("selectMember");
		MemberDTO member = null;

		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			String sql = "SELECT * FROM member WHERE email=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, email);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				member = new MemberDTO();
				member.setName(rs.getString("name"));
				member.setNickName(rs.getString("nickname"));
				member.setEmail(rs.getString("email"));
				member.setBirthdate(rs.getDate("birthdate"));
				member.setPostCode(rs.getString("postcode"));
				member.setAddress1(rs.getString("address1"));
				member.setAddress2(rs.getString("address2"));

				System.out.println(member);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("SQL 오류 - selectMember() : " + e.getMessage());
		} finally {
			close(rs);
			close(pstmt);
		}

		return member;
	}

	public int motifyMember(MemberDTO member) {
		System.out.println("motifyMember");
		int updateCount = 0;

		PreparedStatement pstmt = null;

		try {
			String sql = "UPDATE member SET nickname=?,name=?,birthdate=?, postcode=?,address1=?,address2=? WHERE email=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, member.getNickName());
			pstmt.setString(2, member.getName());
			pstmt.setDate(3, member.getBirthdate());
			pstmt.setString(4, member.getPostCode());
			pstmt.setString(5, member.getAddress1());
			pstmt.setString(6, member.getAddress2());
			pstmt.setString(7, member.getEmail());

			updateCount = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("SQL 오류 - updatemember() : " + e.getMessage());
		} finally {
			close(pstmt);
		}

		return updateCount;
	}

}
