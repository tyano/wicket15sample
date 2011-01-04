package jp.javelindev.wicket.component;

import jp.javelindev.wicket.payload.RemoveRequest;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;

public class RemoveLink extends Link<Void> {
    private IModel<Boolean> displayStatusModel;
    
    public RemoveLink(String id, IModel<Boolean> displayStatusModel) {
        super(id);
        this.displayStatusModel = displayStatusModel;
    }

    @Override
    public void onClick() {
        boolean isDisplayed = displayStatusModel.getObject();
        send(getPage(), Broadcast.BREADTH, new RemoveRequest(this, isDisplayed));
    }
}
