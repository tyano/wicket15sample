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

package jp.javelindev.wicket.page;

import jp.javelindev.wicket.component.RssImage;
import jp.javelindev.wicket.payload.ImageRefresh;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 *
 * @author Tsutomu YANO
 */
public class CheckerBoardPage extends WebPage {

    public CheckerBoardPage(PageParameters parameters) {
        super(parameters);
        
        RepeatingView row = new RepeatingView("gridrow");
        add(row);
        for(int i = 0; i < 4; i++) {
            WebMarkupContainer container = new WebMarkupContainer(row.newChildId());
            row.add(container);
            
            RepeatingView column = new RepeatingView("rssImage");
            for(int j = 0; j < 6; j++) {
                RssImage image = new RssImage(column.newChildId());
                if(j == 0) image.add(new AttributeAppender("class", Model.of("first"), " "));
                column.add(image);
            }
            container.add(column);
        }
        
        AjaxLink<Void> refreshLink = new AjaxLink<Void>("refreshLink") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                send(getPage(), Broadcast.BREADTH, new ImageRefresh<AjaxLink>(this, target));
            }
        };
        add(refreshLink);
    }
}
