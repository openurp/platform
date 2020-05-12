[#ftl]
[@b.head/]
[#include "../nav.ftl"/]
<table class="indexpanel">
  <tr>
    <td class="index_view">
    [@b.form name="funcForm" action="!search" target="blobMeta_list" title="ui.searchForm" theme="search"]
      [@b.select name="blobMeta.profile.id" label="业务" items=profiles empty="..." option="id,name" style="width:100px"/]
      [@b.textfields names="blobMeta.name;文件名,blobMeta.owner;所有者,blobMeta.path;路径"/]
    [/@]
    </td>
    <td class="index_content">[@b.div id="blobMeta_list" href="!search?orderBy=blobMeta.updatedAt desc"/]
    </td>
  </tr>
</table>
[@b.foot/]
