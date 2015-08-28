[@b.head/]
[#include "macros.ftl"/]
<style>
.WB_global_nav {
    background: none repeat scroll 0 0 #fff;
    border-top: 2px solid black;
    box-shadow: 0 0 1px 0 rgba(0, 0, 0, 0.15);
    height: 48px;
    left: 0;
    position: fixed;
    top: 0;
    width: 100%;
    z-index: 9999;
}
body { padding-top: 50px; }
.container{
    width:100%;
}
</style>

[@b.navbar  class="navbar-fixed-top" brand="基础服务管理"]
  [@b.navlist]
    [#list menus as m]
    [@b.navitem ][@b.a href="!menus?indexno=${m.indexno}" target="menu"]${m.title}[/@][/@]
    [/#list]
  [/@]
[/@]
<div class="container">
<div class="row">
[@b.div id="menu" class="col-md-1"]
[#if menus?size>0][@display menus?first/][/#if]
[/@]
[@b.div id="main" class="col-md-11"]
  选择一个菜单
[/@]
</div>
</div>
