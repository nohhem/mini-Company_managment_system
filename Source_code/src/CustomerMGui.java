import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.sun.glass.events.WindowEvent;
import com.sun.org.apache.xpath.internal.WhitespaceStrippingElementMatcher;

import javax.swing.JScrollPane;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.awt.event.ActionEvent;
import javax.swing.JSlider;

public class CustomerMGui implements Customerparent {

	private JFrame frame;
	private JTable table;
	private MainGui parent;
	Connection con = sql_connect.ConnecrDB();
	ResultSet rs = null;
	PreparedStatement pst=null;
	private DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CustomerMGui window = new CustomerMGui();
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
	public CustomerMGui() {
		initialize();
		UpdateCustomerslist();
	}
	public CustomerMGui(MainGui parent) {
		this.parent=parent;
		initialize();
		UpdateCustomerslist();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 763, 489);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(java.awt.event.WindowEvent e) {
				parent.openedframe=false;
			}
		});

			
		frame.getContentPane().setLayout(null);
		frame.setVisible(true);
		
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(129, 11, 618, 393);
		frame.getContentPane().add(scrollPane);
		
		table = new JTable();
		scrollPane.setViewportView(table);
		table.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"cus_id", "Name", "company name", "Phone No", "Phone No2", "address", "date added"
			}
		) {
			boolean[] columnEditables = new boolean[] {
					false, false, false, false, false, false, false
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		table.getColumnModel().getColumn(2).setPreferredWidth(119);
		
		JButton btnAddNewCustomer = new JButton("new customer");
		btnAddNewCustomer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AddCustomerGui addCustomerGui = new AddCustomerGui(CustomerMGui.this,"add");
			}
		});
		btnAddNewCustomer.setBounds(0, 11, 113, 23);
		frame.getContentPane().add(btnAddNewCustomer);
		
		JButton btndelete = new JButton("delete");
		btndelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
				int id = Integer.parseInt((String) table.getValueAt(table.getSelectedRow(), 0));
				String query=String.format("DELETE FROM customers  WHERE cus_id =%d ", id);
				
					pst=con.prepareStatement(query);
					pst.execute();
					UpdateCustomerslist();
				}catch(ArrayIndexOutOfBoundsException aio){
					JOptionPane.showMessageDialog(null, "please choose a customer to delete");
				}catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				
			}
		});
		btndelete.setBounds(0, 40, 113, 23);
		frame.getContentPane().add(btndelete);
		
		JButton btnEdit = new JButton("Edit");
		btnEdit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try{
					
				
				int srow=table.getSelectedRow();
				int id = Integer.valueOf((String) table.getValueAt(srow, 0));
				String name=(String) table.getValueAt(srow, 1);
				String companyName=(String)table.getValueAt(srow, 2);
				String phone1 = (String)table.getValueAt(srow, 3);
				String phone2=(String) table.getValueAt(srow, 4);
				String address=(String) table.getValueAt(srow, 5);
				
				Customer customer = new Customer(id, name, companyName, phone1, phone2, address);
				
				AddCustomerGui addCustomerGui = new AddCustomerGui(CustomerMGui.this,"edit",customer);
			
				}catch(ArrayIndexOutOfBoundsException aio){
					JOptionPane.showMessageDialog(null, "please choose a Customer to edit !");
				}catch (Exception e) {
					
				}
				}
				
		});
		btnEdit.setBounds(0, 74, 113, 23);
		frame.getContentPane().add(btnEdit);
	}
	public void UpdateCustomerslist(){
		try {
			System.out.println("started reading customers info from the DB");
			
			DefaultTableModel DTM=((DefaultTableModel) table.getModel());
			//clear the existing rows in the table
			int rowcount=DTM.getRowCount();
			for (int i =rowcount-1 ; i >=0; i--) {
				DTM.removeRow(i);
			}
			
			String query="select * from customers";
			System.out.println(query);
			pst=con.prepareStatement(query);
			rs=pst.executeQuery();
			
			
			 while (rs.next()){
				String [] empinfo={String.valueOf(rs.getInt(1)),rs.getString(2),rs.getString(3)
						,rs.getString(4),rs.getString(5),rs.getString(6),
						df.format(new Date(rs.getLong("date_added")))
						};
				System.out.println(empinfo[1]);
				DTM.addRow(empinfo);
				
				//System.out.println("the employee "+rs.getString("fname")+ "with the id "+rs.getInt("id"));

			}
				
			}catch(SQLException sqe){
				System.out.println("sql ex");
				sqe.getSQLState();
				sqe.getMessage();
			}
			catch (Exception e) {
				System.out.println("general");
				// TODO: handle exception
				e.getMessage();
				
				e.printStackTrace();
			}
	}

	
}
