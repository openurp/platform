[#ftl/]
[#macro displayMenuAnchor menu]
[#if Parameters['name']??]
[@b.a href="!index?menu.id=${menu.id}&name=${(Parameters['name']!)?url('utf-8')}" title=menu.remark!""][#nested/][/@]
[#else]
[@b.a href="!index?menu.id=${menu.id}" title=menu.remark!""][#nested/][/@]
[/#if]
[/#macro]

[#macro displayCommandAnchor menu]
[@b.a href="!access?menu.id=${menu.id}" title=menu.remark!""][#nested/][/@]
[/#macro]

[#assign maxItem=5]
[#macro displayMenu(mymenu)]
  [#local i=0]
  [#local hasFolder=false/]
  [#list mymenu.children as sub]
  [#local moreitem=false/]
  [#if menus?contains(sub)]
    [#if i=maxItem]
    <div style="text-align:right">[#local moreitem=true/][@displayMenuAnchor mymenu]更多项(${mymenu.children?size-maxItem})...[/@]</div>
    [#local i=i+1][#break/]
    [#else]
    <li [#if sub.children?size>0]class="menu"[#local hasFolder=true/][#else]class="resource"[/#if]>[#if sub.children?size>0][@displayMenuAnchor sub]${sub.title}[/@][#else][@displayCommandAnchor sub]${sub.title}[/@][/#if]</li>
    [#local i=i+1]
    [/#if]
  [/#if]
  [/#list]
  [#if !moreitem][#local i=i+1][/#if]
  [#if i<=maxItem]
    [#list i..maxItem as b]<br>[/#list]
  [/#if]

  [#if !moreitem]
  <div style="text-align:right">[#if hasFolder][#if mymenu.children?size>0][@displayMenuAnchor mymenu]打开[/@][#else][@displayCommandAnchor mymenu]打开[/@][/#if][#else]<br/>[/#if]</div>
  [/#if]
[/#macro]

[#macro displayMenus(menus)]
  [#list menus as sub]
    <li class="resource">[@displayCommandAnchor sub]${sub.title}[/@]</li>
  [/#list]
[/#macro]

[#macro displayResource(mymenu)]
  [#local i=0]
  [#local moreitem=false/]
  [#list mymenu.resources as resource]
  [@ems.guard res=resource.name]
    [#if i=maxItem]
    [#local moreitem=true/]
    [#local i=i+1][#break/]
    [#else]
    <li class="resource">[#if !resource.entry]${resource.title}[#else][@b.a href=resource.name! title=resource.remark!""]${resource.title}[/@][/#if]</li>
    [#local i=i+1]
    [/#if]
  [/@]
  [/#list]
  [#local i=i+1]
  [#if i<=maxItem]
    [#list i..maxItem as b]<br>[/#list]
  [/#if]
  <div style="text-align:right">[@displayCommandAnchor mymenu]进入[#if moreitem](更多项(${mymenu.resources?size-maxItem})...)[/#if][/@]</div>
[/#macro]