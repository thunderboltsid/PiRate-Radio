package me.kuboschek.twitcast;

import com.google.gson.Gson;
import com.twitter.hbc.core.Client;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.BlockingQueue;

/**
 * Created by leonhard on 16/04/16.
 */
public class MsgProcessor implements Runnable {
    Client c;
    BlockingQueue<String> e;
    Gson g;

    public MsgProcessor(Client hosebirdClient, BlockingQueue<String> evt) {
        this.c = hosebirdClient;
        this.e = evt;
        this.g = new Gson();
    }

    public void run() {
        // on a different thread, or multiple different threads....
        while (!c.isDone()) {
            String msg = null;
            try {
                msg = e.take();
                Tweet t = g.fromJson(msg, Tweet.class);
                System.out.println(t.text);

                HttpClient client = HttpClientBuilder.create().build();

                URIBuilder builder = new URIBuilder("http://localhost:5000/")
                        .addParameter("line", t.text);

                HttpGet method = new HttpGet(builder.build());
                client.execute(method);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            } catch (URISyntaxException e1) {
                e1.printStackTrace();
            } catch (ClientProtocolException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }
}
