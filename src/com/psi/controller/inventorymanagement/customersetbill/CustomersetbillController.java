package com.psi.controller.inventorymanagement.customersetbill;

import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.psi.controller.base.BaseController;
import com.psi.entity.Page;
import com.psi.entity.system.User;
import com.psi.service.basedata.payment.PaymentManager;
import com.psi.service.inventorymanagement.customersetbill.CustomersetbillManager;
import com.psi.service.inventorymanagement.salebill.SalebillManager;
import com.psi.service.inventorymanagement.salebill.impl.SalebillService;
import com.psi.service.system.BillCodePsi.BillCodeManager;
import com.psi.util.AppUtil;
import com.psi.util.Const;
import com.psi.util.Jurisdiction;
import com.psi.util.ObjectExcelView;
import com.psi.util.PageData;
import com.psi.util.ProductBillCodeUtil;
import com.psi.util.enumproduct.EnumProductUtil;

/**
 * 说明：客户结算单
 */
@Controller
@RequestMapping(value="/customersetbill")
public class CustomersetbillController extends BaseController {
	
	String menuUrl = "customersetbill/list.do"; //菜单地址(权限用)
	@Resource(name="customersetbillService")
	private CustomersetbillManager customersetbillService;
	@Resource(name="billCodeService")
	private BillCodeManager billCodeService;
	@Resource(name="salebillService")
	private SalebillManager salebillService;
	@Resource(name="paymentService")
	private PaymentManager paymentService;
	
	
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/save")
	@ResponseBody
	public Object save() {
		logBefore(logger, Jurisdiction.getUsername()+"新增customersetbill");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "add")){return null;} //校验权限
		Map<String,Object> map = new HashMap<String,Object>();
		PageData pd = new PageData();
		pd = this.getPageData();
		try {
			customersetbillService.save(pd);
			map.put("msg","success");
		}catch(Exception e) {
			map.put("msg","error");
		}
		return AppUtil.returnObject(new PageData(), map);
	}
	
	/**删除
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/delete")
	public void delete(PrintWriter out) throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"删除customersetbill");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return;} //校验权限
		PageData pd = new PageData();
		pd = this.getPageData();
		customersetbillService.delete(pd);
		out.write("success");
		out.close();
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	@ResponseBody
	public Object edit() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"修改customersetbill");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "edit")){return null;} //校验权限
		Map<String,Object> map = new HashMap<String,Object>();
		PageData pd = new PageData();
		pd = this.getPageData();
		try {
			customersetbillService.edit(pd);
			map.put("msg","success");
		}catch(Exception e) {
			map.put("msg","error");
		}
		return AppUtil.returnObject(new PageData(), map);
	}
	
	@Autowired
	private ProductBillCodeUtil productBillCodeUtil;
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	public ModelAndView list(Page page) throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"列表customersetbill");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;} //校验权限(无权查看时页面会有提示,如果不注释掉这句代码就无法进入列表页面,所以根据情况是否加入本句代码)
		ModelAndView mv = this.getModelAndView();
				PageData pd = new PageData();
		pd = this.getPageData();
		String keywords = pd.getString("keywords");				//关键词检索条件
		if(null != keywords && !"".equals(keywords)){
			pd.put("keywords", keywords.trim());
		}
		String lastLoginStart = pd.getString("lastStart");	//开始时间
		String lastLoginEnd = pd.getString("lastEnd");		//结束时间
		if(lastLoginStart != null && !"".equals(lastLoginStart)){
			pd.put("lastStart", lastLoginStart+" 00:00:00");
		}
		if(lastLoginEnd != null && !"".equals(lastLoginEnd)){
			pd.put("lastEnd", lastLoginEnd+" 23:59:59");
		} 
		String billstatus = pd.getString("billstatus");
		if(billstatus == null) {
			billstatus = "1";
		}
		pd.put("billstatus", billstatus);
		page.setPd(pd);
		
		List<PageData>	customerSetList = customersetbillService.list(page);	//列出customersetbill列表
		mv.setViewName("inventorymanagement/customersetbill/customersetbill_list");
		mv.addObject("customerSetList", customerSetList);
		mv.addObject("pd", pd);
		mv.addObject("QX",Jurisdiction.getHC());	//按钮权限
		return mv;
	}
	
	


	/**明细列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/listdetail")
	public ModelAndView listdetail(Page page) throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"客户结算明细列表");
		String menuUrl = "customersetbill/listdetail.do"; //菜单地址(权限用)
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;} //校验权限(无权查看时页面会有提示,如果不注释掉这句代码就无法进入列表页面,所以根据情况是否加入本句代码)
		ModelAndView mv = this.getModelAndView();
				PageData pd = new PageData();
		pd = this.getPageData();
		String keywords = pd.getString("keywords");				//关键词检索条件
		if(null != keywords && !"".equals(keywords)){
			pd.put("keywords", keywords.trim());
		}
		
		
		String lastLoginStart = pd.getString("lastStart");	//开始时间
		String lastLoginEnd = pd.getString("lastEnd");		//结束时间
		if(lastLoginStart != null && !"".equals(lastLoginStart)){
			pd.put("lastStart", lastLoginStart+" 00:00:00");
		}
		if(lastLoginEnd != null && !"".equals(lastLoginEnd)){
			pd.put("lastEnd", lastLoginEnd+" 23:59:59");
		} 
	
		
		page.setPd(pd);
		
		List<PageData>	customerSetList = customersetbillService.listdetail(page);	//列出customersetbill列表
		mv.setViewName("inventorymanagement/customersetbill/customersetbill_listdetail");
		mv.addObject("customerSetList", customerSetList);
		mv.addObject("pd", pd);
		mv.addObject("QX",Jurisdiction.getHC());	//按钮权限
		return mv;
	}
	
	
	/**列表(弹窗选择用)
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/windowsList")
	public ModelAndView windowsList(Page page) throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"列表customersetbill");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;} //校验权限(无权查看时页面会有提示,如果不注释掉这句代码就无法进入列表页面,所以根据情况是否加入本句代码)
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		String keywords = pd.getString("keywords");				//关键词检索条件
		if(null != keywords && !"".equals(keywords)){
			pd.put("keywords", keywords.trim());
		}
		String lastLoginStart = pd.getString("lastStart");	//开始时间
		String lastLoginEnd = pd.getString("lastEnd");		//结束时间
		if(lastLoginStart != null && !"".equals(lastLoginStart)){
			pd.put("lastStart", lastLoginStart+" 00:00:00");
		}
		if(lastLoginEnd != null && !"".equals(lastLoginEnd)){
			pd.put("lastEnd", lastLoginEnd+" 00:00:00");
		} 
		page.setPd(pd);
		List<PageData>	varList = customersetbillService.list(page);	//列出customersetbill列表
		mv.setViewName("inventorymanagement/customersetbill/windows_customersetbill_list");
		mv.addObject("varList", varList);
		mv.addObject("pd", pd);
		mv.addObject("QX",Jurisdiction.getHC());	//按钮权限
		return mv;
	}
	
	/**去新增页面
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/goAdd")
	public ModelAndView goAdd()throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		Session session = Jurisdiction.getSession();
		User user = (User)session.getAttribute(Const.SESSION_USER);
		pd.put("PSI_NAME", user.getNAME());	//用户主键
//		pd.put("LDATE", new Date().getTime());//录单时间
		mv.setViewName("inventorymanagement/customersetbill/customersetbill_edit");
		mv.addObject("msg", "save");
		mv.addObject("pd", pd);
		mv.addObject("varListL", EnumProductUtil.productDistributionModeList());
		mv.addObject("QX",Jurisdiction.getHC());	//按钮权限
		return mv;
	}	
	
	 /**去修改页面
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/goEdit")
	public ModelAndView goEdit()throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = customersetbillService.findById(pd);	//根据ID读取
		mv.setViewName("inventorymanagement/customersetbill/customersetbill_edit");
		mv.addObject("msg", "edit");
		mv.addObject("pd", pd);
		mv.addObject("varListL", EnumProductUtil.productDistributionModeList());
		mv.addObject("QX",Jurisdiction.getHC());	//按钮权限
		return mv;
	}	
	
	 /**查看页面
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/goView")
	public ModelAndView goView()throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = customersetbillService.findById(pd);	//根据ID读取
		mv.setViewName("inventorymanagement/customersetbill/customersetbill_view");
		mv.addObject("msg", "edit");
		mv.addObject("pd", pd);
		mv.addObject("varListL", EnumProductUtil.productDistributionModeList());
		return mv;
	}
	
	 /**批量删除
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteAll")
	@ResponseBody
	public Object deleteAll() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"批量删除customersetbill");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return null;} //校验权限
		PageData pd = new PageData();		
		Map<String,Object> map = new HashMap<String,Object>();
		pd = this.getPageData();
		List<PageData> pdList = new ArrayList<PageData>();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(null != DATA_IDS && !"".equals(DATA_IDS)){
			String[] ids = DATA_IDS.split(",");
			StringBuffer idstr = new StringBuffer("");
			for(int i=0;i<ids.length;i++) {
				idstr.append("'"+ids[i]+"',");
			}
			customersetbillService.deleteAll(idstr.toString().substring(0,idstr.toString().length()-1),(String)pd.get("PK_SOBOOKS"));
			pd.put("msg", "ok");
		}else{
			pd.put("msg", "no");
		}
		pdList.add(pd);
		map.put("list", pdList);
		return AppUtil.returnObject(pd, map);
	}
	/**批量审批
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/approvalAll")
	@ResponseBody
	public Object approvalAll() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"批量审批customersetbill");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "edit")){return null;} //校验权限
		PageData pd = new PageData();		
		Map<String,Object> map = new HashMap<String,Object>();
		pd = this.getPageData();
		try{
			String DATA_IDS = pd.getString("DATA_IDS");
			if(null != DATA_IDS && !"".equals(DATA_IDS)){
				String[] ids = DATA_IDS.split(",");
				customersetbillService.approvalAll(ids);
				map.put("msg", "success");
			}else{
				map.put("msg", "error");
			}
		}catch(Exception e) {
			map.put("msg", "error");
		}
		return AppUtil.returnObject(pd, map);
	}
	/**单张审批
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/approvalone")
	@ResponseBody
	public Object aapprovalone() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"单张审批customersetbill");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "edit")){return null;} //校验权限
		PageData pd = new PageData();		
		Map<String,Object> map = new HashMap<String,Object>();
		pd = this.getPageData();
		try {
			customersetbillService.approvalone(pd);
			map.put("msg", "success");
		}catch(Exception e) {
			map.put("msg", "error");
		}
		return AppUtil.returnObject(pd, map);
	}
	/**单张反审
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/unapprovalone")
	@ResponseBody
	public Object unapprovalone() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"单张反审customersetbill");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "edit")){return null;} //校验权限
		PageData pd = new PageData();		
		Map<String,Object> map = new HashMap<String,Object>();
		pd = this.getPageData();
		try {
			customersetbillService.unapprovalone(pd);
			map.put("msg", "success");
		}catch(Exception e) {
			map.put("msg", e.getMessage());
		}
		return AppUtil.returnObject(pd, map);
	}
	
	
	
	/**批量结算
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/settleAll")
	@ResponseBody
	public Object settleAll() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"批量结算inorder");
		//menuUrl 应该为进货单列表的！！！！！！！
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "edit")){return null;} //校验权限
		PageData pd = new PageData();		
		Map<String,Object> map = new HashMap<String,Object>();
		pd = this.getPageData();
		List<PageData> pdList = new ArrayList<PageData>();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(null != DATA_IDS && !"".equals(DATA_IDS)){
			String[] ids = DATA_IDS.split(",");
			StringBuffer idstr = new StringBuffer("");
			for(int i=0;i<ids.length;i++) {
				idstr.append("'"+ids[i]+"',");
			}
			customersetbillService.settleAll(idstr.toString().substring(0,idstr.toString().length()-1),(String)pd.get("PK_SOBOOKS"));
			pd.put("msg", "ok");
		}else{
			pd.put("msg", "no");
		}
		pdList.add(pd);
		map.put("list", pdList);
		return AppUtil.returnObject(pd, map);
	}
	
	/**导出到excel
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/excel")
	public ModelAndView exportExcel() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"导出供应商结算单到excel");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;}
		ModelAndView mv = new ModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		Map<String,Object> dataMap = new HashMap<String,Object>();
		List<String> titles = new ArrayList<String>();
		titles.add("单据编号");	//1
		titles.add("客户名称");	//2
		titles.add("付款方式");	//3
		titles.add("总金额");	//4
		titles.add("应付金额");	//5
		titles.add("实付金额");	//6
		titles.add("单据状态");	//7
		titles.add("发票类型");	//8
		titles.add("票号");	//9
		titles.add("备注");	//10
		titles.add("日期");	//11
		titles.add("经手人");	//12
		dataMap.put("titles", titles);
		
		String lastLoginStart = pd.getString("lastStart");	//开始时间
		String lastLoginEnd = pd.getString("lastEnd");		//结束时间
		int flag1=1;
		if(lastLoginStart != null && !"".equals(lastLoginStart)){
			pd.put("lastStart", lastLoginStart+" 00:00:00");
			flag1=0;
		}
		int flag2=1;
		if(lastLoginEnd != null && !"".equals(lastLoginEnd)){
			pd.put("lastEnd", lastLoginEnd+" 23:59:59");
			flag2=0;
		} 
		if(flag1==0&&flag2==0){
			pd.put("flag", 1);
		}
		
		List<PageData> varOList = customersetbillService.listAllToExcel(pd);
		
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("BILLCODE"));	    //1
			vpd.put("var2", varOList.get(i).getString("CUATOMERNAME"));	    //2
			vpd.put("var3", varOList.get(i).getString("PAYMETHODNAME"));	//3
			vpd.put("var4", varOList.get(i).get("PAYABLEALLAM").toString());	    //4
			vpd.put("var5", varOList.get(i).get("PAYABLEAMOUNT").toString());	//5
			vpd.put("var6", varOList.get(i).get("PAYMENTAMOUNT").toString());	    //6
			
			String BILLSTATUS = varOList.get(i).getString("BILLSTATUS");
			if(BILLSTATUS.equals("1")){
				vpd.put("var7", "未审核");	    //7
			}else if(BILLSTATUS.equals("2")){
				vpd.put("var7", "已审核");	    //7
			}else if(BILLSTATUS.equals("3")){
				vpd.put("var7", "作废");	    //7
			}
			
			vpd.put("var8", varOList.get(i).getString("INVOICETYPE"));	    //8
			vpd.put("var9", varOList.get(i).getString("BILLNO"));	    //9
			vpd.put("var10", varOList.get(i).getString("NOTE"));	    //10
			vpd.put("var11", varOList.get(i).getString("LDATE"));	    //11
			vpd.put("var12",varOList.get(i).getString("NAME"));   //12
		
			varList.add(vpd);
		}
		dataMap.put("title", "客户结算单"); 
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}
	
	
	
	/**导出到excel
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/excelDetail")
	public ModelAndView excelDetail() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"导出客户结算明细单到excel");
		String menuUrl = "customersetbill/listdetail.do"; //菜单地址(权限用)
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;}
		ModelAndView mv = new ModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		Map<String,Object> dataMap = new HashMap<String,Object>();
		List<String> titles = new ArrayList<String>();
		titles.add("单据编号");	//1
		titles.add("单据类型");	//2
		titles.add("录单日期");	//3
		titles.add("客户名称");	//4
		titles.add("经手人");	//5
		titles.add("支付方式");	//6
		titles.add("金额");	//7
		dataMap.put("titles", titles);
		
		String lastLoginStart = pd.getString("lastStart");	//开始时间
		String lastLoginEnd = pd.getString("lastEnd");		//结束时间
		int flag1=1;
		if(lastLoginStart != null && !"".equals(lastLoginStart)){
			pd.put("lastStart", lastLoginStart+" 00:00:00");
			flag1=0;
		}
		int flag2=1;
		if(lastLoginEnd != null && !"".equals(lastLoginEnd)){
			pd.put("lastEnd", lastLoginEnd+" 23:59:59");
			flag2=0;
		} 
		if(flag1==0&&flag2==0){
			pd.put("flag", 1);
		}
		
		List<PageData>	varOList = customersetbillService.excelDetail(pd);	//列出customersetbill列表
		
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("BILLCODE"));	    //1
			vpd.put("var2", "客户结算单");	    //2
			vpd.put("var3", varOList.get(i).getString("LDATE"));	//3
			vpd.put("var4", varOList.get(i).getString("CUATOMERNAME"));	    //4
			vpd.put("var5", varOList.get(i).getString("NAME"));	//5
			vpd.put("var6", String.valueOf(varOList.get(i).get("PAYMENTAMOUNT")));	    //8
			vpd.put("var7", varOList.get(i).getString("PAYMETHODNAME"));	    //9
	
			varList.add(vpd);
		}
		dataMap.put("title", "客户结算明细单"); 
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}
	
	
	@InitBinder
	public void initBinder(WebDataBinder binder){
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(format,true));
	}
	
	@RequestMapping("/listCustomterbillByPay")
	public ModelAndView listCustomterbillByPay(Page page) throws Exception{
	/*	logBefore(logger, Jurisdiction.getUsername()+"查看根据客户罗列支付方式的金额");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;}*/
		PageData pd= new PageData();
		pd=this.getPageData();
		ModelAndView mv =  new ModelAndView();
		if(pd.getString("PAYMETHOD_ID")!=null){
			mv.addObject("paymentId",pd.getString("PAYMETHOD_ID"));
		}
		String keywords = pd.getString("keywords");				//关键词检索条件
		if(null != keywords && !"".equals(keywords)){
			pd.put("keywords", keywords.trim());
		}
		String lastLoginStart = pd.getString("lastStart");	//开始时间
		String lastLoginEnd = pd.getString("lastEnd");		//结束时间
		int start=1;
		int end=1;
		if(lastLoginStart != null && !"".equals(lastLoginStart)){
			pd.put("lastStart", lastLoginStart+" 00:00:00");
			start=0;
		}
		if(lastLoginEnd != null && !"".equals(lastLoginEnd)){
			pd.put("lastEnd", lastLoginEnd+" 23:59:59");
			end=0;
		} 
		
		if(start==1&&end==1){ //如果没有输入时间，默认当前时间
			SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd");
			String date=sdf.format(new Date());
			pd.put("date", date);
		}
		List<PageData> lpd=customersetbillService.listCustomterbillByPay(pd);
		List<String> list= new ArrayList();
		page.setPd(pd);
		if(lpd!=null&&lpd.size()!=0){
			for(int i = 0;i<lpd.size();i++){
				String SALEBILL_IDS=lpd.get(i).getString("SALEBILL_IDS"); //切割SALEBILL_IDS
				String[] str=SALEBILL_IDS.split(",");
				lpd.get(i).put("SALEBILLIDS", str[0]);
				list.add(str[0]);
			}
		}
		page.setList(list);
		List<PageData> list1 = new ArrayList();
		if(lpd.size()!=0 &&lpd!=null){
		      list1=salebillService.listSalebillByID(page);
		}
		List<PageData> list2=new ArrayList();
		if(list1!=null && list1.size()!=0){
			for(int i=0;i<lpd.size();i++){
				for(int j=0;j<list1.size();j++){
					if(lpd.get(i).getString("SALEBILLIDS").equals("'"+list1.get(j).getString("SALEBILL_ID")+"'")){
						lpd.get(i).put("CUATOMERNAME", list1.get(j).getString("CUATOMERNAME"));
						list2.add(lpd.get(i));
						break;
					}
				
				}
			}
		}
		List<PageData>  paymentlist =paymentService.listAll(pd);
		mv.setViewName("inventorymanagement/customersetbill/customterbypay");
		mv.addObject("paymentlist",paymentlist);
		mv.addObject("varlist",list2);
		mv.addObject("pd", pd);
		mv.addObject("QX",Jurisdiction.getHC());	//按钮权限
		return mv;
	}
	
}
