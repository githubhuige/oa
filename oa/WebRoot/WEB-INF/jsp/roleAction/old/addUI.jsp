<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    
    <title>添加岗位</title>
    
  </head>
  
  <body>
    <s:form action="roleAction_add">
    	<tr><s:textfield name="name" label="名称"></s:textfield></tr>
    	<tr><s:textfield name="description" label="说明"></s:textfield></tr>
    	<s:submit value="提交"></s:submit>
    </s:form>
  </body>
</html>
