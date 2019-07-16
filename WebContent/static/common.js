var bootpath = getRootPath_web();
var webPath = getWebPath();

//获取到主机地址与项目名称这一级
function getRootPath_web() {
	// 获取当前网址，如： http://localhost:8083/uimcardprj/share/meun.jsp
	var curWwwPath = window.document.location.href;
	// 获取主机地址之后的目录，如： uimcardprj/share/meun.jsp
	var pathName = window.document.location.pathname;
	var pos = curWwwPath.indexOf(pathName);
	// 获取主机地址，如： http://localhost:8083
	var localhostPaht = curWwwPath.substring(0, pos);
	// 获取带"/"的项目名，如：/uimcardprj
	var projectName = pathName.substring(0, pathName.substr(1).indexOf('/') + 1);
	return (localhostPaht + projectName);
}
// 获取主机地址
function getWebPath() {
    // 获取当前网址，如： http://localhost:8083/uimcardprj/share/meun.jsp
    var curWwwPath = window.document.location.href;
    // 获取主机地址之后的目录，如： uimcardprj/share/meun.jsp
    var pathName = window.document.location.pathname;
    var pos = curWwwPath.indexOf(pathName);
    // 获取主机地址，如： http://localhost:8083
    return curWwwPath.substring(0, pos);
}

// ajax异步获取参数（POST）
function asyncCallAjaxByPost(urlLink,data,callBack) {
	var modelData = {};
	var resultData = {};
	if ( null != data && 'undefined' != typeof(data) ) {
		modelData = data;
	}
	$.ajax({
		url : getRootPath_web() + urlLink,
		type : "POST",
		data : modelData,
		dataType : "json",
		cache:false,// false是不缓存，true为缓存
		async:true
	}).done(function(result) {
		if ( !result ) {
			resultData = false;
		} else {
			resultData = result;
		}
		callBack(resultData);
	}).fail(function() {
		resultData = false;
		callBack(resultData);
	});
}

// ajax同步获取参数（GET）
function asyncCallAjaxByGet(urlLink,data,callBack) {
    var modelData = {};
    var resultData = {};
    if ( null != data && 'undefined' != typeof(data) ) {
        modelData = data;
    }
    $.ajax({
        url : webPath + urlLink,
        type : "GET",
        data : modelData,
        dataType : "json",
        cache:false,// false是不缓存，true为缓存
        async:false
    }).done(function(result) {
        if ( !result ) {
            resultData = false;
        } else {
            resultData = result;
        }
        callBack(resultData);
    }).fail(function() {
        resultData = false;
        callBack(resultData);
    });
}

// 校验空值
function isEmpty (data) {
    var dataType = $.type(data);
    var result = true;
    if (data === 0) return false;
    // 对data的类型进行特异性判断
    switch (dataType) {
        case 'object':
            // 如果是{}
            Object.keys(data).length !== 0 ? result = false : '';
            // DOM元素keys为空，多加一层判断
            // if (data instanceof HTMLElement) result = false;
            break;
        case 'array':
            // 如果是[]
            data.length !== 0 && data[0] !== null ? result = false : '';
            break;
        case 'number':
            // 如果是NaN
            result = isNaN(data);
            break;
        case 'string':
            // 判断是否为空字符串或全部为空格
            if (data !== '' && !data.match(/^\s*$/)) {
                result = false;
            }
            break;
        default:
            result = !Boolean(data);
    }
    return result;
}

function isNotEmpty (data) {
    return !isEmpty(data);
}

//提示消息
function toaster (title, body, type, opt) {
    var _option = {
        // append to body
        appendTo: 'body',
        // is stackable?
        stack: false,
        // 'toast-top-left'
        // 'toast-top-right'
        // 'toast-top-center'
        // 'toast-bottom-left'
        // 'toast-bottom-right'
        // 'toast-bottom-center'
        position_class: 'toast-top-center',
        // true = snackbar
        fullscreen: false,
        // width
        width: 100,
        // space between toasts
        spacing: 20,
        // in milliseconds
        timeout: 4000,
        // has close button
        has_close_btn: true,
        // has icon
        has_icon: true,
        // is sticky
        sticky: false,
        // border radius in pixels
        border_radius: 6,
        // has progress bar
        has_progress: true,
        // RTL support
        rtl: false
    };
    opt = opt || {};
    $.extend(_option, opt);
    title = title || '';
    body = body || '';
    if (type !== 'success' && type !== 'error' && type !== 'info' && type !== 'notice' && type !== 'upload')
    {
        type = 'info';
    }
    $.Toast(title, body, type, _option);
}

