# Hibernate ORM Examples

This repository contains two Java applications demonstrating Hibernate ORM (Object-Relational Mapping) functionality with different use cases:

1. **Student CRUD Operations** - Basic Hibernate operations with HQL queries
2. **Alien-Laptop Relationship** - Many-to-Many relationship mapping

## Prerequisites

- Java Development Kit (JDK) 8 or higher
- MySQL/PostgreSQL database installed and running
- Hibernate ORM libraries
- JDBC driver for your database

## Project Structure

```
project/
├── Main.java (Student Example)
├── Student.java (Entity class)
├── org/example/
│   ├── Main.java (Alien-Laptop Example)
│   ├── Alien.java (Entity class)
│   └── Laptop.java (Entity class)
└── hibernate.con.xml (Hibernate configuration)
```

## Dependencies

### Maven Dependencies

Add these to your `pom.xml`:

```xml
<dependencies>
    <!-- Hibernate Core -->
    <dependency>
        <groupId>org.hibernate</groupId>
        <artifactId>hibernate-core</artifactId>
        <version>6.2.0.Final</version>
    </dependency>
    
    <!-- MySQL Connector (or PostgreSQL) -->
    <dependency>
        <groupId>com.mysql</groupId>
        <artifactId>mysql-connector-j</artifactId>
        <version>8.0.33</version>
    </dependency>
    
    <!-- For PostgreSQL, use this instead:
    <dependency>
        <groupId>org.postgresql</groupId>
        <artifactId>postgresql</artifactId>
        <version>42.6.0</version>
    </dependency>
    -->
</dependencies>
```

### Gradle Dependencies

```gradle
implementation 'org.hibernate:hibernate-core:6.2.0.Final'
implementation 'com.mysql:mysql-connector-j:8.0.33'
// or for PostgreSQL: implementation 'org.postgresql:postgresql:42.6.0'
```

## Configuration

### hibernate.con.xml

Create a `hibernate.con.xml` file in your resources directory:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE hibernate-configuration PUBLIC
        "-//Hibernate/Hibernate Configuration DTD 3.0//EN"
        "http://www.hibernate.org/dtd/hibernate-configuration-3.0.dtd">
<hibernate-configuration>
    <session-factory>
        <!-- Database connection settings -->
        <property name="hibernate.connection.driver_class">com.mysql.cj.jdbc.Driver</property>
        <property name="hibernate.connection.url">jdbc:mysql://localhost:3306/your_database</property>
        <property name="hibernate.connection.username">root</property>
        <property name="hibernate.connection.password">your_password</property>
        
        <!-- SQL dialect -->
        <property name="hibernate.dialect">org.hibernate.dialect.MySQLDialect</property>
        
        <!-- Echo all executed SQL to stdout -->
        <property name="hibernate.show_sql">true</property>
        <property name="hibernate.format_sql">true</property>
        
        <!-- Drop and re-create the database schema on startup -->
        <property name="hibernate.hbm2ddl.auto">update</property>
        
        <!-- Connection pool size -->
        <property name="hibernate.connection.pool_size">10</property>
    </session-factory>
</hibernate-configuration>
```

## Example 1: Student CRUD Operations

### Features Demonstrated

- **SessionFactory Configuration** - Setting up Hibernate
- **HQL Queries** - Querying with Hibernate Query Language
- **CRUD Operations** (commented):
  - Create: `session.persist(st)`
  - Read: `session.get(Student.class, id)`
  - Update: `session.merge(st)`
  - Delete: `session.remove(st)`

### Student Entity

Create the `Student.java` entity class:

```java
import jakarta.persistence.*;

@Entity
@Table(name = "student")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int rollNo;
    
    private String name;
    private int age;
    
    // Constructors, getters, and setters
    public Student() {}
    
    public int getRollNo() { return rollNo; }
    public void setRollNo(int rollNo) { this.rollNo = rollNo; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }
    
    @Override
    public String toString() {
        return "Student{rollNo=" + rollNo + ", name='" + name + "', age=" + age + "}";
    }
}
```

### Current Functionality

The program performs an HQL query to find all students named 'Rose':

```java
Query query = session.createQuery("from Student where name Like 'Rose'", Student.class);
List<Student> students = query.getResultList();
```

### How to Run

```bash
javac Main.java Student.java
java Main
```

## Example 2: Alien-Laptop Many-to-Many Relationship

### Features Demonstrated

- **Many-to-Many Relationship** - Bidirectional mapping between Alien and Laptop
- **Cascade Operations** - Persisting related entities
- **Join Tables** - Automatic creation of relationship tables
- **Batch Persistence** - Saving multiple entities in one transaction

### Entity Classes

#### Alien.java

```java
package org.example;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "alien")
public class Alien {
    @Id
    private int aid;
    private String aname;
    private String tech;
    
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
        name = "alien_laptop",
        joinColumns = @JoinColumn(name = "alien_id"),
        inverseJoinColumns = @JoinColumn(name = "laptop_id")
    )
    private List<Laptop> laptops;
    
    // Constructors, getters, setters, and toString
}
```

#### Laptop.java

```java
package org.example;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "laptop")
public class Laptop {
    @Id
    private int lid;
    private String brand;
    private String model;
    private int ram;
    
    @ManyToMany(mappedBy = "laptops")
    private List<Alien> aliens;
    
    // Constructors, getters, setters, and toString
}
```

### Database Schema

The application creates three tables:

1. **alien** - Stores alien information
2. **laptop** - Stores laptop information
3. **alien_laptop** - Join table for many-to-many relationship

### Sample Data

The program creates:
- 3 Aliens: Nidhi (Java), Soumya (Python), Ramya (AI)
- 3 Laptops: Asus Rog (16GB), Lenovo ThinkPad (64GB), Dell XPS (32GB)

With relationships:
- Nidhi owns Asus and Lenovo
- Soumya owns Lenovo and Dell
- Ramya owns Asus

### How to Run

```bash
javac -d . org/example/*.java
java org.example.Main
```

## Common Operations

### Create (Insert)
```java
Transaction transaction = session.beginTransaction();
session.persist(entity);
transaction.commit();
```

### Read (Retrieve)
```java
Student student = session.get(Student.class, id);
```

### Update
```java
Transaction transaction = session.beginTransaction();
session.merge(entity);
transaction.commit();
```

### Delete
```java
Transaction transaction = session.beginTransaction();
session.remove(entity);
transaction.commit();
```

### HQL Queries
```java
Query<Student> query = session.createQuery("from Student where age > :age", Student.class);
query.setParameter("age", 20);
List<Student> students = query.getResultList();
```

## Important Notes

⚠️ **Always Close Resources**: Both examples properly close session and SessionFactory

⚠️ **Transaction Management**: All write operations (persist, merge, remove) must be within a transaction

⚠️ **Cascade Types**: The Alien-Laptop example uses `CascadeType.ALL`, which propagates all operations to related entities

⚠️ **Lazy vs Eager Loading**: Be aware of fetch strategies when accessing related entities

## Troubleshooting

### ClassNotFoundException: jakarta.persistence.*
- Ensure Jakarta Persistence API is in your classpath
- Use Hibernate 6.x with Jakarta instead of javax

### Table not found
- Check `hibernate.hbm2ddl.auto` property (use "update" or "create")
- Verify database credentials and URL

### LazyInitializationException
- Fetch related entities within an active session
- Use `FetchType.EAGER` or initialize collections before closing session

### Duplicate entry
- Ensure IDs are unique when manually setting them
- Use `@GeneratedValue` for auto-increment

## Best Practices

1. **Use try-with-resources** for automatic resource management
2. **Implement proper exception handling** around database operations
3. **Use named queries** for frequently used HQL queries
4. **Enable connection pooling** for production applications
5. **Never commit hardcoded credentials** - use environment variables
6. **Use PreparedStatements/Parameters** to prevent SQL injection

## Learning Path

1. Start with Example 1 (Student) to understand basic CRUD operations
2. Move to Example 2 (Alien-Laptop) to learn relationship mapping
3. Experiment with different HQL queries
4. Try other relationship types (One-to-One, One-to-Many)
5. Explore Criteria API for type-safe queries

## Additional Resources

- [Hibernate Documentation](https://hibernate.org/orm/documentation/)
- [Jakarta Persistence API](https://jakarta.ee/specifications/persistence/)
- [HQL Reference](https://docs.jboss.org/hibernate/orm/6.2/userguide/html_single/Hibernate_User_Guide.html#hql)

## License

These examples are for educational purposes demonstrating Hibernate ORM capabilities.
