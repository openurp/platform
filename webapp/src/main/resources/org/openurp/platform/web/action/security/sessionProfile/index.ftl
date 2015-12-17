[#ftl]
[@b.head/]
<table  class="indexpanel">
  <tr>
    <td class="index_view">
    [@b.form name="sessonProfileSearchForm"  action="!search" target="sessonProfilelist" title="ui.searchForm" theme="search"]
      [@b.select name="profile.app.id" label="应用" items=apps option="id,title" style="width:100px"/]
      [@b.textfields names="profile.capacity;上限"/]
    [/@]
    </td>
    <td class="index_content">[@b.div id="sessonProfilelist" href="!search?profile.app.id=${apps?first.id}" /]</td>
  </tr>
</table>
[@b.foot/]