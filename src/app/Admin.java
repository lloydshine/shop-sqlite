package app;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.JButton;
import javax.swing.JTable;

public class Admin extends JFrame implements ActionListener{

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField idTf;
	private JTextField nameTf;
	private JTextField priceTf;
	private JMenuBar mb;
	private JMenu p1;
	private JMenuItem buy;
	private JTable table;
	private JButton addbtn, removebtn, savebtn;
	
	private static ArrayList<Item> itemList = null;
	private static DefaultTableModel model = new DefaultTableModel();
	Object[] columns = {"ID","ItemName","Price"};
	Object[] rowdata = new Object[3];

	public Admin() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(378, 550);
		setLocationRelativeTo(null); 
		setResizable(false);
		setTitle("Admin Panel");
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		mb = new JMenuBar();
		p1 = new JMenu("View");
		buy = new JMenuItem("Buy");
		buy.addActionListener(this);
		p1.add(buy);
		mb.add(p1);
		setJMenuBar(mb);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 0, 362, 252);
		contentPane.add(scrollPane);
		
		table = new JTable();
		table.setDefaultEditor(Object.class, null);
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int selected = table.getSelectedRow();
				Item item = itemList.get(selected);
				idTf.setText(Integer.toString(item.getItemID()));
				nameTf.setText(item.getItemName());
				priceTf.setText(Float.toString(item.getItemPrice()));
			}
		});
		scrollPane.setViewportView(table);
		model.setColumnIdentifiers(columns);
		itemList = DBConnector.getItems();
		addValues();
		scrollPane.setViewportView(table);
		
		idTf = new JTextField();
		idTf.setBounds(23, 303, 97, 29);
		contentPane.add(idTf);
		idTf.setColumns(10);
		
		nameTf = new JTextField();
		nameTf.setColumns(10);
		nameTf.setBounds(130, 303, 97, 29);
		contentPane.add(nameTf);
		
		priceTf = new JTextField();
		priceTf.setColumns(10);
		priceTf.setBounds(237, 303, 97, 29);
		contentPane.add(priceTf);
		
		JLabel l1 = new JLabel("ID");
		l1.setFont(new Font("Tahoma", Font.PLAIN, 15));
		l1.setBounds(59, 278, 16, 14);
		contentPane.add(l1);
		
		JLabel l2 = new JLabel("NAME");
		l2.setFont(new Font("Tahoma", Font.PLAIN, 15));
		l2.setBounds(154, 278, 45, 14);
		contentPane.add(l2);
		
		JLabel l3 = new JLabel("PRICE");
		l3.setFont(new Font("Tahoma", Font.PLAIN, 15));
		l3.setBounds(260, 280, 45, 14);
		contentPane.add(l3);
		
		addbtn = new JButton("ADD");
		addbtn.setBounds(31, 373, 89, 23);
		contentPane.add(addbtn);
		addbtn.addActionListener(this);
		
		savebtn = new JButton("SAVE");
		savebtn.setBounds(140, 373, 76, 23);
		contentPane.add(savebtn);
		savebtn.addActionListener(this);
		
		removebtn = new JButton("REMOVE");
		removebtn.setBounds(237, 373, 89, 23);
		contentPane.add(removebtn);
		removebtn.addActionListener(this);
	}
	
	void addValues() {
		model.setRowCount(0);
		Collections.sort(itemList, Comparator.comparing(Item::getItemID));
		for(int x = 0; x < itemList.size(); x++) {
			rowdata[0] = itemList.get(x).getItemID();
			rowdata[1] = itemList.get(x).getItemName();
			rowdata[2] = itemList.get(x).getItemPrice();
			model.addRow(rowdata);
		}
		table.setModel(model);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String id = idTf.getText();
		String name = nameTf.getText();
		String price = priceTf.getText();
		int selected = table.getSelectedRow();
		Item item = null;
		if(e.getSource() == buy) {
			this.dispose();
			Main frame = new Main();
			frame.setVisible(true);
		} else if (e.getSource() == addbtn) {
			if(id.length() == 0 || name.length() == 0 || price.length() == 0) {
				JOptionPane.showMessageDialog(this, "Missing Fields!", "Error", 2);
			} else {
				try {
					if(DBConnector.findDuplicate(Integer.parseInt(id))) {
						JOptionPane.showMessageDialog(this, "Duplicate ID!", "Error", 2);
						return;
					}
					Item newitem = new Item(Integer.parseInt(id),name,Float.parseFloat(price));
					itemList.add(newitem);
					addValues();
					idTf.setText("");
					nameTf.setText("");
					priceTf.setText("");
					DBConnector.insertItem(newitem.getItemID(), newitem.getItemName(), newitem.getItemPrice());
				} catch(Exception error) {
					JOptionPane.showMessageDialog(this, "Enter Valid Values!", "Error", 2);
					error.printStackTrace();
					return;
				}
			}
		} else if (e.getSource() == removebtn) {
			if(selected == -1) {
				JOptionPane.showMessageDialog(this, "Select an Item!", "Error", 2);
				return;
			}
			item = itemList.get(selected);
			itemList.remove(selected);
			addValues();
			idTf.setText("");
			nameTf.setText("");
			priceTf.setText("");
			DBConnector.removeItem(item.getItemID());
	   } else if (e.getSource() == savebtn) {
		   if(selected == -1) {
				JOptionPane.showMessageDialog(this, "Select an Item!", "Error", 2);
				return;
		   } else {
			   if(id.length() == 0 || name.length() == 0 || price.length() == 0) {
					JOptionPane.showMessageDialog(this, "Missing Fields!", "Error", 2);
			   } else {
				   try {
					   item = itemList.get(selected);
					   if(item.getItemID() != Integer.parseInt(id)) {
						   if(DBConnector.findDuplicate(Integer.parseInt(id))) {
							   JOptionPane.showMessageDialog(this, "Duplicate ID!", "Error", 2);
							   return;
						   }
					   }
					   int currentid = item.getItemID();
					   item.setItemID(Integer.parseInt(id));
					   item.setItemName(name);
					   item.setItemPrice(Float.parseFloat(price));
					   addValues();
					   idTf.setText("");
					   nameTf.setText("");
					   priceTf.setText("");
					   DBConnector.editItem(currentid, item.getItemID(), item.getItemName(), item.getItemPrice());
				  } catch(Exception error) {
					  JOptionPane.showMessageDialog(this, "Enter Valid Values!", "Error", 2);
					  return;
				  }
			   }
		   }
	   }
	}
}
