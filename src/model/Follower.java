package model;

import java.util.ArrayList;
import java.util.Date;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

/**
 * Follower Model for MongoDB First Project
 * 
 * @author Iskandar Setiadi
 * @version 0.1, by IS @since November 2, 2014
 *
 */

public class Follower {

	/** List of Attributes */
	private String username;
	private String follower;
	private Date since;

	public static String COLLECTION_NAME = "Followers";

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFollower() {
		return follower;
	}

	public void setFollower(String follower) {
		this.follower = follower;
	}

	public Date getSince() {
		return since;
	}

	public void setSince(Date since) {
		this.since = since;
	}

	public boolean follow(DB db) {
		boolean ret = false;
		DBCollection coll = db.getCollection(COLLECTION_NAME);

		BasicDBObject doc = new BasicDBObject("username", username).append(
				"follower", follower).append("since", since);
		coll.insert(doc);

		ret = true;
		return ret;
	}

	public static ArrayList<String> retrieveFollowers(DB db, String username) {
		ArrayList<String> ret = new ArrayList<String>();
		DBCollection coll = db.getCollection(COLLECTION_NAME);

		BasicDBObject query = new BasicDBObject("username", username);

		// Cursor
		DBCursor cursor = coll.find(query);
		
		try {
			   while(cursor.hasNext()) {
			       DBObject object = cursor.next();
			       ret.add((String) object.get("follower"));
			   }
			} finally {
			   cursor.close();
			}

		return ret;
	}

}
