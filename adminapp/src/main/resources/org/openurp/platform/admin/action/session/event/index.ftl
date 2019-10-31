[#ftl]
[@b.head/]
[#assign eventTypes={'0':'登录','1':'退出'} /]
<table class="indexpanel">
  <tr>
    <td class="index_view">
    [@b.form name="configSearchForm" action="!search" target="configlist" title="ui.searchForm" theme="search"]
      [@b.textfields names="sessionEvent.principal;用户,sessionEvent.username;姓名,sessionEvent.name;事件"/]
      [@b.select name="sessionEvent.eventType" items=eventTypes label="类型" empty="..."/]
      <input type="hidden" name="orderBy" value="sessionEvent.updatedAt desc"/>
    [/@]
    </td>
    <td class="index_content">[@b.div id="configlist" href="!search?orderBy=sessionEvent.updatedAt desc"/]
    </td>
  </tr>
</table>
[@b.foot/]
