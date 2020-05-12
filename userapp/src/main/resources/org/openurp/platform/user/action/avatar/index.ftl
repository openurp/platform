[@b.head "照片管理"/]
  <div class="container" style="width:600px">
    <div>
      <h1>照片管理 <small>照片上传</small></h1>
    </div>
[@b.form action="!upload"   enctype="multipart/form-data" class="form-inline" role="form"]
    <label for="zipfile" class="control-label">选择照片文件：</label>
    <div class="form-group">
      <input type="file" name="photo"  id="photo" class="form-control">
    </div>
    <div class="form-group">
        [@b.submit class="btn btn-primary" value="上传"/]
    </div>
[/@]
</div>
[@b.foot/]
