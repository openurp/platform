[#ftl]
[@b.head/]
[#include "scope.ftl"/]
[#include "../status.ftl"/]
[@b.grid items=resources var="resource" sortable="true"]
  [@b.gridbar title='数据资源']
  bar.addItem("${b.text("action.new")}",action.add());
  bar.addItem("${b.text("action.edit")}",action.edit());
  bar.addItem("${b.text("action.delete")}",action.remove());
  bar.addItem("${b.text("action.export")}",action.exportData("title:common.title,name:common.name,scope:可见范围,enabled:common.status,remark:common.remark",null,"fileName=资源信息"));
  [/@]
  [@b.row]
    [@b.boxcol /]
    [@b.col  width="34%" property="name" align="left" style="padding-left:10px"title="common.name"/]
    [@b.col  width="17%" property="title" title="common.title"]${(resource.title)!}[/@]
    [@b.col  width="34%" property="typeName" align="left" title="类型"/]
    [@b.col  width="10%" property="scope" title="可见范围"][@resourceScope resource.scope/][/@]
  [/@]
[/@]
[@b.foot/]