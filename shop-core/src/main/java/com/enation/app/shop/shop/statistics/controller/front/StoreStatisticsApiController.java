package com.enation.app.shop.shop.statistics.controller.front;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.enation.app.shop.goods.service.ICategoryManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.enation.app.shop.shop.statistics.service.IB2b2cOperatorStatisticsManager;
import com.enation.app.shop.statistics.service.IB2b2cGoodsStatisticsManager;
import com.enation.eop.SystemSetting;
import com.enation.framework.action.GridJsonResult;
import com.enation.framework.action.JsonResult;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.database.Page;
import com.enation.framework.util.DateUtil;
import com.enation.framework.util.JsonResultUtil;
import com.enation.framework.util.StringUtil;

import net.sf.json.JSONArray;

/**
 * (商家中心统计api)
 *
 * @author zjp
 * @version v1.0
 * @since v6.2
 * 2017年3月17日 上午11:03:15
 */
@Controller
@RequestMapping("/api/store-statistics")
public class StoreStatisticsApiController {
    @Autowired
    private IB2b2cOperatorStatisticsManager b2b2cOperatorStatisticsManager;//运营报告管理类
    @Autowired
    private IB2b2cGoodsStatisticsManager b2b2cBackendGoodsStatisticsManager;
    @Autowired
    private ICategoryManager categoryManager;
    @Autowired
    private com.enation.app.shop.shop.statistics.service.IB2b2cGoodsStatisticsManager b2b2cGoodsStatisticsManager;
    protected final Logger logger = Logger.getLogger(getClass());

    /**
     * 获取销售统计  下单金额echarts-json格式数据
     *
     * @param year       查询年份
     * @param month      查询月份
     * @param cycle_type 查询周期
     * @param storeid    店铺id
     * @return json格式数据集
     */
    @ResponseBody
    @RequestMapping("get-sales-money")
    public JsonResult getSalesMoney(Integer year, Integer month, Integer cycle_type, Integer storeid) {
        String salesMoney = (String) this.b2b2cOperatorStatisticsManager.getSalesMoney(year, month, cycle_type, storeid);
        int indexOf = salesMoney.indexOf("{", 1);
        int length = salesMoney.length();
        salesMoney = salesMoney.substring(indexOf, length - 1);
        return JsonResultUtil.getObjectJson(salesMoney);
    }

    /**
     * 获取销售统计  下单金额 datagrid-json格式数据
     *
     * @param year       查询年份
     * @param month      查询月份
     * @param cycle_type 查询周期
     * @param storeid    店铺id
     * @return json格式数据集
     */
    @ResponseBody
    @RequestMapping("get-sales-money-dgjson")
    public JsonResult getSalesMoneyDgjson(Integer year, Integer month, Integer cycle_type, Integer storeid) {
        int page = this.getPage();
        int pageSize = this.getPageSize();
        GridJsonResult salesMoneyDgjson = this.b2b2cOperatorStatisticsManager.getSalesMoneyDgjson(year, month, cycle_type, storeid, page, pageSize);
        return JsonResultUtil.getObjectJson(salesMoneyDgjson);

    }

    /**
     * 获取销售统计 下单量echarts-json格式数据集
     *
     * @param year       查询年份
     * @param month      查询月份
     * @param cycle_type 查询周期
     * @param storeid    店铺id
     * @return json格式数据集
     */
    @ResponseBody
    @RequestMapping("get-sales-num")
    public JsonResult getSalesNum(Integer year, Integer month, Integer cycle_type, Integer storeid) {
        String salesNum = (String) this.b2b2cOperatorStatisticsManager.getSalesNum(year, month, cycle_type, storeid);
        int indexOf = salesNum.indexOf("{", 1);
        int length = salesNum.length();
        salesNum = salesNum.substring(indexOf, length - 1);
        return JsonResultUtil.getObjectJson(salesNum);
    }

    /**
     * 获取区域分析 下单会员数  highchart-json格式数据
     *
     * @param cycle_type 查询周期
     * @param year       查询年份
     * @param month      查询月份
     * @param storeid    店铺id
     * @return json格式数据集
     */
    @ResponseBody
    @RequestMapping("region-member-list-json")
    public JsonResult regionMemberListJson(Integer cycle_type, Integer year, Integer month, Integer storeid) {
        try {
            String regionStatistics = b2b2cOperatorStatisticsManager.getRegionStatistics(1, cycle_type, year, month, storeid);
            regionStatistics = regionStatistics.replace("\n", "");
            return JsonResultUtil.getObjectJson(regionStatistics);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResultUtil.getErrorJson("获取json失败！");
        }
    }

    /**
     * 获取区域分析 会员下单金额 highchart-json格式数据
     *
     * @param cycle_type 查询周期
     * @param year       查询年份
     * @param month      查询月份
     * @param storeid    店铺id
     * @return json格式数据集
     */
    @ResponseBody
    @RequestMapping("region-money-list-json")
    public JsonResult regionMoneyListJson(Integer cycle_type, Integer year, Integer month, Integer storeid) {
        try {
            String regionStatistics = b2b2cOperatorStatisticsManager.getRegionStatistics(2, cycle_type, year, month, storeid);
            regionStatistics = regionStatistics.replace("\n", "");
            return JsonResultUtil.getObjectJson(regionStatistics);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResultUtil.getErrorJson("获取json失败！");
        }
    }

    /**
     * 获取区域分析 会员下单数量highchart-json格式数据
     *
     * @param cycle_type 查询周期
     * @param year       查询年份
     * @param month      查询月份
     * @param storeid    店铺id
     * @return json格式数据集
     */
    @ResponseBody
    @RequestMapping("region-num-list-json")
    public JsonResult regionNumListJson(Integer cycle_type, Integer year, Integer month, Integer storeid) {
        try {
            String regionStatistics = b2b2cOperatorStatisticsManager.getRegionStatistics(2, cycle_type, year, month, storeid);
            regionStatistics = regionStatistics.replace("\n", "");
            return JsonResultUtil.getObjectJson(regionStatistics);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResultUtil.getErrorJson("获取json失败！");
        }
    }

    /**
     * 读取收藏列表
     *
     * @param storeId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/get-collect-json")
    public JsonResult getCollectJson(Integer storeId) {
        Page webPage = this.b2b2cBackendGoodsStatisticsManager.getCollectPage(this.getPage(), this.getPageSize(), storeId);
        return JsonResultUtil.getObjectJson(webPage);
    }

    /**
     * 读取收藏甘特图Json
     *
     * @param storeId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/get-collect-chart-json")
    public JsonResult getCollectChartJson(Integer storeId) {
        Page webPage = this.b2b2cBackendGoodsStatisticsManager.getCollectPage(1, 50, storeId);
        List list = (List) webPage.getResult();
        String string = JSONArray.fromObject(list).toString();
        return JsonResultUtil.getObjectJson(string);
    }

    @ResponseBody
    @RequestMapping(value = "/cat-list-json", produces = MediaType.APPLICATION_JSON_VALUE)
    public Object addStorelistJson() {
        List addlist = categoryManager.listAllChildren(0);
        String s = JSONArray.fromObject(addlist).toString();
        s = s.replace("name", "text").replace("category_id", "id");
        return s;
    }

    /**
     * 获取购买分析  客单价分布和购买时段分布highchart-json格式数据
     *
     * @param cycle_type 查询周期
     * @param year       查询年份
     * @param month      查询月份
     * @param storeid    店铺id
     * @return json格式数据集
     */
    @ResponseBody
    @RequestMapping("buy-list-json")
    public JsonResult buyListJson(Integer cycle_type, Integer year, Integer month, Integer storeid) {
        Object buyStatistics = b2b2cOperatorStatisticsManager.getBuyStatistics(cycle_type, year, month, storeid);
        return JsonResultUtil.getObjectJson(buyStatistics);
    }

    /**
     * 获取商品销售明细
     *
     * @param cat_id 商品类
     * @param name   商品名称
     * @return Json格式的字符串 result = 1 代表成功 否者失败
     */
    @ResponseBody
    @RequestMapping(value = "/get-goods-sales-detail")
    public JsonResult getGoodsSalesDetail(Integer cat_id, String name) {
        try {
            // 获取数据
            List<Map<String, Object>> list = this.b2b2cGoodsStatisticsManager.getGoodsDetal(cat_id, name);
            return JsonResultUtil.getObjectJson(list);
        } catch (RuntimeException e) {
            e.printStackTrace();
            this.logger.error("获取商品下单总额排行出错", e);
            return JsonResultUtil.getErrorJson("获取商品下单总额排行出错:" + e.getMessage());
        }
    }

    /**
     * 获取商品下单总金额排行
     *
     * @param top_num
     * @param start_date 开始时间 可为空
     * @param end_date   结束时间 可为空
     * @return Json格式的字符串 result = 1 代表成功 否者失败
     */
    @ResponseBody
    @RequestMapping(value = "/get-goods-order-price-top")
    public JsonResult getGoodsOrderPriceTop(String start_date, String end_date) {
        try {

            Integer top_num = 30;            //排名名次 默认30
            String startDateStamp = "";        //开始时间戳
            String endDateStamp = "";        //结束时间戳

            // 1.判断并赋值
            if (start_date != null && !"".equals(start_date)) {
                startDateStamp = String.valueOf(DateUtil.getDateline(start_date, "yyyy-MM-dd HH:mm:ss"));
            }
            if (end_date != null && !"".equals(end_date)) {
                endDateStamp = String.valueOf(DateUtil.getDateline(end_date, "yyyy-MM-dd HH:mm:ss"));
            }

            // 2.获取数据
            List<Map<String, Object>> list = this.b2b2cGoodsStatisticsManager.getGoodsOrderPriceTop(top_num, startDateStamp, endDateStamp);
            return JsonResultUtil.getObjectJson(list);
        } catch (RuntimeException e) {
            e.printStackTrace();
            this.logger.error("获取商品下单总额排行出错", e);
            return JsonResultUtil.getErrorJson("获取商品下单总额排行出错:" + e.getMessage());
        }
    }

    /**
     * 获取下单商品排行
     *
     * @param top_num    排名名次 <b>必填</b>
     * @param start_date 开始时间 可为空
     * @param start_date 结束时间 可为空
     * @return Json格式的字符串 result = 1 代表成功 否者失败
     */
    @ResponseBody
    @RequestMapping(value = "/get-goods-num-top")
    public JsonResult getGoodsNumTop(String start_date, String end_date) {
        try {

            Integer top_num = 30;            //排名名次 默认30
            String startDateStamp = "";        //开始时间戳
            String endDateStamp = "";        //结束时间戳

            // 1.判断并赋值
            if (start_date != null && !"".equals(start_date)) {
                startDateStamp = String.valueOf(DateUtil.getDateline(start_date, "yyyy-MM-dd HH:mm:ss"));
            }
            if (end_date != null && !"".equals(end_date)) {
                endDateStamp = String.valueOf(DateUtil.getDateline(end_date, "yyyy-MM-dd HH:mm:ss"));
            }

            // 2.获取数据
            List<Map<String, Object>> list = this.b2b2cGoodsStatisticsManager.getGoodsNumTop(top_num, startDateStamp, endDateStamp);

            return JsonResultUtil.getObjectJson(list);
        } catch (RuntimeException e) {
            e.printStackTrace();
            this.logger.error("获取下单商品数量排行出错", e);
            return JsonResultUtil.getErrorJson("获取下单商品数量排行出错:" + e.getMessage());
        }
    }

    /**
     * 获取当前分页页码
     *
     * @return 当前分页页码
     */
    public int getPage() {

        HttpServletRequest request = ThreadContextHolder.getHttpRequest();

        /**
         * 通过request获取当前页码
         */
        int page = StringUtil.toInt(request.getParameter("page"), 1);

        /**
         * 使页码不得小于1
         */
        page = page < 1 ? 1 : page;

        return page;
    }

    /**
     * 获取分页大小<br>
     * 如果easy ui grid没有传递分页参数，则使用后台设置的分页参数
     *
     * @return 分页大小
     */
    protected int getPageSize() {

        HttpServletRequest request = ThreadContextHolder.getHttpRequest();

        /**
         * easy ui grid可能传递过来的分页参数
         */
        int rows = StringUtil.toInt(request.getParameter("rows"), 0);

        /**
         * 如果easy ui grid没有传递分页参数，则使用后台设置的分页参数
         */
        if (rows == 0) {
            rows = SystemSetting.getBackend_pagesize();
        }

        return rows;
    }

}
