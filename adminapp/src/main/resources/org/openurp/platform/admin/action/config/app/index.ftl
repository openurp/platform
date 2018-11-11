[#ftl]
[@b.head/]
<table class="indexpanel">
  <tr>
    <td class="index_view">
    [@b.form name="dataSourceAppSearchForm" action="!search" target="dataSourceApplist" title="ui.searchForm" theme="search"]
      [@b.textfields names="app.name;名称,app.title;标题"/]
      [@b.select name="app.appType.id" label="类型" items=appTypes empty="..."/]
      [@b.select name="app.domain.id" label="领域" items=domains empty="..."/]
      <input type="hidden" name="orderBy" value="name"/>
    [/@]
    </td>
    <td class="index_content">[@b.div id="dataSourceApplist" href="!search?orderBy=name"/]
    </td>
  </tr>
</table>
[@b.foot/]
