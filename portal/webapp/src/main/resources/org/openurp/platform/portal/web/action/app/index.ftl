[@b.head/]

<div class="module">
[#list apps as app]
  <li><a href="${app.url}" target="_blank">${app.title}</a></li>
[/#list]
</div>
[@b.foot/]