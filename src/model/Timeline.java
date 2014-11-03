package model;

import java.util.ArrayList;
import java.util.UUID;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

/**
 * Timeline Model for MongoDB First Project
 * 
 * @author Iskandar Setiadi
 * @version 0.1, by IS @since November 2, 2014
 *
 */

public class Timeline {

	/** List of Attributes */
	private String username;
	private UUID time;
	private UUID tweet_id;

	public static String COLLECTION_NAME = "Timeline";

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public UUID getTime() {
		return time;
	}

	public void setTime(UUID time) {
		this.time = time;
	}

	public UUID getTweet_id() {
		return tweet_id;
	}

	public void setTweet_id(UUID tweet_id) {
		this.tweet_id = tweet_id;
	}

	public void addTweet(DB db) {
		DBCollection coll = db.getCollection(COLLECTION_NAME);
		BasicDBObject doc = new BasicDBObject("username", username).append(
				"time", time).append("tweet_id", tweet_id);
		coll.insert(doc);
	}

	public ArrayList<Tweet> retrieveTimeline(DB db) {
		ArrayList<Tweet> ret = new ArrayList<Tweet>();

		DBCollection coll = db.getCollection(COLLECTION_NAME);

		BasicDBObject query = new BasicDBObject("username", username);

		// Cursor
		DBCursor cursor = coll.find(query).sort(new BasicDBObject("_id", -1));

		// TODO : We need to change the database structure!
		try {
			while (cursor.hasNext()) {
				DBObject object = cursor.next();
				Tweet n_tweet = new Tweet();
				n_tweet.setTweet_id((UUID) object.get("tweet_id"));
				n_tweet.retrieveTweet(db);

				ret.add(n_tweet);
			}
		} finally {
			cursor.close();
		}

		return ret;
	}

}
