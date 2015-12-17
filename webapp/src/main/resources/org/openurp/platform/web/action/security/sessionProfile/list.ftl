[#ftl]
[@b.head/]
[@b.messages slash="true"/]
[@b.form action="!save"]
<table width="70%" class="table">
  <thead>
  <tr>
    <th>角色</th>
    <th>上限</th>
    <th>过期时间(分)</th>
    <th>单用户最大会话数</th>
  </tr>
  </thead>
  [#list profiles as profile]
  <tr>
    <td>${profile.category.name}</td>
    <td><input name="capacity_${profile.category.id}" value="${profile.capacity}" style="width:50px" maxlength="5"/></td>
    <td><input name="timeout_${profile.category.id}" value="${profile.timeout}" style="width:50px" maxlength="5"/>分</td>
    <td><input name="maxSession_${profile.category.id}" value="${profile.maxSession}" style="width:35px" maxlength="2"/></td>
  </tr>
  [/#list]
  <tr style="[#if categories?size==0]display:none[/#if]" >
    <td>新增:[@b.select name="categoryId_new" items=categories empty="..."/]
    <input type="hidden" name="profile.app.id" value="${Parameters['profile.app.id']}"></td>
    <td><input name="capacity_new" value="" style="width:50px" maxlength="5"/></td>
    <td><input name="timeout_new" value="" style="width:50px" maxlength="5"/>分</td>
    <td><input name="maxSession_new" value="" style="width:35px" maxlength="2"/></td>
  </tr>
    <tr>
    <td  colspan="5">注:最大会话数指单个用户同时在线数量&nbsp;&nbsp;
    &nbsp;
    [@b.reset/]&nbsp;&nbsp;[@b.submit value="提交" onsubmit="validateProfile"/]
    </td>
  </tr>
</table>
[/@]
<script type="text/javascript">
  function validateProfile(form){
    [#if profiles??]
    [#list profiles as profile]
    if(!(/^\d+$/.test(form['capacity_${profile.category.id}'].value))){alert("${profile.category.name}最大用户数限制应为0或正整数");return false;}
    if(!(/^\d+$/.test(form['maxSession_${profile.category.id}'].value))){alert("${profile.category.name}最大会话数应为0或正整数");return false;}
    if(!(/^\d+$/.test(form['timeout_${profile.category.id}'].value))){alert("${profile.category.name}过期时间应为0或正整数");return false;}
    [/#list]
    [/#if]
    if(form['categoryId_new'].value!=""){
      if(!(/^\d+$/.test(form['capacity_new'].value))){alert("新增配置最大用户数限制应为0或正整数");return false;}
      if(!(/^\d+$/.test(form['maxSession_new'].value))){alert("新增配置最大会话数应为0或正整数");return false;}
      if(!(/^\d+$/.test(form['timeout_new'].value))){alert("新增配置过期时间应为0或正整数");return false;}
    }
    return true;
  }
</script>
[@b.foot/]