import java.awt.EventQueue;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;

public class DetailsGui {
	 Connection con =null ;
	 ResultSet rs=null;
	 PreparedStatement pst=null;

	private JFrame frame;
	private JTable table;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DetailsGui window = new DetailsGui(null);
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public DetailsGui(String operationId) {
		initialize();
		filltheTablewithDetailsFromDB(operationId);
		
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 584, 382);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setVisible(true);
		
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 0, 567, 344);
		frame.getContentPane().add(scrollPane);
		
		table = new JTable();
		table.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"good name", "good code", "quantity", "price"
			}
		) {
			boolean[] columnEditables = new boolean[] {
				false, false, false, false
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		table.getColumnModel().getColumn(0).setPreferredWidth(98);
		table.getColumnModel().getColumn(1).setPreferredWidth(84);
		table.getColumnModel().getColumn(2).setPreferredWidth(114);
		table.getColumnModel().getColumn(3).setPreferredWidth(92);
		scrollPane.setViewportView(table);
	}
	
	public void filltheTablewithDetailsFromDB(String operationId ){
		//this func gets the details from goodsofoperation table from DB and display them in the table
		con=sql_connect.ConnecrDB();
		String query= "select * from goodsofoperation where op_id == ?";
		try {
			pst=con.prepareStatement(query);
			pst.setString(1, operationId);
			rs=pst.executeQuery();
			
			DefaultTableModel DTM = (DefaultTableModel) table.getModel();
			System.out.println("befor while ");
			while(rs.next()){
				String []details={
						rs.getString(1) // to get the good name
						,rs.getString(2)// to get the good code
						,rs.getString("quantity")
						,rs.getString("price")};
				System.out.println("the details");
				System.out.println(details[1]);
				DTM.addRow(details);
			}
			
			con.close();
			rs.close();
			pst.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
}
