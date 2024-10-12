package com.bootcamptoprod.filter;

import com.aayushatharva.brotli4j.encoder.Encoder;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Order(1)
@Component
public class BrotliCompressionFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // Check if Brotli compression is supported by the client
        String acceptEncoding = httpRequest.getHeader("Accept-Encoding");
        if (acceptEncoding != null && acceptEncoding.contains("br")) {
            System.out.println("Brotli compression request received");
            // Use a custom wrapper to capture the response content
            BrotliHttpServletResponseWrapper wrappedResponse = new BrotliHttpServletResponseWrapper(httpResponse);

            // Proceed with the filter chain
            chain.doFilter(request, wrappedResponse);

            System.out.println("Initiating response compression");
            // Compress the captured response content with Brotli
            byte[] uncompressedData = wrappedResponse.getResponseData();
            byte[] brotliCompressedData = Encoder.compress(uncompressedData);

            // Modify response headers
            httpResponse.setHeader("Content-Encoding", "br");
            httpResponse.setContentLength(brotliCompressedData.length);

            // Write the compressed response back to the output stream
            httpResponse.getOutputStream().write(brotliCompressedData);

        } else {
            // Proceed without Brotli compression if not supported
            chain.doFilter(request, response);
        }
    }

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void destroy() {
    }
}
