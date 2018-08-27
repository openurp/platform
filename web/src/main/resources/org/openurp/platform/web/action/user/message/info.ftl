[@b.head/]
<div class="box box-primary">
            <div class="box-header with-border">
              <h3 class="box-title">查看消息</h3>

              <div class="box-tools pull-right">
                <a href="#" class="btn btn-box-tool" data-toggle="tooltip" title="" data-original-title="Previous"><i class="fa fa-chevron-left"></i></a>
                <a href="#" class="btn btn-box-tool" data-toggle="tooltip" title="" data-original-title="Next"><i class="fa fa-chevron-right"></i></a>
              </div>
            </div>
            <!-- /.box-header -->
            <div class="box-body no-padding">
              <div class="mailbox-read-info">
                <h3>${message.title}</h3>
                <h5>发自: ${message.sender.name}(${message.sender.code})
                  <span class="mailbox-read-time pull-right">${message.sentAt?string('yyyy-MM-dd HH:mm')}</span></h5>
              </div>
              <div class="mailbox-read-message">
                ${message.content}
              </div>
            </div>

            <div class="box-footer">
              <div class="pull-right">
                <button type="button" class="btn btn-default" onclick="bg.Go('${b.url("!editNew?recipient.code="+message.sender.code)}','messageList')"><i class="fa fa-reply"></i> Reply</button>
              </div>
            </div>
          </div>
[@b.foot/]