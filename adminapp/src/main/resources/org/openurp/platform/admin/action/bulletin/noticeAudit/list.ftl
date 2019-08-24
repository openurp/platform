[#ftl]
[@b.head/]

[@b.grid items=notices var="notice"]
  [@b.gridbar]
    bar.addItem("${b.text("action.info")}",action.info());
    bar.addItem("审核通过",action.multi('audit','确定审核通过?',"passed=1"));
    bar.addItem("退回",action.multi('audit','确定退回?',"passed=0"));
  [/@]
  [@b.row]
    [@b.boxcol /]
    [@b.col width="37%" property="title" title="标题"]
      [@b.a href="!info?id=${notice.id}"]${notice.title!}[/@]
      [#if notice.popup]<sup>弹窗</sup>[/#if]
    [/@]
    [@b.col width="12%" property="app.title" title="应用"/]
    [@b.col width="10%" property="userCategory.id" title="用户类别"]${notice.userCategory.name}[/@]
    [@b.col width="15%" property="createdAt" title="起草日期"]${notice.createdAt?string("yy-MM-dd")}[/@]
    [@b.col width="5%" property="sticky" title="置顶"]${notice.sticky?string("是","否")}[/@]
    [@b.col width="5%" property="archived" title="归档"]${notice.archived?string("是","否")}[/@]
    [@b.col width="6%" property="status" title="状态"]${notice.status.title}[/@]
  [/@]
[/@]
[@b.foot/]
