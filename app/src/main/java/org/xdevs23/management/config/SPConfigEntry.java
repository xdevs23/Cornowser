package org.xdevs23.management.config;

import java.util.ArrayList;

public class SPConfigEntry {

    public ArrayList<String> keys, values;

    public SPConfigEntry(String rawString) {
        this();
        if ( rawString.isEmpty() || !rawString.contains(";;") ) return;

        String[] everything = rawString.split(";;");

        for ( int i = 0; i < everything.length; i += 2) {
            keys  .add(everything[i  ]);
            values.add(everything[i+1]);
        }
    }

    public SPConfigEntry() {
        createNew();
    }

    public SPConfigEntry createNew() {
        values = new ArrayList<>();
        keys   = new ArrayList<>();
        return this;
    }

    public String getValue(String key) {
        for ( int i = 0; i < keys.size(); i++ )
            if (keys.get(i).equals(key)) return values.get(i);
        return "";
    }

    public String getKeyForValue(String value) {
        for ( int i = 0; i < values.size(); i++ )
            if (values.get(i).equals(value)) return keys.get(i);
        return "";
    }

    public void putValue(String key, String value) {
        for ( int i = 0; i < keys.size(); i++ )
            if (keys.get(i).equals(key)) { values.set(i, value); return; }
        values  .add(value);
        keys    .add(key);
    }

    public void removeValue(String key) {
        for ( int i = 0; i < keys.size(); i++ )
            if (keys.get(i).equals(key)) { keys.remove(i); values.remove(i); }
    }

    @Override
    public String toString() {
        String[] everything = new String[keys.size()*2];

        // everything[0] = keys[0]
        // everything[1] = values[0]
        // everything[2] = keys[1]
        // everything[3] = values[1]
        for ( int i = 0; i < keys  .size(); i++ ) {
            everything[i * 2]     =   keys.get(i);
            everything[i * 2 + 1] = values.get(i);
        }

        StringBuilder finalRawString = new StringBuilder();

        for ( String s : everything )
            finalRawString.append(s).append(";;");

        return (finalRawString.toString().isEmpty() ? "" : finalRawString
                .substring(0, finalRawString.length() - 2)); // This is to remove the leading ;;
    }

}
