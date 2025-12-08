package com.dave.StreamProcessor;

import com.dave.Exception.ProtocolException;

import java.io.IOException;

public interface StreamProcessor {

    /**
     * Get the next complete message from the InputStream as String.
     */
    String read() throws ProtocolException;

    /**
     * Send a message to the output stream in the appropriate format.
     */
    void send(String message) throws IOException;

}
