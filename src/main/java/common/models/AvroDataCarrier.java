package common.models;

import java.util.List;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericRecord;

public class AvroDataCarrier {
    private Schema schema;
    private List<GenericRecord> records;

    public AvroDataCarrier(List<GenericRecord> records) {
        this.records = records;

        if (records.size() > 0) {
            this.schema = records.get(0).getSchema();
        }
    }

    public Schema getSchema() {
        return schema;
    }

    public List<GenericRecord> getRecords() {
        return records;
    }
}
