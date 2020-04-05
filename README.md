## SQL for MongoDB test task

Translates SQL query to MongoDB shell command.

##### Query syntax:

    SELECT select_expression
    FROM table_name
    [ WHERE where_condition ]
    [ LIMIT row_count ]
    [ OFFSET row_count ]
    
    select_expression = { * | identifier [, identifier] ... }
    where_condition = expression [AND expression] ...
    expression = identifier operator value
    operator = { = | <> | < | > }
    
    value = { string literal | non-negative integer }
    identifier = string literal
    String literal may consist of latin letters, digits and underscores, or may be single-quoted string without escapes.

##### Arguments:

The only argument of program is a string representing SQL query to translate into MongoDB shell command.

If the argument is omitted, the program runs in interactive mode.

##### How to run program using gradle task:

    gradle jar
    java -jar ./build/libs/sql-for-mongodb-test-task-2020-1.0-SNAPSHOT.jar [query]