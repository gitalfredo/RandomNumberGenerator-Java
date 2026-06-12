package rngbuilder;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.ScrollPaneConstants;
import javax.swing.ListSelectionModel;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

public class RandomNumberGeneratorWB {

	private JFrame frame;
	private JPopupMenu popup; // Right click popup on table row
	private JTable table;
	private JScrollPane scrollPane;


	Randomizer randomizer = new Randomizer();
	ArrayList<String> passwords = new ArrayList<>();

	TableModel tableModel;
	private JComboBox<String> comboBox;
	private JLabel lblControls;
	private JLabel lblNewLabel_1;
	private JMenuItem menuItemOpen;
	private JMenuItem menuItemClear;

	private JMenuItem contextCopy;    // Select option copy to transfer a single string to clipboard
	private JMenuItem contextAll;    // Select option copy all strings to clipboard
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {

					RandomNumberGeneratorWB window = new RandomNumberGeneratorWB();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 * @wbp.parser.entryPoint
	 */
	public RandomNumberGeneratorWB() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {

		frame = new JFrame();
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage("C:\\Users\\Alfredo\\EclipseWorkspace\\Java SWING\\res\\icon_scoreboard.png"));
		frame.setResizable(false);
		// Get favicon path and set ImageIcon
	 
		frame.setTitle("Random Number Generator");
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		

		JButton btnNewButton = new JButton("Generate");
		btnNewButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO: 1) Run randomizer at start; 2) Save initial comboBox value at start and then again when changing values
				// Get chosen byteSize
				String byteSizeStr = comboBox.getSelectedItem().toString();
				int byteSize = 0;
				try {
					byteSize = Integer.parseInt(byteSizeStr);
				} catch (NumberFormatException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} 
				 
				
				final int bytes = byteSize; 
				CompletableFuture.supplyAsync((Supplier<String>) ()->{
					String password = randomizer.generatePassword(bytes);
					
					return password;
					
				}).thenAccept((password)->{
					SwingUtilities.invokeLater(()->{
						passwords.add(password);
						// Update tableModel and reset
						tableModel.setPassList(passwords);
						table.revalidate();
					});					
				});				
			}
		});
		btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnNewButton.setBounds(293, 44, 133, 21);
		frame.getContentPane().add(btnNewButton);
		
		
		// Table ****************************************************
		// Define right click pop up
		popup = new JPopupMenu();
		// add menu items to popup
		
		
		// Copy single row to clipboard
		contextCopy = new JMenuItem("Copy");
		contextCopy.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				int row = table.getSelectedRow();
				String password = passwords.get(row);
				StringSelection strSelection = new StringSelection(password);
				Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
				clip.setContents(strSelection, strSelection);
				
			}
		});
		popup.add(contextCopy);		
		popup.addSeparator();
		
		// Copy single row to clipboard
		contextAll = new JMenuItem("Copy All Passwords");
		contextAll.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
				String str = "";
				for (int i = 0; i < passwords.size(); i++) {
					if (i<passwords.size()-1) 
						str += passwords.get(i) + "\n";									
					else
						str += passwords.get(i);
				}	
				StringSelection strSelection = new StringSelection(str);
				clip.setContents(strSelection, strSelection);
				
			}
		});
		popup.add(contextAll);

		
		tableModel = new TableModel(passwords);
		
		table = new JTable(tableModel);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);		
		
		table.addMouseListener(new MouseAdapter() {			
			@Override
			public void mouseReleased(MouseEvent e) {
				if(e.isPopupTrigger()) {
					// Check if mouse is at valid row
					int r = table.rowAtPoint(e.getPoint());  
					// Move selected row to valid row
					if (r>=0 && r<table.getRowCount())						
						table.setRowSelectionInterval(r, r);
					
					System.out.println("r="+r);
					popup.show(e.getComponent(), e.getX(), e.getY());
				}					
			}
			
		});
		
		
		//table.setBounds(10, 10, 273, 243);
		scrollPane = new JScrollPane(table);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBounds(10, 10, 273, 243);
		frame.getContentPane().add(scrollPane);
		
		comboBox = new JComboBox<>();
		comboBox.setToolTipText("Password Length");
		comboBox.setFont(new Font("Tahoma", Font.PLAIN, 12));
		comboBox.setModel(new DefaultComboBoxModel<>(new String[] {"20", "30", "40"}));
		comboBox.setSelectedIndex(1);
		comboBox.setBounds(353, 75, 73, 21);
		frame.getContentPane().add(comboBox);

		lblControls = new JLabel("Controls");
		lblControls.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblControls.setBounds(293, 11, 133, 23);
		frame.getContentPane().add(lblControls);

		lblNewLabel_1 = new JLabel("Length");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblNewLabel_1.setBounds(293, 75, 50, 21);
		frame.getContentPane().add(lblNewLabel_1);
		
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		JMenu menuFile = new JMenu("File");
		menuBar.add(menuFile);
		
		JMenuItem menuItemSave = new JMenuItem("Save");
		menuItemSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SwingUtilities.invokeLater(new Runnable() {

					@Override
					public void run() {
						// If empty list, exit
						if(passwords.isEmpty()) return;
						
						// Serialize
						try(FileOutputStream fileOutputStream = new FileOutputStream("serializedData.ser")){
						ObjectOutputStream objOutputStream = new ObjectOutputStream(fileOutputStream);
				 
						objOutputStream.writeObject(passwords);
							 
						objOutputStream.flush();	//Flush
						objOutputStream.close();	//Close
						fileOutputStream.close();
						
						JOptionPane.showMessageDialog(menuFile, "Data serialized to file 'serializedData.ser'");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					}
				});
			}
		});
		
		menuItemOpen = new JMenuItem("Open");
		menuItemOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SwingUtilities.invokeLater(new Runnable() {

					@Override
					public void run() {
						// Create a JFileChooser
						JFileChooser fileChooser = new JFileChooser(new File(System.getProperty("user.dir")));
						fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

						// Display the JFileChooser 
						int r = fileChooser.showOpenDialog(null);
						String fileName = "";   // Default fileName
						String path = null;
			            if (r == JFileChooser.APPROVE_OPTION) {
			                // set the label to the path of the selected directory
			            	fileName = fileChooser.getSelectedFile().getName();
			            	path = fileChooser.getSelectedFile().getAbsolutePath();			                System.out.println(fileName+" at "+fileChooser.getSelectedFile().getAbsolutePath()); 
			            }
			            // if the user cancelled the operation
			            else {
			            	System.out.println("the user cancelled the operation.. using default");
			            	return;
			            }						
						
			            // Only allow 'ser' extension
			            int dIndex = fileName.lastIndexOf('.');
			            String extension = fileName.substring(dIndex+1);
			            
						// Check if file exists and not directory
						if(!extension.equals("ser")) {
							JOptionPane.showMessageDialog(menuFile, "Error: only 'ser' extension allowed, got "+extension);
							return;
						}
						
						// Deserialize
						try(FileInputStream fileInputStream = new FileInputStream(path)){
							ObjectInputStream objInputStream = new ObjectInputStream(fileInputStream);
							passwords = (ArrayList<String>)objInputStream.readObject();
							objInputStream.close();
							fileInputStream.close(); 
							
							// Update gui
							tableModel.setPassList(passwords);
							table.revalidate();
							
							// Feedback
							JOptionPane.showMessageDialog(menuFile, "Loaded data from file '"+fileName+"'");
							
						} catch (FileNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (ClassNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}
					
				});
			}
		});
		menuFile.add(menuItemOpen);
		menuFile.add(menuItemSave);
		
		JMenuItem menuItemExit = new JMenuItem("Exit");
		menuItemExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
				SwingUtilities.invokeLater(new Runnable() {

					@Override
					public void run() {
						System.exit(0);						
					}
					
				});
			}
		});
		
		menuItemClear = new JMenuItem("Clear");
		menuItemClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SwingUtilities.invokeLater(new Runnable() {

					@Override
					public void run() {
						passwords.clear();
						
						tableModel.setPassList(passwords);
						table.revalidate();
						
					}
					
				});
			}
		});
		menuFile.add(menuItemClear);
		menuFile.add(menuItemExit);

		
	}
}
