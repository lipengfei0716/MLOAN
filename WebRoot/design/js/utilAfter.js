											/**	 table	*/
function showHideTr(tableId, row) { // 显示、隐藏 指定行
		
	var Table = document.getElementById(tableId);
	var tr = Table.rows[row];
	if (tr != null){
		var state = tr.style.display;
		switch (state) {
			case "none": tr.style.display=""; break;
			default:     tr.style.display="none";
		}
	}
}
function showTr(tableId,row,col) {	//显示    指定行、列
	var Table = document.getElementById(tableId);var td = Table.rows[row].cells[col];if(td!=null)td.style.display = "";}
function hiddenTr(tableId,row,col) {	//隐藏  指定行、列
	var Table = document.getElementById(tableId);var td = Table.rows[row].cells[col];if(td!=null)td.style.display = "none";}
function showHiddenTd(tableId,iCol,state) {	//显示/隐藏  指定列行、列   -----第一列存在合并单元格
	var Table = document.getElementById(tableId);
	var len = Table.rows.length;
	Table.rows[0].cells[iCol].style.display = state;
	var i = 0;
	while (i < (len-2)) {
		Table.rows[i++].cells[iCol].style.display = state;
	}
}

function hiddenTd(tableId,iCol) {	//隐藏  指定列行、列   -----第一列存在合并单元格
	var Table = document.getElementById(tableId);
	var len = Table.rows.length;
	Table.rows[0].cells[iCol].style.display = "none";
	var i = 2;
	var i1 = 2;
	while (i1 < (len-2)) {
		var k = Table.rows[i1-1].cells[0].rowSpan; // 第一列
		var i2 = 1;
		while (i2 < k) {
				Table.rows[i].cells[iCol-1].style.display = "none";
			i2 += 2;
			i += 2;
		}
		i1 += k;
		i ++;
	}
}
function showTd(tableId,iCol) {	//显示  指定列行、列   -----第一列存在合并单元格
	var Table = document.getElementById(tableId);
	var len = Table.rows.length;
	Table.rows[0].cells[iCol].style.display = "";
	var i = 2;
	var i1 = 2;
	while (i1 < (len-2)) {
		var k = Table.rows[i1-1].cells[0].rowSpan; // 第一列
		var i2 = 1;
		while (i2 < k) {
				Table.rows[i].cells[iCol-1].style.display = "";
			i2 += 2;
			i += 2;
		}
		i1 += k;
		i ++;
	}
}
function delAllTr(tableId)				//删除所有行
	{var tb = document.getElementById(tableId);var rowNum=tb.rows.length;for (i=0;i<rowNum;i++){tb.deleteRow(i);rowNum=rowNum-1;i=i-1;}}											
											/**	Map集合	*/
function Map() {
	this.elements = new Array();
	// 获取MAP元素个数
	this.size = function() {return this.elements.length;};
	// 判断MAP是否为空
	this.isEmpty = function() {return (this.elements.length < 1);};
	// 删除MAP所有元素
	this.clear = function() {this.elements = new Array();};
	// 向MAP中增加元素（key, value)
	this.put = function(_key, _value) {this.elements.push({key: _key,value: _value});};
	// 删除指定KEY的元素，成功返回True，失败返回False
	this.remove = function(_key) {var bln = false;try {for (i = 0; i < this.elements.length; i++) {if (this.elements[i].key == _key) {this.elements.splice(i, 1);return true;}}} catch (e) {bln = false;}return bln;};
	// 获取指定KEY的元素值VALUE，失败返回NULL
	this.get = function(_key) {try {for (i = 0; i < this.elements.length; i++) {if (this.elements[i].key == _key) {return this.elements[i].value;}}} catch (e) {return null;}};
	// 获取指定索引的元素（使用element.key，element.value获取KEY和VALUE），失败返回NULL
	this.element = function(_index) {if (_index < 0 || _index >= this.elements.length) {return null;}return this.elements[_index];};
	// 判断MAP中是否含有指定KEY的元素
	this.containsKey = function(_key) {var bln = false;try {for (i = 0; i < this.elements.length; i++) {if (this.elements[i].key == _key) {bln = true;}}} catch (e) {bln = false;}return bln;};
	// 判断MAP中是否含有指定VALUE的元素
	this.containsValue = function(_value) {var bln = false;try {for (i = 0; i < this.elements.length; i++) {if (this.elements[i].value == _value) {bln = true;}}} catch (e) {bln = false;}return bln;};
	// 获取MAP中所有VALUE的数组（ARRAY）
	this.values = function() {var arr = new Array();for (i = 0; i < this.elements.length; i++) {arr.push(this.elements[i].value);}return arr;};
	// 获取MAP中所有KEY的数组（ARRAY）
	this.keys = function() {var arr = new Array();for (i = 0; i < this.elements.length; i++) {arr.push(this.elements[i].key);}return arr;};}
/**
 * 根据对象中  key取值，对所有value排序
 * sortBy('ar1', false, parseInt); 将ar1升序排列数字类型    
 * sortBy('ar1', true, String)  将ar1降序排列字符串  */
function sortBy(filed, rev, primer) {rev = (rev) ? -1 : 1;return function (a, b) {a = a[filed];b = b[filed];if (typeof (primer) != 'undefined') {a = primer(a);b = primer(b);}if (a < b) { return rev * -1; }if (a > b) { return rev * 1; }return 1;}};
							
													/**	List集合	*/
function List() {
	this.value = [];
	/* 添加 */
	this.add = function(obj) {return this.value.push(obj);};
	/* 大小 */
	this.size = function() {return this.value.length;};
	/* 返回指定索引的值 */
	this.get = function(index) {return this.value[index];};
	/* 删除指定索引的值 */
	this.remove = function(index) {this.value.splice(index, 1);return this.value;};
	/* 删除全部值 */
	this.removeAll = function() {return this.value = [];};
	/* 是否包含某个对象 */
	this.constains = function(obj) {for ( var i in this.value) {if (obj == this.value[i]) {return true;} else {continue;}}return false;};
	this.getAll = function() {var allInfos = '';for ( var i in this.value) {if (i != (value.length - 1)) {allInfos += this.value[i] + ",";} else {allInfos += this.value[i];}}alert(allInfos);return allInfos += this.value[i] + ",";};}
													/**图表  柱状、折线、饼状*/
//------------------柱状图-------------------------
function ipFlow(title,id,x,list_y){
	//基于准备好的dom，初始化echarts实例
	var myChart = echarts.init(document.getElementById(id));
	// 指定图表的配置项和数据
	option = {
		title: {text: title,x:'center'},
		animation:false,
	    color: ['#3398DB'],
	    tooltip : {trigger: 'axis',axisPointer : {            // 坐标轴指示器，坐标轴触发有效
	            type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
	        }},
	    grid: {left: '0%',right: '0%',bottom: '16%',containLabel: true},
	    xAxis : [{type : 'category',data : eval(x),axisTick: {alignWithLabel: true},axisLabel: {rotate: 0}}],yAxis : [{type : 'value' ,axisLabel: {formatter: '{value} Byte'}}],
	    series : eval(list_y)//[{name:'直接访问',type:'bar',barWidth: '60%',data:[1,2,1,8]}]
	};
	// 使用刚指定的配置项和数据显示图表。
	myChart.setOption(option);
	return myChart;
}
/*function use2(title,id,x,y,list_y) {
	var myChart = echarts.init(document.getElementById(id));
	option = {
	    title: {text: title,x:'center'},
	    tooltip : {trigger: 'axis',axisPointer : { // 坐标轴指示器，坐标轴触发有效
	            			type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
        }},legend: {data:  eval(y)						   //['链路1','链路2','链路3']
	    },
	    grid: {left: '2%',right: '4%',bottom: '0%',containLabel: true},
	    xAxis : [{type : 'category',data :  eval(x)	//['ip1','ip2']
	    }],yAxis : [{type : 'value'}],
	    series :  eval(list_y) //[{name:'链路1',type:'bar',data:[ip1, ip2]},{name:'链路2',type:'bar',data:[ip1, ip2]}]
	};
	myChart.setOption(option);
	return myChart;
}*/
function linkChart1(title,id,y,list_y,topLeg,topGr) {
	var myChart = echarts.init(document.getElementById(id));
	id = {
		title: {text: title,x:'center'},// 相对位置，放置在容器正中间
	    tooltip : {triggerOn:'mousemove',   //  mousemove  click
	    		   formatter: function(params) {return linkFormat1(params,id)},  
	    		   padding: [1,1,1,1],  // 上右下左
	    		   trigger: 'axis',   //item
				   axisPointer : {type : 'shadow'} // 坐标轴指示器，坐标轴触发有效     默认为直线，可选为：'line' | 'shadow'
	    		},
	    position: function (point, params, dom) {
	  		      // 固定在顶部
	  		      return [point[0]+15, "0%"];
	  		  },
        legend: {top:topLeg,data:['DNS Lookup(ms)', 'Interval(ms)', 'SYN(ms)','Time to First Byte(ms)','Interactive(ms)','Delay(ms)','FIN(ms)']},
        grid: {top:topGr,left: '0%',right: '3%',bottom: '0%',containLabel: true},calculable : true,
        xAxis : [{type : 'value',axisLabel:{formatter:function(val) {return val+"ms";}}}],
        yAxis : {show:false,type : 'category',data :eval(y),axisLabel:{formatter:function(val) {return val;},interval:0}},
        series : eval(list_y),color:['#008B8B','#8A2BE2','#CDCD00','#00FF00','#00BFFF','#CD0000','#7A8B8B'],barGap:'1%'
	};
	myChart.setOption(id);
	return myChart;
}
function linkChart2(title,id,y,list_y,topLeg,topGr) {
	var myChart = echarts.init(document.getElementById(id));
	id = {
		title: {text: title,x:'center'},// 相对位置，放置在容器正中间
	/*	position: function (point, params, dom) {
		      // 固定在顶部
		      return [point[0], point[1]];
		  },*/
	    tooltip : {triggerOn:'mousemove',   //  mousemove  click
	    		   padding: [15,1,1,1],  // 上右下左
	    		   trigger: 'item',
	    		   formatter: function(params) {return linkFormat2(params,id)},  
				   axisPointer : {type : 'shadow'} // 坐标轴指示器，坐标轴触发有效     默认为直线，可选为：'line' | 'shadow'
	    		},
        legend: {top:topLeg,data:['DNS Lookup(ms)', 'Interval(ms)', 'SYN(ms)','Time to First Byte(ms)','Interactive(ms)','Delay(ms)','FIN(ms)']},
        grid: {top:topGr,left: '0%',right: '3%',bottom: '0%',containLabel: true},calculable : true,
        xAxis : [{type : 'value',axisLabel:{formatter:function(val) {return val+"ms";}}}],
        yAxis : {show:false,type : 'category',data :eval(y),axisLabel:{formatter:function(val) {return val;},interval:0}},
        series : eval(list_y),color:['#008B8B','#8A2BE2','#CDCD00','#00FF00','#00BFFF','#CD0000','#7A8B8B'],barGap:'1%'
	};
	myChart.setOption(id);
	return myChart;
}
function linkChart3(title,id,y,list_y,topLeg,topGr) {
	var myChart = echarts.init(document.getElementById(id));
	id = {
		title: {left: '0%',text: title,x:'center'},// 相对位置，放置在容器正中间
	    tooltip : {triggerOn:'mousemove',   //  mousemove  click
	    		   formatter: function(params) {return linkFormat1(params,id)},  
	    		   padding: [1,1,1,1],  // 上右下左
	    		   trigger: 'axis',   //item
				   axisPointer : {type : 'shadow'} // 坐标轴指示器，坐标轴触发有效     默认为直线，可选为：'line' | 'shadow'
	    		},
	    position: function (point, params, dom) {
	  		      // 固定在顶部
	  		      return [point[0]+15, "0%"];
	  		  },
        legend: {top:topLeg,data:['DNS Lookup(ms)', 'Interval(ms)', 'SYN(ms)','Time to First Byte(ms)','Interactive(ms)','Delay(ms)','FIN(ms)']},
        grid: {top:topGr,left: '0%',right: '3%',bottom: '0%',containLabel: true},calculable : true,
        xAxis : [{type : 'value',axisLabel:{formatter:function(val) {return val+"ms";}}}],
        yAxis : {show:true,type : 'category',data :eval(y),axisLabel:{formatter:function(val) {return val;},interval:0}},
        series : eval(list_y),color:['#008B8B','#8A2BE2','#CDCD00','#00FF00','#00BFFF','#CD0000','#7A8B8B'],barGap:'1%'
	};
	myChart.setOption(id);
	return myChart;
}
function linkChart4(title,id,y,list_y,topLeg,topGr) {
	var myChart = echarts.init(document.getElementById(id));
	id = {
		title: {left: '0%',text: title,x:'center'},// 相对位置，放置在容器正中间
	/*	position: function (point, params, dom) {
		      // 固定在顶部
		      return [point[0], point[1]];
		  },*/
	    tooltip : {triggerOn:'mousemove',   //  mousemove  click
	    		   padding: [15,1,1,1],  // 上右下左
	    		   trigger: 'item',
	    		   formatter: function(params) {return linkFormat2(params,id)},  
				   axisPointer : {type : 'shadow'} // 坐标轴指示器，坐标轴触发有效     默认为直线，可选为：'line' | 'shadow'
	    		},
        legend: {top:topLeg,data:['DNS Lookup(ms)', 'Interval(ms)', 'SYN(ms)','Time to First Byte(ms)','Interactive(ms)','Delay(ms)','FIN(ms)']},
        grid: {top:topGr,left: '0%',right: '3%',bottom: '0%',containLabel: true},calculable : true,
        xAxis : [{type : 'value',axisLabel:{formatter:function(val) {return val+"ms";}}}],
        yAxis : {show:true,type : 'category',data :eval(y),axisLabel:{formatter:function(val) {return val;},interval:0}},
        series : eval(list_y),color:['#008B8B','#8A2BE2','#CDCD00','#00FF00','#00BFFF','#CD0000','#7A8B8B'],barGap:'1%'
	};
	myChart.setOption(id);
	return myChart;
}
function linkFormat1(params,id) {
    var res = params[0].name;  
    var y = id.yAxis.data;
    var yNo = 0;
    for(var key in y){
    	if(y[key]==res)
    		break;
    	yNo++;
    }
    var myseries = id.series;  
    var linkTime=0;
    var serLen = myseries.length;
    for (var i = 0; i < myseries.length; i++) {  
    	var name = myseries[i].name;
    	var len = name.length-4;
        res+= '<br/>'+name.substring(0,len);  
        res+=' : '+myseries[i].data[yNo];  
        if(i>0){
         	 if(i<serLen-2){
         		 linkTime += myseries[i].data[yNo];
         	 }else if(myseries[serLen-1].data[yNo]>0){
         		 linkTime += myseries[i].data[yNo];
              }
          }
    }  
    res+= '<br/>链路时长：'+linkTime.toFixed(2);
    return res;  
}
function linkFormat2(params,id) {
    var res = params.name;  
    var y = id.yAxis.data;
    var yNo = 0;
    for(var key in y){
    	if(y[key]==res)
    		break;
    	yNo++;
    }
    var myseries = id.series;  
    var linkTime=0;
    var serLen = myseries.length;
    for (var i = 0; i < myseries.length; i++) {  
    	var name = myseries[i].name;
    	var len = name.length-4;
        res+= '<br/>'+name.substring(0,len);  
        res+=' : '+myseries[i].data[yNo];  
        if(i>0){
         	 if(i<serLen-2){
         		 linkTime += myseries[i].data[yNo];
         	 }else if(myseries[serLen-1].data[yNo]>0){
         		 linkTime += myseries[i].data[yNo];
              }
          }
    }  
    res+= '<br/>链路时长：'+linkTime.toFixed(2);
    return res;  
}
function link1(titles,y,START,DNS,Interval,FIRST_DATA,Interactive,Delay,id,pointWidth1) {
	    $("#"+id).highcharts({
	            chart: {
	                type: 'bar'
	            },
	            title: {
	                text: titles
	            },
	            xAxis: {
	                categories: y
	            },
	            yAxis: {
	                min: 0,
	                title: {
	                    text: 'Total fruit consumption'
	                }
	            },
	            legend: {
	                reversed: true
	            },
	            plotOptions: {
	                series: {
	                    stacking: 'normal',
	                    pointWidth: pointWidth1
	                }
	            },
	            series: [{
	                name: 'START',
	                data: START
	            }, {
	                name: 'DNS',
	                data: DNS
	            }, {
	                name: 'Interval',
	                data: Interval
	            }, {
	                name: 'FIRST_DATA',
	                data: FIRST_DATA
	            }, {
	                name: 'Interactive',
	                data: Interactive
	            }, {
	                name: 'Delay',
	                data: Delay
	            }]
	        });
}
//------------------折线图-------------------------
function use2(title,id,list,x,list_y) {    //折线图堆叠
	var myChart = echarts.init(document.getElementById(id));
	option = {
			animation:false,
		    title: {text: title,x:'center'},
		    tooltip: {top:'20%',trigger: 'axis'},
		    legend: {data:eval(list),top: '5%'},
		    grid: {top:'20%',left: '1%', right: '2%',bottom: '0%',containLabel: true},
		    toolbox: {feature: { }},
		    xAxis: {type: 'category',axisLabel: {formatter: '{value}s'},boundaryGap: false,data: eval(x)},		//  ['周一','周二','周三','周四','周五','周六','周日']
		    yAxis: { type: 'value',axisLabel: {formatter: '{value} Byte'}},series: eval(list_y)// [{name:'status',type:'line',stack: '总量',data:[1,2,1,8]}]
		};
	myChart.setOption(option);
	return myChart;
}
//------------------3D饼图-------------------------
function packetLen(title,id,x,list_y,subtext) {
	var myChart = echarts.init(document.getElementById(id));
	option = {
			animation:false,
		    title : {text: title,subtext: subtext,x:'center'	//
		    },
		    tooltip : { trigger: 'item', formatter: "{a} <br/>{b} : {c} ({d}%)" },
		    legend: {orient: 'vertical',left: 'left',top: '10%',data: eval(x)	//['直接访问','邮件营销']  与下面data->name数据对应
		    },
		    grid: {left: '0%',right: '0%',bottom: '0%',containLabel: true},
		    series : [{ label: {normal: {position: 'inner',formatter: '{d}%',textStyle: {color: '#ffffff',fontWeight: 'bold',fontSize: 14}}},name: '',type: 'pie' ,radius : '55%',center: ['50%', '60%']
		           ,data: eval(list_y)//[{value:335, name:'直接访问'},{value:310, name:'邮件营销'}]
		           ,itemStyle: {emphasis: {shadowBlur: 10,shadowOffsetX: 0, shadowColor: 'rgba(0, 0, 0, 0.5)'}}}]};
	myChart.setOption(option);
	return myChart;
}
								/**图表生成图片 至 服务器*/
function imageEx(imgname,myChart){
	var data = (""+imgname+"=" + encodeURIComponent(myChart.getDataURL({type:'png', pixelRatio: 1,backgroundColor: 'white'})));
    $.ajax({type:'post',data: data, dataType:'xml',url : "/MLOAN/img/chart.req?imgname="+imgname}); }
								/**checkbox*/
function checkAll(arg,bool){  //复选框 	全选/取消  arg = checkname,bool=false/true
	var checkboxes = document.getElementsByName(arg);for (var i = 0; i < checkboxes.length; i++) {if(checkboxes[i].value=="Server IP")continue;checkboxes[i].checked = eval(bool);}}
								/** 判断 后台session 是否失效*/
function sessionExist(data) {
	var username = data.session;
	if (username == "lose") {
		layer.config({
			extend : [ 'skin/espresso/style.css' ]// 加载新皮肤
		});
		layer
			.open({
				type : 1,
				skin : 'layer-ext-espresso' // 样式类名
				,shift : 0 // 弹窗动画 ， ie6-9不支持 //,shadeClose: true //开启遮罩关闭
				,content : '<input onclick="window.location.href=\'/MLOAN/jsp/admin/login.jsp\'" type="button" value=\'确定\'style=\'margin-top: 17px;margin-left: 96px;font-size: 16px;cursor: pointer;\'>',
				closeBtn : 0,
				title : '会话超时，请重新登录&nbsp;&nbsp;&nbsp;',
				area : [ '245px', '110px' ],
				offset : [ '150px' ]
			});
	}else if(username == "remote"){
		layer.config({
			extend : [ 'skin/espresso/style.css' ]
		});
		layer
				.open({
					type : 1,
					skin : 'layer-ext-espresso',shift : 0,
					content : '<input onclick="window.location.href=\'/MLOAN/jsp/admin/login.jsp\'" type="button" value=\'确定\'style=\'margin-top: 17px;margin-left: 130px;font-size: 16px;cursor: pointer;\'>',
					closeBtn : 0,
					title : '用户已在其它地点登录，请重新登录！',
					area : [ '325px', '110px' ],
					offset : [ '150px' ]
				});
	}
}




