import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

public class AddgoodGui {
	private JComboBox comboBox_unit, comboBox_section;

	private JFrame frmInesrtGoodInformation;
	private JTextField textField_name;
	private JTextField textField_Quantity;
	private JTextField textField_purchasePrice;
	private JTextField textfield_sellPrice;
	private JTextField textField_description;

	private JComboBox comboBox_code;
	private sql_connect sql = new sql_connect();
	Connection con = null;
	ResultSet rs = null;
	PreparedStatement pst = null;

	private JRadioButton rdbtnNewGood, rdbtnExistGood;

	private JButton btnAdd;
	private Object parent;

	private DefaultComboBoxModel comboBoxModel_code;

	private JDialog jdialog;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AddgoodGui window = new AddgoodGui(null, "add", null);

					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	/*
	 * public AddgoodGui(WarehouseMGui parent) { initialize( parent);
	 * 
	 * }
	 */

	public AddgoodGui(Object parent, String type, Good good) {
		con = sql_connect.ConnecrDB();
		initialize(parent, type, good);

	}

	/**
	 * Initialize the contents of the frame.
	 */
	@SuppressWarnings("deprecation")
	private void initialize(Object parent, String type, Good good) {
	     jdialog = new JDialog((Frame) parent, "insert good info", true);
	   
		
		
		frmInesrtGoodInformation = new JFrame();
		frmInesrtGoodInformation.setBounds(100, 100, 289, 407);
		frmInesrtGoodInformation.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frmInesrtGoodInformation.getContentPane().setLayout(null);
		frmInesrtGoodInformation.setResizable(true);

		JLabel lblGoodCode = new JLabel("Code");
		lblGoodCode.setBounds(10, 57, 75, 22);
		lblGoodCode.setHorizontalAlignment(SwingConstants.LEFT);
		frmInesrtGoodInformation.getContentPane().add(lblGoodCode);

		JLabel lblGoodName = new JLabel("name");
		lblGoodName.setBounds(10, 78, 75, 22);
		lblGoodName.setHorizontalAlignment(SwingConstants.LEFT);
		frmInesrtGoodInformation.getContentPane().add(lblGoodName);

		JLabel lblGoodQuantity = new JLabel("Quantity");
		lblGoodQuantity.setBounds(10, 105, 75, 22);
		lblGoodQuantity.setHorizontalAlignment(SwingConstants.LEFT);
		frmInesrtGoodInformation.getContentPane().add(lblGoodQuantity);

		JLabel lblGoodUnit = new JLabel("Unit");
		lblGoodUnit.setBounds(10, 126, 67, 20);
		lblGoodUnit.setHorizontalAlignment(SwingConstants.LEFT);
		frmInesrtGoodInformation.getContentPane().add(lblGoodUnit);

		JLabel lblPrice = new JLabel("Purchase Price");
		lblPrice.setBounds(10, 156, 75, 22);
		frmInesrtGoodInformation.getContentPane().add(lblPrice);

		JLabel lblGoodInformation = new JLabel("good information :");
		lblGoodInformation.setBounds(10, 11, 198, 22);
		lblGoodInformation.setFont(new Font("Tahoma", Font.BOLD, 13));
		frmInesrtGoodInformation.getContentPane().add(lblGoodInformation);

		textField_name = new JTextField();
		textField_name.setBounds(95, 79, 140, 20);
		frmInesrtGoodInformation.getContentPane().add(textField_name);
		textField_name.setColumns(10);

		textField_Quantity = new JTextField();
		textField_Quantity.setBounds(95, 106, 140, 20);
		frmInesrtGoodInformation.getContentPane().add(textField_Quantity);
		textField_Quantity.setColumns(10);

		textField_purchasePrice = new JTextField();
		textField_purchasePrice.setBounds(95, 157, 140, 20);
		frmInesrtGoodInformation.getContentPane().add(textField_purchasePrice);
		textField_purchasePrice.setColumns(10);

		JButton btnOk = new JButton("OK");
		btnOk.setBounds(10, 334, 89, 23);
		frmInesrtGoodInformation.getContentPane().add(btnOk);
		btnOk.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				try {
				if (type == "add") {

					Good good = extractgoodinformationfromGui();
					// System.out.println(good.toString());
					((WarehouseMGui) parent).AddNewGood(good);
					jdialog.dispose();
				} else if (type == "edit") {
					// System.out.println(good.toString());
					Good good = extractgoodinformationfromGui();
					((WarehouseMGui) parent).updateExistingGood(good);// update
																		// the
																		// good
																		// info
																		// on DB
					jdialog.dispose();
				} else if (parent instanceof ExportGoodGui) {
					
					rdbtnNewGood.disable();
					//Good good = getgoodinfoFromDatabase((String) comboBox_code.getSelectedItem());
					Good good= new Good();
					good.setCode((String) comboBox_code.getSelectedItem());
					good.setSellPrice(Double.valueOf(textfield_sellPrice.getText()));
					good.setquantity(Double.valueOf(textField_Quantity.getText()));
					
					// to check if there is enough amount to export
					con=sql.ConnecrDB();
					String query="select * from goods where code ==?";
					pst=con.prepareStatement(query);
					pst.setString(1, good.getCode());
					rs=pst.executeQuery();
					
					Double quantityInwarehouse=rs.getDouble("quantityin");
					con.close();
					rs.close();
					pst.close();
					if(quantityInwarehouse< good.getquantity()){
						JOptionPane.showMessageDialog(null, "the quantity you selected isn't available "
								+ "\n, the current quantity of this good is "+quantityInwarehouse);
						
					}else{
						System.out.println(good.toString());
						((ExportGoodGui) parent).addGoodtoList(good);
						jdialog.dispose();
					}	
						
				}
				frmInesrtGoodInformation.dispose();
				
				}catch(NumberFormatException nfe){
					JOptionPane.showMessageDialog(null, "please enter the information correctly");
				}catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				

			}
		});

		JLabel lblSection = new JLabel("Section");
		lblSection.setBounds(10, 209, 46, 14);
		frmInesrtGoodInformation.getContentPane().add(lblSection);

		comboBox_section = new JComboBox(new String[] { "A1", "A2" });
		comboBox_section.setBounds(95, 209, 140, 20);
		comboBox_section.setEditable(true);
		AutoCompleteDecorator.decorate(comboBox_section);

		frmInesrtGoodInformation.getContentPane().add(comboBox_section);

		JLabel lblSellprice = new JLabel("SellPrice");
		lblSellprice.setBounds(10, 184, 75, 14);
		frmInesrtGoodInformation.getContentPane().add(lblSellprice);

		textfield_sellPrice = new JTextField();
		textfield_sellPrice.setBounds(95, 181, 140, 20);
		frmInesrtGoodInformation.getContentPane().add(textfield_sellPrice);
		textfield_sellPrice.setColumns(10);

		textField_description = new JTextField();
		textField_description.setBounds(10, 257, 232, 66);

		textField_description.setColumns(10);

		JLabel lblDescription = new JLabel("description:");
		lblDescription.setBounds(10, 240, 140, 20);
		frmInesrtGoodInformation.getContentPane().add(lblDescription);

		comboBox_unit = new JComboBox(new String[] { "kg", "pcs", "ton", "liter", "m3" });

		comboBox_unit.setBounds(95, 126, 89, 21);
		frmInesrtGoodInformation.getContentPane().add(comboBox_unit);

		btnAdd = new JButton("Add");
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String unit = JOptionPane.showInputDialog("please enter a new unit to add");
				comboBox_unit.addItem(unit);
			}
		});
		btnAdd.setBounds(194, 125, 61, 23);
		frmInesrtGoodInformation.getContentPane().add(btnAdd);

		frmInesrtGoodInformation.getContentPane().add(textField_description);

		JPanel panel = new JPanel();
		panel.setBounds(10, 32, 225, 22);
		frmInesrtGoodInformation.getContentPane().add(panel);
		panel.setLayout(null);

		rdbtnNewGood = new JRadioButton("new good");
		rdbtnNewGood.setBounds(0, 0, 109, 23);
		panel.add(rdbtnNewGood);

		rdbtnExistGood = new JRadioButton("exist good");
		rdbtnExistGood.setBounds(111, 0, 109, 23);
		panel.add(rdbtnExistGood);
		ButtonGroup btngroup = new ButtonGroup();
		rdbtnExistGood.setSelected(true);
		btngroup.add(rdbtnExistGood);
		btngroup.add(rdbtnNewGood);
		ActionListener rbtnsAction = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (rdbtnExistGood.isSelected()) {
					setImportstyleExistgood();
				} else if (rdbtnNewGood.isSelected()) {
					setImportstyleNewgood();
				}

			}
		};
		rdbtnExistGood.addActionListener(rbtnsAction);
		rdbtnNewGood.addActionListener(rbtnsAction);

		comboBox_code = new JComboBox();
		comboBox_code.setBounds(95, 58, 138, 20);
		frmInesrtGoodInformation.getContentPane().add(comboBox_code);
		comboBoxModel_code = new DefaultComboBoxModel();
		comboBox_code.setModel(comboBoxModel_code);
		comboBox_code.setEditable(true);
		AutoCompleteDecorator.decorate(comboBox_code);

		if (type == "edit") {
			frmInesrtGoodInformation.setTitle("Edit a good");
			comboBox_code.setSelectedItem(good.getCode());
			textField_name.setText(good.getName());
			textField_Quantity.setText(String.valueOf(good.getquantity()));
			comboBox_unit.setSelectedItem(good.getUnit());
			textField_purchasePrice.setText(String.valueOf(good.getPurchasePrice()));
			textfield_sellPrice.setText(String.valueOf(good.getSellPrice()));
			comboBox_section.setSelectedItem(good.getSection());
			textField_description.setText(good.getDescription());
			rdbtnExistGood.hide();
			rdbtnNewGood.hide();
			System.out.println("hidden");

		} else if (type == "add") {
			rdbtnExistGood.hide();
			rdbtnNewGood.hide();

		} else if (type == "import") {
			if (rdbtnExistGood.isSelected()) {
				setImportstyleExistgood();
			}

		} else if (type == "export") {
			frmInesrtGoodInformation.setTitle("Inesrt good information");
			System.out.println("export mode");
			setexportstyle();

		}
		
		jdialog.setContentPane(frmInesrtGoodInformation.getContentPane());
		jdialog.setSize(frmInesrtGoodInformation.getSize());
		jdialog.setVisible(true);
		frmInesrtGoodInformation.setVisible(false);
	}

	public void setexportstyle() {
		setImportstyleExistgood();
		rdbtnNewGood.setEnabled(false);
		textField_purchasePrice.setEditable(false);
		textfield_sellPrice.setEditable(true);

	}

	private void setImportstyleExistgood() {
		System.out.println("import exist good");
		comboBox_code.setEditable(false);
		comboBox_code.setEnabled(true);

		textField_name.setEditable(false);
		comboBox_unit.setEnabled(false);
		btnAdd.setEnabled(false);
		textfield_sellPrice.setEditable(false);
		comboBox_section.setEnabled(false);
		textField_description.setEditable(false);
		String[] arr = getGoodsCodes((WarehouseMGui) parent);

		for (int i = 0; i < arr.length; i++) {
			comboBox_code.addItem(arr[i]);
		}
		// comboBox_code = new JComboBox(arr);

	}

	public void setImportstyleNewgood() {
		System.out.println("import new good");
		textField_name.setEditable(true);
		comboBox_code.setEditable(true);

		comboBox_code.removeAllItems();
		textField_name.setEditable(true);
		textField_Quantity.setEditable(true);
		comboBox_unit.setEnabled(true);
		btnAdd.setEnabled(true);
		textField_purchasePrice.setEditable(true);
		textfield_sellPrice.setEditable(true);
		comboBox_section.setEnabled(true);
		textField_description.setEditable(true);
	}

	public String[] getGoodsCodes(WarehouseMGui parent) {
		// a function that get the codes already exist in the database
		String[] arr = null;
		try {

			String query = "select code from goods ";
			pst = con.prepareStatement(query);
			rs = pst.executeQuery();
			ArrayList<String> codearrlist = new ArrayList<String>();
			while (rs.next()) {
				codearrlist.add(rs.getString("code"));

			}
			arr = new String[codearrlist.size()];
			codearrlist.toArray(arr);
			for (String s : arr)
				System.out.println(s);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return arr;
	}

	public Good extractgoodinformationfromGui() {
		try{
			
		
		String code = (String) comboBox_code.getSelectedItem();
		String name = textField_name.getText();
		double quantity = Double.valueOf(textField_Quantity.getText());
		String unit = (String) comboBox_unit.getSelectedItem();
		double purchaseprice = Double.valueOf(textField_purchasePrice.getText());
		double sellPrice = Double.valueOf(textfield_sellPrice.getText());
		String section = comboBox_section.getSelectedItem().toString();
		String description = textField_description.getText();
		Good good = new Good(code, name, quantity, unit, purchaseprice, sellPrice, section, description);
		return good;
		}catch(NumberFormatException NFE){
			JOptionPane.showMessageDialog(null, "please insert the information correctly");
			return null;
		}
		
	}

	private Good getgoodinfoFromDatabase(String goodcode) {
		String code = goodcode;

		String query = "select * from goods where code = ?";

		try {
			con=sql.ConnecrDB();
			pst = con.prepareStatement(query);
			pst.setString(1, code);
			rs = pst.executeQuery();
			String[] goodinfo = { rs.getString("code"), rs.getString("name"),
					String.valueOf(rs.getDouble("quantityin")), String.valueOf(rs.getString("unit")),
					String.valueOf(rs.getDouble("priceoriginal")), String.valueOf(rs.getDouble("pricesell")),
					String.valueOf(rs.getString("section")), String.valueOf(rs.getString("description")) };
			Good good = new Good(goodinfo[0], goodinfo[1], goodinfo[2], goodinfo[3], goodinfo[4], goodinfo[5]);
			System.out.println(good);

			return good;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}