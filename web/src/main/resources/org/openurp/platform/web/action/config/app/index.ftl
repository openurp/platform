[#ftl]
[@b.head/]
<table class="indexpanel">
  <tr>
    <td class="index_view">
    [@b.form name="dataSourceAppSearchForm" action="!search" target="dataSourceApplist" title="ui.searchForm" theme="search"]
      [@b.textfields names="app.name;名称,app.title;标题"/]
      [@b.select name="app.appType" label="类型" items={'web-service':'web服务 ','web-app':'web应用'} empty="..."/]
      <input type="hidden" name="orderBy" value="name"/>
    [/@]
    </td>
    <td class="index_content">[@b.div id="dataSourceApplist" href="!search?orderBy=name"/]
    </td>
  </tr>
</table>
[@b.foot/]
