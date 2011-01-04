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
package jp.javelindev.wicket.payload;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;

/**
 *
 * @author Tsutomu YANO
 */
public class ImageRefresh<T> extends AbstractEventPayload<T> implements IAjaxTargetPayload {

    private AjaxRequestTarget requestTarget;

    public ImageRefresh(T source, AjaxRequestTarget requestTarget) {
        super(source);
        this.requestTarget = requestTarget;
    }

    @Override
    public void addComponent(Component... components) {
        requestTarget.add(components);
    }
}
