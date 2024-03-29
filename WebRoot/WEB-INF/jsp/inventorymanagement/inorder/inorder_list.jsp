﻿<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html>
<html lang="en">
<head>
<base href="<%=basePath%>">
<!-- 下拉框 -->
<link rel="stylesheet" href="static/ace/css/chosen.css" />
<!-- jsp文件头和头部 -->
<%@ include file="../../system/index/top.jsp"%>
<script type="text/javascript" src="static/js/myjs/head.js"></script>
<!-- 日期框 -->
<link rel="stylesheet" href="static/ace/css/datepicker.css" />
</head>
<body class="no-skin">
	<!-- /section:basics/navbar.layout -->
	<div class="main-container" id="main-container">
		<!-- /section:basics/sidebar -->
		<div class="main-content">
			<div class="main-content-inner">
				<div class="page-content">
					<div class="row">
						<div class="col-xs-12">
							
						<!-- 检索  -->
						<form action="inorder/list.do" method="post" name="Form" id="Form">
						<table style="margin-top:5px;">
							<tr>
								<td>
									<div class="nav-search">
										<span class="input-icon">
											<input type="text" placeholder="这里输入关键词" class="nav-search-input" id="nav-search-input" autocomplete="off" name="keywords" value="${pd.keywords }" placeholder="这里输入关键词"/>
											<i class="ace-icon fa fa-search nav-search-icon"></i>
										</span>
									</div>
								</td>
								<td>
									<select class="chosen-select form-control" name="SUPPLIER_ID" id="SUPPLIER_ID" data-placeholder="选择供应商" style="vertical-align:top;width:98%;" >
										<option value="">选择供应商</option>
										<c:forEach items="${supplierList}" var="var">
											<option value="${var.SUPPLIER_ID }" <c:if test="${var.SUPPLIER_ID == pd.SUPPLIER_ID }">selected</c:if>>${var.SUPPLIERNAME }</option>  <!-- <c:if test="${var.SUPPLIER_ID == pd.SUPPLIER_ID }">selected</c:if> -->
										</c:forEach>
									</select>
								</td>
								<td>
									<select class="chosen-select form-control" name="ISSETTLEMENTED" id="ISSETTLEMENTED" data-placeholder="选择结算状态" style="vertical-align:top;width:98%;" >
										<option value="">选择结算状态</option>
										<option value="0" <c:if test="${'0' == pd.ISSETTLEMENTED }">selected</c:if>>未结算</option>
										<option value="1" <c:if test="${'1' == pd.ISSETTLEMENTED }">selected</c:if>>已结算</option>
										<option value="2" <c:if test="${'2' == pd.ISSETTLEMENTED }">selected</c:if>>结算中</option>
									</select>
								</td>
								<td>
									<select class="chosen-select form-control" name="BILLSTATUS" id="BILLSTATUS" data-placeholder="选择审核状态" style="vertical-align:top;width:98%;" >
										<option value="">选择审核状态</option>
										<option value="1" <c:if test="${'1' == pd.BILLSTATUS }">selected</c:if>>未审核</option>
										<option value="2" <c:if test="${'2' == pd.BILLSTATUS }">selected</c:if>>已审核</option>
										<option value="3" <c:if test="${'3' == pd.BILLSTATUS }">selected</c:if>>作废</option>
									</select>
								</td>
								<td style="padding-left:2px;"><input class="span10 date-picker" name="lastStart" id="lastStart"  value="${pd.lastStart }" type="text" data-date-format="yyyy-mm-dd" readonly="readonly" style="width:88px;" placeholder="开始日期" title="开始日期"/></td>
    							<td style="padding-left:2px;"><input class="span10 date-picker" name="lastEnd" id="lastEnd"  value="${pd.lastEnd }" type="text" data-date-format="yyyy-mm-dd" readonly="readonly" style="width:88px;" placeholder="结束日期" title="结束日期"/></td> 
								<c:if test="${QX.cha == 1 }">
								<td style="vertical-align:top;padding-left:2px"><a class="btn btn-light btn-xs" onclick="tosearch();"  title="检索"><i id="nav-search-icon" class="ace-icon fa fa-search bigger-110 nav-search-icon blue"></i></a></td>
								</c:if>
							
								<c:if test="${QX.toExcel == 1 }"><td style="vertical-align:top;padding-left:2px;">	&nbsp;&nbsp;&nbsp;<a class="btn btn-xs btn-success" onclick="toExcel();" title="导出到EXCEL">导出EXCEL</a></td></c:if>
								<%-- <c:if test="${QX.toExcel == 1 }"><td style="vertical-align:top;padding-left:2px;"><a class="btn btn-light btn-xs" onclick="toExcel();" title="导出到EXCEL"><i id="nav-search-icon" class="ace-icon fa fa-download bigger-110 nav-search-icon blue"></i></a></td></c:if> --%>
								<td style="padding-left:2px;;vertical-align:top;">
									<c:if test="${QX.add == 1 }">
									<a class="btn btn-xs btn-success" onclick="add();">新增</a>
									<!-- <a class="btn btn-mini btn-success" onclick="add();">新增</a> -->
									</c:if>
									<c:if test="${QX.del == 1 }"><c:if test="${'1' == pd.BILLSTATUS || '3' == pd.BILLSTATUS}">
									<a class="btn btn-xs btn-danger" onclick="makeAll('确定要删除选中的数据吗?');" title="批量删除" >批量删除</i></a>
									</c:if></c:if>
									<c:if test="${QX.InOrdershenpiApproval == 1 }">
										<a class="btn btn-xs btn-success" onclick="shenpiAll('确定要批量审批选中的数据吗?');" title="批量审批" >批量审批</a>
									</c:if>
								</td>
							</tr>
						</table>
						<!-- 检索  -->
					
						<table id="simple-table" class="table table-striped table-bordered table-hover" style="margin-top:5px;">	
							<thead>
								<tr>
									<th class="center" style="width:35px;">
									<label class="pos-rel"><input type="checkbox" class="ace" id="zcheckbox" /><span class="lbl"></span></label>
									</th>
									<th class="center" style="width:50px;">序号</th>
									<th class="center">单据编号</th>
									<th class="center">供应商</th>
									<th class="center">总金额</th>
									<th class="center">未付金额</th>
									<th class="center">已付金额</th>
									<th class="center">本次付款</th>
									<th class="center">结算状态</th>
									<th class="center">审核状态</th>
									
									<th class="center">日期</th>
									<th class="center">经手人</th>
									<th class="center">备注</th>
									<th class="center">操作</th><!-- style="width:150px;" -->
								</tr>
							</thead>
													
							<tbody>
							<!-- 开始循环 -->	
							<c:choose>
								<c:when test="${not empty varList}">
									<c:if test="${QX.cha == 1 }">
									<c:forEach items="${varList}" var="var" varStatus="vs">
										<tr id="${var.INORDER_ID}" onclick="clickaction('${var.INORDER_ID}');">
											<td class='center' id="checkbox">
												<label class="pos-rel"><input type='checkbox' name='ids' value="${var.INORDER_ID}" class="ace" /><span class="lbl"></span></label>
											</td>
											<td class='center' style="width: 30px;">${vs.index+1}</td>
											<td class='center'>${var.BILLCODE}</td>
											<td class='center'>${var.SUPPLIERNAME}</td>
											<td class='center'>${var.ALLAMOUNT}</td>
											<td class='center'>${var.UNPAIDAMOUNT}</td>
											<td class='center'>${var.PAIDAMOUNT}</td>
											<td class='center'>${var.THISPAY}</td>
											<td class='center'>
												<c:if test="${var.ISSETTLEMENTED == 2}">
													结算中
												</c:if>
												<c:if test="${var.ISSETTLEMENTED == 1}">
													已结算
												</c:if>
												<c:if test="${var.ISSETTLEMENTED == 0}">
													未结算
												</c:if>
											</td>
											<td class='center' >
												<c:if test="${var.BILLSTATUS == 1}">
													<font color="blue">未审核</font>
												</c:if>
												<c:if test="${var.BILLSTATUS == 2}">
													<font color="green">已审核</font>
												</c:if>
												<c:if test="${var.BILLSTATUS == 3}">
													<font color="red">作废</font>
												</c:if>
											</td>
									
											<td class='center'>${var.CREATETIME}</td>
											<td class='center'>${var.PSI_NAME}</td>
											<td class='center'>${var.NOTE}</td>
											<td class="center">
												<c:if test="${QX.edit != 1 && QX.del != 1 }">
												<span class="label label-large label-grey arrowed-in-right arrowed-in"><i class="ace-icon fa fa-lock" title="无权限"></i></span>
												</c:if>
												<div class="hidden-sm hidden-xs btn-group">
													<c:if test="${QX.InOrderfanshenApproval == 1 }">
													<a class="btn btn-mini btn-danger"  style="height:26px;" onclick="fanshen('${var.INORDER_ID}','${var.ISSETTLEMENTED}','${var.BILLSTATUS}');">反审</a>
													</c:if>
													<c:if test="${QX.daying == 1 }">
														<a class="btn btn-mini btn-success"  style="height:26px;" onclick="daying('${var.INORDER_ID}');">打印</a>
													</c:if>
														
													<c:if test="${QX.InOrdershenpiApproval == 1 }">
													<a class="btn btn-mini btn-success" style="height:26px;" onclick="shenpi('${var.INORDER_ID}','${var.BILLSTATUS}');">审批</a>
													</c:if>
													<a class="btn btn-xs btn-success" title="查看" onclick="view('${var.INORDER_ID}');">
														<i class="ace-icon fa fa-eye bigger-120" title="查看"></i>
													</a>
													<c:if test="${QX.edit == 1 }">
													<a class="btn btn-xs btn-success" title="编辑" onclick="edit('${var.INORDER_ID}','${var.BILLSTATUS}');">
														<i class="ace-icon fa fa-pencil-square-o bigger-120" title="编辑"></i>
													</a>
													</c:if>
													<c:if test="${QX.del == 1 }">
													<a class="btn btn-xs btn-danger" onclick="del('${var.INORDER_ID}','${var.BILLSTATUS}','${var.ISSETTLEMENTED}');">
														<i class="ace-icon fa fa-trash-o bigger-120" title="删除"></i>
													</a>
													</c:if>
												</div>
												<div class="hidden-md hidden-lg">
													<div class="inline pos-rel">
														<button class="btn btn-minier btn-primary dropdown-toggle" data-toggle="dropdown" data-position="auto">
															<i class="ace-icon fa fa-cog icon-only bigger-110"></i>
														</button>
			
														<ul class="dropdown-menu dropdown-only-icon dropdown-yellow dropdown-menu-right dropdown-caret dropdown-close">
															<li>
																<a style="cursor:pointer;" onclick="view('${var.INORDER_ID}');" class="tooltip-success" data-rel="tooltip" title="查看">
																	<span class="green">
																		<i class="ace-icon fa fa-eye bigger-120"></i>
																	</span>
																</a>
															</li>
															<c:if test="${QX.edit == 1 }">
															<li>
																<a style="cursor:pointer;" onclick="edit('${var.INORDER_ID}');" class="tooltip-success" data-rel="tooltip" title="修改">
																	<span class="green">
																		<i class="ace-icon fa fa-pencil-square-o bigger-120"></i>
																	</span>
																</a>
															</li>
															</c:if>
															<c:if test="${QX.del == 1 }">
															<li>
																<a style="cursor:pointer;" onclick="del('${var.INORDER_ID}');" class="tooltip-error" data-rel="tooltip" title="删除">
																	<span class="red">
																		<i class="ace-icon fa fa-trash-o bigger-120"></i>
																	</span>
																</a>
															</li>
															</c:if>
														</ul>
													</div>
												</div>
											</td>
										</tr>
									
									</c:forEach>
									</c:if>
									<c:if test="${QX.cha == 0 }">
										<tr>
											<td colspan="100" class="center">您无权查看</td>
										</tr>
									</c:if>
								</c:when>
								<c:otherwise>
									<tr class="main_info">
										<td colspan="100" class="center" >没有相关数据</td>
									</tr>
								</c:otherwise>
							</c:choose>
							</tbody>
						</table>
						<div class="page-header position-relative">
						<table style="width:100%;">
							<tr>
								<%-- <td style="vertical-align:top;">
									<c:if test="${QX.add == 1 }">
									<a class="btn btn-mini btn-success" onclick="add();">新增</a>
									<!-- <a class="btn btn-mini btn-success" onclick="add();">新增</a> -->
									</c:if>
									<c:if test="${QX.del == 1 }"><c:if test="${'1' == pd.BILLSTATUS || '3' == pd.BILLSTATUS}">
									<a class="btn btn-mini btn-danger" onclick="makeAll('确定要删除选中的数据吗?');" title="批量删除" >批量删除</i></a>
									</c:if></c:if>
									<c:if test="${QX.InOrdershenpiApproval == 1 }">
										<a class="btn btn-mini btn-success" onclick="shenpiAll('确定要批量审批选中的数据吗?');" title="批量审批" >批量审批</a>
									</c:if>
								</td> --%>
								<td style="vertical-align:top;"><div class="pagination" style="float: right;padding-top: 0px;margin-top: 0px;">${page.pageStr}</div></td>
							</tr>
						</table>
						</div>
						</form>
						<form action="#" method="post" name="actionForm" id="actionForm"></form>
						</div>
					</div>
				</div>
				<table id="simple-table" class="table table-striped table-bordered table-hover" style="margin-top:5px;">	
							<thead>
								<tr>
									<th class="center" style="width:50px;">序号</th>
									<th class="center">商品名称</th>
									<th class="center">商品编号</th>
									<th class="center">仓库</th>
									<th class="center">单价</th>
									<th class="center">数量</th>
									<th class="center">计量单位</th>
									<th class="center">金额</th>
									<th class="center">备注</th>
								</tr>
							</thead>
							<tbody id="realtbody">
							
							</tbody>
				</table>
			</div>
		</div>
		<!-- /.main-content -->

		<!-- 返回顶部 -->
		<a href="#" id="btn-scroll-up" class="btn-scroll-up btn btn-sm btn-inverse">
			<i class="ace-icon fa fa-angle-double-up icon-only bigger-110"></i>
		</a>

	</div>
	<!-- /.main-container -->
	<!-- basic scripts -->
	<!-- 页面底部js¨ -->
	<%@ include file="../../system/index/foot.jsp"%>
	<!-- 删除时确认窗口 -->
	<script src="static/ace/js/bootbox.js"></script>
	<!-- ace scripts -->
	<script src="static/ace/js/ace/ace.js"></script>
	<!-- 下拉框 -->
	<script src="static/ace/js/chosen.jquery.js"></script>
	<!-- 日期框 -->
	<script src="static/ace/js/date-time/bootstrap-datepicker.js"></script>
	<!--提示框-->
	<script type="text/javascript" src="static/js/jquery.tips.js"></script>
	<script type="text/javascript">
		$(top.hangge());//关闭加载状态
		
		//新增
		function add(){
			document.forms.actionForm.action="inorder/goAdd.do";
	        document.forms.actionForm.submit();
			//siMenu('z190','lm180','添加进货单','inorder/goAdd.do');
		}
		//修改
		function edit(Id,BILLSTATUS){
			console.log("1")
			if(BILLSTATUS != 1){
				$("#"+Id+" #checkbox").tips({
					side:1,
		            msg:'该单据不可修改',
		            bg:'#AE81FF',
		            time:5
		        });
				return;
			}
			var url = 'inorder/goEdit.do?INORDER_ID='+Id;
			document.forms.actionForm.action=url;
	        document.forms.actionForm.submit();
			//siMenu('z191','lm181','修改进货单',url);
		}
		//查看
		function view(Id){
			var url = 'inorder/goView.do?INORDER_ID='+Id;
			document.forms.actionForm.action=url;
	        document.forms.actionForm.submit();
			//siMenu('z191','lm181','查看进货单',url);
		}
		
		
		//导出excel
		function toExcel(){
			var lastStart =  $("#lastStart").val();
			var lastEnd  = $("#lastEnd").val();
			window.location.href='<%=basePath%>inorder/excel.do?lastStart='+lastStart+'&lastEnd='+lastEnd;
		}
		
		
		//点击一行显示详情
		function clickaction(INORDER_ID){
			$.ajax({
		        method:'POST',
		        url:'inorder/inOrderlistBody',
		        data:{'INORDER_ID':INORDER_ID},
		        dataType:'json',
		        success: function (res) {
		        	printinorderhtml(res);
		        }
		    });
		}
		function printinorderhtml(res){
			var strhtml = "";
            if(res.varList.length == 0){
            	strhtml +="<tr class='main_info'>";
            	strhtml +="<td colspan='100' class='center' >没有相关数据</td>";
            	strhtml +="</tr>";
            	
            }else{
            	for(var i = 0; i < res.varList.length; i++){
	            	var html = "";
            		if(res.QX.cha == 1){
	            		html +="<tr id='"+res.varList[i].INORDER_ID+"'>";
	            		html +="<td class='center' style='width: 30px;'>"+(i+1)+"</td>";
	            		html +="<td class='center'>"+res.varList[i].GOODNAME+"</td>";
	            		html +="<td class='center'>"+res.varList[i].GOODCODE_ID+"</td>";
	            		html +="<td class='center'>"+res.varList[i].WHNAME+"</td>";
	            		html +="<td class='center' id='ALLAMOUNT' >"+res.varList[i].UNITPRICE_ID+"</td>";
	            		html +="<td class='center' id='UNPAIDAMOUNT'>"+res.varList[i].PNUMBER+"</td>";
	            		html +="<td class='center' id='PAIDAMOUNT'>"+res.varList[i].NAME+"</td>";
	            		html +="<td class='center' id='THISPAY'>"+res.varList[i].AMOUNT+"</td>";
	            		html +="<td class='center'>"+res.varList[i].NOTE+"</td>";
						html += " </tr>";
	            	}else if(res.QX.cha == 0){
	            		html += "<tr> ";
	            		html += "	<td colspan='100' class='center'>您无权查看</td> ";
					    html += "</tr> ";
	            	}
            		strhtml += html;
	            }
            }
            $("#realtbody").html("");
            $("#realtbody").html(strhtml);
		};
		
		//检索
		function tosearch(){
			top.jzts();
			$("#Form").submit();
		}
		$(function() {
		
			//日期框
			$('.date-picker').datepicker({
				autoclose: true,
				todayHighlight: true
			});
			
			//下拉框
			if(!ace.vars['touch']) {
				$('.chosen-select').chosen({allow_single_deselect:true}); 
				$(window)
				.off('resize.chosen')
				.on('resize.chosen', function() {
					$('.chosen-select').each(function() {
						 var $this = $(this);
						 $this.next().css({'width': $this.parent().width()});
					});
				}).trigger('resize.chosen');
				$(document).on('settings.ace.chosen', function(e, event_name, event_val) {
					if(event_name != 'sidebar_collapsed') return;
					$('.chosen-select').each(function() {
						 var $this = $(this);
						 $this.next().css({'width': $this.parent().width()});
					});
				});
				$('#chosen-multiple-style .btn').on('click', function(e){
					var target = $(this).find('input[type=radio]');
					var which = parseInt(target.val());
					if(which == 2) $('#form-field-select-4').addClass('tag-input-style');
					 else $('#form-field-select-4').removeClass('tag-input-style');
				});
			}
			
			
			//复选框全选控制
			var active_class = 'active';
			$('#simple-table > thead > tr > th input[type=checkbox]').eq(0).on('click', function(){
				var th_checked = this.checked;//checkbox inside "TH" table header
				$(this).closest('table').find('tbody > tr').each(function(){
					var row = this;
					if(th_checked) $(row).addClass(active_class).find('input[type=checkbox]').eq(0).prop('checked', true);
					else $(row).removeClass(active_class).find('input[type=checkbox]').eq(0).prop('checked', false);
				});
			});
		});
		
				
		//跟踪记录
		function chaImg(INORDER_ID){
			 top.jzts();
			 var diag = new top.Dialog();
			 diag.Drag=true;
			 diag.Title ="跟踪记录";
			 diag.URL = '<%=basePath%>customerimg/list.do?INORDER_ID='+INORDER_ID;
			 diag.Width = 800;
			 diag.Height = 650;
			 diag.Modal = false;			//有无遮罩窗口
			 diag. ShowMaxButton = true;	//最大化按钮
		     diag.ShowMinButton = true;		//最小化按钮
			 diag.CancelEvent = function(){ //关闭事件
				diag.close();
			 };
			 diag.show();
		}
		
		//消费记录
		function consume(INORDER_ID){
			 top.jzts();
			 var diag = new top.Dialog();
			 diag.Drag=true;
			 diag.Title ="消费记录";
			 diag.URL = '<%=basePath%>outku/list.do?INORDER_ID='+INORDER_ID;
			 diag.Width = 800;
			 diag.Height = 600;
			 diag.Modal = false;			//有无遮罩窗口
			 diag. ShowMaxButton = true;	//最大化按钮
		     diag.ShowMinButton = true;		//最小化按钮
			 diag.CancelEvent = function(){ //关闭事件
				diag.close();
			 };
			 diag.show();
		}
				
		//删除
		function del(Id,BILLSTATUS,ISSETTLEMENTED){
			if(BILLSTATUS != 1||ISSETTLEMENTED == 1){
				$("#"+Id+" #checkbox").tips({
					side:1,
		            msg:'该单据不可删除',
		            bg:'#AE81FF',
		            time:5
		        });
				return;
			}
			bootbox.confirm("确定要删除吗?", function(result) {
				if(result) {
					top.jzts();
					var url = "<%=basePath%>inorder/delete.do?INORDER_ID="+Id+"&tm="+new Date().getTime();
					$.get(url,function(data){
						tosearch();
					});
				}
			});
		}
	
 		//打印
		function daying(INORDER_ID){
			bootbox.confirm("确定要打印送货单吗?", function(result) {
				if(result) {
					window.location.href='<%=basePath%>daying/createExcel.do?INORDER_ID='+INORDER_ID;
				}
			});
		}


		//审批
		function shenpi(Id,BILLSTATUS){
			if(BILLSTATUS != 1){
				$("#"+Id+" #checkbox").tips({
					side:1,
		            msg:'该单据不可审批',
		            bg:'#AE81FF',
		            time:5
		        });
				return;
			}
			top.jzts();
			var url = "<%=basePath%>inorder/shenpi.do?INORDER_ID="+Id+"&tm="+new Date().getTime();
			$.get(url,function(data){
				tosearch();
			});
		}
		//反审
		function fanshen(Id,ISSETTLEMENTED,BILLSTATUS){
			if(ISSETTLEMENTED == 1){
				$("#"+Id+" #checkbox").tips({
					side:1,
		            msg:'该单据已结算，请先反审结算单',
		            bg:'#AE81FF',
		            time:5
		        });
				return;
			}
			if(BILLSTATUS != 2){
				$("#"+Id+" #checkbox").tips({
					side:1,
		            msg:'该单据未审批，不可反审',
		            bg:'#AE81FF',
		            time:5
		        });
				return;
			}
			bootbox.confirm("确定要反审吗?", function(result) {
				if(result) {
					top.jzts();

					var url = "<%=basePath%>inorder/fanshen.do?INORDER_ID="+Id+"&tm="+new Date().getTime();
					$.get(url,function(data){
						tosearch();
					});
				}
			});
		}
		//批量审批
		function shenpiAll(msg){
			bootbox.confirm(msg, function(result) {
				if(result) {
					var str = '';
					for(var i=0;i < document.getElementsByName('ids').length;i++){
					  if(document.getElementsByName('ids')[i].checked){
					  	if(str=='') str += document.getElementsByName('ids')[i].value;
					  	else str += ',' + document.getElementsByName('ids')[i].value;
					  }
					}
					if(str==''){
						bootbox.dialog({
							message: "<span class='bigger-110'>您没有选择任何内容!</span>",
							buttons: 			
							{ "button":{ "label":"确定", "className":"btn-sm btn-success"}}
						});
						$("#zcheckbox").tips({
							side:1,
				            msg:'点这里全选',
				            bg:'#AE81FF',
				            time:8
				        });
						return;
					}
				}
			});
		};
		//批量操作
		function makeAll(msg){
			bootbox.confirm(msg, function(result) {
				if(result) {
					var str = '';
					for(var i=0;i < document.getElementsByName('ids').length;i++){
					  if(document.getElementsByName('ids')[i].checked){
					  	if(str=='') str += document.getElementsByName('ids')[i].value;
					  	else str += ',' + document.getElementsByName('ids')[i].value;
					  }
					}
					if(str==''){
						bootbox.dialog({
							message: "<span class='bigger-110'>您没有选择任何内容!</span>",
							buttons: 			
							{ "button":{ "label":"确定", "className":"btn-sm btn-success"}}
						});
						$("#zcheckbox").tips({
							side:1,
				            msg:'点这里全选',
				            bg:'#AE81FF',
				            time:8
				        });
						return;
					}else{
						if(msg == '确定要删除选中的数据吗?'){
							top.jzts();
							$.ajax({
								type: "POST",
								url: '<%=basePath%>inorder/deleteAll.do?tm='+new Date().getTime(),
						    	data: {DATA_IDS:str},
								dataType:'json',
								//beforeSend: validateData,
								cache: false,
								success: function(data){
									 $.each(data.list, function(i, list){
											tosearch();
									 });
								}
							});
						}
					}
				}
			});
		};
		
	</script>


</body>
</html>