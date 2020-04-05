## SQL for MongoDB test task

Translates SQL query to MongoDB shell command.

Query syntax:

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