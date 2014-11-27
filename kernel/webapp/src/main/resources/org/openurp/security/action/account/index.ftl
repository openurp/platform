[#ftl]
[@b.head/]
<div>
<table class="indexpanel">
<tr>
  <td class="index_view">
  [@b.form name="userSearchForm" action="!search" title="ui.searchForm" target="userlist" theme="search"]
    [@b.textfields names="user.code,user.person.name"/]
    [@b.select name="user.enabled" label="common.status" value="1" empty="..." items={'1':'${b.text("action.activate")}','0':'${b.text("action.freeze")}'}/]
  [/@]
  </td>
  <td class="index_content">
  [@b.div id="userlist" href="!search?user.enabled=1" /]
  </td>
</tr>
</table>
</div>
<script>
$.get("http://www.duantihua.com:9090/security/showcase/status/json", function(data) {
  alert(data);
})
/*var jqxhr = $.get("http://www.duantihua.com:8080/code/kernel/code/nation", function(data) {
alert(data);
})
.success(function() { alert("second success"); })
.error(function(e) {
 alert(e);
 })
.complete(function(jxhr) {
 alert("complete"); 
 });*/
</script>
[@b.foot/]