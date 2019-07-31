import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.awt.event.ActionEvent;

public class MainGui {

	private JFrame mainframe;
	
	// to indicate if there is frame working before instancing a new one 
	boolean openedframe=false; 
	
	

	
  
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainGui window = new MainGui();
					window.mainframe.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainGui() {
		initialize();
		
		sql_connect.con=sql_connect.ConnecrDB();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		mainframe = new JFrame();
		mainframe.setBounds(100, 100, 743, 448);
		mainframe.setDefaultCloseOperation(0);// TO DO NOTHING ON CLOSE 
		mainframe.getContentPane().setLayout(null);
		mainframe.addWindowListener(new WindowAdapter() {
			
			@Override
			public void windowClosing(WindowEvent e) {

				int num=JOptionPane.showConfirmDialog(mainframe, "Are you sure you want to exit ?");
				if(num==0)
					mainframe.dispose();
				else{
					
				}
			}
		
			
			
		});
		JButton btnEmpM = new JButton("Employees Managment");
		btnEmpM.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (openedframe==false){
					openedframe=true;
					EmpMGui empwindow = new EmpMGui(MainGui.this);
			
					
				}else{
					
					JOptionPane.showMessageDialog(mainframe, "please close the other frame !!");
				}
				
				
				
			}
		});
		btnEmpM.setBounds(10, 11, 190, 38);
		mainframe.getContentPane().add(btnEmpM);
		
		JButton btnWharehouseMangament = new JButton("Warehouse management");
		btnWharehouseMangament.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				if (openedframe==false){
					openedframe=true;
					WarehouseMGui warehouseMGui=new WarehouseMGui(MainGui.this);				
				}else{
					JOptionPane.showMessageDialog(mainframe, "please close the other frame !!");
				}
			}
		});
		btnWharehouseMangament.setBounds(10, 60, 190, 38);
		mainframe.getContentPane().add(btnWharehouseMangament);
		
		JButton btnCurstomersManagmaent = new JButton("Curstomers managment");
		btnCurstomersManagmaent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (openedframe==false){
					openedframe=true;
					CustomerMGui customerMGui = new CustomerMGui(MainGui.this);
				
				}else{
					JOptionPane.showMessageDialog(mainframe, "please close the other frame !!");
				}
			}
		});
		btnCurstomersManagmaent.setBounds(10, 109, 190, 38);
		mainframe.getContentPane().add(btnCurstomersManagmaent);
	}
}
