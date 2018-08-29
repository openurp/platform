[#ftl]
[@b.head/]

[@b.grid items=notices var="notice"]
  [@b.gridbar]
    bar.addItem("${b.text("action.info")}",action.info());
    bar.addItem("${b.text("action.new")}",action.add());
    bar.addItem("${b.text("action.modify")}",action.edit());
    bar.addItem("${b.text("action.delete")}",action.remove("确认删除?"));
  [/@]
  [@b.row]
    [@b.boxcol /]
    [@b.col width="40%" property="title" title="标题"][@b.a href="!info?id=${notice.id}"]${notice.title!}[/@][/@]
    [@b.col width="15%" property="domain.title" title="业务"/]
    [@b.col width="10%" property="userCategory.id" title="用户类别"]${notice.userCategory.name}[/@]
    [@b.col width="15%" property="publishedOn" title="发布日期"]${notice.publishedOn?string("yy-MM-dd")}[/@]
    [@b.col width="10%" property="sticky" title="是否置顶"]${notice.sticky?string("是","否")}[/@]
  [/@]
[/@]
[@b.foot/]
