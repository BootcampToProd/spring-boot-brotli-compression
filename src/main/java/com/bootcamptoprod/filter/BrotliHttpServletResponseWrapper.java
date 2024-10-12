package com.bootcamptoprod.filter;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.WriteListener;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

public class BrotliHttpServletResponseWrapper extends HttpServletResponseWrapper {

    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private final PrintWriter printWriter = new PrintWriter(outputStream);

    public BrotliHttpServletResponseWrapper(HttpServletResponse response) {
        super(response);
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        return new ServletOutputStream() {
            @Override
            public boolean isReady() {
                // Indicate whether the stream is ready to be written to.
                return true; // Changed to true for better compatibility
            }

            @Override
            public void setWriteListener(WriteListener writeListener) {
                // No-op for synchronous processing
            }

            @Override
            public void write(int b) throws IOException {
                outputStream.write(b); // Write data to buffer
            }
        };
    }

    @Override
    public PrintWriter getWriter() {
        return printWriter; // Use PrintWriter to capture text data
    }

    public byte[] getResponseData() {
        printWriter.flush(); // Ensure all data is written to the buffer
        return outputStream.toByteArray(); // Return buffered response data
    }
}