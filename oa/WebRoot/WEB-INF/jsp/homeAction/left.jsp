<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s"  %>
<html>
<head>
	<title>导航菜单</title>
	<%@ include file="/WEB-INF/jsp/public/common.jspf"%>
	<link type="text/css" rel="stylesheet" href="style/blue/menu.css" />
	
	<script type="text/javascript">
		function menuClick( menuDiv ){
			<%--
				$(".MenuLevel2"):因为class="MenuLevel2" ，所以前面加.；代表把MenuLeve12封装为对象
				not( $(menuDiv).next() )：表示不是顶级菜单的下一个元素就隐藏【默认一进来就隐藏顶级菜单下的元素】
				$(menuDiv).next().toggle();表示顶级菜单的下一个元素自动切换，
			--%>
			// $(".MenuLevel2").not( $(menuDiv).next() ).hide();  
			$(menuDiv).next().toggle(); // show(), hide(), toggle()【包含show与hide，即隐藏与展示】
			
		}
	
		
	</script>
	
</head>
<body style="margin: 0">
<div id="Menu"> 

    <ul id="MenuUl">
		<%-- 
			顶级菜单
			#application.topPrivilegeList：从最大域中得到顶级菜单
			之所以从这里获取，是因为菜单没有变化，在服务器启动时就加载，只加载一次
		 --%>
		<s:iterator value="#application.topPrivilegeList">
		<s:if test="#session.user.hasPrivilegeByName(name)">
	        <li class="level1">
	            <div onClick="menuClick(this);" class="level1Style">
	            	<img src="style/images/MenuIcon/${icon}" class="Icon" /> 
	            	<%--从application域中取数据 --%>
	            	${name}
	            </div>
	            <%-- 二级菜单 display: none;不展示二级菜单 --%>
	            <ul style="" class="MenuLevel2">
	            	<s:iterator value="children">
	            	<%-- 
	            		#session.user.hasPrivilegeByName(name)
	            		登陆成功后，用户存在session域中，通过上面的方法可以判断用户是否有权限
	            		#session.user:表示有用户了。
	            		name是权限名
	            	--%>
	            	<s:if test="#session.user.hasPrivilegeByName(name)">
		                <li class="level2">
		                    <div class="level2Style">
			                    <img src="style/images/MenuIcon/menu_arrow_single.gif" /> 
			                    <a target="right" href="${pageContext.request.contextPath}/${url}.action"> ${name}</a>
		                 	</div>
		                </li>
	                </s:if>
	            	</s:iterator>
	            </ul>
	        </li>
        </s:if>
	</s:iterator>        
    </ul>
    
</div>
</body>
</html>