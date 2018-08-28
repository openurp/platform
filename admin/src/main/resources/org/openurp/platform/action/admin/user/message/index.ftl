[#ftl]
[@b.head/]
<link rel="stylesheet" href="http://localhost/static/font-awesome/4.7.0/css/font-awesome.css"/>
    <section class="content-header">
      <h1>
        我的消息
        <small>${stats['1']!0} 新消息</small>
      </h1>
      <ol class="breadcrumb">
        <li><a href="#"><i class="fa fa-dashboard"></i> 首頁</a></li>
        <li class="active">消息</li>
      </ol>
    </section>

    <section class="content">
      <div class="row">
        <div class="col-md-3">
          [@b.a href="!editNew" target="messageList" class="btn btn-primary btn-block margin-bottom"]发送新消息[/@]

          <div class="box box-solid">
            <div class="box-body no-padding">
              <ul class="nav nav-pills nav-stacked" id="msgboxList">
                <li class="active">
                [@b.a href="!search?message.status=1" target="messageList"]
                  <i class="fa fa-envelope-o" aria-hidden="true"></i>新消息
                  [#if (stats['1']!0)>0]
                  <span class="label label-primary pull-right">${stats['1']!0}</span>
                  [/#if]
                [/@]
                </li>
                <li>
                   [@b.a href="!search?message.status=2" target="messageList"]
                    <i class="fa fa-envelope-open" aria-hidden="true"></i>已读
                    [#if (stats['2']!0)>0]
                    <span class="label label-primary pull-right">${stats['2']!0}</span>
                    [/#if]
                   [/@]
                </li>
                <li>[@b.a href="!sentList" target="messageList"]
                  <i class="fa fa-file-text-o"></i> 已发送
                   [/@]
                </li>
                <li>
                    [@b.a href="!search?message.status=3" target="messageList"]
                    <i class="fa fa-trash" aria-hidden="true"></i>垃圾箱
                    [#if (stats['3']!0)>0]
                    <span class="label label-primary pull-right">${stats['3']!0}</span>
                    [/#if]
                    [/@]
                </li>
              </ul>
            </div>
          </div>

        </div>
      [@b.div id="messageList" href="!search?message.status=1"  class="col-md-9"/]
      </div>
    </section>
    <script>
      jQuery("#msgboxList > li > a").click(function(){
         jQuery(this).parent().siblings().removeClass("active")
         jQuery(this).parent().addClass("active")
      });
    </script>
[@b.foot/]
