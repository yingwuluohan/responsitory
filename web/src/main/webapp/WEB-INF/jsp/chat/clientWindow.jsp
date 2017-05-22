<%--
  Created by IntelliJ IDEA.
  User: dongfangnan
  Date: 2017/5/19
  Time: 17:04
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <script src="/js/jquery-1.8.2.min.js"></script>
    <script src="/js/json2.js"></script>
    <script src="/js/json_parse.js"></script>
    <script>
        $(function(){

            $.post( "" ,{ },function(data){

            });
        });
    </script>
</head>
<body>
<form method="post" action="/chat/sendMessage" >
    <input name="message" type="text" size=50 >
    <input type="submit" value="send" >
</form>
</body>
</html>
