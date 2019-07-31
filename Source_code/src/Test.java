import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Test {
	
	public static void main(String[] args) {
		 //getting current date and time using Date class
	       DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
	       Date dateobj = new Date();
	       String date=df.format(dateobj);
	       System.out.println(date);
	       
	       
	       int i = (int) (new Date().getTime()/1000);
	       long ti= new Date().getTime();
	       System.out.println("Integer : " + i);
	       System.out.println("Long : "+ new Date().getTime());
	       System.out.println("Long date : " + new Date(new Date().getTime()));
	       System.out.println("Int Date : " + new Date(((long)i)*1000L));
	       System.out.println(ti);
	       System.out.println(df.format(new Date(ti)));
	       
	}

}
