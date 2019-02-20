<%-- <%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">

<title>My JSP 'result.jsp' starting page</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

</head>

<body>
	<table border="1">
		<tr>
			<td colspan="5" align="center">欧米单车锁🔐接口列表</td>
		</tr>
		<tr>
			<td>接口号</td>
			<td>业务描述</td>
			<td>测试接口</td>
			<td>请求参数</br>（所有接口必填：requestType：业务编号;token：请求验证信息，自动登录（在登录成功后返回，获取到后保存到本地，相当于登录凭证，登录注册不用传））;
				pageNo:分页页码（选填）
			</td>
			<td>返回参数，data：数据结果 </br>code(状态码)101:TOKEN错误
				102:TOKEN已过期（过期时需重新登录）103:设备类型错误
				104：异地登陆（需强制退出）;105:TOKEN待刷新（需调用20026刷新token）;20005：账户被冻结；200:正常；
				201:缺少参数;202:数据不存在;203:数据已存在;204：短信验证码无效或已过期；205:短信单日请求最大数(提示：您今日的短信次数已用完);
				206:请求频繁异常（提示：您的请求过于频繁，请稍后再试。）;500:服务器异常
			</td>
		</tr>

		<tr>
			<td colspan="5" align="center"><font color="red">登录注册</font>(IOS传入：deviceType[1：Android
				2：IOS]：设备类型 deviceToken:推送密钥)；deviceUUID：应用在设备中的UUID</td>
		</tr>
		<tr>
			<td>10001</td>
			<td>账号加密码登录</td>
			<td><a
				href="<%=basePath%>app/login?industryType=1&requestType=10001&phone=13410996848&password=123456">
					<%=basePath%>app/login?industryType=1&requestType=10001&phone=13410996848&password=123456
			</a></td>
			<td>phone:手机号或邮箱；password：密码;</td>
			<td>202:账号和密码错误 登录成功：用户信息实体</td>
		</tr>
		<tr>
			<td>10002</td>
			<td>手机号直接登录（自动注册）</td>
			<td><a
				href="<%=basePath%>app/login?industryType=1&requestType=10002&phone=13410996848&code=123456">
					<%=basePath%>app/login?industryType=1&requestType=10002&phone=13410996848&code=123456
			</a></td>
			<td>phone:手机号或邮箱；thirdId：第三方注册返回的ID（选填）；</td>
			<td>isRegister：1：新注册用户 （不返回为正常登录） 登录成功：同上</td>
		</tr>

		<tr>
			<td>10003</td>
			<td>第三方平台用户登录（自动注册）</td>
			<td><a
				href="<%=basePath%>app/login?industryType=1&requestType=10003&uuid=13410996848&type=1">
					<%=basePath%>app/login?industryType=1&requestType=10003&uuid=13410996848&type=1
			</a></td>
			<td>uuid:第三方平台返回的用户唯一ID；type：1:微信 2:QQ 3:Facebook 4:Instagram
				5:Twitter 6:Google;7:小程序</td>
			<td>isRegister：1：新注册用户返回thirdId，用于手机验证时提交绑定；登录成功：同上</td>
		</tr>

		<tr>
			<td>10004</td>
			<td>手机号直接登录（不注册，暂用于密码重置验证）</td>
			<td><a
				href="<%=basePath%>app/login?industryType=1&requestType=10004&phone=13410996848&code=123456">
					<%=basePath%>app/login?industryType=1&requestType=10004&phone=13410996848&code=123456
			</a></td>
			<td>phone:手机号或邮箱；code：验证码</td>
			<td>data:token信息，用于重置密码验证</td>
		</tr>

		<tr>
			<td colspan="5" align="center"><font color="red">用户资料</font></td>
		</tr>
		<tr>
			<td>20001</td>
			<td>获取个人中心用户信息</td>
			<td><a href="<%=basePath%>app/user?requestType=20001&token=1">
					<%=basePath%>app/user?requestType=20001&token=1
			</a></td>
			<td>无附加参数</td>
			<td>userInfo:用户资料 useInfo：当日骑行信息(acitiveDay:活跃天数);member:1
				-会员标记（不是会员无此字段）</td>
		</tr>
		<tr>
			<td>20008</td>
			<td>获取用户详细信息</td>
			<td><a href="<%=basePath%>app/user?requestType=20008&token=1">
					<%=basePath%>app/user?requestType=20008&token=1
			</a></td>
			<td>无附加参数</td>
			<td>userInfo:用户资料（emailAuth：邮箱验证，不可重复验证，修改邮箱后可再次验证。）
				cardList：银行卡列表</td>
		</tr>
		<tr>
			<td>20002</td>
			<td>修改用户基础资料</td>
			<td><a href="<%=basePath%>app/user?requestType=20002&token=1">
					<%=basePath%>app/user?requestType=20002&token=1
			</a></td>
			<td>选填参数（phone:手机号 nickName:昵称 headUrl：头像地址 inviteCode:好友邀请码;）</td>
			<td>1：0 成功：失败</td>
		</tr>
		<tr>
			<td>20003</td>
			<td>修改密码</td>
			<td><a
				href="<%=basePath%>app/user?requestType=20003&token=1&oldPassword=11&newPassword=22">
					<%=basePath%>app/user?requestType=20003&token=1&oldPassword=11&newPassword=22>
			</td>
			<td>oldPassword:原密码 newPassword:新密码</td>
			<td>1：0 成功：失败</td>
		</tr>
		<tr>
			<td>20004</td>
			<td>手机认证（暂提供第三方注册使用，手机号注册已自动完成认证）<font color="red">[第一步]，失败请考虑顺序问题</font></td>
			<td><a
				href="<%=basePath%>app/user?requestType=20004&token=1&phone=12345&code=1234">
					<%=basePath%>app/user?requestType=20004&token=1&phone=12345&code=1234
			</a></td>
			<td>phone：手机号 code:短信验证码</td>
			<td>1：0 成功：失败</td>
		</tr>
		<tr>
			<td>20005</td>
			<td>实名认证/修改用户详细信息<font color="red">[第二步]，失败请考虑顺序问题</font></td>
			<td><a
				href="<%=basePath%>app/user?requestType=20005&token=1&firstName=Alina&lastName=Candy&email=alina.candy@hotmail.com
				&password=123456&address=ttt&zipCode=2323&counTry=china&type=1">
					<%=basePath%>app/user?requestType=20005&token=1&firstName=Alina&lastName=Candy&email=alina.candy@hotmail.com
					&password=123456&address=ttt&zipCode=2323&counTry=china&type=1
			</a></td>
			<td>firstName：对应sunname type: 添加类型 1：实名认证 2：资料修改
				"lastName","email","password","address","zipCode","counTry"，inviteCode:邀请码
				,（选填:idcard:身份证;cityId：城市ID;birthday:生日；gender:性别【1:男 2：女】）</td>
			<td>资料修改： data:（1：0 成功：失败） 实名认证：result:（1：0
				成功：失败）deposit:押金额度;token:接口令牌（修改城市后返回）;code:20005,邮箱被占用</td>
		</tr>
		<tr>
			<td>20006</td>
			<td>绑定银行卡<font color="red">[第四步]，失败请考虑顺序问题</font></td>
			<td><a
				href="<%=basePath%>app/user?requestType=20006&token=1&card_number=12345&exp_date=1234&cvv=11&name_on_card=tt&type=1">
					<%=basePath%>app/user?requestType=20006&token=1&card_number=12345&exp_date=1234&cvv=11&name_on_card=tt&type=1
			</a></td>
			<td>type 添加类型 1：绑定(注册) 2：添加（个人资料） 其他对照设计图即可</td>
			<td>1：0 成功：失败</td>
		</tr>
		<tr>
			<td>20007</td>
			<td>设置好友邀请码</td>
			<td><a
				href="<%=basePath%>app/user?requestType=20007&token=1&code=t222">
					<%=basePath%>app/user?requestType=20007&token=1&code=t222
			</a></td>
			<td>code:邀请码</td>
			<td>1：0 成功：失败</td>
		</tr>
		<tr>
			<td>20009</td>
			<td>获取用户优惠券列表</td>
			<td><a href="<%=basePath%>app/user?requestType=20009&token=1">
					<%=basePath%>app/user?requestType=20009&token=1
			</a></td>
			<td><font color="red">pageNo:页码</font>showAll:是否显示所有
				0/1,需要激活的列表传入1</td>
			<td>name：优惠券名称；value：优惠值；type：优惠类型 1：折扣
				2：抵扣；start_time：优惠开始时间；end_time:结束时间</td>
		</tr>
		<tr>
			<td>20010</td>
			<td>删除银行卡</td>
			<td><a
				href="<%=basePath%>app/user?requestType=20010&token=1&cardId=1">
					<%=basePath%>app/user?requestType=20010&token=1&cardId=1
			</a></td>
			<td>cardId:卡号</td>
			<td>1：0 成功：失败</td>
		</tr>
		<tr>
			<td>20011</td>
			<td>退还押金</td>
			<td><a href="<%=basePath%>app/user?requestType=20011&token=1">
					<%=basePath%>app/user?requestType=20011&token=1
			</a></td>
			<td></td>
			<td>0:申请失败 1：申请成功 2：未缴付押金 3：已在申请中;dealDay:处理时长（在申请成功后返回，例如5-8天）</td>
		</tr>

		<tr>
			<td>20012</td>
			<td>激活优惠券</td>
			<td><a
				href="<%=basePath%>app/user?requestType=20012&token=1&couponCode=323kk32">
					<%=basePath%>app/user?requestType=20012&token=1&couponCode=323kk32
			</a></td>
			<td>couponCode/couponId 二选一</td>
			<td>0:激活失败 1：激活成功 20001:优惠券已过期 20002:优惠券已被使用</td>
		</tr>

		<tr>
			<td>20013</td>
			<td>获取我的红包</td>
			<td><a href="<%=basePath%>app/user?requestType=20013&token=1">
					<%=basePath%>app/user?requestType=20013&token=1
			</a></td>
			<td></td>
			<td>amount：当前红包总额;minAmount:最少提现额度；cashCount：今日提现次数</td>
		</tr>
		<tr>
			<td>20014</td>
			<td>红包提现</td>
			<td><a
				href="<%=basePath%>app/user?requestType=20014&token=1&amount=0.01&type=2&account=13410996848">
					<%=basePath%>app/user?requestType=20014&token=1&amount=0.01&type=2&account=1341099684
			</a></td>
			<td>amount：提现金额,type： 1(微信) 2（支付宝）,account：收款账号（支付宝:登陆账号
				微信：暂未确定）</td>
			<td>20003：红包余额不足；cashId：提现记录Id;orderId:第三方提现订单ID</td>
		</tr>
		<tr>
			<td>20015</td>
			<td>提现进度查询</td>
			<td><a
				href="<%=basePath%>app/user?requestType=20015&token=1&cashId=1&orderId=1">
					<%=basePath%>app/user?requestType=20015&token=1&cashId=1&orderId=1
			</a></td>
			<td>cashId：提现记录Id;orderId:第三方提现订单ID</td>
			<td>status： 0：申请中 1:成功 2:失败 3：审核中 4：退票 5：未知 </br> info：错误信息
			</td>
		</tr>
		<tr>
			<td>20016</td>
			<td>获取所有订单</td>
			<td><a href="<%=basePath%>app/user?requestType=20016&token=1">
					<%=basePath%>app/user?requestType=20016&token=1
			</a></td>
			<td><font color="red">pageNo:页码</font>tradeType:订单类型 1：充值或余额退款
				0：全部</td>
			<td>status：0：待支付 1：支付成功 2:交易关闭 3;已部分退款 4:已全额退款;way(wayStr):支付方式
				1:账户余额 2：微信 3：支付宝 4:PayPay 5:VISA 6:Strip 7:长租免费 8:优惠券 9:红包
				type(typeStr):支付类型 1：消费 2：充值 3:押金 4：长租</td>
		</tr>
		<tr>
			<td>20017</td>
			<td>分享成功</td>
			<td><a
				href="<%=basePath%>app/user?requestType=20017&token=1&type=1"> <%=basePath%>app/user?requestType=20017&token=1&type=1
			</a></td>
			<td>type:分享类型 1：微信 2:QQ 3:微博 4:Facebook 5：twitter</td>
			<td></td>
		</tr>
		<tr>
			<td>20018</td>
			<td>获取信用积分列表</td>
			<td><a href="<%=basePath%>app/user?requestType=20018&token=1">
					<%=basePath%>app/user?requestType=20018&token=1
			</a></td>
			<td><font color="red">pageNo:页码</font></td>
			<td>rule_type:1:加分 2:减分 3:设为0；ule_name：规则名称；count：数量；date:日期</td>
		</tr>
		<tr>
			<td>20019</td>
			<td>获取用户钱包信息</td>
			<td><a href="<%=basePath%>app/user?requestType=20019&token=1">
					<%=basePath%>app/user?requestType=20019&token=1
			</a></td>
			<td></td>
			<td>amount:余额；deposit:已交押金金额；member：会员信息;cashVo:余额提现信息（余额退款）;</td>
		</tr>
		<tr>
			<td>20020</td>
			<td>用户红包列表获取</td>
			<td><a href="<%=basePath%>app/user?requestType=20020&token=1">
					<%=basePath%>app/user?requestType=20020&token=1
			</a></td>
			<td><font color="red">pageNo:页码</font></td>
			<td></td>
		</tr>
		<tr>
			<td>20021</td>
			<td>获取最新版本（Android）</td>
			<td><a
				href="<%=basePath%>app/user?requestType=20021&token=1&versionCode=1">
					<%=basePath%>app/user?requestType=20021&token=1&versionCode=1
			</a></td>
			<td>versionCode:版本号</td>
			<td>version_name：版本名称；version_code：版本号；content：更新内容；url：文件路径</td>
		</tr>

		<tr>
			<td>20022</td>
			<td>重置密码</td>
			<td><a
				href="<%=basePath%>app/user?requestType=20022&token=1&password=123456">
					<%=basePath%>app/user?requestType=20022&token=1&password=123456
			</a></td>
			<td>password:新密码</td>
			<td></td>
		</tr>
		<tr>
			<td>20023</td>
			<td>修改手机号</td>
			<td><a
				href="<%=basePath%>app/user?requestType=20023&token=1&phone=12345&code=1234">
					<%=basePath%>app/user?requestType=20023&token=1&phone=12345&code=1234
			</a></td>
			<td>phone：手机号 code:短信验证码;password:当前密码（选填）</td>
			<td>1：0 成功：失败</td>
		</tr>
		<tr>
			<td>20024</td>
			<td>通过代码添加优惠券（不激活）</td>
			<td><a
				href="<%=basePath%>app/user?requestType=20024&token=1&couponCode=1234">
					<%=basePath%>app/user?requestType=20024&token=1&couponCode=1234
			</a></td>
			<td>couponCode</td>
			<td>1：0 成功：失败</td>
		</tr>
		<tr>
			<td>20025</td>
			<td>余额退款</td>
			<td><a
				href="<%=basePath%>app/user?requestType=20025&token=1&amount=1234">
					<%=basePath%>app/user?requestType=20025&token=1&amount=1234
			</a></td>
			<td>amount:提现金额</td>
			<td>1：0 成功：失败；20006：余额不足</td>
		</tr>
		<tr>
			<td>20026</td>
			<td>刷新token</td>
			<td><a href="<%=basePath%>app/user?requestType=20026&token=1">
					<%=basePath%>app/user?requestType=20026&token=1
			</a></td>
			<td></td>
			<td>token</td>
		</tr>
		<tr>
			<td>20028</td>
			<td>发送邮箱验证链接</td>
			<td><a
				href="<%=basePath%>app/user?requestType=20028&industryId=2&email=11">
					<%=basePath%>app/user?requestType=20028&industryId=2&email=11
			</a></td>
			<td>email:邮箱</td>
			<td>data 0:1 成功/失败</td>
		</tr>
		<tr>
			<td colspan="5" align="center"><font color="red">单车</font> </br> 返回结果
				30001：单车正在使用中； 30002：单车已损坏； 30003：单车已报废 ；30004：单车已被预约； 30005：单车解锁成功
				； 30006：已有预约的单车
				;30007:用户未通过认证;30008:用户余额不足;30009:用户有未支付的订单;30010：单车正在解锁；30011:已在使用其他单车;30012:骑行越界;30013：未绑定银行卡
				30014:骑行已结束；30015：禁停区停放;30016:取消预约次数已上限，不能预约；30017:解锁次数已上限;30018：单车未连接；30019:单车未激活；20004:用户被暂停使用单车</td>
		</tr>
		<tr>
			<td>30001</td>
			<td>获取附近单车</td>
			<td><a
				href="<%=basePath%>app/bike?industryType=1&requestType=30001&lat=32.22&lng=32.45">
					<%=basePath%>app/bike?industryType=1&requestType=30001&lat=32.22&lng=32.45
			</a></td>
			<td>lat，lng 目标位置经纬度；cur_lat,cur_lng 当期位置经纬度</td>
			<td>number：单车编号；type_id：类型ID；price：价格；g_lat，g_lng：经纬度；readpack：是否为红包单车（0:1）;
				<font color="red">【New】bikeType:1 单车 2滑板车;</font>
				redpackRuleVo:（红包规则实体）：id:规则ID；type:1:现金（max_amount：最大金额；free_use_time：免费使用时长；least_use_time：最低使用时长）；
				2：优惠券（coupon_num:优惠券数量；must_in_area:是否必须停发在指定区域才可获取优惠券；area_ids:区域ids[在点击单车详情后请求区域列表30014，传入ids]）
			</td>
		</tr>
		<tr>
			<td>30002</td>
			<td>预约单车</td>
			<td><a
				href="<%=basePath%>app/bike?requestType=30002&token=1&bikeId=1">
					<%=basePath%>app/bike?requestType=30002&token=1&bikeId=1
			</a></td>
			<td>bikeId：单车ID</td>
			<td>1：0 成功：失败 ；
				timeOut:过期时长;maxCancelCount:当日最大取消预约次数；leftCount：剩余取消预约次数；</td>
		</tr>

		<tr>
			<td>30003</td>
			<td>取消预约单车</td>
			<td><a href="<%=basePath%>app/bike?requestType=30003&token=1">
					<%=basePath%>app/bike?requestType=30003&token=1
			</a></td>
			<td></td>
			<td>1：0 成功：失败；</td>
		</tr>

		<tr>
			<td>30004(get)</td>
			<td>开获取开锁进度<font color="red">(需根据此接口判定开锁是否成功，可2秒请求一次)</font></td>
			<td><a
				href="<%=basePath%>app/bike?requestType=30004&token=1&bikeNumber=66755000210">
					<%=basePath%>app/bike?requestType=30004&token=1&bikeNumber=66755000210
			</a></td>
			<td>bikeNumber：单车编号;inputNumber:是否为输入编号，手动解锁（只能通过蓝牙解锁）</td>
			<td>202： 请求不存在或开锁超时； 30005： 单车解锁成功;30010：正在解锁;</td>
		</tr>

		<tr>
			<td>30004（post）</td>
			<td>开始使用单车<font color="red">(需根据此接口判定开锁是否成功，可2秒请求一次)</font></td>
			<td><a
				href="<%=basePath%>app/bike?requestType=30004&token=1&bikeNumber=66755000210&startLat=32.22&startLng=32.45">
					<%=basePath%>app/bike?requestType=30004&token=1&bikeNumber=66755000210&startLat=32.22&startLng=32.45
			</a></td>
			<td>bikeNumber：单车编号 ；startLat，startLng 起点经纬度;moreRide:1 多骑行
				；rideUser:骑行中名称</td>
			<td>1：0 请求 成功：请求 失败 30005：
				单车解锁成功;(【蓝牙版】mac：蓝牙地址；date：骑行时间戳);redpackRule:红包信息;<font color="red">【New】bikeType:1 单车 2滑板车;</font></td>
		</tr>

		<tr>
			<td>30005</td>
			<td>更新单车骑行路径<font color="red">(需根据此接口判定骑行是否完成，可5秒请求一次)</font></td>
			<td><a
				href="<%=basePath%>app/bike?requestType=30005&token=1&bikeNumber=66755000210&lat=32.22&lng=32.45">
					<%=basePath%>app/bike?requestType=30005&token=1&bikeNumber=66755000210&lat=32.22&lng=32.45
			</a></td>
			<td>bikeNumber：单车编号 ；lat，lng 当前经纬度;lats,lngs
				历史运动经纬度，APP后台运行时保存，使用完成后需清楚。</td>
			<td>type: 1：骑行信息（骑行中） 2：订单信息（骑行完成）</br>
				1.骑行信息：number:单车编号；startTime：开始时间；endTime：结束时间；startLat，startLng：开始经纬度；endLat，endLng：结束经纬度
				；orbit:运动路径;distance：骑行距离（km）；duration:骑行时长(min);calorie:消耗卡里路;carbon:节约碳排量;out_area:0:正常:1骑行超出边界2：在禁停区</br>
				单车信息： number:单车编号；price:单价</br> 2.tradeVo（订单信息）： amount：消费金额；status：支付状态；</br>
				bikeUseVo（骑行信息）：
				number:单车编号；startTime：开始时间；endTime：结束时间；startLat，startLng：开始经纬度；endLat，endLng：结束经纬度
				；orbit:运动路径;distance：骑行距离（km）；duration:骑行时长(min);calorie:消耗卡里路;carbon:节约碳排量;out_area:0:正常:1骑行超出边界2：在禁停区</br>
				leaseVo（长租优惠）：，长租开始时间：start_time；长租结束时间：end_time;</br>
				couponVo（优惠券优惠），name：优惠券名称；value：优惠值；type：优惠类型 1：折扣
				2：抵扣；start_time：优惠开始时间；end_time:结束时间</br> 以上两种优惠只会出现一种
				redpackVo:红包信息，amount：红包金额</br>

			</td>
		</tr>

		<tr>
			<td>30006</td>
			<td>获取当前单车的使用信息<font color="red">（每次进入应用请求）</font></td>
			<td><a href="<%=basePath%>app/bike?requestType=30006&token=1">
					<%=basePath%>app/bike?requestType=30006&token=1
			</a></td>
			<td>versionCode：当前版本号（Android）</td>
			<td>type: 1：预约信息 2：骑行信息（骑行中） 3：订单信息（骑行完成）</br> 1.预约信息：date：预约时间 </br>
				maxRideCount:最大骑行数； bikeVo： 单车信息 gLat，gLng：单车经纬度 status:状态 number：编号
				useStatus:1-使用，3-正在解锁(正在解锁需进行提示);2,3同 接口 30005</br>
				newsVo:活动资讯；id:活动ID（根据ID更新本地信息）;title：活动标题;start_time:开始时间；
				end_time:结束时间（根据开始结束时间判断是否显示，不用显示的缓存到本地，下次直接从本地拿，提高效率）
				imageVo:活动图片（path:路径；height:高度；width:宽度）
				topVersion:最新版本信息（Android）；version_name：版本名称；version_code：版本号；content：更新内容；url：文件路径；
				messageCount：信箱未读消息数
				;bindCard:是否绑定银行卡;cityVo:区域边界信息;balance：余额；balancePay:可用余额支付（适用于待支付订单，当为1是自动调起40003接口并进行loading提示；当为0时，提示充值/加银行卡）</br>
			</td>
		</tr>
		<tr>
			<td>30007</td>
			<td>获取单车类型</td>
			<td><a href="<%=basePath%>app/bike?requestType=30007&token=1">
					<%=basePath%>app/bike?requestType=30007&token=1
			</a>unit_type 单位 1：分钟 2：小时 3：月份</td>
			<td>无附加参数</td>
			<td></td>
		</tr>
		<tr>
			<td>30008</td>
			<td>获取行程集合</td>
			<td><a href="<%=basePath%>app/bike?requestType=30008&token=1">
					<%=basePath%>app/bike?requestType=30008&token=1
			</a></td>
			<td><font color="red">pageNo:页码</font></td>
			<td></td>
		</tr>
		<tr>
			<td>30009</td>
			<td>开收据</td>
			<td><a href="<%=basePath%>app/bike?requestType=30009&token=1">
					<%=basePath%>app/bike?requestType=30009&token=1
			</a></td>
			<td>trade_ids：骑行订单ID（逗号，分割）；"firstname"：surname,"lastname","phone","address","zipCode","country"</td>
			<td>1：0 成功：失败；</td>
		</tr>
		<!--  
		<tr>
			<td>30010</td>
			<td>修改订单状态为已通知<font color="red">每次收到已完成订单时调用</font></td>
			<td><a href="<%=basePath%>app/bike?requestType=30010&token=1">
					<%=basePath%>app/bike?requestType=30010&token=1
			</a></td>
			<td></td>
			<td>1：0 成功：失败；</td>
		</tr>
		-->
		<tr>
			<td>30011</td>
			<td>报告单车信息-不能开锁</td>
			<td><a
				href="<%=basePath%>app/bike?requestType=30011&token=1&number=1">
					<%=basePath%>app/bike?requestType=30011&token=1&number=1
			</a></td>
			<td>number：单车编号；选填:content:内容</td>
			<td>1：0 成功：失败；</td>
		</tr>
		<tr>
			<td>30012</td>
			<td>报告单车信息-故障</td>
			<td><a
				href="<%=basePath%>app/bike?requestType=30012&token=1&number=1&errorType=1">
					<%=basePath%>app/bike?requestType=30012&token=1&number=1&errorType=1
			</a></td>
			<td>number：单车编号；errorType：故障类型（从1开始，分别代表： "QR Code
				missing","Handle grips missing","Frame" ,"Bell
				missing","Transmission","Tyre/Wheel","Broken
				bke","Pedal","Light",Lock,Seat,Brake）；选填:imageUrls：图片URL数组(imageWidths,imageHeights)
				,content:内容</td>
			<td>1：0 成功：失败；</td>
		</tr>
		<tr>
			<td>30013</td>
			<td>报告单车信息-违章</td>
			<td><a
				href="<%=basePath%>app/bike?requestType=30013&token=1&number=1">
					<%=basePath%>app/bike?requestType=30013&token=1&number=1
			</a></td>
			<td>number：单车编号；选填:imageUrls：图片URL数组(imageWidths,imageHeights),content:内容</td>
			<td>1：0 成功：失败；</td>
		</tr>
		<tr>
			<td>30014</td>
			<td>获取停车区域</td>
			<td><a
				href="<%=basePath%>app/bike?requestType=30014&token=1&lat=1&lng=1">
					<%=basePath%>app/bike?requestType=30014&token=1&lat=1&lng=1
			</a></td>
			<td>lat,lng：当前经纬度;ids:id集合</td>
			<td>name:名称 ； detail：点集合；type:区域类型 1：停车区域 2：禁停区域 3:强制停车区域</td>
		</tr>
		<tr>
			<td>30015</td>
			<td>骑行订单详情</td>
			<td><a
				href="<%=basePath%>app/bike?requestType=30015&token=1&id=1"> <%=basePath%>app/bike?requestType=30015&token=1&id=1
			</a></td>
			<td>id：订单ID</td>
			<td>同30005订单信息</td>
		</tr>

		<tr>
			<td>30016</td>
			<td>报告单车信息</td>
			<td><a
				href="<%=basePath%>app/bike?requestType=30013&token=1&number=1">
					<%=basePath%>app/bike?requestType=30013&token=1&number=1
			</a></td>
			<td>number：单车编号；type:1.不能开锁 2:故障 3：违章 4 (关锁未结费) 5.忘记关锁 6:被偷的车
				7:被破坏的车 8：无人认领的车 9：其他问题 10：消费问题
				选填:imageUrls：图片URL数组(imageWidths,imageHeights),content:内容;lat，lng
				当前经纬度(选填)</td>
			<td>1：0 成功：失败；</td>
		</tr>

		<tr>
			<td>30017</td>
			<td>开始骑行（适用于带蓝牙设备）</td>
			<td><a
				href="<%=basePath%>app/bike?requestType=30017&token=1&date=1132">
					<%=basePath%>app/bike?requestType=30017&token=1&date=1132
			</a></td>
			<td>date:骑行时间戳;(选填 power：电量)</td>
			<td>1：0 成功：失败；</td>
		</tr>

		<tr>
			<td>30018</td>
			<td>结束骑行（适用与带蓝牙设备）</td>
			<td><a
				href="<%=basePath%>app/bike?requestType=30018&token=1&number=1&timeStamp=2323&runTime=1">
					<%=basePath%>app/bike?requestType=30018&token=1&number=1&timeStamp=2323&runTime=1
			</a></td>
			<td>uid：骑行用户ID；date：开锁时间戳；runTime：骑行时间</td>
			<td>订单信息（同30006）</td>
		</tr>

		<tr>
			<td>30019</td>
			<td>寻车铃(TCP)</td>
			<td><a
				href="<%=basePath%>app/bike?requestType=30019&token=1&bikeId=1132">
					<%=basePath%>app/bike?requestType=30019&token=1&bikeId=1132
			</a></td>
			<td>bikeId:单车ID</td>
			<td>1：0 成功：失败；</td>
		</tr>
		<tr>
			<td>30020</td>
			<td>获取单车价格信息</td>
			<td><a
				href="<%=basePath%>app/bike?requestType=30020&token=1&bikeId=1132">
					<%=basePath%>app/bike?requestType=30020&token=1&bikeId=1132
			</a></td>
			<td>bikeId:单车ID</td>
			<td>price:价格；unit_type 3单位 1：分钟 2：小时 3：月份；count:数量</td>
		</tr>
		<tr>
			<td>30021</td>
			<td>用户故障上报列表</td>
			<td><a href="<%=basePath%>app/bike?requestType=30021&token=1">
					<%=basePath%>app/bike?requestType=30021&token=1
			</a></td>
			<td>pageNo</td>
			<td>id:记录ID；bnumber：单车编号；uid：用户ID；type：故障类型（同提交一致）；typeStr：类型字符串；
				error_type：故障位置：（同提交一致）；errorTypeStr：位置字符串；content：故障内容；
				date：报告时间；status：处理状态 0：待审核 1：正在审核 2：通过审核 3：未通过审核
				4:已自动审核(适用于关锁未结费或不能开锁故障）；statusStr：状态字符串； bike_useid：骑行记录ID；
				review_note：审核备注；review_date：审核时间；lat,lng：报告经纬度；</td>
		</tr>

		<tr>
			<td>30022</td>
			<td>用户故障上报详情</td>
			<td><a
				href="<%=basePath%>app/bike?requestType=30021&token=1&id=1"> <%=basePath%>app/bike?requestType=30021&token=1&id=1
			</a></td>
			<td>id:故障ID</td>
			<td>同30022</td>
		</tr>
		<tr>
			<td>30030</td>
			<td><font color="red">【New】</font>骑行结束，请求网络关锁（目前适用于滑板车）</td>
			<td><a
				href="<%=basePath%>app/bike?requestType=30030&token=1&date=1"> <%=basePath%>app/bike?requestType=30030&token=1&date=1
			</a></td>
			<td>date:骑行时间戳</td>
			<td>data 0/1 失败/成功</td>
		</tr>
		<tr>
			<td colspan="5" align="center"><font color="red">支付</font> </br>
				模块必传参数： payType（支付方式 支付成功;way(wayStr):支付方式 1:账户余额 2：微信 3：支付宝
				4:PayPay[paymentMethodNonce:支付信息;firstName;lastName] 5:VISA
				6:Strip[quickPay:1（快捷支付，保存卡信息）] 7:长租免费 8:优惠券 9:红包 10:Anet
				11:AcquiroPay[status为200时返回的data为请求支付url，在webview中打开链接]）
				12:PayU[status为200时返回的data为请求支付所需参数，请求URL：https://secure.payu.ro/order/lu.php?+params(POST)，在webview中打开链接]）
				userIP:设备IP地址(仅微信支付需要) </br>
				返回参数说明：余额支付和支付宝支付返回参数为JSON格式，微信格式为XML（终端在接收到此参数后直接用此参数调起支付）</br> 返回结果 0:1
				操作成功/失败 40001:支付异常
				;error：异常信息;40002:余额不足;40003:无默认银行卡信息;40004:添加银行卡失败;</td>
		</tr>


		<tr>
			<td>40001(P)</td>
			<td>押金支付</td>
			<td><a href="<%=basePath%>app/pay?requestType=40001&token=1">
					<%=basePath%>app/pay?requestType=40001&token=1
			</a></td>
			<td>anet支付:首次：cardNumber：卡号 ；expirDate：过期日期；cardCode：安全码（选填）
				非首次：customerPaymentProfileId：银行卡ID（列表返回的）</td>
			<td>1：0 成功：失败；</td>
		</tr>
		<tr>
			<td>40002(P)</td>
			<td>充值支付</td>
			<td><a
				href="<%=basePath%>app/pay?requestType=40002&token=1&amount=1">
					<%=basePath%>app/pay?requestType=40002&token=1&amount=1
			</a></td>
			<td>amount：金额；amountId：金额ID</td>
			<td>1：0 成功：失败；</td>
		</tr>
		<tr>
			<td>40003(P)</td>
			<td>骑行订单支付</td>
			<td><a href="<%=basePath%>app/pay?requestType=40003&token=1">
					<%=basePath%>app/pay?requestType=40003&token=1
			</a></td>
			<td>无附加参数</td>
			<td>1：0 成功：失败；</td>
		</tr>
		</tr>
		<tr>
			<td>40004(P)</td>
			<td>单车长租支付</td>
			<td><a
				href="<%=basePath%>app/pay?requestType=40004&token=1&typeId=1">
					<%=basePath%>app/pay?requestType=40004&token=1&typeId=1
			</a></td>
			<td>typeId：长租类型ID</td>
			<td>1：0 成功：失败；</td>
		</tr>
		<tr>
			<td>40005(P)</td>
			<td>校验支付宝支付结果信息</td>
			<td><a
				href="<%=basePath%>app/pay?requestType=40005&token=1&result=str">
					<%=basePath%>app/pay?requestType=40005&token=1&result=str
			</a></td>
			<td>result:支付宝支付返回的JSON格式结果</br> 返回参数说明：success（支付成功） failure（支付失败）
				注意：返回参数未经JSON格式化
			</td>
		</tr>
		<tr>
			<td>40006(P)</td>
			<td>校验支付结果信息</td>
			<td><a
				href="<%=basePath%>app/pay?requestType=40006&token=1&tradeNo=1">
					<%=basePath%>app/pay?requestType=40006&token=1&tradeNo=1
			</a></td>
			<td>tradeNo:第三方支付订单号；</td>
			<td>trainId:交易订单ID</br> 返回参数说明：return_code：SUCCESS（支付成功） FAIL（支付失败）；
				return_msg：OK（支付成功）其他为错误信息 注意：返回参数为XML格式化
			</td>
		</tr>
		<tr>
			<td>40007</td>
			<td>获取押金</td>
			<td><a href="<%=basePath%>app/pay?requestType=40007&token=1">
					<%=basePath%>app/pay?requestType=40007&token=1
			</a></td>
			<td>无附加参数</td>
			<td>data:押金</td>
		</tr>
		<tr>
			<td>40008</td>
			<td>获取充值金额</td>
			<td><a href="<%=basePath%>app/pay?requestType=40008&token=1">
					<%=basePath%>app/pay?requestType=40008&token=1
			</a></td>
			<td>无附加参数</td>
			<td>id:金额id； amount：充值金额；gift：赠送金额/数量;gift_type;赠送类型 1：余额 2：优惠券</td>
		</tr>
		<tr>
			<td>40009</td>
			<td>用户银行卡列表获取(仅限Anet支付或绑定的银行卡)</td>
			<td><a href="<%=basePath%>app/pay?requestType=40009&token=1">
					<%=basePath%>app/pay?requestType=40009&token=1
			</a></td>
			<td>payType</td>
			<td>Ant:(CustomerPaymentProfileId：卡号ID；getPayment().getCreditCard().getCardNumber()：卡号（已作处理，不显示全部）)
				Strip:(id:卡号ID；last4：后四位；brand：类型) ;defaultPaymentId:默认支付银行卡ID</td>
		</tr>
		<tr>
			<td>40010</td>
			<td>保存银行卡</td>
			<td><a href="<%=basePath%>app/pay?requestType=40010&token=1">
					<%=basePath%>app/pay?requestType=40010&token=1
			</a></td>
			<td>payType；Anet:（cardNumber：卡号；expirDate：过期日期；cardCode：安全码（选填））
				Strip:tokenId</td>
			<td>data:0:1</td>
		</tr>
		<tr>
			<td>40011</td>
			<td>获取PayPal支付信息</td>
			<td><a href="<%=basePath%>app/pay?requestType=40010&token=1">
					<%=basePath%>app/pay?requestType=40010&token=1
			</a></td>
			<td>payRequestType：支付类型接口号，如：押金支付，40001；其他（充值：amount;长租：typeId）</td>
			<td>token:支付请求密钥；currency：币种；amount:金额；</td>
		</tr>

		<tr>
			<td>40012</td>
			<td>设置默认支付银卡信息</td>
			<td><a
				href="<%=basePath%>app/pay?requestType=40012&token=1&customerPaymentProfileId=1">
					<%=basePath%>app/pay?requestType=40012&token=1&customerPaymentProfileId=1
			</a></td>
			<td>payType;customerPaymentProfileId：卡ID；</td>
			<td>data:0:1</td>
		</tr>

		<tr>
			<td colspan="5" align="center"><font color="red">其他</font></br>
				50001:短信数超出单日最大;50002:一分钟只能发送一条</td>
		</tr>
		</tr>
		<tr>
			<td></td>
			<td>文件上传</td>
			<td><a href="<%=basePath%>app/upload?requestType=1"> <%=basePath%>app/upload?requestType=1
			</a></td>
			<td></td>
			<td>data:图片路径</td>
		</tr>
		<tr>
			<td>50001</td>
			<td>网页内容</td>
			<td><a
				href="<%=basePath%>other?requestType=50001&industryId=2&type=1">
					<%=basePath%>other?requestType=50001&industryId=2&type=1
			</a></td>
			<td>industryId:产业ID；type：类型：1 用户协议 2：充值协议 3:找不到车 4：违章 5：押金说明
				；6：红包攻略；7：积分规则；8：隐私条款</td>
			<td></td>
		</tr>
		<tr>
			<td>50002</td>
			<td>发送验证码</td>
			<td><a
				href="<%=basePath%>other?requestType=50002&industryId=2&phone=13410996848&smsType=1">
					<%=basePath%>other?requestType=50002&industryId=2&phone=13410996848&smsType=1
			</a></td>
			<td>industryId:产业ID；phone：手机号;phoneCode:区号（选填）;smsType:1,注册 2，登录、忘记密码</td>
			<td>1：0 成功：失败；</td>
		</tr>

		<tr>
			<td>50003</td>
			<td>获取国家地区列表</td>
			<td><a href="<%=basePath%>other?requestType=50003"> <%=basePath%>other?requestType=50003
			</a></td>
			<td></td>
			<td>english_name：地区英文名；chinese_name：地区中文名；phone_code：电话区号；</td>
		</tr>

		<tr>
			<td>50004</td>
			<td>资讯活动内容</td>
			<td><a
				href="<%=basePath%>other?requestType=50004&industryId=2&newsId=1">
					<%=basePath%>other?requestType=50004&industryId=2&newsId=1
			</a></td>
			<td>newsId:活动D；</td>
			<td></td>
		</tr>

		<tr>
			<td>50005</td>
			<td>获取单车支持城市</td>
			<td><a href="<%=basePath%>other?requestType=50005&industryId=2">
					<%=basePath%>other?requestType=50005&industryId=2
			</a></td>
			<td></td>
			<td>id:城市ID；name:城市名称</td>
		</tr>

		<tr>
			<td>50006</td>
			<td>验证邀请码</td>
			<td><a
				href="<%=basePath%>other?requestType=50006&industryId=2&inviteCode=12d22">
					<%=basePath%>other?requestType=50006&industryId=2&inviteCode=12d22
			</a></td>
			<td>inviteCode:邀请码；</td>
			<td>data 0:1 不存在/存在</td>
		</tr>

		<tr>
			<td>50008</td>
			<td>发送邮箱验证码</td>
			<td><a
				href="<%=basePath%>other?requestType=50008&industryId=2&email=test@omni.com&emailType=1">
					<%=basePath%>other?requestType=50008&industryId=2&email=test@omni.com&emailType=1
			</a></td>
			<td>industryId:产业ID；email：邮箱;emailType:1,注册 2，登录、忘记密码</td>
			<td>1：0 成功：失败；</td>
		</tr>

		<tr>
			<td colspan="5" align="center"><font color="red">信箱</font></td>
		</tr>
		<tr>
			<td>60001</td>
			<td>获取消息列表</td>
			<td><a
				href="<%=basePath%>app/message?requestType=60001&industryId=2&token=1">
					<%=basePath%>app/message?requestType=60001&industryId=2&token=1
			</a></td>
			<td></td>
			<td>title:消息标题；content:消息内容;status:1 已收藏</td>
		</tr>
		<tr>
			<td>60002</td>
			<td>添加消息</td>
			<td><a
				href="<%=basePath%>app/message?requestType=60002&industryId=2&token=1&title=test&content=testcontent">
					<%=basePath%>app/message?requestType=60002&industryId=2&token=1&title=test&content=testcontent
			</a></td>
			<td>title:消息标题；content:消息内容</td>
			<td></td>
		</tr>

		<tr>
			<td>60003</td>
			<td>获取消息回复列表</td>
			<td><a
				href="<%=basePath%>app/message?requestType=60003&industryId=2&token=1&id=1">
					<%=basePath%>app/message?requestType=60003&industryId=2&token=1&id=1
			</a></td>
			<td>id:主体消息Id</td>
			<td>content:回复消息内容</td>
		</tr>

		<tr>
			<td>60004</td>
			<td>添加回复</td>
			<td><a
				href="<%=basePath%>app/message?requestType=60004&industryId=2&token=1&id=1&content=testcontent">
					<%=basePath%>app/message?requestType=60004&industryId=2&token=1&id=1&content=testcontent
			</a></td>
			<td>id:主体消息Id；content:消息内容</td>
			<td></td>
		</tr>

		<tr>
			<td>60005</td>
			<td>修改消息状态</td>
			<td><a
				href="<%=basePath%>app/message?requestType=60005&industryId=2&token=1&id=1&status=1">
					<%=basePath%>app/message?requestType=60005&industryId=2&token=1&id=1&status=1
			</a></td>
			<td>id:主体消息Id；status：1收藏 0：取消收藏 -1:删除</td>
			<td></td>
		</tr>
	</table>
</body>
</html>
 --%>