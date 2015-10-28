<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
	<title>配置权限</title>
    <%@ include file="/WEB-INF/jsp/public/common.jspf" %>
    <script language="javascript" src="${pageContext.request.contextPath}/script/jquery_treeview/jquery.treeview.js"></script>
	<link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/style/blue/file.css" />
	<link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/script/jquery_treeview/jquery.treeview.css" />
	<script type="text/javascript">
		<%-- jQury中的几个方法
			find():查找所有的后辈元素
			parent():查找上一级元素
			parents():查找所有上一级元素
			children():查找下一级元素
			siblings():查找兄弟元素
			next():查找下一个（弟）元素
			prev():查找上一个（兄）元素
		--%>
		$(function(){
			// 给所有的权限复选框添加事件
			$("[name=privilegeIds]").click(function(){
			
				// 自己选中或取消时，把所有的下级权限也都同时选中或取消
				<%--
					this:代表自已，也就是触发事件的元素
					整句话翻译：看下面的结构
					<ul>
						<li>
							<input value="系统管理"/>
							<ul>
								<li></li>
							<ul/>
						</li>
					</ul>
					假如选中的是input,那么input是没有下级了，所以要想选中或者取消他的所有下级
					可以通过它的兄弟ul查找所有ul下的li
					attr("checked", this.checked):代表我做啥事，$(this).siblings("ul").find("input")
					也做啥事
					
					$(this).parent().parent().siblings("input"):
						input是第二个ul的兄弟
						所以来到了系统管理了
				--%>
				$(this).siblings("ul").find("input").attr("checked", this.checked);
			
				// 当选中一个权限时，也要同时选中所有的直系上级权限
				if(this.checked){
				
					$(this).parents("li").children("input").attr("checked", true);
				}else{
				// 当取消一个权限时，同级没有选中的权限了，就也取消他的上级权限，再向上也这样做。
					if($(this).parent().siblings("li").children("input:checked").size() == 0){
						//只是取消它的上一级
						$(this).parent().parent().siblings("input").attr("checked", false);
						var start = $(this).parent().parent();
						if(start.parent().siblings("li").children("input:checked").size() == 0){
							start.parent().parent().siblings("input").attr("checked", false);
						}
						<%--
							<ul>
								<li>
									<input value="系统管理"/>
									<ul>
										<li>
											<input value="岗位管理">
											<ul>
												<li></li>
											</ul>
										</li>
										<li>
											<input value="部门管理">
										</li>
										<li>
										
											<input value="用户管理">
										</li>
									<ul/>
								</li>
							</ul>
						
						--%>
					}
				
				}
			
		
			});
		
		
		
		});
		
		
	</script>
</head>
<body>

<!-- 标题显示 -->
<div id="Title_bar">
    <div id="Title_bar_Head">
        <div id="Title_Head"></div>
        <div id="Title"><!--页面标题-->
            <img border="0" width="13" height="13" src="${pageContext.request.contextPath}/style/images/title_arrow.gif"/> 配置权限
        </div>
        <div id="Title_End"></div>
    </div>
</div>

<!--显示表单内容-->
<div id=MainArea>

    <s:form action="roleAction_setPrivilege">
    	<s:hidden name="id"></s:hidden>
        
        <div class="ItemBlock_Title1"><!-- 信息说明 --><div class="ItemBlock_Title1">
        	<img border="0" width="4" height="7" src="${pageContext.request.contextPath}/style/blue/images/item_point.gif" /> 正在为【${role.name}】配置权限 </div> 
        </div>
        
        <!-- 表单内容显示 -->
        <div class="ItemBlockBorder">
            <div class="ItemBlock">
                <table cellpadding="0" cellspacing="0" class="mainForm">
					<!--表头-->
					<thead>
						<tr align="LEFT" valign="MIDDLE" id="TableTitle">
							<td width="300px" style="padding-left: 7px;">
								<!-- 全选 -->
								<input type="checkbox" onClick="$('[name=privilegeIds]').attr('checked', this.checked)"/>
								<label for="cbSelectAll">全选</label>
							</td>
						</tr>
					</thead>
                   
			   		<!--显示数据列表-->
					<tbody id="TableData">
						<tr class="TableDetail1">
							<!-- 显示权限树 -->
							<td>
							
<%-- 使用Struts2的自定义标签
<s:checkboxlist name="privilegeIds" list="#privilegeList" listKey="id" listValue="name"></s:checkboxlist>				
--%>

<%-- 直接写HTML，并自行实现回显效果 
	 value="${id}":${id}表示根据id从四个域中获取数据 
	 id="cb_${id}" ：与下面的label的for对应，表示点击汉字时也可以选中
	 <s:property value="%{id in privilegeIds ? 'checked' : ''}"/>：回显

<s:iterator value="#listPrivilege">
	<input type="checkbox" name="privilegeIds" value="${id}" id="cb_${id}" 
		<s:property value="%{id in privilegeIds ? 'checked' : ''}"/>
	>
	<label for="cb_${id}">${name}</label>
	<br/>
</s:iterator> 
--%>
<ul id="root">
<s:iterator value="#listTopPrivilege">
<%-- 第一级--%>
	<li>
		<input type="checkbox" name="privilegeIds" value="${id}" id="cb_${id}" 
			<s:property value="%{id in privilegeIds ? 'checked' : ''}"/>
		>
		<label for="cb_${id}"><span class="folder">${name}</span></label>

		<ul>
		<s:iterator value="children">
		<%-- 第二级--%>
			<li>
				<input type="checkbox" name="privilegeIds" value="${id}" id="cb_${id}" 
					<s:property value="%{id in privilegeIds ? 'checked' : ''}"/>
				>
				<label for="cb_${id}"><span class="folder">${name}</span></label>
				
				<ul>
				<s:iterator value="children">
		
				<%-- 第三级--%>
					<li>
						<input type="checkbox" name="privilegeIds" value="${id}" id="cb_${id}" 
							<s:property value="%{id in privilegeIds ? 'checked' : ''}"/>
						>
						<label for="cb_${id}"><span class="folder">${name}</span></label>
					</li>
					</s:iterator>
				</ul>
			</li>
			</s:iterator>
		</ul>
	</li>
	</s:iterator>
</ul>						
							
							</td>
						</tr>
					</tbody>
                </table>
            </div>
        </div>
        
        <!-- 表单操作 -->
        <div id="InputDetailBar">
            <input type="image" src="${pageContext.request.contextPath}/style/images/save.png"/>
            <a href="javascript:history.go(-1);"><img src="${pageContext.request.contextPath}/style/images/goBack.png"/></a>
        </div>
    </s:form>
</div>

<%-- $("#root").treeview();
	等价于：$(function(){
				$("#root").treeview();
			}
			);
			$("#root"):拿到id为root的元素
		这段代码代表当上面的div文档加载完，这段代码马上执行
		如果放在<head>中，则加载的速度慢一点，会加载完所有的文档再加载这个
--%>
<script type="text/javascript">
		$("#root").treeview();
</script>
<div class="Description">
	说明：<br />
	1，选中一个权限时：<br />
	&nbsp;&nbsp;&nbsp;&nbsp; a，应该选中他的所有直系上级。<br />
	&nbsp;&nbsp;&nbsp;&nbsp; b，应该选中他的所有直系下级。<br />
	2，取消选择一个权限时：<br />
	&nbsp;&nbsp;&nbsp;&nbsp; a，应该取消选择他的所有直系下级。<br />
	&nbsp;&nbsp;&nbsp;&nbsp; b，如果同级的权限都是未选择状态，就应该取消选中他的直接上级，并向上做这个操作。<br />

	3，全选/取消全选。<br />
	4，默认选中当前岗位已有的权限。<br />
</div>

</body>
</html>
