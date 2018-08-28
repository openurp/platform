[#ftl]
[@b.head/]
<table class="indexpanel">
  <tr>
    <td class="index_view">
    [@b.form name="docSearchForm" action="!search" target="doclist" title="ui.searchForm" theme="search"]
      [@b.textfields names="doc.name;标题"/]
      [@b.select label="用户类别" name="doc.userCategory.id" items=userCategories/]
      <input type="hidden" name="orderBy" value="doc.updatedAt desc"/>
    [/@]
    </td>
    <td class="index_content">[@b.div id="doclist" href="!search?orderBy=doc.updatedAt desc"/]
    </td>
  </tr>
</table>
[@b.foot/]
