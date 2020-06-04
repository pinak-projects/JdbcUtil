/**
 * @author Pinak Jakhar
 */
package org.jdbcUtil;

import java.sql.Connection;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * The Interface IJdbcUtil.
 */
interface IJdbcUtil {

	/**
	 * Find one.
	 *
	 * @param con the connection instance
	 * @param query the query
	 * @param params the params
	 * @return the JSON object
	 * @throws Exception the exception
	 */
	JSONObject findOne(Connection con, String query, Object... params) throws Exception;

	/**
	 * Find all.
	 *
	 * @param con the connection instance
	 * @param query the query
	 * @param params the params
	 * @return the JSON array
	 * @throws Exception the exception
	 */
	JSONArray findAll(Connection con, String query, Object... params) throws Exception;

	/**
	 * Insert.
	 *
	 * @param con the connection instance
	 * @param query the query
	 * @param params the params
	 * @return the true or false
	 * @throws Exception the exception
	 */
	boolean insert(Connection con, String query, Object... params) throws Exception;
	
	/**
	 * InsertAndGetId.
	 *
	 * @param con the connection instance
	 * @param query the query
	 * @param params the params
	 * @return the int
	 * @throws Exception the exception
	 */
	int insertAndGetId(Connection con, String query, Object... params) throws Exception;

	/**
	 * Update.
	 *
	 * @param con the connection instance
	 * @param query the query
	 * @param params the params
	 * @return the int
	 * @throws Exception the exception
	 */
	int update(Connection con, String query, Object... params) throws Exception;

	/**
	 * Count.
	 *
	 * @param con the connection instance
	 * @param query the query
	 * @param params the params
	 * @return the int
	 * @throws Exception the exception
	 */
	int count(Connection con, String query, Object... params) throws Exception;

}
