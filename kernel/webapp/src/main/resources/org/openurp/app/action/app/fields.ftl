[@b.textfield name="app.name" label="名称" value="${app.name!}" required="true" maxlength="200"/]
[@b.field label="引用资源"]
  <div style="margin-left:120px;">
    <style>.itable th, .itable td{padding:3px 5px;}</style>
    <table border="1" class="formTable itable dstable">
      <thead>
        <th>数据源</th>
        <th>名称</th>
        <th>用户名</th>
        <th>密码</th>
        <th>最大连接数</th>
        <th>备注</th>
        <th>操作</th>
      </thead>
      <tbody>
        [#list app.datasources as v]
          <tr>
            <td>${v.config.name}</td>
            <td><input name="ds${v.config.id}.name" value="${v.name!}" style="width:80px"/></td>
            <td><input name="ds${v.config.id}.username" value="${v.username!}" style="width:80px"/></td>
            <td><input type="password" name="ds${v.config.id}.password" value="" style="width:80px"/></td>
            <td>
               <input name="ds" type="hidden" value="${v.config.id}"/>
               <input name="ds${v.config.id}.config.id" type="hidden" value="${v.config.id}"/>
               <input class="maxActive" name="ds${v.config.id}.maxActive" value="${v.maxActive}" style="width:60px"/>
            </td>
            <td><input name="ds${v.config.id}.remark" value="${v.remark!}" style="width:100px"/></td>
            <td><button class="delDataSourceBtn">删除</button></td>
           </tr>
        [/#list]
      </tbody>
    </table>
    <p><button class="addBtn">添加</button><p>
  </div>
[/@]
[@b.textfield name="app.secret" label="密钥" value="${app.secret}" maxlength="200"/]
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
            '<td><input name="ds" type="hidden" value="'+id+'"/><input name="ds'+id+'.config.id" type="hidden" value="'+id+'"/><input name="ds'+id+'.username"/></td>'+
            '<td><input type="password" name="ds'+id+'.password"/></td>'+
            '<td><input class="maxActive" name="ds'+id+'.maxActive"/></td>'+
            '<td><input name="ds'+id+'.remark"/></td><td><button class="delDataSourceBtn">删除</button></td></tr>');
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