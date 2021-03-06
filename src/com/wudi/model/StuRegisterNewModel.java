package com.wudi.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import com.wudi.bean.FaceSeachModel;
import com.wudi.util.StringUtil;

public class StuRegisterNewModel extends Model<StuRegisterNewModel> {
	private static final long serialVersionUID = 1L;
	public static final String tableName = "stu_registernews";
	public static final StuRegisterNewModel dao = new StuRegisterNewModel();
	
	public String getId() {
		return get("id");
	}
	public void setId(String id) {
		set("id", id);
	}
	public Date getReg_time() {
		return get("reg_time");
	}
	public void setReg_time(Date reg_time) {
		set("reg_time", reg_time);
	}
	public String getAddr() {
		return get("addr");
	}
	public void setAddr(String addr) {
		set("addr", addr);
	}
	public int getType() {
		return get("type");
	}
	public void setType(int type) {
		set("type", type);
	}
	public int getWeek() {
		return get("week");
	}
	public void setWeek(int week) {
		set("week", week);
	}
	public String getRemark() {
		return get("remark");
	}
	public void setRemark(String remark) {
		set("remark", remark);
	}
	public String gettcsuid() {
		return get("tcsuid");
	}
	public void settcsuid(String tcsuid) {
		set("tcsuid", tcsuid);
	}
	public String getstuid() {
		return get("stuid");
	}
	public void setstuid(String stuid) {
		set("stuid", stuid);
	}
	public int getstatus() {
		return get("status");
	}
	public void setstatus(int status) {
		set("status", status);
	}
	public String getUserid() {
		return get("userid");
	}
	public String getClassid() {
		return get("classid");
	}
	public void setClassid(String classid) {
		set("classid", classid);
	}
	
	public static StuRegisterNewModel getById(String id) {
		String sql = "select * from " + tableName +" where id = ?";
		return dao.findFirst(sql, id);
	}
	public static List<StuRegisterNewModel> getBysss(String tcsuid,int week, String classid) {
		String sql = "select a.*,b.userid from " + tableName +" a left join "+StudentModel.tableName+" b on a.stuid=b.id where a.week =? and a.tcsuid=? and a.classid = ?";
		return dao.find(sql, week,tcsuid,classid);
	}
	public static StuRegisterNewModel getByStuid(String id) {
		String sql = "select a.*,b.userid from " + tableName +" as a left join "+StudentModel.tableName+" as b on a.stuid=b.id where stuid = ?";
		return dao.findFirst(sql, id);
	}
	public static Page<StuRegisterNewModel> getList(int pageNumber, int pageSize, String key) {
		String sele_sql = "select a.*,b.no,c.username,d.classroom,f.nickname,g.nickname as classname ";
		StringBuffer from_sql = new StringBuffer();
		from_sql.append("from ").append(tableName).append(" as a left join ").append(StudentModel.tableName).append(" as b on a.stuid=b.id");
		from_sql.append(" left join ").append(UserModel.tableName).append(" as c on b.userid=c.id");
		from_sql.append(" left join ").append(ArrangeSubjectModel.tableName).append(" as d on a.tcsuid=d.id");
		from_sql.append(" left join ").append(SubjectModel.tableName).append(" as f on d.subject=f.id");
		from_sql.append(" left join ").append(ClassinfoModel.tableName).append(" as g on b.clas=g.id");
		if(!StringUtil.isBlankOrEmpty(key)) {
			from_sql.append("where c.username like '%"+key+"%'");
		}
		return dao.paginate(pageNumber, pageSize, sele_sql, from_sql.toString());
	}
	
	/**
	 * 老师开始签到的时候，先给学生签到表生成记录，后面再更新状态
	 * @param claid
	 * @return
	 */
	public static boolean addStuRegByClass(List<FaceSeachModel> list,String classid,String tcsuid,int week) {
		boolean result=true;
		ClassinfoModel c=ClassinfoModel.getById(classid);//找一下这个班级是否存在？
		if(c!=null) {//存在，就开始添加数据
			List<StudentModel> li=StudentModel.getListbyClassid(classid);//把班级的学生都找出来
			List<StuRegisterNewModel> srli=new ArrayList<StuRegisterNewModel>();
			for( StudentModel m:li) {
				StuRegisterNewModel s=new StuRegisterNewModel();
				s.setId(StringUtil.getId());
				s.setstatus(0);//0未签到，1已签到，-1其他，2旷课，3事假，4病假
				s.setType(0);//0上课，1下课
				s.setReg_time(null);
				for(FaceSeachModel f:list) {
					if(f.getUser_id().equals(m.getUserid())) {
						s.setstatus(1);//0未签到，1已签到，-1其他，2旷课，3事假，4病假
						s.setReg_time(new Date());
						break;
					}					
				}
				s.setstuid(m.getId());
				s.settcsuid(tcsuid);
				s.setWeek(week);
				s.setClassid(classid);
				srli.add(s);
			}
			 Db.batchSave(srli, 10);//批量保存数据
		}else {
			 result=false;
		}
		return result;
	}
	
	public static List<StuRegisterNewModel> signIn(List<FaceSeachModel> list,String tcsuid,String classid,int week) {
		List<StuRegisterNewModel> stulist=StuRegisterNewModel.getBysss(tcsuid,week,classid);
		if(stulist.size()<1) {//说明第一次拍照签到，要先把学生信息签到初始化
			addStuRegByClass(list,classid,tcsuid,week);
		}else {
			for(StuRegisterNewModel st:stulist) {
				if(st.getstatus()!=1) {//已经签到，就不用再签了,如果还没有签，就继续
					for(FaceSeachModel m:list) {
						if(st.getUserid().equals(m.getUser_id())) {
							st.setstatus(1);//0未签到，1已签到，-1其他，2旷课，3事假，4病假
							st.setReg_time(new Date());
							st.setType(0);//0上课，1下课
							break;
						}

					}
				}
			
			}
			Db.batchUpdate(stulist, 10);//批量保存数据
		}
		return getListN(tcsuid,week,classid);
	}
	
	public static boolean delStu_register(String id) {
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
	public static List<StuRegisterNewModel> getListN(String tcsuid,int week, String classid) {
		String sql = "SELECT c.username ,b.`no`,a.`status` from stu_registernews as a LEFT JOIN student as b on a.stuid=b.id LEFT JOIN `user` as c on b.userid=c.id where a.week =? and a.tcsuid=? and a.classid = ? ";
		return dao.find(sql, week,tcsuid,classid);
	}
	public static List<StuRegisterNewModel> getstuArr(String prentid) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT c.classid,f.nickname as coursename,count(*) count from ").append(Stu_pareModel.tableName).append(" a LEFT JOIN ");
		sql.append(StudentModel.tableName).append(" b on a.stu_id=b.id LEFT JOIN ");
		sql.append(tableName).append(" c on c.stuid=b.id  LEFT JOIN ");
		sql.append(ArrangeSubjectModel.tableName).append(" d on c.tcsuid = d.id LEFT JOIN ");
		sql.append(SubjectModel.tableName).append(" f on d.`subject`=f.id LEFT JOIN ");
		sql.append(ParentsModel.tableName).append(" g on a.pare_id=g.id ");
		sql.append(" where g.userid=? and c.`status`=1 group by c.classid,f.nickname ");
		return dao.find(sql.toString(),prentid);
	}
	
	public static List<StuRegisterNewModel> getclassArr(String id,String classid) {
		String sql = "SELECT d.`no`,e.username as stuname,count(*) as count from teacher a LEFT JOIN " + 
				"		arrange_subject b on a.id=b.teacher LEFT JOIN " + 
				"		classinfo c on c.id=b.classid  LEFT JOIN " + 
				"		student d on c.id = d.clas LEFT JOIN " + 
				"		`user` e on e.id=d.userid LEFT JOIN " + 
				"		stu_registernews f on f.classid=c.id " + 
				"		where a.userid=? and f.`status`=1 and f.classid=? GROUP BY e.username,d.`no`";
		return dao.find(sql.toString(), id,classid);
	}
	
	public static List<StuRegisterNewModel> getSubArr(String id,String classid){
		String sql = "SELECT d.nickname,e.stuid,e.classid,COUNT(*) as count FROM " + 
				"student a LEFT JOIN " + 
				"classinfo b ON a.clas=b.id LEFT JOIN " + 
				"arrange_subject c ON c.classid=b.id LEFT JOIN " + 
				"`subject` d ON d.id=c.`subject` LEFT JOIN " + 
				"stu_registernews e ON e.stuid=a.id WHERE a.userid=? and e.`status`=1 and e.classid=? GROUP BY e.stuid,d.nickname,e.classid";
		return dao.find(sql.toString(), id,classid);
	}
	
}
