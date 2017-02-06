	function ajaxTest(){  
	$.ajax({  
	    data:"name="+$("#name").val(),  
	    type:"GET",  
	    dataType: 'json',  
	    url:"resolve/test.do",  
	    //contentType: "application/json; charset=utf-8",
	    cache: false,
	    async: false,
	    success:function(data){  
	    	var html = "";
            $.each(data,function (index, item) { 
                //循环获取数据    
                html+="<br/>" + item.no + "----" + item.goAddress;  
            });  
            $("#result").html(html);
	    }, 
	    error:function(data){
	    	alert("aa");
	        alert("出错了！！:"+data.msg);  
	        $("#result").html(data.msg) ; 
	    }
	});  
}  
function ajaxserver(){
    //解决中文乱码问题的方法 1，页面端发出的数据做一次encodeURI，服务器端使用 new String(old.getBytes("iso8859-1"),"utf-8")
    //var url= "AJAXServer?name="+encodeURI($("#userName").val() ) ; // encodeURI处理中文乱码问题
   // 解决中文乱码问题的方法 2.页面端发出的数据做两次encodeURI处理， 服务器端用URLDecoder.decode(old,"utf-8");
    var url= "resolve/ajaxget.do?name="+encodeURI(encodeURI($("#userName").val() )); // encodeURI处理中文乱码问题
    url=convertURL(url);//获取函数的返回值'login?uname='+ uname + '&psw=' + psw
          $.get(url,null,function(data){
              $("#result").html(data); //简洁版
          });
    System.out.println(url);
}
//给URL增加时间戳，骗过浏览器，不读取缓存
function convertURL(url){
    //获取时间戳
    var timstamp=(new Date()).valueOf();
    //将时间戳信息拼接到URL上
    if(url.indexOf("?")>=0){//用indexof判断该URL地址是否有问号
    url=url+"&t="+timstamp;
    }else{
       url=url+"?t="+timstamp;  
    }
    return  url;
}