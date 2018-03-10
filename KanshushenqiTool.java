import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.*;

import com.alibaba.fastjson.JSON;

import model.chaps;

public class KanshushenqiTool {
	public static final String DBURL = "jdbc:sqlite:/Users/tony/dev/temp/books/book/com.kanshushenqi.ebook.app/databases/JW_BOOK_3.db";
	public static final String BOOKPATH = "/Users/tony/dev/temp/books/book/books/";

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Connection conn = null;
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		byte buff[] = new byte[4096];
		String vol = "";
		try {
//			bos = new BufferedOutputStream(new FileOutputStream(new File(BOOKPATH + "1588.txt")));
			bos = new BufferedOutputStream(new FileOutputStream(new File(BOOKPATH + "89230.txt")));
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection(DBURL);
			Statement s = conn.createStatement();
//			ResultSet rs = s.executeQuery("select * from chapterbean where hascontent<>0 limit 1890,1394");
			ResultSet rs = s.executeQuery("select * from chapterbean where hascontent<>0 limit 3284,1922");
			ResultSetMetaData meta = null;
			if(rs != null){
				meta = rs.getMetaData();
				int cols = meta.getColumnCount();
				for (int i = 1; i <= cols; i++) {
					System.out.println(meta.getColumnName(i));
				}
			}
			while (rs.next()){
				//id integer primary key autoincrement,changesourceurl text, url text, name text, nid text, 
				//novelid text, oid text, pid text, rollname text, position integer, hascontent integer
				String name = rs.getString("name");
				String novelid = rs.getString("novelid");
				String oid = rs.getString("oid");
//				if(oid.equals("4684397") || oid.equals("4701652")||oid.equals("4926668")||oid.equals("5012725")||oid.equals("5012731")||oid.equals("5013625")) {
//					System.out.println(rs.getString("url"));
//					continue;
//				}
				String rollname = rs.getString("rollname");
				System.out.println(name+"|"+novelid+"|"+oid+"|"+rollname);
				if(!rollname.equals(vol)){
					bos.write(("#"+rollname +"\n").getBytes());
					vol = rollname;
				}
				bos.write(("## "+name +"\n").getBytes());
				bis = new BufferedInputStream(new FileInputStream(new File(BOOKPATH+novelid+"/1/"+oid+".txt")), 4096);
				while(bis.available() > 0) {
					int rsize = bis.read(buff);
					bos.write(buff, 0, rsize);
				}
				bis.close();
				bos.write("\n".getBytes());
//				chaps chaps =  JSON.parseObject(str,chaps.class);
//				if(chaps != null){
//					System.out.println(chaps.getData().getCname() + " \tfile:" + chaps.getData().getCid());
//				}
			}
			rs.close();
			s.close();
			conn.close();
			bos.close();
//			bis.close();
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

}
