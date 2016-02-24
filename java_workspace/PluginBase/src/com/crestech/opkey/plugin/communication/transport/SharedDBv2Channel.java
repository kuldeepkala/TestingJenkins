package com.crestech.opkey.plugin.communication.transport;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

import javax.xml.bind.Marshaller;

public class SharedDBv2Channel extends TransportLayer {

	private Connection connection = null;
	private String dbPath;

	public SharedDBv2Channel(String dbPath) throws ClassNotFoundException {
		this.dbPath = dbPath;
		Class.forName("org.sqlite.JDBC"); // Registering JDBC Driver for SQLite
	}

	@Override
	public void run() {
		ResultSet resultSet = null;
		Statement statement = null;

		while (true) {
			try {
				takeRest();

				if (connection == null)
					return;

				statement = connection.createStatement();
				resultSet = statement.executeQuery("SELECT MSG_ID, MSG FROM OPKEY_TO_PLUGIN;");

				if (!resultSet.next())
					continue; // no message from OpKey

				if (Thread.interrupted())
					throw new InterruptedException();

				String MSG = resultSet.getString("MSG");
				String MSG_ID = resultSet.getString("MSG_ID");

				/*
				 * now delete the already read message from the table. this is
				 * something like dequeuing. this channel doesn't support seek
				 * operation.
				 */
				statement.execute("DELETE FROM OPKEY_TO_PLUGIN WHERE MSG_ID = '" + MSG_ID + "';");
				messageReceived(MSG);

			} catch (InterruptedException e) {
				return;

			} catch (Exception e) {
				e.printStackTrace();

			} finally {
				if (resultSet != null)
					try {
						resultSet.close();
					} catch (Exception e) {
					}

				if (statement != null)
					try {
						statement.close();
					} catch (Exception e) {
					}
			}
		}

	}

	private void takeRest() throws InterruptedException {
		Thread.sleep(10);
	}

	@Override
	public void SendMessage(Object msg, Marshaller messageSerializer) throws Exception {
		// generating a random message id
		String id = UUID.randomUUID().toString();
		String xmlRepresentation = serialize(msg, messageSerializer);

		PreparedStatement ps = null;

		try {
			ps = connection.prepareStatement("INSERT INTO PLUGIN_TO_OPKEY (msg_id, msg) VALUES ('" + id + "', ?);");
			ps.setString(1, xmlRepresentation);
			ps.execute();

		} finally {
			if (ps != null)
				ps.close();
		}

	}

	@Override
	protected void setup() throws Exception {
		connection = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
	}

	@Override
	protected void cleanup() {
		try {
			connection.close();

		} catch (SQLException e) {
			// don't bother. just eat up the exception
			e.printStackTrace();

		} finally {
			connection = null;
		}
	}

}
