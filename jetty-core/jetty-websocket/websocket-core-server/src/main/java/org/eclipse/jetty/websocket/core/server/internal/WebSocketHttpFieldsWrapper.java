package org.eclipse.jetty.websocket.core.server.internal;

import java.util.Collections;

import org.eclipse.jetty.http.HttpHeader;
import org.eclipse.jetty.websocket.core.ExtensionConfig;
import org.eclipse.jetty.websocket.core.server.ServerUpgradeResponse;
import org.eclipse.jetty.websocket.core.server.WebSocketNegotiation;

public class WebSocketHttpFieldsWrapper extends HttpFieldsWrapper
{
    private final WebSocketNegotiation _negotiation;
    private final ServerUpgradeResponse _response;

    public WebSocketHttpFieldsWrapper(Mutable fields, ServerUpgradeResponse response, WebSocketNegotiation negotiation)
    {
        super(fields);
        _negotiation = negotiation;
        _response = response;
    }

    @Override
    public boolean onPutField(String name, String value)
    {
        if (HttpHeader.SEC_WEBSOCKET_SUBPROTOCOL.is(name))
        {
            _response.setAcceptedSubProtocol(value);
            return false;
        }

        if (HttpHeader.SEC_WEBSOCKET_EXTENSIONS.is(name))
        {
            _response.setExtensions(ExtensionConfig.parseList(value));
            return false;
        }

        return super.onPutField(name, value);
    }

    @Override
    public boolean onAddField(String name, String value)
    {
        if (HttpHeader.SEC_WEBSOCKET_SUBPROTOCOL.is(name))
        {
            _response.setAcceptedSubProtocol(value);
            return false;
        }

        if (HttpHeader.SEC_WEBSOCKET_EXTENSIONS.is(name))
        {
            _response.addExtensions(ExtensionConfig.parseList(value));
            return false;
        }

        return super.onAddField(name, value);
    }

    @Override
    public boolean onRemoveField(String name)
    {
        if (HttpHeader.SEC_WEBSOCKET_SUBPROTOCOL.is(name))
        {
            _response.setAcceptedSubProtocol(null);
            return false;
        }

        if (HttpHeader.SEC_WEBSOCKET_EXTENSIONS.is(name))
        {
            _response.addExtensions(Collections.emptyList());
            return false;
        }

        return super.onRemoveField(name);
    }
}
