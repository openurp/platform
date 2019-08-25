[#ftl]
[@b.head/]
<table class="indexpanel">
  <tr>
    <td class="index_view">
    [@b.form name="noticeSearchForm" action="!search" target="noticelist" title="ui.searchForm" theme="search"]
      [@b.textfields names="notice.title;标题"/]
      [@b.select label="用户类别" name="notice.userCategory.id" items=userCategories  empty="..."/]
      [@b.select label="应用" name="notice.app.id" items=apps option="id,title" empty="..."/]
      [@b.select label="状态" name="notice.status" items={"0":"草稿","1":"提交","2":"审核不通过","3":"审核通过"}  empty="..."/]
      [@b.textfield label="起草人" name="notice.operator.name" /]
      <input type="hidden" name="orderBy" value="notice.publishedAt desc"/>
    [/@]
    </td>
    <td class="index_content">[@b.div id="noticelist" href="!search?orderBy=notice.createdAt desc"/]
    </td>
  </tr>
</table>
[@b.foot/]
