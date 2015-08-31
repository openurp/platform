[@b.head/]
[#include "/org/openurp/platform/portal/nav/local.ftl"/]
<style>
.nav > li > select {
    padding: 5px 15px;
    position: relative;
    vertical-align:middle;
    margin-top:7px
}
</style>
<script>
  function changeDefaultApp(appId){
     this.location="${b.url('!index')}"+"?app.id="+appId
  }
</script>
[@displayFrame brand="权限系统" tops=menus]
<ul class="nav navbar-nav" style="float:right">
      <li>默认应用:[@b.select items=apps name="app.id" value=appId option=r"${item.name} ${item.title}" onchange="changeDefaultApp(this.value)"/]</li>
      <li>[@b.a href="login!logout"]退出[/@]</li>
</ul>
[/@]
