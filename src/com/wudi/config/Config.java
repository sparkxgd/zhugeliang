package com.wudi.config;

import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.template.Engine;
import com.wudi.controller.AdminController;
import com.wudi.controller.WeixinController;
import com.wudi.model.ClassinfoModel;
import com.wudi.model.ConfigModel;
import com.wudi.model.MajorModel;
import com.wudi.model.NewsModel;
import com.wudi.model.ParentsModel;
import com.wudi.model.RoleModel;
import com.wudi.model.Stu_pareModel;
import com.wudi.model.StudentModel;
import com.wudi.model.TeacherModel;
import com.wudi.model.TeamModel;
import com.wudi.model.TeamersModel;
import com.wudi.model.UserIntegralModel;
import com.wudi.model.UserModel;
import com.wudi.model.UserintegralDetailModel;
import com.wudi.plugin.BaiduPlugin;

public class Config extends JFinalConfig {

	@Override
	public void configConstant(Constants me) {
		// 加载配置文件，注意：配置文件必须放在src目录下,要不然找不到
		loadPropertyFile("config.properties");
		// 配置一些系统变量
		me.setDevMode(getPropertyToBoolean("DevMode", true));//设置为开发模式，方便查看日志
		me.setError404View("/WEB-INF/admin/404.html");
		
	}

	@Override
	public void configRoute(Routes me) {
		// 设置路由，客户端访问就是在这里设置的路径地址
		me.add("/admin", AdminController.class,"WEB-INF/admin");//后台数据管理访问路径：localhost:8086/admin
		me.add("/wudi", WeixinController.class);//微信小程序访问路径：localhost:8086/wudi
		
		
		
	}

	@Override
	public void configEngine(Engine me) {

	}

	@Override
	public void configPlugin(Plugins me) {
		// 插入其他插件，比如，连接数据库的mysql插件和连接多数据库插件
		DruidPlugin dsMysql = new DruidPlugin(getProperty("jdbcUrl"), getProperty("user"),
				getProperty("password").trim());
		{
			dsMysql.setTestOnBorrow(true);
			dsMysql.setTestOnReturn(true);
			dsMysql.setMaxWait(20000);
		}
		//mysql插件
		ActiveRecordPlugin arpMysql = new ActiveRecordPlugin("mysql", dsMysql);
		//从配置文件里查找出来，是否显示sql语句
		boolean showSql = getPropertyToBoolean("showSql", true);
		//系统设置是否显示sql
		arpMysql.setShowSql(showSql);
		{
			//将数据库表，绑定到这来来，注意，表名和类要相对应
			arpMysql.addMapping("config", ConfigModel.class);
			arpMysql.addMapping("user", UserModel.class);
			arpMysql.addMapping("role", RoleModel.class);
			arpMysql.addMapping("news", NewsModel.class);
			arpMysql.addMapping("student", StudentModel.class);//学生表
			arpMysql.addMapping("teacher", TeacherModel.class);//教师表
			arpMysql.addMapping("parents", ParentsModel.class);//家长表
			arpMysql.addMapping("major", MajorModel.class);//专业表
			arpMysql.addMapping("stu_pare", Stu_pareModel.class);//学生家长关联表
			arpMysql.addMapping("classinfo", ClassinfoModel.class);//班级表
		//添加插件
		me.add(dsMysql);
		me.add(arpMysql);
		}
		
		
		//加载百度ai插件
		BaiduPlugin baiduai=new BaiduPlugin(getProperty("APP_ID"),getProperty("API_KEY"), getProperty("SECRET_KEY"));
		me.add(baiduai);
		
	}

	@Override
	public void configInterceptor(Interceptors me) {
	}
	@Override
	public void configHandler(Handlers me) {
		
	}
}
