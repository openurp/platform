[#ftl]
[@b.head/]
[#include "../func-nav.ftl"/]
[#include "scope.ftl"/]
<table class="indexpanel">
  <tr>
    <td class="index_view">
    [@b.form name="funcForm" action="!search" target="func_resource_list" title="ui.searchForm" theme="search"]
      [@b.select name="resource.app.id" label="应用" items=apps option="id,title" style="width:100px"/]
      [@b.textfields names="resource.name;名称"/]
      [@b.field label="可见范围"]
      <select name="resource.scope" style="width:100px">
      <option value="">...</option>
        [#list scopes?keys as i]
        <option value="${i}">${scopes[i?string]}</option>
        [/#list]
      </select>
      [/@]
      <input type="hidden" name="orderBy" value="resource.name"/>
    [/@]
    </td>
    <td class="index_content">[@b.div id="func_resource_list" href="!search?resource.app.id=${apps?first.id}&orderBy=name"/]
    </td>
  </tr>
</table>
[@b.foot/]