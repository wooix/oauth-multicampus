<%@ page language="java" contentType="text/plain; charset=utf-8"
    pageEncoding="utf-8"%>
<%
	String message = (String)request.getAttribute("message");
	response.getWriter().println(message);
%>
