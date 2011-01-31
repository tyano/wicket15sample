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
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.event.IEvent;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.HiddenField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Tsutomu YANO
 */
public class RssImageLink extends Panel implements IImageSource {
    private static final Logger LOGGER = LoggerFactory.getLogger(RssImageLink.class);
    private String source;
    private final String NO_IMAGE_URL = urlFor(getApplication().getSharedResources().get(RssImageLink.class, "noImage.jpg", getWebRequest().getLocale(), null, null, false), null).toString();
    private WebMarkupContainer imageLink;
    private HiddenField<Boolean> hidden;
    
    public RssImageLink(String id, IModel<Boolean> model) {
        super(id, model);
        construct();
    }

    public RssImageLink(String id) {
        super(id);
        construct();
    }
    
    public Boolean getModelObject() {
        return (Boolean)getDefaultModelObject();
    }
    
    public void setModelObject(Boolean value) {
        setDefaultModelObject(value);
    }
    
    public void addBehaviorToImage(Behavior... behaviors) {
        imageLink.add(behaviors);
    }
    
    public void addBehaviorToField(Behavior... behaviors) {
        hidden.add(behaviors);
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderJavaScript(
                  "function changeValue(imageId, fieldId) {\n"
                + "    var image = document.getElementById(imageId);\n "
                + "    var field = document.getElementById(fieldId);\n "
                + "    var oldClassName = image.className;\n"
                + "    if(field.value == 'true') {\n"
                + "        field.value = 'false';\n"
                + "        if(oldClassName.indexOf('now-selected') >= 0) {\n"
                + "            image.className = oldClassName.replace('now-selected', 'not-selected');\n"
                + "        } else {\n"
                + "            image.className = oldClassName + ' not-selected';\n"
                + "        }\n"
                + "    } else {\n"
                + "        field.value = 'true';\n"
                + "        if(oldClassName.indexOf('not-selected') >= 0) {\n"
                + "            image.className = oldClassName.replace('not-selected', 'now-selected');\n"
                + "        } else {\n"
                + "            image.className = oldClassName + ' now-selected';\n"
                + "        }\n"
                + "    }\n"
                + "}", "clickscript");
    }

    @SuppressWarnings("unchecked")
    final void construct() {
        setRenderBodyOnly(true);
        
        hidden = new HiddenField<Boolean>("linkValue", (IModel<Boolean>)this.getDefaultModel());
        hidden.setOutputMarkupId(true);
        add(hidden);
        
        imageLink = new WebMarkupContainer("imageLink");
        imageLink.setOutputMarkupId(true);
        add(imageLink);
        imageLink.add(new AttributeAppender("onclick", true, new AbstractReadOnlyModel<String>() {
            @Override
            public String getObject() {
                final String script = "changeValue('" + imageLink.getMarkupId() + "', '" + hidden.getMarkupId() + "');";
                return script;
            }
        }, ";"));
        
        imageLink.add(new AttributeModifier("src", new AbstractReadOnlyModel<String>()      {
            @Override
            public String getObject() {
                return getImageSource();
            }
        }));
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        Form<?> parentForm = findParent(Form.class);
        if(parentForm == null) {
            throw new IllegalStateException("RssImageLink must be attached on a Form instance. Can not find a parent form object.");
        }
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
            if (Boolean.TRUE.equals(getModelObject())) {
                source = null;
                setModelObject(false);
                ImageRefresh<?> imageRefreshPayload = (ImageRefresh<?>) event.getPayload();
                imageRefreshPayload.addComponent(imageLink);
                imageRefreshPayload.addComponent(hidden);
            }
        }
    }    
}
