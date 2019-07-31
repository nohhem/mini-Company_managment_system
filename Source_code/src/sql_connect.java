
import java.sql.*;

import javax.swing.JOptionPane;

public class sql_connect {
	public final String column_code="code",column_name="name",
					column_quantityin="quantityin",column_unit="unit",column_priceoriginal="priceoriginal",
					column_pricesell="pricesell",column_section="section",column_description="description";
	
	public final String column_payment_amount="amount",column_payment_date="date",column_payment_description="description";
	public static Connection con = null;
	public static Connection ConnecrDB(){
		try {
			Class.forName("org.sqlite.JDBC");
			Connection con=DriverManager.getConnection("jdbc:sqlite:database1.db");
			//JOptionPane.showMessageDialog(null, "Connection established");
			return con;
		} catch (Exception e) {
			// TODO: handle exception
			JOptionPane.showMessageDialog(null,e);
			return null;
		}
		
		
	}
}
