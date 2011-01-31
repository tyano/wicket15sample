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

import jp.javelindev.wicket.component.RssImageLink;
import jp.javelindev.wicket.payload.ImageRefresh;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 *
 * @author Tsutomu YANO
 */
public class CheckerBoardPage extends WebPage {
    private static final int ROW_NUMBER = 4;
    private static final int COLUMN_NUMBER = 6;
    
    private boolean[][] clickValues = new boolean[ROW_NUMBER][COLUMN_NUMBER];
    private FeedbackPanel feedback = new FeedbackPanel("feedback");
    
    public CheckerBoardPage(PageParameters parameters) {
        super(parameters);
        feedback.setOutputMarkupId(true);
        add(feedback);
        
        Form<Void> submitForm = new Form<Void>("submitForm");
        add(submitForm);
        
        RepeatingView row = new RepeatingView("gridrow");
        submitForm.add(row);
        for(int i = 0; i < ROW_NUMBER; i++) {
            WebMarkupContainer container = new WebMarkupContainer(row.newChildId());
            row.add(container);
            
            RepeatingView column = new RepeatingView("rssImage");
            for(int j = 0; j < COLUMN_NUMBER; j++) {
                RssImageLink image = new RssImageLink(column.newChildId(), new PropertyModel<Boolean>(this, "clickValues." + i + "." + j));
                image.setRenderBodyOnly(true);
                image.addBehaviorToImage(new AttributeAppender("class", Model.of("grid2h grid2v"), " "));
                if(j == 0) image.addBehaviorToImage(new AttributeAppender("class", Model.of("first"), " "));
                column.add(image);
            }
            container.add(column);
        }
        
        AjaxSubmitLink refreshLink = new AjaxSubmitLink("refresh", submitForm) {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                target.add(feedback);
                send(getPage(), Broadcast.BREADTH, new ImageRefresh<AjaxSubmitLink>(this, target));
            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
                target.add(feedback);
            }
        };
        add(refreshLink);
    }
}
