function add(){
	$("#article").append("<div class='layui-form-item'>"+
		    "<div class='layui-inline'>"+
		      "<textarea id='content' name='desc' style='width: 700px;' placeholder='请输入内容' class='layui-textarea'></textarea>"+
		    "</div>"+
		    "<div class='layui-inline'>"+
		      "<a class='layui-btn layui-btn-sm layui-btn-danger' onclick='delButton(this);'><i class='layui-icon'>&#xe640;</i></a>"+
		    "</div>"+
		  "</div>");
}

function addImg(){
	$("#article").append("<div class='layui-form-item'>"+
		    "<div class='layui-inline'>"+
				"<img src='http://localhost:8080/statics/images/logo2.png'  width='112' height='112' />"+	
	    	"</div>"+
	    	"<div class='layui-inline'>"+
	      		"<a class='layui-btn layui-btn-sm layui-btn-danger' onclick='delButton(this);'><i class='layui-icon'>&#xe640;</i></a>"+
	    	"</div>"+
	  	"</div>");
}

function getText(text){
	 var html="<div class='layui-form-item'>"+
	    "<div class='layui-inline'>"+
	      "<textarea id='content' name='desc' style='width: 700px;' placeholder='请输入内容' class='layui-textarea'>"+text+"</textarea>"+
	    "</div>"+
	    "<div class='layui-inline'>"+
	      "<a class='layui-btn layui-btn-sm layui-btn-danger' onclick='delButton(this);'><i class='layui-icon'>&#xe640;</i></a>"+
	    "</div>"+
	  "</div>"
	    return html;
}

function getImgHtml(url){
		var html="<div class='layui-form-item'>"+
	    "<div class='layui-inline'>"+
			"<img src='"+url+"'  width='112' height='112' />"+	
		"</div>"+
		"<div class='layui-inline'>"+
	  		"<a class='layui-btn layui-btn-sm layui-btn-danger' onclick='delButton(this);'><i class='layui-icon'>&#xe640;</i></a>"+
		"</div>"+
		"</div>"
		return html;
}

function delButton(k){
	 layer.confirm('确认要删除吗？', {
		  btn: ['删除','取消'] //按钮
		  ,area:['80px','150px']
		}, function(){
			$(k).parent().parent().remove();
		  	 layer.msg({
			    time: 20000
			 }); 
		}, function(){
		    layer.msg({
			    time: 20000
			}); 
		});
};

function getContext(){
	var divContext = $("#article .layui-form-item");
	var jsonList = new Array();
	for(var i=0;i<divContext.length;i++){
		var articleContext = null;
		var content = $(divContext[i]).children();
		var picture = content.find("[name='img']"); //找到图片
		var imgSrc = picture.attr('src')//取src路径
		if(imgSrc != null){
			articleContext = imgSrc;
			jsonList.push({"index":i,"type":"img","content":articleContext});  
		}else{
			articleContext = content.find("#content").val();
			jsonList.push({"index":i,"type":"text","content":articleContext}); 
		}  
	}
	return jsonList;
}

  function getActExt(){
	var content=$("#act").children();
	var jsonList = new Array();
	$.each(content,function(){
		var label=$(this).find(".layui-form-label").text();
		var value=$(this).find("input").val();
		jsonList.push({"label":label.replace(":",""),"value":value})
	})
	return jsonList;
  }  
  
  
  function initContextHtml(context){
	   var contextHtml="";
	   for(var i=0;i<context.length;i++){
		 var type=context[i].type;
		 if(context[i].content==""||context[i].content==undefined) continue;
	   	if(type=="text"){
	   		contextHtml=contextHtml+getText(context[i].content)
	   	}else if(type=="img"){
	   		contextHtml=contextHtml+getImgHtml(context[i].content)
	   	}
	   	
	   }
	   $("#article").html(contextHtml);
	   
  }
  
 
  function initActExtVal(actExt){
	  for(var j=0;j<actExt.length;j++){
	      $("label:contains('"+actExt[j].label+"')").next().children(0).val(actExt[j].value);
	  }
	   
  }
  
  function initSpecId(specIds){
	  if(specIds!=""){
		 var specs= specIds.split(",");
		  for(var i=0;i<specs.length;i++){
			  $("input[name='specValId'][value='"+specs[i]+"']").attr("checked",true);
		  }
	  }
	  
  }
  
  function getSpecValId(){
		var chk_value ="";
		$("input[name='specValId']:checked").each(function(){
			chk_value=chk_value+$(this).val()+",";
		});
		return chk_value;
	}
  
  function delImg(){
		 layer.confirm('确认要删除吗？', {
			  btn: ['删除','取消'] //按钮
			  ,area:['80px','150px']
			}, function(){
				$("#exhImg").html("");
				$("#picUrl").val("");
			  	 layer.msg({
				    time: 20000
				 }); 
			}, function(){
			    layer.msg({
				    time: 20000
				}); 
			});
	};


 
 
 
  
 