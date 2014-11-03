package core;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

import helper.Constants;
import model.Friend;
import model.Timeline;
import model.Tweet;
import model.User;
import model.Userline;

import com.mongodb.DB;
import com.mongodb.MongoClient;

/**
 * Main class for MongoDB First Project
 * 
 * @author Iskandar Setiadi
 * @version 0.1, by IS @since November 2, 2014
 *
 */

public class Main {

	public static void main(String[] args) throws Exception {
		String input;
		User user = new User();
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				System.in));
		
		// Connect to the MongoDB
		System.out.println("-- Initializing --");
		MongoClient mongoClient = new MongoClient(Constants.SERVER_ADDRESS,
				Constants.SERVER_PORT);
		DB db = mongoClient.getDB(Constants.DATABASE_NAME);
		System.out.println("-- Finished Initializing --");
		
		/** Home */
		boolean isFinished = false;
		boolean isLoggedIn = false;
		while (!isFinished) {
			if (!isLoggedIn) {
				String username, password;

				System.out.println("-- Main Menu -- ");
				System.out.println("1. Sign In");
				System.out.println("2. Sign Up");
				System.out.println("999. Exit");
				System.out.print("Input: ");
				System.out.flush();

				input = reader.readLine();

				switch (input) {
				case "1":
					System.out.print("Username: ");
					System.out.flush();
					username = reader.readLine();
					System.out.print("Password: ");
					System.out.flush();
					password = reader.readLine();

					user.setUsername(username);
					user.setPassword(password);
					isLoggedIn = user.login(db);

					if (isLoggedIn)
						System.out.println("\nWelcome, " + username + " !");
					else
						System.out
								.println("\nUsername / Password does not exist!");

					break;
				case "2":
					System.out.print("Username: ");
					System.out.flush();
					username = reader.readLine();
					System.out.print("Password: ");
					System.out.flush();
					password = reader.readLine();

					// TODO : Prior checking for username uniqueness, Hash
					// password
					user.setUsername(username);
					user.setPassword(password);
					user.save(db);

					isLoggedIn = true;
					break;
				case "999":
					isFinished = true;
					break;
				default:
					System.out.println("Unrecognized Input!");
				}
			} else {
				String friend_username, user_tweet, username;
				boolean is_updated;
				ArrayList<Tweet> tweets = null;

				System.out.println("-- Menu -- ");
				System.out.println("1. Follow a Friend");
				System.out.println("2. Tweet");
				System.out.println("3. Show Userline");
				System.out.println("4. Show Timeline");
				System.out.println("888. Sign Out");
				System.out.println("999. Exit");
				System.out.print("Input: ");
				System.out.flush();

				input = reader.readLine();

				switch (input) {
				case "1":
					System.out.print("Friend's Username: ");
					System.out.flush();
					friend_username = reader.readLine();
					
					Friend add_friend = new Friend();
					add_friend.setUsername(user.getUsername());
					add_friend.setFriend(friend_username);
					
					is_updated = add_friend.follow(db);
					
					if (is_updated) {
						System.out.println("You have followed " + friend_username + " !");
					} else {
						System.out.println("Fail to follow " + friend_username +" !");
					}
					break;
				case "2":
					System.out.print("Your Tweet: ");
					System.out.flush();
					user_tweet = reader.readLine();
					
					Tweet add_tweet = new Tweet();
					add_tweet.setUsername(user.getUsername());
					add_tweet.setBody(user_tweet);
					
					is_updated = add_tweet.tweeting(db);
					
					if (is_updated) {
						System.out.println("Your new tweet has been posted successfully!");
					} else {
						System.out.println("Error in tweeting!");
					}
					
					break;
				case "3":
					System.out.print("Choose username for userline: ");
					System.out.flush();
					username = reader.readLine();
					
					Userline userline = new Userline();
					userline.setUsername(username);
					
					tweets = userline.retrieveUserline(db);
					showTweets(tweets);
					break;
				case "4":
					System.out.print("Choose username for timeline: ");
					System.out.flush();
					username = reader.readLine();
					
					Timeline timeline = new Timeline();
					timeline.setUsername(username);
					
					tweets = timeline.retrieveTimeline(db);
					showTweets(tweets);
					break;
				case "888":
					isLoggedIn = false;
					System.out.println("You have been logged out successfully!");
					break;
				case "999":
					isFinished = true;
					break;
				default:
					System.out.println("Unrecognized Input!");
				}

			}
		}
		
		// Close connection
		mongoClient.close();
	}
	
	private static void showTweets(ArrayList<Tweet> tweets) {
		if (tweets.size() == 0) System.out.println("- No Tweet -");
		else {
			System.out.println("- Tweet(s) -");
			for (Tweet t : tweets) {
				System.out.println(t.getUsername() + " : " + t.getBody());
			}
		}
	}
}
