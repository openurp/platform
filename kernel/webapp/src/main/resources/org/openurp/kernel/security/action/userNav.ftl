[#ftl]
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
</style>
<div>
[@b.navbar title="权限管理" class="WB_global_nav"]
  [@b.navlist]
    [@b.navitem title="entity.user" href="/security/user"]用户管理[/@]
    [@b.navitem title="entity.role" href="/security/role"]角色管理[/@]
  [/@]
[/@]
</div>

