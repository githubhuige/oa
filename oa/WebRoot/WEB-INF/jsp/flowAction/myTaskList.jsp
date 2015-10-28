<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
 
<html>
<head>
    <title>待我审批</title>
    <%@ include file="/WEB-INF/jsp/public/common.jspf" %>
</head>
<body> 

<div id="Title_bar">
    <div id="Title_bar_Head">
        <div id="Title_Head"></div>
        <div id="Title"><!--页面标题-->
            <img border="0" width="13" height="13" src="../style/images/title_arrow.gif"/> 待我审批
        </div>
        <div id="Title_End"></div>
    </div>
</div>


<div id="MainArea">
    <table cellspacing="0" cellpadding="0" class="TableStyle">
        <!-- 表头-->
        <thead>
            <tr align="CENTER" valign="MIDDLE" id="TableTitle">
				<td width="250">标题</td>
				<td width="115">申请人</td>
				<td width="115">申请日期</td>
				<td>相关操作</td>
			</tr>
		</thead>		
		<!--显示数据列表-->
        <tbody id="TableData" class="dataContainer" datakey="formList">
        <s:iterator value="#myTaskList">
			<tr class="TableDetail1 template">
				<td><s:a action="FlowAction_approveUI?applicationId=%{application.id}&taskId=%{task.id}">${title}</</s:a></td>
				<td>${applicant.name}&nbsp;</td>
				<td><s:date name="application.applyTime" format="yyyy-MM-dd HH:mm:ss"/>&nbsp;</td>
				<td><s:a action="FlowAction_approveUI?applicationId=%{application.id}&taskId=%{task.id}">审批处理</s:a>
					<!-- <a href="showForm.html">查看申请信息</a> -->
					<s:a action="FlowAction_approveHistory?applicationId=%{application.id}">查看流转记录</s:a>
				</td>
			</tr>
        </s:iterator>
        </tbody>
    </table>
    
    <!-- 其他功能超链接 -->
    <div id="TableTail"><div id="TableTail_inside"></div></div>
</div>

<!--分页信息-->

<div class="Description">
	说明：<br />
	1，这里列出的所有的表单状态都为"审批中"。
</div>

</body>
</html>
