[#ftl]
[@b.head/]
[@b.grid items=apps var="app"]
  [@b.gridbar]
    bar.addItem("${b.text("action.new")}",action.add());
    bar.addItem("${b.text("action.modify")}",action.edit());
    bar.addItem("${b.text("action.delete")}",action.remove("确认删除?"));
  [/@]
  [@b.row]
    [@b.boxcol /]
    [@b.col width="30%" property="name" title="名称"][@b.a href="!info?id=${app.id}"]${app.name!}[/@][/@]
    [@b.col width="70%" property="remark" title="备注"]${app.remark!}[/@]
  [/@]
[/@]
[@b.foot/]
