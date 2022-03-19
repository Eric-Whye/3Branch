import twitter4j.*;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {


        Configuration config = new Configuration();
        System.out.println(config.getNumTweets());

        Twitter twitter = new TwitterFactory().getInstance();
        List<Status> tweets = new ArrayList<>();
        Query query = new Query("#reddit");
        query.setCount(config.getNumTweets());
        try{
            QueryResult result = twitter.search(query);
            tweets.addAll(result.getTweets());
        }catch (TwitterException te){ te.printStackTrace();}

        for (Status tweet : tweets){
            String user = tweet.getUser().getScreenName();
            String msg = tweet.getText();

            System.out.println(user + " wrote " + msg);
        }
        /* {
            Twitter twitter = TwitterFactory.getSingleton();
            List<Status> statuses = twitter.getHomeTimeline();
            System.out.println("Showing home timeline.");
            for (Status status : statuses) {
                System.out.println(status.getUser().getName() + ":" +
                        status.getText());
            }
        }catch(Exception e){e.printStackTrace();}*/

    }
}
