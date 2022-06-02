//
// ========================================================================
// Copyright (c) 1995-2022 Mort Bay Consulting Pty Ltd and others.
//
// This program and the accompanying materials are made available under the
// terms of the Eclipse Public License v. 2.0 which is available at
// https://www.eclipse.org/legal/epl-2.0, or the Apache License, Version 2.0
// which is available at https://www.apache.org/licenses/LICENSE-2.0.
//
// SPDX-License-Identifier: EPL-2.0 OR Apache-2.0
// ========================================================================
//

package org.eclipse.jetty.ee10.servlet;

import java.io.File;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

import jakarta.servlet.MultipartConfigElement;
import jakarta.servlet.http.Part;
import org.eclipse.jetty.http.HttpField;
import org.eclipse.jetty.http.HttpHeader;
import org.eclipse.jetty.http.MimeTypes;
import org.eclipse.jetty.server.Request;

import static org.eclipse.jetty.ee10.servlet.ServletContextRequest.__MULTIPART_CONFIG_ELEMENT;

/**
 * A {@link CompletableFuture} that is completed once a {@link MimeTypes.Type#FORM_ENCODED}
 * content has been parsed asynchronously from the {@link org.eclipse.jetty.io.Content.Source}.
 */
public class FutureMultiPartFormFields
{
    private static final CompletableFuture<Collection<Part>> NONE = CompletableFuture.completedFuture(null);

    public static MultiPartFormInputStream forRequest(Request request)
    {
        Object attr = request.getAttribute(MultiPartFormInputStream.class.getName());
        if (attr instanceof MultiPartFormInputStream multiParts)
            return multiParts;

        String contentType = request.getHeaders().get(HttpHeader.CONTENT_TYPE);
        String baseType = HttpField.valueParameters(contentType, null);
        if (MimeTypes.Type.MULTIPART_FORM_DATA.is(baseType) && request.getAttribute(__MULTIPART_CONFIG_ELEMENT) != null)
        {
            MultipartConfigElement config = (MultipartConfigElement)request.getAttribute(__MULTIPART_CONFIG_ELEMENT);
            if (config == null)
                throw new IllegalStateException("No multipart config for servlet");

            MultiPartFormInputStream multiPartFormInputStream = new MultiPartFormInputStream(request, contentType, config,
                ((File)request.getContext().getAttribute("javax.servlet.context.tempdir")));
            return multiPartFormInputStream;
        }

        return null;
    }
}
