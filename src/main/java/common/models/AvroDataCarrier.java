package common.models;

import java.util.List;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericRecord;

public class AvroDataCarrier {
    private Schema schema;
    private List<GenericRecord> records;

    public AvroDataCarrier(Schema schema, List<GenericRecord> records) {
        this.schema = schema;
        this.records = records;
    }

    public Schema getSchema() {
        return schema;
    }

    public List<GenericRecord> getRecords() {
        return records;
    }
}
