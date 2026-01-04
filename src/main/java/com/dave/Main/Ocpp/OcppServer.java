package com.dave.Main.Ocpp;

import com.dave.Main.Logging.Logger;
import com.dave.Main.State.State;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class OcppServer {
    private static final Logger LOGGER = Logger.INSTANCE;

    private final State state;

    @Autowired
    public OcppServer(State state) {
        this.state = state;
    }

    @PostConstruct
    public void init() {
        new Thread(this::start).start();
    }


    private void start() {
        try (ServerSocket ws = new ServerSocket(1234)) {
            LOGGER.print("Waiting for client on port 1234 ...");

            try (ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor()) {

                while (!ws.isClosed()) {
                    Socket socket = ws.accept();
                    executorService.execute(new OcppSocketHandler(socket, state));
                }

            }

        } catch (Exception e) {
            LOGGER.print(e.getMessage());
            LOGGER.print("Restarting ...");
            start();
        }
    }

}
