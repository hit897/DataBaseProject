package dataBase.View;

import javax.swing.JFrame;

import dataBase.Controller.DatabaseAppController;

public class DatabaseFrame extends JFrame
{
	private DatabasePanel basePanel;

	public DatabaseFrame(DatabaseAppController baseController)
	{
		basePanel = new DatabasePanel(baseController);
		setupFrame();
	}

	private void setupFrame()
	{
		this.setContentPane(basePanel);
		this.setSize(1000, 1000);
		setVisible(true);
	}

}
