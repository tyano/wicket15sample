/*
 * Copyright 2007 Tsutomu Yano
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

/*
 * HaseriRss.java
 *
 * Created on 2007/03/28, 21:21
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package jp.javelindev.wicket;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.fetcher.FeedFetcher;
import com.sun.syndication.fetcher.FetcherException;
import com.sun.syndication.fetcher.impl.FeedFetcherCache;
import com.sun.syndication.fetcher.impl.HttpURLFeedFetcher;
import com.sun.syndication.fetcher.impl.LinkedHashMapFeedInfoCache;
import com.sun.syndication.io.FeedException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 *
 * @author ben
 */
public class HaseriRss {
    public static class HaseriRssException extends Exception {
        public HaseriRssException() {
            super();
        }
        
        public HaseriRssException(String message) {
            super(message);
        }
        
        public HaseriRssException(String message, Throwable ex) {
            super(message, ex);
        }
        
        public HaseriRssException(Throwable ex) {
            super(ex);
        }
    }
    
    static final URL RSS_URL;
    
    final FeedFetcherCache infoCache = new LinkedHashMapFeedInfoCache();
    final FeedFetcher fetcher = new HttpURLFeedFetcher(infoCache);
    final Random generator = new Random(System.currentTimeMillis());
    final ScheduledExecutorService service = Executors.newScheduledThreadPool(1);

    int crawlingSpanSec = 600;
    
    List<SyndEntry> entryList;
    
    static {
        try {
            RSS_URL = new URL("http://serif.hatelabo.jp/seriflist/rss");    
        } catch (MalformedURLException ignored) { throw new RuntimeException(); }            
    }
    
    /** Creates a new instance of HaseriRss */
    public HaseriRss() {
        this(600);
    }

    public HaseriRss(int sec) {
        if(sec <= 0) throw new IllegalArgumentException("a argument 'sec' should be greater than 0.");
        this.crawlingSpanSec = sec;
        
        service.scheduleAtFixedRate(new Runnable() {
            public void run() {
                try {
                    crawl();
                } catch (FeedException ex) {
                    throw new RuntimeException(ex);
                } catch (FetcherException ex) {
                    throw new RuntimeException(ex);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }, sec, sec, TimeUnit.SECONDS);
    }
    
    synchronized void crawl() throws IOException, FeedException, FetcherException {
        SyndFeed feed = fetcher.retrieveFeed(RSS_URL);
        entryList = feed.getEntries();
    }
    
    public synchronized List<SyndEntry> getEntryList() throws HaseriRssException {
        if(entryList == null) {
            try {
                crawl();
            } catch (FeedException ex) {
                throw new HaseriRssException(ex);
            } catch (IOException ex) {
                throw new HaseriRssException(ex);
            } catch (FetcherException ex) {
                throw new HaseriRssException(ex);
            }
        }
        return new ArrayList<SyndEntry>(entryList);
    }
    
    public synchronized SyndEntry getRandomEntry() throws HaseriRssException {
        return getEntryList().get(generator.nextInt(entryList.size()));
    }
    
    public void stopCrawlingThread() {
        if(!service.isShutdown()) {
            service.shutdown();
            Logger.global.info("Rss Crawler Thread has been stoped.");
        }
    }
    
    public boolean isShutdown() {
        return service.isShutdown();
    }

    protected void finalize() throws Throwable {
        stopCrawlingThread();
        super.finalize();
    }
}
