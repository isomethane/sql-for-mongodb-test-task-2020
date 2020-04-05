package ru.spb.hse.isomethane

import ru.spb.hse.isomethane.parser.MongoSQLBaseVisitor
import ru.spb.hse.isomethane.parser.MongoSQLParser
import java.lang.RuntimeException

class MongoSQLPrintVisitor : MongoSQLBaseVisitor<String>() {
    override fun visitStatement(ctx: MongoSQLParser.StatementContext): String {
        return ctx.selectStatement().accept(this)
    }

    override fun visitSelectStatement(ctx: MongoSQLParser.SelectStatementContext): String {
        val tableName = ctx.fromPart().accept(this)
        val query = ctx.wherePart()?.accept(this) ?: "{}"
        val projection = ctx.selectPart().accept(this)
        val projectionArgument = if (projection.isEmpty()) projection else ", $projection"
        val limit = ctx.limitPart()?.accept(this) ?: ""
        val offset = ctx.offsetPart()?.accept(this) ?: ""
        return "db.$tableName.find($query$projectionArgument)$limit$offset"
    }

    override fun visitSelectPart(ctx: MongoSQLParser.SelectPartContext): String {
        return ctx.ASTERISK()?.let { "" }
            ?: ctx.identifier().joinToString(prefix = "{", postfix = "}") { "${it.text}: true" }
    }

    override fun visitFromPart(ctx: MongoSQLParser.FromPartContext): String {
        return ctx.identifier().text
    }

    override fun visitWherePart(ctx: MongoSQLParser.WherePartContext): String {
        return ctx.expression().joinToString(prefix = "{", postfix = "}") { it.accept(this) }
    }

    override fun visitLimitPart(ctx: MongoSQLParser.LimitPartContext): String {
        return ".limit(${ctx.number().text})"
    }

    override fun visitOffsetPart(ctx: MongoSQLParser.OffsetPartContext): String {
        return ".skip(${ctx.number().text})"
    }

    override fun visitExpression(ctx: MongoSQLParser.ExpressionContext): String {
        val expression = ctx.operator().EQ()?.let { ctx.value().text }
            ?: "{\$${ctx.operator().accept(this)}: ${ctx.value().text}}"
        return "${ctx.identifier().text}: $expression"
    }

    override fun visitOperator(ctx: MongoSQLParser.OperatorContext): String {
        return ctx.EQ()?.let { "eq" }
            ?: ctx.NE()?.let { "ne" }
            ?: ctx.LT()?.let { "lt" }
            ?: ctx.GT()?.let { "gt" }
            ?: throw RuntimeException("Unknown operator")

    }
}