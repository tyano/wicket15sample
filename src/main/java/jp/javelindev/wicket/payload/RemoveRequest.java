/*
 * Copyright 2010 Tsutomu YANO.
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

/**
 *
 * @author Tsutomu YANO
 */
public class RemoveRequest extends AbstractEventPayload<Component> {
    private boolean remove;
    
    public RemoveRequest(Component source, boolean remove) {
        super(source);
        this.remove = remove;
    }

    public boolean isRemove() {
        return remove;
    }
}