package com.dave.Logging;

import com.dave.util.Utils;

public enum Logger {

    INSTANCE;

    public void print(String msg) {
        System.out.println("[" + Utils.dateTime() + "]: " + msg + "\n");
    }

    public void logIncomingMsg(String msg, String sender) {
        printMsg(msg, "from: " + sender);
    }

    public void logOutgoingMsg(String msg, String recipient) {
        printMsg(msg, "to:   " + recipient);
    }

    private void printMsg(String body, String header) {
        System.out.println("========== [" + Utils.dateTime() + "] [" + header + "] ==========");
        System.out.println(body);
        System.out.println("========================================================================\n");
    }

}
