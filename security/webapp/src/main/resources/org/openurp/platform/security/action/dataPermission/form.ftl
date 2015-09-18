[#ftl]
[@b.toolbar title="ui.dataPermission.info"]
bar.addBack("${b.text("action.back")}");
[/@]
[@b.form action="!save" theme="list"]
  [@b.select label="关联数据对象" name="permission.resource.id" value=permission.resource! option=r"${item.name} ${item.title}" required="true" items=dataResources?sort_by("title") width="250px"/]
  [@b.select label="角色" name="permission.role.id" value=permission.role! empty="..." items=roles?sort_by("indexno") width="200px"/]
  [@b.select label="entity.funcResource" name="permission.funcResource.id" empty="..." option=r"${item.name} ${item.title!}" value=permission.funcResource! items=funcResources?sort_by("name") width="200px"/]
  [@b.textarea label="过滤条件" name="permission.filters" required="true" value="${permission.filters!}" maxlength="400" rows="4" style="width:500px;" comment="使用{alias}表示查询对象"/]
  [@b.startend label="生效时间范围" name="permission.beginAt,permission.endAt" start=permission.beginAt! end=permission.endAt! required="true,false"/]
  [@b.textfield label="common.remark" name="permission.remark" value="${permission.remark!}" maxlength="50" width="100px"/]
  [@b.formfoot]
    [#if permission.id>0]
    <input type="hidden" name="permission.id" value="${permission.id}"/>
    [/#if]
    [@b.reset/]&nbsp;&nbsp;[@b.submit value="action.submit" /]
  [/@]
</table>
[/@]