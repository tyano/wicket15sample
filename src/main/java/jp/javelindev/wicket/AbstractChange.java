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
package jp.javelindev.wicket;

import org.apache.wicket.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Tsutomu YANO
 */
public abstract class AbstractChange implements ChangePayload<Component> {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractChange.class);
    
    private Component source;
    private ChangeTiming timing;

    public AbstractChange(Component source, ChangeTiming timing) {
        this.source = source;
        this.timing = timing;
    }

    public ChangeTiming getTiming() {
        return timing;
    }

    @Override
    public Component getEventSource() {
        return source;
    }

    @Override
    public void processChangeEvent(Component target, ChangeTiming timing) {
        switch (timing) {
            case BEFORE:
                target.modelChanging();
                break;
            case AFTER:
                target.modelChanged();
                break;
            default:
                throw new IllegalStateException("No such Timing: " + timing);
        }
    }

    /**
     * 
     * @param <T> {@link AbstractChange}のサブクラスである、リスン対象となるペイロードクラス
     * @param eventPayload 送られてきたペイロードオブジェクト
     * @param targetPayloadClass リスンするペイロードクラス。{@code eventPayload}がこのクラスでない場合は無視される。
     * @param targetComponent リスンしているペイロードクラスと合致する{@code eventPayload}だった場合、
     *                        {@link Component#modelChanging()}あるいは{@link Component#modelChanged()}を呼ぶ対象となるコンポーネント。
     */
    public static <T extends AbstractChange> void listenToModelChange(Object eventPayload, Class<T> targetPayloadClass, Component targetComponent) {
        if (targetPayloadClass.isAssignableFrom(eventPayload.getClass())) {
            T change = targetPayloadClass.cast(eventPayload);
            LOGGER.info("change event: {} timing: {}", targetPayloadClass.getCanonicalName(), change.getTiming());
            change.processChangeEvent(targetComponent, change.getTiming());
        }
    }
}
