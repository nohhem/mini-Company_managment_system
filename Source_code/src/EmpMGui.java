import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;
import javax.xml.ws.FaultAction;

import com.sun.org.apache.bcel.internal.generic.FNEG;

import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.ConnectException;
import java.awt.event.ActionEvent;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EmpMGui {
	
	private String Columnid="id",Columnfname="fname",Columnlname="lname",Columnsalary="msalary";


	// when updating the payments to know if it is the total or for an employee
	//total,emp
	String flag="total";
	
	private MainGui parent;
	private DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
	public JFrame frmEmployeesManagment;
	private JTextField textFieldid;
	private JTextField textFieldfname;
	private JTextField textFieldlaname;
	private JTextField textFieldsalary;
	private String [] column_headers={"Id","first name","last name","salary"};
	private String str[][]={};
	
	private JTable table_Employeeslist;
	
	Connection con = null;
	ResultSet rs = null;
	PreparedStatement pst=null;
	private JTable table_payments;
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					EmpMGui empwindow = new EmpMGui();
					
			
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public EmpMGui() {
		con=sql_connect.ConnecrDB();
		initialize();
		UpdateEmployeelist();
		UpdateEmployeePayments();
		frmEmployeesManagment.setVisible(true);
		
	}
	public EmpMGui(MainGui parent) {
		
		con=sql_connect.ConnecrDB();
		initialize();
		UpdateEmployeelist();
		UpdateEmployeePayments();
		frmEmployeesManagment.setVisible(true);
		frmEmployeesManagment.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.parent=parent;
		
		
	}
	
		


	/**
	 * Initialize the contents of the frame.
	 */
	@SuppressWarnings("serial")
	private void initialize() {
	
		
		frmEmployeesManagment = new JFrame();
		frmEmployeesManagment.setTitle("Employees Managment");
		frmEmployeesManagment.setBounds(100, 100, 1055, 521);
		frmEmployeesManagment.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmEmployeesManagment.getContentPane().setLayout(null);
		frmEmployeesManagment.addWindowListener(new WindowAdapter() {
		
			@Override
			public void windowClosed(WindowEvent e) {
				parent.openedframe=false;
			}
		});
		
		JLabel lblId = new JLabel("id");
		lblId.setBounds(10, 11, 54, 29);
		frmEmployeesManagment.getContentPane().add(lblId);
		
		JLabel lblFirstName = new JLabel("first name");
		lblFirstName.setBounds(10, 51, 59, 29);
		frmEmployeesManagment.getContentPane().add(lblFirstName);
		
		JLabel lblLastName = new JLabel("last name");
		lblLastName.setBounds(10, 91, 69, 29);
		frmEmployeesManagment.getContentPane().add(lblLastName);
		
		JLabel lblSalary = new JLabel("salary");
		lblSalary.setBounds(10, 131, 59, 29);
		frmEmployeesManagment.getContentPane().add(lblSalary);
		
		textFieldid = new JTextField();
		textFieldid.setBounds(74, 15, 102, 20);
		textFieldid.setEditable(false);
		textFieldid.setEnabled(false);
		frmEmployeesManagment.getContentPane().add(textFieldid);
		textFieldid.setColumns(10);
		
		textFieldfname = new JTextField();
		textFieldfname.setBounds(74, 55, 102, 20);
		frmEmployeesManagment.getContentPane().add(textFieldfname);
		textFieldfname.setColumns(10);
		
		textFieldlaname = new JTextField();
		textFieldlaname.setBounds(74, 95, 102, 20);
		frmEmployeesManagment.getContentPane().add(textFieldlaname);
		textFieldlaname.setColumns(10);
		
		textFieldsalary = new JTextField();
		textFieldsalary.setBounds(74, 135, 102, 20);
		textFieldsalary.setText("");
		frmEmployeesManagment.getContentPane().add(textFieldsalary);
		textFieldsalary.setColumns(10);
		
		JButton btnSave = new JButton("Add new Employee");
		btnSave.setBounds(10, 215, 133, 23);
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String fname=textFieldfname.getText();
				String lname=textFieldlaname.getText();
				int salary = Integer.parseInt(textFieldsalary.getText());
				
				try {
					String query1="insert into employees(?,?,?)values(?,?,?)";
					String query=String.format("insert into employees(%s,%s,%s)values('%s','%s',%d)",
							Columnfname,Columnlname,Columnsalary,fname,lname,salary);
					System.out.println("the query string is "+query);
					pst=con.prepareStatement(query);
					
					/*
					pst.setString(1, fname);
					pst.setString(2,lname);
					pst.setInt(3, salary);*/
					
					pst.execute();
					System.out.println("saved !!");
					UpdateEmployeelist();
					
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			
			}
		});
		frmEmployeesManagment.getContentPane().add(btnSave);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(180, 11, 339, 432);
		frmEmployeesManagment.getContentPane().add(scrollPane);
		
		
		DefaultTableModel model= new DefaultTableModel();
		table_Employeeslist = new JTable(model);
		table_Employeeslist.setModel(new DefaultTableModel(
				new Object[][] {},new String[] {"id", "name", "last name", "salary"}) {
			boolean[] columnEditables = new boolean[] {
				false, false, false, false
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		table_Employeeslist.getColumnModel().getColumn(3).setPreferredWidth(90);
		scrollPane.setViewportView(table_Employeeslist);
		 
		table_Employeeslist.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
		    @Override
		    public void valueChanged(ListSelectionEvent event) {
		        if (table_Employeeslist.getSelectedRow() > -1) {
		            // print first column value from selected row
		            //System.out.println(table.getValueAt(table.getSelectedRow(), 0).toString());
		        	String id = (String) table_Employeeslist.getValueAt(table_Employeeslist.getSelectedRow(), 0);
		            String fname=(String) table_Employeeslist.getValueAt(table_Employeeslist.getSelectedRow(), 1);
		            String lname=(String) table_Employeeslist.getValueAt(table_Employeeslist.getSelectedRow(), 2);
		            String salary=(String) table_Employeeslist.getValueAt(table_Employeeslist.getSelectedRow(),3);
		            textFieldid.setText(id);
		            textFieldfname.setText(fname);
		            textFieldlaname.setText(lname);
		            textFieldsalary.setText(salary);
		            
		            UpdateEmployeePaymentsWithId(Integer.valueOf(id));
		        }
		    }
		});
		
		JButton btnUpdate = new JButton("update info");
		btnUpdate.setBounds(10, 249, 130, 23);
		btnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int id = Integer.parseInt(textFieldid.getText());
				String fname=textFieldfname.getText();
				String lname=textFieldlaname.getText();
				int salary = Integer.parseInt(textFieldsalary.getText());
				String query=String.format("UPDATE employees set %s='%s',%s='%s',%s=%d where %s = %d " ,
						Columnfname,fname,Columnlname,lname,Columnsalary,salary,Columnid,id);
				System.out.println("the query "+query);
				
				try {
					
					pst=con.prepareStatement(query);
					pst.executeUpdate();
					UpdateEmployeelist();
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				
				
			}
		});
		frmEmployeesManagment.getContentPane().add(btnUpdate);
		

		
		JButton btnReadData = new JButton("Refresh Data");
		btnReadData.setBounds(13, 398, 130, 23);
		btnReadData.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				UpdateEmployeelist();
		}});
		frmEmployeesManagment.getContentPane().add(btnReadData);
		btnReadData.hide();
		
		JButton btnDelete = new JButton("delete");
		btnDelete.setBounds(10, 283, 130, 20);
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				if(table_Employeeslist.getSelectedRow()!= -1){
				
				int option=JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this Employee ?");
				System.out.println(option + "the option number");
				
				 if (option==0){
					int id = Integer.parseInt((String) table_Employeeslist.getValueAt(table_Employeeslist.getSelectedRow(), 0));
					String query=String.format("DELETE FROM employees  WHERE id =%d ", id);
					try {
						pst=con.prepareStatement(query);
						pst.execute();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					UpdateEmployeelist();
				  }
					
			}else if(table_Employeeslist.getSelectedRow()== -1)
				JOptionPane.showMessageDialog(null, "please select an Employee to delet ");
				}
			
		});
		frmEmployeesManagment.getContentPane().add(btnDelete);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(659, 11, 380, 432);
		frmEmployeesManagment.getContentPane().add(scrollPane_1);
		
		table_payments = new JTable();
		table_payments.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Amount", "description", "date", "Emp Name", "Emp id"
			}
		) {
			boolean[] columnEditables = new boolean[] {
				false, false, false, false, false
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		table_payments.getColumnModel().getColumn(1).setPreferredWidth(110);
		scrollPane_1.setViewportView(table_payments);
		String label = "<html>" + "Show Employee" + "<br>" + " payments" + "</html>";
		JButton btnShowEmplpayments = new JButton(label);
		btnShowEmplpayments.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				flag="emp";
			int id =Integer.valueOf((String) table_Employeeslist.getValueAt(table_Employeeslist.getSelectedRow(), 0));
			UpdateEmployeePaymentsWithId(id);
			}
		});
		btnShowEmplpayments.setBounds(529, 32, 120, 51);
		frmEmployeesManagment.getContentPane().add(btnShowEmplpayments);
		btnShowEmplpayments.hide();
		
		 label = "<html>" + "Show Total" + "<br>" + " payments" + "</html>";
		JButton btnShowTotalPayments = new JButton(label);
		btnShowTotalPayments.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				flag="total";
				UpdateEmployeePayments();
			}
			
		});
		btnShowTotalPayments.setBounds(529, 94, 123, 51);
		frmEmployeesManagment.getContentPane().add(btnShowTotalPayments);
		
		JButton btnPay = new JButton("pay salary");
		btnPay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
			int selectedrow=table_Employeeslist.getSelectedRow();
			int id = Integer.valueOf((String) table_Employeeslist.getValueAt(selectedrow, 0)) ;
			String fname =(String) table_Employeeslist.getValueAt(selectedrow, 1);
			String lname=(String) table_Employeeslist.getValueAt(selectedrow,2);
			String fullname=fname+" "+lname;
			Double salary= Double.valueOf((String) table_Employeeslist.getValueAt(table_Employeeslist.getSelectedRow(), 3));
			String description = "salary payment";
			long time= new Date().getTime();
			
			System.out.println(id);
			
			
				/*pst=con.prepareStatement("select msalary from employees  where id = "+id);
				rs=pst.executeQuery();
			
			 salary=rs.getInt("msalary");
			*/
			String query=String.format("insert into employees_payments (%s,%s,%s,%s,%s) values('%s','%s',%d,'%s',%d)",
					"amount","description","date","emp_name","emp_id",salary,description,time,fullname,id);
			System.out.println("the insert query string is "+query);
			
			System.out.println(query);
			pst=con.prepareStatement(query);
			pst.execute();
			
		
				UpdateEmployeePaymentsWithId(id);
			

			}catch(ArrayIndexOutOfBoundsException AOE){
				JOptionPane.showMessageDialog(null, "choose an Employee !!");
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			}
		});
		btnPay.setBounds(10, 314, 130, 20);
		frmEmployeesManagment.getContentPane().add(btnPay);
		
		
	}
	
		public void UpdateEmployeelist(){
			// read the payments of all employees and display it to the table
			try {
				System.out.println("started reading info from the DB");
				
				DefaultTableModel DTM=((DefaultTableModel) table_Employeeslist.getModel());
				int rowcount=DTM.getRowCount();
				for (int i =rowcount-1 ; i >=0; i--) {
					DTM.removeRow(i);
				}
				String query="select * from employees ";
				pst=con.prepareStatement(query);
				rs=pst.executeQuery();
				
				
				 while (rs.next()){
					String [] empinfo={String.valueOf(rs.getInt("id"))
							,rs.getString("fname")
							,rs.getString("lname")
							,rs.getInt("msalary")+""};
					DTM.addRow(empinfo);
					//System.out.println("the employee "+rs.getString("fname")+ "with the id "+rs.getInt("id"));
					
				}
				/* pst.close();
					rs.close();
					con.close();*/
				}catch(SQLException sqe){
					System.out.println("sql ex");
					sqe.getSQLState();
					sqe.getMessage();
					
				}
					catch (Exception e) {
				System.out.println("general exeption");
					// TODO: handle exception
					e.printStackTrace();
				}
		}
		
		public void UpdateEmployeePayments(){
			try {
				System.out.println("started reading ppayments info from the DB");
				
				DefaultTableModel DTM=((DefaultTableModel) table_payments.getModel());
				//clear the existing rows in the table
				int rowcount=DTM.getRowCount();
				for (int i =rowcount-1 ; i >=0; i--) {
					DTM.removeRow(i);
				}
				
				String query="select * from employees_payments";
				System.out.println(query);
				pst=con.prepareStatement(query);
				rs=pst.executeQuery();
				
				
				 while (rs.next()){
					String [] empinfo={String.valueOf(rs.getDouble("amount"))
							,rs.getString("description")
							,df.format(new Date(rs.getLong("date")))
							,rs.getString("emp_name")
							,String.valueOf(rs.getInt("emp_id"))};
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

		public void UpdateEmployeePaymentsWithId(int id){
			// read the payments of specific employee from DB and display it on the table
			try {
				System.out.println("started reading ppayments info from the DB");
				
				DefaultTableModel DTM=((DefaultTableModel) table_payments.getModel());
				//clear the existing rows in the table
				int rowcount=DTM.getRowCount();
				for (int i =rowcount-1 ; i >=0; i--) {
					DTM.removeRow(i);
				}
				
				String query="select * from employees_payments where emp_id="+id;
				System.out.println(query);
				pst=con.prepareStatement(query);
				rs=pst.executeQuery();
				
				
				 while (rs.next()){
					String [] empinfo={String.valueOf(rs.getDouble("amount"))
							,rs.getString("description")
							,df.format(new Date(rs.getLong("date")))
							,rs.getString("emp_name")
							,String.valueOf(rs.getInt("emp_id"))};
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
		public void closeSqlConnection(){
			try {
				pst.close();
				rs.close();
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		}
}

