package jp.javelindev.wicket.component;

import jp.javelindev.wicket.payload.RemoveRequest;
import org.apache.wicket.event.IEvent;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.Model;

public class RemoveLabel extends Label {

    public RemoveLabel(String id) {
        super(id);
        setDefaultModel(Model.of(getString("removeLabel")));
    }

    @Override
    public void onEvent(IEvent<?> event) {
        if (event.getPayload() instanceof RemoveRequest) {
            RemoveRequest request = (RemoveRequest)event.getPayload();
            String newLabel = request.isRemove() ? getString("displayLabel") : getString("removeLabel");
            setDefaultModelObject(newLabel);
        }
    }
}
