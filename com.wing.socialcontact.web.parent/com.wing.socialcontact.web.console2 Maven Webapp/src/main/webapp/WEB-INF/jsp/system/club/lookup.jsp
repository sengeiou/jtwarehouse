<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/commons/include.inc.jsp" %>
<%--
	模块：系统管理--分类树
--%>

<div class="pageContent">
	<div class="pageFormContent" layoutH="58" style="padding-left: 20px;" >
		 <ul id="clubclass_lookup_tree" class="ztree"></ul>
	
	</div>
</div>

<script type="text/javascript">
	$(function(){
		$.ajax({
			url:"clubclass/lookUp.do",
			cache: false,
			dataType:"json",
			success:function(json){
				var setting = {
						view: {
							dblClickExpand: false,
							showIcon: false
						},
						data: {
							simpleData: {
								enable: true
							}
						}
				};
				var zNodes = new Array();
				
				$.each(json, function(i, d) {
					
					zNodes.push({id :  d.id,name : d.name,
						pId : d.parentId,open:true,click:"$.bringBack({id:'"+d.id+"',name:'"+d.name+"'})"});
					
				});
				
				$.fn.zTree.init($("#clubclass_lookup_tree"), setting, zNodes);
		
			}
		});
		
	});
</script>
