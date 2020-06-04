/**
 * @author Pinak Jakhar
 */
package org.jdbcUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class contains utility static methods.
 */
final class Utility {

	/**
	 * Prevents instantiation.
	 */
	private Utility() {
	}

	/**
	 * Put value from ResultSet to JSON Object.
	 *
	 * @param resultSetMetaData the result set meta data
	 * @param colNo             the column number in resultset
	 * @param resultSet         the result set
	 * @param jsonObject        the json object
	 * @throws JSONException the JSON exception
	 * @throws SQLException  the SQL exception
	 */
	public synchronized static void putValueToJSON(final ResultSetMetaData resultSetMetaData, final int colNo,
			final ResultSet resultSet, final JSONObject jsonObject) throws JSONException, SQLException {
		switch (resultSetMetaData.getColumnType(colNo)) {
		case Types.ARRAY:
			jsonObject.put(resultSetMetaData.getColumnName(colNo), resultSet.getArray(colNo));
			break;
		case Types.BIGINT:
			jsonObject.put(resultSetMetaData.getColumnName(colNo), resultSet.getInt(colNo));
			break;
		case Types.BOOLEAN:
			jsonObject.put(resultSetMetaData.getColumnName(colNo), resultSet.getBoolean(colNo));
			break;
		case Types.BLOB:
			jsonObject.put(resultSetMetaData.getColumnName(colNo), resultSet.getBlob(colNo));
			break;
		case Types.DOUBLE:
			jsonObject.put(resultSetMetaData.getColumnName(colNo), resultSet.getDouble(colNo));
			break;
		case Types.FLOAT:
			jsonObject.put(resultSetMetaData.getColumnName(colNo), resultSet.getFloat(colNo));
			break;
		case Types.INTEGER:
			jsonObject.put(resultSetMetaData.getColumnName(colNo), resultSet.getInt(colNo));
			break;
		case Types.NVARCHAR:
			jsonObject.put(resultSetMetaData.getColumnName(colNo), resultSet.getNString(colNo));
			break;
		case Types.VARCHAR:
			jsonObject.put(resultSetMetaData.getColumnName(colNo), resultSet.getString(colNo));
			break;
		case Types.TINYINT:
			jsonObject.put(resultSetMetaData.getColumnName(colNo), resultSet.getInt(colNo));
			break;
		case Types.SMALLINT:
			jsonObject.put(resultSetMetaData.getColumnName(colNo), resultSet.getInt(colNo));
			break;
		case Types.DATE:
			jsonObject.put(resultSetMetaData.getColumnName(colNo), resultSet.getDate(colNo));
			break;
		case Types.TIMESTAMP:
			jsonObject.put(resultSetMetaData.getColumnName(colNo), resultSet.getTimestamp(colNo));
			break;
		default:
			jsonObject.put(resultSetMetaData.getColumnName(colNo), resultSet.getObject(colNo));
			break;
		}
	}

	/**
	 * Sets the binding parameters to prepared statement.
	 *
	 * @param preparedStatement the prepared statement
	 * @param params            the params
	 * @throws SQLException the SQL exception
	 */
	public synchronized static void setParamsToPreparedStatement(PreparedStatement preparedStatement, Object[] params)
			throws SQLException {
		if (params.length > 0) {
			for (int i = 0; i < params.length; i++) {
				preparedStatement.setObject(i + 1, params[i]);
			}
		}
	}

	/**
	 * Check if the sql query string is null or empty.
	 *
	 * @param query the sql query
	 */
	public synchronized static void queryStringNullCheck(String query) {
		if (query == null || query.isEmpty()) {
			throw new NullPointerException("SQL query must not be null or empty.");
		}
	}

	/**
	 * Map result set to JSON Object.
	 *
	 * @param resultSet the result set
	 * @return the JSON object
	 * @throws SQLException the SQL exception
	 */
	public synchronized static final JSONObject mapResultSetToJSON(final ResultSet resultSet) throws SQLException {
		final ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
		final JSONObject jsonObject = new JSONObject();
		for (int i = 1; i <= resultSetMetaData.getColumnCount(); i++) {
			putValueToJSON(resultSetMetaData, i, resultSet, jsonObject);
		}
		return jsonObject;
	}

	/**
	 * Close DB connection.
	 *
	 * @param resultSet         the result set instance
	 * @param preparedStatement the prepared statement instance
	 * @param con               the connection instance
	 */
	public synchronized static void closeDBConnection(ResultSet resultSet, PreparedStatement preparedStatement, Connection con) {
		try {
			if (resultSet != null && con != null && preparedStatement != null) {
				resultSet.close();
			}

			if (preparedStatement != null && con != null) {
				preparedStatement.close();
			}

			if (con != null) {
				con.close();
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}
}
