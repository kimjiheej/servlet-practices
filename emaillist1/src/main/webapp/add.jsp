
    <%@ page import = "emaillist.dao.EmaillistDao" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 <%@ page import="emaillist.vo.EmaillistVo" %>
    
    <%
      request.setCharacterEncoding("utf-8");
     String firstName =  request.getParameter("fn");
     String lastName = request.getParameter("ln");
     String email = request.getParameter("email");
     
     EmaillistVo vo = new EmaillistVo();
     vo.setFirstName(firstName);
     vo.setLastName(lastName);
     vo.setEmail(email);
     new EmaillistDao().insert(vo);
    %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
        <h1>성공적으로 이메일이 등록 되었습니다.</h1>
</body>
</html>