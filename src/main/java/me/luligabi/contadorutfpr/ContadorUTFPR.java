package me.luligabi.contadorutfpr;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    public static void main(String[] args) throws InterruptedException, ParseException, TwitterException {
        if(!DATE_FILE.exists() || DATE_FILE.isDirectory()) {
            LOGGER.error("Could not find data.properties file! Aborting program...");
            System.exit(0);
        }

        Properties properties = new Properties();
        try (InputStream input = new FileInputStream(DATE_FILE_PATH)) {
            properties.load(input);
            LOGGER.debug("Loaded data.properties file");
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        LocalDate startingDate = new SimpleDateFormat("yyyy-MM-dd").parse(properties.getProperty("semesterStartDate"))
                .toInstant().atZone(ZoneId.of(properties.getProperty("timeZone"))).toLocalDate();
        LocalDate endingDate = new SimpleDateFormat("yyyy-MM-dd").parse(properties.getProperty("semesterEndDate"))
                .toInstant().atZone(ZoneId.of(properties.getProperty("timeZone"))).toLocalDate();

        LOGGER.info("Starting tweet loop...");
        tweetLoop(properties, startingDate, endingDate);
    }


    private static void tweetLoop(Properties properties, LocalDate startingDate, LocalDate endingDate) throws InterruptedException, TwitterException {
        LocalDate currentDate = LocalDate.now(ZoneId.of(properties.getProperty("timeZone")));

        if(currentDate.isAfter(startingDate.minusDays(1)) && currentDate.isBefore(endingDate.plusDays(1))) {
            createTweet(TweetUtils.getTweet(
                    ChronoUnit.DAYS.between(currentDate, endingDate),
                    properties.getProperty("currentSemester"),
                    properties.getProperty("universityName"),
                    ((float) ChronoUnit.DAYS.between(startingDate, currentDate) / (float) ChronoUnit.DAYS.between(startingDate, endingDate)))
            );
            LOGGER.info("Tweet sent!");
        } else {
            LOGGER.info("Today ({}) is outside the current semester's scope ({} to {}). Skipping post today!", currentDate, startingDate, endingDate);
        }
        if(Boolean.parseBoolean(properties.getProperty("isLooping"))) {
            LOGGER.info("Sleeping for 24 hours...");
            TimeUnit.DAYS.sleep(1);
            LOGGER.info("Restarting tweet loop...");
            tweetLoop(properties, startingDate, endingDate);
        } else {
            LOGGER.info("Shutting down!");
        }
    }


    private static void createTweet(String tweet) throws TwitterException {
        Twitter twitter = TwitterFactory.getSingleton();
        twitter.updateStatus(tweet);
    }


    private static final String DATE_FILE_PATH = URLDecoder.decode(
            ContadorUTFPR.class.getProtectionDomain().getCodeSource().getLocation().getPath() + "data.properties",
            StandardCharsets.UTF_8
    );
    private static final File DATE_FILE = new File(DATE_FILE_PATH);

    private static final Logger LOGGER = LoggerFactory.getLogger("ContadorUTFPR");

}