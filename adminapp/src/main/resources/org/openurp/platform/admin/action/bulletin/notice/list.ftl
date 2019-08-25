[#ftl]
[@b.head/]

[@b.grid items=notices var="notice"]
  [@b.gridbar]
    bar.addItem("${b.text("action.info")}",action.info());
    bar.addItem("起草",action.add());
    bar.addItem("${b.text("action.modify")}",action.edit());
    bar.addItem("${b.text("action.delete")}",action.remove("确认删除?"));
  [/@]
  [@b.row]
    [@b.boxcol /]
    [@b.col width="37%" property="title" title="标题"]
      [@b.a href="!info?id=${notice.id}"]${notice.title!}[/@]
      [#if notice.popup]<sup>弹窗</sup>[/#if]
      [#if notice.sticky]<sup>置顶</sup>[/#if]
    [/@]
    [@b.col width="12%" property="app.title" title="应用"/]
    [@b.col width="10%" property="userCategory.id" title="用户类别"]${notice.userCategory.name}[/@]
    [@b.col width="10%" property="createdAt" title="起草日期"]${notice.createdAt?string("yy-MM-dd")}[/@]
    [@b.col width="13%" title="有效期"]${notice.beginOn?string("MM-dd")}~${notice.endOn?string("MM-dd")}[/@]
    [@b.col width="5%" property="archived" title="归档"]${notice.archived?string("是","否")}[/@]
    [@b.col width="9%" property="status" title="状态"]${notice.status.title}[/@]
  [/@]
[/@]
[@b.foot/]
