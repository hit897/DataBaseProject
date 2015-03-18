package dataBase.Controller;

import dataBase.View.DatabaseFrame;

public class DatabaseAppController
{
	private DatabaseFrame appFrame;
	private DatabaseController database;

	public DatabaseAppController()
	{
		database = new DatabaseController(this);
		appFrame = new DatabaseFrame(this);
	}

	public void start()
	{

	}

	public DatabaseFrame getAppFrame()
	{
		return appFrame;
	}
	
	public DatabaseController getDatabase()
	{
		return database;
	}
}
