[#ftl]
[@b.head/]
[@b.toolbar title="数据源"/]
<table class="indexpanel">
  <tr>
    <td class="index_view">
    [@b.form name="dataSourceCfgSearchForm" action="!search" target="dataSourceCfglist" title="ui.searchForm" theme="search"]
      [@b.textfields names="dataSourceCfg.name;名称"/]
      [@b.textfields names="dataSourceCfg.url;地址"/]
      <input type="hidden" name="orderBy" value="name"/>
    [/@]
    </td>
    <td class="index_content">[@b.div id="dataSourceCfglist" href="!search?orderBy=name"/]
    </td>
  </tr>
</table>
[@b.foot/]