[#ftl]
[@b.head/]
[@b.toolbar title="修改数据源"]bar.addBack();[/@]
<style>form.listform label.title{width:120px;}</style>
[@b.tabs]
  [@b.form action="!update?id=${db.id}" theme="list"]
    [@b.textfield name="db.name" label="名称" value="${db.name!}" required="true" maxlength="200"/]
    [@b.textfield name="db.url" label="URL" value="${db.url!}" required="true" maxlength="200" style="width:300px;"/]
    [@b.textfield name="db.driverClassName" label="DriverClassName" value="${db.driverClassName!}" required="true" maxlength="200"/]
    [@b.textarea name="db.remark" label="备注" value="${db.remark!}" maxlength="200"/]
    [@b.formfoot]
      [@b.reset/]&nbsp;&nbsp;[@b.submit value="action.submit"/]
    [/@]
  [/@]
[/@]
[@b.foot/]