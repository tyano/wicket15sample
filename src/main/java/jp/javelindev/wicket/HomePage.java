package jp.javelindev.wicket;

import org.apache.wicket.event.Broadcast;
import org.apache.wicket.event.IEvent;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.PropertyModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static jp.javelindev.wicket.AbstractChange.*;

/**
 * Homepage
 */
public class HomePage extends WebPage {

    private static final long serialVersionUID = 1L;
    // TODO Add any page properties or variables here
    private static final Logger LOGGER = LoggerFactory.getLogger(HomePage.class);
    private String removeButtonLabel = "消す";

    /**
     * Constructor that is invoked when page is invoked without a session.
     * @param parameters
     *            Page parameters
     */
    public HomePage(final PageParameters parameters) {

        // Add the simplest type of label
        add(new Label("message", "If you see this message wicket is properly configured and running"));

        // TODO Add your page's components here
        final Link<Void> targetButton = new Link<>("targetButton")   {
            @Override
            public void onClick() {
            }

            @Override
            public void onEvent(IEvent<?> event) {
                super.onEvent(event);
                if (event.getPayload() instanceof RemoveRequest) {
                    setVisibilityAllowed(!isVisibilityAllowed());
                }
            }
        };
        add(targetButton.setOutputMarkupPlaceholderTag(true));

        final Label buttonLabel = new Label("buttonLabel", new PropertyModel<String>(this, "removeButtonLabel"))  {
            @Override
            public void onEvent(IEvent<?> event) {
                super.onEvent(event);
                listenToModelChange(event.getPayload(), LabelChange.class, this);
                
                if (event.getPayload() instanceof RemoveRequest) {
                    String label = targetButton.isVisibilityAllowed() ? "消す" : "表示する";
                    setRemoveButtonLabel(label);
                }
            }
        };

        Link<Void> removeButton = new Link<>("removeButton")   {
            @Override
            public void onClick() {
                send(getPage(), Broadcast.BREADTH, new RemoveRequest(this));
            }
        };
        add(removeButton);
        removeButton.add(buttonLabel);
    }

    public String getRemoveButtonLabel() {
        return removeButtonLabel;
    }

    public void setRemoveButtonLabel(String removeButtonLabel) {
        send(this, Broadcast.BREADTH, new LabelChange(this, ChangeTiming.BEFORE));
        this.removeButtonLabel = removeButtonLabel;
        send(this, Broadcast.BREADTH, new LabelChange(this, ChangeTiming.AFTER));
    }
}
