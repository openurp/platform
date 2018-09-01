[#ftl]
[@b.head/]
<table class="indexpanel">
  <tr>
    <td class="index_view">
    [@b.form name="noticeSearchForm" action="!search" target="noticelist" title="ui.searchForm" theme="search"]
      [@b.textfields names="notice.title;标题"/]
      [@b.select label="用户类别" name="notice.userCategory.id" items=userCategories/]
      <input type="hidden" name="orderBy" value="notice.publishedOn desc"/>
    [/@]
    </td>
    <td class="index_content">[@b.div id="noticelist" href="!search?orderBy=notice.publishedOn desc"/]
    </td>
  </tr>
</table>
[@b.foot/]
