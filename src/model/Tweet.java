package model;

import java.util.ArrayList;
import java.util.UUID;

import com.fasterxml.uuid.EthernetAddress;
import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.TimeBasedGenerator;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

/**
 * Tweet Model for MongoDB First Project
 * 
 * @author Iskandar Setiadi
 * @version 0.1, by IS @since November 2, 2014
 *
 */

public class Tweet {

	/** List of Attributes */
	private UUID tweet_id;
	private String username;
	private String body;

	public static String COLLECTION_NAME = "Tweet";

	public UUID getTweet_id() {
		return tweet_id;
	}

	public void setTweet_id(UUID tweet_id) {
		this.tweet_id = tweet_id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public void retrieveTweet(DB db) {
		DBCollection coll = db.getCollection(COLLECTION_NAME);

		BasicDBObject query = new BasicDBObject("tweet_id", tweet_id);

		DBObject doc = coll.findOne(query);

		if (doc != null) {
			username = (String) doc.get("username");
			body = (String) doc.get("body");
		}
	}

	public boolean tweeting(DB db) {
		boolean ret = false;
		tweet_id = UUID.randomUUID();
		// Time based UUID
		EthernetAddress addr = EthernetAddress.fromInterface();
		TimeBasedGenerator uuidGenerator = Generators.timeBasedGenerator(addr);
		UUID time = uuidGenerator.generate();

		DBCollection coll = db.getCollection(COLLECTION_NAME);
		
		// Insert to Tweet
		BasicDBObject doc = new BasicDBObject("tweet_id", tweet_id).append(
				"username", username).append("body", body);
		coll.insert(doc);
		
		// Insert to Userline
		Userline userline = new Userline();
		userline.setUsername(username);
		userline.setTime(time);
		userline.setTweet_id(tweet_id);
		userline.tweeting(db);
		
		// Insert to Timeline
		Timeline timeline = new Timeline();
		timeline.setUsername(username);
		timeline.setTime(time);
		timeline.setTweet_id(tweet_id);
		timeline.addTweet(db);
		
		// Retrieve All Followers
		ArrayList<String> f = Follower.retrieveFollowers(db, username);
		
		// Insert to All Followers Timeline
		for (String f_entity : f) {
			Timeline timeline_follower = new Timeline();
			timeline_follower.setUsername(f_entity);
			timeline_follower.setTime(time);
			timeline_follower.setTweet_id(tweet_id);
			timeline_follower.addTweet(db);
		}
		
		ret = true;

		return ret;
	}

}
