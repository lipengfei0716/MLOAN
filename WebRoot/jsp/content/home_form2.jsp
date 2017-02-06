<%@ page pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
  <head>
	<link 	href="<%=request.getContextPath()%>/design/css/content/home_form2.css" rel="stylesheet" type="text/css"/>
	<script src="<%=request.getContextPath()%>/design/js/content/home_form2.js" type="text/javascript"></script>
	<script src="<%=request.getContextPath()%>/design/js/config.js" type="text/javascript"></script>
  </head>
  	<div class="filelist" id="filelist">
		<div class="left">
			<div id="file" class="file"></div>
			<div style='font-size: 12px;margin-left: 12px;margin-top: 5px;'>
				<input type="radio" id="rad1" name="rad[]" onclick="DocumentType(1)"/>Txt文档
				<input type="radio" id="rad2" name="rad[]" checked="checked" onclick="DocumentType(2)"/>Pcap文档
				<input type="radio" id="rad3" name="rad[]" onclick="DocumentType(3)"/>查看全部
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<div id='nums' style='float: right;margin-right: 55px;'>已选：0</div>
			 </div>
		</div>
		<div style="float: left; width: 100%;">
			<div class="root" id="root" onclick="root()">分析</div>
		</div>
	</div>
	<div class="norm" id="normid">
		<a style="display:inherit;text-decoration: none;"><img src="<%=request.getContextPath()%>/design/img/index/use.png" width="14" height="14" />通用指标</a>
		<div class='comm'>
			<table>
				<tr class="tr">
					<td><input type="checkbox" class="boxutil" name="a" id="a2" value="IP 归属" checked='checked'/></td>
					<td class="index" colspan="3"
						title="IP归属地，用于判断是否存在调度问题">IP 归属</td>
					<td><input type="checkbox" class="boxutil" name="a" id="a3" value="测试地归属" checked='checked'/></td>
					<td class="index" colspan="3"
						title="">测试地归属</td>
					<td><input type="checkbox" class="boxutil" name="a" id="a4" value="请求类别" checked='checked'/></td>
					<td class="index" colspan="3"
						title="">请求类别</td>
					<td><input type="checkbox" class="boxutil" name="a" id="a5" value="DNS时延" checked='checked'/></td>
					<td class="index" style='border-right: solid 0px;'
						title="DNS服务器响应客户端请求的时间">DNS时延</td>
					<td style='width:19px;border-left: solid 0px;border-right: solid 0px;'></td>
					<td style='width:73px;border-left: solid 0px;border-right: solid 0px;'></td>
					<td><input type="checkbox" class="boxutil" name="a" id="a6" value="TCP建链时延" checked='checked'/></td>
					<td class="index" style='border-right: solid 0px;'
						title="客户端与服务器建链时，TCP三次握手完成的时间">TCP建链时延</td>
					<td style='width:19px;border-left: solid 0px;border-right: solid 0px;'></td>
					<td style='width:73px;border-left: solid 0px;border-right: solid 0px;'></td>
					<td><input type="checkbox" class="boxutil" name="a" id="a9" value="包数" checked='checked'/></td>
					<td class="index" style='border-right: solid 0px;'
						title="整个log文件内，所有服务器IP的总包数、每一个服务器IP的交互数据包数">包数</td>
					<td style='width:19px;border-left: solid 0px;border-right: solid 0px;'></td>
					<td style='width:73px;border-left: solid 0px;'></td>
				</tr>
				<tr class="tr">
					<td><input type="checkbox" class="boxutil" name="a" id="a8" value="链路数" checked='checked'/></td>
					<td class="index" style='border-right: solid 0px;'
						title="整个log文件内，所有服务器IP的链路总数、每一个服务器IP的链路数">链路数</td>
					<td style='width:19px;border-left: solid 0px;border-right: solid 0px;'></td>
					<td style='width:73px;border-left: solid 0px;border-right: solid 0px;'></td>
					<td><input type="checkbox" class="boxutil" name="a" id="a11" value="交互流量" checked='checked'/></td>
					<td class="index" style='border-right: solid 0px;'
						title="整个log文件内，所有服务器IP的总流量（包含IP包头）、每一个服务器IP的交互流量">交互流量</td>
					<td style='width:19px;border-left: solid 0px;border-right: solid 0px;'></td>
					<td style='width:73px;border-left: solid 0px;border-right: solid 0px;'></td>
					<td><input type="checkbox" class="boxutil" name="a" id="a12" value="交互时间" checked='checked'/></td>
					<td class="index" style='border-right: solid 0px;'
						title="整个log文件内，所有服务器IP交互时间总和、每一个服务器IP的交互时延">交互时间</td>
						
					<td style='width:19px;border-left: solid 0px;border-right: solid 0px;'></td>
					<td style='width:73px;border-left: solid 0px;border-right: solid 0px;'></td>
				
					<td><input type="checkbox" class="boxutil" name="a" id="a13" value="链路时间有效率" checked='checked'/></td>
					<td class="index" colspan="3" style='border-right: solid 0px;'
						title="无">链路时间有效率</td>
					<td><input type="checkbox" class="boxutil" name="a" id="a14" value="重传识别" checked='checked'/></td>
					<td class="index" style='border-right: solid 0px;'
						title="无">重传识别</td>
					<td style='width:19px;border-left: solid 0px;border-right: solid 0px;'></td>
					<td style='width:73px;border-left: solid 0px;border-right: solid 0px;'></td>
					<td><input type="checkbox" class="boxutil" name="a" id="a15" value="平均链路时间有效率" checked='checked'/></td>
					<td class="index" colspan="3"
						title="无">平均链路时间有效率</td>
				</tr>
				<tr class="tr">
					<td><input type="checkbox" class="boxutil" name="a" id="a16" value="平均RTT时间" checked='checked'/></td>
					<td class="index" style='border-right: solid 0px;'
						title="无">平均RTT时间</td>
					<td style='width:19px;border-left: solid 0px;border-right: solid 0px;'></td>
					<td style='width:73px;border-left: solid 0px;border-right: solid 0px;'></td>
					<td><input type="checkbox" class="boxutil" name="a" id="a17" value="断链时延" checked='checked'/></td>
					<td class="index" style='border-right: solid 0px;'
						title="无">断链时延</td>
						
					<td style='width:19px;border-left: solid 0px;border-right: solid 0px;'></td>
					<td style='width:73px;border-left: solid 0px;border-right: solid 0px;'></td>
					
					<td><input type="checkbox" class="boxutil" name="a" id="a21" value="与上条链路串并行关系" checked='checked'/></td>
					<td class="index" colspan="3"
						title="无">与上条链路串并行关系</td>
					<td><input type="checkbox" class="boxutil" name="a" id="a22" value="与上条链路间隔时延" checked='checked'/></td>
					<td class="index" colspan="3"
						title="无">与上条链路间隔时延</td>
					<td><input type="checkbox" class="boxutil" name="a" id="a23" value="串行链路频繁拆建链判断" checked='checked'/></td>
					<td class="index" colspan="3" title="无">串行链路频繁拆建链判断</td>
					<td><input type="checkbox" class="boxutil" name="a" id="a1" value="其他IP" checked='checked'/></td>
					<td class="index" colspan="3"
						title="整个log文件内含有的除了服务器IP以外的所有IP地址">其他IP</td>
				</tr>
				<tr class="tr">
					<td><input type="checkbox" class="boxutil" name="a" id="a7" value="Time to First Byte" checked='checked'/></td>
					<td class="index" colspan="3"
						title="客户端与服务器TCP建链完成后，传输第一个有效数据包之前，耗费的时间">Time to First Byte</td>
				</tr>
			</table>
		</div><br/>
	   	<a style="text-decoration: none;"><img src="<%=request.getContextPath()%>/design/img/index/indiv.png" width="14" height="14"/>业务个性指标</a>
		<div class="kidney">
			<div class="func" id="func">
				<ul>
					<li class="hit">视频</li>
					<li>即时通讯</li>
					<li>网页</li>
					<li>通话</li>
				</ul>
			</div>
			<div class="pers" id="pers">
				<table>
					<tr class="tr">
						<!-- <td class="td1"><input type="checkbox" class="index" name="aa" value="Server IP" checked='checked'/></td>
						<td class="funcIn">Server IP</td> -->
						<td class="td1"><input type="checkbox" class="index" name="aa" id="aa1" value="其他IP" checked='checked'/></td>
						<td class="funcIn">其他IP</td>
						<td class="td1"><input type="checkbox" class="index" name="aa" id="aa2" value="断链时延" checked='checked'/></td>
						<td class="funcIn">断链时延</td>
						<td class="td1"><input type="checkbox" class="index" name="aa" id="aa3" value="请求类别" checked='checked'/></td>
						<td class="funcIn">请求类别</td>
						<td class="td1"><input type="checkbox" class="index" name="aa" id="aa4" value="链路图" checked='checked'/></td>
						<td class="funcIn">请求类别</td>
					</tr>
				</table>
		  </div>
		</div>
		<a style='text-decoration: none;'>图形展示</a>
		<div class="charts">
			<table>
				<tr class="tr">
					<td><input type="checkbox" class="boxutil" name="a" id="a10" value="IP流量分布图" checked='checked'/></td>
					<td class="index"
						title="整个log文件内，所有服务器IP、其他IP的交互流量分布图">IP流量分布图</td>
					<td><input type="checkbox" class="boxutil" name="a" id="a18" value="吞吐曲线图" checked='checked'/></td>
					<td class="index"
						title="无">吞吐曲线图</td>
					<td><input type="checkbox" class="boxutil" name="a" id="a19" value="包长分布图" checked='checked'/></td>
					<td class="index"
						title="无">包长分布图</td>
					<td><input type="checkbox" class="boxutil" name="a" id="a20" value="链路图" checked='checked'/></td>
					<td class="index"
						title="无">链路图</td>
				</tr>
			</table>
		</div>
		<span style="margin-left: 15px;line-height: 50px;float:left;width: 100%;">
			<input type="button" value="存储为"  onclick="backup()"/>
			<input type="button" value="导入" onclick="checkselect()"/>
			<input type="button" value="全选" onclick="allCheck()"/>	
			<input type="button" value="恢复默认" onclick="def()"/>	
			<input style="float: right;margin-right: 15px;margin-top: 25px" type="button" value="确定" onclick="root()"/>
		</span>
	</div>
	<div id="over" class="over"></div>
	<div id="layout" class="layout"><img src="<%=request.getContextPath() %>/design/img/2013112931.gif" alt="" /><br/>正在解析请稍后</div>
