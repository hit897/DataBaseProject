package dataBase.View;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import dataBase.Controller.DatabaseAppController;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SpringLayout;
import javax.swing.table.DefaultTableModel;

public class DatabasePanel extends JPanel
{
	private DatabaseAppController baseController;
	private JButton appButton;
	private JTextArea displayArea;
	private JScrollPane displayPane;
	private SpringLayout baseLayout;
	private JTable dataTable;
	private JPasswordField password;
	
	public DatabasePanel(DatabaseAppController baseController)
	{
		this.baseController = baseController;
		appButton = new JButton("test the query");
		displayArea = new JTextArea(10, 30);
		displayPane = new JScrollPane(displayArea);
		baseLayout = new SpringLayout();
		password = new JPasswordField(null, 20);
		
		
		setupTable();
		setupDisplayPane();
		setupPanel();
		setupLayout();
		setupListeners();
	}
	
	private void setupTable()
	{
		dataTable = new JTable(new DefaultTableModel(baseController.getDatabase().tableInfo(), baseController.getDatabase().getMetaData()));
		
		displayPane = new JScrollPane(dataTable);

	}
	
	private void setupDisplayPane()
	{
		displayArea.setWrapStyleWord(true);
		displayArea.setLineWrap(true);
		displayArea.setEditable(false);
	}
	
	private void setupPanel()
	{
		this.setBackground(Color.MAGENTA);
		this.setLayout(baseLayout);
		this.add(appButton);
		this.add(displayPane);
		this.add(password);
		password.setEchoChar('♂');
		//♂
		password.setForeground(Color.BLUE);
		
	}

	private void setupLayout()
	{
		baseLayout.putConstraint(SpringLayout.WEST, displayPane, 74, SpringLayout.WEST, this);
		baseLayout.putConstraint(SpringLayout.EAST, displayPane, 310, SpringLayout.WEST, this);
		baseLayout.putConstraint(SpringLayout.WEST, password, 102, SpringLayout.WEST, this);
		baseLayout.putConstraint(SpringLayout.NORTH, displayPane, 85, SpringLayout.NORTH, this);
		baseLayout.putConstraint(SpringLayout.SOUTH, displayPane, -124, SpringLayout.SOUTH, this);
		baseLayout.putConstraint(SpringLayout.SOUTH, password, -6, SpringLayout.NORTH, displayPane);
		baseLayout.putConstraint(SpringLayout.NORTH, appButton, 11, SpringLayout.SOUTH, displayPane);
		baseLayout.putConstraint(SpringLayout.WEST, appButton, 0, SpringLayout.WEST, displayPane);
	}

	private void setupListeners()
	{
		appButton.addActionListener(new ActionListener()
		{

			
			public void actionPerformed(ActionEvent click)
			{
				String databaseAnswer = baseController.getDatabase().displayTables();
				displayArea.setText(databaseAnswer);				
			}
		});
	}
}
