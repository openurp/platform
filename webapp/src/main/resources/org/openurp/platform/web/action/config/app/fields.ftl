[@b.textfield name="app.indexno" label="分类号" value="${app.indexno!}" required="true" maxlength="50"/]
[@b.textfield name="app.name" label="名称" value="${app.name!}" required="true" maxlength="200"/]
[@b.textfield name="app.title" label="标题" value="${app.title!}" required="true" maxlength="200"/]
[@b.radios name="app.appType" label="类型" items={'web-service':'web服务 ','web-app':'web应用'} value="${app.appType!}" required="true" /]
[@b.select name="app.domain.id" label="领域" value="${app.domain.id!}" option="id,title" required="true" items=domains/]
[@b.textfield name="app.url" label="URL" value="${app.url!}" required="true" maxlength="200" style="width:300px"/]
[@b.field label="引用资源"]
  <div style="margin-left:120px;">
    <style>.itable th, .itable td{padding:3px 5px;}</style>
    <table border="1" class="formTable itable dstable">
      <thead>
        <th>数据源</th>
        <th style="width:60px">名称</th>
        <th style="width:60px">用户名</th>
        <th>密码</th>
        <th>最大连接数</th>
        <th>备注</th>
        <th>操作</th>
      </thead>
      <tbody>
        [#list app.datasources as v]
          <tr>
            <td>${v.db.name}</td>
            <td><input name="ds${v.db.id}.name" value="${v.name!}" style="width:40px" maxlength="40"/></td>
            <td><input name="ds${v.db.id}.username" value="${v.username!}" style="width:80px"/></td>
            <td><input type="password" name="ds${v.db.id}.password" value="" style="width:80px"/></td>
            <td>
               <input name="ds" type="hidden" value="${v.db.id}"/>
               <input name="ds${v.db.id}.db.id" type="hidden" value="${v.db.id}"/>
               <input class="maxActive" name="ds${v.db.id}.maxActive" value="${v.maxActive}" style="width:60px"/>
            </td>
            <td><input name="ds${v.db.id}.remark" value="${v.remark!}" style="width:100px"/></td>
            <td><button class="delDataSourceBtn">删除</button></td>
           </tr>
        [/#list]
      </tbody>
    </table>
    <p><button class="addBtn">添加</button><p>
  </div>
[/@]
[@b.radios name="app.enabled" label="是否可用"  value=app.enabled required="true" /]
[@b.textfield name="app.secret" label="密钥" value="${app.secret!}" maxlength="200" required="true"/]
[@b.textarea name="app.remark" label="备注" value="${app.remark!}" maxlength="200"/]
[@b.formfoot]
  [@b.reset/]&nbsp;&nbsp;[@b.submit value="action.submit" onsubmit="beforeSubmit"/]
[/@]
<script>
  $(".addBtn").click(function (){
    var w = 500;   //宽度
    var h = 600;   //高度
    var t = (screen.height-h)/2; //离顶部距离
    var l = (screen.width-w)/2; //离左边距离
    window.open("${b.url('!datasource')}","","top="+t+",left="+l+",height="+h+", width="+w+", modal=yes, titlebar=no, toolbar=no, menubar=no, scrollbars=yes, resizable=no,fullscreen=1, location=no,status=no");  
    return false;
  });
  function addDataSource(datas){
    $.each(datas, function (index, value){
      var id = value[0], name = value[1];
      if($("input[value='" + id + "']").length == 0){
        var tr = $('<tr><td>'+name+'</td>'+
            '<td><input name="ds" type="hidden" value="'+id+'"/><input name="ds'+id+'.db.id" type="hidden" value="' + id + '"/><input name="ds'+id+'.name" style="width:60px" maxlength="40"/></td>'+
            '<td><input name="ds'+id+'.username" style="width:60px"/></td>'+
            '<td><input type="password" name="ds'+id+'.password" style="width:100px"/></td>'+
            '<td><input class="maxActive" name="ds'+id+'.maxActive" style="width:60px"/></td>'+
            '<td><input name="ds'+id+'.remark" style="width:100px"/></td>'+
            '<td><button class="delDataSourceBtn">删除</button></td></tr>');
        $(".dstable").append(tr);
        tr.hide().fadeIn();
      }
    });
  }
  $(".dstable").on("click", ".delDataSourceBtn", function (){
    $(this).parent().parent().fadeOut(function (){$(this).remove()});
    return false;
  });
  
  function beforeSubmit(){
    var allNumber = true;
    $(".maxActive").each(function (){
      allNumber = allNumber && /^\d+$/.test(this.value);
    });
    if(!allNumber){
      alert("请在最大连接数内输入一个整数");
      return false;
    }
    return true;
  }
</script>