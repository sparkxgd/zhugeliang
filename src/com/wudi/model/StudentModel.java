package com.wudi.model;

import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import com.wudi.util.StringUtil;

public class StudentModel extends Model<StudentModel> {
	private static final long serialVersionUID = 1L;
	public static final String tableName = "student";
	public static final StudentModel dao = new StudentModel();
	
	public String getId() {
		return get("id");
	}
	public void setId(String id) {
		set("id", id);
	}
	public String getNo() {
		return get("no");
	}
	public void setNo(String no) {
		set("no", no);
	}
	public String getClas() {
		return get("clas");
	}
	public void setClas(String clas) {
		set("clas", clas);
	}
	public int getType() {
		return get("type");
	}
	public void setType(int type) {
		set("type", type);
	}
	
	
	public static StudentModel getById(String id) {
		String sql = "select * from " + tableName + " where id = ? ";
		return dao.findFirst(sql, id);
	}
	
	public static Page<StudentModel> getList(int pageNumber, int pageSize,String key) {
		
//		String sele_sql="select a.*,b.name as dep_name ";
//		StringBuffer from_sql=new StringBuffer();
//		from_sql.append("from ").append(tableName).append(" as a left join ").append(DepartmentModel.tableName).append(" as b ");
//		from_sql.append(" on a.dep_no=b.id ");
//		if(!StringUtil.isBlankOrEmpty(key)) {
//			from_sql.append(" where id like '%"+key+"%'");
//		}
		String sele_sql = "select * ";
		StringBuffer from_sql = new StringBuffer();
		from_sql.append("from ").append(tableName);
		if(!StringUtil.isBlankOrEmpty(key)) {
			from_sql.append(" where name like '%"+key+"%'");
		}
		return dao.paginate(pageNumber,pageSize,sele_sql,from_sql.toString());
	}
	
	public static boolean save(String no, String clas, int type) {
		StudentModel m = new StudentModel();
		m.setId(StringUtil.getId());
		m.setNo(no);
		m.setClas(clas);
		m.setType(type);
		return m.save();
	}
	
	public static boolean updateStudent(String id, String no, String clas, int type) {
		StudentModel m = dao.findById(id);
		m.setNo(no);
		m.setClas(clas);
		m.setType(type);
		return m.update();
	}
	
	public static boolean delStudent(String id) {
		try {
			String delsql = "delete from " + tableName + "where id=?";
			int iRet = Db.update(delsql, id);
			if(iRet > 0) {
				return true;
			}
			else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static List<StudentModel> getListAll() {
		StringBuffer sql=new StringBuffer();
		sql.append("select *  from ").append(tableName);
		return dao.find(sql.toString());
	}
}