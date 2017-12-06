/**
 * create by jianghongyan 2016-08-04
 */
$(function(){
	//1.初始化概况展示区数据
	initStatus30day();
	//2.初始化概况统计图
	initLineChart30day();
});

/**
 * 初始化曲线图
 * @param id	html 初始化div的id
 * @param conf	相关配置
 */
function initLineChart(id,conf){

	
	var myChart = echarts.init(document.getElementById('main'));
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
	            data:['下单金额']
	        },
		    xAxis:  {
		        type: 'category',
		        boundaryGap: 'true',
		        data: conf.categories
		    },
		    yAxis: {
		        type: 'value',
		        name:'下单金额',
		    	axisLabel : {
	            formatter: '{value} ￥'
	        },
	        	boundaryGap : 'true'
	    
		    },
		    series: [
		        {
		            name:'下单金额',
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
 * 初始化展示区
 */
function initStatus30day(){
	$.ajax({
		url:ctx+"/api/store-profile/get-last30day-status.do?store_id="+store_id,
		type:"GET",
		dataType:"json",
		success:function(result){
			if(result.result==1){
				var json=result.data;
				for(var i in json){
					$("#"+i).html(json[i]);
				}
			}
		},
		error:function(e){
			$.message.error('出现错误，请重试！');
		}
	});
}
/**
 * 初始化统计图
 */
function initLineChart30day(){
	$.ajax({
		url:ctx+"/api/store-profile/get-last30day-linechart.do?store_id="+store_id,
		type:"GET",
		dataType:"json",
		success:function(result){
			if(result.result==1){
				var conf=getPriceDisConfig(result.data,"最近30天销售走势");
				initLineChart("main",conf);
			}
		},
		error:function(e){
			$.message.error('出现错误，请重试！');
		}
	});
}


/**
 * 获取价格销量统计图配置
 * @param json 数据
 */
function getPriceDisConfig(json,ytitle){
	var conf = {}; //配置

	var data = []; // Y轴 排名数据
	var categories = []; //X轴 名次数据

	for(var i in json) {
		var order = json[i];
		
		//添加到数组
		data.push(order.t_money);
		categories.push("" + order.day);
	}

	var conf = {
		title : ytitle, //统计图标题
		//X 轴数据 [数组]
		categories : categories,
		//Y轴数据 [数组]
		
		data : data

	};
	return conf;
};