[#ftl]
[@b.head/]
[@b.toolbar title="数据授权"]
  function save(){if(confirm("确定设置?")){bg.form.submit(document.profileForm);}}
  bar.addItem("${b.text("action.save")}",save);
  function changeDomain(){
    document.profileForm.action='${b.url("!editNew")}'
    bg.form.submit(document.profileForm)
  }
[/@]
[@b.form name="profileForm" action="!save"]
  [#if profile.persisted]
  <input type="hidden" name="profile.id" value="${profile.id!}"/>
  领域:${profile.domain.title}
  [#else]
  <input type="hidden" name="profile.user.id" value="${profile.user.id}"/>
  [@b.select items=domains label="领域" value=profile.domain.id option="id,title" name="profile.domain.id" theme="list" onchange="changeDomain()"/]
  [/#if]
  <input type="hidden" name="_params" value="&profile.user.id=${profile.user.id}"/>
  [#if fields?size==0]该领域没有增加涉及到的数据维度[/#if]
  [@b.tabs]
    [#list fields?sort_by("title") as field]
    [@b.tab label="${field.name}(${field.title})"]
    [#if ignoreDimensions?seq_contains(field)]
    <div>
      <input name="ignoreDimension${field.id}" type="radio" value="1" [#if userIgnoreDimensions?seq_contains(field)]checked="checked"[/#if] id="ignoreDimension${field.id}_1"><label for="ignoreDimension${field.id}_1">使用通配符*</label>
      <input name="ignoreDimension${field.id}" type="radio" value="0" [#if !userIgnoreDimensions?seq_contains(field)]checked="checked"[/#if] id="ignoreDimension${field.id}_2"><label for="ignoreDimension${field.id}_2">选择或填写具体值</label>
    </div>
    [/#if]
    [#if field.multiple && field.keyName?exists]
      [@b.grid items=mngDimensions[field.name] var="value"]
        [@b.row]
          [#assign checked=false/]
          [#list userDimensions[field.name]?if_exists as userValue]
            [#if (userValue[field.keyName]!"")?string == value[field.keyName]?string]
            [#assign checked=true/]
            [#break/]
           [/#if]
          [/#list]
          [@b.boxcol property=field.keyName boxname=field.name checked=checked /]
          [#if field.properties??]
          [#list field.properties?split(",") as pName][@b.col title=pName]${value[pName]!}[/@][/#list]
          [#else]
          [@b.col title="可选值"]${value}[/@]
          [/#if]
        [/@]
      [/@]
    [#else]
    <table class="grid" width="100%">
      <tr><td colspan="2"><input type="text" name="${field.name}" value="${userDimensions[field.name]!}"/>[#if field.multiple]多个值请用,格开[/#if]</td></tr>
    </table>
    [/#if]
    [/@]
    [/#list]
  [/@]
[/@]
[@b.foot/]
