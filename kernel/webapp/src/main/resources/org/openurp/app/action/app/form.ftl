[#ftl]
[@b.head/]
[@b.toolbar title="修改应用"]bar.addBack();[/@]
<style>form.listform label.title{width:120px;}</style>
[@b.tabs]
  [@b.form action="!update?id=${app.id}" theme="list"]
    [#include "fields.ftl"/]
  [/@]
[/@]
[@b.foot/]