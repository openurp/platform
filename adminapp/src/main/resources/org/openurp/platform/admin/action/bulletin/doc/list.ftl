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
    [@b.col width="43%" property="name" title="标题"][@b.a href="!info?id=${doc.id}" target="_blank"]${doc.name!}[/@][/@]
    [@b.col width="12%" property="app.title" title="应用"/]
    [@b.col width="15%" title="用户类别"][#list doc.userCategories as uc]${uc.name}[#if uc_has_next]&nbsp;[/#if][/#list][/@]
    [@b.col width="10%" property="uploadBy" title="上传人"]${doc.uploadBy.name}[/@]
    [@b.col width="15%" property="updatedAt" title="上传时间"]${doc.updatedAt?string("yy-MM-dd HH:mm")}[/@]
  [/@]
[/@]
[@b.foot/]
