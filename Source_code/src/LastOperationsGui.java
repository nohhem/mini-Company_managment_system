import java.awt.EventQueue;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;
import com.sun.org.apache.xpath.internal.WhitespaceStrippingElementMatcher;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ActionEvent;

public class LastOperationsGui {

	private JFrame frame;
	private JTable table;
	Connection con = null;
	ResultSet rs = null;
	PreparedStatement pst=null;
	Object parent;

	private DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
	
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LastOperationsGui window = new LastOperationsGui();
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
	public LastOperationsGui() {
		initialize();
		
	}
	public LastOperationsGui(Object parent) {
		this.parent=parent;
		initialize();
		
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 741, 459);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setVisible(true);
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				((WarehouseMGui) parent).frameisopened=false;
			}
			
		});
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(107, 2, 608, 418);
		frame.getContentPane().add(scrollPane);
		
		table = new JTable();
		table.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"op_id", "type", "customer_id", "amount", "date"
			}
		) {
			boolean[] columnEditables = new boolean[] {
				false, false, false, false, false
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		
		scrollPane.setViewportView(table);
		
		JButton btnDetails = new JButton("Details");
		btnDetails.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try{
					String operationId=(String) table.getModel().getValueAt(table.getSelectedRow(), 0);
					System.out.println(operationId+"  opid sent to details ");
					DetailsGui detailsGui = new DetailsGui(operationId);
				}catch(ArrayIndexOutOfBoundsException AIOE){
					JOptionPane.showMessageDialog(null, "please select an Operation");
				}
				
				
			}
		});
		btnDetails.setBounds(8, 5, 89, 23);
		frame.getContentPane().add(btnDetails);
		UpdateTheoperationsTable();
	}
	
	
	private void UpdateTheoperationsTable(){ // to fill the operations  table with operation from DB
		DefaultTableModel DTM = (DefaultTableModel) table.getModel();
		String query ="select * from operationList ";
		con=sql_connect.ConnecrDB();
		try {
			pst=con.prepareStatement(query);
			rs=pst.executeQuery();
			
			while(rs.next())// keep adding the rows in RS to the table until it reach to the last row
			{
				String [] goodinfo={
					     String.valueOf(rs.getInt("op_id"))
						,rs.getString("type")
						,String.valueOf(rs.getString("customer_id"))
						,String.valueOf(rs.getString("amount"))
						,String.valueOf(df.format(rs.getLong("date")))
						};
			 System.out.println(goodinfo[1]);
			 DTM.addRow(goodinfo);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
	}
}
