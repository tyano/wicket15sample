package jp.javelindev.wicket;

import jp.javelindev.wicket.component.TargetBox;
import jp.javelindev.wicket.component.RemoveLabel;
import jp.javelindev.wicket.component.RemoveLink;
import org.apache.wicket.Component;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Homepage
 */
public class HomePage extends WebPage {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = LoggerFactory.getLogger(HomePage.class);
    
    private TargetBox targetBox;
    
    public HomePage(final PageParameters parameters) {

        String name = parameters.get("name").toString("no name");
        String address = parameters.get("address").toString("no address");
        
        add(new Label("message", "name: " + name + " address: " + address));

        targetBox = new TargetBox("targetBox");
        add(targetBox.setOutputMarkupPlaceholderTag(true));
        
        IModel<Boolean> displayStatusModel = new AbstractReadOnlyModel<Boolean>() {
            @Override
            public Boolean getObject() {
                return targetBox.isVisibilityAllowed();
            }
        };

        Link<Void> removeButton = new RemoveLink("removeButton", displayStatusModel);
        add(removeButton);
        
        Label buttonLabel = new RemoveLabel("buttonLabel");
        removeButton.add(buttonLabel);
    }
}
