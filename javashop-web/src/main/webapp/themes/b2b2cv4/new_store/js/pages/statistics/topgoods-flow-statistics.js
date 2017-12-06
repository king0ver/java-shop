/**
 * create by jianghongyan 2016-08-04
 * 流量统计-店铺总流量js
 */
var topNum=30;
$(function(){
	getTopGoodsFlowStatistics();
});

/**
 * 初始化柱状图
 * @param id	html 初始化div的id
 * @param conf	相关配置
 */
function initHistogram(conf){

	var myChart = echarts.init(document.getElementById('main'));
	var year = $("#year").val();
	var month = $("#month").val();
	var cycle_type = $("#cycle_type").val();

	$.ajax({
		type : "post",
		async : true,            //异步请求	（同步请求将会锁住浏览器，用户其他操作必须等待请求完成才可以执行）
		url : ctx + "/api/flow-statistics/get-topgoods-flow-statistics.do",
		data : {
			'year' : year,
			'month' : month,
			'cycle_type' : cycle_type,
			'storeid' : store_id
		},
		type : "post",
		dataType:"json",  //返回数据形式为json
		success : function(result) {
		    //请求成功时执行该函数内容，result即为服务器返回的json对象
		    if (result) {
		           myChart.setOption({        //加载数据图表
		        	   tooltip: {
		                    trigger: 'item',
		                    formatter: function(params){
		                    	var dataname = result.data[params.dataIndex].goods_name;
		                    	return dataname + '<br/>' + params.seriesName + ' : ' +params.value +'次';
		                    }
		                }
		        	   
		           });
		    }
		},
		error : function(errorMsg) {
		    //请求失败时执行该函数
		alert("图表请求数据失败!");
		myChart.hideLoading();
		}		                    
	})
	
	var option = {
		    color: ['#7cb5ec'],
		    title: {
		    	x:'center',
		        text: conf.title
		    },
		    toolbox: {
		        
		        feature:{
		            magicType: {type: ['line', 'bar']},
		            restore: {},
		            saveAsImage: {}
		        },
		    },
		    
		    tooltip: {
	            show: true,
	            lable:{
	            axisPointer :
		                      {
	              shadowColor: 'rgba(0, 0, 0, 0.5)',
	              shadowBlur: 10
		                      },         
	            } 
	        },
	        legend: {
	        	x:'center',y:'bottom',
	            data:['访问量']
	        },
	        xAxis : [
	            {
	            	boundaryGap : 'true',
	                type : 'category',
	                data : conf.categories
	            }
	        ],
	        yAxis : [
	            {
	            	boundaryGap : 'true',
	            	name:'访问量(次)',
	            	type: 'value',
	            	axisLabel : {
	            		formatter: '{value} 次'
	                },
	            }
	        ], 
	        series : [
	            {
	            	name :"访问量",
	                type:"bar",
	                data:conf.data
	            }
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
	var num = topNum;								// top几

	var data = [];	// Y轴 排名数据
	var categories = []; //X轴 名次数据
	var name = [];
	
	// 遍历生成 data,categories
	for(var i in json) {
		var member = json[i];
	     data.push(member.num);
		categories.push(parseInt(i) + 1);
		};
		var conf = {
			title : "商品访问量Top" + num ,
			categories : categories,				
        	data : data, 
	};
	return conf;
};
function refreshTab(tabId, startDate, endDate){
	getTopGoodsFlowStatistics(startDate, endDate);
}

function getTopGoodsFlowStatistics(){

	var year = $("#year").val();
	var month = $("#month").val();
	var cycle_type = $("#cycle_type").val();

	// ajax配置
	var options = {
		url : ctx+"/api/flow-statistics/get-topgoods-flow-statistics.do",
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
				setTimeout(function(){
					initHistogram(conf);
				},1000)

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