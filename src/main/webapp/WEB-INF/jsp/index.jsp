<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c"    uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt"  uri="http://java.sun.com/jsp/jstl/fmt" %>
    
<!DOCTYPE html>
<html>
<head>
<title>Synthetic Responder</title>
</head>
<body>

<h1>Response Settings</h1>
<form:form commandName="settings">
<table>
  	<tr>
  		<td>Fail mode: </td>
  		<td><form:radiobuttons path="failMode" items="${settings.failModeOptions}"/>
  	</tr>
	<tr>
      <td>Response code: </td>
      <td><form:input path="responseCode" /></td>
  	</tr>
  	<tr>
    	<td colspan="2"><input type="submit" value="Save Changes" /></td>
    </tr>
</table>
</form:form>

<h1>Response URL</h1>
<a href="response">Response URL</a>

<h1>Request history</h1>
<table>
<tr><th>When</th><th>IP</th><th>Mode</th><th>Code</th></tr>
<c:forEach var="h" items="${history}">
<tr>
  <td><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${h.when}" /></td>
  <td>${h.remoteAddr}</td>
  <td>${h.mode}</td>
  <td>${h.responseCode}</td>
</tr>
</c:forEach>
</table>
</body>
</html>