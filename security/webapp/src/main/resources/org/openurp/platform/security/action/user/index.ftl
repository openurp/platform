[#ftl]
[@b.head/]
[#include "../user-nav.ftl"/]
<div>
<table class="indexpanel">
<tr>
  <td class="index_view">
  [@b.form name="userSearchForm" action="!search" title="ui.searchForm" target="userlist" theme="search"]
    [@b.select name="app.id" label="应用" items=apps option="id,title" style="width:100px"/]
    [@b.textfields names="user.code,user.name,roleName;user.roles"/]
    [@b.select name="user.enabled" label="common.status" value="1" empty="..." items={'1':'${b.text("action.activate")}','0':'${b.text("action.freeze")}'}/]
  [/@]
  </td>
  <td class="index_content">
  [@b.div id="userlist" href="!search?user.enabled=1&app.id=${apps?first.id}" /]
  </td>
</tr>
</table>
</div>
[@b.foot/]