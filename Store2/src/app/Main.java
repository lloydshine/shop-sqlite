package app;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.JLabel;               // Project Imports
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.event.ActionEvent;

public class Main extends JFrame implements CaretListener,ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4592376944561398346L;
	private JPanel contentPane;
	private JTextField productcodeTf;
	private JTextField descriptionTf;
	private JTextField priceTf;
	private JTextField quantityTf;            //Local Variables, can be accessed within the class
	private JTextField totalTf;
	private JTextField payTf;
	private JTextField changeTf;
	private JButton computeBtn;
	private JButton btnPay;
	private JMenuBar mb;
	private JMenu p1;
	private JMenuItem admin;

	private float total_price;
	private static ArrayList<Item> itemList = null;
	private static Item item = null;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DBConnector.createTable();
					Main frame = new Main();
					frame.setVisible(true);
				} catch (Exception e) {}
			}
		});
	}
	public Main() {
		itemList = DBConnector.getItems();
		setResizable(false);
		setTitle("Shop Manager");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(378, 550);
		setLocationRelativeTo(null);  
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5)); //Elements properties
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		mb = new JMenuBar();
		p1 = new JMenu("View");
		admin = new JMenuItem("Admin");
		admin.addActionListener(this);
		p1.add(admin);
		mb.add(p1);
		setJMenuBar(mb);
		
		JLabel label1 = new JLabel("Product Code:");
		label1.setFont(new Font("Tahoma", Font.PLAIN, 18));
		label1.setBounds(10, 20, 112, 22);
		label1.setHorizontalAlignment(SwingConstants.RIGHT);
		contentPane.add(label1);
		
		productcodeTf = new JTextField();
		productcodeTf.setBounds(128, 15, 224, 27);
		contentPane.add(productcodeTf);
		productcodeTf.setColumns(10);
		productcodeTf.addCaretListener(this); //CaretListener to keep track of this Textfield
		
		JLabel label2 = new JLabel("Description:");
		label2.setHorizontalAlignment(SwingConstants.RIGHT);
		label2.setFont(new Font("Tahoma", Font.PLAIN, 18));
		label2.setBounds(10, 69, 112, 22);
		contentPane.add(label2);
		
		descriptionTf = new JTextField();
		descriptionTf.setEditable(false);
		descriptionTf.setColumns(10);
		descriptionTf.setBounds(128, 64, 224, 27);
		contentPane.add(descriptionTf);
		
		JLabel label3 = new JLabel("Price:");
		label3.setHorizontalAlignment(SwingConstants.RIGHT);
		label3.setFont(new Font("Tahoma", Font.PLAIN, 18));
		label3.setBounds(10, 120, 112, 22);
		contentPane.add(label3);
		
		priceTf = new JTextField();
		priceTf.setEditable(false);
		priceTf.setColumns(10);
		priceTf.setBounds(128, 115, 224, 27);
		contentPane.add(priceTf);
		
		JLabel label4 = new JLabel("Quantity:");
		label4.setHorizontalAlignment(SwingConstants.RIGHT);
		label4.setFont(new Font("Tahoma", Font.PLAIN, 18));
		label4.setBounds(10, 173, 112, 22);
		contentPane.add(label4);
		
		quantityTf = new JTextField();
		quantityTf.setColumns(10);
		quantityTf.setBounds(128, 168, 224, 27);
		contentPane.add(quantityTf);
		
		computeBtn = new JButton("COMPUTE TOTAL AMOUNT");
		computeBtn.setFont(new Font("Tahoma", Font.BOLD, 14));
		computeBtn.setBounds(128, 221, 224, 36);
		contentPane.add(computeBtn);
		computeBtn.addActionListener(this); //Adds action listener to this button
		
		totalTf = new JTextField();
		totalTf.setEditable(false);
		totalTf.setColumns(10);
		totalTf.setBounds(128, 294, 224, 27);
		contentPane.add(totalTf);
		
		JLabel label5 = new JLabel("Total Amount:");
		label5.setHorizontalAlignment(SwingConstants.RIGHT);
		label5.setFont(new Font("Tahoma", Font.PLAIN, 17));
		label5.setBounds(10, 299, 112, 22);
		contentPane.add(label5);
		
		payTf = new JTextField();
		payTf.setColumns(10);
		payTf.setBounds(128, 340, 224, 27);
		contentPane.add(payTf);
		
		JLabel label6 = new JLabel("Pay:");
		label6.setHorizontalAlignment(SwingConstants.RIGHT);
		label6.setFont(new Font("Tahoma", Font.PLAIN, 18));
		label6.setBounds(10, 345, 112, 22);
		contentPane.add(label6);
		
		btnPay = new JButton("PAY");
		btnPay.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnPay.setBounds(128, 395, 224, 36);
		contentPane.add(btnPay);
		btnPay.addActionListener(this); //Action Listener
		
		changeTf = new JTextField();
		changeTf.setEditable(false);
		changeTf.setColumns(10);
		changeTf.setBounds(128, 449, 224, 27);
		contentPane.add(changeTf);
		
		JLabel label7 = new JLabel("Change:");
		label7.setHorizontalAlignment(SwingConstants.RIGHT);
		label7.setFont(new Font("Tahoma", Font.PLAIN, 17));
		label7.setBounds(10, 454, 112, 22);
		contentPane.add(label7);
	}
	
	@Override
	public void caretUpdate(CaretEvent e) {
		item = null;
		for(Item i : itemList) {
			try {
				if(i.getItemID() == Integer.parseInt(productcodeTf.getText())) {
					item = i;
				}
			} catch(Exception err) { break; }
		}
		if (item != null) {
			String str = String.format("%,.2f", item.getItemPrice());
			descriptionTf.setText(item.getItemName()); 
			priceTf.setText(str); 
		} else {
			descriptionTf.setText("");
			priceTf.setText("");
			totalTf.setText("");
			changeTf.setText("");   //Else set all the textfield to blank to reset!
			quantityTf.setText("");
			payTf.setText(" ");
		}
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == computeBtn) {
			if(priceTf.getText().length() != 0 && quantityTf.getText().length() != 0) {
				total_price = Float.parseFloat(priceTf.getText().replaceAll(",","")) * Integer.parseInt(quantityTf.getText());
				totalTf.setText(String.format("%,.2f", total_price));
			}
		} else if (e.getSource() == btnPay) { // action perform for btnPay
			if(priceTf.getText().length() != 0 && quantityTf.getText().length() != 0 && payTf.getText().length() != 0) {
				if (Integer.parseInt(payTf.getText()) >= total_price) { // checks if the payment > total price(local variable)
					float change = Integer.parseInt(payTf.getText()) - total_price;
					changeTf.setText(String.format("%,.2f", change));
				} else { 
					JOptionPane.showMessageDialog(this, "Not Enough Money!", "Error", 2);
				}
			}
		} else if(e.getSource() == admin) {
			this.dispose();
			Admin frame = new Admin();
			frame.setVisible(true);
		}
	}
}