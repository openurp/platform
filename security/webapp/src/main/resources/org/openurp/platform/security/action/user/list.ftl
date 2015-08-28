[#ftl]
[@b.head/]
[#include "../status.ftl"/]
[@b.grid items=users var="user"]
  [@b.gridbar]
  function activateUser(isActivate){return action.multi("activate","确定提交?","isActivate="+isActivate);}
  bar.addItem("${b.text("action.modify")}",action.edit());
  bar.addItem("${b.text("action.export")}",action.exportData("name,fullname,mail,creator.fullname,effectiveAt,invalidAt:common.invalidAt,passwordExpiredAt,createdAt:common.createdAt,updatedAt:common.updatedAt,enabled","Csv","&fileName=用户信息"));
  [/@]
  [@b.row]
    [@b.boxcol/]
    [@b.col property="code" width="15%"]&nbsp;[@b.a href="!info?id=${user.id}" target="_blank"]${user.code}[/@][/@]
    [@b.col property="name" width="35%"/]
    [@b.col property="category.name" width="10%" /]
    [@b.col property="beginOn" title="user.beginOn-endOn" width="27%"]${user.beginOn?string("yyyy-MM")}~${(user.endOn?string("yyyy-MM"))!}[/@]
    [@b.col property="enabled" width="8%"][@enableInfo user.enabled/][/@]
  [/@]
[/@]
[@b.foot/]