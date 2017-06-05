<%@ page import="com.fang.common.project.redis.PropertiesConfigUtils" %>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<fe:html title="登录" initSeajs="true" ie="true" defaultHead="<!DOCTYPE html><html>"
         assets="/project/b-ms-cloud/1.x/css/t-login/page.css">
<body>
<div class="p-header">
    <div class="i-box">
        <a class="p-lg fl" href="#" target="_bank">
            <img src="<%=PropertiesConfigUtils.getProperty("domains.ui") %>/project/b-ms-cloud/1.x/i/i-logo.png">
        </a>
        <b class="p-b fl"></b>
    </div>
</div>
<div class="p-login-body">
    <div class="i-box">
        <form action="" id="jp-login-form" method="post">
            <div class="p-layer">
                <h2>登录</h2>
                <ul>
                    <li><div class="p-inp"><input type="text" placeholder="请输入用户名/手机号" id="jp-tel"/><em></em></div></li>
                    <li><div class="p-inp"><input type="password" placeholder="请输入密码" id="jp-pass"/><em></em></div></li>

                    <li><a href="javascript:;" class="p-btn" id="jp-sub">登录</a></li>
                </ul>
                <div class="fc p-link">
                    <div class="fl"><a href="javascript:;" onclick="toBackPwd('${ssoUrl}','<%="domain"%>');" target="_blank">忘记密码</a></div>
                    <div class="fr"><a href="/registPage">注册</a></div>
                </div>
            </div>
        </form>
    </div>
</div>
</body>
<script type="text/javascript">
    seajs.config({
        alias:{
            'project/b-ms-cloud/1.x/js/t-login/page': 'project/b-ms-cloud/1.x/js/t-login/page.js'
        }
    });
    seajs.use(['project/b-ms-cloud/1.x/js/t-login/page'],function(app){
        var json = {
            "login" :  login,
            'yzmAjaxUrl':'/randomLogin?uuid=${uuid}'//验证码请求地址
        };
        app.init(json);
    });
</script>
<script type="text/javascript">
    function login(){
    	var mobileEmail = $('#jp-tel').val();
    	var password = $('#jp-pass').val();
    	var random = $('#jp-yzm').val();
    	$.ajax({
    	    url:'/login',
    	    type:'post',
    	    async: false,
    	    dataType:'json',
    	    data:{
    	    	mobileEmail : mobileEmail,
    	        password : password,
    	        uuid:'${uuid}'
    	    },
    	    success:function(data){
    	        if(data.success==true){
                    window.location="/first";
    	        }else{
    	        	changeRandom();
    	        	//console.info(data.errMsg);
    	        	alert(data.errMsg);
    	        }
    	    }
    	})
    }

    function regLogin(data){
    	var ssoUrl = '${ssoUrl}/quickLogin.do';
    	var userName = data.userName;
    	var password = data.password;
        var result =data.system;//== 'cloud'
        var type = data.userType;
    	$.ajax({
    		url: ssoUrl,
    		async :false,
    		cache:false,
    		type:'post',
    		dataType: 'jsonp',
    	    data:{
                userName : userName,
                password : password,
                type:"jsonp"
            },
            success:function(dataSso){
            	if(dataSso=='success'&& result == 'koolearn' || ( dataSso=='success'&& result == 'cloud'&& type == 0  )  ){//
                    window.location="/makeSureUserRole?userId=" + data.userId;
                }else if(dataSso=='success'&& result == 'cloud' ){
                    //alert("跳转");
                    window.location="/first";
                }
            }
    	})
    }
    
    function changeRandom(){
        var randomImg = $("#randomImg");
        randomImg.attr("src", "/random?uuid=${uuid}&time="+new Date().getTime());
     }

</script>
</fe:html>

