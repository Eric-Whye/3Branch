import twitter4j.*;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        Configuration config = new Configuration();
        System.out.println(config.getNumTweets());
        
        Twitter twitter = new TwitterFactory().getInstance();
        try{
            QueryResult result = twitter.search(new Query("#reddit"));
            System.out.println(result.getTweets());
        }catch (TwitterException te){ te.printStackTrace();}
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
