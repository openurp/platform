[#macro displayMenus  menus]
[#list menus as m][@displayMenu m/][/#list]
[/#macro]

[#macro displayMenu menu]
[#if menu.entry??]
  [@b.navlist  class="nav nav-pills"]
    [@b.navitem href=menu.entry.name target="main"]${menu.title}[/@]
  [/@]
[#else]
[@b.navlist class="nav nav-pills"]
[#list menu.children as m]
  [#if m.entry??]
    [@b.navitem href=m.entry.name target="main"]${m.title}[/@]
  [#else]
    [@display m/]
  [/#if]
[/#list]
[/@]
[/#if]
[/#macro]

[#macro displayFrame brand tops mainHref="" fixed=true]
<style>
[#if fixed]body { padding-top: 50px; }[/#if]
.container{
    width:100%;
}

#menu .nav > li > a {
    padding: 10px 10px;
}
</style>

[#if fixed]
[#assign navbarclass="navbar-fixed-top"]
[#else]
[#assign navbarclass=""]
[/#if]

[@b.navbar class=navbarclass brand=brand style="margin-bottom: 0px;"]
  [@b.navlist]
    [#list tops as m]
      [#if m_index==0][@b.navitem  href="!menus?indexno=${m.indexno}" target="menu" active=true ]${m.title}[/@]
      [#else][@b.navitem  href="!menus?indexno=${m.indexno}" target="menu" ]${m.title}[/@][/#if]
    [/#list]
  [/@]
[/@]

<div class="container">
  <div class="row">
  [@b.div id="menu" class="col-md-1" style="padding-left: 0px; padding-right: 0px;"][#if tops?size>0][@displayMenu tops?first/][/#if][/@]
  [#if mainHref?length>0]
  [@b.div id="main" class="col-md-11" style="padding-left: 0px; padding-right: 0px;" href=mainHref]...[/@]
  [#else]
  [@b.div id="main" class="col-md-11" style="padding-left: 0px; padding-right: 0px;" ]选择一个菜单[/@]
  [/#if]
  </div>
</div>
[/#macro]