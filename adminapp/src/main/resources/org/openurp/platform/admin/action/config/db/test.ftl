[#ftl]
[@b.head/]
[@b.toolbar title="连接测试结果"]
  bar.addBack("${b.text("action.back")}");
[/@]

<div style="width:400px; margin:50px auto;">
<ol>
[#list result as v]
<li>[#if v._2]<span style="color:green;margin-right:10px;">√</span>[#else]<span style="color:red;margin-right:10px;">X</span>[/#if]${v._1.name}</li>
[/#list]
</ol>
</div>

[@b.foot/]
