[#ftl]
[@b.head/]

[@b.grid items=sessionEvents var="sessionEvent"]
  [@b.row]
    [@b.boxcol /]
    [@b.col width="16%" property="name" title="事件"/]
    [@b.col width="13%" property="principal" title="用户"]
    ${sessionEvent.principal} ${sessionEvent.username}
    [/@]
    [@b.col width="14%" property="ip" title="IP"/]
    [@b.col width="13%" property="updatedAt" title="时间"]
      ${sessionEvent.updatedAt?string("yy-MM-dd HH:mm")}
    [/@]
    [@b.col width="40%" property="detail" title="明细"]
       <span> ${sessionEvent.detail?html}</span>
    [/@]
  [/@]
[/@]
[@b.foot/]
