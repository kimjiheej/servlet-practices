package guestbook.vo;

import java.security.Timestamp;



public class GuestbookVo {

	 public GuestbookVo() {
		 
	 }
	 
	
     private Long no;
     private String name;
     private String password;
     private String contents;
     private java.sql.Timestamp regDate;
     
     
	public Long getNo() {
		return no;
	}


	public void setNo(Long no) {
		this.no = no;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public String getContents() {
		return contents;
	}


	public void setContents(String contents) {
		this.contents = contents;
	}


	public java.sql.Timestamp getRegDate() {
		return regDate;
	}


	public void setRegDate(java.sql.Timestamp regDate) {
		this.regDate = regDate;
	}


	@Override
	public String toString() {
		return "GuestbookVo [no=" + no + ", name=" + name + ", password=" + password + ", contents=" + contents
				+ ", regDate=" + regDate + "]";
	}
	
	
   
}
