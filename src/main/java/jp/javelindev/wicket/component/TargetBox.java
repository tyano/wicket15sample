package jp.javelindev.wicket.component;

import jp.javelindev.wicket.payload.RemoveRequest;
import org.apache.wicket.event.IEvent;
import org.apache.wicket.markup.html.WebMarkupContainer;

public class TargetBox extends WebMarkupContainer {

    public TargetBox(String id) {
        super(id);
    }

    @Override
    public void onEvent(IEvent<?> event) {
        if (event.getPayload() instanceof RemoveRequest) {
            setVisibilityAllowed(!isVisibilityAllowed());
        }
    }
}
