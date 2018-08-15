[@b.head/]
  [@b.toolbar title="修改账户"]
    [#if Parameters['updated']??]
    bar.addItem("退出","logout()")
    function logout(){
      document.location="${b.url('logout')}"
    }
   [/#if]
    bar.addClose();
  [/@]
  <div class="container">
   [@b.form name="accountForm" action="!save" theme="list" title="修改账户" ]
     [@b.field label="账户"]${principal.name} ${principal.description}[/@]
     [@b.password label="新密码" name="password" maxlength="15" required="true"/]
     [@b.password label="重复新密码" name="password2" maxlength="15" required="true"/]
     [@b.formfoot]
       [@b.submit value="提交" onsubmit="valid"/]
     [/@]
   [/@]
   <script>
     function valid(form){
       if(form['password2'].value!=form['password'].value){
         alert("新密码和重复密码不一致");
         return false;
       }
       return true;
     }
   </script>
  </div>
[@b.foot/]
