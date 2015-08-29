[#ftl]
[@b.head loadui=false/]
<div class="logindiv">
    [@b.form name="loginForm" action="!login" target="_top"]
        <table class="bulletin">
            <tr>
                <td>
                </td>
            </tr>
        </table>
        <table class="logintable">
            <tr>
                <td colspan="2" style="text-align:center;color:red;">[@b.messages/]</td>
            </tr>
            <tr>
                <td><label for="username">用户名:&nbsp;</label></td>
                <td>
                    <input name="username" id="username" tabindex="1" title="请输入用户名" type="text" value="${name!}" style="width:105px;"/>
                </td>
            </tr>
            <tr>
                <td>
                    <label for="password">密　码:&nbsp;</label>
                </td>
                <td>
                    <input id="password" name="password"  tabindex="2" type="password" style="width:105px;"/>
                    <input name="encodedPassword" type="hidden" value=""/>
                </td>
            </tr>
            <tr>
                <td colspan="2">
                    [@b.submit name="submitBtn" tabindex="6" class="blue-button" value="登录" onsubmit="checkLogin"][/@]
                </td>
            </tr>
        </table>
        <table class="footage">
            <tr>
                <td style="text-align:right">
                </td>
            </tr>
        </table>
    [/@]
</div>

<div class="foot"></div>

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
    if("${locale.language}".indexOf("en")!=-1){document.getElementById('local_en').checked=true;}
    var username=beangle.cookie.get("username");
    if(null!=username){ form['username'].value=username;}
    (function(){
        // 侦测登陆页面是否被嵌套了
        if(jQuery("body > div:not(.logindiv,.foot,.browser-hint)").length) {
            window.location = "${base}/login.action?v=1";
        }
    })();
</script>
[@b.foot/]
