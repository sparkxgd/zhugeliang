package com.wudi.model;

import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import com.wudi.util.StringUtil;

public class ArrangeSubjectModel extends Model<ArrangeSubjectModel> {
	private static final long serialVersionUID = 1L;
	public static final String tableName = "arrange_subject";
	public static final ArrangeSubjectModel dao = new ArrangeSubjectModel();
	
	public String getId() {
		return get("id");
	}
	public void setId(String id) {
		set("id", id);
	}
	public String getTeacher() {
		return get("teacher");
	}
	public void setTeacher(String teacher) {
		set("teacher", teacher);
	}
	public String getSubject() {
		return get("subject");
	}
	public void setSubject(String subject) {
		set("subject", subject);
	}
	public String getClass_time() {
		return get("class_time");
	}
	public void setClass_time(String class_time) {
		set("class_time", class_time);
	}
	public String getClassid() {
		return get("classid");
	}
	public void setClassid(String classid) {
		set("classid", classid);
	}
	public String getClassroom() {
		return get("classroom");
	}
	public void setClassroom(String classroom) {
		set("classroom", classroom);
	}
	
	public static ArrangeSubjectModel getById(String id) {
		String sql = "select * from " + tableName + " where id = ?";
		return dao.findFirst(sql, id);
	}
	public static List<ArrangeSubjectModel> getArrSub(String teacher) {
		String sql = "select a.*,b.id as teacher,c.id as userid,d.id as roomid,d.nickname as roomname,e.id as classid,e.nickname as classname,f.nickname as subjcname " + 
				"from arrange_subject as a LEFT JOIN teacher as b on b.id=a.teacher " + 
				"LEFT JOIN `user` as c on c.id=b.userid " + 
				"left join classroom as d on a.classroom=d.id " + 
				"LEFT JOIN classinfo as e on a.classid=e.id " + 
				"LEFT JOIN `subject` AS f on a.`subject`=f.id  where c.id= ?";
		StringBuffer from_sql = new StringBuffer();
		return dao.find(sql, teacher);
	} 
	public static Page<ArrangeSubjectModel> getList(int pageNumber, int pageSize, String key) {
		String sele_sql = "select a.*,b.nickname as classname,c.nickname as classroom,f.username,g.nickname as subjcname ";
		StringBuffer from_sql = new StringBuffer();
		from_sql.append("from ").append(tableName).append(" as a left join ").append(ClassinfoModel.tableName).append(" as b ").append(" on a.classid=b.id ");
		from_sql.append("left join ").append(ClassroomModel.tableName).append(" as c ").append(" on a.classroom=c.id ");
		from_sql.append(" left join ").append(TeacherModel.tableName).append(" as d on a.teacher=d.id ");
		from_sql.append(" left join ").append(UserModel.tableName).append(" as f on d.userid=f.id ");
		from_sql.append(" left join ").append(SubjectModel.tableName).append(" as g on a.subject=g.id ");
		if(!StringUtil.isBlankOrEmpty(key)) {
			from_sql.append("where f.username like '%"+key+"%'");
		}
		return dao.paginate(pageNumber, pageSize, sele_sql, from_sql.toString());
	}
	public static boolean save(String teacher, String subject, String class_time, String classid, String classroom) {
		ArrangeSubjectModel m = new ArrangeSubjectModel();
		m.setId(StringUtil.getId());
		m.setTeacher(teacher);
		m.setSubject(subject);
		m.setClass_time(class_time);
		m.setClassid(classid);
		m.setClassroom(classroom);
		return m.save();
	}
	public static boolean updata(String id, String teacher, String subject, String class_time, String classid, String classroom) {
		ArrangeSubjectModel m = ArrangeSubjectModel.getById(id);
		m.setId(id);
		m.setTeacher(teacher);
		m.setSubject(subject);
		m.setClass_time(class_time);
		m.setClassid(classid);
		m.setClassroom(classroom);
		return m.update();
	}
	public static boolean delArrang_sub(String id) {
		try {
			String delsql = "delete from " + tableName + " where id=?";
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
	public static List<ArrangeSubjectModel> getListAll() {
		StringBuffer sql=new StringBuffer();
		sql.append("select *  from ").append(tableName);
		return dao.find(sql.toString());
	}
	
	public static List<ArrangeSubjectModel> getStuSub(String classid){
		String sql = "select a.*,g.username as teachername,f.nickname as roomname,b.nickname as subject,c.nickname as classname from arrange_subject AS a " + 
				"LEFT JOIN `subject` as b ON a.`subject`=b.id " + 
				"left join teacher as d on a.teacher=d.id "+
				"left join user as g on d.userid=g.id "+
				"left join classroom as f on f.id=a.classroom "+
				"left join classinfo as c on a.classid=c.id WHERE classid = ?";
		return dao.find(sql, classid);
	}
	
	public static ArrangeSubjectModel getStuid(String id) {
		StringBuffer sql = new StringBuffer();
		sql.append("select f.* from ").append(StudentModel.tableName).append(" as a ");
		sql.append("left join ").append(Stu_pareModel.tableName).append(" as b on a.id=b.stu_id ");
		sql.append("left join ").append(ParentsModel.tableName).append(" as c on c.id=b.pare_id ");
		sql.append("left join ").append(UserModel.tableName).append(" as d on d.id=c.userid ");
		sql.append("left join ").append(ClassinfoModel.tableName).append(" as e on a.clas=e.id ");
		sql.append(" left join ").append(tableName).append(" as f on f.classid=e.id ");
		sql.append(" WHERE d.id=?");
		return dao.findFirst(sql.toString(), id);
	}
}
