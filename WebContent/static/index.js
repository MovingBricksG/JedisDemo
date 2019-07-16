var t = 120;
var interval;
function refer() {
	$("#countdown").text("请于" + t + "秒内填写验证码 "); // 显示倒计时
	t--; // 计数器递减
	if (t <= 0) {
		clearInterval(interval);
		$("#countdown").text("验证码已失效，请重新发送！ ");
	}
}

$(function() {
	$("#sendCode").click(function() {
		var number = $.trim($('#number').val());
		if (isEmpty(number)) {
			toaster('输入为空', '', 'error');
		} else {
			isPoneAvailable(number) ? asyncCallAjaxByPost('/check/canSend', {
				"number" : number
			}, function(res) {
				if (res.status === 1) {
					asyncCallAjaxByPost('/send/sendCode', {
						"number" : number
					}, function(res) {
						if (res.status === 1) {
							t = 120;
							clearInterval(interval);
							interval = setInterval("refer()", 1000);// 启动1秒定时
							toaster(res.data, '', 'info');
						} else {
							toaster(res.msg, '', 'info');
						}
					});
				} else {
					clearInterval(interval);
					toaster(res.msg, '', 'info');
				}
			}) : toaster('手机号格式错误', '', 'error');
		}
	});

	$("#verifyCode").click(function() {
		var number = $.trim($('#number').val());
		var code = $.trim($('#code').val());
		if (isEmpty(number)) {
			toaster('手机号输入为空', '', 'error');
			return;
		}
		if (isEmpty(code)) {
			toaster('验证输入为空', '', 'error');
			return;
		}
		isPoneAvailable(number) ? asyncCallAjaxByPost('/check/checkCode', {
			'number' : number,
			'code' : code
		}, function(res) {
			if (res.status === 1) {
				toaster(res.msg, '', 'success');
				clearInterval(interval);
	    		$("#countdown").text("")
			} else {
				toaster(res.msg, '', 'error')
			}
		}) : toaster('手机号格式错误', '', 'error');
	});

	function isPoneAvailable(number) {
		var myreg = /^[1][3,4,5,7,8][0-9]{9}$/;
		if (!myreg.test(number)) {
			return false;
		} else {
			return true;
		}
	}

});