[#ftl]
[@b.head/]
[@b.toolbar title="应用功能点管理"/]
[#include "scope.ftl"/]
<table class="indexpanel">
  <tr>
    <td class="index_view">
    [@b.form name="funcForm" action="!search" target="func_resource_list" title="ui.searchForm" theme="search"]
      [@b.textfields names="resource.name;名称"/]
      [@b.field label="可见范围"]
      <select name="resource.scope" style="width:100px">
      <option value="">...</option>
        [#list scopes?keys as i]
        <option value="${i}">${scopes[i?string]}</option>
        [/#list]
      </select>
      [/@]
      [@b.select name="resource.app.id" label="应用" items=apps option="id,title" empty="..." style="width:100px"/]
      <input type="hidden" name="orderBy" value="resource.name"/>
    [/@]
    </td>
    <td class="index_content">[@b.div id="func_resource_list" href="!search?orderBy=name"/]
    </td>
  </tr>
</table>
[@b.foot/]