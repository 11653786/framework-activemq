<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2016/1/25 0025
  Time: 13:29
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>easyui练习</title>
    <link rel="stylesheet" type="text/css"
          href="${pageContext.request.contextPath}/static/css/themes/default/easyui.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/themes/icon.css">
    <script type="text/javascript" src="${pageContext.request.contextPath}/static/js/easyui/jquery.min.js"></script>
    <script type="text/javascript"
            src="${pageContext.request.contextPath}/static/js/easyui/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/static/js/util.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/static/list/user.js"></script>
</head>
<body>
<input type="button" value="s" id="aa" />
<div id="toolbar">
    <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true">添加</a>
    <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-help',plain:true">帮助</a>
</div>
<table id="datagrid">

</table>


</body>
</html>
