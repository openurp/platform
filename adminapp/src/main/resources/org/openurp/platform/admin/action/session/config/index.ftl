[#ftl]
[@b.head/]
<table class="indexpanel">
  <tr>
    <td class="index_view">
    [@b.form name="configSearchForm" action="!search" target="configlist" title="ui.searchForm" theme="search"]
      [@b.textfields names="sessionConfig.category.name;用户分类"/]
      <input type="hidden" name="orderBy" value="sessionConfig.category.name"/>
    [/@]
    </td>
    <td class="index_content">[@b.div id="configlist" href="!search?orderBy=sessionConfig.category.name"/]
    </td>
  </tr>
</table>
[@b.foot/]
