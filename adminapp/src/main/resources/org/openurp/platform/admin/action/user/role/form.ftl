[#ftl]
[@b.head/]
[@b.toolbar title="ui.roleInfo"]bar.addBack();[/@]
    [@b.form action=b.rest.save(role) title="ui.roleInfo" theme="list"]
      [@b.textfield name="role.name" label="common.name" value="${role.name!}" required="true" maxlength="50"/]
      [@b.radios label="common.status"  name="role.enabled" value=role.enabled items="1:action.activate,0:action.freeze" comment="冻结会禁用该角色及其所有下级"/]
      [@b.select label="上级角色" name="parent.id" value=(role.parent.id)! style="width:200px;"  items=parents?sort_by("indexno") option=r"${item.indexno} ${item.name}" empty="..."/]
      [@b.textfield label="同级顺序号" name="indexno" value="${role.index!}" required="true" maxlength="2" check="match('integer').range(1,100)" /]
      [@b.textfield name="role.remark" label="common.remark" value="${role.remark!}" maxlength="50"/]
      [@b.formfoot]
        [@b.reset/]&nbsp;&nbsp;[@b.submit value="action.submit"/]
      [/@]
    [/@]
[@b.foot/]
