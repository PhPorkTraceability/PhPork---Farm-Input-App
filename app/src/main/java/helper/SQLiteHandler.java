/**
 * Author: Ravi Tamada
 * URL: www.androidhive.info
 * twitter: http://twitter.com/ravitamada
 * */

package helper;

/**
 * Created by marmagno on 10/12/2015.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

public class SQLiteHandler extends SQLiteOpenHelper {

    private static final String TAG = SQLiteHandler.class.getSimpleName();

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "phpork";

    // Table names
    //private static final String TABLE_USER = "farm_users";
    //private static final String TABLE_EMP = "employees";
    private static final String TABLE_LOC = "location";
    private static final String TABLE_HOUSE = "house";
    private static final String TABLE_PEN = "pen";
    private static final String TABLE_PIG = "pig";
    private static final String TABLE_RFID_TAGS = "rfid_tags";
    private static final String TABLE_TAG_PIG = "rfid_tag_pig";
    private static final String TABLE_WEIGHT = "weight_record";
    private static final String TABLE_FEEDS = "feeds";
    private static final String TABLE_FT = "feed_transaction";
    private static final String TABLE_MEDS = "medication";
    private static final String TABLE_MR = "med_record";
    private static final String TABLE_PB = "pig_breeds";
    private static final String TABLE_GROUP = "pig_group";

    // Table Employee Columns names
    /*
    private static final String KEY_EMPID = "employee_id";
    private static final String KEY_NAME = "employee_name";
    private static final String KEY_CONTACT = "contact_no";

    // Table Farm Users
    private static final String KEY_ID = "user_id";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_ACCOUNT = "accnt_type";
    */

    // Table Location
    private static final String KEY_LOC_ID = "loc_id";
    private static final String KEY_LOC_NAME = "loc_name";
    private static final String KEY_ADD = "address";

    // Table House
    private static final String KEY_HOUSEID = "house_id";
    private static final String KEY_HOUSENO = "house_no";
    private static final String KEY_HNAME = "house_name";
    private static final String KEY_FUNCTION = "function";

    // Table Pen
    private static final String KEY_PENID = "pen_id";
    private static final String KEY_PENNO = "pen_no";

    // Table Pig Breeds
    private static final String KEY_BREEDID = "breed_id";
    private static final String KEY_BREEDNAME = "breed_name";

    // Table Pig
    private static final String KEY_PIGID = "pig_id";
    private static final String KEY_BOARID = "boar_id";
    private static final String KEY_SOWID = "sow_id";
    private static final String KEY_GENDER = "gender";
    private static final String KEY_BIRTH = "birth_date";
    private static final String KEY_PIGSTAT = "pig_status";

    // Table Weight Record
    private static final String KEY_WRID = "record_id";
    private static final String KEY_RECDATE = "record_date";
    private static final String KEY_RECTIME = "record_time";
    private static final String KEY_WEIGHT = "weight";

    // Table RFID Tags
    private static final String KEY_TAGID = "tag_id";
    private static final String KEY_TAGRFID = "tag_rfid";
    private static final String KEY_TAGSTAT = "status";

    // Table RFID TAG PIG
    private static final String KEY_LABEL = "label";

    // Table Feeds
    private static final String KEY_FEEDID = "feed_id";
    private static final String KEY_FEEDNAME = "feed_name";
    private static final String KEY_FEEDTYPE = "feed_type";
    private static final String KEY_PRODDATE = "prod_date";

    // Table Feed Transaction
    private static final String KEY_FTID = "ft_id";
    private static final String KEY_QUANTITY = "quantity";
    private static final String KEY_DATEGIVEN = "date_given";
    private static final String KEY_TIMEGIVEN = "time_given";

    // Table Medication
    private static final String KEY_MEDID = "med_id";
    private static final String KEY_MEDNAME = "med_name";
    private static final String KEY_MEDTYPE = "med_type";

    // Table Med Record
    private static final String KEY_MRID = "mr_id";

    private static final String KEY_SYNCSTAT = "sync_status";

    public SQLiteHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        /*
        String CREATE_EMP_TABLE = "CREATE TABLE " + TABLE_EMP + "("
                + KEY_EMPID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_CONTACT + " TEXT," + KEY_ADD + " TEXT" + ")";
        db.execSQL(CREATE_EMP_TABLE);

        String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_USERNAME + " TEXT,"
                + KEY_PASSWORD + " TEXT UNIQUE," + KEY_ACCOUNT + " TEXT,"
                + KEY_EMPID + " INTEGER," + "FOREIGN KEY(" + KEY_EMPID + ") REFERENCES "
                + TABLE_EMP + "(" + KEY_EMPID + ")" + ")";
        db.execSQL(CREATE_USER_TABLE);
        */

        String CREATE_LOC_TABLE = "CREATE TABLE " + TABLE_LOC + "("
                + KEY_LOC_ID + " INTEGER PRIMARY KEY," + KEY_LOC_NAME + " TEXT,"
                + KEY_ADD + " TEXT" + ")";
        db.execSQL(CREATE_LOC_TABLE);

        String CREATE_HOUSE_TABLE = "CREATE TABLE " + TABLE_HOUSE + "("
                + KEY_HOUSEID + " INTEGER PRIMARY KEY," + KEY_HOUSENO + " TEXT,"
                + KEY_HNAME + " TEXT," + KEY_FUNCTION + " TEXT,"
                + KEY_LOC_ID + " INTEGER," + "FOREIGN KEY(" + KEY_LOC_ID + ") REFERENCES "
                + TABLE_LOC + "(" + KEY_LOC_ID + ")" + ")";
        db.execSQL(CREATE_HOUSE_TABLE);

        String CREATE_PEN_TABLE = "CREATE TABLE " + TABLE_PEN + "("
                + KEY_PENID + " INTEGER PRIMARY KEY," + KEY_PENNO + " TEXT,"
                + KEY_FUNCTION + " TEXT,"
                + KEY_HOUSEID + " INTEGER," + "FOREIGN KEY(" + KEY_HOUSEID + ") REFERENCES "
                + TABLE_HOUSE + "(" + KEY_HOUSEID + ")" + ")";
        db.execSQL(CREATE_PEN_TABLE);

        String CREATE_BREED_TABLE = "CREATE TABLE " + TABLE_PB + "("
                + KEY_BREEDID + " INTEGER PRIMARY KEY," + KEY_BREEDNAME + " TEXT" + ")";
        db.execSQL(CREATE_BREED_TABLE);

        String CREATE_PIG_TABLE = "CREATE TABLE " + TABLE_PIG + "("
                + KEY_PIGID + " INTEGER PRIMARY KEY," + KEY_BOARID + " INTEGER,"
                + KEY_SOWID + " INTEGER," + KEY_GENDER + " TEXT," + KEY_BIRTH + " DATE,"
                + KEY_PIGSTAT + " TEXT,"
                + KEY_PENID + " INTEGER,"
                + KEY_BREEDID + " INTEGER,"
                + "FOREIGN KEY(" + KEY_PENID + ") REFERENCES "
                + TABLE_PEN + "(" + KEY_PENID + "),"
                + "FOREIGN KEY(" + KEY_BREEDID + ") REFERENCES "
                + TABLE_PB + "(" + KEY_BREEDID + ")" + ")";
        db.execSQL(CREATE_PIG_TABLE);

        String CREATE_WEIGHT_TABLE = "CREATE TABLE " + TABLE_WEIGHT + "("
                + KEY_WRID + " INTEGER PRIMARY KEY," + KEY_RECDATE + " DATE,"
                + KEY_RECTIME + " TIME," + KEY_WEIGHT + " DECIMAL(10,2),"
                + KEY_PIGID + " INTEGER," + "FOREIGN KEY(" + KEY_PIGID + ") REFERENCES "
                + TABLE_PIG + "(" + KEY_PIGID + ")" + ")";
        db.execSQL(CREATE_WEIGHT_TABLE);

        String CREATE_RFIDTAGS_TABLE = "CREATE TABLE " + TABLE_RFID_TAGS + "("
                + KEY_TAGID + " INTEGER PRIMARY KEY," + KEY_TAGRFID + " TEXT,"
                + KEY_TAGSTAT + " TEXT"  + ")";
        db.execSQL(CREATE_RFIDTAGS_TABLE);

        String CREATE_RFIDTAGPIG_TABLE = "CREATE TABLE " + TABLE_TAG_PIG+ "("
                + KEY_TAGID+ " INTEGER PRIMARY KEY," + KEY_LABEL + " TEXT,"
                + KEY_PIGID + " INTEGER," + "FOREIGN KEY(" + KEY_PIGID + ") REFERENCES "
                + TABLE_PIG + "(" + KEY_PIGID + ")" + ")";
        db.execSQL(CREATE_RFIDTAGPIG_TABLE);

        String CREATE_FEEDS_TABLE = "CREATE TABLE " + TABLE_FEEDS + "("
                + KEY_FEEDID + " INTEGER PRIMARY KEY," + KEY_FEEDNAME + " TEXT,"
                + KEY_FEEDTYPE + " TEXT," + KEY_PRODDATE + " DATE" + ")";
        db.execSQL(CREATE_FEEDS_TABLE);

        String CREATE_FT_TABLE = "CREATE TABLE " + TABLE_FT + "("
                + KEY_FTID + " INTEGER PRIMARY KEY," + KEY_QUANTITY + " DECIMAL(10,2),"
                + KEY_DATEGIVEN + " DATE," + KEY_TIMEGIVEN + " TIME,"
                + KEY_PIGID + " INTEGER,"
                + KEY_FEEDID + " INTEGER,"
                + "FOREIGN KEY(" + KEY_PIGID + ") REFERENCES "
                + TABLE_PIG + "(" + KEY_PIGID + "),"
                + "FOREIGN KEY(" + KEY_FEEDID + ") REFERENCES "
                + TABLE_FEEDS + "(" + KEY_FEEDID + ")" + ")";

        db.execSQL(CREATE_FT_TABLE);

        String CREATE_MEDS_TABLE = "CREATE TABLE " + TABLE_MEDS + "("
                + KEY_MEDID + " INTEGER PRIMARY KEY," + KEY_MEDNAME + " TEXT,"
                + KEY_MEDTYPE + " TEXT" + ")";
        db.execSQL(CREATE_MEDS_TABLE);

        String CREATE_MEDREC_TABLE = "CREATE TABLE " + TABLE_MR + "("
                + KEY_MRID + " INTEGER PRIMARY KEY,"
                + KEY_DATEGIVEN + " DATE," + KEY_TIMEGIVEN + " TIME,"
                + KEY_PIGID + " INTEGER,"
                + KEY_MEDID + " INTEGER,"
                + "FOREIGN KEY(" + KEY_PIGID + ") REFERENCES "
                + TABLE_PIG + "(" + KEY_PIGID + "),"
                + "FOREIGN KEY(" + KEY_MEDID + ") REFERENCES "
                + TABLE_MEDS + "(" + KEY_MEDID + ")" + ")";
        db.execSQL(CREATE_MEDREC_TABLE);

        Log.d(TAG, "Database tables created");

    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        //db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        //db.execSQL("DROP TABLE IF EXISTS " + TABLE_EMP);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MR);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FEEDS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEDS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WEIGHT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RFID_TAGS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TAG_PIG);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PIG);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PB);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PEN);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HOUSE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOC);

        // Create tables again
        onCreate(db);
    }

    /**
     * Storing employee details in database
     */
    /*
    public void addEmployee(int employee_id, String employee_name, String contact_no, String address) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_EMPID, employee_id); // Name
        values.put(KEY_NAME, employee_name); // Name
        values.put(KEY_CONTACT, contact_no); // Contact
        values.put(KEY_ADD, address); // Address

        // Inserting Row
        long id = db.insert(TABLE_EMP, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New employee inserted into sqlite: " + id);
    }
    */
    /**
     * Storing user details in database
     */
    /*
    public void addFarmUser(String username, String password, String account, int employee_id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_USERNAME, username); // Username
        values.put(KEY_PASSWORD, password); // Password
        values.put(KEY_ACCOUNT, account); // Account
        values.put(KEY_EMPID, employee_id); // Employee_id

        // Inserting Row
        long id = db.insert(TABLE_USER, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New user inserted into sqlite: " + id);
    }
    */
    public void addLoc(String loc_id, String loc_name, String loc_add) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_LOC_ID, loc_id); // Username
        values.put(KEY_LOC_NAME, loc_name); // Password
        values.put(KEY_ADD, loc_add); // Account

        // Inserting Row
        long id = db.insert(TABLE_LOC, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New loc inserted into sqlite: " + id);
    }

    public void addHouse(String house_id, String house_no, String house_name,
                         String function, String loc_id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_HOUSEID, house_id);
        values.put(KEY_HOUSENO, house_no);
        values.put(KEY_HNAME, house_name);
        values.put(KEY_FUNCTION, function);
        values.put(KEY_LOC_ID, loc_id);

        // Inserting Row
        long id = db.insert(TABLE_HOUSE, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New house inserted into sqlite: " + id);
    }

    public void addPen(String pen_id, String pen_no, String function, String house_id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PENID, pen_id);
        values.put(KEY_PENNO, pen_no);
        values.put(KEY_FUNCTION, function);
        values.put(KEY_HOUSEID, house_id);

        // Inserting Row
        long id = db.insert(TABLE_PEN, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New pen inserted into sqlite: " + id);
    }

    public void addBreed(String breed_id, String breed_name) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_BREEDID, breed_id);
        values.put(KEY_BREEDNAME, breed_name);

        // Inserting Row
        long id = db.insert(TABLE_PB, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New pig breed inserted into sqlite: " + id);
    }

    public void addTags(String tag_id, String tag_rfid, String status){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TAGID, tag_id);
        values.put(KEY_TAGRFID, tag_rfid);
        values.put(KEY_TAGSTAT, status);

        // Inserting Row
        long id = db.insert(TABLE_RFID_TAGS, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New tag inserted into sqlite: " + id);
    }

    /**
     * Storing pig details in database
     */
    public void addPig(String pig_id, String boar_id, String gender, String birth_date,
                       String pig_status, String pen_id, String breed_id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PIGID, pig_id);
        values.put(KEY_BOARID, boar_id);
        //values.put(KEY_SOWID, sow_id);
        values.put(KEY_GENDER, gender);
        values.put(KEY_BIRTH, birth_date);
        values.put(KEY_PIGSTAT, pig_status);
        values.put(KEY_PENID, pen_id);
        values.put(KEY_BREEDID, breed_id);

        // Inserting Row
        long id = db.insert(TABLE_PIG, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New pig inserted into sqlite: " + id);
    }

    public void assignPigTag(String tag_id, String label, String pig_id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TAGID, tag_id);
        values.put(KEY_LABEL, label);
        values.put(KEY_PIGID, pig_id);

        // Inserting Row
        long id = db.insert(TABLE_TAG_PIG, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New assignment data inserted into sqlite: " + id);
    }

    public void addWeightRec(String record_id, String record_date, String record_time,
                         String weight, String pig_id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_WRID, record_id);
        values.put(KEY_RECDATE, record_date);
        values.put(KEY_RECTIME, record_time);
        values.put(KEY_WEIGHT, weight);
        values.put(KEY_PIGID, pig_id);

        // Inserting Row
        long id = db.insert(TABLE_WEIGHT, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New weight record inserted into sqlite: " + id);
    }

    public void addWeightRecByAuto(String weight, String pig_id, String date, String time) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_RECDATE, date);
        values.put(KEY_RECTIME, time);
        values.put(KEY_WEIGHT, weight);
        values.put(KEY_PIGID, pig_id);

        // Inserting Row
        long id = db.insert(TABLE_WEIGHT, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New weight record inserted into sqlite: " + id);
    }

    public void addFeeds(String feed_id, String feed_name, String feed_type, String prod_date) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_FEEDID, feed_id);
        values.put(KEY_FEEDNAME, feed_name);
        values.put(KEY_FEEDTYPE, feed_type);
        values.put(KEY_PRODDATE, prod_date);

        // Inserting Row
        long id = db.insert(TABLE_FEEDS, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New feed inserted into sqlite: " + id);
    }

    public void feedPigRec(String ft_id, String quantity, String date_given,
                         String time_given, String pig_id, String feed_id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_FTID, ft_id);
        values.put(KEY_QUANTITY, quantity);
        values.put(KEY_DATEGIVEN, date_given);
        values.put(KEY_TIMEGIVEN, time_given);
        values.put(KEY_PIGID, pig_id);
        values.put(KEY_FEEDID, feed_id);

        // Inserting Row
        long id = db.insert(TABLE_FT, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New pig feed record inserted into sqlite: " + id);
    }

    public void addMeds(String med_id, String med_name, String med_type) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_MEDID, med_id);
        values.put(KEY_MEDNAME, med_name);
        values.put(KEY_MEDTYPE, med_type);

        // Inserting Row
        long id = db.insert(TABLE_MEDS, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New feed inserted into sqlite: " + id);
    }

    public void addMedRec(String mr_id, String date_given, String time_given,
                          String pig_id, String med_id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_MRID, mr_id);
        values.put(KEY_DATEGIVEN, date_given);
        values.put(KEY_TIMEGIVEN, time_given);
        values.put(KEY_PIGID, pig_id);
        values.put(KEY_MEDID, med_id);

        // Inserting Row
        long id = db.insert(TABLE_MR, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New pig med record inserted into sqlite: " + id);
    }

    /**
     * Getting user data from database
     */
    /*
    public HashMap<String, String> getUserDetails(String username, String password, String account) {
        HashMap<String, String> user = new HashMap<String, String>();
        String selectQuery = "SELECT  * FROM " + TABLE_USER + " a INNER JOIN "
                + TABLE_EMP + " b ON(a.employee_id = b.employee_id) WHERE a.username = '"
                + username + "' AND a.password = '" + password + "' AND a.accnt_type = '"
                + account + "'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            user.put("employee_name", cursor.getString(1));
            user.put("contact_no", cursor.getString(2));
            user.put("address", cursor.getString(3));
            user.put("accnt_type", cursor.getString(4));
        }

        cursor.close();
        db.close();
        // return user
        Log.d(TAG, "Fetching user from Sqlite: " + user.toString());

        return user;
    }
    */

    public ArrayList<HashMap<String, String>> getPens(String location) {
        ArrayList<HashMap<String, String>> list = new ArrayList<>();


        String selectQuery = "SELECT a.pen_id, a.pen_no, a.function, a.house_id FROM "
                + TABLE_PEN + " a JOIN " + TABLE_HOUSE
                + " b ON(a.house_id = b.house_id) JOIN " + TABLE_LOC
                + " c ON(b.loc_id = c.loc_id) WHERE c.loc_name = '"
                + location + "'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        Log.d(TAG, String.valueOf(cursor.getCount()));
        // Move to first row
        cursor.moveToFirst();

        for(int i = 0; i < cursor.getCount();i++)
        {
            HashMap<String, String> result = new HashMap<String, String>();

            result.put(KEY_PENID, cursor.getString(0));
            result.put(KEY_PENNO, cursor.getString(1));
            result.put(KEY_FUNCTION, cursor.getString(2));
            result.put(KEY_HOUSEID, cursor.getString(3));
            cursor.moveToNext();
            list.add(result);

            Log.d(TAG, "Getting result: " + list.toString());

        }

        cursor.close();
        db.close();
        // return user
        Log.d(TAG, "Fetching pens from Sqlite: " + list.toString());

        return list;
    }

    public ArrayList<HashMap<String, String>> getInactiveTags() {
        ArrayList<HashMap<String, String>> list
                = new ArrayList<>();

        String selectQuery = "SELECT tag_id, tag_rfid, status FROM " + TABLE_RFID_TAGS
                + " WHERE status = 'Inactive'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        cursor.moveToFirst();

        for(int i = 0; i < cursor.getCount();i++) {
            HashMap<String, String> result = new HashMap<String, String>();

            result.put(KEY_TAGID, cursor.getString(0));
            result.put(KEY_TAGRFID, cursor.getString(1));
            result.put(KEY_TAGSTAT, cursor.getString(2));
            cursor.moveToNext();
            list.add(result);

            Log.d(TAG, "Getting result: " + list.toString());
        }

        cursor.close();
        db.close();
        // return user
        Log.d(TAG, "Fetching tags from Sqlite: " + list.toString());

        return list;
    }

    public ArrayList<HashMap<String, String>> getRFIDS() {
        ArrayList<HashMap<String, String>> list
                = new ArrayList<>();

        String selectQuery = "SELECT tag_id, tag_rfid FROM " + TABLE_RFID_TAGS;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        cursor.moveToFirst();

        for(int i = 0; i < cursor.getCount();i++) {
            HashMap<String, String> result = new HashMap<String, String>();

            result.put(KEY_TAGID, cursor.getString(0));
            result.put(KEY_TAGRFID, cursor.getString(1));
            cursor.moveToNext();
            list.add(result);

            Log.d(TAG, "Getting result: " + list.toString());
        }

        cursor.close();
        db.close();
        // return user
        Log.d(TAG, "Fetching tags from Sqlite: " + list.toString());

        return list;
    }

    public ArrayList<HashMap<String, String>> getBoars(){
        ArrayList<HashMap<String, String>> list
                = new ArrayList<>();

        String selectQuery = "SELECT a.pig_id, b.breed_name FROM "
                + TABLE_PIG + " a JOIN " + TABLE_PB +
                " b ON(a.breed_id = b.breed_id) WHERE a.gender = 'M'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        cursor.moveToFirst();

        for(int i = 0; i < cursor.getCount();i++) {
            HashMap<String, String> result = new HashMap<String, String>();

            result.put(KEY_PIGID, cursor.getString(0));
            result.put(KEY_BREEDNAME, cursor.getString(1));
            cursor.moveToNext();
            list.add(result);

            Log.d(TAG, "Getting result: " + list.toString());
        }

        cursor.close();
        db.close();
        // return user
        Log.d(TAG, "Fetching boars from Sqlite: " + list.toString());

        return list;
    }

    public ArrayList<HashMap<String, String>> getSows(){
        ArrayList<HashMap<String, String>> list
                = new ArrayList<>();

        String selectQuery = "SELECT a.pig_id, b.breed_name FROM "
                + TABLE_PIG + " a JOIN " + TABLE_PB +
                " b ON(a.breed_id = b.breed_id) WHERE a.gender = 'F'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        cursor.moveToFirst();

        for(int i = 0; i < cursor.getCount();i++) {
            HashMap<String, String> result = new HashMap<String, String>();

            result.put(KEY_PIGID, cursor.getString(0));
            result.put(KEY_BREEDNAME, cursor.getString(1));
            cursor.moveToNext();
            list.add(result);

            Log.d(TAG, "Getting result: " + list.toString());
        }

        cursor.close();
        db.close();
        // return user
        Log.d(TAG, "Fetching sows from Sqlite: " + list.toString());

        return list;
    }

    public int getMaxPigID(){
        int id = 0;

        String selectQuery = "SELECT MAX(pig_id) FROM " + TABLE_PIG;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            String res = cursor.getString(0);
            if(res != null){
                id = Integer.parseInt(res);
            }
        }

        cursor.close();
        db.close();

        return id;
    }

    public int getMaxTagID(){
        int id = 0;

        String selectQuery = "SELECT MAX(tag_id) FROM " + TABLE_RFID_TAGS;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            String res = cursor.getString(0);
            if(res != null){
                id = Integer.parseInt(res);
            }
        }

        cursor.close();
        db.close();

        return id;
    }
    /*
    public boolean isUserExists(String username, String password, String account)
    {
        Boolean isExists;
        String selectQuery = "SELECT  * FROM " + TABLE_USER + " a INNER JOIN "
                + TABLE_EMP + " b ON(a.employee_id = b.employee_id) WHERE a.username = '"
                + username + "' AND a.password = '" + password + "' AND a.accnt_type = '"
                + account + "'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0)
            isExists = true;
        else
            isExists = false;
        cursor.close();
        db.close();

        return isExists;
    }
    */
    public boolean isPigExists(String pig_id)
    {
        Boolean isExists;
        String selectQuery = "SELECT * FROM " + TABLE_PIG + " WHERE pig_id = '" + pig_id + "'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0)
            isExists = true;
        else
            isExists = false;
        cursor.close();
        db.close();

        return isExists;
    }

    public void updateTag(String tag_id, String status)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE rfid_tags " +
                "SET status = '" + status + "' " +
                "WHERE tag_id = '" + tag_id + "'";
        db.execSQL(query);
        db.close();

        Log.d(TAG, "Updated RFID tag");
    }


    /**
     * Re crate database Delete all tables and create them again
     */
    public void deleteUsers() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        //db.delete(TABLE_USER, null, null);
        db.close();

        Log.d(TAG, "Deleted all user info from sqlite");
    }
}