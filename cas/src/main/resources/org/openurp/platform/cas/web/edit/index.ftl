[@b.head/]
  [@b.toolbar title="修改账户密码"]
    [#if Parameters['updated']??]
    bar.addItem("退出","logout()")
    function logout(){
      document.location="${b.url('logout')}"
    }
   [/#if]
    bar.addClose();
  [/@]
  <div class="container">
   [@b.form name="accountForm" action="!save" theme="list" title="修改账户密码" ]
     [@b.field label="账户"]${principal.name} ${principal.description}[/@]
     [@b.password label="新密码" name="password" maxlength="15" required="true" comment="请输入包含数字、大写字母、小写字母、特殊符号的至少两种的、8位以上的密码"/]
     [@b.password label="重复新密码" name="password2" maxlength="15" required="true"/]
     [@b.formfoot]
       [#if Parameters['service']??]
       <input type="hidden" name="service" value="${Parameters['service']?html}"/>
       [/#if]
       [@b.submit value="提交" onsubmit="valid"/]
     [/@]
   [/@]
   <script>
     function valid(form){
       if(form['password2'].value!=form['password'].value){
         alert("新密码和重复密码不一致");
         return false;
       }
       if(checkPassword(form['password'].value).indexOf("Weak") > -1){
          alert("检测出弱密码，请输入包含数字、大写字母、小写字母、特殊符号的至少两种的、8位以上的密码");
          return false;
       }
       return true;
     }
     function checkPassword(password){
        var strengthValue = {
          'caps': false,
          'special': false,
          'numbers': false,
          'small': false
        };
        var strength = {
                  0: 'Very Weak',
                  1: 'Weak',
                  2: 'Medium',
                  3: 'Strong',
                  4: 'Very Strong'
                };
        if(password.length < 8) {
          return "Very Weak";
        }
        for(var index=0; index < password.length; index++) {
          let char = password.charCodeAt(index);
          if(!strengthValue.caps && char >= 65 && char <= 90) {
              strengthValue.caps = true;
          } else if(!strengthValue.numbers && char >=48 && char <= 57){
            strengthValue.numbers = true;
          } else if(!strengthValue.small && char >=97 && char <= 122){
            strengthValue.small = true;
          } else if(!strengthValue.special && (char >=33 && char <= 47) || (char >=58 && char <= 64)) {
            strengthValue.special = true;
          }
        }
        var strengthIndicator = 0;
        for(var metric in strengthValue) {
          if(strengthValue[metric] === true) {
            strengthIndicator++;
          }
        }
        return strength[strengthIndicator];
     }
   </script>
  </div>
[@b.foot/]
