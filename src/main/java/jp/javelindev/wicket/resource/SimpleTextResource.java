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

package jp.javelindev.wicket.resource;

import java.io.UnsupportedEncodingException;
import java.util.Locale;
import org.apache.wicket.request.IRequestParameters;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.http.WebRequest;
import org.apache.wicket.request.http.WebResponse;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.ByteArrayResource;
import org.apache.wicket.resource.loader.ClassStringResourceLoader;
import org.apache.wicket.util.string.StringValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Tsutomu YANO
 */
public class SimpleTextResource extends ByteArrayResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleTextResource.class);
    
    private SimpleTextResource(String contentType, byte[] array, Locale locale) {
        super(contentType, array, locale);
    }

    public static SimpleTextResource create() {
        try {
            String text = new ClassStringResourceLoader(SimpleTextResource.class).loadStringResource(SimpleTextResource.class, "text", null, null, null);
            return new SimpleTextResource("text/plain", text.getBytes("Shift_JIS"), null);
        } catch (UnsupportedEncodingException ex) {
            throw new IllegalStateException("unsupported encoding: Shift_JIS", ex);
        }
    }

    @Override
    protected ResourceResponse newResourceResponse(Attributes attributes) {
        LOGGER.info("newResourceResponse");
        
        PageParameters params = attributes.getParameters();
        
        for(int i = 0; i < params.getIndexedCount(); i++) {
            StringValue value = params.get(i);
            LOGGER.info("index " + i + " = " + value.toString());
        }
        
        for (String name : params.getNamedKeys()) {
            StringValue value = params.get(name);
            LOGGER.info(name + " = " + value.toString());
        }
        return super.newResourceResponse(attributes);
    }
}
