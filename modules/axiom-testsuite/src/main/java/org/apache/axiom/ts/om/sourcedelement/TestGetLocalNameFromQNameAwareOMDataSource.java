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
package org.apache.axiom.ts.om.sourcedelement;

import java.io.StringReader;

import javax.xml.namespace.QName;

import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMMetaFactory;
import org.apache.axiom.om.OMNamedInformationItem;
import org.apache.axiom.om.OMSourcedElement;
import org.apache.axiom.om.QNameAwareOMDataSource;
import org.apache.axiom.om.ds.WrappedTextNodeOMDataSourceFromReader;
import org.apache.axiom.ts.AxiomTestCase;

/**
 * Tests that {@link OMNamedInformationItem#getLocalName()} behaves correctly on a
 * {@link OMSourcedElement} backed by a {@link QNameAwareOMDataSource}.
 */
public class TestGetLocalNameFromQNameAwareOMDataSource extends AxiomTestCase {
    public TestGetLocalNameFromQNameAwareOMDataSource(OMMetaFactory metaFactory) {
        super(metaFactory);
    }

    protected void runTest() throws Throwable {
        OMFactory factory = metaFactory.getOMFactory();
        OMSourcedElement element = factory.createOMElement(
                new WrappedTextNodeOMDataSourceFromReader(new QName("root"), new StringReader("test")));
        assertEquals("root", element.getLocalName());
        assertFalse(element.isExpanded());
    }
}