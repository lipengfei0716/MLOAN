								/**自定义按钮*/
function radio(){
	$.fn.rdo = function() {	//单选
		return $(this).each( function(k,v) {
			var $this = $(v);
			if( $this.is(':radio') && !$this.data('radio-replaced') ) {
				// add some data to this checkbox so we can avoid re-replacing it.
				$this.data('radio-replaced', true);
				// create HTML for the new checkbox.
				var $l = $('<label for="'+$this.attr('id')+'" class="radio"></label>');
				var $p = $('<span class="pip"></span>');
				// insert the HTML in before the checkbox.
				$l.append( $p ).insertBefore( $this );
				$this.addClass('replaced');
				// check if the radio is checked, apply styling. trigger focus.
				$this.on('change', function() { $('label.radio').each( function(k,v) {var $v = $(v);if( $('#'+ $v.attr('for') ).is(':checked') ) {$v.addClass('on');  } else {$v.removeClass('on'); }});$this.trigger('focus');});		
				$this.on('focus', function() { $l.addClass('focus') });
				$this.on('blur', function() { $l.removeClass('focus') });		
				// check if the radio is checked on init.
				$('label.radio').each( function(k,v) { var $v = $(v);if( $('#'+ $v.attr('for') ).is(':checked') ) {$v.addClass('on'); } else { $v.removeClass('on');}});}});}; 
	$(':radio').rdo();}
function box(){
	$.fn.chkbox = function() { //复选框
		return $(this).each( function(k,v) {var $this = $(v);if( $this.is(':checkbox')) {
				// create HTML for the new checkbox.
				var $l = $('<label for="'+$this.attr('id')+'"></label>');
				// insert the HTML in before the checkbox.
				$this = insertAfter($this, $l);$this.addClass('boxUtil');}});};
	$(':checkbox').chkbox();}
function insertAfter(a,b){b.insertBefore( a );a.insertBefore( b ); return a;}
							/**cookie操作*/
function addCookie(key, value, timeout) {// 添加cookie
	
	var str = key + "=" + escape(value);
	if (timeout > 0) {// 为0时不设定过期时间，浏览器关闭时cookie自动消失
		var date = new Date();
		var ms = timeout * 60 * 1000;  //分钟 
		date.setTime(date.getTime() + ms);
		str += "; expires=" + date.toGMTString();
	}
	document.cookie = str;
} 
function getCookie(cookieKey){// 获取指定名称的cookie的值
    var cookies = document.cookie.split("; "); 
    for (var i = 0; i < cookies.length; i++) { 
        var cookie = cookies[i].split("="); 
        if (cookie[0] == cookieKey) 
            return unescape(cookie[1]); 
    } 
} 
function delCookie(name){//为了删除指定名称的cookie，可以将其过期时间设定为一个过去的时间 
    var date = new Date(); 
    date.setTime(date.getTime() - 10000); 
    document.cookie = name + "=a; expires=" + date.toGMTString(); 
} 
function allCookie(){//读取所有保存的cookie字符串 
    var str = document.cookie; 
    if (str == "") { 
        str = "没有保存任何cookie"; 
    } 
    alert(str); 
} 


