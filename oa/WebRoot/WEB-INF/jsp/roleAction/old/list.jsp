<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    
    <title>列表</title>
    
  </head>
  
  <body>
  <%--表示从valuestake的map集合中取数据 --%>
   	<s:iterator value="#listRole">
   		${id},
		${name},
		${description},
		<s:a action="roleAction_delete?id=%{id }" onclick="return confirm('确定要删除吗？')">删除</s:a>
		<s:a action="roleAction_updateUI?id=%{id }">修改</s:a>
		<br/><br/>
   	</s:iterator>
	<s:a action="roleAction_addUI?">添加</s:a>
  </body>
</html>
