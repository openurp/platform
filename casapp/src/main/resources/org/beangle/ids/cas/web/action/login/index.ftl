<!DOCTYPE html>
<html lang="zh_CN">
  <head>
    <meta charset="utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <meta http-equiv="pragma" content="no-cache"/>
    <meta http-equiv="cache-control" content="no-cache"/>
    <meta http-equiv="content-style-type" content="text/css"/>
    <meta http-equiv="content-script-type" content="text/javascript"/>
    <meta http-equiv="expires" content="0"/>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <title>教学管理系统</title>
    ${b.css("bootstrap","css/bootstrap.min.css")}
    ${b.css("bootstrap","css/bootstrap-theme.min.css")}
    ${b.css("openurp-default","css/login.css")}
    <!--[if lt IE 9]>
    <script src="https://oss.maxcdn.com/html5shiv/3.7.3/html5shiv.min.js"></script>
    <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
  </head>
<body>

<div class="logindiv">
    <div class="bulletin">
        <img style="width:100%;height:80px" src="${b.static_url('urp','images/banner.jpg')}"/>
        <table><tr><td><img style="width:100%;height:230px" src="${b.static_url('urp','images/bg.jpg')}"/></td></tr></table>
    </div>
    <div class="login">
   <img style="width:182px;height:35px;margin-top:22px;margin-bottom:23px" src="${b.static_url('openurp-default','images/banner.png')}"/>
     <form name="loginForm" action="${base}/login" target="_top" method="post">
     [#if Parameters['sid_name']??]<input type="hidden" name="sid_name" value="${Parameters['sid_name']}">[/#if]
     [#if Parameters['service']??]<input type="hidden" name="service" value="${Parameters['service']}">[/#if]
        <table class="logintable">
            <tr style="height:30px">
                <td colspan="2" style="text-align:center;color:red;">${error!}</td>
            </tr>
            <tr>
                <td><label for="username">用户名:&nbsp;</label></td>
                <td>
                    <input name="username" id="username" tabindex="1" autofocus="autofocus" title="请输入用户名" type="text" value="${(Parameters['username']?html)!}" style="width:105px;"/>
                </td>
            </tr>
            <tr>
                <td><label for="password">密　码:&nbsp;</label></td>
                <td><input id="password" name="password"  tabindex="2" type="password" style="width:105px;"/></td>
            </tr>
            <tr>
                <td colspan="2">
                    <input type="submit" name="submitBtn" tabindex="6" class="blue-button"  onclick="checkLogin(this.form)" value="登录"/>
                </td>
            </tr>
        </table>
        <table class="foottable">
            <tr>
                <td><img src="${b.static_url('urp','images/weixin.png')}" height="80px"></td>
            </tr>
        </table>
     </form>
   </div>
</div>

<script type="text/javascript">
    var form  = document.loginForm;
    function checkLogin(form){
        if(!form['username'].value){
            alert("用户名称不能为空");return false;
        }
        if(!form['password'].value){
            alert("密码不能为空");return false;
        }
        return true;
    }
</script>
</body>
</html>