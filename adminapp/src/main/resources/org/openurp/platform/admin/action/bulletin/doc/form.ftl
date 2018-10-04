[#ftl]
[@b.head/]
[@b.toolbar title="新建/修改文档"]bar.addBack();[/@]
<style>form.listform label.title{width:120px;}</style>
[@b.form action=b.rest.save(doc) theme="list"   enctype="multipart/form-data"]
  [@b.textfield name="doc.name" label="标题" value="${doc.name!}" required="true" maxlength="100"/]
  [@b.select name="doc.domain.id" label="业务" value="${(doc.domain.id)!}" option="id,title" required="true" items=domains/]
  [@b.select name="doc.userCategory.id" label="面向用户" value="${(doc.userCategory.id)!}" required="true" items=userCategories/]
  [@b.radios name="doc.archived" label="是否归档" value=doc.archived required="true" /]
  [@b.field label="文件"]
    <input name="docfile" type="file"/>
  [/@]
  [@b.formfoot]
   [@b.reset/]&nbsp;&nbsp;[@b.submit value="action.submit"/]
  [/@]
[/@]
[@b.foot/]
