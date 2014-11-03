package model;

import java.util.Date;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

/**
 * Friend Model for MongoDB First Project
 * 
 * @author Iskandar Setiadi
 * @version 0.1, by IS @since November 2, 2014
 *
 */

public class Friend {

	/** List of Attributes */
	private String username;
	private String friend;
	private Date since;

	public static String COLLECTION_NAME = "Friends";

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFriend() {
		return friend;
	}

	public void setFriend(String friend) {
		this.friend = friend;
	}

	public Date getSince() {
		return since;
	}

	public void setSince(Date since) {
		this.since = since;
	}

	public boolean follow(DB db) {
		boolean ret = false;
		DBCollection coll = db.getCollection(User.COLLECTION_NAME);

		if (since == null)
			since = new Date();

		BasicDBObject query = new BasicDBObject("username", friend);

		DBObject doc = coll.findOne(query);

		if (doc != null) {
			coll = db.getCollection(COLLECTION_NAME);
			BasicDBObject doc2 = new BasicDBObject("username", username)
					.append("friend", friend).append("since", since);
			coll.insert(doc2);
			
			// Add to Follower table
			Follower add_follower = new Follower();
			add_follower.setUsername(friend);
			add_follower.setFollower(username);
			add_follower.setSince(since); // use same timestamp
			ret = add_follower.follow(db);
		}

		return ret;
	}

}
