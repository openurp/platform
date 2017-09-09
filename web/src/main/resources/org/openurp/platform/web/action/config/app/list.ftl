[#ftl]
[@b.head/]

[#assign types = {"web-service":"web服务","web-app":"web应用"} ]

[@b.grid items=apps var="app"]
  [@b.gridbar]
    bar.addItem("${b.text("action.new")}",action.add());
    bar.addItem("${b.text("action.modify")}",action.edit());
    bar.addItem("${b.text("action.delete")}",action.remove("确认删除?"));
  [/@]
  [@b.row]
    [@b.boxcol /]
    [@b.col width="10%" property="indexno" title="分类号"/]
    [@b.col width="30%" property="name" title="名称"][@b.a href="!info?id=${app.id}"]${app.name!}[/@][/@]
    [@b.col width="20%" property="title" title="标题"/]
    [@b.col width="15%" property="domain.title" title="领域"/]
    [@b.col width="10%" property="appType" title="类型"]${types[app.appType]!'unknown'}[/@]
    [@b.col width="10%" property="enabled" title="是否可用" /]
  [/@]
[/@]
[@b.foot/]
