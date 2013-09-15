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
package org.apache.axiom.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.ops4j.pax.exam.CoreOptions.frameworkProperty;
import static org.ops4j.pax.exam.CoreOptions.junitBundles;
import static org.ops4j.pax.exam.CoreOptions.options;
import static org.ops4j.pax.exam.CoreOptions.url;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerClass;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerClass.class)
public class ServiceTest {
    @Configuration
    public static Option[] configuration() {
        return options(
                url("link:classpath:org.apache.geronimo.specs.geronimo-activation_1.1_spec.link"),
                url("link:classpath:org.apache.servicemix.specs.stax-api-1.0.link"),
                url("link:classpath:stax2-api.link"),
                url("link:classpath:woodstox-core-asl.link"),
                url("link:classpath:org.apache.james.apache-mime4j-core.link"),
                url("link:classpath:org.apache.ws.commons.axiom.axiom-api.link"),
                url("link:classpath:org.apache.ws.commons.axiom.axiom-impl.link"),
                url("link:classpath:org.apache.ws.commons.axiom.axiom-dom.link"),
                junitBundles(),
                frameworkProperty("foo").value("bar"));
    }
    
    @Inject BundleContext context;
    
    @Test
    public void testLLOMMetaFactoryServicePresent() throws Exception {
        ServiceReference[] omfactRefs = context
                .getServiceReferences("org.apache.axiom.om.OMMetaFactory", "(implementationName=llom)");
        assertNotNull(omfactRefs);
        assertEquals(1, omfactRefs.length);
    }
    
    @Test
    public void testDOOMMetaFactoryServicePresent() throws Exception {
        ServiceReference[] omfactRefs = context
                .getServiceReferences("org.apache.axiom.om.OMMetaFactory", "(implementationName=doom)");
        assertNotNull(omfactRefs);
        assertEquals(1, omfactRefs.length);
    }
}
