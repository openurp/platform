[#ftl]
[@b.head/]
[@b.toolbar title="应用信息"]
  bar.addBack("${b.text("action.back")}");
[/@]
<table class="infoTable">
  <tr>
    <td class="title" width="20%">名称</td>
    <td class="content">${app.name}</td>
  </tr>
  <tr>
    <td class="title" width="20%">引用资源</td>
    <td class="content">
    <style>.itable th, .itable td{padding:3px 5px;}</style>
    <table border="1" class="itable dstable">
      <thead>
        <th>数据源</th>
        <th>名称</th>
        <th>用户名</th>
        <th>密钥</th>
        <th>最大连接数</th>
      </thead>
      <tbody>
        [#list app.datasources as v]
          <tr>
            <td>${v.db.name}</td>
            <td align="center">${v.name}</td>
            <td align="center">${v.username}</td>
            <td align="center">${v.password}</td>
            <td align="center">${v.maxActive}</td>
          </tr>
        [/#list]
      </tbody>
    </table>
    </td>
  </tr>
</table>
[@b.foot/]