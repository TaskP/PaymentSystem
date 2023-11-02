package com.example.payment.app;

import org.springframework.core.SpringVersion;

/**
 * Print system and application information.
 *
 */
public final class AppInfo {

    /**
     * Default private.
     */
    private AppInfo() {
        super();
    }

    /**
     * Main.
     *
     * @param args
     */
    public static void main(final String[] args) {
        System.out.println();
        System.out.println("  Java Version:" + System.getProperty("java.version"));
        System.out.println("   Java Vendor:" + System.getProperty("java.vendor"));
        System.out.println("     Java Home:" + System.getProperty("java.home"));
        System.out.println("Spring Version:" + SpringVersion.getVersion());
    }
}
