[#ftl]
[@b.head/]
<div id="restrict_metas">
[@b.grid items=dimensions var="dimension"]
  [@b.gridbar ]
    bar.addItem("${b.text("action.new")}",action.add());
    bar.addItem("${b.text("action.edit")}",action.edit());
    bar.addItem("${b.text("action.delete")}",action.remove());
  [/@]
  [@b.row]
    [@b.boxcol/]
    [@b.col property="name" title="名称" width="10%"/]
    [@b.col property="title" title="描述"  width="10%"/]
    [@b.col property="typeName" title="类型" width="10%"/]
    [@b.col property="multiple" title="多值" width="5%"]${dimension.multiple?string('是','否')}[/@]
    [@b.col property="properties" title="属性" width="10%"][#if dimension.keyName??]${dimension.keyName},[/#if]${dimension.properties!}[/@]
    [@b.col property="source" title="来源"  width="30%"/]
    [@b.col property="apps" title="应用系统" width="25%"] [#list dimension.apps as app]${app.title!}[#if app_has_next],[/#if][/#list][/@]
  [/@]
[/@]
</div>
[@b.foot/]