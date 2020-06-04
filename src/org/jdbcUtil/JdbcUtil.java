package org.jdbcUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Utility class which provides methods to perform common jdbc database
 * operations.
 * 
 * @author Pinak Jakhar
 * @version 1.0.0
 */
public final class JdbcUtil implements IJdbcUtil {

	/**
	 * Prevents instantiation.
	 */
	private JdbcUtil() {
	}

	private static JdbcUtil jdbcUtil;

	/**
	 * Singleton instance
	 */
	public static JdbcUtil getInstance() {
		if (jdbcUtil == null) {
			synchronized (JdbcUtil.class) {
				if (jdbcUtil == null) {
					jdbcUtil = new JdbcUtil();
				}
			}
		}
		return jdbcUtil;
	}

	/**
	 * Fetch single record from the database table
	 *
	 * @param con    the connection instance
	 * @param query  the sql query
	 * @param params parameters to bind in the query
	 * @return the JSON object
	 * @throws Exception the exception
	 */
	@Override
	public synchronized final JSONObject findOne(final Connection con, final String query, final Object... params)
			throws Exception {
		Utility.queryStringNullCheck(query);
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			preparedStatement = con.prepareStatement(query);
			Utility.setParamsToPreparedStatement(preparedStatement, params);
			Logger.getGlobal().info(query);
			resultSet = preparedStatement.executeQuery();
			if (!resultSet.next()) {
				return new JSONObject();
			}
			return Utility.mapResultSetToJSON(resultSet);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			Utility.closeDBConnection(resultSet, preparedStatement, con);
		}
	}

	/**
	 * Fetch multiple records from the database table
	 *
	 * @param con    the connection instance
	 * @param query  the sql query
	 * @param params parameters to bind in the query
	 * @return the JSON array collection of json objects
	 * @throws Exception the exception
	 */
	@Override
	public synchronized final JSONArray findAll(final Connection con, final String query, final Object... params)
			throws Exception {
		Utility.queryStringNullCheck(query);
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		final JSONArray jsonArray = new JSONArray();
		try {
			preparedStatement = con.prepareStatement(query);
			Utility.setParamsToPreparedStatement(preparedStatement, params);
			Logger.getGlobal().info(query);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				jsonArray.put(Utility.mapResultSetToJSON(resultSet));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			Utility.closeDBConnection(resultSet, preparedStatement, con);
		}
		return jsonArray;
	}

	/**
	 * Create a new record in the database table
	 *
	 * @param con    the connection instance
	 * @param query  the sql query
	 * @param params parameters to bind in the query
	 * @return primary key of newly created record
	 * @throws Exception the exception
	 */
	@Override
	public synchronized final int insertAndGetId(final Connection con, final String query, final Object... params)
			throws Exception {
		Utility.queryStringNullCheck(query);
		PreparedStatement preparedStatement = null;
		ResultSet generatedKeys = null;
		int id = 0;
		try {
			con.setAutoCommit(false);
			preparedStatement = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			Utility.setParamsToPreparedStatement(preparedStatement, params);
			if (preparedStatement.executeUpdate() < 1) {
				return id;
			}
			generatedKeys = preparedStatement.getGeneratedKeys();
			if (generatedKeys.next()) {
				con.commit();
				id = generatedKeys.getInt(1);
			}
			return id;
		} catch (SQLException sqlException) {
			con.rollback();
			sqlException.printStackTrace();
			throw sqlException;
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		} finally {
			Utility.closeDBConnection(generatedKeys, preparedStatement, con);
		}
	}

	/**
	 * Create a new record in the database table
	 *
	 * @param con    the connection instance
	 * @param query  the sql query
	 * @param params parameters to bind in the query
	 * @return true if successfully inserted, false otherwise
	 * @throws Exception the exception
	 */
	@Override
	public synchronized final boolean insert(final Connection con, final String query, final Object... params)
			throws Exception {
		Utility.queryStringNullCheck(query);
		PreparedStatement preparedStatement = null;
		try {
			con.setAutoCommit(false);
			preparedStatement = con.prepareStatement(query);
			Utility.setParamsToPreparedStatement(preparedStatement, params);
			if (preparedStatement.executeUpdate() < 1) {
				return false;
			}
			con.commit();
			return true;
		} catch (SQLException sqlException) {
			con.rollback();
			sqlException.printStackTrace();
			throw sqlException;
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		} finally {
			Utility.closeDBConnection(null, preparedStatement, con);
		}
	}

	/**
	 * Update existing record in the database table
	 *
	 * @param con    the connection instance
	 * @param query  the sql query
	 * @param params parameters to bind in the query
	 * @return the total number of affected rows
	 * @throws Exception the exception
	 */
	@Override
	public synchronized final int update(final Connection con, final String query, final Object... params)
			throws Exception {
		Utility.queryStringNullCheck(query);
		PreparedStatement preparedStatement = null;
		int affectedRows = 0;
		try {
			con.setAutoCommit(false);
			preparedStatement = con.prepareStatement(query);
			Utility.setParamsToPreparedStatement(preparedStatement, params);
			affectedRows = preparedStatement.executeUpdate();
			if (affectedRows < 1) {
				return 0;
			}
			con.commit();
			return affectedRows;
		} catch (SQLException sqlException) {
			con.rollback();
			sqlException.printStackTrace();
			throw sqlException;
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		} finally {
			Utility.closeDBConnection(null, preparedStatement, con);
		}
	}

	/**
	 * Count the number of rows in a table according to the condition
	 *
	 * @param con    the connection instance
	 * @param query  the sql query
	 * @param params parameters to bind in the query
	 * @return the number of results
	 * @throws Exception the exception
	 */
	@Override
	public synchronized final int count(final Connection con, final String query, final Object... params)
			throws Exception {
		Utility.queryStringNullCheck(query);
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			preparedStatement = con.prepareStatement(query);
			Utility.setParamsToPreparedStatement(preparedStatement, params);
			Logger.getGlobal().info(query);
			resultSet = preparedStatement.executeQuery();
			return resultSet.getInt(1);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			Utility.closeDBConnection(resultSet, preparedStatement, con);
		}
	}

}
