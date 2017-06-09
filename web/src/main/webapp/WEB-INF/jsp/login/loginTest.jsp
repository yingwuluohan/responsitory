<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<body>

<div class="p-login-body">
    <div class="i-box">
        <form action="" id="formTest1" method="post" action="my.global.com:8080/login">
            <div class="p-layer">
                <h2>登录</h2>
                <ul>
                    <li> <input type="text" placeholder="请输入用户名/手机号" id="mobile" name="mobile" value="13258155657"/><em></em> </li>
                    <li> <input type="password" placeholder="请输入密码" id="password" name="password"/><em></em> </li>

                    <li><a href="javascript:;" class="p-btn" id="jp-sub" onclick="login()">登录</a></li>
                </ul>
            </div>
        </form>
    </div>
</div>
</body>
<script src="/js/jquery-1.8.2.min.js"></script>
<script src="/js/json2.js"></script>
<script src="/js/json_parse.js"></script>
<script type="text/javascript">
    function login(){
    	var mobileEmail = $("#mobile").val();
    	var password = $("#password").val();

        var turnForm = document.createElement("form");
        //一定要加入到body中！！
        document.body.appendChild(turnForm);
        turnForm.method = "get";
        turnForm.action = "/login";
        turnForm.target = '_self';
        //创建隐藏表单
        var subjectIdElement = document.createElement("input");
        subjectIdElement.setAttribute("name","mobileEmail");
        subjectIdElement.setAttribute("type","hidden");
        subjectIdElement.setAttribute("value",mobileEmail);
        turnForm.appendChild(subjectIdElement);
        turnForm.submit();
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


