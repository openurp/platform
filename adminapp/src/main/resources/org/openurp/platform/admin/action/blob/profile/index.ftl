[#ftl]
[@b.head/]
[#include "../nav.ftl"/]
<table class="indexpanel">
  <tr>
    <td class="index_view">
    [@b.form name="funcForm" action="!search" target="profile_list" title="ui.searchForm" theme="search"]
      [@b.textfields names="profile.path;路径"/]
    [/@]
    </td>
    <td class="index_content">[@b.div id="profile_list" href="!search?orderBy=profile.path"/]
    </td>
  </tr>
</table>
[@b.foot/]
