package com.redhat.insights.expandjsonsmt;

import org.apache.kafka.connect.data.Schema;
import org.bson.BsonDocument;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SchemaParserTest {

    @Test
    public void simple() {
        BsonDocument bson = BsonDocument.parse("{\"name\":\"Josef\",\"age\":31}");
        Schema schema = SchemaParser.bsonDocument2Schema(new Key(), bson);
        assertEquals(2, schema.fields().size());
        assertEquals(Schema.OPTIONAL_STRING_SCHEMA, schema.field("name").schema());
        assertEquals(Schema.OPTIONAL_INT32_SCHEMA, schema.field("age").schema());
    }

    @Test
    public void complex() {
        BsonDocument bson = BsonDocument.parse("{\"name\":{\"first\":\"Josef\",\"last\":\"Hak\"},\"age\":31}");
        Schema schema = SchemaParser.bsonDocument2Schema(new Key(), bson);
        assertEquals(2, schema.fields().size());
        assertEquals(2, schema.field("name").schema().fields().size());
        assertEquals("Name", schema.field("name").schema().name());
        assertEquals(Schema.OPTIONAL_STRING_SCHEMA, schema.field("name").schema().field("first").schema());
        assertEquals(Schema.OPTIONAL_STRING_SCHEMA, schema.field("name").schema().field("last").schema());
        assertEquals(Schema.OPTIONAL_INT32_SCHEMA, schema.field("age").schema());
    }

    @Test
    public void withArrayOfStrings() {
        BsonDocument bson = BsonDocument.parse("{\"arr\":[\"\"]}");
        Schema schema = SchemaParser.bsonDocument2Schema(new Key(), bson);
        assertEquals(1, schema.fields().size());
        assertEquals(Schema.OPTIONAL_STRING_SCHEMA, schema.field("arr").schema()
                .valueSchema());
    }

    @Test
    public void withArrayOfIntegers() {
        BsonDocument bson = BsonDocument.parse("{\"arr\":[0]}");
        Schema schema = SchemaParser.bsonDocument2Schema(new Key(), bson);
        assertEquals(1, schema.fields().size());
        assertEquals(Schema.OPTIONAL_INT32_SCHEMA, schema.field("arr").schema()
                .valueSchema());
    }

    @Test
    public void withArrayOfObjects() {
        BsonDocument bson = BsonDocument.parse("{\"arr\":[{\"a\":0}]}");
        Schema schema = SchemaParser.bsonDocument2Schema(new Key(), bson);
        assertEquals(1, schema.fields().size());
        assertEquals(Schema.OPTIONAL_INT32_SCHEMA, schema.field("arr").schema()
                .valueSchema().field("a").schema());
    }

    @Test
    public void withEmptyKey() {
        BsonDocument bson = BsonDocument.parse("{\"\": \"empty key\", \"not-empty\": \"not-empty\"}");
        Schema schema = SchemaParser.bsonDocument2Schema(new Key(), bson);
        assertEquals(1, schema.fields().size());
        assertEquals(Schema.OPTIONAL_STRING_SCHEMA, schema.field("not-empty").schema());
    }

    @Test
    public void complexStructNames() {
        BsonDocument bson = BsonDocument.parse("{\"name\":{\"first\":\"Josef\",\"last\":\"Hak\"},\"mother\":{\"name\":{\"first\":\"Catherine\",\"last\":\"Hak\"}},\"age\":59}");
        Schema schema = SchemaParser.bsonDocument2Schema(new Key(), bson);
        assertEquals("Name", schema.field("name").schema().name());
        assertEquals("Mother", schema.field("mother").schema().name());
        assertEquals("MotherName", schema.field("mother").schema().field("name").schema().name());
    }
}
