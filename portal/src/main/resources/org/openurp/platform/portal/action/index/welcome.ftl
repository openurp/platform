[@b.head/]

<style>
.thin-table.table > tbody > tr > td {
  padding: 0.5rem;
  height:35px;
  font-size:13px;
  line-height:1.42857;
  vertical-align: top;
  border-top: 1px solid #dee2e6;
}
</style>
<div class="content-header">
  <div class="container-fluid">
    <div class="row mb-2">
      <div class="col-sm-6">
        <h1 class="m-0 text-dark">欢迎 <small>${user.name}</small></h1>
      </div><!-- /.col -->
      <div class="col-sm-6">
        <ol class="breadcrumb  float-sm-right">
          <li class="breadcrumb-item"><i class="fas fa-tachometer-alt" style="margin-top: 6px;"></i> 首页</li>
          <li class="breadcrumb-item active">欢迎</li>
        </ol>
      </div><!-- /.col -->
    </div><!-- /.row -->
  </div>
</div>

<section class="content">
  <div class="row">
    <section class="col-lg-6">
      [@b.card class="card-info card-primary card-outline"]
        [#assign title]<i class="far fa-bell"></i> 通知公告[/#assign]
        [@b.card_header class="border-transparent" title=title  minimal="true" closeable="true" style="cursor: move;"/]
        [@b.card_body class="p-0"]
          <div class="table-responsive">
            <table id="notice_table" class="table no-margin m-0 thin-table">
              <tbody>
              [#list notices as notice]
              <tr>
                <td><a href="${webappBase}/platform/user/notice/${notice.id}" target="_blank">${notice.title}</a></td>
                <td>${notice.createdAt?string('yyyy-MM-dd')}</td>
              </tr>
              [/#list]
              </tbody>
            </table>
          </div>
        [/@]
        [@b.card_footer]
          <a href="${webappBase}/platform/user/notice" target="_blank" class="btn btn-sm btn-default btn-flat float-right">更多...</a>
        [/@]
      [/@]
    </section>
    <section class="col-lg-6">
      [@b.card class="card-info card-primary card-outline"]
        [#assign title]<i class="fas fa-file-pdf"></i> 通知公告[/#assign]
        [@b.card_header class="border-transparent" title=title  minimal="true" closeable="true" style="cursor: move;"/]
        [@b.card_body class="p-0"]
          <div class="table-responsive">
            <table class="table no-margin m-0  thin-table">
              <tbody>
              [#assign extMap={"xls":'xls.gif',"xlsx":'xls.gif',"docx":"doc.gif","doc":"doc.gif","pdf":"pdf.gif","zip":"zip.gif","":"generic.gif"}]
              [#list docs as doc]
              <tr>
                <td>
                  <image src="${b.static_url("openurp-default","images/file/"+extMap[doc.name?keep_after_last(".")]?default("generic.gif"))}">&nbsp;
                  <a href="${webappBase}/platform/user/doc/${doc.id}" target="_blank">${doc.name}</a>
                </td>
                <td>${doc.updatedAt?string('yyyy-MM-dd')}</td>
              </tr>
              [/#list]
              </tbody>
            </table>
          </div>
        [/@]
        [@b.card_footer]
          <a href="${webappBase}/platform/user/doc" target="_blank" class="btn btn-sm btn-default btn-flat float-right">更多...</a>
        [/@]
      [/@]
    </section>
  </div>
</section>
[@b.foot/]
