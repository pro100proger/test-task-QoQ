# Test Assignment

This assignment involves migrating the SQL procedure `PROC_PRE_AUTO_RCPT` into Java code using Spring Boot and MongoDB. A partial implementation already exists.

## Steps

1. **Examine the Procedure**  
   Open `ProcedureSQL.txt` to understand `PROC_PRE_AUTO_RCPT`.

2. **Review Table Structures**  
   See `TableStructures.txt` for a clear view of the database tables involved.

3. **Procedure Complexity**  
   Don't be discouraged by how large or complex the procedure looks. Use any resources you like (e.g., GPT) to understand how to convert its logic into Java.

4. **Create Missing Entities**  
   We have only defined some of the `@Document` entities. Identify the remaining tables used by the procedure and create corresponding entities.

5. **Create Repositories**  
   After defining entities, create MongoDB repositories following Spring Data MongoDB conventions.

6. **Complete the Java Logic**  
   In `ProcPreAutoRcptService.java`, fill in the missing parts so that it precisely replicates the procedure. Currently, only part of the logic is implemented.

7. **Build and Verify**  
   Run `mvn clean install` to confirm that your code compiles and you see BUILD SUCCESS. No tests are required, but your solution must logically match the original procedure.

8. **Running the Project**  
   If you want to run the Spring Boot application:
   1. From the project root, run `docker compose up` to start Mongo.
   2. Run the Spring application (e.g., `mvn spring-boot:run`).
   3. Confirm there are no errors at startup.

9. **One place with missing procedure**
   You will find in the code one procedure call, which code is not included into test. You should make a stub for it.

**Note**: You only need to ensure the Java code reflects the exact SQL logic. No additional testing is necessary.