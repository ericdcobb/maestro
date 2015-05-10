import com.google.common.io.CharStreams
import groovyx.net.http.ContentType
import groovyx.net.http.HTTPBuilder
import groovyx.net.http.Method
import org.apache.http.message.BufferedHeader
import org.eclipse.jetty.server.Request

import javax.servlet.http.HttpServletResponse

sourceRequest = (Request) request;
targetResponse = (HttpServletResponse) response;

def http = new HTTPBuilder(url)

def methods = ['GET': Method.GET, 'POST': Method.POST, 'PUT': Method.PUT, 'DELETE' : Method.DELETE, 'HEAD': Method
        .HEAD, 'PATCH' : 'PATCH']

def sourceBody = CharStreams.toString(sourceRequest.getReader());
println sourceBody
println methods[sourceRequest.getMethod()]
http.request(methods[sourceRequest.getMethod()], ContentType.TEXT) {     req ->
    uri.path = sourceRequest.getPathInfo() // overrides any path in the default URL

    if (sourceRequest.getMethod() != 'GET') {
        body = sourceBody
    }

    sourceRequest.getHeaderNames().each({
        if (it != 'Content-Length'){
            headers[it] = sourceRequest.getHeader(it)
        }
    })

    println headers;

    response.success = { resp, reader ->
        handleResponse(resp, reader)
    }

    response.failure = { resp ->
        handleResponse(resp, null)
    }
}

private void handleResponse(resp, reader) {
    println resp.status
    targetResponse.setStatus(resp.status)
    resp.headers.each() {
        def bh = ((BufferedHeader) it);
        targetResponse.setHeader(bh.getName(), bh.getValue())
    }
    if (reader != null){
        targetResponse.getWriter() << reader
    }
}





