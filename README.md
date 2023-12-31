## Table of Contents
- Payment System
    * [Technical requirements](#technical-requirements)
    * [Task](#task)
    * [Submission](#submission)
- [Implementation](#implementation)    
    * [Structure](#structure)
    * [Transaction Business Logic](#transaction-business-logic)
    * [Testing](#testing)
    * [Linter](#linter)
- [Project setup](#project-setup)
- [Database setup](#database-setup)
- [Check build](#check-build)
- [Build Docker image](#build-docker-image)
- [Load initial data](#load-initial-data)
- [API](#api)

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
    
### Transaction Business Logic  
1. Orders for Tangible Products (Shippable Goods)  
   1.1 - Authorize Transaction: Hold customer's amount when the order is created. Transaction status is pending.  
   1.2 - Charge Transaction: Successfully or partially process the order and charge the full or partial amount of the held amount. Reference transaction is in '1.1'. Change the status of the referenced transaction to 'Approved' .  
   1.3 - Refund Transaction: Customer returns the order, resulting in a refund of the amount collected in the Charge transaction. Reference transaction is in '1.2'. Change the status of the referenced transaction to 'Refunded' .  
   1.4 - Reversal Transaction: Unable to process the order, resulting in the release of the held customer amount. Reference transaction is in '1.1'. Change the status of the referenced transaction to 'Reversed' .  

2. Orders for Non-Tangible Products (e.g., Streaming  Services)  
   2.1 - Charge Transaction: Charge the customer with the full amount.   
   2.2 - Refund Transaction: Customer cancels the order, resulting in a refund of the amount collected in the Charge transaction. Reference transaction is in '2.1'. Change the status of the referenced transaction to 'Refunded' .  

3. Data Cleansing - Transactions that do not reflect in the Merchant totalTransactionSum are eligible for deletion.  
   3.1 - Authorize Transactions with a status pending for more than 1 week are deleted.  
   3.2 - Reversal Transactions with referenced Authorize Transactions older than 1 month are deleted.  

4. Atomic - Logic implementation must be atomic, which means that either all operation steps are done or none.       

  * Distinction between the two scenarios is that the Charge Transaction either has or does not have a reference to the Authorize Transaction.

### Testing  
Tests are located in the ```test``` directory with package definitions matching those of the classes under test. There are two types of tests ```Happy``` and ```Negative``` and are separated in different testing classes.  

### Linter
[Checkstyle](https://checkstyle.org) is configured and used as a linting tool.  
- Configuration is ```config/checkstyle/checkstyle.xml``` based on https://github.com/checkstyle/checkstyle/blob/master/src/main/resources/sun_checks.xml with modified LineLength to 160 instead of 80.  
- Suppressions are ```config/checkstyle/suppressions.xml```  
- ```./gradlew checkStyleMain``` runs checks in main  
- ```./gradlew checkStyleTest``` runs checks in test    

### Project setup    
1. Clone repository  
    ```
    git clone https://github.com/TaskP/PaymentSystem PaymentSystem
    ```  
2. Build project - we build project excluding checks and tests since we did not setup database yet   
    ```
    cd PaymentSystem  
    ./gradlew build -x test -x check
    ```
   
### Database setup  
1. MySQL or MariaDB    
    Create database, user and grant privileges to user. You can select a different DB name,Username, an password.  
    Connect to a running server with a user that has grant privileges and execute  
	```  
	CREATE USER 'taskpusr'@'%' IDENTIFIED BY 'task!@#';  
	CREATE DATABASE taskpdb;  
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
2. PostgreSQL  
    Create database, user and grant privileges to user. You can select a different DB name,Username, an password.  
    Connect to a running server with a user that has SUPERUSER role and execute  
    ```
    CREATE USER taskpusr WITH PASSWORD 'task!@#' NOCREATEDB LOGIN;  
    CREATE DATABASE taskpdb;  
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

### Check build	  
1. Set database connection properties as environment variables
	```
	export TASKP_DB_URL='jdbc:mysql://127.0.0.1:3306/taskpdb'
	export TASKP_DB_USER='taskpusr'
	export TASKP_DB_PASS='task!@#'
2. Do check
	```
	./gradlew check
	``` 
    
### Build Docker image
1. Build project 	
	```
	./gradlew build
	``` 
2. Build image
	```
	docker build -t taskp .
	``` 
	or use the provided docker-compose.yml
	```
	docker-compose --profile taskp build
	``` 
	
### Load initial data      
1. Format  
	1.1. Users    
	Format: Column 1 - Username, Column 2 - Full name, Column 3 - Password, Column 4 - Role, Column 5 (Optional) - Status  
	Status of newly imported users is active unless there is Column 5 with case insensitive "false" or "inactive"  
	1.2. Merchants  
	Format: Column 1 - Name, Column 2 - Description, Column 3 - Email, Column 4 (Optional) - Status  
	The status of newly imported merchants is set to "active" by default.  
	If Column 4 contains "false" or "inactive" (case insensitive), the status is set to "inactive."  
	A new user will be created for each merchant, and their username and password will be set to the lowercase of their email (if provided) or name. 

2. Manually  
    2.1. Load users from data/users.csv    
    ```
    ./gradlew userImport -PCSVFile=data/users.csv  
    ```
    or  
    ```
    java -cp build/libs/PaymentSystem.jar -Dspring.profiles.active=cli \  
    -Dloader.main=com.example.payment.app.main.AppCliUserImport \  
    org.springframework.boot.loader.PropertiesLauncher data/users.csv
    ```
    2.2. Load merchants from data/merchants.csv    
    ```
    ./gradlew merchantImport -PCSVFile=data/merchants.csv  
    ```
    or  
    ```
    java -cp build/libs/PaymentSystem.jar -Dspring.profiles.active=cli \  
    -Dloader.main=com.example.payment.app.main.AppCliMerchantImport \  
    org.springframework.boot.loader.PropertiesLauncher data/merchants.csv  
    ```
3. Using docker  
    ```
    docker run -it --rm --entrypoint /opt/taskp/import.sh taskp:latest
    ```
### Start local  
1. Via gradle  
	```
	./gradlew bootRun
	```  
2. Command line  
	```
	java -jar build/libs/PaymentSystem.jar
	```
	 
3. In Docker  
    ```
    docker run --rm --name taskp -p 8080:8080 \  
	-e TASKP_DB_URL='jdbc:mysql://127.0.0.1:3306/taskpdb' \  
	-e TASKP_DB_USER='taskpusr' \  
	-e TASKP_DB_PASS='task!@#' \  
	-d taskp
	```
	or via docker-compose
	```
	docker-compose --profile taskp up
	```
### Deploy to remote host  
1. Prepare remote linux host  
2. Install docker and docker-compose and start docker   
3. Create virtual interface add assign ip  
	```
	#on remote
	ip link add virbr0 type bridge
	ip -4 addr add 192.168.122.1/24 dev virbr0
	ip link set dev virbr0 up
	```   
4. Create and run MySQL container  
	```
	#on remote	
	docker run --name mysql -p 3306:3306 \  
	-e MYSQL_USER=taskpusr \  
	-e MYSQL_PASSWORD='task!@#' \  
	-e MYSQL_ROOT_PASSWORD=rPwd931 \  
	-e MYSQL_DATABASE=taskpdb -d mysql:8.2
	```
5. Load and start image

	```
	#on remote
	mkdir -p /opt/taskp/docker/image
	```
		
	```
	#on local  
	rsync -t --checksum --ignore-times --progress -avz -e 'ssh ' "build/taskp.tar" root@remote:/opt/taskp/docker/image/taskp.tar
	```
	
	```
	#on remote
	docker load -i /opt/taskp/docker/image/taskp.tar
	
	docker run --name taskp -p 8080:8080 \  
	-e TASKP_DB_URL='jdbc:mysql://192.168.122.1:3306/taskpdb' \  
	-e TASKP_DB_USER='taskpusr' \  
	-e TASKP_DB_PASS='task!@#' \  
	-d taskp
	```
	```
	#on remote Load data
	docker exec -it taskp /opt/taskp/import.sh	
	```
	
### API 	  
1. Prepare environment. Export variables url, username and password with administrator role and merchant username and password  
	```
	export TASK_URL="http://127.0.0.1:8080"
	export ADMIN_AUTH='root:root!@#'
	export MERCHANT_AUTH='acme@example.com:acme@example.com'
	```
2. Users - supports both urls "/api/user" and "/api/users"  
	2.1. List users. Method GET.  
	```
	curl -k --user ${ADMIN_AUTH} ${TASK_URL}/api/user
	```
	or  
	```
	curl -k --user ${ADMIN_AUTH} ${TASK_URL}/api/users  
	```
	2.2. Find User by username. If username is null or empty then lists all users. Method GET.  	
	```
	curl -k --user ${ADMIN_AUTH} ${TASK_URL}/api/users?username=root
	```
	2.3. Create user. If id is not set then a new one is set internally. Method POST. On success returns an HTTP 201 (Created), on duplicate user returns HTTP 409 Conflict. On invalid data returns HTTP 400.  
	```
	curl -k -X POST --user ${ADMIN_AUTH} ${TASK_URL}/api/users \
	-H 'Content-Type: application/json' \
	-d '{"id":1698709109326709,"username":"CurlTest","fullName":"Merchant","password":"merchant!@#","role":1,"status":true}'
    ```  
    2.4. Update user. Method PUT. On success returns an HTTP 200 (OK) status code. On user not found returns HTTP 404 Not found. On duplicate user error returns HTTP 409 Conflict. On invalid data returns HTTP 400.    
    ```
	curl -k -X PUT --user ${ADMIN_AUTH} ${TASK_URL}/api/users/1698709109326709 \
	-H 'Content-Type: application/json' \
	-d '{"id":1698709109326709,"username":"CurlTestUpdate","fullName":"Merchant Update","password":null,"roleName":"Merchant","status":true}'
    ```
	2.5. Delete user by user id. Method: DELETE. On success returns an HTTP 204 (NO CONTENT) status code. On user not found returns HTTP 404 Not found.  On user that is referenced by merchant(constraint violation) returns HTTP 400 Bad request
    ```
    curl -k -X DELETE --user ${ADMIN_AUTH} ${TASK_URL}/api/users/1698709109326709
    ```
 3. Transactions  
    3.1. List transactions for the authorized merchant. In pages. Page size is 10.  
    ```
    curl -k --user ${MERCHANT_AUTH} ${TASK_URL}/api/merchant/transaction?page=1
    ```
    3.2. Create Authorize
    ```
    curl -k -X POST --user ${MERCHANT_AUTH} ${TASK_URL}/api/merchant/transaction \
    -H 'Content-Type: application/json' \
    -d '{"type":"Authorize","amount":10,"customerEmail":"customer@example.com"}'
    ```
    3.2. Create Authorize and Reverse
    ```
    curl -k -X POST --user ${MERCHANT_AUTH} ${TASK_URL}/api/merchant/transaction \
    -H 'Content-Type: application/json' \
    -d '{"type":"Authorize","uuid":"aaaaaaaa-0000-0000-0000-000000000001","amount":10,"customerEmail":"customer@example.com"}'
    
    curl -k -X POST --user ${MERCHANT_AUTH} ${TASK_URL}/api/merchant/transaction \
    -H 'Content-Type: application/json' \
    -d '{"type":"Reversal","referenceId":"aaaaaaaa-0000-0000-0000-000000000001"}'
    ```
    
    3.3. Create Charge  
    3.3.1. Non-tangible Charge
    ```
    curl -k -X POST --user ${MERCHANT_AUTH} ${TASK_URL}/api/merchant/transaction \
    -H 'Content-Type: application/json' \
    -d '{"type":"Charge","amount":11,"customerEmail":"customer@example.com"}'    
    ```
    3.3.2. Tangible Charge  
    ```
    curl -k -X POST --user ${MERCHANT_AUTH} ${TASK_URL}/api/merchant/transaction \
    -H 'Content-Type: application/json' \
    -d '{"type":"Authorize","uuid":"cccccccc-0000-0000-0000-000000000001","amount":10,"customerEmail":"acme-customer@example.com","customerPhone":"+1 206 555 0100"}'

    curl -k -X POST --user ${MERCHANT_AUTH} ${TASK_URL}/api/merchant/transaction \
    -H 'Content-Type: application/json' \
    -d '{"type":"Charge","amount":10,"referenceId":"cccccccc-0000-0000-0000-000000000001"}'
    ```
    3.3.3. Tangible Charge with Refund  
    ```
    curl -k -X POST --user ${MERCHANT_AUTH} ${TASK_URL}/api/merchant/transaction \
    -H 'Content-Type: application/json' \
    -d '{"type":"Authorize","uuid":"cccccccc-0000-0000-0005-000000000001","amount":10,"customerEmail":"acme-customer@example.com","customerPhone":"+1 206 555 0100"}'

    curl -k -X POST --user ${MERCHANT_AUTH} ${TASK_URL}/api/merchant/transaction \
    -H 'Content-Type: application/json' \
    -d '{"type":"Charge","uuid":"cccccccc-0000-0000-0005-000000000002","amount":10,"referenceId":"cccccccc-0000-0000-0005-000000000001"}'
    
    curl -k -X POST --user ${MERCHANT_AUTH} ${TASK_URL}/api/merchant/transaction \
    -H 'Content-Type: application/json' \
    -d '{"type":"Refund","uuid":"cccccccc-0000-0000-0005-000000000003","amount":10,"referenceId":"cccccccc-0000-0000-0005-000000000002"}'
    ```
   