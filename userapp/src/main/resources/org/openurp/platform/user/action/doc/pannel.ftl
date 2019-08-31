[#ftl/]
<table style="font-size:10pt" width="100%">
	   <tbody><tr>
	     <td width="75%">标题</td>
	     <td width="25%">发布时间</td>
	   </tr>
[#list docs as doc]
	   <tr>
	    <td>
	      <a style="color:blue" alt="查看详情" target="_blank" href="${b.url("!info?id="+doc.id)}">
	      ${doc.name}</a>
	    </td>
      <td>${(doc.publishedAt?string("yyyy-MM-dd"))!}</td>
	   </tr>
[/#list]
</tbody></table>
[@b.a href="!index"]&nbsp;更多...[/@]
