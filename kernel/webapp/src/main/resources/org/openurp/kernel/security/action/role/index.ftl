[#ftl]
[@b.head/]
<table  class="indexpanel">
  <tr>
    <td class="index_view">
    [@b.form name="roleSearchForm"  action="!search" target="rolelist" title="ui.searchForm" theme="search"]
      [@b.textfields names="role.name;common.name,role.creator.name;common.creator"/]
      [@b.select name="role.app.id" label="应用" items=apps option="id,title" empty="..." style="width:100px"/]
    [/@]
    </td>
    <td class="index_content">[@b.div id="rolelist" href="!search?orderBy=role.indexno" /]</td>
  </tr>
</table>
[@b.foot/]