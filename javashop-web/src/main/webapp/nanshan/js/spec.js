(function($) {
    $.fn.myPlugin = function(catId,articlId,renderCallback) {
    	if(catId == null){
    		return;
    	}
    	var params = {"catId":catId};
    	if(articlId != null){
    		params = {"articlId":articlId,"catId":catId};
    	}
    	var $this = $(this);
    	var contentInfoHtml = "";
		$.ajax({
            type: "POST",
            dataType: "JSON",
            url: "/core/admin/spec/getSpecInfo.do",
            data: params,
            success: function (data) {
            		if(data.result == 1){
            			var contentInfo = "<div class='layui-collapse'>"+
      				  						"<div class='layui-colla-item'>"+
      				  							"<h2 class='layui-colla-title'>基础属性</h2>" +
      				  								"<div class='layui-colla-content'>";
            			var json =  eval("("+data.message+")");
            			var specList = json.specList;
            			var specValList = json.specValList;
            			var aa = "";
	    				  $.each(specList,function(i,spec){
	          				aa+="<div class='layui-form-item'>" +
	          								"<label class='layui-form-label'>"+spec.spec_name+"</label>" +
	          										"<div class='layui-input-block'>"
	          				 $.each(specValList,function(i,specVal){
	          					 if(spec.spec_id == specVal.spec_id){
	          						aa +="<input name='specVal' title="+specVal.specval_name+" type='checkbox'>";
	          					 }
	          				 });
	          				aa +="</div></div>";
	    				  });
	    				  contentInfo =contentInfo+aa+"</div></div></div>";
	    				  $this.html(contentInfo);
	    				  renderCallback();
            		}
            	},
            error: function(data) {
            		alert("error:"+data.responseText);
            }
    	});
    	return {
    		getData : function(){
    			alert("test123");
    		}
    	}
    };
})(jQuery);


function getSpecInfoList(catId,articlId){
	
	if(catId == null){
		return;
	}
	var params = {"catId":catId};
	if(articlId != null){
		params = {"articlId":articlId,"catId":catId};
	}
	var contentInfoHtml = "";
	$.ajax({
        type: "POST",
        dataType: "JSON",
        url: "/core/admin/spec/getSpecInfo.do",
        data: params,
        success: function (data) {
        		if(data.result == 1){
        			var contentInfo = "<div class='layui-collapse'>"+
  				  						"<div class='layui-colla-item'>"+
  				  							"<h2 class='layui-colla-title'>基础属性</h2>" +
  				  								"<div class='layui-colla-content'>";
        			var json =  eval("("+data.message+")");
        			var specList = json.specList;
        			var specValList = json.specValList;
        			var aa = "";
    				  $.each(specList,function(i,spec){
          				aa+="<div class='layui-form-item'>" +
          								"<label class='layui-form-label'>"+spec.spec_name+"</label>" +
          										"<div class='layui-input-block'>"
          				 $.each(specValList,function(i,specVal){
          					 if(spec.spec_id == specVal.spec_id){
          						aa +="<input name='specVal' title="+specVal.specval_name+" type='checkbox'>";
          					 }
          				 });
          				aa +="</div></div>";
    				  });
    				  contentInfo =contentInfo+aa+"</div></div></div>"    				  
    				  console.log(contentInfo);
    				  $("#specInfoId").html(contentInfo);
    			
    				  console.log(layui);
    				  var form = layui.form();
    				  console.log(form);
			  		  form.render();
        		}
        	},
        error: function(data) {
        		alert("error:"+data.responseText);
        }
	});
	
}
