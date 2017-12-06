/**
 * create by xuliepng 2016年12月06日
 */
$(function(){
	loadCharts(myChart);
	
	//刷新甘特图
	$.ajax({
        type: "post",
        url: ctx+"/api/store-profile/get-collect-chart-json.do?storeId="+store_id,
        dataType: "json",
        success: function (data) {
        	myChart.setOption({
        		tooltip: {
    	            trigger: 'item',
    	            formatter: function(params){
    	            	var dataname = data[params.dataIndex].goods_name;
    	            	return dataname + '<br/>' + params.seriesName + ' : ' +params.value +'次';
    	            }
    	        },
    	        xAxis:{
    	            data: function (){
    	                var list = [];
    	                for (var i = 1;i<=data.length; i++) {
    	                	list.push(i);
    	                	}
    	                return list;
    	            }()
    	        },
    	        series: [{
    	        	data: function (){
    	        		var list = [];
    	        		for(var i=0;i<data.length;i++){
    	        			list.push(data[i].y);
    	        			}
    	        		return list;
    	        		}()
    	        		}]
	           });
        },
        error: function (msg) {
            alert("出现错误，请稍后重试");
        }
    });
	
});

//生成图表
function loadCharts(obj){
	var option = {
			title: {
	        	x: 'center',
	            text: '收藏商品排行Top50'
	        },
	        legend: {},
	        toolbox: {
		        show : true,
		        feature : {
		            mark : {show: true},
		            magicType : {show: true, type: ['line', 'bar']},
		            restore : {show: true},
		            saveAsImage : {show: true}
		        }
		    },
	        grid: {  
	            top: 80,  
	            right:100,  
	            bottom:70  
	        }, 
	        xAxis:{
	        	type: 'category',
	            axisTick: {
	                alignWithLabel: true
	            },
	            data:[]
	        },
	        yAxis: {
	            type: 'value',
	            name: '收藏数',
	            position: 'left',
	            axisLabel: {
	                formatter: '{value} 次'
	            }
	        },
	    	series: [{
		        name: ['收藏数'],
		        type: 'bar',
		        data:[],
		        itemStyle: {
		            normal: {
		                color:'#7cb5ec',
		                label: {
		                    show: true,
		                    position: 'top',
		                    formatter: '{c}'
		                }
		            }
		        },
	    	}]
	};
	/** 使用刚指定的配置项和数据显示图表。*/
    myChart.setOption(option);
}