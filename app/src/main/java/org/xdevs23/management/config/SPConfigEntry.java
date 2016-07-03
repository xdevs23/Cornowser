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

    public void putValue(String key, String value, boolean atBeginning, boolean overwrite) {
        if(!overwrite)
            for (int i = 0; i < keys.size(); i++)
                if (keys.get(i).equals(key)) { values.set(i, value); return; }
        if(!atBeginning) {
            values.add(value);
            keys  .add(key);
        } else {
            values.add(0, value);
            keys  .add(0, key);
        }
    }

    public void putValue(String key, String value) {
        putValue(key, value, false, true);
    }

    public void putValue(String key, String value, boolean atBeginning) {
        putValue(key, value, atBeginning, true);
    }

    public void removeValue(String key) {
        for ( int i = 0; i < keys.size(); i++ )
            if (keys.get(i).equals(key)) { keys.remove(i); values.remove(i); }
    }

    public void rotate() {
        String[] tmpKeys   =   keys.toArray(new String[  keys.size()]);
        String[] tmpValues = values.toArray(new String[values.size()]);

        createNew();

        for ( int i = tmpKeys.length - 1; i >= 0; i-- ) {
            keys  .add(tmpKeys  [i]);
            values.add(tmpValues[i]);
        }
    }

    public SPConfigEntry getRotated() {
        String[] tmpKeys   =   keys.toArray(new String[  keys.size()]);
        String[] tmpValues = values.toArray(new String[values.size()]);

        SPConfigEntry newConfig = new SPConfigEntry();

        for ( int i = tmpKeys.length - 1; i >= 0; i-- ) {
            newConfig.  keys.add(tmpKeys  [i]);
            newConfig.values.add(tmpValues[i]);
        }

        return newConfig;
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
