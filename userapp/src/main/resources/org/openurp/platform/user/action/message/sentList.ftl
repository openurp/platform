[#ftl]
[@b.head/]
[#assign statusMap={'1':'未读','2':'已读','3':'删除'}/]
<div class="box box-primary">
   [@b.form  name="messageListForm" id="messageListForm" action="!sentList"]
   [#list Parameters as k,v]
   [#if k!="message.id" && k!="message.title" ]
   <input name="${k}" value="${v}" type="hidden"/>
   [/#if]
   [/#list]
    <div class="box-header with-border">
      <h3 class="box-title">已发送消息</h3>
      <div class="box-tools pull-right">
        <div class="has-feedback">
          <input type="text" id="messageSearchBox" name="message.title" value="${Parameters['message.title']!}" class="form-control input-sm" placeholder="查询消息" >
          <span class="glyphicon glyphicon-search form-control-feedback"></span>
        </div>
      </div>
    </div>
   [/@]

    <div class="box-body no-padding">
      <div class="mailbox-controls">
        <button type="button" class="btn btn-default btn-sm"  onclick="gotoPage(${messages.pageIndex})"><i class="fa fa-refresh"></i></button>
        <div class="pull-right">
          [#if messages.totalItems==0]
          0-0/0
          [#else]
          ${(messages.pageIndex-1)*messages.pageSize+1}-${(messages.pageIndex-1)*messages.pageSize+messages.items.size}/${messages.totalItems}
          [/#if]
          <div class="btn-group">
            <button type="button" class="btn btn-default btn-sm" [#if messages.hasPrevious] onclick="gotoPage(${messages.pageIndex-1})"[#else] disabled="disabled"[/#if]><i class="fa fa-chevron-left"></i></button>
            <button type="button" class="btn btn-default btn-sm" [#if messages.hasNext] onclick="gotoPage(${messages.pageIndex+1})"[#else] disabled="disabled"[/#if]><i class="fa fa-chevron-right"></i></button>
          </div>
        </div>
      </div>

      <div class="table-responsive mailbox-messages">
        <table class="table table-hover table-striped" id="messageGrid">
          <tbody>
          [#list messages as message]
          <tr>
            <td class="mailbox-name">[@b.a href="!info?id=${message.id}"]${message.recipient.name}[/@]</td>
            <td class="mailbox-subject">
               <b>${message.title}</b> - [#if message.content?length>30]${message.content?substring(0,30)?html}...[#else]${message.content?html}[/#if]
            </td>
            <td>${statusMap[message.status?string]}</td>
            <td class="mailbox-date">${message.sentAt?string('yy-MM-dd HH:mm')}</td>
          </tr>
          [/#list]
          </tbody>
        </table>
      </div>
    </div>
  </div>

  <script>
    function toggleAll(){
      jQuery("#messageGrid [type='checkbox']").each(function(){
          var ele=jQuery(this);
          if(!ele.is(":checked")){
            ele.prop("checked",true);
            jQuery(this).parent("tr").addClass("griddata-selected");
          }else{
              ele.prop("checked",false);
              jQuery(this).parent("tr").removeClass("griddata-selected");
          }
        });
      }
      function gotoPage(pageIndex){
        if(pageIndex){
          bg.form.addInput(document.messageListForm,'pageIndex',pageIndex);
        }
        bg.form.submit(document.messageListForm);
      }

      $("#messageSearchBox").keydown(function (e) {
       if (e.keyCode == 13) {
        bg.form.submit(document.messageListForm);
       }
      });
  </script>
[@b.foot/]
