package me.luligabi.contadorutfpr;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class ContadorUTFPR {

    public static void main(String[] args) throws InterruptedException, ParseException, TwitterException { // TODO: Add check if current date is outside semester's scope
        if(!DATE_FILE.exists() || DATE_FILE.isDirectory()) {
            System.out.println("Could not find dates.properties file! Aborting program...");
            System.exit(0);
        }

        Properties properties = new Properties();
        try (InputStream input = new FileInputStream(DATE_FILE_PATH)) {
            properties.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        LocalDate startingDate = new SimpleDateFormat("yyyy-MM-dd").parse(properties.getProperty("semesterStartDate"))
                .toInstant().atZone(ZoneId.of(properties.getProperty("timeZone"))).toLocalDate();
        LocalDate endingDate = new SimpleDateFormat("yyyy-MM-dd").parse(properties.getProperty("semesterEndDate"))
                .toInstant().atZone(ZoneId.of(properties.getProperty("timeZone"))).toLocalDate();

        tweetLoop(properties, startingDate, endingDate);
    }


    private static void tweetLoop(Properties properties, LocalDate startingDate, LocalDate endingDate) throws InterruptedException, TwitterException {
        LocalDate currentDate = LocalDate.now(ZoneId.of(properties.getProperty("timeZone")));

        createTweet(TweetUtils.getTweet(
                ChronoUnit.DAYS.between(currentDate, endingDate),
                properties.getProperty("currentSemester"),
                properties.getProperty("universityName"),
                ((float) ChronoUnit.DAYS.between(startingDate, currentDate) / (float) ChronoUnit.DAYS.between(startingDate, endingDate)))
        );
        TimeUnit.DAYS.sleep(1);
        tweetLoop(properties, startingDate, endingDate);
    }


    private static void createTweet(String tweet) throws TwitterException { //FIXME: Special characters (á, é, ê) are corrupted on final tweet
        Twitter twitter = TwitterFactory.getSingleton();
        twitter.updateStatus(tweet);
    }

    private static final String DATE_FILE_PATH = URLDecoder.decode(
            ContadorUTFPR.class.getProtectionDomain().getCodeSource().getLocation().getPath() + "data.properties",
            StandardCharsets.UTF_8
    );
    private static final File DATE_FILE = new File(DATE_FILE_PATH);

}