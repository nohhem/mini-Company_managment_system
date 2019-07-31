import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

public class ExportGoodGui extends JFrame implements Customerparent {

	private JFrame frame;
	private JTable table;
	private JLabel lblCustomer;
	
	private ArrayList<Good> goodsarrl= new ArrayList<Good>();
	private DefaultTableModel tableModel;
	 Connection con =null ;
	 ResultSet rs=null;
	 PreparedStatement pst=null;
	private JComboBox comboBox_customer;
	private JDialog jdialog;
	private WarehouseMGui parent;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ExportGoodGui window = new ExportGoodGui();
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ExportGoodGui() {
		con=sql_connect.ConnecrDB();
		initialize();
	}
	public ExportGoodGui(WarehouseMGui parent) {
		con=sql_connect.ConnecrDB();
		initialize();
		this.parent=parent;
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		frame = new JFrame();
		frame.setBounds(100, 100, 789, 431);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				parent.frameisopened=false;
			}
		});
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(215, -1, 558, 382);
		frame.getContentPane().add(scrollPane);
		
		table = new JTable();
		table.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"good Code", "good name", "quantity", "price per unit", "total price", "Description"
			}
		) {
			boolean[] columnEditables = new boolean[] {
				false, false, false, true, false, false
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		scrollPane.setViewportView(table);
		
		JButton btnExportAGood = new JButton("Add a good to export");
		btnExportAGood.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				AddgoodGui addgoodGui = new AddgoodGui(ExportGoodGui.this, "export",null);
				
				
			}
		});
		btnExportAGood.setBounds(10, 11, 160, 23);
		frame.getContentPane().add(btnExportAGood);
		
		lblCustomer = new JLabel("Customer :");
		lblCustomer.setBounds(10, 109, 160, 23);
		frame.getContentPane().add(lblCustomer);
		
		
		comboBox_customer = new JComboBox();
		
		comboBox_customer.setBounds(10, 143, 160, 23);
		AutoCompleteDecorator.decorate(comboBox_customer);
		comboBox_customer.setModel(new DefaultComboBoxModel());
		frame.getContentPane().add(comboBox_customer);
		
		
		
		JButton btnAddANew = new JButton("Add a new Customer");
		btnAddANew.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				
			AddCustomerGui addCustomerGui = new AddCustomerGui(ExportGoodGui.this,null);
				
				
			}
		});
		btnAddANew.setBounds(10, 177, 160, 23);
		frame.getContentPane().add(btnAddANew);
		
		JButton btnFinish = new JButton("finish");
		btnFinish.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				// first an insertion wil be made to the operationList table
				// then an op_id will be obtain from the first inseration to make another insertions
				//to goodsofoperation table  with goods related to that operation
				
				String query ="insert into operationList (type,customer_id,amount,date)"
						+ " values (?,?,?,?);";
				try {
				if(table.getRowCount()!=0){
				System.out.println(con);
				
				con=sql_connect.ConnecrDB();
				pst=con.prepareStatement(query);
				pst.setString(1, "export");
				pst.setLong(2,((Customer) comboBox_customer.getSelectedItem()).getId());
				pst.setDouble(3, getTotalAmount());
				pst.setLong(4, new Date().getTime());
				pst.execute();
				
				
				
				pst=con.prepareStatement("select last_insert_rowid()"); // to get the op_id
				int opId= pst.executeQuery().getInt(1);
				System.out.println(" the operation_id is "+opId);
				
				// insert every good in the jtable into the goodsofopeartion table
				for (int i = 0; i < table.getModel().getRowCount(); i++) {
					 query ="insert into goodsofoperation (op_id,gd_name,gd_code,quantity,price)"
							+ " values (?,?,?,?,?)";
					 
					 double qunatityExported =(double) table.getModel().getValueAt(i, 2);
					 String goodCode=goodsarrl.get(i).getCode();
					 double price = (double) table.getModel().getValueAt(i, 3);
					 
					 pst=con.prepareStatement(query);
					 pst.setInt(1, opId);
					 pst.setString(2, goodsarrl.get(i).getName());
					 pst.setString(3, goodCode);
					 pst.setDouble(4,qunatityExported );
					 pst.setDouble(5,price );
					 
					 System.out.println(pst.toString());
					 pst.execute();
					 // subtract the quantities exported from the existing goods
					 query="update goods set quantityin = quantityin - ? WHERE code == ? ";
					 pst=con.prepareStatement(query);
					 pst.setDouble(1, qunatityExported);
					 pst.setString(2, goodCode);
					 pst.execute();
					 
					    
					 
				}
				
				 pst.close();
				 rs.close();
				 con.close();
				 frame.dispose();
				 }else{
					 JOptionPane.showMessageDialog(null, "you have not added any goods to the operation !!");
				 }
					
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		btnFinish.setBounds(10, 323, 160, 23);
		frame.getContentPane().add(btnFinish);
		 tableModel=((DefaultTableModel) table.getModel());
		
		 
		frame.setVisible(true);	
		
		 comboBox_customer.setModel(new DefaultComboBoxModel(getCustomerlistfromDatabase1()));
	}
	
	public String[] getCustomerlistfromDatabaseStrings(){
	 
	 String query= "select * from customers ";
	 try {
		pst=con.prepareStatement(query);
		rs=pst.executeQuery();
		String[] arr= new String [numOfRowsRs(rs)];
		int i=0;
		while(rs.next()){
			
			arr[i]=rs.getInt("cus_id")+" - "+rs.getString("cname");
			System.out.println(arr[i]);
			i++;
		}
		
		
		
		return arr;
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return null;
	
	 
	}
	
	public Customer[] getCustomerlistfromDatabase1(){
		System.out.println("getCustomerlistfromDatabase1 invoked");
		 
		 String query= "select * from customers ";
		 try {
			 con=sql_connect.ConnecrDB();
			System.out.println(con);
			
			pst=con.prepareStatement(query);
			rs=pst.executeQuery();
			Customer[] arr= new Customer [numOfRowsRs(rs)];
			rs=pst.executeQuery();
			int i=0;
			while(rs.next()){
				Customer customer = new Customer();
				customer.setName(rs.getString("cname"));
				customer.setId(rs.getInt("cus_id"));
				arr[i]=customer;
				System.out.println(arr[i]);
				i++;
				
			}
			rs.close();
			con.close();
			System.out.println(con.isClosed());
			System.out.println(con==null);
			return arr;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			
		}
		return null;
		
		 
		}
	public int numOfRowsRs(ResultSet rs){
		int count = 0;
		try {
			while (rs.next()) {
			    ++count;
			    System.out.println(count +" "+rs.getString(2));
			    // Get data from the current row and use it
			}
			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return count;
	}
	
	public void addGoodtoList(Good good){
		goodsarrl.add(good);
		updategoodslist(good);
		
	}
	public void updategoodslist(Good good){
			Good gd=good;
			String code=gd.getCode();
			String gname=gd.getName();
			double gquantity=gd.getquantity();
			double price=gd.getSellPrice();
			double totalprice=price*gquantity;
			String description=gd.getDescription();
			Object[] arr=new Object[10];
			arr[0]=code;
			arr[1]=gname;
			arr[2]=gquantity;
			arr[3]=price;
			arr[4]=totalprice;
			arr[5]=description;
			tableModel.addRow(arr);
			
		
	}
	
	public Double getTotalAmount(){
		Double total=0.0;
		for (int i = 0; i < table.getRowCount(); i++) {
			total=total+(Double) table.getModel().getValueAt(i, 4);
		}
		return total;
	}
	public void UpdateCustomerslist(){
		comboBox_customer.setModel(new DefaultComboBoxModel(getCustomerlistfromDatabase1()));
	}
}

