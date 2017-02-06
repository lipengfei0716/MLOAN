/*****************  init  *************************/		
$(document).ready(function(){

	var index = localStorage.getItem("index");
	if(index=="1")
		MainTop(eval(1));
	else if(index=="3")
		MainTop(eval(3));
	else
		MainTop(eval(2));
});
/*****************  file input  *************************/
function addFile() {
	var upFile = '<input type="file" name="file1"><br>';
	document.getElementById("files").insertAdjacentHTML("beforeEnd", upFile);}
function deleteFile() {
	var file = document.getElementById("files").lastChild;
	if (file == null)
		return;
	document.getElementById("files").removeChild(file);
	file = document.getElementById("files").lastChild; // 移除换行符<br>所以要移两次
	document.getElementById("files").removeChild(file); // 如果在表格里面不加<br>就自动换行的，可以去掉，自己把握
}
/*****************  Event  *************************/
function MainTop(index){
	switch (index) {
	case 2:
		localStorage.setItem("index","2");
		document.getElementById('label_Left'+(index)).style.background='#44acdb';
		document.getElementById('label_Left'+(index-1)).style.background='#0089c7';
		document.getElementById('label_Left'+(index+1)).style.background='#0089c7';
		document.getElementById('home_maintop_div'+(index-1)).style.display='none';
		document.getElementById('home_maintop_div'+(index)).style.display='';
		document.getElementById('home_maintop_div'+(index+1)).style.display='none';
		document.getElementById('home_form'+(index-1)).style.display='none';
		document.getElementById('home_form'+(index)).style.display='';
		document.getElementById('home_form'+(index+1)).style.display='none';break;
	case 3:
		localStorage.setItem("index","3");
		document.getElementById('label_Left'+(index)).style.background='#44acdb';
		document.getElementById('label_Left'+(index-2)).style.background='#0089c7';
		document.getElementById('label_Left'+(index-1)).style.background='#0089c7';
		document.getElementById('home_maintop_div'+(index-2)).style.display='none';
		document.getElementById('home_maintop_div'+(index-1)).style.display='none';
		document.getElementById('home_maintop_div'+(index)).style.display='';
		document.getElementById('home_form'+(index-2)).style.display='none';
		document.getElementById('home_form'+(index-1)).style.display='none';
		document.getElementById('home_form'+(index)).style.display='';
		normIpRefresh();break;
	default:
		localStorage.setItem("index","1");
		document.getElementById('label_Left'+(index)).style.background='#44acdb';
		document.getElementById('label_Left'+(index+1)).style.background='#0089c7';
		document.getElementById('label_Left'+(index+2)).style.background='#0089c7';
		document.getElementById('home_maintop_div'+(index)).style.display='';
		document.getElementById('home_maintop_div'+(index+1)).style.display='none';
		document.getElementById('home_maintop_div'+(index+2)).style.display='none';
		document.getElementById('home_form'+(index)).style.display='';
		document.getElementById('home_form'+(index+1)).style.display='none';
		document.getElementById('home_form'+(index+2)).style.display='none';break;}}
$('#home_maintop_div1 ul li').click(function(){
	$(this).addClass('hit').siblings().removeClass('hit');
	$('#home_form1>div:eq('+$(this).index()+')').show().siblings().hide();})
$('#home_maintop_div2 ul li').click(function(){
	$(this).addClass('hit').siblings().removeClass('hit');
	$('#home_form2>div:eq('+$(this).index()+')').show().siblings().hide();})
$('#indicator').click(function(){    //  一键分析   跳转至  指标选取
	document.getElementById("form2_li1").className = "";
	document.getElementById("form2_li2").className = "hit";
	$('#home_form2>div:eq(1)').show().siblings().hide();})
	
$('#home_maintop_div3 ul li').click(function(){
	$(this).addClass('hit').siblings().removeClass('hit');
	$('#home_form3>div:eq('+$(this).index()+')').show().siblings().hide();
	normIpRefresh();})	
$("#home_form2").show(function(){
	document.getElementById('normid').style.display='none';})
function show(){
	document.getElementById("showDiv").style.display="";
	}
function hidden1(){
	document.getElementById("showDiv").style.display="none";
	}