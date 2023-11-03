## Table of Contents
- [Payment System Task](#payment-system-task)
    * [Technical requirements](#technical-requirements)
    * [Task](#task)
    * [Submission](#submission)
- [Implementation](#implementation)    
    * [Structure](#structure)
    * [Configuration](#configuration)
    * [Testing](#testing)
- [Linter](#linter)
- [Running the project locally](#Running-the-project-locally)

## Payment System Task

### Technical requirements

1. Use Spring MVC / Hibernate ORM / MySQL / PostgreSQL
2. Frontend Framework
    * React JS (optional)
    * Bootstrap
3. Cover all changes with JUnit tests
4. Add integration tests (optional)
5. Include code linters
6. For models, try to use:
    * Hibernate / JPA Single Table Inheritance
    * Polymorphic associations
    * Scopes
    * Validations and custom validator object, if necessary
    * Factory pattern
    * Encapsulate some logic in packages
7. For controllers, try to:
    * Keep them **thin**
    * Encapsulate business logic in service objects
8. Try to showcase background and cron jobs
9. Dockerize the Application (optional)
    * Create the application in the Docker environment
    * Use application and database containers
    * Use Docker compose - https://docs.docker.com/compose

### Task

1. Relations:  
    1.1. Ensure you have merchant and admin user roles (UI)  
    1.2. Merchants have many payment transactions of different types  
    1.3. Transactions are related (belongs_to)
    * You can also have follow/referenced transactions that refer/depend to/on the initial transaction
        * Authorize Transaction -> Charge Transaction -> Refund Transaction
        * Authorize Transaction -> Reversal Transaction
        * Only **approved** or **refunded** transactions can be referenced otherwise the status of the submitted transaction will be created with **error** status
    * Ensure you prevent a merchant from being deleted unless there are no related payment transactions
2. Models:  
    2.1. Merchant: name , description , email , status (**active**, **inactive**), total_transaction_sum  
    2.2. Transaction: uuid , amount , status (**approved**, **reversed**, **refunded**, **error**), customer_email , customer_phone , reference_id  
   * Use validations for: uuid , amount > 0, customer_email , status
   * Transaction Types
        * **Authorize transaction** - has amount and used to hold customer's amount
        * **Charge** transaction - has amount and used to confirm the amount is taken from the customer's account and transferred to the merchant
            * The merchant's total transactions amount has to be the sum of the **approved Charge** transactions
        * **Refund** transaction - has amount and used to reverse a specific amount (whole amount) of the Charge Transaction and return it to thecustomer
            * Transitions the **Charge** transaction to status **refunded**
            * The **approved Refund** transactions will decrease the merchant's total transaction amount
        * **Reversal** transaction - has no amount , used to invalidate the Authorize Transaction
            * Transitions the **Authorize** transaction to status **reversed**

3. Inputs and tasks:  
    3.1. Imports new merchants and admins from **CSV** (rake task)  
    3.2. A background Job for deleting transactions older than an hour (cron job)  
    3.3. Accepts payments using XML / JSON API (single point **POST** request)  
    * Include API authentication layer (Basic authentication, OAuth or **JWT** authentication)
    * No transactions can be submitted unless the merchant is in **active** state
4. Presentation:  
    4.1. Display, edit, destroy merchants  
    4.2. Display transactions  

### Submission

1. Add the task to a GitHub/Bitbucket/GitLab repo - either a public or a private one. Organize the Git commits the following way:  
    1.1. Initial commit with all changes not directly related to the task - the newly installed Spring app, .gitignore file, etc.  
    1.2. All subsequent commits should be logically organized reflecting the steps you've taken developing the application  
    * Neither one large commit with all changes nor a multitude of smaller commits for every little tiny change.
2. If for some reason you can't provide a GitHub/Bitbucket/GitLab repo, please, at least include the .git folder.
3. Document your code where needed and add a short README.

## Implementation
Spring Boot Application with Hibernate JPA. Supports MySQL and PostgreSQL as a database. Build environment is Gradle with Groovy.

### Structure
  * Four major loosely coupled packages  
    * ```app``` - Application entry points. All classes that have main method are located here  
    * ```common``` - Contains utilities, and helper classes that are commonly used across the application. Does not import/refer to any of the other packages  
    * ```iam``` - Identity and Access Management (IAM). Implements role-based user access. Imports/refers only to ```common``` package  
    * ```merchant``` - All merchant specific functionalities are implemented here. Imports/refers to ```common``` and  to ```iam``` (to implement Merchant->User relation)
  * All application logic is in ```iam``` and ```merchant``` packages with similar structure  
    * ```config``` - Spring Boot Configurations. Sets locations for entity, component, and repository relevant to package  
    * ```controller``` - handles and routes incoming HTTP requests    
    * ```factory``` - implementation of the Factory Design Pattern   
    * ```model``` - entities and data structures  
    * ```repository``` - data access of entities
    * ```security``` - only in ```iam```. Contains configuration and setup of PasswordEncoder and SecurityConfig
    * ```service``` - encapsulates the business logic  
### Testing
Tests are located in the ```test``` directory with package definitions matching those of the classes under test. There are two types of tests ```Happy``` and ```Negative``` and are separated in different testing classes.  

## Linter
[Checkstyle](https://checkstyle.org) is configured and used as a linting tool.  
- Configuration is ```config/checkstyle/checkstyle.xml``` based on https://github.com/checkstyle/checkstyle/blob/master/src/main/resources/sun_checks.xml with modified LineLength to 160 instead of 80.  
- Suppressions are ```config/checkstyle/suppressions.xml```  
- ```./gradlew checkStyleMain``` runs checks in main  
- ```./gradlew checkStyleTest``` runs checks in test    

## Running the project locally

1. Project setup  
    1.1. Clone repository  
    ```
    git clone https://github.com/TaskP/PaymentSystem PaymentSystem
    ```  
    1.2. Build project  
    ```  
    cd PaymentSystem    
    ./gradlew build
    ```
2. Setup Database  
    2.1. MySQL or MariaDB    
    2.1.1. Create database, user and grant privileges to user. You can select a different DB name,Username, an password.  
    Connect to a running server with a user that has grant privileges and execute
    ```
    CREATE DATABASE taskpdb;
    CREATE USER 'taskpusr'@'%' IDENTIFIED BY 'task!@#';
    GRANT ALL PRIVILEGES ON taskpdb.* TO 'taskpusr'@'%';
    FLUSH PRIVILEGES;
    ```  
    or create and run docker container manually
    ```
    docker run --name mysql -p 3306:3306 -e MYSQL_USER=taskpusr -e MYSQL_PASSWORD='task!@#' -e MYSQL_ROOT_PASSWORD=rPwd931 -e MYSQL_DATABASE=taskpdb -d mysql:8.2
    ```   
    or use the provided docker-compose.yml
    ```
    docker-compose --profile mysql up
    ```
    2.1.2. Set server IP instead of 192.168.122.1, DB name,Username, and password in application.properties and in application-cli.properties
    ```
    spring.datasource.username=taskpusr
    spring.datasource.password=task!@#
    spring.datasource.url=jdbc:mysql://192.168.122.1:3306/taskpdb
    ```
    2.2. PostgreSQL  
    2.2.1. Create database, user and grant privileges to user. You can select a different DB name,Username, an password.  
    Connect to a running server with a user that has SUPERUSER role and execute
    ```
    CREATE DATABASE taskpdb;
    CREATE USER taskpusr WITH PASSWORD 'task!@#' NOCREATEDB LOGIN;
    ALTER DATABASE taskpdb OWNER TO taskpusr;
    ```  
    or create and run docker container manually
    ```
	docker run --name postgres -p 5432:5432 -e POSTGRES_USER=taskpusr -e POSTGRES_PASSWORD='task!@#' -e POSTGRES_DB=taskpdb -d postgres:16.0
    ```   
    or use the provided docker-compose.yml
    ```
    docker-compose --profile postgres up
    ```
    2.2.2. Set server IP instead of 192.168.122.1, DB name,Username, and password in application.properties and in application-cli.properties
    ```
    spring.datasource.username=taskpusr
    spring.datasource.password=task!@#
    spring.datasource.url=jdbc:postgresql://192.168.122.1:5432/taskpdb
    ```
3. Load initial data  
    3.1. Load users from data/users.csv  
	Format: Column 1 - Username, Column 2 - Full name, Column 3 - Password, Column 4 - Role, Column 5 (Optional) - Status  
	Status of newly imported users is active unless there is Column 5 with case insensitive "false" or "inactive"  
 
    ```
    ./gradlew userImport -PCSVFile=data/users.csv
    ```
    or
    ```
    java -cp build/libs/PaymentSystem-1.0.1.jar -Dspring.profiles.active=cli -Dloader.main=com.example.payment.app.AppCliUserImport org.springframework.boot.loader.PropertiesLauncher data/users.csv
    ```
    3.2. Load merchants from data/merchants.csv  
	Format: Column 1 - Name, Column 2 - Description, Column 3 - Email, Column 4 (Optional) - Status  
	Status of newly imported merchants is active unless there is Column 4 with case insensitive "false" or "inactive"  
 
    ```
    ./gradlew merchantImport -PCSVFile=data/merchants.csv
    ```
    or
    ```
    java -cp build/libs/PaymentSystem-1.0.1.jar -Dspring.profiles.active=cli -Dloader.main=com.example.payment.app.AppCliMerchantImport org.springframework.boot.loader.PropertiesLauncher data/merchants.csv
    ```
    
        


    
