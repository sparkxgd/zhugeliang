layui.config({
	base : "js/"
}).use(['form','layer','jquery','laypage','table'],function(){
	var form = layui.form,
		layer = parent.layer === undefined ? layui.layer : parent.layer,
		laypage = layui.laypage,
		table = layui.table,
		$ = layui.$;
	
	var per;
	$.get("getPermission",function(data){
		var p=data.user.permission;
		var obj=$.parseJSON(p);
		var v=obj['c101'];
		per=v;
		if(v==1){
			var arr=new Array();
			arr.push("<a class='layui-btn layui-btn-normal add_btn' id='add_b'> <i class='layui-icon'>&#xe608;</i>添加</a>");
			$("#add_xiao").append(arr.join("\n"));
		}
	});
	

//==================一个table实例================================
	  table.render({
	    elem: '#demo',//渲染对象
	    height: 'full-88',//表格高度
	    url: 'querySchool', //数据接口
	    where: {key: ''},//给后台传的参数
	    page: true, //开启分页
	    limit: 10,//每页显示信息条数
	    id: 'testReload',
	    cols: [[ //表头
	      {field: 'id', title: 'ID', sort: true, fixed: 'left'}
	      ,{field: 'nickname', title: '学校名称',align:'center'}
	      ,{field: 'school', title: '学校代码', align:'center'}
	      ,{field: 'remark', title: '备注',align:'center' }
	      ,{fixed: 'right',  align:'center', title: '操作', templet:function(d){
	    	  var arr=new Array();
	    	  if(per==1){
		    	  arr.push("<a class='layui-btn layui-btn-xs' lay-event='edit'><i class='layui-icon'>&#xe642;</i>编辑</a>");
		    	  arr.push("<a class='layui-btn layui-btn-xs layui-btn-danger' lay-event='del'><i class='layui-icon'></i>删除</a>");
	    	  }
	    	  return arr.join("\n");
	      	}
	     } //这里的toolbar值是模板元素的选择器
	    ]]
	  });
	  
//====================点击【搜索】按钮事件===========================
  var active = {
		  reload : function() {
			  var demoReload = $('#demoReload');
							// 执行重载
			  table.reload('testReload', {
				  page : {
					  curr : 1// 重新从第 1 页开始
					  },
					  where : {//要查询的字段
						  key : demoReload.val(),
						  id : 11
						  }
					  });
			  }
  };
  var activeop = {
		  reload : function() {
			  var demoReload = $('#demoReload');
							// 执行重载
			  table.reload('testReload', {				 
					  where : {//要查询的字段
						  key : demoReload.val()
						  }
					  });
			  }
  };
//绑定搜索事件
  $('.layui-btn').on('click', function() {
	  var type = $(this).data('type');
	  active[type] ? active[type].call(this) : '';
	  });
  
//=============绑定【添加】事件============================
//	$(window).one("resize",function(){
		$(".add_btn").click(function(){
			var index = layui.layer.open({
				title : "【添加信息】",
				icon: 2,
				type : 2,
				skin: 'layui-layer-lan',
				area: ['800px', '600px'],
				content : "openSchoolAdd",
				success : function(layero, index){
					setTimeout(function(){
						layui.layer.tips('点击此处返回列表', '.layui-layer-setwin .layui-layer-close', {
							tips: 3
						});
					},500)
				}
			})			
			layui.layer.full(index);
		})
//	}).resize();
  
//=======================监听工具条====================================
	table.on('tool(test)', function(obj){ //注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
	  var data = obj.data; //获得当前行数据
	  var layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
	  var tr = obj.tr; //获得当前行 tr 的DOM对象
	 
	  if(layEvent === 'detail'){ //查看
		  activeop.reload();  
	  } else if(layEvent === 'del'){
		  layer.confirm('确定删除此信息？',{icon:3, title:'提示信息'},function(index){
				var msgid;
				//向服务端发送删除指令
		 		 $.ajax({//异步请求返回给后台
			    	  url:'delSchool',
			    	  type:'POST',
			    	  data:{"id":data.id},
			    	  dataType:'json',
			    	  beforeSend: function(re){
			    		  msgid = top.layer.msg('数据处理中，请稍候',{icon: 16,time:false,shade:0.8});
			          },
			    	  success:function(d){
			    		  top.layer.close(msgid);
			    		  if(d.result==1){
			    			//弹出loading
						   		layer.closeAll("iframe");
						   		obj.del(); //删除对应行（tr）的DOM结构，并更新缓存
						  	 //刷新父页面
						   	activeop.reload();
			    		  }else{
			    			  top.layer.msg("操作失败！，数据库操作有问题！！");
			    		  }
				    		
			    	  },
			    	  error:function(XMLHttpRequest, textStatus, errorThrown){
			    		  top.layer.msg('操作失败！！！服务器有问题！！！！<br>请检测服务器是否启动？', {
			    		        time: 20000, //20s后自动关闭
			    		        btn: ['知道了']
			    		      });
			           }
			      });
		 //关闭当前提示	
	      layer.close(index);
	    });
	  } else if(layEvent === 'edit'){ //编辑
		  var index = layui.layer.open({
              title : "修改信息",
              type : 2,
              skin: 'layui-layer-lan',
              area: ['800px', '600px'],
              content : "openSchoolEdit?id="+data.id,
              success : function(layero, index){
                  setTimeout(function(){
                      layui.layer.tips('点击此处返回列表', '.layui-layer-setwin .layui-layer-close', {
                          tips: 3
                      });
                  },500)
              }
          })          
          layui.layer.full(index);
	  }
	});
})


