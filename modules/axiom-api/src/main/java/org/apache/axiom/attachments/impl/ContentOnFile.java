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

package org.apache.axiom.attachments.impl;

import org.apache.axiom.attachments.CachedFileDataSource;
import org.apache.axiom.attachments.lifecycle.LifecycleManager;
import org.apache.axiom.attachments.lifecycle.impl.FileAccessor;

import javax.activation.DataSource;
import javax.mail.MessagingException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * PartOnFile stores that attachment in a file.
 * This implementation is used for very large attachments to reduce
 * the in-memory footprint.
 * 
 * The PartOnFile object is created by the PartFactory
 * @see ContentStoreFactory
 */
public class ContentOnFile extends ContentStore {

    FileAccessor fileAccessor;
    LifecycleManager manager;
    
    
    /**
     * Create a PartOnFile from the specified InputStream
     * @param in1 InputStream containing data
     * @param in2 InputStream containing data
     * @param attachmentDir String 
     */
    ContentOnFile(LifecycleManager manager, InputStream is1, InputStream is2, String attachmentDir) throws IOException {
        this.manager = manager;
        fileAccessor = manager.create(attachmentDir);
        
        // Now write the data to the backing file
        OutputStream fos = fileAccessor.getOutputStream();
        BufferUtils.inputStream2OutputStream(is1, fos);
        BufferUtils.inputStream2OutputStream(is2, fos);
        fos.flush();
        fos.close();
        
    }

    public InputStream getInputStream() throws IOException {
        try {
            return fileAccessor.getInputStream();
        } catch (MessagingException ex) {
            // The FileAccessor API uses MessagingException, although we no longer use javax.mail.
            // Convert the exception to an IOException to keep the attachments API clean.
            IOException ex2 = new IOException(ex.getMessage());
            ex2.setStackTrace(ex.getStackTrace());
            throw ex2;
        }
    }
    
    public DataSource getDataSource(String contentType) {
        // The attachment cleanup code in Axis2 relies on the assumption that we
        // produce a CachedFileDataSource here.
        CachedFileDataSource ds = new CachedFileDataSource(fileAccessor.getFile());
        ds.setContentType(contentType);
        return ds;
    }

    public void writeTo(OutputStream out) throws IOException {
        InputStream in = getInputStream();
        try {
            BufferUtils.inputStream2OutputStream(in, out);
        } finally {
            in.close();
        }
    }

    /* (non-Javadoc)
     * @see org.apache.axiom.attachments.impl.AbstractPart#getSize()
     */
    public long getSize() {
        return fileAccessor.getSize();
    }

    public void destroy() throws IOException {
        manager.delete(fileAccessor.getFile());
        // TODO: recover the shutdown hook code from DataHandlerExtImpl
    }
}
