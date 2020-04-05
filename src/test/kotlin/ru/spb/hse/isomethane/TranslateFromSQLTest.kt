package ru.spb.hse.isomethane

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class TranslateFromSQLTest {
    @Test
    fun testSelectAll() {
        val query = "SELECT * FROM collection"
        val expected = "db.collection.find({})"
        val actual = translateFromSQL(query)
        assertEquals(expected, actual)
    }

    @Test
    fun testSelectOneColumn() {
        val query = "SELECT firstColumn FROM collection"
        val expected = "db.collection.find({}, {firstColumn: true})"
        val actual = translateFromSQL(query)
        assertEquals(expected, actual)
    }

    @Test
    fun testSelectMultipleColumns() {
        val query = "SELECT firstColumn, secondColumn, thirdColumn FROM collection"
        val expected = "db.collection.find({}, {firstColumn: true, secondColumn: true, thirdColumn: true})"
        val actual = translateFromSQL(query)
        assertEquals(expected, actual)
    }

    @Test
    fun testFromMultipleTables() {
        val query = "SELECT * FROM collection1, collection2"
        assertThrows(MongoSQLParseException::class.java) { translateFromSQL(query) }
    }

    @Test
    fun testFromIncorrectTableName() {
        val query = "SELECT * FROM 1collection"
        assertThrows(MongoSQLParseException::class.java) { translateFromSQL(query) }
    }

    @Test
    fun testSelectIdentifiers() {
        val query = "SELECT firstColumn, 'second column', _thirdColumn3 FROM collection"
        val expected = "db.collection.find({}, {firstColumn: true, 'second column': true, _thirdColumn3: true})"
        val actual = translateFromSQL(query)
        assertEquals(expected, actual)
    }

    @Test
    fun testOffset() {
        val query = "SELECT * FROM collection OFFSET 30"
        val expected = "db.collection.find({}).skip(30)"
        val actual = translateFromSQL(query)
        assertEquals(expected, actual)
    }

    @Test
    fun testOffsetIncorrectValue() {
        val query = "SELECT * FROM collection OFFSET a"
        assertThrows(MongoSQLParseException::class.java) { translateFromSQL(query) }
    }

    @Test
    fun testLimit() {
        val query = "SELECT * FROM collection LIMIT 50"
        val expected = "db.collection.find({}).limit(50)"
        val actual = translateFromSQL(query)
        assertEquals(expected, actual)
    }

    @Test
    fun testLimitIncorrectValue() {
        val query = "SELECT * FROM collection LIMIT a"
        assertThrows(MongoSQLParseException::class.java) { translateFromSQL(query) }
    }

    @Test
    fun testLimitAndOffset() {
        val query = "SELECT * FROM collection LIMIT 4 OFFSET 3"
        val expected = "db.collection.find({}).limit(4).skip(3)"
        val actual = translateFromSQL(query)
        assertEquals(expected, actual)
    }

    @Test
    fun testLimitAndOffsetReverse() {
        val query = "SELECT * FROM collection OFFSET 3 LIMIT 4"
        assertThrows(MongoSQLParseException::class.java) { translateFromSQL(query) }
    }

    @Test
    fun testWhereEqualsCondition() {
        val query = "SELECT * FROM students WHERE name = 'Vasya'"
        val expected = "db.students.find({name: 'Vasya'})"
        val actual = translateFromSQL(query)
        assertEquals(expected, actual)
    }

    @Test
    fun testWhereNotEqualCondition() {
        val query = "SELECT * FROM students WHERE age <> 20"
        val expected = "db.students.find({age: {\$ne: 20}})"
        val actual = translateFromSQL(query)
        assertEquals(expected, actual)
    }

    @Test
    fun testWhereLessThanCondition() {
        val query = "SELECT * FROM students WHERE age < 20"
        val expected = "db.students.find({age: {\$lt: 20}})"
        val actual = translateFromSQL(query)
        assertEquals(expected, actual)
    }

    @Test
    fun testWhereGreaterThanCondition() {
        val query = "SELECT * FROM students WHERE age > 20"
        val expected = "db.students.find({age: {\$gt: 20}})"
        val actual = translateFromSQL(query)
        assertEquals(expected, actual)
    }

    @Test
    fun testWhereMultipleConditions() {
        val query = "SELECT * FROM students WHERE name <> Petya AND surname = 'Ivanov' AND age > 17"
        val expected = "db.students.find({name: {\$ne: Petya}, surname: 'Ivanov', age: {\$gt: 17}})"
        val actual = translateFromSQL(query)
        assertEquals(expected, actual)
    }

    @Test
    fun testAllParts() {
        val query = "SELECT a, b FROM collection WHERE c = d AND e > 9 LIMIT 10 OFFSET 2"
        val expected = "db.collection.find({c: d, e: {\$gt: 9}}, {a: true, b: true}).limit(10).skip(2)"
        val actual = translateFromSQL(query)
        assertEquals(expected, actual)
    }

    @Test
    fun testCaseInsensitivity() {
        val query = "Select a, b From collection Where c = d And e > 9 Limit 10 Offset 2"
        val expected = "db.collection.find({c: d, e: {\$gt: 9}}, {a: true, b: true}).limit(10).skip(2)"
        val actual = translateFromSQL(query)
        assertEquals(expected, actual)
    }
}