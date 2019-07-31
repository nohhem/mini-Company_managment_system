import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import jdk.nashorn.internal.scripts.JO;

import javax.swing.JButton;
import javax.swing.SwingConstants;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.awt.event.ActionEvent;
import javax.swing.JTextPane;

public class WarehouseMGui extends JFrame{

	private JFrame frmWharehouseManagement;
	private JTable table;
	private MainGui parent;
	Connection con = null;
	ResultSet rs = null;
	PreparedStatement pst=null;
	public boolean frameisopened; // a variable to keep track if there is open frame or not
	
	private sql_connect sql =new sql_connect();
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
				WarehouseMGui window = new WarehouseMGui();
					window.frmWharehouseManagement.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public WarehouseMGui() {
		
		initialize();
		con=sql_connect.ConnecrDB();
		UpdateGoodslist();
		
	}
public WarehouseMGui(MainGui parent) {
		this.parent=parent;
		initialize();
		con=sql_connect.ConnecrDB();
		UpdateGoodslist();
		
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		
		frmWharehouseManagement = new JFrame();
		frmWharehouseManagement.setTitle("Wharehouse Management");
		frmWharehouseManagement.setBounds(100, 100, 862, 484);
		frmWharehouseManagement.getContentPane().setLayout(null);
		frmWharehouseManagement.setVisible(true);
		frmWharehouseManagement.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frmWharehouseManagement.addWindowListener(new WindowAdapter() {
			@Override
			
			public void windowClosed(WindowEvent e) {
				parent.openedframe=false;
			}
		});
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBounds(172, 0, 678, 444);
		frmWharehouseManagement.getContentPane().add(scrollPane);
		
		table = new JTable();
		table.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"good Code", "good name", "Quantity in", "Unit", "price of purchase", "price of sell", "Section", "Description"
			}
		) {
			boolean[] columnEditables = new boolean[] {
				false, false, false, false, false, false, false, false
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		table.getColumnModel().getColumn(1).setPreferredWidth(81);
		table.getColumnModel().getColumn(4).setPreferredWidth(102);
		scrollPane.setViewportView(table);
	
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		
		
		
		JButton btnAddANew = new JButton("add a new good");
		btnAddANew.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AddgoodGui addgoodGui= new AddgoodGui(WarehouseMGui.this,"add",null);
				
				
			}
		});
		btnAddANew.setBounds(10, 14, 152, 31);
		frmWharehouseManagement.getContentPane().add(btnAddANew);
		
		JButton btnEditAGood = new JButton("edit a good");
		btnEditAGood.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				Good good =null;
				try {
					
					String code = (String) table.getValueAt(table.getSelectedRow(), 0);
					String query= String.format("select * from goods where code = '%s' ", code);
					pst=con.prepareStatement(query);
					rs=pst.executeQuery();
					String name=rs.getString("name");
					Double quantity=rs.getDouble(sql.column_quantityin);
					String unit=rs.getString(sql.column_unit);
					Double originalprice=rs.getDouble(sql.column_priceoriginal);
					Double sellprice=rs.getDouble(sql.column_pricesell);
					String section=rs.getString(sql.column_section);
					String description =rs.getString(sql.column_description);
					
					 good = new Good(code, name, quantity, unit, originalprice, sellprice,section,description);
					
						System.out.println(good.toString());
						AddgoodGui addgoodGui= new AddgoodGui(WarehouseMGui.this,"edit",good);
						
				}catch(ArrayIndexOutOfBoundsException AOIE){
					JOptionPane.showMessageDialog(null, "please select a good to edit");
				}
				
				catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			
				
			}
		});
		btnEditAGood.setBounds(10, 56, 152, 31);
		frmWharehouseManagement.getContentPane().add(btnEditAGood);
		
		JButton btnInesrtAQuantity = new JButton("import");
		btnInesrtAQuantity.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(frameisopened)
					JOptionPane.showMessageDialog(WarehouseMGui.this, "Close The other frame before opening a new one !!");
				else{
					ImportGoodGui importgoodGui= new ImportGoodGui(WarehouseMGui.this);
					frameisopened=true;
				}
				
				
			}
		});
		btnInesrtAQuantity.setBounds(10, 165, 152, 37);
		frmWharehouseManagement.getContentPane().add(btnInesrtAQuantity);
		
		JButton btnExport= new JButton("export");
		btnExport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				if(frameisopened)
					JOptionPane.showMessageDialog(WarehouseMGui.this, "Close The other frame before opening a new one !!");
				else{
					ExportGoodGui exportGoodGui = new ExportGoodGui(WarehouseMGui.this);
					frameisopened=true;
				}
				
				
			}
		});
		btnExport.setBounds(10, 213, 152, 37);
		frmWharehouseManagement.getContentPane().add(btnExport);
		
		JButton btnShowTheLast = new JButton("last operations");
		btnShowTheLast.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if(frameisopened)
					JOptionPane.showMessageDialog(WarehouseMGui.this, "Close The other frame before opening a new one !!");
				else{
					LastOperationsGui lastOperationsGui = new LastOperationsGui(WarehouseMGui.this);
					frameisopened=true;
				}
				
				
				
			}
		});
		btnShowTheLast.setAlignmentX(1);
		btnShowTheLast.setBounds(10, 261, 152, 45);
		frmWharehouseManagement.getContentPane().add(btnShowTheLast);
		
		JButton btnRefresh = new JButton("refresh");
		btnRefresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				UpdateGoodslist();
			}
		});
		btnRefresh.setBounds(10, 379, 152, 23);
		frmWharehouseManagement.getContentPane().add(btnRefresh);
		
		JButton btnDeleteGood = new JButton("Delete good");
		btnDeleteGood.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
				con=sql_connect.ConnecrDB();
				String query = "delete from goods where code == ?";
					pst=con.prepareStatement(query);
					pst.setString(1, (String) table.getValueAt(table.getSelectedRow(),0));
					pst.execute();
					
					UpdateGoodslist();
					con.close();
					pst.close();
					
				}catch(ArrayIndexOutOfBoundsException aio){
					JOptionPane.showMessageDialog(null, "please select a good to delete ");
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		});
		btnDeleteGood.setBounds(10, 98, 152, 31);
		frmWharehouseManagement.getContentPane().add(btnDeleteGood);
		
	}
	public void AddNewGood(Good newgood) throws Exception{
		//this function receive a Good and insert it into the DB to goods table
		
		String code=newgood.getCode();
		String name=newgood.getName();
		Double quntity=newgood.getquantity();
		String unit=newgood.getUnit();
		Double purprice=newgood.getPurchasePrice();
		Double sellprice=newgood.getSellPrice();
		String section=newgood.getSection();
		String description = newgood.getDescription();
		
		
	
		
		
		try {
			
			//make a sql statment to exqute the  
			String query=String.format("insert into goods(%s,%s,%s,%s,%s,%s,%s,%s)"
					+ "values('%s','%s',%f ,'%s',%f,%f,'%s','%s')"
					,sql.column_code,sql.column_name
					,sql.column_quantityin,sql.column_unit
					,sql.column_priceoriginal,sql.column_pricesell
					,sql.column_section,sql.column_description
					,code,name,quntity,  unit,purprice,sellprice,  section,description);
					
			System.out.println("the good query string is "+query);
			pst=con.prepareStatement(query);
			
			/*
			pst.setString(1, fname);
			pst.setString(2,lname);
			pst.setInt(3, salary);*/
			
			pst.execute();
			
			System.out.println("saved !!");
			
			con.close();
			pst.close();
			rs.close();
			UpdateGoodslist();
			
		}catch(java.sql.SQLException se){
			if (se.getErrorCode()==0){
				JOptionPane.showMessageDialog(null, "the code you entered does already exist please write another one");
				throw se;
			}
			else{
				JOptionPane.showMessageDialog(null, se.getMessage());
				throw se;
			}
			
		}
		
		
	}
	public void UpdateGoodslist(){
		try {
			// update the goods table information from the DB
			con=sql.ConnecrDB();
			System.out.println("started reading good info from the DB");
			
			DefaultTableModel DTM=((DefaultTableModel) table.getModel());
			//clear every previous row before adding the updated rows
			int rowcount=DTM.getRowCount();
			for (int i =rowcount-1 ; i >=0; i--) {
				DTM.removeRow(i);
			}
			String query="select * from goods";
			// re read the database and get the update data
			pst=con.prepareStatement(query);
			rs=pst.executeQuery();
			System.out.println("the query of goods is done");
			
			
			 while (rs.next()){
				 String [] goodinfo={
						     rs.getString("code")
							,rs.getString("name")
							,String.valueOf(rs.getDouble("quantityin"))
							,String.valueOf(rs.getString("unit"))
							,String.valueOf(rs.getDouble("priceoriginal"))
							,String.valueOf(rs.getDouble("pricesell"))
							,String.valueOf(rs.getString("section"))
							,String.valueOf(rs.getString("description"))};
				 
				 DTM.addRow(goodinfo);
				
				
			}
			 
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
	}
	
	public void updateExistingGood(Good newgood){
		String code=newgood.getCode();
		String name=newgood.getName();
		Double quantity=newgood.getquantity();
		String unit=newgood.getUnit();
		Double purprice=newgood.getPurchasePrice();
		Double sellprice=newgood.getSellPrice();
		String section=newgood.getSection();
		String description = newgood.getDescription();
		
		
	
		
		
		try {
			
			//make a sql statment to exqute the  
		;
			
			String query1=String.format("UPDATE goods set "
			        +" %s=?,%s=?,%s=?,%s=?, %s=?,%s=?,%s=?,%s=?  where %s = ? "
					,sql.column_code,sql.column_name
					,sql.column_quantityin,sql.column_unit
					,sql.column_priceoriginal,sql.column_pricesell
					,sql.column_section,sql.column_description
					,sql.column_code);
			
			String query="update goods set "+sql.column_description+" = ? where ;";
			System.out.println("the good query string is "+query1);
			con.close();
			pst.close();
			rs.close();
			con=sql.ConnecrDB();
			pst=con.prepareStatement(query1,Statement.RETURN_GENERATED_KEYS);
			pst.setString(1, code);
			pst.setString(2,name);
			pst.setDouble(3,quantity );
			pst.setString(4, unit);
			pst.setDouble(5, purprice);
			pst.setDouble(6, sellprice);
			pst.setString(7, section);
			pst.setString(8, description);
			pst.setString(9, code);
			
			//pst.setString(1, description);
			
			/*
			pst.setString(1, fname);
			pst.setString(2,lname);
			pst.setInt(3, salary);*/
			
			pst.executeUpdate();
			
			System.out.println(pst.getGeneratedKeys().getString(1));
			System.out.println("saved !!");
			con.close();
			pst.close();
			rs.close();
			
			UpdateGoodslist();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
	}
}
