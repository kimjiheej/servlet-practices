package guestbook.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import guestbook.vo.GuestbookVo;



public class GuestbookDao {


	private Connection getConnection() throws SQLException {
		Connection conn = null;
		try {
			Class.forName("org.mariadb.jdbc.Driver");
			String url = "jdbc:mariadb://192.168.0.201:3306/webdb?charset=utf8";
			conn = DriverManager.getConnection(url, "webdb", "webdb");
		} catch (ClassNotFoundException e) {
			System.out.println("드라이버 로딩 실패:" + e);
		}
		return conn;
	}

	public int insert(GuestbookVo vo) {
		int result = 0;
		Connection conn = null;
		PreparedStatement pstmt1 = null;
		PreparedStatement pstmt2 = null;
		PreparedStatement pstmt3 = null;
		
		try {
			conn = getConnection();
			pstmt1 = conn.prepareStatement("update guestbook_log set count = count + 1 where date = current_date()");
			pstmt2 = conn.prepareStatement("insert into guestbook_log values(current_date(), 1)");
			pstmt3 = conn.prepareStatement("insert into guestbook values(null, ?, ?, ?, now())");
			pstmt3.setString(1, vo.getName());
			pstmt3.setString(2, vo.getPassword());
			pstmt3.setString(3, vo.getContents());
			
			// TX:BEGIN ///////////////
			conn.setAutoCommit(false);
			
			//DML1
			int rowCount = pstmt1.executeUpdate();
			
			//DML2
			if(rowCount == 0) {
				pstmt2.executeUpdate();
			}
			
			//DML3
			result = pstmt3.executeUpdate();
			
			// TX:END(SUCCESS) /////////
			conn.commit();
		} catch (SQLException e) {
			System.out.println("Error:" + e);
			
			// TX:END(FAIL) /////////
			try {
				conn.rollback();
			} catch(SQLException ignored) {
			}
			
		} finally {
			try {
				if(pstmt3 != null) {
					pstmt3.close();
				}
				if(pstmt2 != null) {
					pstmt2.close();
				}
				if(pstmt1 != null) {
					pstmt1.close();
				}
				if(conn != null) {
					conn.close();
				}
			} catch(SQLException ignored) {
			}
		}
		
		return result;
	}
	public List<GuestbookVo> findAll() {
		List<GuestbookVo> result = new ArrayList<>();
		try (
				Connection conn = getConnection();
				PreparedStatement pstmt = conn.prepareStatement("select no, name, password, contents, reg_date from guestbook order by no desc");
				ResultSet rs = pstmt.executeQuery();
				) {
			while(rs.next()) {
				Long no = rs.getLong(1);
				String name = rs.getString(2);
				String password = rs.getString(3);
				String contents = rs.getString(4);
				// 데이터베이스에서 직접 시간 정보를 가져옴 (Timestamp 형식)
				Timestamp reg_date = rs.getTimestamp(5);

				GuestbookVo vo = new GuestbookVo();

				vo.setNo(no);
				vo.setName(name);
				vo.setPassword(password);
				vo.setRegDate(reg_date);
				vo.setContents(contents);
				result.add(vo);
			}
		} catch (SQLException e) {
			System.out.println("error:" + e);
		}

		return result;
	}

	// delete 
	public void delete(String no, String password) {
	    Connection conn = null;
	    PreparedStatement pstmt1 = null;
	    PreparedStatement pstmt2 = null;

	    int result = 0;
	    try {
	        conn = getConnection();
	     

	        // guestbook_log 업데이트
	        pstmt1 = conn.prepareStatement(
	            "UPDATE guestbook_log SET count = count - 1 WHERE date = (SELECT DATE(reg_date) FROM guestbook WHERE no = ?)"
	        );
	        pstmt1.setLong(1, Long.parseLong(no));

	        // guestbook 삭제
	        pstmt2 = conn.prepareStatement(
	            "DELETE FROM guestbook WHERE no = ? AND password = ?"
	        );
	        pstmt2.setLong(1, Long.parseLong(no));
	        pstmt2.setString(2, password);

	       conn.setAutoCommit(false);
	       
	       result = pstmt2.executeUpdate();
	       
	       pstmt1.executeUpdate();
	       
	       result = pstmt2.executeUpdate();
	       
	       
	        conn.commit();  // 트랜잭션 커밋
	    } catch (SQLException e) {
	        if (conn != null) {
	            try {
	                conn.rollback();  // 예외 발생 시 롤백
	            } catch (SQLException rollbackEx) {
	                System.out.println("rollback error: " + rollbackEx);
	            }
	        }
	        System.out.println("error: " + e);
	    } finally {
	        try {
	            if (pstmt1 != null) {
	                pstmt1.close();
	            }
	            if (pstmt2 != null) {
	                pstmt2.close();
	            }
	            if (conn != null) {
	                conn.close();
	            }
	        } catch (SQLException closeEx) {
	            System.out.println("close error: " + closeEx);
	        }
	    }
	}
}


