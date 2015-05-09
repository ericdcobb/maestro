import groovyx.net.http.ContentType
import groovyx.net.http.HTTPBuilder
import groovyx.net.http.Method
import org.apache.http.message.BufferedHeader
import org.eclipse.jetty.server.Request

import javax.servlet.http.HttpServletResponse

sourceRequest = (Request) request;
targetResponse = (HttpServletResponse) response;

def http = new HTTPBuilder(url)

def methods = ['GET': Method.GET]

http.request(methods[sourceRequest.getMethod()], ContentType.TEXT) {     req ->
    uri.path = sourceRequest.getPathInfo() // overrides any path in the default URL
    headers.'User-Agent' = 'Mozilla/5.0'

    sourceRequest.getHeaderNames().each({headers[it] = sourceRequest.getHeader(it)})
    response.success = { resp, reader ->
        handleResponse(resp, reader)
    }

    response.failure = { resp ->
        handleResponse(resp, reader)
    }
}

private void handleResponse(resp, reader) {
    targetResponse.setStatus(resp.status)
    resp.headers.each() {
        def bh = ((BufferedHeader) it);
        targetResponse.setHeader(bh.getName(), bh.getValue())
    }

    targetResponse.getWriter() << reader
}





