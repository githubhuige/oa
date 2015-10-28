<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<html>
<head>
    <title>部门列表</title>
	<%@ include file="/WEB-INF/jsp/public/common.jspf" %>
</head>
<body>
 
<div id="Title_bar">
    <div id="Title_bar_Head">
        <div id="Title_Head"></div>
        <div id="Title"><!--页面标题-->
            <img border="0" width="13" height="13" src="${pageContext.request.contextPath}/style/images/title_arrow.gif"/> 部门管理
        </div>
        <div id="Title_End"></div>
    </div>
</div>

<div id="MainArea">
    <table cellspacing="0" cellpadding="0" class="TableStyle">
       
        <!-- 表头-->
        <thead>
            <tr align=center valign=middle id=TableTitle>
            	<td width="150px">部门名称</td>
				<td width="150px">上级部门名称</td>
				<td width="200px">职能说明</td>
				<td>相关操作</td>
            </tr>
        </thead>

		<!--显示数据列表-->
        <tbody id="TableData" class="dataContainer" datakey="departmentList">
        
        <s:iterator value="#listDepartment">
			<tr class="TableDetail1 template">
			<%--departmentAction中的parentId通过set方法接受这里的parentId,这里的id是Deparment中的id,model中的id --%>
				<td><s:a action="departmentAction_list?parentId=%{id}">${name}</s:a>&nbsp;</td>
				<td>${parent.name}&nbsp;</td>
				<td>${description}&nbsp;</td>
				<td>
				<%--parentId=%{parent.id} 删除完这一级后。回到上一级 --%>
					<s:a action="departmentAction_delete?id=%{id}&parentId=%{parent.id}" onclick="return window.confirm('这将删除所有的下级部门，您确定要删除吗？')">删除</s:a>
					<s:a action="departmentAction_updateUI?id=%{id}">修改</s:a>
				</td>
			</tr>
		</s:iterator>	
			
        </tbody>
    </table>
    
    <!-- 其他功能超链接 -->
    <div id="TableTail">
        <div id="TableTail_inside">
            <s:a action="departmentAction_addUI?parentId=%{parentId}"><img src="${pageContext.request.contextPath}/style/images/createNew.png" /></s:a>
        	<%--#parent.parent.id表示：因为是父部门id，所以返回上一级呢，
        	说明相对于这一级来说是parent.id，而上一级也要显示父类，所以回到上一级意味着：
        	parentId就是这一级的父类的父类
        	parent在model中，但是没有值，所以需要通过departmentService.getById()
        	获取parent的对象，然后存在值栈的map集合中，通过#parent.parent.id取出来
        	 --%>
        	 <s:if test="#parent != null">
        		<s:a action="departmentAction_list?parentId=%{#parent.parent.id}">返回上一级</s:a>
        	</s:if>
        </div>
    </div>
</div>

<!--说明-->	
<div id="Description"> 
	说明：<br />
	1，列表页面只显示一层的（同级的）部门数据，默认显示最顶级的部门列表。<br />
	2，点击部门名称，可以查看此部门相应的下级部门列表。<br />
	3，删除部门时，同时删除此部门的所有下级部门。
</div>

</body>
</html>
