/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.axiom.soap.impl.dom.factory;

import org.apache.axiom.om.OMDataSource;
import org.apache.axiom.om.OMNamespace;
import org.apache.axiom.om.OMXMLParserWrapper;
import org.apache.axiom.om.impl.dom.ParentNode;
import org.apache.axiom.om.impl.dom.factory.OMDOMFactory;
import org.apache.axiom.om.impl.dom.factory.OMDOMMetaFactory;
import org.apache.axiom.soap.SOAPEnvelope;
import org.apache.axiom.soap.SOAPHeaderBlock;
import org.apache.axiom.soap.SOAPMessage;
import org.apache.axiom.soap.SOAPProcessingException;
import org.apache.axiom.soap.impl.builder.SOAPFactoryEx;
import org.apache.axiom.soap.impl.dom.SOAPEnvelopeImpl;
import org.apache.axiom.soap.impl.dom.SOAPMessageImpl;

public abstract class DOMSOAPFactory extends OMDOMFactory implements SOAPFactoryEx {
    public DOMSOAPFactory(OMDOMMetaFactory metaFactory) {
        super(metaFactory);
    }

    public DOMSOAPFactory() {
    }

    public final SOAPMessage createSOAPMessage(OMXMLParserWrapper builder) {
        return new SOAPMessageImpl(builder, this);
    }

    public final SOAPEnvelope createSOAPEnvelope(SOAPMessage message, OMXMLParserWrapper builder) {
        return new SOAPEnvelopeImpl((ParentNode)message, null, builder, this, false);
    }

    public final SOAPEnvelope getDefaultEnvelope() throws SOAPProcessingException {
        SOAPEnvelopeImpl env = new SOAPEnvelopeImpl(null, getNamespace(), null, this, true);
        createSOAPHeader(env);
        createSOAPBody(env);
        return env;
    }

    public final SOAPMessage createSOAPMessage() {
        return new SOAPMessageImpl(this);
    }

    public SOAPHeaderBlock createSOAPHeaderBlock(OMDataSource source) {
        throw new UnsupportedOperationException("TODO");
    }

    public SOAPHeaderBlock createSOAPHeaderBlock(String localName, OMNamespace ns, OMDataSource ds) throws SOAPProcessingException {
        throw new UnsupportedOperationException("TODO");
    }

}
