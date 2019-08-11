[#ftl]
[@b.head/]
[#include "../nav.ftl"/]
<table class="indexpanel">
  <tr>
    <td class="index_view">
    [@b.form name="credentialSearchForm" action="!search" target="credentiallist" title="ui.searchForm" theme="search"]
      [@b.textfields names="credential.name;名称"/]
      [@b.textfields names="credential.username;用户名"/]
      <input type="hidden" name="orderBy" value="name"/>
    [/@]
    </td>
    <td class="index_content">[@b.div id="credentiallist" href="!search?orderBy=name"/]
    </td>
  </tr>
</table>
[@b.foot/]
