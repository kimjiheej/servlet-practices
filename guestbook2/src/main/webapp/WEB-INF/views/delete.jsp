<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="guestbook.vo.GuestbookVo" %>
<%@ page import="guestbook.dao.GuestbookDao" %>
    
    
<%
   request.setCharacterEncoding("utf-8");
   String no = request.getParameter("no");  
   String password = request.getParameter("password");
   
   
   GuestbookVo vo = new GuestbookVo();

	new GuestbookDao().delete(no,password);
   

   
   response.sendRedirect(request.getContextPath() + "/gb"); 
   
%>