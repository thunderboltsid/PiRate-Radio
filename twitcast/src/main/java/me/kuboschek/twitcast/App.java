package me.kuboschek.twitcast;

import com.google.common.collect.Lists;
import com.twitter.hbc.ClientBuilder;
import com.twitter.hbc.core.Client;
import com.twitter.hbc.core.Constants;
import com.twitter.hbc.core.Hosts;
import com.twitter.hbc.core.HttpHosts;
import com.twitter.hbc.core.endpoint.StatusesFilterEndpoint;
import com.twitter.hbc.core.processor.StringDelimitedProcessor;
import com.twitter.hbc.httpclient.auth.Authentication;
import com.twitter.hbc.httpclient.auth.OAuth1;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws IOException {
        /** Set up your blocking queues: Be sure to size these properly based on expected TPS of your stream */
        BlockingQueue<String> msgQueue = new LinkedBlockingQueue<String>(100000);

        /** Declare the host you want to connect to, the endpoint, and authentication (basic auth or oauth) */
        Hosts hosebirdHosts = new HttpHosts(Constants.STREAM_HOST);
        StatusesFilterEndpoint hosebirdEndpoint = new StatusesFilterEndpoint();


        // Optional: set up some followings and track terms
        List<String> terms = Lists.newArrayList("#mlh", "#Copenhacks", "Copenhacks", "@kuboschek", "@MLHacks");

        // These secrets should be read from a config file
        Authentication hosebirdAuth = new OAuth1("thrNxPV4fvKJfeIb4F7Ew", "JLCMIPfH94zdfILNPYYO8uACgwAJbOoEOgYYtllxmdE", "569271614-Lh1tNMMaHDJeVPcO0bc17sYeAQWHa0WHuyf3Y0BN", "msv3fRON0NIr5YZvIjcQ5NdzJUMctEQIzh5w6Nu7d8nuT");


        hosebirdEndpoint.trackTerms(terms);

        ClientBuilder builder = new ClientBuilder()
                .name("Hosebird-Client-01")                              // optional: mainly for the logs
                .hosts(hosebirdHosts)
                .authentication(hosebirdAuth)
                .endpoint(hosebirdEndpoint)
                .processor(new StringDelimitedProcessor(msgQueue));
                //.eventMessageQueue(eventQueue);                          // optional: use this if you want to process client events

        Client hosebirdClient = builder.build();


        // Attempts to establish a connection.
        hosebirdClient.connect();

        Runnable msgProc = new MsgProcessor(hosebirdClient, msgQueue);

        Thread t = new Thread(msgProc);
        t.start();

        System.in.read();
    }


}
