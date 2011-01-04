package jp.javelindev.wicket.component;

import jp.javelindev.wicket.payload.RemoveRequest;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.markup.html.link.Link;

public class RemoveLink extends Link<Void> {

    public RemoveLink(String id) {
        super(id);
    }

    @Override
    public void onClick() {
        send(getPage(), Broadcast.BREADTH, new RemoveRequest(this));
    }
}
