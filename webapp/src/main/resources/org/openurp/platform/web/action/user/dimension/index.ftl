[#ftl]
[@b.head/]
<table class="indexpanel">
<tr>
  <td class="index_view">
  [@b.form name="searchForm" action="!search" title="ui.searchForm" target="dimensionList" theme="search"]
    [@b.textfields names="dimension.name;名称,dimension.title;标题"/]
  [/@]
  </td>
  <td class="index_content">
  [@b.div id="dimensionList" href="!search" /]
  </td>
</tr>
</table>
[@b.foot/]