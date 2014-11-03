package model;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

/**
 * User Model for MongoDB First Project
 * 
 * @author Iskandar Setiadi
 * @version 0.1, by IS @since November 2, 2014
 *
 */

public class User {

	/** List of Attributes */
	private String username;
	private String password;
	
	public static String COLLECTION_NAME = "Users";

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	// For security, don't provide getter for password

	public void setPassword(String password) {
		this.password = password;
	}

	public void save(DB db) {
		DBCollection coll = db.getCollection(COLLECTION_NAME);
		BasicDBObject doc = new BasicDBObject("username", username).append("password", password);
		coll.insert(doc);
	}

	public boolean login(DB db) {
		boolean ret = false;
		DBCollection coll = db.getCollection(COLLECTION_NAME);
		
		BasicDBObject query = new BasicDBObject("username", username);
		
		DBObject doc = coll.findOne(query);
		
		if (doc != null)
			if (((String) doc.get("password")).equals(password)) ret = true;
		
		return ret;
	}

}
