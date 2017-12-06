/**
 * create by jianghongyan 2016-08-03
 */


/**
/**
 * 出售右侧横向echart柱状图图
 */
function initHistogram(conf){
	var myChart = echarts.init(document.getElementById('order_price_statistics'));
	var options = {
			color: ['#3398DB'],
			title : {			//图形上方标题
	        	x : 'center',
				text : conf.title
			},
			tooltip : {
				 trigger: 'item',		//触发类型
	             formatter : function(params){			//鼠标放置图形触发提示内容
	                var dataname = conf.name[params.dataIndex].name;
	                return dataname + '<br/>' +params.seriesName + ' : ' + params.value +'元';
	             }
			},
			toolbox: {
		        show : true,
		        feature : {
		            magicType : {show: true, type: ['line', 'bar']},
		            restore : {show: true},
		            saveAsImage : {show: true}
		        }
		    },
			legend : {
            	bottom : '10',
            	data : ['总金额']
            },
            xAxis : {				//x坐标
				type : 'category',
				data : conf.categories,
				nameLocation : 'middle',
				nameGap : '30',
				nameTextStyle : {
					fontWeight : 'bold'
				}
			},
			yAxis : {			//y坐标
				min : 0,
				name : '下单金额（元）',
				type : 'value',
				nameLocation : 'middle',
				nameGap : '50'
			},
			series : [			//图形显示类型
						{
							name : '总金额',
							type : 'bar',
							data : conf.data
						}          
					]
		};
	myChart.setOption(options);
}
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
			getOrderPrice();
			break;
		case 2:
			 getGoodsNum();
			break;
		default:
			getOrderPrice();
	}
};