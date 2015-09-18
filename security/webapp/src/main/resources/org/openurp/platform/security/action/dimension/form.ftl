[#ftl]
[@b.head/]
[@b.toolbar title="数据限制域元信息配置"]bar.addBack("${b.text("action.back")}");[/@]
[@b.form action="!save" theme="list"]
  [@b.textfield label="common.name" name="dimension.name" value="${dimension.name!}" required="true" maxlength="50"/]
  [@b.textfield label="标题" name="dimension.title" value="${dimension.title!}" required="true" maxlength="50"/]
  [@b.textfield  label="类型" name="dimension.typeName" value=dimension.typeName! required="true" style="width:300px"/]
  [@b.textfield label="关键字" name="dimension.keyName" value="${dimension.keyName!}" maxlength="50" style="width:50px"/]
  [@b.textfield label="其他属性" name="dimension.properties" value="${(dimension.properties)!}" style="width:300x;" maxlength="100" comment="多个属性用,分割"/]
  [@b.textfield label="数据源" name="dimension.source" value="${(dimension.source)!}" style="width:400px;" maxlength="100" comment="基本类型，此处可以为空;使用oql:表示对象查询,sql:表示sql查询"/]
  [@b.radios label="是否允许多值" name="dimension.multiple"  value=dimension.multiple/]
  [@b.select2 label="应用系统" name1st="appId1st" name2nd="appId2nd" 
     items1st=apps items2nd= dimension.apps
     option="id,title"/]
  [@b.formfoot]
    <input type="hidden" name="dimension.id" value="${(dimension.id)!}"/>
    [@b.reset/]&nbsp;&nbsp;[@b.submit value="action.submit" /]
  [/@]
</table>
[/@]
[@b.foot/]