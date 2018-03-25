[#ftl]
[@b.head/]
[@b.toolbar title="修改用户"]bar.addBack("${b.text("action.back")}");[/@]
[@b.form name="userForm" action="!update" class="listform" theme="list"]
    <input type="hidden" name="user.id" value="${user.id}" />
    [@b.textfield name="user.code" value="${user.code}" style="width:200px;" required="true" maxlength="30"/]
    [@b.textfield name="user.name" value="${user.name!}" style="width:200px;" required="true" maxlength="30"/]
    [@b.select name="user.category.id" label="user.category.name" value=user.category! items=categories /]
    [@b.radios name="user.enabled" value=user.enabled items="1:action.activate,0:action.freeze"/]
    [@b.radios name="user.locked" value=user.locked items="1:锁定,0:解锁"/]
    [@b.password label="user.password" name="password" value="" maxlength="20" showStrength="true"/]
    [@b.startend label="user.beginOn-endOn" name="user.beginOn,user.endOn" required="true,false" start=user.beginOn end=user.endOn format="date"/]
    [@b.datepicker name="user.passwordExpiredOn" value=user.passwordExpiredOn format="date"/]
    [@b.textarea cols="50" rows="1" name="user.remark" value=user.remark! maxlength="50"/]
    [@b.formfoot][@b.reset/]&nbsp;&nbsp;[@b.submit value="action.submit"/][/@]
[/@]
[@b.foot/]
