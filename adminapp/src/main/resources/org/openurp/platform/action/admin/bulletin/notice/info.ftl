[#ftl]
[@b.head/]
[@b.toolbar title="公告信息"]
  bar.addBack("${b.text("action.back")}");
[/@]
  <div class="box-body no-padding">
    <div class="mailbox-read-info">
      <h3>${notice.title}</h3>
      <h5>发自: ${notice.operator.name}(${notice.operator.code})
        <span class="mailbox-read-time pull-right">${notice.publishedOn?string('yyyy-MM-dd')}</span></h5>
    </div>
    <div class="mailbox-read-message">
      ${notice.content}
    </div>
  </div>
[@b.foot/]
