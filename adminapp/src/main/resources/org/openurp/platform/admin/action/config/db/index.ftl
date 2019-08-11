[#ftl]
[@b.head/]
[#include "../nav.ftl"/]
<table class="indexpanel">
  <tr>
    <td class="index_view">
    [@b.form name="dbSearchForm" action="!search" target="dblist" title="ui.searchForm" theme="search"]
      [@b.textfields names="db.name;名称"/]
      [@b.textfields names="db.url;地址"/]
      <input type="hidden" name="orderBy" value="name"/>
    [/@]
    </td>
    <td class="index_content">[@b.div id="dblist" href="!search?orderBy=name"/]
    </td>
  </tr>
</table>
[@b.foot/]
