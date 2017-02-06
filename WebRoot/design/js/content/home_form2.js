//初始化加载文件列表
$(document).ready(function(){DocumentType(2);radio();});
//备份选中状态的id
function backup(){
	var index =layer.prompt({
		  formType: 0,		// 0（文本）默认 1（密码）2（多行文本）
		  title: '备份文件名',
		  offset: ['100px','']
		}, function(listname){//文件名
		    var id_array=new Array();  
			$('input[name="a"]:checked').each(function(){  
			    id_array.push($(this).attr('id'));//向数组中添加元素  
			});  
			$('input[name="aa"]:checked').each(function(){  
			    id_array.push($(this).attr('id'));
			}); 
			var idstr=id_array.join(',');//将数组元素连接起来以构建一个字符串  
			$.ajax( {
				data : {'listname':listname,'list':idstr},
				type : "GET",
				dataType : "json",
				url : "/MLOAN/data/norm.do",
				error: function() {
					alert("error!---服务器返回失败");		//服务器返回失败调用的方法
				},
				success : function(data) {
					if(data.success!=null){
						layer.close(index); 
						alert(data.success);
					}else{
						alert(data.errormsg);
					}
}})});}
//导入选中状态
function checkselect(){
	var username = $("#username").text();  //后期调整为当前-----用户名
	$.ajax({
		data : {'username':username},
		type : "GET",
		dataType : "json",			//  /ajax/map     index/chart
		url : "/MLOAN/data/selectinfo.do",
		error: function() {
			alert("error!---导入时服务器返回失败");		//服务器返回失败调用的方法
		},
		success : function(data) {
			var list = "";
			for (var i = 0; i < data.length; i++) {
				list+="<p style='border: 1px solid #eee;cursor: pointer;' onclick='layer_p("+i+")'>"+data[i].listname+"</p>";
			}
			if(data.length==0)
				list = "无备份文件";
			layer.open({
				  content: list
				  ,btn: ['确定', '取消']
				  ,offset: ['100px','']
				  ,yes: function(index, layero){
					  var open = document.getElementsByClassName("layui-layer-content")[0];
						  ps = open.getElementsByTagName('p');
						  listname="";
					  for(var i = 0; i < ps.length; i++) {
							var bg = ps[i].style.background;
							if(bg=="rgb(189, 216, 243)"){
								listname = ps[i].innerHTML;
							};
						}
					  layer.close(index); //如果设定了yes回调，需进行手工关闭
					  for (var i = 0; i < data.length; i++) {
							if(listname==data[i].listname){
								checkAll("a","false");   //先取消所有选中
								checkAll("aa","false");
								array = (data[i].list).split(',');
								for(var i=0;i<array.length;i++){
									var arg = document.getElementById(array[i]);
									if(arg!=null)
										arg.checked = true;
								}
								break;
							}}
				  },btn2: function(index, layero){
				  },cancel: function(){}});
}})
}
function def(){
	checkAll("a","false");
	checkAll("aa","false");
	var ids = indexDef();  // 默认指标配置
	for ( var id in ids) {
		var arg = document.getElementById(ids[id]);
		if(arg!=null)
			arg.checked = true;
	}
};
function allCheck(){
	checkAll("a","true");
	checkAll("aa","true");
};
//导入文件列表   状态
function layer_p(no){
	var open = document.getElementsByClassName("layui-layer-content")[0];
	var ps = open.getElementsByTagName('p');
	for(var i = 0; i < ps.length; i++) {
		ps[i].style.background = "#ffffff";
	}
	ps[no].style.background = "#bdd8f3";
}
//一键分析  （传递默认指标，并跳转报告数据展示解析后的数据）
var filecheck = null;
function root(){
		var fileNum = $('input[name="filecheck"]:checked').length;
		if(fileNum==0){
			alert("请选择文件");
			return;
		}
	    var id_array=new Array();  // 文件列表
		$('input[name="filecheck"]:checked').each(function(){  
		    id_array.push($(this).attr('id'));//向数组中添加元素  
		});  
		filecheck = id_array.join(',');//将数组元素连接起来以构建一个字符串  
		
		id_array.length=0; //通用指标
		$('input[name="a"]:checked').each(function(){  
			var id = $(this).attr('value');
		    id_array.push(id);
		});  
		var norm=id_array.join(',');
		
		id_array.length=0; //业务个性指标
		$('input[name="aa"]:checked').each(function(){  
		    id_array.push($(this).attr('value'));
		});  
		var kidney=id_array.join(',');
			
		if(norm=="" && kidney==""){
			alert("请选择通用指标");
			return;
		}
		document.getElementById("over").style.display = "block";
	    document.getElementById("layout").style.display = "block";
		$.ajax( {
			data : {'filecheck':filecheck,'norm':norm,'kidney':kidney},
			type : "GET",
			dataType : "json",
			url : "/MLOAN/reportOut/oneKey.do",
			error: function() {
				alert("error!---服务器返回失败");		//服务器返回失败调用的方法
				document.getElementById("over").style.display = "";
			    document.getElementById("layout").style.display = "";
			},success : function(data){
				sessionExist(data);
				document.getElementById("over").style.display = "";
			    document.getElementById("layout").style.display = "";
				chartAjax(data);	//图表数据
}})}
var list_i=0;
function listchecking(name){
	if(list_i%2==0){
		checkAll(name,true);
		document.getElementById('listchecking').style.background='#cecece';
	}else{
		checkAll(name,false);
		document.getElementById('listchecking').style.background='';
	}list_i++;}
var file_i=0;
function filechecking(name){
	if(file_i%2==0){
		checkAll(name,true);
		document.getElementById('filechecking').style.background='#cecece';
	}else{
		checkAll(name,false);
		document.getElementById('filechecking').style.background='';
	}file_i++;
	nums();
}
/**
 * 文档的格式
 */
function DocumentType(format){
	if(format==1){
		format = "txt";
	}else if(format==2){
		format = "pcap";
	}else if(format==3){
		format = "all";
	}
	$.ajax({
		dataType:"json",
		url : "/MLOAN/file/fileFormatList.do?format="+format,
		error: function(){
			alert("error!---说明服务器返回失败");
		},success : function(data) {
			sessionExist(data);
			var sortBy = function (filed, rev, primer) {
			    rev = (rev) ? -1 : 1;
			    return function (a, b) {
			        a = a[filed];
			        b = b[filed];
			        if (typeof (primer) != 'undefined') {
			            a = primer(a);
			            b = primer(b);
			        }
			        if (a < b) { return rev * -1; }
			        if (a > b) { return rev * 1; }
			        return 1;
			    }
			};
			data.sort(sortBy('last_date', true, String));//data.sort(sortBy('last_date', false, parseInt)); 将last_date升序排列数字类型 

			var file = "<table style='margin-top: 5px;margin-left: 5px;border-collapse: collapse;border: solid 1px  #cccccc;'><tr class='title' style='text-align: center;background: #f8f8f8;font-size: 14px;'>"; 

			file+="<td id='filechecking' style='width: 40px;cursor: pointer;' onclick=\"filechecking('filecheck')\"><input type='button' value='全选' style='cursor: pointer;'></td>";   
			file+="<td style='border-left: solid 1px #eee;width: 250px;'>名称</td>";
			file+="<td style='border-left: solid 1px #eee;width: 160px;'>生成时间</td>"
			file+="<td style='border-left: solid 1px #eee;width:50px;'>大小</td>";   
			
			for(var i=0;i<data.length;i++){
				var args = data[i];
				var addDelNo = i+1;
				file += "<tr class='tr'>";	// id='"+args.name+"'
				file += "<td style='border-top: solid 1px #eee;text-align: center;'>";
				file += "<input type='checkbox' class='boxutil' name='filecheck' id='"+args.name+"' value='"+i+"'onclick='nums()'></td>";   
				file += "<td style='border-left: solid 1px #eee;border-top: solid 1px #eee;'>"+args.name+"</td>";   
				file += "<td style='border-left: solid 1px #eee;border-top: solid 1px #eee;text-align: center;'>"+args.last_date+"</td>";
				file += "<td style='border-left: solid 1px #eee;text-align: right;border-top: solid 1px #eee;'>"+args.size+"</td>";   
			}
			file+="</table>";  
			$("#file").html(file);
			box();
}})}
$('#func ul li').click(function(){
	$(this).addClass('hit').siblings().removeClass('hit');
	$('#genre>div:eq('+$(this).index()+')').show().siblings().hide();})
	
function nums(){
	var i=0;
	$('input[name="filecheck"]:checked').each(function(){  
	  i++;
	});  
	document.getElementById('nums').innerHTML = '已选：'+i;
}	
	
	
	
	
	
	
	
	
	