[#ftl]
[@b.head/]
[@b.toolbar title="新建应用"]bar.addBack();[/@]
<style>form.listform label.title{width:120px;}</style>
[@b.tabs]
  [@b.form action="!save" theme="list"]
    [#include "fields.ftl"/]
  [/@]
[/@]
[@b.foot/]