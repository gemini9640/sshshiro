<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@page import="org.springframework.context.ApplicationContext"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="com.sshs.service.OperatorService"%>
<%@include file="/jsp/common/include.jsp" %>
<%
    ApplicationContext applicationContext= WebApplicationContextUtils.getRequiredWebApplicationContext(session.getServletContext());
	OperatorService operatorService=(OperatorService)applicationContext.getBean("operatorService");

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
 <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>创建子管理员</title>
</head>
<body>
<div id="excel_menu_left">
其他  --> 创建子管理员 <a href="javascript:history.back();"><font color="red">上一步</font></a>
</div>
<div id="excel_menu">
<s:fielderror />
<s:form action="createSubOperator" onsubmit="submitonce(this);" namespace="/office/operator" name="mainform" id="mainform" theme="simple">
<s:if test="#session.operator.role=='admin'">
 <p align="left">类型:&nbsp;<s:select emptyOption="true" name="authority" list="#application.OperatorRole" listKey="role" listValue="roleName"></s:select></p>
</s:if>
<s:else>
  <p align="left">等级:&nbsp;<s:select emptyOption="true" name="authority" list="@bet.code.enums.OperatorRole@getSubOperator(#session.operator.role)" listKey="code" listValue="text"></s:select></p>
</s:else>
<p align="left">子管理员帐号:&nbsp;<s:textfield maxlength="18" name="newOperator" size="30" /></p>
<p align="left">密码:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<s:password name="password" size="30" /></p>
<p align="left">重复密码:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<s:password name="retypePassword" size="30" /></p>
<p align="left">推广编号:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<s:textfield name="serial" size="10" value="%{#request.serial}" readonly="true"/></p>
<p align="left"><s:submit value="创建子管理员" align="left" /></p>
</s:form>
</div>
</body>
</html>