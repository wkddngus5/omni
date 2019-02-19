<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
ResourceBundle rb= ((ResourceBundle)session.getAttribute("languageRD"));
%>
<_AJAX_>

<!-- dwz.dialog -->
<_PAGE_ id="dialogFrag"><![CDATA[
<div class="dialog" style="top:150px; left:300px;">
	<div class="dialogHeader" onselectstart="return false;" oncopy="return false;" onpaste="return false;" oncut="return false;">
		<div class="dialogHeader_r">
			<div class="dialogHeader_c">
				<a class="close" href="#close">close</a>
				<a class="maximize" href="#maximize">maximize</a>
				<a class="restore" href="#restore">restore</a>
				<a class="minimize" href="#minimize">minimize</a>
				<h1>弹出窗口</h1>
			</div>
		</div>
	</div>
	<div class="dialogContent layoutBox unitBox">
	</div>
	<div class="dialogFooter"><div class="dialogFooter_r"><div class="dialogFooter_c"></div></div></div>
	<div class="resizable_h_l" tar="nw"></div>
	<div class="resizable_h_r" tar="ne"></div>
	<div class="resizable_h_c" tar="n"></div>
	<div class="resizable_c_l" tar="w" style="height:300px;"></div>
	<div class="resizable_c_r" tar="e" style="height:300px;"></div>
	<div class="resizable_f_l" tar="sw"></div>
	<div class="resizable_f_r" tar="se"></div>
	<div class="resizable_f_c" tar="s"></div>
</div>
]]></_PAGE_>
<!-- dwz.dialog shadow -->
<_PAGE_ id="dialogProxy"><![CDATA[
<div id="dialogProxy" class="dialog dialogProxy">
	<div class="dialogHeader">
		<div class="dialogHeader_r">
			<div class="dialogHeader_c">
				<a class="close" href="#close">close</a>
				<a class="maximize" href="#maximize">maximize</a>
				<a class="minimize" href="#minimize">minimize</a>
				<h1></h1>
			</div>
		</div>
	</div>
	<div class="dialogContent"></div>
	<div class="dialogFooter">
		<div class="dialogFooter_r">
			<div class="dialogFooter_c">
			</div>
		</div>
	</div>
</div>
]]></_PAGE_>
<!-- dwz.dialog taskbar -->
<_PAGE_ id="taskbar"><![CDATA[
<div id="taskbar" style="left:0px; display:none;">
	<div class="taskbarContent">
		<ul></ul>
	</div>
	<div class="taskbarLeft taskbarLeftDisabled" style="display:none;">taskbarLeft</div>
	<div class="taskbarRight" style="display:none;">taskbarRight</div>
</div>
]]></_PAGE_>

<!-- other dwz fragment -->
<_PAGE_ id="dwzFrag"><![CDATA[
<div id="splitBar"></div>
<div id="splitBarProxy"></div>

<!--拖动效果-->
<div class="resizable"></div>
<!--阴影-->
<div class="shadow" style="width:508px; top:148px; left:296px;">
	<div class="shadow_h">
		<div class="shadow_h_l"></div>
		<div class="shadow_h_r"></div>
		<div class="shadow_h_c"></div>
	</div>
	<div class="shadow_c">
		<div class="shadow_c_l" style="height:296px;"></div>
		<div class="shadow_c_r" style="height:296px;"></div>
		<div class="shadow_c_c" style="height:296px;"></div>
	</div>
	<div class="shadow_f">
		<div class="shadow_f_l"></div>
		<div class="shadow_f_r"></div>
		<div class="shadow_f_c"></div>
	</div>
</div>
<!--遮盖屏幕-->
<div id="alertBackground" class="alertBackground"></div>
<div id="dialogBackground" class="dialogBackground"></div>

<div id='background' class='background'></div>
<div id='progressBar' class='progressBar'><%=rb.getString("page_tips_loading") %></div>
]]></_PAGE_>

<!-- dwz.pagination -->
<_PAGE_ id="pagination"><![CDATA[
<ul>
	<li class="j-first">
		<a class="first" href="javascript:;"><span><%=rb.getString("common_page_index") %></span></a>
		<span class="first"><span><%=rb.getString("common_page_index") %></span></span>
	</li>
	<li class="j-prev">
		<a class="previous" href="javascript:;"><span><%=rb.getString("common_page_previous") %></span></a>
		<span class="previous"><span><%=rb.getString("common_page_previous") %></span></span>
	</li>
	#pageNumFrag#
	<li class="j-next">
		<a class="next" href="javascript:;"><span><%=rb.getString("common_page_next") %></span></a>
		<span class="next"><span><%=rb.getString("common_page_next") %></span></span>
	</li>
	<li class="j-last">
		<a class="last" href="javascript:;"><span><%=rb.getString("common_page_end") %></span></a>
		<span class="last"><span><%=rb.getString("common_page_end") %></span></span>
	</li>
	<li class="jumpto"><input class="textInput" type="text" size="4" value="#currentPage#" /><input class="goto" type="button" value="确定" /></li>
</ul>
]]></_PAGE_>

	
<!-- dwz.alertMsg -->
<_PAGE_ id="alertBoxFrag"><![CDATA[
<div id="alertMsgBox" class="alert"><div class="alertContent"><div class="#type#"><div class="alertInner"><h1>#title#</h1><div class="msg">#message#</div></div><div class="toolBar"><ul>#butFragment#</ul></div></div></div><div class="alertFooter"><div class="alertFooter_r"><div class="alertFooter_c"></div></div></div></div>
]]></_PAGE_>
		
<_PAGE_ id="alertButFrag"><![CDATA[
<li><a class="button" rel="#callback#" onclick="alertMsg.close()" href="javascript:"><span>#butMsg#</span></a></li>
]]></_PAGE_>

<_PAGE_ id="calendarFrag"><![CDATA[
<div id="calendar">
	<div class="main">
		<div class="head">
			<table width="100%" border="0" cellpadding="0" cellspacing="2">
			<tr>
				<td><select name="year"></select></td>
				<td><select name="month"></select></td>
				<td width="20"><span class="close">×</span></td>
			</tr>
			</table>
		</div>
		<div class="body">
			<dl class="dayNames"><%=rb.getString("page_week_values") %></dl>
			<dl class="days"><%=rb.getString("page_days_select_options") %></dl>
			<div style="clear:both;height:0;line-height:0"></div>
			
		</div>
		
		<div class="foot">
			<table class="time">
				<tr>
					<td>
						<input type="text" class="hh" maxlength="2" start="0" end="23"/>:
						<input type="text" class="mm" maxlength="2" start="0" end="59"/>:
						<input type="text" class="ss" maxlength="2" start="0" end="59"/>
					</td>
					<td><ul><li class="up">&and;</li><li class="down">&or;</li></ul></td>
				</tr>
			</table>
			<button type="button" class="clearBut"><%=rb.getString("common_clearn_title") %></button>
			<button type="button" class="okBut"><%=rb.getString("common_commit_title") %></button>
		<div>
		<div class="tm">
			<ul class="hh">
				<li>0</li>
				<li>1</li>
				<li>2</li>
				<li>3</li>
				<li>4</li>
				<li>5</li>
				<li>6</li>
				<li>7</li>
				<li>8</li>
				<li>9</li>
				<li>10</li>
				<li>11</li>
				<li>12</li>
				<li>13</li>
				<li>14</li>
				<li>15</li>
				<li>16</li>
				<li>17</li>
				<li>18</li>
				<li>19</li>
				<li>20</li>
				<li>21</li>
				<li>22</li>
				<li>23</li>
			</ul>
			<ul class="mm">
				<li>0</li>
				<li>5</li>
				<li>10</li>
				<li>15</li>
				<li>20</li>
				<li>25</li>
				<li>30</li>
				<li>35</li>
				<li>40</li>
				<li>45</li>
				<li>50</li>
				<li>55</li>
			</ul>
			<ul class="ss">
				<li>0</li>
				<li>10</li>
				<li>20</li>
				<li>30</li>
				<li>40</li>
				<li>50</li>
			</ul>
		</div>
	</div>
</div>
]]></_PAGE_>

<_PAGE_ id="navTabCM"><![CDATA[
<ul id="navTabCM">
	<li rel="reload"><%=rb.getString("page_navtab_reload") %></li>
	<li rel="closeCurrent"><%=rb.getString("page_navtab_close_current") %></li>
	<li rel="closeOther"><%=rb.getString("page_navtab_close_other") %></li>
	<li rel="closeAll"><%=rb.getString("page_navtab_close_all") %></li>
</ul>
]]></_PAGE_>
<_PAGE_ id="dialogCM"><![CDATA[
<ul id="dialogCM">
	<li rel="closeCurrent"><%=rb.getString("page_dialog_close_current") %></li>
	<li rel="closeOther"><%=rb.getString("page_dialog_close_other") %></li>
	<li rel="closeAll"><%=rb.getString("page_dialog_close_all") %></li>
</ul>
]]></_PAGE_>
<_PAGE_ id="externalFrag"><![CDATA[
<iframe src="{url}" style="width:100%;height:{height};" frameborder="no" border="0" marginwidth="0" marginheight="0"></iframe>
]]></_PAGE_>
<_MSG_ id="statusCode_503"><![CDATA[<%=rb.getString("page_server_maintenance_tips") %>]]></_MSG_>
<_MSG_ id="validateFormError"><![CDATA[<%=rb.getString("page_data_complete_tips") %>]]></_MSG_>
<_MSG_ id="sessionTimout"><![CDATA[<%=rb.getString("page_login_session_timeout") %>]]></_MSG_>
<_MSG_ id="alertSelectMsg"><![CDATA[<%=rb.getString("page_select_error_tips") %>]]></_MSG_>
<_MSG_ id="forwardConfirmMsg"><![CDATA[<%=rb.getString("page_next_tips") %>]]></_MSG_>

<_MSG_ id="dwzTitle"><![CDATA[富客户端框架]]></_MSG_>
<_MSG_ id="mainTabTitle"><![CDATA[我的主页]]></_MSG_>
</_AJAX_>