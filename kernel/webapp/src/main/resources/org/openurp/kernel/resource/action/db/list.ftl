[#ftl]
[@b.head/]
[@b.grid items=dbs var="db"]
  [@b.gridbar]
    bar.addItem("${b.text("action.new")}",action.add());
    bar.addItem("${b.text("连接测试")}",action.multi("test"));
    bar.addItem("${b.text("action.modify")}",action.edit());
    bar.addItem("${b.text("action.delete")}",action.remove("确认删除?"));
  [/@]
  [@b.row]
    [@b.boxcol /]
    [@b.col width="30%" property="name" title="名称"][@b.a href="!info?id=${db.id}"]${db.name!}[/@][/@]
    [@b.col width="30%" property="url" title="URL"]${db.url!}[/@]
    [@b.col width="20%" property="username" title="用户名"]${db.username!}[/@]
    [@b.col width="20%" property="remark" title="备注"]${db.remark!}[/@]
  [/@]
[/@]
[@b.foot/]
