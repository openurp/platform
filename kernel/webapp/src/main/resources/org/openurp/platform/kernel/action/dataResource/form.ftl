[#ftl]
[@b.head/]
[#include "scope.ftl"/]
[@b.toolbar title="新建/修改资源"]bar.addBack();[/@]
<div style="width:850px">
[@b.form action="!save" title="app.funcresource.info" theme="list"]
  [@b.textfield name="resource.name" required="true" label="common.name" value="${resource.name!}" maxlength="50" style="width:250px" comment="资源名称唯一"/]
  [@b.textfield name="resource.title" required="true" label="标题" value="${resource.title!}" maxlength="50"/]
  [@b.textfield label="类型" name="resource.typeName" value="${(resource.typeName)!}" required="true" style="width:400px" /]
  [@b.radios label="可见范围" name="resource.scope" items=scopeTitles value=(resource.scope?string)!'PRIVATE'/]
  [@b.radios label="common.status" name="resource.enabled" value=resource.enabled items="1:action.activate,0:action.freeze"/]
  [@b.textfield name="resource.remark" label="common.remark" value="${resource.remark!}" maxlength="50"/]
  [@b.formfoot]
    [@b.reset/]&nbsp;&nbsp;[@b.submit value="action.submit"/]
    <input type="hidden" name="resource.id" value="${(resource.id)!}" />
  [/@]
[/@]
</div>
[@b.foot/]