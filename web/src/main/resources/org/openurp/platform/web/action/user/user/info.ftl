[#ftl]
[@b.head/]
[@b.toolbar title='${b.text("entity.user")}${b.text("common.detail")}']bar.addBack("${b.text("action.back")}");[/@]
[#macro info(name,title='')]
  [#if title=='']
   <td class="title">${b.text('user.'+name)}:</td>
  [#else]
  <td class="title">${b.text(title)}:</td>
  [/#if]
   <td class="content"> ${(user[name]?string)!}</td>
[/#macro]

<table class="infoTable">
  <tr>
   [@info 'code' /]
   [@info 'name' /]
  </tr>
  <tr>
    <td class="title">状态:</td>
    <td class="content">[#if user.enabled] ${b.text("action.activate")}[#else]${b.text("action.freeze")}[/#if]</td>
    <td class="title">是否锁定:</td>
    <td class="content">[#if user.locked]锁定[#else]正常[/#if]</td>
  </tr>
  <tr>
    <td class="title" >${b.text("user.members")}:</td>
    <td  class="content" colspan="3">[#list user.roles?sort_by(['role','indexno']) as m]${m.role.indexno} ${m.role.name}([#if m.member]${b.text('member.member')}[/#if][#if m.manager] ${b.text('member.manager')}[/#if][#if m.granter] ${b.text('member.granter')}[/#if])<br>[/#list]</td>
  </tr>
  <tr>
    <td class="title">身份:</td>
    <td class="content">${user.category.name}</td>
    <td class="title">有效期:</td>
    <td class="content">${user.beginOn!}~${user.endOn!}</td>
  </tr>
  <tr>
    <td class="title" >密码过期时间:</td>
    <td class="content">${(user.passwordExpiredOn)!}  </td>
    [@info 'updatedAt','common.updatedAt' /]
  </tr>
  <tr>
  <td class="title" >${b.text("common.remark")}:</td>
  <td class="content">${user.remark!}</td>
  </tr>
</table>
[@b.foot/]
