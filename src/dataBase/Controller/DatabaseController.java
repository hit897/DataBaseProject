package dataBase.Controller;

import java.sql.*;

import javax.swing.JOptionPane;

public class DatabaseController
{
	// This is the String you send to the database.
	private String connectionString;
	// This is how you talk to the databases.
	private Connection databaseConnection;
	private DatabaseAppController baseController;
	private String currentQuery;

	/**
	 * This is the entire controller for controlling the the connection with the
	 * databases.
	 * 
	 * @date 03/04/15
	 */
	public DatabaseController(DatabaseAppController baseController)
	{
		connectionString = "jdbc:mysql://localhost?user=root";
		this.baseController = baseController;
		checkDriver();
		setupConnection();
	}

	/**
	 * This connects to the database.
	 */
	private void setupConnection()
	{
		try
		{
			databaseConnection = DriverManager.getConnection(connectionString);
		}
		catch (SQLException currentException)
		{
			displayErrors(currentException);
			JOptionPane.showMessageDialog(null, "Starting ANYWAYS, WITHOUT WORKING!");
		}

	}

	/**
	 * This clooses the connection.
	 */
	public void closeConnection()
	{
		try
		{
			databaseConnection.close();
		}
		catch (SQLException currentException)
		{
			displayErrors(currentException);
		}
	}

	/**
	 * This makes sure you have the correct drivers to connect to the database.
	 */
	private void checkDriver()
	{
		try
		{
			Class.forName("com.mysql.jdbc.Driver");

		}
		catch (Exception currentException)
		{
			displayErrors(currentException);
			System.exit(1);
		}
	}

	/**
	 * This displays the errors that occur.
	 */
	private void displayErrors(Exception currentException)
	{
		JOptionPane.showMessageDialog(baseController.getAppFrame(), currentException.getMessage());
		if (currentException instanceof SQLException)
		{
			JOptionPane.showMessageDialog(baseController.getAppFrame(), "SQL State: " + ((SQLException) currentException).getSQLState());
			JOptionPane.showMessageDialog(baseController.getAppFrame(), "SQL Error Code: " + ((SQLException) currentException).getErrorCode());
		}
	}

	/**
	 * This displays whatever tables are inside the database as a string.
	 * 
	 * @return returns the tables as Strings.
	 */
	public String displayTables()
	{
		String results = "";
		String query = "SHOW TABLES";

		try
		{
			Statement firstStatement = databaseConnection.createStatement();
			ResultSet answer = firstStatement.executeQuery(query);
			while (answer.next())
			{
				results += answer.getString(1) + "\n";
			}
			answer.close();
			firstStatement.close();
		}
		catch (SQLException currentSQLError)
		{
			displayErrors(currentSQLError);
		}

		return results;

	}

	/**
	 * This displays whatever is inside the tables as a string.
	 * 
	 * @return
	 */
	public String describeTables()
	{
		String results = "";
		String query = "DESCRIBE ";

		try
		{
			Statement firstStatement = databaseConnection.createStatement();
			ResultSet answer = firstStatement.executeQuery(query);
			while (answer.next())
			{
				results += answer.getString(1) + "\t" + answer.getString(2) + "\t" + answer.getString(3) + "\t" + answer.getString(4) + "\n";
			}
			answer.close();
			firstStatement.close();
		}
		catch (SQLException currentSQLError)
		{
			displayErrors(currentSQLError);
		}

		return results;

	}

	public String[][] tableInfo()
	{
		String[][] results;
		String query = "SHOW TABLES";
		try
		{
			Statement firstStatement = databaseConnection.createStatement();
			ResultSet answer = firstStatement.executeQuery(query);
			int rowCount;
			answer.last();
			rowCount = answer.getRow();
			answer.beforeFirst();
			results = new String[rowCount][1];

			while (answer.next())
			{
				results[answer.getRow() - 1][0] = answer.getString(1);
			}

			answer.close();
			firstStatement.close();
		}
		catch (SQLException currentSQLError)
		{
			results = new String[][] { { "empty" } };
			displayErrors(currentSQLError);
		}

		return results;
	}

	public String[] getMetaData()
	{
		String[] columnInformation = null;
		String query = "SHOW TABLES";

		try
		{
			Statement firstStatement = databaseConnection.createStatement();
			ResultSet answer = firstStatement.executeQuery(query);
			ResultSetMetaData myMeta = answer.getMetaData();

			columnInformation = new String[myMeta.getColumnCount()];
			for (int spot = 0; spot < myMeta.getColumnCount(); spot++)
			{
				columnInformation[spot] = myMeta.getColumnName(spot + 1);
			}

			answer.close();
			firstStatement.close();
		}
		catch (SQLException currentSQLError)
		{
			displayErrors(currentSQLError);
		}
		return columnInformation;

	}

	public String[][] realInfo()
	{
		String[][] results;
		String query = "SELECT * FROM `INNODB_SYS_COLUMNS`";

		try
		{
			Statement firstStatement = databaseConnection.createStatement();
			ResultSet answer = firstStatement.executeQuery(query);
			int columnCount = answer.getMetaData().getColumnCount();
			int rowCount;
			answer.last();
			rowCount = answer.getRow();
			answer.beforeFirst();
			results = new String[rowCount][columnCount];

			while (answer.next())
			{
				for (int col = 0; col < columnCount; col++)
				{
					results[answer.getRow() - 1][col] = answer.getString(col + 1);
				}
			}

			answer.close();
			firstStatement.close();
		}
		catch (SQLException currentSQLError)
		{
			results = new String[][] { { "errorProcessing" } };
			displayErrors(currentSQLError);
		}
		return results;
	}

	public String[][] selectQueryResults(String query)
	{
		this.currentQuery = query;
		String[][] results;

		try
		{
			if (checkForDataViolation())
			{
				throw new SQLException("Attemted illegal modification of data", ":( Done tried to mess up da data state :(", Integer.MIN_VALUE);
			}

			Statement firstStatement = databaseConnection.createStatement();
			ResultSet answer = firstStatement.executeQuery(query);
			int columnCount = answer.getMetaData().getColumnCount();
			int rowCount;
			answer.last();
			rowCount = answer.getRow();
			answer.beforeFirst();
			results = new String[rowCount][columnCount];

			while (answer.next())
			{
				for (int col = 0; col < columnCount; col++)
				{
					results[answer.getRow() - 1][col] = answer.getString(col + 1);
				}
			}

		}
		catch (SQLException currentSQLError)
		{
			results = new String[][] { { "errorProcessing" } };
			displayErrors(currentSQLError);
		}
		return results;
	}

	private boolean checkForDataViolation()
	{
		if (currentQuery.toUpperCase().contains(" DROP ") || currentQuery.toUpperCase().contains(" TRUNCATE ") || currentQuery.toUpperCase().contains(" SET ") || currentQuery.toUpperCase().contains(" ALTER "))
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	public int addToTable()
	{
		int rowsAffected = 0;
		String insertQuery = "INSERT INTO 'games'.'PC Games' " + "(`id`,`name`,`platform`,`number_of_players`) " + "VALUES (50, bobert, PC, 4);";

		try
		{
			Statement insertStatement = databaseConnection.createStatement();
			rowsAffected = insertStatement.executeUpdate(insertQuery);
			insertStatement.close();

		}
		catch (Exception currentException)
		{
			displayErrors(currentException);
		}

		return rowsAffected;
	}
}