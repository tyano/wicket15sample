package jp.javelindev.wicket.component;

import jp.javelindev.wicket.payload.RemoveRequest;
import org.apache.wicket.Component;
import org.apache.wicket.event.IEvent;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;

public class RemoveLabel extends Label {

    private final IModel<? extends Component> depend;
    private String label;

    public RemoveLabel(String id, IModel<? extends Component> targetModel) {
        super(id);
        this.depend = targetModel;
        setDefaultModelObject(getString("removeLabel"));
    }

    @Override
    public void onEvent(IEvent<?> event) {
        if (event.getPayload() instanceof RemoveRequest) {
            RemoveRequest request = (RemoveRequest)event.getPayload();
            Component target = depend.getObject();
            String newLabel = request.isRemove() ? getString("displayLabel") : getString("removeLabel");
            setDefaultModelObject(newLabel);
        }
    }
}
