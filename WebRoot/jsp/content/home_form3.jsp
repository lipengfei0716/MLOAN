<%@ page pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
  <head>
    <script src="<%=request.getContextPath()%>/design/js/off/echarts.js" type="text/javascript"></script>
    <script src="<%=request.getContextPath()%>/design/js/content/home_form3.js" type="text/javascript"></script>
    <script src="<%=request.getContextPath()%>/design/js/off/highcharts1.js"></script>
    <script src="<%=request.getContextPath()%>/design/js/off/exporting.js"></script>
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/design/css/content/home_form3.css" />
  </head>
  <div class='norm' id='norm' >
	<!--这是悬浮 -->
		<div id="comm" class="comm">
			<span><img src="<%=request.getContextPath()%>/design/img/index/use.png" width="14px" height="14px" />通用</span>
			<div class='table1' id='table1'> 
				<table id="tb1" class="tb1" cellspacing="0"></table>
				<p  class='tb1De' id='tb1De'>&nbsp;&nbsp;&nbsp;
					<a href='JavaScript:normData()' id='tb1DeTen' class='tb1DeTen'></a>
				</p>
				<!-- 位置放到了table中了 -->
		     </div>
		     <div class='table1Info' id='table1Info'> 
	             <div>
					<table id="tb1Detail" class="tb1Detail" style='display: none;' cellspacing="0"></table>
					<div id="tag1" class="tag1" style='display:none;'></div>
				</div>
				 <div id='contr1' class='contr1'  style='display: none;'>
					 <img src="<%=request.getContextPath()%>/design/img/index/assembly.png" width="20" height="20"  onclick="tag1()"/>
					 <img src="<%=request.getContextPath()%>/design/img/index/copy.png" width="20" height="20"  id="img1"/>
				</div>
			</div>			
		</div>
		
		<div id="indiv" class="indiv">
		   	<span><img src="<%=request.getContextPath()%>/design/img/index/indiv.png" width="14" height="14"/>业务个性</span>
			<div class="table1">	
				<table id="tb2" class="tb2" cellspacing="0"></table>
				<div id="tag2" class="tag2"></div>
			</div>
		</div> 
	</div>
	<div class='charts' id='charts'></div>
	<div class="model">
		<span>模板选取</span>
		<div class="temp">
			<ul class="box">
				<li>
					<img alt="" src="<%=request.getContextPath()%>/design/img/index/Template.jpg"/>
					<input type="checkbox"/>总结类模板
				</li>
				<li>
					<img alt="" src="<%=request.getContextPath()%>/design/img/index/Template.jpg"/><br/>
					<input type="checkbox"/>研究类模板
				</li><li>
					<img alt="" src="<%=request.getContextPath()%>/design/img/index/Template.jpg"/><br/>
					<input type="checkbox"/>测评类模板
				</li><li>
					<img alt="" src="<%=request.getContextPath()%>/design/img/index/Template.jpg"/><br/>
					<input type="checkbox"/>汇报类模板
				</li>
			</ul>
		</div>
		<div class="report">
			<span>报告格式</span><br/>
			<div>PDF</div><input type="radio" name="FileType"  value="1" onclick="show()"/>
			<div>EXCEL</div><input type="radio" name="FileType"  value="2" onclick="hidden1()"/>
		</div>
		<div id="showDiv" style="display: none">
			<br/><span>水印编辑</span><br/>
			<input type="text" value="CMLAB-测试所-无线室" id="watermark" style="width:230px;height: 50px;"/>
		</div>
		<input type="button" onclick="ExportExcel()" value="生成报告" class="build"/>
	</div>
