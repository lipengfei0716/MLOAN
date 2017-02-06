function findObj(theObj, theDoc) {
            var p, i, foundObj;
            if (!theDoc) theDoc = document;
            if ((p = theObj.indexOf("?")) > 0 && parent.frames.length) {
                theDoc = parent.frames[theObj.substring(p + 1)].document; theObj = theObj.substring(0, p);
            }
            if (!(foundObj = theDoc[theObj]) && theDoc.all)
                foundObj = theDoc.all[theObj]; for (i = 0; !foundObj && i < theDoc.forms.length; i++) foundObj = theDoc.forms[i][theObj]; for (i = 0; !foundObj &&
theDoc.layers && i < theDoc.layers.length; i++) foundObj = findObj(theObj, theDoc.layers[i].document);
            if (!foundObj && document.getElementById)
                foundObj = document.getElementById(theObj); return foundObj;
        }
//添加一个列
count = 1;
function AddNewColumn() {
    var txtTDLastIndex = findObj("txtTDLastIndex", document);
            var columnID = parseInt(txtTDLastIndex.value);
 
            var tab = document.getElementById("tab");
            var rowLength = tab.rows.length;
            var columnLength = tab.rows[1].cells.length;
 
            for (var i = 0; i < rowLength; i++) {
                if (i == 0) {
                    var oTd = tab.rows[0].insertCell(columnLength);
                    oTd.innerHTML = "<div align='center' style='width:40px'><a href='javascript:' onclick=\"DeleteSignColumn(" + (++columnID) + ")\">删除</a></div>";
        } else if (i == 1) {//第一列:序号
            var oTd = tab.rows[1].insertCell(columnLength);
            oTd.innerHTML = "<div style='background: #D3E6FE;width=100%;'>" + (++count) + "</div>";
        } else if (i > 1) {
            var oTd = tab.rows[i].insertCell(columnLength);
            oTd.id = "column" + columnID;
            oTd.innerHTML = "<textarea id=''  rows='4' style='width:150; height:40px;'></textarea>";
                }
            }
 
        }
 
        //添加一个行
var index = 1;
function AddNewRow() {
    var txtTRLastIndex = findObj("txtTRLastIndex", document);
            var rowID = parseInt(txtTRLastIndex.value);
 
            var tab = findObj("tab", document);
            var columnLength = tab.rows[0].cells.length;
 
            //添加行
    var newTR = tab.insertRow(tab.rows.length);
    newTR.id = "SignItem" + rowID;
 
            for (var i = 0; i < columnLength; i++) {
                if (i == 0) {//第一列:序号
            newTR.insertCell(0).innerHTML = ++index;
        } else if (i > 0 && i < 4) {
            newTR.insertCell(i).innerHTML = "<input id='textItem0' type='text' style='border: 0px' size='12' />";
        }
        else if (i >= 4) {
            newTR.insertCell(i).innerHTML = "<textarea id=''  rows='4' style='width:150; height:40px;'></textarea>";
                }
            }
 
            //添加列:删除按钮
    var lastTd = newTR.insertCell(columnLength);
    lastTd.innerHTML = "<div align='center' style='width:40px'><a href='javascript:' onclick=\"DeleteSignRow('SignItem" + rowID + "')\">删除</a></div>";
 
            //将行号推进下一行
            txtTRLastIndex.value = (rowID + 1).toString();
        }
 
        //删除指定行
function DeleteSignRow(rowid) {
    var tab = findObj("tab", document);
            var signItem = findObj(rowid, document);
 
            //获取将要删除的行的Index
            var rowIndex = signItem.rowIndex;
 
            //删除指定Index的行
            tab.deleteRow(rowIndex);
 
 
            //重新排列序号，如果没有序号，这一步省略
            for (i = 2; i < tab.rows.length; i++) {
                tab.rows[i].cells[0].innerHTML = i - 1;
            }
 
            --index
        }
 
        //删除指定列
function DeleteSignColumn(columnId) {
    var tab = document.getElementById("tab");
            var columnLength = tab.rows[1].cells.length;
 
            //删除指定单元格 
            for (var i = 0; i < tab.rows.length; i++) {
                tab.rows[i].deleteCell(columnId);
            }
 
            //重新排列序号，如果没有序号，这一步省略
            var column = columnLength - 4;
 
            for (var j = 1; j < column; j++) {
                tab.rows[1].cells[j + 3].innerHTML = "<div style='background: #D3E6FE;width=100%;'>" + j + "</div>";
            }
 
            --count;
        }
 
 
        //清空列表
function ClearAllSign() {
    //if (confirm('确定要清空所有吗？')) {
    index = 0;
    var tab = findObj("tab", document);
            var rowscount = tab.rows.length;
 
            //循环删除行,从最后一行往前删除
            for (i = rowscount - 1; i > 1; i--) {
                tab.deleteRow(i);
            }
 
            //重置最后行号为1
    var txtTRLastIndex = findObj("txtTRLastIndex", document);
    txtTRLastIndex.value = "1";
 
            //预添加一行
    AddNewRow();
    //}
}
//------------------table分页-------------------------
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