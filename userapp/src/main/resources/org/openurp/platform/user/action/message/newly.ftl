  <li class="header">你有${messages.totalItems}新消息</li>
  <li>
    <ul class="menu">
    [#list messages as message]
      <li>
        <a href="${urp.webapp}/platform/user/message/${message.id}" target="_blank">
          <div class="pull-left">
            <img src="${avatarUrls[message.sender.code]}" class="img-circle" alt="${message.sender.name}">
          </div>
          <h4>
            ${message.title}
            <small><i class="fa fa-clock-o"></i> ${message.sentAt?string('yy-MM-dd')}</small>
          </h4>
          <p>${message.content}</p>
        </a>
      </li>
      [/#list]
    </ul>
  </li>
  <li class="footer"><a href="${urp.webapp}/platform/user/message" target="_blank">查看所有消息</a></li>
  <script>
     [#if  Parameters['callback']??]
        ${Parameters['callback']}(${messages.totalItems});
     [/#if]
  </script>
