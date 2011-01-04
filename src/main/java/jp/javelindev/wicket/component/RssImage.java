/*
 * Copyright 2011 Tsutomu YANO.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package jp.javelindev.wicket.component;

import com.sun.syndication.feed.synd.SyndEnclosure;
import com.sun.syndication.feed.synd.SyndEntry;
import jp.javelindev.wicket.HaseriRss;
import jp.javelindev.wicket.HaseriRss.HaseriRssException;
import jp.javelindev.wicket.WicketApplication;
import jp.javelindev.wicket.payload.ImageRefresh;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.event.IEvent;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Tsutomu YANO
 */
public class RssImage extends AjaxLink<Void> implements IImageSource {

    private static final Logger LOGGER = LoggerFactory.getLogger(RssImage.class);
    private String source;
    private final String NO_IMAGE_URL = urlFor(getApplication().getSharedResources().get(RssImage.class, "noImage.jpg", getWebRequest().getLocale(), null, null, false), null).toString();
    private boolean selected = false;

    public RssImage(String id) {
        super(id);
        setOutputMarkupId(true);
        add(new AttributeModifier("src", new AbstractReadOnlyModel<String>()      {
            @Override
            public String getObject() {
                return getImageSource();
            }
        }));

        add(new AttributeAppender("class", new AbstractReadOnlyModel<String>()   {
            @Override
            public String getObject() {
                return selected ? "selected" : "not-selected";
            }
        }, " "));
    }

    @Override
    public String getImageSource() {
        if (source == null) {
            WicketApplication application = WicketApplication.get();
            try {
                HaseriRss rss = application.getRssSource();
                SyndEntry entry = rss.getRandomEntry();
                source = ((SyndEnclosure) entry.getEnclosures().get(0)).getUrl();
            } catch (HaseriRssException ex) {
                LOGGER.error("can not retrieve a image.", ex);
                source = NO_IMAGE_URL;
            }
        }
        return source;
    }

    @Override
    public void onEvent(IEvent<?> event) {
        super.onEvent(event);
        if (event.getPayload() instanceof ImageRefresh) {
            if (selected) {
                source = null;
                selected = false;
                ImageRefresh<?> imageRefreshPayload = (ImageRefresh<?>) event.getPayload();
                imageRefreshPayload.addComponent(this);
            }
        }
    }

    @Override
    public void onClick(AjaxRequestTarget target) {
        selected = !selected;
        target.add(this);
    }
}
