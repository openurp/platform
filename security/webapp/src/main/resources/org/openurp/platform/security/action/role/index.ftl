[#ftl]
[@b.head/]
[#include "../user-nav.ftl"/]
<table  class="indexpanel">
  <tr>
    <td class="index_view">
    [@b.form name="roleSearchForm"  action="!search" target="rolelist" title="ui.searchForm" theme="search"]
      [@b.select name="role.app.id" label="应用" items=apps option="id,title" style="width:100px"/]
      [@b.textfields names="role.name;common.name,role.creator.name;common.creator"/]
    [/@]
    </td>
    <td class="index_content">[@b.div id="rolelist" href="!search?role.app.id=${apps?first.id}&orderBy=role.indexno" /]</td>
  </tr>
</table>
[@b.foot/]