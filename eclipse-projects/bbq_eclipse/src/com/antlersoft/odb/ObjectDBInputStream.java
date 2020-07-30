package com.antlersoft.odb;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

public class ObjectDBInputStream extends ObjectInputStream {
    private DBHolder _holder;
    public ObjectDBInputStream(DBHolder holder, InputStream in) throws IOException {
        super(in);
        _holder = holder;
    }

    public ObjectDBInputStream(DBHolder holder) throws IOException {
        super();
        _holder = holder;
    }

    ObjectDB getObjectDB() {
        return _holder.getObjectDB();
    }

    public static class DBHolder
    {
        ObjectDB _db;
        public void setObjectDB(ObjectDB db)
        {
            _db = db;
        }

        ObjectDB getObjectDB()
        {
            return _db;
        }
    }
}
