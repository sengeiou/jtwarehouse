<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/commons/include.inc.jsp"%>
<% 
	String path = request.getContextPath(); 
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	request.getSession().setAttribute("path", path);
%>
<style>
* { margin:0; padding:0;}
.hyjb_box {}
.hyjb_dl {float:left; margin: 12px 12px; width:125px; list-style:none; }
.hyjb_dl dl { position:relative; width:125px; height:105px; }
.hyjb_dl dl dt { width:120px; height:100px; border:1px solid #dddddd;}
.hyjb_dl dl dd { position:absolute; right:-10px; top:-10px; }
.hyjb_dl .hyjb_shuru { width:125px; margin-top:-22px;}
.hyjb_dl .hyjb_shuru input { border:none; width:110px; height:26px; background:#f7f7f7; text-align:center;}
.hyjb_tj { float:left; margin:5px 0px 0 5px; }
#dataForm img{width:100%;height:100%;}
.Wdate{
	background-color:#fff !important;
	background-image:none !important;
}
</style>

<div style="width:100%;">						
<form method="post" action="" id="dataForm">
	<input type="hidden" name="id" id="id" value="${obj.id}">
	<input type="hidden" name="coverImg" id="coverImg" value="${obj.coverImg}">
	<input type="hidden" name="createTime" id="createTime" value="${fns:fmt(obj.createTime,'yyyy-MM-dd HH:mm:ss')}">
	<input type="hidden" name="createUserId" id="createUserId" value="${obj.createUserId}">
	<input type="hidden" name="createUserName" id="createUserName" value="${obj.createUserName}">
	<input type="hidden" name="deleted" id="deleted" value="${obj.deleted}">
	<input type="hidden" name="projIds" id="projIds">
	<input type="hidden" name="meetingIds" id="meetingIds">
	<input type="hidden" name=webinarIds id="webinarIds">
	<input type="hidden" name="guests" id="guests">
	<input type="hidden" name="contentVisibleRange" id="contentVisibleRange" >
	<input type="hidden" name="videoVisibleRange" id="videoVisibleRange" >
	<table class="table table-bordered" style="margin-top:0px;width:99%;">
		<tr>
			<th style="width: 15%;vertical-align:middle">????????????<font color="red">*</font>???</th>
			<td>
				<div class="hyjb_box" style="margin:0 10px;height:110px;">
					<ul class="hyjb_dl" style="margin:0px;">
				    	<li>
				    		<dl style="margin-bottom:0px;">
				            	<dt>
				            		<a href="javascript:void(0)">
		            			<c:choose>
			            			<c:when test="${not empty obj.coverImg and obj.coverImg!=''}">
				            			<img status="1" style="width:100%;height:100%;" 
				            				src="${obj.coverImg}" id="hb"/>
			            			</c:when>
			            			<c:otherwise>
				            			<img status="0" style="width:100%;height:100%;" 
				            				src="${path}/resource/images/tjy/djsc.png" id="hb" onclick="uploadImg('hb')" />
			            			</c:otherwise>
		            			</c:choose>
				            		</a>
				            	</dt>
				            	<dd>
				            		<a href="javascript:void(0)">
				            			<img id="deleteHb" <c:if test="${empty obj.coverImg or obj.coverImg==''}">style="display:none;"</c:if> onclick="removeHb(this)" src="${path}/resource/images/tjy/close_04.gif" width="25" height="25" />
				            		</a>
				            	</dd>
				   	    	</dl>
				   	    </li>
				    </ul>
				</div>
		   	    <div style="color:gray;"><i>???????????????2M???PNG???JPG???JPEG????????????????????????640*320</i></<div>
			</td>
			<th style="width: 15%;vertical-align:middle">???????????????</th>
			<td style="vertical-align:middle">
				<select class="span2" name="showEnable">
					<option value="0" <c:if test="${0==obj.showEnable}">selected</c:if>>???</option>
					<option value="1" <c:if test="${1==obj.showEnable}">selected</c:if>>???</option>
				</select>
			</td>
		</tr>
		<tr>
			<th>??????<font color="red">*</font>???</th>
			<td>
				<input type="text" name="sort"  onkeyup="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}" onafterpaste="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}" id="sort" class="easyui-numberbox"  value="${obj.sort}"   maxlength="3" />
			</td>
			<th style="width:15%;">????????????<font color="red">*</font>???</th>
			<td>
				<input type="text" maxlength="100" name="titles" onkeyup="this.value=this.value.replace(/(^\s*)|(\s*$)/g, '')" id="mau_titles" value="${obj.titles}" class="span2" style="width:95%"/>
			</td>
			
		</tr>
		<tr>
			<th>???<font color="red">*</font>???</th>
			<td>
				<select  name="province"  id="province"  onchange="querycitys(2)">
			    	<c:forEach items="${provinceList}" var="p">
			    	<c:choose>
			    		<c:when test="${obj.province eq p.id }">
			    		<option id="${p.id }" value="${p.id } " selected="selected">${p.disName } </option>
			    		</c:when>
			    		<c:otherwise>
			    		<option id="${p.id }" value="${p.id } ">${p.disName } </option>
			    		</c:otherwise>
			    	</c:choose>
			    	</c:forEach>
			    	</select>
			</td>
			<th>???<font color="red">*</font>???</th>
			<td>
				<select name="city" id="activitycitys" onchange="querycountys()" >
				</select>
			</td>
		</tr>
		<tr>
			<th>???<font color="red">*</font>???</th>
			<td>
				<select name="county" id="activitycountys">
				</select>
			</td>
			<th>????????????<font color="red">*</font>???</th>
			<td>
				<input type="text" maxlength="100" name="place" onkeyup="this.value=this.value.replace(/(^\s*)|(\s*$)/g, '')" id="mau_place"  style="width:95%" value="${obj.place}"/>
			</td>
		</tr>
		<tr>
			<th>????????????<font color="red">*</font>???</th>
			<td>
				<input id="mau_startTime" name="startTime"  style="width:120px;" class="Wdate time-field" 
					onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',readOnly:true,isShowClear:false,isShowToday:false})"
					onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',readOnly:true,isShowClear:false,isShowToday:false})" 
					value="${fns:fmt(obj.startTime,'yyyy-MM-dd HH:mm')}" readonly="readonly"/>~
				<input id="mau_endTime" name="endTime"  style="width:120px;" class="Wdate time-field" 
					onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',readOnly:true,isShowClear:false,isShowToday:false})"
					onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',readOnly:true,isShowClear:false,isShowToday:false})" 
					value="${fns:fmt(obj.endTime,'yyyy-MM-dd HH:mm')}" readonly="readonly"/>
			</td>
			<th>????????????<font color="red">*</font>???</th>
			<td>
				<input id="mau_query_startSignupTime" name="startSignupTime" style="width:120px;" size=15 class="Wdate time-field" 
					onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',readOnly:true,isShowClear:false,isShowToday:false})"
					onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',readOnly:true,isShowClear:false,isShowToday:false})" 
					value="${fns:fmt(obj.startSignupTime,'yyyy-MM-dd HH:mm')}"
					readonly="readonly"/>~
				<input id="mau_query_endSignupTime" name="endSignupTime" style="width:120px;" size=15 class="Wdate time-field" 
					onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',readOnly:true,isShowClear:false,isShowToday:false})"
					onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',readOnly:true,isShowClear:false,isShowToday:false})" 
					value="${fns:fmt(obj.endSignupTime,'yyyy-MM-dd HH:mm')}"
					readonly="readonly"/>
			</td>
		</tr>
		<tr>
			<th style="width:15%;">???????????????</th>
			<td style="width:35%;">
				<select class="span2" name="types" id="mau_types" onchange="typesChange(this)">
					<option value="1" <c:if test="${1==obj.types}">selected</c:if>>????????????</option>
					<option value="2" <c:if test="${2==obj.types}">selected</c:if>>????????????</option>
					<option value="3" <c:if test="${3==obj.types}">selected</c:if>>????????????+????????????</option>
				</select>
			</td>
			<th>?????????<font color="red">*</font>???</th>
			<td>
				<input type="text" name="sponsor" id="mau_sponsor" value="${obj.sponsor}"  maxlength="100" style="width:95%;"/>
			</td>
		</tr>
		<tr class="living">
			<th><p class="living">?????????????????????</p></th>
			<td>
				<input  readonly="readonly" type="text" id="mau_webinarSubject" name="webinarSubject" value="${obj.webinarSubject}" style="width:200px;"/>
				<input  type="hidden" id="mau_webinarId" name="webinarId" value="${obj.webinarId}"/>
				<button class="btn btn-primary btn-small" type="button" onclick="addVhallDialog()">????????????</button>&nbsp;
			</td> 
			<th>???????????????</th>
			<td style="padding:0;">
				<table style="width:100%;margin-top:0;" class="table table-bordered" >
					<thead>
						<tr>
							<td colspan=3>
								<button class="btn btn-primary btn-small living" type="button" onclick="addVhallOver()">????????????</button>&nbsp;
								<button class="btn btn-primary btn-small" type="button" onclick="delVhall('del')">????????????</button>&nbsp;
							</td>
						</tr>
						<tr>
							<th style="width:15px;text-align: center;"></th>
							<th style="text-align: center;">????????????</th>
						</tr>
					</thead>
					<tbody id="vhallTbody">
					  
					<c:if test="${not empty obj}">
   						<c:forEach items="${obj.meetingRelationList}" var="item">
						<tr data-id="${item.webinarId}">
							<td  class="webinarId" data-id="${item.webinarId}"><input type="checkbox" name="checkbox"></td>
							<td id="" name="webinarSubject">${item.webinarSubject}</td>
						
						</tr>
   						</c:forEach>
   					</c:if>		
					</tbody>
				</table>
			</td>
		</tr>
		<%--<tr>
			<th>????????????????????????</th>
			<td>
				<select class="span2" name="recommendEnable">
					<option value="0" <c:if test="${0==obj.recommendEnable}">selected</c:if>>???</option>
					<option value="1" <c:if test="${1==obj.recommendEnable}">selected</c:if>>???</option>
				</select>
			</td>
			<th>????????????????????????</th>
			<td>
				<select class="span2" name="investmentEnable">
					<option value="0" <c:if test="${0==obj.investmentEnable}">selected</c:if>>???</option>
					<option value="1" <c:if test="${1==obj.investmentEnable}">selected</c:if>>???</option>
				</select>
			</td>
		</tr>--%>
		<tr>
			<th>???????????????</th>
			<td>
				<input maxlength="8" type="text" name="upperlimit" id="mau_upperlimit" value="${obj.upperlimit}" style="width:60px;" 
						onkeyup="num2(this)" onafterpaste="return false" onpaste="return false"/>(0??????????????????)
			</td>
			<th>????????????<font color="red">*</font>???</th>
			<td>
				<c:choose>
				<c:when test="${empty obj or empty obj.ticketPrice or obj.ticketPrice ==0 }">
					<select id="ticketPriceType" onchange="ticketTypeChange(this)">
						<option value="1" selected="selected">??????</option>
						<option value="2">??????</option>
					</select>
					<input type="text" style="display:none;" name="ticketPrice" id="mau_ticketPrice" value="${obj.ticketPrice}"
						onkeyup="num(this)" onafterpaste="return false" onpaste="return false"/>???
				</c:when>
				<c:otherwise>
					<select id="ticketPriceType" onchange="ticketTypeChange(this)">
						<option value="1">??????</option>
						<option value="2" selected="selected">??????</option>
					</select>
					<input type="text" maxlength="10" name="ticketPrice" id="mau_ticketPrice" value="${obj.ticketPrice}"
						onkeyup="num(this)" onafterpaste="return false" onpaste="return false"/>???
				</c:otherwise>
				</c:choose>
			</td>
		</tr>
		<tr>
			<th>???????????????</th>
			<td colspan=3>
				<div class="hyjb_box">
    				<div class="hyjb_tj">
   					<c:if test="${not empty obj}">
   						<c:forEach items="${obj.meetingGuests}" var="item">
    					<ul class="hyjb_dl">
							<li>
								<dl>
									<dt><a href="javascript:void(0)"><img  class="guestImg" src="${item.imgUrl}" /></a></dt>
									<dd><a href="javascript:void(0)"><img onclick="removeGuest(this)" src="${path}/resource/images/tjy/close_04.gif" width="25" height="25" /></a></dd>
								</dl>
							</li>
							<li class="hyjb_shuru"><input maxlength="20" class="guestName" value="${item.name}" type="text" placeholder="?????????????????????" /></li>
						</ul>
   						</c:forEach>
   					</c:if>
    				
   						<img id="addGuest" onclick="uploadImg('guest')" 
   							src="${path}/resource/images/tjy/jia_03.png" 
   							style="cursor:pointer;margin-top:12px;margin-left:12px;height:100px;width:120px;" width="112" height="112" />
    				</div>
				</div>
			</td>
		</tr>
		<tr>
			<th>????????????<font color="red">*</font>???</th>
			<td colspan=3>
				<textarea id="mau_myEditor" name="contents" style="width:700px;height:100px;">
					${obj.contents}
				</textarea>
			</td>
		</tr>
		<tr>
			<th>???????????????</th>
			<td colspan=3>
				<table style="width:100%;" class="table table-bordered" >
					<thead>
						<tr>
							<td colspan=3>
								<button class="btn btn-primary btn-small" type="button" onclick="addProjDialog('add')">????????????</button>&nbsp;
								<button class="btn btn-primary btn-small" type="button" onclick="delProj('del')">????????????</button>&nbsp;
							</td>
						</tr>
						<tr>
							<th style="width:15px;text-align: center;"></th>
							<th style="width:60%;text-align: center;">????????????</th>
							<th style="width:40%;text-align: center;">????????????</th>
						</tr>
					</thead>
					<tbody id="projTbody">
					<c:if test="${not empty obj}">
   						<c:forEach items="${obj.meetingProjects}" var="item">
						<tr data-id="${item.id}">
							<td class="projId" data-id="${item.id}"><input type="checkbox" name="checkbox"></td>
							<td>${item.titles}</td>
							<td>${item.prjTypeName}</td>
						</tr>
   						</c:forEach>
   					</c:if>		
					</tbody>
				</table>
			</td>
		</tr>
		<tr>
			<th>???????????????</th>
			<td colspan=3>
				<table style="width:100%;" class="table table-bordered" >
					<thead>
					<tr>
						<td colspan=3>
							<button class="btn btn-primary btn-small" type="button" onclick="addMeetingDialog('add')">????????????</button>&nbsp;
							<button class="btn btn-primary btn-small" type="button" onclick="delMeetingSuccessive('del')">????????????</button>&nbsp;
						</td>
					</tr>
					<tr>
						<th style="width:15px;text-align: center;"></th>
						<th style="width:100%;text-align: center;">????????????</th>
					</tr>
					</thead>
					<tbody id="meetingTbody">
					<c:if test="${not empty obj}">
						<c:forEach items="${obj.meetingSuccessiveList}" var="item">
							<tr data-id="${item.id}">
								<td class="projId" data-id="${item.id}"><input type="checkbox" name="checkbox"></td>
								<td>${item.titles}</td>
							</tr>
						</c:forEach>
					</c:if>
					</tbody>
				</table>
			</td>
		</tr>
		<tr>
			<th>?????????????????????</th>
			<td>
				<input type="checkbox" <c:if test="${contentYunYou==true}">checked</c:if> id="contentYunYou" name="contentYunYou" value="1"/>??????
				<input type="checkbox" <c:if test="${contentYunQin==true}">checked</c:if> id="contentYunQin" name="contentYunQin" value="2"/>??????
				<input type="checkbox" <c:if test="${contentYunShang==true}">checked</c:if> id="contentYunShang" name="contentYunShang" value="4"/>??????
			</td>
			<th>??????????????????</th>
			<td>
				<input type="checkbox" <c:if test="${videoYunYou==true}">checked</c:if>  id="videoYunYou" name="videoYunYou" value="1"/>??????
				<input type="checkbox" <c:if test="${videoYunQin==true}">checked</c:if> id="videoYunQin" name="videoYunQin" value="2"/>??????
				<input type="checkbox" <c:if test="${videoYunShang==true}">checked</c:if> id="videoYunShang" name="videoYunShang" value="4"/>??????
			</td>
		</tr>
		<tr>
			<th></th>
			<td colspan=3>
				<div  style="margin-top: 10px;margin-bottom: 10px;">
					  <button type="button" onclick="save()" class="btn btn-primary" >??????</button>&nbsp;&nbsp;&nbsp;&nbsp;
					  <button type="button" onclick="cancel()" class="btn clear" >??????</button>
				</div>
			</td>
		 </tr>
	</table>
</form>
<span class="typefile" id="filePicker"></span>
</div>
<script type="text/javascript">
var options = {
        cssPath : 'resource/js/editor/plugins/code/prettify.css',
        filterMode : false,
		uploadJson:'news/upload.do?ysStyle=YS640&moduleName=meeting',
		width : '320px',
		height:'600px',
		resizeType : 1,
		allowImageUpload : true,
		allowFlashUpload : false,
		allowMediaUpload : false,
		allowFileManager : false,
		syncType:"form",
		afterCreate : function() {
							var self = this;
							self.sync();
						},
		afterChange : function() {
							var self = this;
							self.sync();
						},
		afterBlur : function() {
							var self = this;
							self.sync();
						},
						items:['source', '|', 'fullscreen', 'undo', 'redo', 'print', 'cut', 'copy', 'paste',
								'plainpaste', 'wordpaste', '|', 'justifyleft', 'justifycenter', 'justifyright',
								'justifyfull', 'insertorderedlist', 'insertunorderedlist', 'indent', 'outdent', 'subscript',
								'superscript', '|', 'selectall', 'clearhtml','quickformat','|',
								'formatblock', 'fontname', 'fontsize', '|', 'forecolor', 'hilitecolor', 'bold',
								'italic', 'underline', 'strikethrough', 'lineheight', 'removeformat', '|', 'image','flash', 'media', 'table', 'hr', 'emoticons', 'link', 'unlink', '|', 'about']
}; 
var uploader = null;
$(function() {
	if($("#ticketPriceType").val()==1){
		$("#mau_ticketPrice").val(0);
		$("#mau_ticketPrice").hide();
	}else{
		$("#mau_ticketPrice").show();
	}
	typesChange();
	initWebUpload();
	querycitys(1);
	editor = KindEditor.create('#mau_myEditor',options);
});
function delImg(){
	$(".removePic").hide();
	$("#uploadPic").empty();
	$("#uploadPic").append('<span style="display:block;width:80px;height:12px;padding:50px 40px;">??????????????????</span>');
}
var imgType = "";
function uploadImg(type){
	imgType = type;
	$("#filePicker :file").click();
}
function initWebUpload(){
	uploader = WebUploader.create({
		auto: true,
	    server: "meetingApp/uploadpic.do?moduleName=meeting",
	    pick: "#filePicker",
	    fileNumLimit: 5000,//?????????????????????????????????
	    fileSingleSizeLimit: 2 * 1024*1024,
	    duplicate : true,
	    accept: {
	        title: "Images",
	        extensions: "gif,jpg,jpeg,bmp,png",
	        mimeTypes: 'image/jpg,image/jpeg,image/png'
	    }
	});
	uploader.on("uploadSuccess", function( file, response ) {
		if(response.code==0){
			if(imgType=="hb"){
				$("#hb").attr("src",response.dataobj.img_url+"?x-oss-process=style/YSMAX640");
				$("#hb").attr("status","1");
				$("#hb").removeAttr("onclick")
				$("#deleteHb").show();
			}else if(imgType=="guest"){
				var htmlStr = 
					'<ul class="hyjb_dl">'
						+'<li>'
							+'<dl>'
								+'<dt><a href="javascript:void(0)"><img  class="guestImg" src="'+response.dataobj.img_url+'?x-oss-process=style/YS200200" /></a></dt>'
								+'<dd><a href="javascript:void(0)"><img onclick="removeGuest(this)" src="${path}/resource/images/tjy/close_04.gif" width="25" height="25" /></a></dd>'
							+'</dl>'
						+'</li>'
						+'<li class="hyjb_shuru"><input class="guestName" type="text" placeholder="?????????????????????" /></li>'
					+'</ul>';
				$("#addGuest").before(htmlStr)
			}
		}else{
			alert(response.msg)
		}
	});
	uploader.on( 'uploadError', function( file ) {
		alert("????????????")
	});
	uploader.on('error', function(handler) {
		if(handler=="F_EXCEED_SIZE"){
			alert("???????????????2M???PNG???JPG???JPEG????????????????????????"+(imgType==="hb"?"750*350":"100*100"))
			return;
		}else if(handler=="Q_TYPE_DENIED"){
			alert("???????????????2M???PNG???JPG???JPEG????????????????????????"+(imgType==="hb"?"750*350":"100*100"))
			return;
		}
		alert("????????????")
	});
}
var validate = [
   {id: "mau_titles", text: "?????????????????????",type:"text"},
   {id: "mau_place", text: "?????????????????????",type:"text"},
   {id: "mau_startTime", text: "??????????????????",type:"time"},
   {id: "mau_endTime", text: "??????????????????",type:"time"},
   {id: "mau_query_startSignupTime", text: "??????????????????",type:"time"},
   {id: "mau_query_endSignupTime", text: "??????????????????",type:"time"},
   {id: "mau_ticketPrice", text: "????????????"},
   {id: "mau_sponsor", text: "??????????????????",type:"text"},
   {id: "mau_webinarId", text: "??????????????????",type:"select"},
   {id: "mau_myEditor", text: "????????????"}
];
function save(){
	editor.sync();//???????????????????????????texterea
	//????????????????????????
	if($("#hb").attr("status")!="1"){
		alert("?????????????????????");
		return;
	}
	if($("#sort").val()==''){
		alert("???????????????");
		return;
	}
	for(var i in validate){
		if(validate[i]["id"]=="mau_myEditor"){
			if(editor.text().trim().length==0){
				alert("?????????"+validate[i]["text"]);
				return; 
			}else{
				continue;
			}
		}
		if(validate[i]["id"]=="mau_ticketPrice"){
			if($("#ticketPriceType").val()==1){
				$("#mau_ticketPrice").hide(0);
			}else{
				$("#mau_ticketPrice").show();
			}
		}
		if($("#mau_types").val()==1||$("#mau_types").val()==3){
			$(".living").show();
		}else{
			$(".living").hide();
		}
		
		var value = $("#"+validate[i]["id"]).val().trim()||"";
		var type = validate[i]["type"]||"";
		var maxlength = validate[i]["maxlength"]||1000;
		if("select"===type&&value==""&&($("#mau_types").val()==1||$("#mau_types").val()==3)){
			alert("?????????"+validate[i]["text"]);
			return;
		}
		
		if("text"===type&&(value==""||value.cnLength()>maxlength)){
			alert(validate[i]["text"]);
			return; 
		}
		
		if("select"!=type&&value==""){
			alert("?????????"+validate[i]["text"]);
			return; 
		}
	}
	if($("#mau_startTime").val()>=$("#mau_endTime").val()){
		alert("???????????????????????????????????????");
		return;
	}
	if($("#mau_query_startSignupTime").val()>=$("#mau_query_endSignupTime").val()){
		alert("?????????????????????????????????????????????");
		return;
	}
	if($("#mau_query_endSignupTime").val()>$("#mau_endTime").val()){
		alert("?????????????????????????????????????????????");
		return;
	}
	
	if($("#ticketPriceType").val()==2&&
			($("#mau_ticketPrice").val()==0||$("#mau_ticketPrice").val()>999999.99)){
		alert("???????????????????????????????????????0.01 ~  999999.99");
		return;
	}
	if($("#mau_upperlimit").val()>99999999){
		alert("????????????????????????99999999");
		return;
	}
	
	
	$("#coverImg").val($("#hb").attr("src"));
	var guestArr = [];
	//??????????????????
	var guestOk = true;
	$(".hyjb_dl .guestName").each(function(){
		if(guestOk&&($(this).val().trim()==""||$(this).val().cnLength()>20)){
			guestOk =false;
		}
	});
	if(!guestOk){
		alert("???????????????????????????????????????1-20??????");
		return;
	}
	//??????
	$(".hyjb_dl .guestImg").each(function(){
		var imgUrl = $(this).attr("src");
		var name = $(this).parent().parent().parent().parent().next().children(":first").val();
		guestArr.push({imgUrl: imgUrl,name: name});
	});
	//??????
	var projArr=[];
	$(".projId").each(function(){
		var projId = $(this).attr("data-id");
		projArr.push(projId);
	});
	 
	$("#projIds").val(projArr.join(","));

	//????????????
	var meetingIdSuccArr=[];
	$(".meetingIdSucc").each(function(){
		var meetingId = $(this).attr("data-id");
		meetingIdSuccArr.push(meetingId);
	});

	$("#meetingIds").val(meetingIdSuccArr.join(","));
	
	//????????????
	var webinarIdArr=[];
	$(".webinarId").each(function(){
		var webinarId = $(this).attr("data-id");
		webinarIdArr.push(webinarId);
	});
	
	$("#webinarIds").val(webinarIdArr.join(","));
	
	$("#guests").val(JSON.stringify(guestArr));

	//??????????????????
	var contentVisibleRange = 0;

	if($("#contentYunYou").is(':checked')) {
		contentVisibleRange = contentVisibleRange | $("#contentYunYou").val();
	}
	if($("#contentYunQin").is(':checked')) {
		contentVisibleRange = contentVisibleRange | $("#contentYunQin").val();
	}
	if($("#contentYunShang").is(':checked')) {
		contentVisibleRange = contentVisibleRange | $("#contentYunShang").val();
	}

	if(contentVisibleRange==0){
		alert("???????????????????????????");
		return;
	}
	$("#contentVisibleRange").val(contentVisibleRange);
	//??????????????????
	var videoVisibleRange = 0;
	if($("#videoYunYou").is(':checked')) {
		videoVisibleRange = videoVisibleRange | $("#videoYunYou").val();
	}
	if($("#videoYunQin").is(':checked')) {
		videoVisibleRange = videoVisibleRange | $("#videoYunQin").val();
	}
	if($("#videoYunShang").is(':checked')) {
		videoVisibleRange = videoVisibleRange | $("#videoYunShang").val();
	}
	if(videoVisibleRange==0){
		alert("???????????????????????????");
		return;
	}
	$("#videoVisibleRange").val(videoVisibleRange);

    $.ajax({
		url:"meetingApp/meetingsaveorupdate.do",
		type: 'post',	
		data: $("#dataForm").serializeObject(),
		cache: false,
		dataType:"json",
		success:function(json){
			if(json&&json["code"]==="0"){
				$("#meetingindex_datagrid").datagrid('reload');
				$("#meetingaddpage").dialog("destroy");
				$("#meetingupdatepage").dialog("destroy");
			}else{
				alert("????????????");
			}			
		}
	});
}
function cancel(){
	$("#meetingaddpage").dialog("destroy");
}
function removeGuest(obj){
	$(obj).parent().parent().parent().parent().parent().remove();
}
function removeHb(){
	$("#hb").attr("src","${path}/resource/images/tjy/djsc.png");
	$("#hb").attr("status","0");
	$("#hb").attr("onclick","uploadImg('hb')");
	$("#deleteHb").hide();
}
//????????????(????????????)
function addMeetingDialog(){
	var params = {closed: false,cache: false,modal:true,width:1000,height:400,collapsible:false,minimizable:false,maximizable:false};
	MUI.openDialog('????????????','meetingApp/meetingSuccessive/index.do?id=',"meetingSuccessiveindexformeeting",params)
}
//????????????
function addProjDialog(){
	var params = {closed: false,cache: false,modal:true,width:1000,height:400,collapsible:false,minimizable:false,maximizable:false};
	MUI.openDialog('????????????','meetingApp/projet/index.do?id=',"projectindexformeeting",params)
}
//??????????????????
function addVhallDialog(){
	var params = {closed: false,cache: false,modal:true,width:1000,height:400,collapsible:false,minimizable:false,maximizable:false};
	MUI.openDialog('????????????','meetingApp/vhall/index.do?id=',"vhallindexformeeting",params)
}
//??????????????????
function addVhallOver(){
	var params = {closed: false,cache: false,modal:true,width:1000,height:400,collapsible:false,minimizable:false,maximizable:false};
	MUI.openDialog('????????????','meetingApp/vhallapp/index.do?id=',"vhallindexformeeting",params)
}

//????????????
function addMeetingSuccessive(arr){

	for(var i=0;i<arr.length;i++){
		if($("#meetingTbody tr[data-id='"+arr[i].id+"']").length>0){
			continue;
		}
		var $tr = $("<tr data-id=\""+arr[i].id+"\"></tr>");
		$tr.append('<td class="meetingIdSucc" data-id="'+arr[i].id+'"><input type="checkbox" name="checkbox"></td>');
		$tr.append('<td>'+arr[i].titles+'</td>');
		$("#meetingTbody").append($tr);
	}
}
function delMeetingSuccessive(){
	if($("#meetingTbody input:checkbox[name=checkbox]:checked").length==0){
		Msg.alert("??????","??????????????????","warning",null);
		return;
	}
	$("#meetingTbody input:checkbox[name=checkbox]:checked").each(function () {
		$(this).parent().parent().remove();
	})
}

function addProject(arr){

	for(var i=0;i<arr.length;i++){
		if($("#projTbody tr[data-id='"+arr[i].id+"']").length>0){
			continue;
		}
		var $tr = $("<tr data-id=\""+arr[i].id+"\"></tr>");
		$tr.append('<td class="projId" data-id="'+arr[i].id+'"><input type="checkbox" name="checkbox"></td>');
		$tr.append('<td>'+arr[i].titles+'</td>');
		$tr.append('<td>'+arr[i].prjTypeName+'</td>');
		$("#projTbody").append($tr);
	}
}
function delProj(){
	if($("#projTbody input:checkbox[name=checkbox]:checked").length==0){
		Msg.alert("??????","??????????????????","warning",null);
		return;
	}
	$("#projTbody input:checkbox[name=checkbox]:checked").each(function () {
	    $(this).parent().parent().remove();
	})
}
//????????????
function delVhall(){
	if($("#vhallTbody input:checkbox[name=checkbox]:checked").length==0){
		Msg.alert("??????","??????????????????","warning",null);
		return;
	}
	$("#vhallTbody input:checkbox[name=checkbox]:checked").each(function () {
	    $(this).parent().parent().remove();
	})
}
function addVhall(arr){

	for(var i=0;i<arr.length;i++){
		if($("#vhallTbody tr[data-id='"+arr[i].id+"']").length>0){
			continue;
		}
		var $tr = $("<tr data-id=\""+arr[i].id+"\"></tr>");
		$tr.append('<td class="webinarId" data-id="'+arr[i].id+'"><input type="checkbox" name="checkbox"></td>');
		/* $tr.append('<td>'+arr[i].id+'</td>'); */
		$tr.append('<td>'+arr[i].subject+'</td>');
		$("#vhallTbody").append($tr);
	}
}


function num(obj){
	obj.value = obj.value.replace(/[^\d.]/g,""); //??????"??????"???"."???????????????
	obj.value = obj.value.replace(/^\./g,""); //??????????????????????????????
	//obj.value = obj.value.replace(/\.{2,}/g,"."); //??????????????????, ???????????????
	obj.value = obj.value.replace(".","$#$").replace(/\./g,"").replace("$#$",".");
	obj.value = obj.value.replace(/^(\-)*(\d+)\.(\d\d).*$/,'$1$2.$3'); //????????????????????????
}
function num2(obj){
	obj.value = obj.value.replace(/[^\d]/g,""); //??????"??????"???"."???????????????
}
function ticketTypeChange(type){
	if($("#ticketPriceType").val()==1){
		$("#mau_ticketPrice").val(0);
		$("#mau_ticketPrice").hide();
	}else{
		$("#mau_ticketPrice").val(0);
		$("#mau_ticketPrice").show();
	}
}
function typesChange(obj){
	if($("#mau_types").val()==1||$("#mau_types").val()==3){
		$(".living").show();
	}else{
		$(".living").hide();
	}
}

function querycitys(type){
	//??????select
	$("#activitycitys").empty();
	//????????????id
	var pid = $("#province").val();
	$.ajax({
		url : "activity/citys.do",
		dataType : "json",
		data : {
			"pid" :pid 
		},
		async : false,
		success : function(data){
			var opstr = "";
			data.forEach(function(obj, index, array) {
				if("${obj.city}"==obj.id){
					  opstr+='<option id="'+obj.id+'" value="'+obj.id+'"  selected="selected">'+obj.disName+'</option>';
					}else{
				  		opstr+='<option id="'+obj.id+'" value="'+obj.id+'">'+obj.disName+'</option>';
					}
			});
			$("#activitycitys").append(opstr);
			querycountys();
		}
	});
	
}

function querycountys(){
	//??????select
	$("#activitycountys").empty();
	//????????????id
	var pid = $("#activitycitys").val();
	$.ajax({
		url : "activity/citys.do",
		dataType : "json",
		data : {
			"pid" :pid 
		},
		async : false,
		success : function(data){
			var opstr = "";
			data.forEach(function(obj, index, array) {
				if("${obj.county}"==obj.id){
					  opstr+='<option id="'+obj.id+'" value="'+obj.id+'"  selected="selected">'+obj.disName+'</option>';
					}else{
				  		opstr+='<option id="'+obj.id+'" value="'+obj.id+'">'+obj.disName+'</option>';
					}
			});
			$("#activitycountys").append(opstr);
		}
	});
}
</script>