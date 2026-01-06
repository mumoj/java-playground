package com.modernjavainaction.part4_everyday_java.chapter14_module_system;

/**
 * Chapter 14: The Java Module System (Java 9+)
 * 
 * NOTE: The module system requires Java 9+. This chapter provides
 * conceptual examples and documentation for Java 8 users.
 * 
 * Key concepts:
 * - Why modules?
 * - module-info.java
 * - exports, requires, opens
 * - Services (provides, uses)
 * - Migrating to modules
 */
public class ModuleSystemOverview {

    public static void main(String[] args) {
        System.out.println("=== Chapter 14: The Java Module System ===\n");

        System.out.println("NOTE: The module system requires Java 9+.");
        System.out.println("This chapter provides conceptual overview for Java 8 users.\n");

        // 1. Why modules?
        System.out.println("1. Why Modules?");
        System.out.println("   - Better encapsulation (can hide internal packages)");
        System.out.println("   - Reliable dependencies (no more NoClassDefFoundError at runtime)");
        System.out.println("   - Smaller runtime (custom JRE with only needed modules)");
        System.out.println("   - Improved security (no reflective access to internals)\n");

        // 2. Module declaration
        System.out.println("2. Module Declaration (module-info.java):");
        System.out.println("   ```java");
        System.out.println("   module com.example.myapp {");
        System.out.println("       requires java.logging;           // Dependency");
        System.out.println("       requires transitive java.sql;    // Transitive dependency");
        System.out.println("       exports com.example.api;         // Public API");
        System.out.println("       exports com.example.impl to com.example.test;  // Qualified export");
        System.out.println("       opens com.example.model;         // Allow reflection");
        System.out.println("   }");
        System.out.println("   ```\n");

        // 3. Key directives
        System.out.println("3. Key Module Directives:");
        System.out.println("   - requires: Declare dependency on another module");
        System.out.println("   - requires transitive: Re-export dependency to consumers");
        System.out.println("   - exports: Make package accessible to other modules");
        System.out.println("   - exports...to: Qualified export to specific module");
        System.out.println("   - opens: Allow reflective access (for frameworks like Spring)");
        System.out.println("   - uses: Consume a service");
        System.out.println("   - provides...with: Provide service implementation\n");

        // 4. Services
        System.out.println("4. Services (Provider/Consumer Pattern):");
        System.out.println("   Provider module:");
        System.out.println("   ```java");
        System.out.println("   module com.example.provider {");
        System.out.println("       provides com.example.api.Service");
        System.out.println("           with com.example.impl.ServiceImpl;");
        System.out.println("   }");
        System.out.println("   ```");
        System.out.println("   Consumer module:");
        System.out.println("   ```java");
        System.out.println("   module com.example.consumer {");
        System.out.println("       uses com.example.api.Service;");
        System.out.println("   }");
        System.out.println("   ```\n");

        // 5. Migration strategies
        System.out.println("5. Migration Strategies:");
        System.out.println("   Bottom-up: Convert libraries first, then applications");
        System.out.println("   Top-down: Convert applications first using automatic modules");
        System.out.println("   Automatic modules: JARs on module path become modules automatically\n");

        // 6. Common JDK modules
        System.out.println("6. Common JDK Modules:");
        System.out.println("   - java.base: Core (always available, no 'requires' needed)");
        System.out.println("   - java.logging: java.util.logging");
        System.out.println("   - java.sql: JDBC");
        System.out.println("   - java.xml: XML processing");
        System.out.println("   - java.desktop: Swing, AWT");
        System.out.println("   - java.net.http: HTTP Client (Java 11+)\n");

        // 7. Tools
        System.out.println("7. Module Tools:");
        System.out.println("   - jdeps: Analyze dependencies");
        System.out.println("   - jmod: Create module files");
        System.out.println("   - jlink: Create custom runtime");
        System.out.println("   Example: jdeps --jdk-internals myapp.jar\n");

        System.out.println("=== EXERCISES ===");
        System.out.println("Exercise 14.1: Use jdeps to analyze a JAR's dependencies");
        System.out.println("Exercise 14.2: Create a simple modular application (requires Java 9+)");
        System.out.println("Exercise 14.3: Create a minimal JRE with jlink (requires Java 9+)");
    }
}

/*
 * Example module structure (for Java 9+ projects):
 * 
 * src/
 * com.example.api/ <- Module directory
 * module-info.java
 * com/example/api/
 * MyService.java
 * 
 * com.example.impl/ <- Module directory
 * module-info.java
 * com/example/impl/
 * MyServiceImpl.java
 * 
 * com.example.app/ <- Module directory
 * module-info.java
 * com/example/app/
 * Main.java
 * 
 * 
 * Example module-info.java for API module:
 * 
 * module com.example.api {
 * exports com.example.api;
 * }
 * 
 * 
 * Example module-info.java for implementation module:
 * 
 * module com.example.impl {
 * requires com.example.api;
 * provides com.example.api.MyService
 * with com.example.impl.MyServiceImpl;
 * }
 * 
 * 
 * Example module-info.java for application module:
 * 
 * module com.example.app {
 * requires com.example.api;
 * uses com.example.api.MyService;
 * }
 */
