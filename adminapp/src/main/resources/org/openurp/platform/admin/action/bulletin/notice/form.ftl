[#ftl]
[@b.head/]
${b.css("kindeditor","themes/default/default.css")}
${b.script("kindeditor","kindeditor-all-min.js")}
${b.script("kindeditor","lang/zh-CN.js")}
[@b.toolbar title="新建/修改通知公告"]bar.addBack();[/@]
[@b.form action=b.rest.save(notice) theme="list" onsubmit="syncEditor" enctype="multipart/form-data"]
  [@b.textfield name="notice.title" label="标题" value="${notice.title!}" required="true" maxlength="100"/]
  [@b.select name="notice.app.id" label="应用" value=notice.app option="id,title" required="true" items=apps?sort_by('title')/]
  [@b.select name="notice.userCategory.id" label="面向用户" value="${(notice.userCategory.id)!}" required="true" items=userCategories/]
  [@b.radios name="notice.sticky" label="是否置顶" value=notice.sticky required="true" /]
  [@b.radios name="notice.archived" label="是否归档" value=notice.archived required="true" /]
  [@b.radios name="notice.popup" label="是否弹窗" value=notice.popup required="true" /]
  [@b.startend label="有效期限" name="notice.beginOn,notice.endOn" required="true,true" start=notice.beginOn end=notice.endOn format="date"/]
  [@b.textarea name="notice.content" id="notice_content" label="内容" rows="20" cols="90" value=notice.content maxlength="10000"/]

  [#list 1..(3-notice.docs?size) as i]
  [@b.field label="附件"+i]
  <input type="file" name="notice_doc"/>
  [/@]
  [/#list]
  [@b.formfoot]
   [@b.reset/]&nbsp;&nbsp;[@b.submit value="action.submit"/]
  [/@]
[/@]
<script>
      var editor;
      jQuery(document).ready(function (){
          editor = KindEditor.create('textarea[name="notice\.content"]', {
          resizeType : 1,
          allowPreviewEmoticons : false,
          allowImageUpload : false,
          allowFileManager:false
        });
      });

      function syncEditor(){
         $('#notice_content').val(editor.html());
         return true;
      }
    </script>
[@b.foot/]
