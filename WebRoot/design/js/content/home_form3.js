var norm_graph =null;
var huffPuff =null;
var ipFlows = null;
var links = null;
var normWidth = null;
var tb1Width = null;
var Width = null;
var linkd = null;
var currentIndex;
var linkWidth = 14;
var currentIndex;
var network = true;
var ipAddrEnd = false;
function chartAjax(data){
		
		norm_graph =null;
		huffPuff =null;
		ipFlows = null;
		links = null;
		normWidth = null;
		tb1Width = null;
		tb1DetailWidth = null;
		document.getElementById("norm").style.width=null;
		document.getElementById("tb1").style.width=null;
		document.getElementById("table1Info").style.width=null;
		document.getElementById("tb1Detail").style.width=null;
		document.getElementById("right").style.width=null;
		
		MainTop(3);
		delAllTr("tb1");
		delAllTr("tb1Detail");
		var pathName = window.document.location.pathname;
		$("#tag1").empty();
		$("#charts").empty();
		
		$("#tag1").append(data.norm_edit);
		$("#tb1").append(data.norm_table);
		$("#tb1Detail").append(data.norm_norm);
		
		normWidth = $("#norm").width();
		tb1Width = $("#tb1").width();
		tb1DetailWidth = $("#tb1Detail").width();
		rightWidth = $("#right").width(); //home.jsp  全局变量
		
		document.getElementById("tb1").style.width=(normWidth-10)+"px";
		document.getElementById("tb1De").style.width=(normWidth-10)+"px";
		
		document.getElementById("contr1").style.display="none";
		document.getElementById("tb1Detail").style.display="none";
		document.getElementById("tag1").style.display="none";
		if(data.norm_table)
			document.getElementById("tb1").style.border='solid 1px #cccccc';
		if(data.norm_norm!=null)
			document.getElementById("tb1De").style.display='block';
		var norm_minSumList = data.norm_minSumList;
		if(norm_minSumList!=null){
			var minSumList = JSON.parse(norm_minSumList);
			 $.each(minSumList,function(key,value) {
				 document.getElementById(value).style.color="green";
		       });
		}
		normData();
		
		norm_graph = data.norm_graph;
		if(norm_graph!=null){
			var graph=JSON.parse(norm_graph);
			ipFlows = graph.IP流量分布图;
			if(ipFlows!=null){
				$("#charts").append("<div style='width: 550px'>IP流量分布图</div>");
				var ipFlowJs = JSON.parse(ipFlows);
				for(var i in ipFlowJs){
					var info = JSON.parse(ipFlowJs[i]);
					var id = info.id;
					if(eval(i)%2==0)
						$("#charts").append("<div id='"+id+"' class='use1' STYLE='border-width:1pt; border-color:#DDDDDD' ></div>");
					else
						$("#charts").append("<div id='"+id+"' class='use2' STYLE='border-width:1pt; border-color:#DDDDDD' ></div>");
					ipFlow(info.title,id,info.x,info.list_y);
				}
			}
			var spread = graph.包长分布图;
			if(spread!=null){
				$("#charts").append("<div style='width: 550px'>包长分布图</div>");
				var spreadObj = JSON.parse(spread);
				for(var i in spreadObj){
					var spreadJs = JSON.parse(spreadObj[i]);
					var id = spreadJs.id;
					if(eval(i)%2==0)
						$("#charts").append("<div id='"+id+"' class='use1' STYLE='border-width:1pt; border-color:#DDDDDD' ></div>");
					else
						$("#charts").append("<div id='"+id+"' class='use2' STYLE='border-width:1pt; border-color:#DDDDDD' ></div>");
					packetLen(spreadJs.title,id,spreadJs.x,spreadJs.list_y,spreadJs.subtext);
				}
			}
			huffPuff = graph.吞吐曲线图;
			if(huffPuff!=null){
				$("#charts").append("<div style='width: 550px'>吞吐曲线图</div>");
				var huffPuffJs = JSON.parse(huffPuff);
				var id = huffPuffJs.id;
				$("#charts").append("<div id='"+id+"' class='use' STYLE='border-width:1pt; border-color:#DDDDDD' ></div>");
				use2(huffPuffJs.title,id,huffPuffJs.list,huffPuffJs.x_str,huffPuffJs.list_y);
			}
			links = graph.链路图;
			if(links!=null){
				$("#charts").append("<div style='width: 550px'>链路图</div>");
				var linksObj = JSON.parse(links);
				for(var i in linksObj){
					var info = JSON.parse(linksObj[i]);
					var id = info.id;
					var height1 = info.height
					$("#charts").append(
							"<div id='change"+id+"' style='height:"+(eval(height1)+95)+"px; overflow-x: auto;position:relative;width:1150px;float:left;border-right: solid 1px #DDDDDD;'>" +
								"<div  id='"+id+"' style='height:"+(eval(height1)+65)+"px;width:1150px;border: solid 1px #ccc;border-left: solid 1px #DDDDDD;'></div>" +
							"</div>" +
							"<div  id='remain"+id+"' style='height:"+(eval(height1))+"px;width:130px;overflow:hidden;position:absolute;font-size:10px;border-left: solid 1px #DDDDDD;border-top: solid 1px #DDDDDD;line-height:12px;padding-top:45px;background:white;text-align:right;'>"+info.ipPort+"</div>" +
							"<div style='text-align:center'>" +
								"<input type='submit' value='扩大' onclick='magnify(\""+id+"\")'/>" +
								"<input type='submit' value='缩小'  onclick='shrink(\""+id+"\")' />" +
							"</div>");
					if(info.height < 259)
						linkChart1(info.title,id,info.y,info.list_y,info.topLeg,info.topGr);
					else
						linkChart2(info.title,id,info.y,info.list_y,info.topLeg,info.topGr);
				}
			}
			currentIndex = 0;
			network = true;
			ipAddrEnd = false;
			getIpAddr();
		}
}
function getIpAddr(){
	var ips="";
	$("td[name='ipAddr']").each(function(){  
		if(this.innerHTML=="N/A")
			ips += ","+$(this).attr('id');
	});  
	if(ips.length>1 && currentIndex++<3 && network){
		$.ajax( {
			data : {'ips':ips},
			type : "GET",
			dataType : "json",
			url : "/MLOAN/reportOut/ipAddr.do",
			error: function() {ipAddrEnd = true;},
			success : function(data) {
				for(var ip in data){
					document.getElementById(ip).innerHTML = data[ip];
				}
				normIpRefresh();
				getIpAddr();
			}
		})
	}else
		ipAddrEnd = true;
}
function shrink(linkId){
	var width = document.getElementById(linkId).style.width;
	var height = document.getElementById(linkId).style.height;
	var widthCurr = width.substr(0, width.length-2);
	var heightCurr = height.substr(0, width.length-2);
	if(widthCurr>1150){
		var linksObj = JSON.parse(links);
		for(var i in linksObj){
			var info = JSON.parse(linksObj[i]);
			var id = info.id;
			if(id==linkId){
				$("#change"+id+"").empty();
				document.getElementById("change"+id).innerHTML = "<div id='"+id+"' class='use' style='height:"+(eval(info.height)+65)+"px;width:"+(eval(widthCurr)-400)+"px;'></div>";
				if(info.height < 259)
					linkChart1(info.title,id,info.y,info.list_y,info.topLeg,info.topGr);
				else
					linkChart2(info.title,id,info.y,info.list_y,info.topLeg,info.topGr);
			}
		}
	}else{
		alert("已经是最小了")
	}
}
function magnify(linkId){
	var width = document.getElementById(linkId).style.width;
	var widthCurr = width.substr(0, width.length-2)
	if(widthCurr<3150){
		var linksObj = JSON.parse(links);
		for(var i in linksObj){
			var info = JSON.parse(linksObj[i]);
			var id = info.id;
			if(id==linkId){
				$("#change"+id+"").empty();
				document.getElementById("change"+id).innerHTML = "<div id='"+id+"' class='use' style='height:"+(eval(info.height)+65)+"px;width:"+(eval(widthCurr)+400)+"px;'></div>";
				if(info.height < 259)
					linkChart1(info.title,id,info.y,info.list_y,info.topLeg,info.topGr);
				else
					linkChart2(info.title,id,info.y,info.list_y,info.topLeg,info.topGr);
			}
		}
	}else{
		alert("已经是最大了")
	}
}

function changeTd(tagId,tableId,col,no){
	var tags = document.all(tagId).getElementsByTagName("input");
	var status = tags[no].checked; 
	if(status){
		showHiddenTd(tableId,col,"");
	}else{
		showHiddenTd(tableId,col,"none");
	}
}
function draw(){
    var bg = document.getElementById("draw").style.background;
    switch (bg) {
	case "": document.getElementById("norm").style.width='inherit';
			 document.getElementById("ico1").style.width=$("#norm").width+"px";
			 document.getElementById("ico1").style.width=$("#norm").height+"px";
			 document.getElementById("draw").style.background="#0093D3";
			 document.getElementById("draw").innerText= "数据";
			 chart();
			 break;
	default: if(normWidth1>normWidth){
			 	document.getElementById("norm").style.width=normWidth1+"px";
			 }else
				document.getElementById("norm").style.width='inherit';
			 document.getElementById("draw").style.background="";
			 document.getElementById("draw").innerText= "图表";
			 data();
	}
}
var normWidth1 = 0;
function normIpRefresh(){
	
	tb1DetailWidth = $("#tb1Detail").width();
	var state = document.getElementById("contr1").style.display;
	if(state==""){

		var tb1Height = $("#tb1").height();
		document.getElementById("tag1").style.top=(tb1Height+200)+"px";	
		
		var currMax = normWidth-30;
		if(currMax>tb1DetailWidth){
			document.getElementById("tb1").style.width=currMax+"px";
			document.getElementById("tb1De").style.width=currMax+"px";
			document.getElementById("tb1Detail").style.width=currMax+"px";
			document.getElementById("table1Info").style.width=normWidth+"px";
			document.getElementById("tag1").style.left=(currMax-25)+"px";	
		}else{
			document.getElementById("norm").style.width=(tb1DetailWidth+25)+"px";
			document.getElementById("comm").style.width=(tb1DetailWidth+30)+"px";
			document.getElementById("indiv").style.width=(tb1DetailWidth+30)+"px";
			document.getElementById("tb1").style.width=tb1DetailWidth+"px";	
			document.getElementById("tb1De").style.width=tb1DetailWidth+"px";
			document.getElementById("table1Info").style.width=(tb1DetailWidth+35)+"px";	
			document.getElementById("tag1").style.left=(tb1DetailWidth-25)+"px";	
			normWidth1 = tb1DetailWidth+35;
		}
	}
}
function normData(){
	var state = document.getElementById("contr1").style.display;
	if(state=="none"){
		
		document.getElementById("tb1DeTen").innerHTML="-详细信息";	
		var tb1Height = $("#tb1").height();
		document.getElementById("tag1").style.top=(tb1Height+200)+"px";	
		
		document.getElementById("contr1").style.display="";
		document.getElementById("tb1Detail").style.display="";
		var currMax = normWidth-30;
		if(currMax>tb1DetailWidth){
			document.getElementById("tb1").style.width=currMax+"px";
			document.getElementById("tb1De").style.width=currMax+"px";
			document.getElementById("tb1Detail").style.width=currMax+"px";
			document.getElementById("table1Info").style.width=normWidth+"px";
			document.getElementById("tag1").style.left=(currMax-25)+"px";	
		}else{
			document.getElementById("norm").style.width=(tb1DetailWidth+25)+"px";
			document.getElementById("comm").style.width=(tb1DetailWidth+30)+"px";
			document.getElementById("indiv").style.width=(tb1DetailWidth+30)+"px";
			document.getElementById("tb1").style.width=tb1DetailWidth+"px";	
			document.getElementById("tb1De").style.width=tb1DetailWidth+"px";
			document.getElementById("table1Info").style.width=(tb1DetailWidth+35)+"px";	
			document.getElementById("tag1").style.left=(tb1DetailWidth-25)+"px";	
			normWidth1 = tb1DetailWidth+35;
		}
	}else{
		document.getElementById("tb1DeTen").innerHTML="+详细信息";	
		document.getElementById("contr1").style.display="none";
		document.getElementById("tb1Detail").style.display="none";
		document.getElementById("tag1").style.display="none";
		
		if(tb1Width>normWidth){
		 	document.getElementById("norm").style.width='inherit';
			document.getElementById("comm").style.width='1110px';
			document.getElementById("indiv").style.width='1110px';
		 }else{
			document.getElementById("comm").style.width=(normWidth-10)+"px";
			document.getElementById("norm").style.width='inherit';
			document.getElementById("indiv").style.width='inherit';
		 }
		document.getElementById("tb1").style.width=(normWidth-10)+"px";
		document.getElementById("tb1De").style.width=(normWidth-10)+"px";
		normWidth1 = tb1Width;
	}
}
function nullIP(){
	var state = document.getElementById("nullIp").style.display;
	if(state=="none"){
		document.getElementById("tb1DetailTen").innerHTML="&nbsp;&nbsp;&nbsp;-&nbsp;无效IP";
		document.getElementById("nullIp").style.display="";
	}else{
		document.getElementById("tb1DetailTen").innerHTML="&nbsp;&nbsp;&nbsp;+&nbsp;无效IP";
		document.getElementById("nullIp").style.display="none";
	}
}
/**导出方法
 */
function ExportExcel(){
	var  iName=""
	if(norm_graph!=null){
		var graph=JSON.parse(norm_graph);
		ipFlows = graph.IP流量分布图;
		iName="aIP流量分布图";
		if(ipFlows!=null){
			var ipFlowJs = JSON.parse(ipFlows);
			for(var i in ipFlowJs){
				var info = JSON.parse(ipFlowJs[i]);
				var id = info.id;
				console.log(id)
				var myChart = ipFlow(info.title,id,info.x,info.list_y);
				imageEx(id, myChart,iName);
			}
		}
		huffPuff = graph.吞吐曲线图;
		if(huffPuff!=null){
			iName="c吞吐曲线图";
			var huffPuffJs = JSON.parse(huffPuff);
			var id = huffPuffJs.id;
			var myChart = use2(huffPuffJs.title,id,huffPuffJs.list,huffPuffJs.x_str,huffPuffJs.list_y);
			imageEx(id, myChart,iName);
		}
		var spread = graph.包长分布图;
		if(spread!=null){
			iName="b包长分布图";
			var spreadObj = JSON.parse(spread);
			for(var i in spreadObj){
				var spreadJs = JSON.parse(spreadObj[i]);
				var id = spreadJs.id;
				var myChart = packetLen(spreadJs.title,id,spreadJs.x,spreadJs.list_y,spreadJs.subtext);
				imageEx(id, myChart,iName);
			}
		links = graph.链路图;
		if(links!=null){
		iName="d链路图";
			var linksObj = JSON.parse(links);
			for(var i in linksObj){
				var info = JSON.parse(linksObj[i]);
				var id = info.id;
				var myChart = null;
				if(info.height < 259)
					myChart = linkChart3(info.title,id,info.y,info.list_y,info.topLeg,info.topGr);
				else
					myChart = linkChart4(info.title,id,info.y,info.list_y,info.topLeg,info.topGr);
				imageEx(id, myChart,iName);
			}
		}
	}
}
	var dataStr="";
    var tableObj = document.getElementById("tb1");
    var tableObj1 = document.getElementById("tb1Detail");
    var te1 = "";
    var te1= document.getElementById("tb1Detail").style.display;
    /**
     * 通用指标遍历
     */
    for (var i = 0; i < tableObj.rows.length; i++) { //遍历Table的所有Row
    	var tableInfo = "";
    	if(te1!="none"){
    		for (var j = 0; j < tableObj.rows[i].cells.length; j++) {   //遍历Row中的每一列
   			 var te = "";
   			 te = document.getElementById("tb1").rows[i].cells[j].style.display;
   			 if(te!="none"){
   					 tableInfo+= ",,"+tableObj.rows[i].cells[j].innerText;  //获取Table中单元格的内容
   			 	}
   			 }
   		 tableInfo=tableInfo.substring(2);		 
   	     dataStr+=("~~"+tableInfo);
	          }else {
    		for (var j = 0; j < tableObj.rows[i].cells.length-1; j++) {   //遍历Row中的每一列
      			 var te = "";
      			 te = document.getElementById("tb1").rows[i].cells[j].style.display;
      			 if(te!="none"){
      					 tableInfo+= ",,"+tableObj.rows[i].cells[j].innerText;  //获取Table中单元格的内容
      			 	}
      			 }
      		 tableInfo=tableInfo.substring(2);		 
      	     dataStr+=("~~"+tableInfo);
   	          }	
    	}
    		 
    /**
     * 详细信息遍历
     */
    dataStr=dataStr.substring(2);
    var t1= document.getElementById("tb1Detail").style.display;
//    if(t1!="none"){
    	var dataStr1="";
    	for (var x = 0; x < tableObj1.rows.length-2; x++) { //遍历Table的所有Row
        	var tableInfo = "";
        		 for (var y = 0; y < tableObj1.rows[x].cells.length; y++) {   //遍历Row中的每一列
        			 var te = "";
        			 var te1 = "";
          			 te = document.getElementById("tb1Detail").rows[x].cells[y].getAttribute("title");
          			 te1 = document.getElementById("tb1Detail").rows[x].cells[y].style.display;
        			 if(te1!="none"){
        				 tableInfo+= ",,"+tableObj1.rows[x].cells[y].innerText; //获取Table中单元格的内容
        			 	}
        			 }
        		 tableInfo=tableInfo.substring(2);		 
        		 dataStr1+=("~~"+tableInfo);
     	          }
        dataStr1=dataStr1.substring(2);
//    }
    /**
     * 无效ip遍历
     */
    if(t1!="none"){
    	dataNull="";
    	for (var x = tableObj1.rows.length-2; x < tableObj1.rows.length; x++) { //遍历Table的所有Row
        	var tablenull = "";
        		 for (var y = 0; y < 1; y++) {   //遍历Row中的每一列
        			 if(te1!="none"){
        				 tablenull+= ",,"+tableObj1.rows[x].cells[y].innerText; //获取Table中单元格的内容
        			 	}
        			 }
        		 tableInfo=tableInfo.substring(2);		 
        		 dataNull+=("~~"+tablenull);
     	          }
    	dataNull=dataNull.substring(2);
    }
    /**
     * 导出方法
     */
   var Testvalue = document.getElementById('watermark').value;
   var  TitleText =document.getElementById('title').innerText;
   console.log(TitleText)
	var isAutoSend = document.getElementsByName('FileType');
	console.log(isAutoSend)
	for (var i = 0; i < isAutoSend.length; i++) {
	if (isAutoSend[i].checked == true) {
    	if(isAutoSend[i].value==1){
    			DownLoadFile({   
                url:'/MLOAN/FilePdf/CreatePDF.do', //请求的url  
                data:{data1:dataStr,Svalue:Testvalue,data2:dataStr1,data3:dataNull,Title:TitleText,Chenck:filecheck}//要发送的数据  
                });  
			}else if(isAutoSend[i].value==3){
				DownLoadFile({   
	                url:'/MLOAN/file/CreatePPT.do', //请求的url  
	                data:{data1:dataStr,data2:dataStr1}//要发送的数据  
	                });  
			}else if(isAutoSend[i].value==2){
				DownLoadFile({   
	                url:'/MLOAN/file/ExportExcel.do', //请求的url  
	                data:{data1:dataStr,data2:dataStr1,data3:dataNull}//要发送的数据  
	                }); 
		}
	}
 }
}


/**图表生成图片 至 服务器*/
function imageEx(imgname,myChart,iName){
	var data = (""+imgname+"=" + encodeURIComponent(myChart.getDataURL({type:'png', pixelRatio: 1,backgroundColor: 'white'})));
    $.ajax({type:'post',data: data, dataType:'xml',url : "/MLOAN/img/chart.req?imgname="+iName}); }

var DownLoadFile = function (options) {  
    var config = $.extend(true, { method: 'post' }, options);  
    var $iframe = $('<iframe id="down-file-iframe" />');  
    var $form = $('<form target="down-file-iframe" method="' + config.method + '" />');  
    $form.attr('action', config.url);  
    for (var key in config.data) {  
        $form.append('<input type="hidden" name="' + key + '" value="' + config.data[key] + '" />');  
    }  
    $iframe.append($form);  
    $(document.body).append($iframe);  
    $form[0].submit();  
    $iframe.remove();  
}

/*****************  Quote  ************************/
/*function data(){	
	document.getElementById('comm').style.display='';
	document.getElementById('ico1').style.display='none';
	document.getElementById('indiv').style.display='';
	document.getElementById('ico2').style.display='none';
	document.getElementById('tb1').style.display='';
	document.getElementById('tb2').style.display='';}
function chart(){ 
	document.getElementById('comm').style.display='none';
	document.getElementById('ico1').style.display='';
	document.getElementById('indiv').style.display='none';
	document.getElementById('ico2').style.display='';
	document.getElementById('tb1').style.display='none';
	document.getElementById('tb2').style.display='none';}*/
function tag1(){
	var state = document.getElementById('tag1').style.display;
	if(state=='none'){
		document.getElementById('tag1').style.display='';
	}else
		document.getElementById('tag1').style.display='none';}
function tag2(){
	var state = document.getElementById('tag2').style.display;
	if(state=='none'){
		document.getElementById('tag2').style.display='';
	}else
		document.getElementById('tag2').style.display='none';}
