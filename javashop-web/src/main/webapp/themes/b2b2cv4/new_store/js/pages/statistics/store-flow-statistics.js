/**
 * create by jianghongyan 2016-08-04
 * 流量统计-店铺总流量js
 */
$(function(){
	getStoreFlowStatistics();
});


/**
 * 初始化曲线图
 * @param id	html 初始化div的id
 * @param conf	相关配置
 */
 
function initLineChart(id,conf){
	
	var myChart = echarts.init(document.getElementById(id));
	option = {
			color: ['#7cb5ec'],
		    title: {
		    	x:'center',
		        text: conf.title
		    },
		    tooltip: {
		        trigger: 'axis'
		    },
		    toolbox: {
		        feature:{
		            magicType: {type: ['line', 'bar']},
		            restore: {},
		            saveAsImage: {}
		        }
		    },
		    legend: {
		    	x:'center',y:'bottom',
	            data:['访问量']
	        },
		    xAxis:  {
		        type: 'category',
		        boundaryGap: 'true',
		        data: conf.categories
		    },
		    yAxis: {
		        type: 'value',
		        name:'访问量(次)',
		    	axisLabel : {
	            formatter: '{value} (次)'
	        },
	        	boundaryGap : 'true'
	    
		    },
		    series: [
		        {
		            name:'访问量',
		            type:'line',
		            data:conf.data,
		            markPoint: {
		                data: [
		                    {type: 'max', name: '最大值'},
		                    {type: 'min', name: '最小值'}
		                ]
		            },
		            markLine: {
		                data: [
		                    {type: 'average', name: '平均值'}
		                ]
		            }
		        },
		    ]
		};

	myChart.setOption(option);
	
	
	
	
};


/**
 * 获得总流量统计相关配置
 * @param json 数据
 */
function getFlowConfig(json){
	
	var conf = {};			//配置

	var data = [];	// Y轴 排名数据
	var categories = []; //X轴 名次数据
	
	// 遍历生成 data,categories
	for(var i in json) {
		var order = json[i];
		
		//添加到数组
		data.push(order.num);
		categories.push("" + order.day_num);
	}
	
	var conf = {
		title : "访问量统计" ,		//统计图标题		//y轴 描述
										//X 轴数据 [数组]
		categories : categories,				
            							//Y轴数据 [数组]
		data: data						

	};
	return conf;
};
function refreshTab(tabId, startDate, endDate){
	getStoreFlowStatistics(startDate, endDate);
}

function getStoreFlowStatistics(){

	var year = $("#year").val();
	var month = $("#month").val();
	var cycle_type = $("#cycle_type").val();

	// ajax配置
	var options = {
		url : ctx+"/api/flow-statistics/get-store-flow-statistics.do",
		data : {
			'year' : year,
			'month' : month,
			'cycle_type' : cycle_type,
			'storeid' : store_id
		},
		type : "post",
		dataType : "json",
		success : function(data) {

			//如果获得正确的数据
			if (data.result == 1) {

				

				// 1.获取到统计图相关配置
				var conf = getFlowConfig(data.data,cycle_type);

				// 2.初始化统计图
				initLineChart("main", conf);

			} else {
				$.message.error("调用action出错：" + data.message);
			}
		},
		error : function() {
			$.message.error("系统错误，请稍后再试");
		}
	};
	$.ajax(options);
}