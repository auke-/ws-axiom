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

import java.io.ByteArrayOutputStream;
import java.io.StringReader;

import javax.xml.namespace.QName;

import org.apache.axiom.om.OMDataSourceExt;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axiom.om.OMSourcedElement;
import org.apache.axiom.om.ds.ByteArrayDataSource;
import org.apache.axiom.om.ds.WrappedTextNodeOMDataSourceFromReader;
import org.apache.axiom.testutils.suite.TestCaseEx;

public abstract class OMSourcedElementVariant {
    public static final OMSourcedElementVariant[] INSTANCES = {
        new OMSourcedElementVariant("qname-aware-source", false, false, false) {
            public OMSourcedElement createOMSourcedElement(OMFactory factory, QName qname) throws Exception {
                return factory.createOMElement(new WrappedTextNodeOMDataSourceFromReader(qname, new StringReader("test")));
            }
        },
        new OMSourcedElementVariant("unknown-name", true, true, true) {
            public OMSourcedElement createOMSourcedElement(OMFactory factory, QName qname) throws Exception {
                // TODO: can't use createOMElement(QName) here because it would generate a prefix if the prefix in the QName is empty
                OMElement orgElement = factory.createOMElement(qname.getLocalPart(), qname.getNamespaceURI(), qname.getPrefix());
                return factory.createOMElement(new TestDataSource(orgElement.toString()));
            }
        },
        new OMSourcedElementVariant("lossy-prefix", false, false, true) {
            public void addTestProperties(TestCaseEx test) {
                test.addTestProperty("method", "QName");
            }

            public OMSourcedElement createOMSourcedElement(OMFactory factory, QName qname) throws Exception {
                // TODO: can't use createOMElement(QName) here because it would generate a prefix if the prefix in the QName is empty
                OMElement orgElement = factory.createOMElement(qname.getLocalPart(), qname.getNamespaceURI(), qname.getPrefix());
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                orgElement.serialize(baos);
                OMDataSourceExt ds = new ByteArrayDataSource(baos.toByteArray(), "UTF-8");
                ds.setProperty(OMDataSourceExt.LOSSY_PREFIX, Boolean.TRUE);
                return factory.createOMElement(ds, new QName(qname.getNamespaceURI(), qname.getLocalPart()));
            }
        },
        new OMSourcedElementVariant("lossy-prefix", false, false, true) {
            public void addTestProperties(TestCaseEx test) {
                test.addTestProperty("method", "OMNamespace");
            }

            public OMSourcedElement createOMSourcedElement(OMFactory factory, QName qname) throws Exception {
                // TODO: can't use createOMElement(QName) here because it would generate a prefix if the prefix in the QName is empty
                OMElement orgElement = factory.createOMElement(qname.getLocalPart(), qname.getNamespaceURI(), qname.getPrefix());
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                orgElement.serialize(baos);
                OMDataSourceExt ds = new ByteArrayDataSource(baos.toByteArray(), "UTF-8");
                ds.setProperty(OMDataSourceExt.LOSSY_PREFIX, Boolean.TRUE);
                OMNamespace ns;
                if (qname.getNamespaceURI().length() == 0) {
                    ns = null;
                } else {
                    ns = factory.createOMNamespace(qname.getNamespaceURI(), "");
                }
                return factory.createOMElement(ds, qname.getLocalPart(), ns);
            }
        }
    };
    
    private final String name;
    private final boolean localNameRequiresExpansion;
    private final boolean namespaceURIRequiresExpansion;
    private final boolean prefixRequiresExpansion;

    public OMSourcedElementVariant(String name, boolean localNameRequiresExpansion,
            boolean namespaceURIRequiresExpansion,
            boolean prefixRequiresExpansion) {
        this.name = name;
        this.localNameRequiresExpansion = localNameRequiresExpansion;
        this.namespaceURIRequiresExpansion = namespaceURIRequiresExpansion;
        this.prefixRequiresExpansion = prefixRequiresExpansion;
    }

    public String getName() {
        return name;
    }
    
    public boolean isLocalNameRequiresExpansion() {
        return localNameRequiresExpansion;
    }

    public boolean isNamespaceURIRequiresExpansion() {
        return namespaceURIRequiresExpansion;
    }

    public boolean isPrefixRequiresExpansion(QName qname) {
        // Note that if the element is known in advance not to have a namespace, then expansion is never
        // required to determine the prefix
        return prefixRequiresExpansion && (namespaceURIRequiresExpansion || qname.getNamespaceURI().length() != 0);
    }
    
    public void addTestProperties(TestCaseEx test) {
        // Empty. May be overridden in subclasses.
    }

    public abstract OMSourcedElement createOMSourcedElement(OMFactory factory, QName qname) throws Exception;
}
