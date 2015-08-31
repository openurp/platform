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

[#macro displayFrame appName apps topMenus mainHref="" fixed=true]
<style>
[#if fixed]body { padding-top: 50px; }[/#if]
.container{
  width:100%;
}
#menu .nav > li > a {
   padding: 10px 10px;
}
#urp_app_nav{
    display: block;
    float: left;
    height:50px;
    width:50px;
    border-right: 1px solid gray;
}
.nav_logo {
    position: absolute;
    top: 18px;
    left: 18px;
    vertical-align: top;
}
.app_logo{
    height:30px;
}
</style>
<script>
  $(document).ready(function(){
    $('[data-toggle="popover"]').popover({content:moreApp,html:true});   
  });
  function moreApp(){
    return "<ul>[#list apps as app][#if app.appType=='web-app' && app.url?? & app.name != appName]<li><a href='${app.url!}' target='_new'>[#if app.logoUrl??]<img class='app_logo' src='${app.logoUrl}'/>[/#if]${app.title}</a></li>[/#if][/#list]</ul>"
  }
</script>
[#if fixed][#assign navbarclass="navbar-fixed-top"][#else][#assign navbarclass=""][/#if]
[#list apps as app][#if app.name==appName][#assign appTitle = app.title/][#break/][/#if][/#list]
[#assign brand]
 <a id="urp_app_nav" href="#" title="更多应用..." data-toggle="popover" data-placement="bottom"> <i class="nav_logo glyphicon glyphicon-th"></i></a>
 [@b.a href="!index" class="navbar-brand"]${appTitle}[/@]
[/#assign]

[@b.navbar class=navbarclass brand=brand style="margin-bottom: 0px;"]
  [@b.navlist]
    [#list topMenus as m]
      [#if m_index==0][@b.navitem  href="!menus?indexno=${m.indexno}" target="menu" active=true ]${m.title}[/@]
      [#else][@b.navitem  href="!menus?indexno=${m.indexno}" target="menu" ]${m.title}[/@][/#if]
    [/#list]
  [/@]
  [#nested/]
[/@]

<div class="container">
  <div class="row">
  [@b.div id="menu" class="col-md-1" style="padding-left: 0px; padding-right: 0px;"][#if topMenus?size>0][@displayMenu topMenus?first/][/#if][/@]
  [#if mainHref?length>0]
  [@b.div id="main" class="col-md-11" style="padding-left: 0px; padding-right: 0px;" href=mainHref]...[/@]
  [#else]
  [@b.div id="main" class="col-md-11" style="padding-left: 0px; padding-right: 0px;" ]选择一个菜单[/@]
  [/#if]
  </div>
</div>
[/#macro]