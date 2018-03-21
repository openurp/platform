[#ftl]
[@b.head/]
[#include "../status.ftl"/]
[@b.grid items=users var="user"]
  [@b.gridbar]
  function activateUser(isActivate){return action.multi("activate","确定提交?","isActivate="+isActivate);}
  bar.addItem("${b.text("action.new")}",action.add());
  bar.addItem("${b.text("action.modify")}",action.edit());
  bar.addItem("${b.text("action.freeze")}",activateUser('false'),'action-freeze');
  bar.addItem("${b.text("action.activate")}",activateUser('true'),'action-activate');
  bar.addItem("${b.text("action.delete")}",action.remove());
  bar.addItem("${b.text("action.export")}",action.exportData("name,fullname,mail,creator.fullname,effectiveAt,invalidAt:common.invalidAt,passwordExpiredAt,createdAt:common.createdAt,updatedAt:common.updatedAt,enabled","Csv","&fileName=用户信息"));
  [/@]
  [@b.row]
    [@b.boxcol width="5%"/]
    [@b.col property="code" width="15%"]${user.code}[/@]
    [@b.col property="name" width="15%"/]
    [@b.col property="category.name" title="身份" width="25%"/]
    [@b.col property="beginOn" title="user.beginOn-endOn" width="12%"]${user.beginOn}~${(user.endOn)!}[/@]
    [@b.col property="enabled" width="8%"][@enableInfo user.enabled/][/@]
  [/@]
[/@]
[@b.foot/]
