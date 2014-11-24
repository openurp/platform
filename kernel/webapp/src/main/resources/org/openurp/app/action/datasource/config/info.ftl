[#ftl]
[@b.head/]
[@b.toolbar title="性别信息"]
  bar.addBack("${b.text("action.back")}");
[/@]
<table class="infoTable">
  <tr>
    <td class="title" width="20%">名称</td>
    <td class="content">${dataSourceCfg.name}</td>
  </tr>
  <tr>
    <td class="title" width="20%">URL</td>
    <td class="content">${dataSourceCfg.url}</td>
  </tr>
  <tr>
    <td class="title" width="20%">DriverClassName</td>
    <td class="content" >${dataSourceCfg.driverClassName!}</td>
  </tr>
  <tr>
    <td class="title" width="20%">用户名</td>
    <td class="content" >${dataSourceCfg.username!}</td>
  </tr>
  <tr>
    <td class="title" width="20%">备注</td>
    <td class="content" >${dataSourceCfg.remark!}</td>
  </tr>
</table>

[@b.foot/]