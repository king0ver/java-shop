/**
 * create by jianghongyan 2016-06-27
 */


/**
 * 根据tabId刷新页面数据
 * @param tabId	tab页的id
 * @param startDate	条件：开始时间
 * @param endDate	条件：结束时间
 */
function refreshTab(tabId, startDate, endDate){
	
	var tabId = parseInt(tabId);
	//暂时tabId与函数方式定死  也许还有更好的方法
	switch(tabId) {
		case 1:
			getRegionMember(startDate, endDate);
			break;
		case 2:
			getRegionMoney(startDate, endDate);
			break;
		case 3:
			getRegionNum(startDate,endDate);
			break;
		default:
			getRegionMember(startDate, endDate);
	}
};