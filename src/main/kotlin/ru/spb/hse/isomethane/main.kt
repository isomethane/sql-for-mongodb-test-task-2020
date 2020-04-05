package ru.spb.hse.isomethane

import org.antlr.v4.runtime.BailErrorStrategy
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.misc.ParseCancellationException
import ru.spb.hse.isomethane.parser.MongoSQLLexer
import ru.spb.hse.isomethane.parser.MongoSQLParser
import java.lang.Exception
import java.util.*

class MongoSQLParseException(cause: Throwable? = null) : Exception(cause)

fun translateFromSQL(query: String): String {
    try {
        val lexer = MongoSQLLexer(CharStreams.fromString(query))
        val parser = MongoSQLParser(CommonTokenStream(lexer))
        parser.errorHandler = BailErrorStrategy()
        return parser.statement().accept(MongoSQLPrintVisitor())
    } catch (e: ParseCancellationException) {
        throw MongoSQLParseException(e.cause)
    }
}

private fun processOneQuery(query: String) {
    try {
        println(translateFromSQL(query))
    } catch (e: Exception) {
        println(Messages.INCORRECT_QUERY_MESSAGE)
        println(Messages.QUERY_SYNTAX)
    }
}

private fun interactiveLoop() {
    val scanner = Scanner(System.`in`)
    var toExit = false
    println(Messages.QUERY_SYNTAX)
    println()
    println(Messages.INTERACTIVE_MODE_MESSAGE)
    while (!toExit) {
        print("> ")
        val query = scanner.nextLine()
        if (query.trim().equals("exit", ignoreCase = true)) {
            toExit = true
        } else {
            processOneQuery(query)
        }
    }
}

fun main(arguments: Array<String>) {
    when (arguments.size) {
        0 -> interactiveLoop()
        1 -> processOneQuery(arguments[0])
        2 -> println(Messages.INCORRECT_NUMBER_OF_ARGUMENTS)
    }
}

private object Messages {
    val QUERY_SYNTAX = """
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
        String literal may consist of latin letters, digits and underscores,
        or may be single-quoted string without escapes. 
    """.trimIndent()

    val INCORRECT_NUMBER_OF_ARGUMENTS = """
        Incorrect number of arguments.
        Pass query as the only argument or run without arguments to enter interactive mode.
    """.trimIndent()

    val INTERACTIVE_MODE_MESSAGE = """
        Input commands as single lines. Print EXIT to quit interactive mode.
    """.trimIndent()

    const val INCORRECT_QUERY_MESSAGE = "Incorrect query!"
}