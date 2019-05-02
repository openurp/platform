[#ftl]
[@b.head/]

[@b.grid items=docs var="doc"]
  [@b.gridbar]
    bar.addItem("${b.text("action.new")}",action.add());
    bar.addItem("${b.text("action.modify")}",action.edit());
    bar.addItem("${b.text("action.delete")}",action.remove("确认删除?"));
  [/@]
  [@b.row]
    [@b.boxcol /]
    [@b.col width="35%" property="name" title="标题"][@b.a href="!info?id=${doc.id}" target="_blank"]${doc.name!}[/@][/@]
    [@b.col width="12%" property="app.title" title="应用"/]
    [@b.col width="10%" property="userCategory.id" title="用户类别"]${doc.userCategory.name}[/@]
    [@b.col width="10%" property="uploadBy" title="上传人"]${doc.uploadBy.name}[/@]
    [@b.col width="15%" property="updatedAt" title="上传时间"]${doc.updatedAt?string("yy-MM-dd HH:mm")}[/@]
    [@b.col width="8%" property="archived" title="是否归档"]${doc.archived?string("是","否")}[/@]
  [/@]
[/@]
[@b.foot/]
