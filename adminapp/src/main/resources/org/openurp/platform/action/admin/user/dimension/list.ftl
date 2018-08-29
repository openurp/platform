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
    [@b.col property="name" width="10%"/]
    [@b.col property="title" width="10%"/]
    [@b.col property="typeName" width="40%"/]
    [@b.col property="multiple" width="7%"]${dimension.multiple?string('是','否')}[/@]
    [@b.col property="properties" width="15%"][#if dimension.keyName??]${dimension.keyName},[/#if]${dimension.properties!}[/@]
    [@b.col property="domains" width="15%"] [#list dimension.domains as d]${d.title!}[#if d_has_next],[/#if][/#list][/@]
  [/@]
[/@]
</div>
[@b.foot/]
