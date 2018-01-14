(function(){

	var pageNo = 1;

	var total;

	var url;

	var nextPageLoadding = false;

	var self;


	$.fn.extend({
		page : function(options){
			page($(this), options.url, options.total)
		}
	});


	var page = function(_self, _url, _total){

		self = _self;
		url = _url + location.href.substring(location.href.indexOf("?"));
		total = _total;

		$(window).bind('scroll', function(){
			setTimeout(loadTabScroll,200);
		});
	};

	var loadTabScroll=function(){
		var viewH =$(this).height(),//可见高度
			contentH =$(document).height(),//内容高度
			scrollTop =$(this).scrollTop();//滚动高度
		//if(contentH - viewH - scrollTop <= 100) { //到达底部100px时,加载新内容
		console.log(scrollTop/(contentH -viewH))
		if(scrollTop/(contentH -viewH)>=1 && !nextPageLoadding){ //到达底部100px时,加载新内容
			loadNext(++pageNo)
		}
	}

	var loadNext=function(pageNextNum){
		if(pageNextNum <= total){
			$('#loading').html("<div class='loadBox'>"+
				"<img src='/themes/wap/nanshan/css/images/load.gif'>"+
				"</div>");

			nextPageLoadding = true;

			$.get(url, {page : pageNo}, function(data){
				nextPageLoadding = false;
				$('#loading').html("");

				self.append(data);
			});


		}else{
			$('#loading').html("<div class='loadBox'>"+
				"<span class='disV'>內容已经加载完毕</span>"+
				"</div>");
		}

	}


})();








