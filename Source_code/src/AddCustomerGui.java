import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.GridLayout;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import javax.swing.JTextField;
import javax.swing.JPanel;
import com.jgoodies.forms.layout.FormSpecs;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.awt.event.ActionEvent;



public class AddCustomerGui {

	private Customerparent parent;
	private String mode;
	private JFrame frame;
	private JTextField textfield_customer_name;
	private JTextField textField_company;
	private JTextField textfield_phone1;
	private JTextField textfield_phone2;
	private JTextField textField_address;

	Connection con = sql_connect.ConnecrDB();
	ResultSet rs = null;
	PreparedStatement pst=null;
	private Customer customer;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AddCustomerGui window = new AddCustomerGui(null,null);
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


	public AddCustomerGui(Customerparent parent, String mode, Customer customer) {
		// TODO Auto-generated constructor stub
		this.parent=parent;
		this.mode=mode;
		this.customer=customer;
		initialize();
	}
	public AddCustomerGui(Customerparent parent,String mode) {
		// TODO Auto-generated constructor stub
		this.mode=mode;
		this.parent=parent;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 413, 255);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setVisible(true);
		
		JLabel lblCustomerName = new JLabel("customer name");
		lblCustomerName.setBounds(10, 11, 97, 21);
		frame.getContentPane().add(lblCustomerName);
		
		JLabel lblCompanyName = new JLabel("Company name");
		lblCompanyName.setBounds(10, 43, 97, 21);
		frame.getContentPane().add(lblCompanyName);
		
		JLabel lblPhoneNo = new JLabel("Phone No");
		lblPhoneNo.setBounds(10, 75, 97, 21);
		frame.getContentPane().add(lblPhoneNo);
		
		JLabel lblPhoneNo_1 = new JLabel("Phone No2");
		lblPhoneNo_1.setBounds(10, 107, 97, 21);
		frame.getContentPane().add(lblPhoneNo_1);
		
		JLabel lblAddress = new JLabel("address");
		lblAddress.setBounds(10, 139, 97, 21);
		frame.getContentPane().add(lblAddress);
		
		textfield_customer_name = new JTextField();
		textfield_customer_name.setBounds(117, 11, 280, 21);
		frame.getContentPane().add(textfield_customer_name);
		textfield_customer_name.setColumns(10);
		
		textField_company = new JTextField();
		textField_company.setColumns(10);
		textField_company.setBounds(117, 43, 280, 21);
		frame.getContentPane().add(textField_company);
		
		textfield_phone1 = new JTextField();
		textfield_phone1.setColumns(10);
		textfield_phone1.setBounds(117, 75, 280, 21);
		frame.getContentPane().add(textfield_phone1);
		
		textfield_phone2 = new JTextField();
		textfield_phone2.setColumns(10);
		textfield_phone2.setBounds(117, 107, 280, 21);
		frame.getContentPane().add(textfield_phone2);
		
		textField_address = new JTextField();
		textField_address.setColumns(10);
		textField_address.setBounds(117, 139, 280, 21);
		frame.getContentPane().add(textField_address);
		
		if (mode=="edit"){
			textfield_customer_name.setText(customer.getName());
			textField_company.setText(customer.getAddress());
			textfield_phone1.setText(customer.getPhone1());
			textfield_phone2.setText(customer.getPhone2());
			textField_address.setText(customer.getAddress());
		}
		JButton btnAdd = new JButton("Add");
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(textfield_customer_name.getText().isEmpty()&& textField_company.getText().isEmpty())
					JOptionPane.showMessageDialog(null, "Both Names can't be empty !!");
				else if (mode=="add" || mode==null){ // to add a new customer to the DB
					try {
					String name = textfield_customer_name.getText();
					String companyName=textField_company.getText();
					String phoneNo1=textfield_phone1.getText();
					String phoneNo2=textfield_phone2.getText();
				    String addresss=textField_address.getText();
				    long time= new Date().getTime();
				    
				    String query=String.format("insert into customers (%s,%s,%s,%s,%s,%s) values (?,?,?,?,?,?)"
				    		,"cname","company_name","phone","phone2","address","date_added");
				    System.out.println(query);
				    
						pst=con.prepareStatement(query);
						pst.setString(1, name);
						pst.setString(2, companyName);
						pst.setString(3, phoneNo1);
						pst.setString(4, phoneNo2);
						pst.setString(5, addresss);
						pst.setLong(6, time);
						
						pst.execute();
						
						 parent.UpdateCustomerslist();
							frame.dispose();
					}catch(NumberFormatException nfe){
						JOptionPane.showMessageDialog(null, "please insert the info correctly");
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				   
				}
				else if (mode=="edit"){
					
					
					try {
					int id = customer.getId();
					String name = textfield_customer_name.getText();
					String companyName=textField_company.getText();
					String phoneNo1=textfield_phone1.getText();
					String phoneNo2=textfield_phone2.getText();
				    String addresss=textField_address.getText();
				    long time= new Date().getTime();
				    String query="UPDATE customers set cname=?,company_name=?"
				    		+ ",phone=?,phone2=?,address=? where cus_id = ? " ;
							
				    System.out.println(query);
				    
						pst=con.prepareStatement(query);
						pst.setString(1, name);
						pst.setString(2, companyName);
						pst.setString(3, phoneNo1);
						pst.setString(4, phoneNo2);
						pst.setString(5, addresss);
						pst.setInt(6,id);
						
						pst.executeUpdate();
						
						 parent.UpdateCustomerslist();
						 frame.dispose();
					}catch(NumberFormatException nfe){
						JOptionPane.showMessageDialog(null, "please insert the info correctly");
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				   
				}
			    
			}
		});
		btnAdd.setBounds(298, 171, 89, 23);
		frame.getContentPane().add(btnAdd);
	}
	
}
