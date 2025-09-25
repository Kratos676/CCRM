# Campus Course & Records Manager (CCRM)

A comprehensive Java SE console application for managing students, courses, enrollments, and academic records in an educational institution.

## Project Overview

CCRM is a feature-rich academic management system that demonstrates advanced Java programming concepts including Object-Oriented Programming, Design Patterns, Stream API, NIO.2, and modern Java features. The application provides a complete solution for academic administration with robust data management capabilities.

## How to Run

- **JDK Version**: Requires Java SE 17 or higher (developed on JDK 21).

- **Clone the repository**:

```
git clone https://github.com/Kratos676/CCRM
```

- **Compile & Run**: Open the project in an IDE like Eclipse and run the `edu.ccrm.CampusCourseRecordsManager` class.

- **Enable Assertions**: To run with assertions enabled (for the demo), use the VM argument `-ea`.

## Java Evolution Timeline

### Major Java Milestones

- **1995**: Java 1.0 - Initial release by Sun Microsystems
- **1997**: Java 1.1 - Inner classes, JDBC, RMI
- **1998**: Java 1.2 (J2SE) - Collections Framework, Swing
- **2000**: Java 1.3 - HotSpot JVM, JNDI
- **2002**: Java 1.4 - Assertions, NIO, XML processing
- **2004**: Java 5.0 - Generics, Enums, Annotations, Autoboxing
- **2006**: Java 6 - Performance improvements, Scripting API
- **2011**: Java 7 - Diamond operator, Try-with-resources, NIO.2
- **2014**: Java 8 - Lambda expressions, Stream API, Default methods
- **2017**: Java 9 - Module system, JShell
- **2018**: Java 10 - Local variable type inference (var)
- **2018**: Java 11 - HTTP Client, String methods (LTS)
- **2019**: Java 12-13 - Switch expressions (preview)
- **2020**: Java 14 - Records (preview), Pattern matching
- **2021**: Java 17 - Sealed classes, Pattern matching (LTS)
- **2022**: Java 18-19 - Virtual threads (preview)
- **2023**: Java 20-21 - Virtual threads, Pattern matching (LTS)

## Java Platform Comparison

| Feature       | Java ME (Micro Edition) | Java SE (Standard Edition)  | Java EE (Enterprise Edition) |
| ------------- | ----------------------- | --------------------------- | ---------------------------- |
| **Target**    | Mobile/Embedded devices | Desktop & basic server apps | Enterprise applications      |
| **Memory**    | Limited (KB to few MB)  | Moderate (MB to GB)         | Large (GB+)                  |
| **APIs**      | Basic Java + MIDP/CDC   | Complete Java API           | Java SE + Enterprise APIs    |
| **Examples**  | Mobile games, IoT       | Desktop apps, utilities     | Web apps, microservices      |
| **Runtime**   | Minimal JVM             | Full JVM                    | Application servers          |
| **Use Cases** | Smart cards, sensors    | CCRM-like applications      | Banking, e-commerce          |

## Java Architecture: JDK, JRE, JVM

### Java Virtual Machine (JVM)

- **Purpose**: Runtime environment that executes Java bytecode
- **Components**: Class loader, Memory areas (Heap, Stack, Method area), Execution engine
- **Platform-specific**: Different implementations for Windows, Linux, macOS

### Java Runtime Environment (JRE)

- **Includes**: JVM + Java standard libraries + Supporting files
- **Purpose**: Minimum environment needed to run Java applications
- **Contents**: JVM, Core libraries (java.lang, java.util, etc.), Configuration files

### Java Development Kit (JDK)

- **Includes**: JRE + Development tools + Documentation
- **Tools**: javac (compiler), java (launcher), javadoc, jar, debugger
- **Purpose**: Complete environment for developing Java applications

```
┌─────────────────────────────────────┐
│               JDK                   │
│  ┌───────────────────────────────┐  │
│  │             JRE               │  │
│  │  ┌─────────────────────────┐  │  │
│  │  │         JVM             │  │  │
│  │  │  • Class Loader         │  │  │
│  │  │  • Memory Management    │  │  │
│  │  │  • Execution Engine     │  │  │
│  │  └─────────────────────────┘  │  │
│  │  • Standard Libraries         │  │
│  │  • Configuration Files        │  │
│  └───────────────────────────────┘  │
│  • Development Tools (javac, etc.)  │
│  • Documentation                    │
└─────────────────────────────────────┘
```

## Windows Installation & Configuration

### Step 1: Download JDK

1. Visit [Oracle JDK](https://www.oracle.com/java/technologies/downloads/) or [OpenJDK](https://openjdk.org/)
2. Download appropriate version for Windows (x64)
3. Choose installer (.exe) or archive (.zip)

### Step 2: Install JDK

1. Run the installer as Administrator
2. Follow installation wizard
3. Default location: `C:\Program Files\Java\jdk-<version>`
4. Note the installation path

### Step 3: Configure Environment Variables

1. Open System Properties → Advanced → Environment Variables
2. **System Variables**:
   - **JAVA_HOME**: `C:\Program Files\Java\jdk-<version>`
   - **PATH**: Add `%JAVA_HOME%\bin`

### Step 4: Verify Installation

```cmd
# Open Command Prompt and run:
java -version
javac -version
echo %JAVA_HOME%
```

**Expected Output:**

```
java version "17.0.2" 2022-01-18 LTS
Java(TM) SE Runtime Environment (build 17.0.2+8-LTS-86)
Java HotSpot(TM) 64-Bit Server VM (build 17.0.2+8-LTS-86, mixed mode, sharing)
```

### Screenshots Required:

#### 1. Java Version Verification

![Java Version](screenshots/01_Java%20Version.png)
_JDK installation verification showing `java -version` command output_

#### 2. Project Execution - Initial Run

![Project Run](screenshots/02_Project%20run.png)
_Project compilation and initial execution setup_

#### 3. Application Main Menu

![Code Run 1](screenshots/03_Code%20run.png)
_CCRM application main menu and navigation_

#### 4. Student Management Features

![Code Run 2](screenshots/04_Code%20run2.png)
_Student management functionality demonstration_

#### 5. Course Management Features

![Code Run 3](screenshots/05_Code%20run3.png)
_Course management and advanced features in action_

#### 6. Backup and Data Management

![Backup Feature](screenshots/06_backup.png)


## Eclipse IDE Setup

### Step 1: Download Eclipse

1. Visit [Eclipse Downloads](https://www.eclipse.org/downloads/)
2. Download "Eclipse IDE for Java Developers"
3. Install and launch Eclipse

### Step 2: Create New Java Project

1. File → New → Java Project
2. **Project Name**: `CCRM`
3. **JRE Version**: Use default or select specific version
4. **Module**: Uncheck "Create module-info.java"
5. Click Finish

### Step 3: Import Source Code

1. Right-click project → Import
2. General → File System
3. Browse to CCRM source directory
4. Import all `.java` files maintaining package structure

### Step 4: Configure Run Configuration

1. Right-click `CampusCourseRecordsManager.java`
2. Run As → Run Configurations
3. **Main Class**: `edu.ccrm.CampusCourseRecordsManager`
4. **VM Arguments**: `-ea` (to enable assertions)
5. Apply and Run

## Technical Requirements Demonstration

### Language & Core Features

| Topic                   | Implementation Location     | Class/Method                     |
| ----------------------- | --------------------------- | -------------------------------- |
| **Primitive Variables** | MainMenu.java               | demonstrateOperators()           |
| **Objects & Classes**   | domain/\*.java              | All domain classes               |
| **Operators**           | MainMenu.java               | demonstrateOperators()           |
| **Decision Structures** | MainMenu.java, Student.java | processMenuChoice(), fromMarks() |
| **Loops**               | MainMenu.java               | demonstrateLoops()               |
| **Arrays**              | MainMenu.java               | demonstrateArrays()              |
| **Strings**             | Name.java, MainMenu.java    | String methods demo              |

### OOP Concepts

| Concept           | Implementation              | Example                          |
| ----------------- | --------------------------- | -------------------------------- |
| **Encapsulation** | All domain classes          | Private fields + getters/setters |
| **Inheritance**   | Person → Student/Instructor | Abstract class extension         |
| **Abstraction**   | Person.java                 | Abstract methods                 |
| **Polymorphism**  | Person references           | getPersonType(), toString()      |

### Advanced Features

| Feature                   | Location                          | Description                    |
| ------------------------- | --------------------------------- | ------------------------------ |
| **Interfaces**            | Persistable.java, Searchable.java | Default methods, generics      |
| **Enums**                 | Grade.java, Semester.java         | With constructors and methods  |
| **Design Patterns**       | AppConfig.java, Course.Builder    | Singleton, Builder             |
| **Exceptions**            | util/\*.java                      | Custom checked/unchecked       |
| **Nested Classes**        | Person.PersonComparator           | Static nested class            |
| **Inner Classes**         | Student.StudentProgress           | Non-static inner class         |
| **Lambda Expressions**    | StudentService.java               | Stream operations              |
| **Functional Interfaces** | Searchable.java                   | Predicate usage                |
| **Stream API**            | service/\*.java                   | Filtering, mapping, collecting |
| **Date/Time API**         | Person.java, Enrollment.java      | LocalDate usage                |
| **NIO.2**                 | io/\*.java                        | Path, Files operations         |

## Enabling Assertions

Assertions are used throughout the codebase for runtime validation.

### Enable Assertions:

```bash
# Command line
java -ea edu.ccrm.CampusCourseRecordsManager

# Eclipse: Run Configurations → Arguments → VM Arguments
-ea

# Enable for specific packages
-ea:edu.ccrm.domain...

# Enable all except specific class
-ea -da:edu.ccrm.util.DebugClass
```

### Sample Commands:

```bash
# Compile with assertions
javac -d bin src/main/java/edu/ccrm/*.java

# Run with assertions enabled
java -ea -cp bin edu.ccrm.CampusCourseRecordsManager

# Run with verbose assertion output
java -ea -esa -cp bin edu.ccrm.CampusCourseRecordsManager
```

## Project Structure

```
CCRM/
├── src/main/java/edu/ccrm/
│   ├── CampusCourseRecordsManager.java    # Main class
│   ├── cli/
│   │   └── MainMenu.java                  # Console interface
│   ├── config/
│   │   └── AppConfig.java                 # Singleton configuration
│   ├── domain/
│   │   ├── Person.java                    # Abstract base class
│   │   ├── Student.java                   # Student entity
│   │   ├── Instructor.java                # Instructor entity
│   │   ├── Course.java                    # Course entity (Builder pattern)
│   │   ├── Enrollment.java                # Enrollment relationship
│   │   ├── Grade.java                     # Grade enum
│   │   ├── Semester.java                  # Semester enum
│   │   ├── CourseCode.java                # Immutable value class
│   │   ├── Name.java                      # Immutable value class
│   │   ├── Persistable.java               # Interface
│   │   ├── Searchable.java                # Generic interface
│   │   └── Auditable.java                 # Interface with default methods
│   ├── service/
│   │   ├── StudentService.java            # Student business logic
│   │   ├── CourseService.java             # Course business logic
│   │   ├── EnrollmentService.java         # Enrollment management
│   │   └── TranscriptService.java         # Transcript generation
│   ├── io/
│   │   ├── ImportExportService.java       # CSV import/export
│   │   └── BackupService.java             # Backup utilities
│   └── util/
│       ├── DuplicateEnrollmentException.java
│       └── MaxCreditLimitExceededException.java
├── data/                                  # Runtime data directory
├── test-data/                            # Sample CSV files
│   ├── students.csv
│   ├── courses.csv
│   └── instructors.csv
├── screenshots/                          # Installation & demo screenshots
└── README.md                            # This file
```

## Sample Usage

### Demo Flow:

1. **Start Application**: Run main class with assertions enabled
2. **Navigate Menus**: Use number selection for operations
3. **Demo Operations**: Menu option 9 shows language features
4. **Add Sample Data**: Import from test-data CSV files
5. **Generate Reports**: View statistics and transcripts
6. **Export/Backup**: Test file operations

### Key Features to Test:

- Object creation using Builder pattern
- Polymorphic method calls
- Exception handling scenarios
- Stream API operations
- File I/O operations
- Enum functionality

## Error Handling

### Common Issues:

1. **Assertion Errors**: Enable assertions with `-ea` flag
2. **Memory Issues**: Increase heap size with `-Xmx512m`
3. **File Permissions**: Ensure write access to data directory
4. **CSV Format**: Follow exact format in test-data samples

### Debug Mode:

```bash
# Run with debug output
java -ea -Djava.util.logging.config.file=logging.properties -cp bin edu.ccrm.CampusCourseRecordsManager
```

## Future Enhancements

- **Database Integration**: Replace in-memory storage with JPA/Hibernate
- **Web Interface**: Spring Boot REST API with Angular frontend
- **Security**: Authentication and authorization
- **Microservices**: Split into domain-specific services
- **Cloud Deployment**: Docker containers with Kubernetes

## References

1. **Oracle Java Documentation**

   - [Java SE 17 Documentation](https://docs.oracle.com/en/java/javase/17/)
   - [Java Language Specification](https://docs.oracle.com/javase/specs/jls/se17/html/index.html)

2. **Design Patterns**

   - Gang of Four (GoF) Design Patterns
   - Singleton Pattern Implementation
   - Builder Pattern for Complex Objects

3. **Java Best Practices**

   - [Effective Java by Joshua Bloch](https://www.oreilly.com/library/view/effective-java-3rd/9780134686097/)
   - Oracle Java Coding Standards
   - Clean Code Principles

4. **Stream API and Functional Programming**

   - [Java 8 in Action](https://www.manning.com/books/java-8-in-action)
   - Oracle Stream API Documentation
   - Functional Interface Guidelines

5. **File I/O and NIO.2**
   - [Java NIO.2 File API](https://docs.oracle.com/javase/tutorial/essential/io/fileio.html)
   - Path and Files API Documentation

## Acknowledgments

This project demonstrates comprehensive Java SE programming concepts as required for academic evaluation. All code is original implementation showcasing:

- **Object-Oriented Design**: Clean separation of concerns
- **Design Patterns**: Industry-standard implementations
- **Modern Java**: Stream API, functional programming
- **Best Practices**: Documentation, error handling, testing

---

**Author**: Yash Nilesh Deshpande  
**Course**: (CSE2006) Programming in Java  
**Institution**: Vellore Institute of Technology, Bhopal  
**Version**: 1.0  
**License**: Academic Use Only  
**Last Updated**: September 2025
