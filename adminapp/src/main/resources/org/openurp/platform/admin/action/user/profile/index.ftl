[#ftl]
[@b.head/]
[@b.toolbar title="数据配置项"]
  bar.addItem("${b.text('action.new')}","add()");
[/@]
[#if (profiles?size==0)]没有设置[/#if]
 [#list profiles as profile]
  <ul align="center">
  [@b.a href="!edit?id=" + profile.id]修改[/@] [@b.a href="!remove?profile.id=" + profile.id]删除[/@]
  [#list profile.properties?keys as field]
  <li>${field.title}</li>

    [#if profile.properties.get(field)??]
    [#if field.multiple && field.properties?? && profile.properties.get(field)!='*']
    [#list fieldMaps[profile.id?string][field.name]! as value][#list field.properties?split(",") as pName]${value[pName]!} [/#list][#if value_has_next],[/#if][/#list]
    [#else]
    ${fieldMaps[profile.id?string][field.name]!}
    [/#if]
    [/#if]
  [/#list]
  </ul>
[/#list]
<br/>

[@b.form name="profileForm" action="!save"]
  <input type="hidden" name="_params" value="&profile.user.id=${Parameters['profile.user.id']}"/>
  <input type="hidden" name="profile.user.id" value="${Parameters['profile.user.id']}"/>
[/@]
<script type="text/javascript">
  function add(){
    var form = document.profileForm;
    form.action="${b.url('!editNew')}";
    bg.form.submit(form);
  }
  [#--function removeProfile(profileId){
    if(!confirm("确定删除?")) return;
    var form =document.profileForm;
    bg.form.addInput(form,"profile.id",profileId);
    form.action="${b.url('!remove')}";
    bg.form.submit(form);
  }--]
</script>
[@b.foot/]
