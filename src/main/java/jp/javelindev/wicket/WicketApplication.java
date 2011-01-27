package jp.javelindev.wicket;

import jp.javelindev.wicket.page.CheckerBoardPage;
import jp.javelindev.wicket.page.HomePage;
import jp.javelindev.wicket.page.Index;
import jp.javelindev.wicket.resource.SimpleTextResource;
import org.apache.wicket.Application;
import org.apache.wicket.Page;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.request.resource.SharedResourceReference;

/**
 * Application object for your web application. If you want to run this application without deploying, run the Start class.
 * 
 * @see jp.javelindev.wicket.Start#main(String[])
 */
public class WicketApplication extends WebApplication implements Rss
{    
    
    private HaseriRss rssSource;
    
    /**
     * Constructor
     */
	public WicketApplication()
	{
	}
	
	/**
	 * @see org.apache.wicket.Application#getHomePage()
	 */
    @Override
	public Class<? extends Page> getHomePage()
	{
		return Index.class;
	}

    @Override
    protected void init() {
        super.init();
        getMarkupSettings().setDefaultMarkupEncoding("UTF-8");
        getRequestCycleSettings().setResponseRequestEncoding("UTF-8");
        
        mountPage("/index", Index.class);
        mountPage("/home/${name}/address/${address}", HomePage.class);
        mountPage("/checkerboard", CheckerBoardPage.class);
        
        getSharedResources().add("simpletext", SimpleTextResource.create());
        ResourceReference reference = new SharedResourceReference("simpletext");
        mountSharedResource("/simpletext", reference);
        rssSource = new HaseriRss();
    }

    @Override
    public HaseriRss getRssSource() {
        return rssSource;
    }

    @Override
    protected void onDestroy() {
        rssSource.stopCrawlingThread();
        rssSource = null;
        super.onDestroy();
    }
    
    public static WicketApplication get() {
        return (WicketApplication)Application.get();
    }
}
