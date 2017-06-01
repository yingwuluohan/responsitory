<%@ page import="com.fang.common.project.redis.PropertiesConfigUtils" %>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>WebSocket Chat</title>
</head>
<body>
<script type="text/javascript">
    var socket;
    if (!window.WebSocket) {
        window.WebSocket = window.MozWebSocket;
    }
    if (window.WebSocket) {
        socket = new WebSocket("ws://localhost:8090/ws");
        // 监听消息
        socket.onmessage = function(event) {
            var ta = document.getElementById('responseText');
            var ta2 = document.getElementById('lineText');
            var content = event.data;

            if( content.indexOf( "said:" ) == -1  ){
                ta.value = ta.value + '\n' + event.data
            }else{
                ta2.value = ta2.value + '\n' + event.data
            }



        };
        // 打开Socket
        socket.onopen = function(event) {
            var ta = document.getElementById('responseText');
            ta.value = "连接开启!";

            socket.send( "said:"+Math.random() + "上线");
        };
        socket.onclose = function(event) {
            var ta = document.getElementById('responseText');
            ta.value = ta.value + "连接被关闭";
        };

        // 添加一个连接监听器



    }

    function send(message) {
        if (!window.WebSocket) {
            return;
        }
        if (socket.readyState == WebSocket.OPEN) {
            socket.send(message);
        } else {
            alert("连接没有开启.");
        }
    }
</script>
<form onsubmit="return false;">
    <h3>WebSocket  ：</h3>
    <textarea id="responseText" style="width: 500px; height: 150px;"></textarea>
    <br>
    <br>
    <h3>WebSocket 在线人：</h3>
    <textarea id="lineText" style="width: 500px; height: 100px;"></textarea>
    <br>
    <input type="text" name="message"  style="width: 300px" value="Welcome to www.waylau.com">
    <input type="button" value="发送消息" onclick="send(this.form.message.value)">
    <input type="button" onclick="javascript:document.getElementById('responseText').value=''" value="清空聊天记录">
</form>
<br>
<br>
<a href="http://www.waylau.com/" > </a>
</body>
</html>
