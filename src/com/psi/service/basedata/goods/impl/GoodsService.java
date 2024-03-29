package com.psi.service.basedata.goods.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.psi.dao.DaoSupport;
import com.psi.entity.Page;
import com.psi.entity.basedata.GoodsType;
import com.psi.service.basedata.goods.GoodsManager;
import com.psi.util.PageData;

/**
 * 说明： 商品管理
 */
@Service("goodsService")
public class GoodsService implements GoodsManager{

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	/**
	 * 获取所有数据并填充每条数据的子级列表(递归处理)
	 * @param MENU_ID
	 * @return
	 * @throws Exception
	 */
	public List<GoodsType> listAllDict(Map<String,String> parentIdAndPK_SOBOOKS) throws Exception {
		List<GoodsType> dictList = this.listSubDictByParentId(parentIdAndPK_SOBOOKS);
		Map<String,String> map = new HashMap<String, String>();
		map.put("PK_SOBOOKS", parentIdAndPK_SOBOOKS.get("PK_SOBOOKS"));
		for(GoodsType dict : dictList){
			dict.setTreeurl("goods/list.do?GOODTYPE_ID="+dict.getGOODTYPE_ID());
			//dict.setSubDict(this.listAllDict(dict.getGOODTYPE_ID()));
			map.put("PARENTS", dict.getGOODTYPE_ID());
			dict.setSubDict(this.listAllDict(map));
			dict.setTarget("treeFrame");
		}
		return dictList;
	}
	/**
	 * !!!入库单专用!!!
	 * 全部
	 * 获取所有数据并填充每条数据的子级列表(递归处理)
	 * @param MENU_ID
	 * @return
	 * @throws Exception
	 */
	public List<GoodsType> inOrderListAllDict(Map<String,String> parentIdAndPK_SOBOOKS) throws Exception {
		List<GoodsType> dictList = this.listSubDictByParentId(parentIdAndPK_SOBOOKS);
		Map<String,String> map = new HashMap<String, String>();
		map.put("PK_SOBOOKS", parentIdAndPK_SOBOOKS.get("PK_SOBOOKS"));
		for(GoodsType dict : dictList){
			dict.setTreeurl("inorder/goodslist.do?GOODTYPE_ID="+dict.getGOODTYPE_ID());
			//dict.setSubDict(this.listAllDict(dict.getGOODTYPE_ID()));
			map.put("PARENTS", dict.getGOODTYPE_ID());
			dict.setSubDict(this.inOrderListAllDict(map));
			dict.setTarget("treeFrame");
		}
		return dictList;
	}
	
	/**
	 * !!!销售单   仓库调拨!!!
	 * 只获取仓库中存在的
	 * 获取所有数据并填充每条数据的子级列表(递归处理)
	 * @param MENU_ID
	 * @return
	 * @throws Exception
	 */
	public List<GoodsType> salebillListAllDict(Map<String,String> parentIdAndPK_SOBOOKS) throws Exception {
		List<GoodsType> dictList = this.listSubDictByParentId(parentIdAndPK_SOBOOKS);
		Map<String,String> map = new HashMap<String, String>();
		map.put("PK_SOBOOKS", parentIdAndPK_SOBOOKS.get("PK_SOBOOKS"));
		map.put("WAREHOUSE_ID", parentIdAndPK_SOBOOKS.get("WAREHOUSE_ID"));
		for(GoodsType dict : dictList){
			dict.setTreeurl("salebill/goodslist.do?GOODTYPE_ID="+dict.getGOODTYPE_ID()+"&WAREHOUSE_ID=" + parentIdAndPK_SOBOOKS.get("WAREHOUSE_ID"));
			//dict.setSubDict(this.listAllDict(dict.getGOODTYPE_ID()));
			map.put("PARENTS", dict.getGOODTYPE_ID());
			dict.setSubDict(this.salebillListAllDict(map));
			dict.setTarget("treeFrame");
		}
		return dictList;
	}
	/**
	 * !!!库存盘点!!!
	 * 
	 * 获取所有数据并填充每条数据的子级列表(递归处理)
	 * @param MENU_ID
	 * @return
	 * @throws Exception
	 */
	public List<GoodsType> stockCheckListAllDict(Map<String,String> parentIdAndPK_SOBOOKS) throws Exception {
		List<GoodsType> dictList = this.listSubDictByParentId(parentIdAndPK_SOBOOKS);
		Map<String,String> map = new HashMap<String, String>();
		map.put("PK_SOBOOKS", parentIdAndPK_SOBOOKS.get("PK_SOBOOKS"));
		map.put("WAREHOUSE_ID", parentIdAndPK_SOBOOKS.get("WAREHOUSE_ID"));
		for(GoodsType dict : dictList){
			dict.setTreeurl("stockcheck/goodslist.do?GOODTYPE_ID="+dict.getGOODTYPE_ID()+"&WAREHOUSE_ID=" + parentIdAndPK_SOBOOKS.get("WAREHOUSE_ID"));
			//dict.setSubDict(this.listAllDict(dict.getGOODTYPE_ID()));
			map.put("PARENTS", dict.getGOODTYPE_ID());
			dict.setSubDict(this.stockCheckListAllDict(map));
			dict.setTarget("treeFrame");
		}
		return dictList;
	}
	
	/**
	 * 通过ID获取其子级列表
	 * @param parentId
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<GoodsType> listSubDictByParentId(Map<String,String> parentIdAndPK_SOBOOKS) throws Exception {
		return (List<GoodsType>) dao.findForList("GoodsTypeMapper.listSubDictByParentId", parentIdAndPK_SOBOOKS);
	}
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		dao.save("GoodsMapper.save", pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		dao.update("GoodsMapper.delete", pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		
		//修改商品编号（填补之前把商品编号当商品主键使用的坑）
		//判断商品编号是否有变化
		PageData pageData = (PageData)dao.findForObject("GoodsMapper.findById", pd);
		if(((String)pageData.get("GOODCODE")).equals((String)pd.get("GOODCODE"))) {//没有，不用处理
		}else {//有，把引用到商品编号的所有表对应的商品编号都要修改
			updateGOODCODE((String)pageData.get("GOODCODE"), (String)pd.get("GOODCODE"), pd);
		}
		dao.update("GoodsMapper.edit", pd);
	}
	
	private void updateGOODCODE(String oldGOODCODE, String newGOODCODE, PageData pd) throws Exception {
		PageData pageDate = new PageData();
		pageDate.put("GOODCODE", oldGOODCODE);
		pageDate.put("newGOODCODE", newGOODCODE);
		pageDate.put("PK_SOBOOKS", pd.get("PK_SOBOOKS"));
		dao.update("StockMapper.editGOODCODE", pageDate);
		dao.update("IncomerecordMapper.editGOODCODE", pageDate);
		dao.update("InOrderBodyMapper.editGOODCODE", pageDate);
		dao.update("InOrderAndSuppsetBackMapper.editGOODCODE", pageDate);
		dao.update("SalebillBodyMapper.editGOODCODE", pageDate);
		dao.update("SalebillAndCustomersetBackMapper.editGOODCODE", pageDate);
		dao.update("SalePriceRecordMapper.editGOODCODE", pageDate);
		dao.update("StockCheckBodyMapper.editGOODCODE", pageDate);
		dao.update("WhallocateBodyMapper.editGOODCODE", pageDate);
	}
	/**修改库存
	 * @param pd
	 * @throws Exception
	 */
	public void editKuCun(PageData pd)throws Exception{
		dao.update("GoodsMapper.editKuCun", pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> list(Page page)throws Exception{
		//本来的列表
		List<PageData> result;
		try{
		 result = (List<PageData>)dao.findForList("GoodsMapper.datalistPage", page);
		}catch(Exception e){
			return null;
		}
		//给列表的每个商品添加一个  仓库ID，仓库名，库存#仓库ID，仓库名，库存#  格式的 WAREHOUSE_ID_NAME_STOCK 值
		PageData map = new PageData(); //查询条件
		PageData WareHouse = new PageData(); //仓库
		PageData temp = new PageData(); //临时保存查询到的仓库ID，仓库名，库存
		map.put("PK_SOBOOKS", page.getPd().get("PK_SOBOOKS"));
		
		//循环每个商品添加 WAREHOUSE_ID_NAME_STOCK
		for (PageData pageData : result) {
			map.put("GOOD_ID", pageData.get("GOODCODE"));
			String WH = (String)pageData.get("WAREHOUSE_IDs");
			String[] strings = null ;
			if(WH!=null){
				strings = WH.split(",");
			}
			String WAREHOUSE_ID_NAME_STOCK = "";
			if(strings!=null){
				for (String WID : strings) {
					map.put("WAREHOUSE_ID", WID);
					temp = (PageData)dao.findForObject("StockMapper.findByWarehouseAndGood", map);
					WareHouse = (PageData)dao.findForObject("WarehouseMapper.findById", map);
					Integer stock = 0;
					String  WHNAME = (String) WareHouse.get("WHNAME");
					if(temp != null) {
						stock =  (Integer) temp.get("STOCK");
					}
					WAREHOUSE_ID_NAME_STOCK = WAREHOUSE_ID_NAME_STOCK + WID + "," + WHNAME +"," + stock + "#";
				
				}
			}else{
				List<PageData> lpd=(List<PageData>)dao.findForList("WarehouseMapper.listAll", page.getPd());
				if(lpd!=null&&lpd.size()!=0){
					for(int i=0;i<lpd.size();i++){
						WAREHOUSE_ID_NAME_STOCK=WAREHOUSE_ID_NAME_STOCK+lpd.get(i).getString("WAREHOUSE_ID")+","+lpd.get(i).getString("WHNAME")+",0#";
					}
				}
			}
			pageData.put("WAREHOUSE_ID_NAME_STOCK", WAREHOUSE_ID_NAME_STOCK);
		}
		return result;
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> listAll(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("GoodsMapper.listAll", pd);
	}
	
	/**通过产品编码
	 * @param pd
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> listByBm(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("GoodsMapper.listByBm", pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return (PageData)dao.findForObject("GoodsMapper.findById", pd);
	}
	
	/**通过id获取数据(查看详细信息)
	 * @param pd
	 * @throws Exception
	 */
	public PageData findByIdToCha(PageData pd)throws Exception{
		return (PageData)dao.findForObject("GoodsMapper.findByIdToCha", pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		dao.delete("GoodsMapper.deleteAll", ArrayDATA_IDS);
	}
	
	/**商品入库，增加库存
	 * @param pd
	 * @throws Exception
	 */
	public void editZCOUNT(PageData pd)throws Exception{
		dao.update("GoodsMapper.editZCOUNT", pd);
	}
	
	/**
	 * 库存预警
	 * 检查商品的库存是否低于下限
	 * 返回低于下限的商品的列表  包括字段： 名称、编号、当前库存、库存下限
	 */
	@Override
	public List<PageData> checkGoodsStockDownNum(PageData pd) throws Exception {
		return (List<PageData>)dao.findForList("GoodsMapper.checkGoodsStockDownNum", pd);
	}
	@Override
	public Object findByGOODCODE(PageData pd) throws Exception {
		// TODO Auto-generated method stub
		return (PageData)dao.findForObject("GoodsMapper.findByGOODCODE", pd);
	}
	@Override
	public String findByname(PageData pd) throws Exception {
		// TODO Auto-generated method stub
		return (String)dao.findForObject("GoodsMapper.findByUnitName", pd);
	}
	@Override
	public void saveUnit(PageData pd) throws Exception {
		dao.save("GoodsMapper.saveUnit", pd);
	}
	@Override
	public void saveGoods(PageData pd) throws Exception {
		// TODO Auto-generated method stub
		dao.save("GoodsMapper.saveGoods", pd);
	}
	@Override
	public Object findByCode(PageData pd) throws Exception {
		
		return (PageData)dao.findForObject("GoodsMapper.findByCode", pd);
	}
	@Override
	public List<PageData> listAllDetail(PageData pd) throws Exception {
		// TODO Auto-generated method stub
		return (List<PageData>) dao.findForList("GoodsMapper.listAllDetail", pd);
	}
	@Override
	public String findPKBYName(PageData pd) throws Exception {
		// TODO Auto-generated method stub
		return (String)dao.findForObject("GoodsMapper.findPKBYName", pd);
	}
	@Override
	public void editPic(PageData pd) throws Exception {
		// TODO Auto-generated method stub
		dao.update("GoodsMapper.savePic", pd);
	}

	@Override
	public String findbyTypeId(PageData pd) throws Exception {
		// TODO Auto-generated method stub
		return (String)dao.findForObject("GoodsMapper.findbyTypeId", pd);
	}
	@Override
	public List<PageData> listAllInorderSale(Page page) throws Exception {
		// TODO Auto-generated method stub
		return (List<PageData>)dao.findForList("GoodsMapper.datalistPageByAllInorderSale", page);
	}
	
	
}

