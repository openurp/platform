[#ftl]
[@b.head/]
[@b.toolbar title="新建/修改资源"]bar.addBack();[/@]
<div style="width:850px">
[#assign saveAction][#if resource.persisted]!update?id=${resource.id}[#else]!save[/#if][/#assign]
[@b.form action=saveAction title="数据资源信息" theme="list"]
  [@b.textfield name="resource.name" required="true" label="common.name" value="${resource.name!}" maxlength="50" style="width:250px" comment="资源名称唯一"/]
  [@b.textfield name="resource.title" required="true" label="标题" value="${resource.title!}" maxlength="50"/]
  [@b.textfield label="类型" name="resource.typeName" value="${(resource.typeName)!}" required="true" style="width:400px" /]
  [@b.select label="领域" name="resource.domain.id" value=resource.domain!  option="id,title" empty="..." items=domains width="200px" required="true"/]
  [@b.radios label="common.status" name="resource.enabled" value=resource.enabled items="1:action.activate,0:action.freeze"/]
  [@b.textfield name="resource.remark" label="common.remark" value="${resource.remark!}" maxlength="50"/]
  [@b.formfoot]
    [@b.reset/]&nbsp;&nbsp;[@b.submit value="action.submit"/]
  [/@]
[/@]
</div>
[@b.foot/]
