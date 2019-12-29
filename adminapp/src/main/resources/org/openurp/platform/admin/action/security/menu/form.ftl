[#ftl]
[@b.head/]
[@b.toolbar title="info.moduleUpdate"]bar.addBack();[/@]
[#assign userMsg]${b.text("entity.menu")}[/#assign]
[#assign labelInfo]${b.text("ui.editForm",userMsg)}[/#assign]

[@b.form action=b.rest.save(menu) title=labelInfo theme="list"]
  [@b.textfield label="common.name" name="menu.name" value="${menu.name!}" style="width:200px;"  required="true" maxlength="100" /]
  [@b.textfield label="标题" name="menu.title" value="${menu.title!}" style="width:200px;" required="true" maxlength="50"/]
  [@b.select label="上级菜单" name="parent.id" value=(menu.parent)! style="width:200px;"  items=parents option="id,description" empty="..."/]
  [@b.number label="同级顺序号" name="indexno" value="${menu.lastindex!}" required="true" maxlength="2" min="1" max="100" /]
  [@b.radios label="common.status"  name="menu.enabled" value=menu.enabled items="1:action.activate,0:action.freeze" comment="冻结会禁用该菜单及其所有下级"/]
  [@b.select label="menu.entry"   empty="..." name="menu.entry.id" value="${(menu.entry.id)!}" items=resources?sort_by("name") option="id,description" style="width:400px;"  /]
  [@b.select2 label="使用资源" name1st="Resources" name2nd="resource.id" items1st=alternatives?sort_by("name") items2nd = menu.resources?sort_by("name") option="id,description"/]
  [@b.textarea label="common.remark"  name="menu.remark" maxlength="50" value=menu.remark! rows="2" cols="40"/]
  [@b.formfoot]
    [@b.reset/]&nbsp;&nbsp;
    <input type="hidden" name="menu.app.id" value="${menu.app.id}"/>
    [@b.submit value="action.submit" /]
  [/@]
[/@]
[@b.foot/]
