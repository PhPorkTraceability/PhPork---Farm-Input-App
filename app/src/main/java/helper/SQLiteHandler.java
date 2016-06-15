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
    private static final String TABLE_GROUP = "pig_groups";
    private static final String TABLE_PIG = "pig";
    private static final String TABLE_RFID_TAGS = "rfid_tags";
    private static final String TABLE_WEIGHT = "weight_record";
    private static final String TABLE_FEEDS = "feeds";
    private static final String TABLE_FT = "feed_transaction";
    private static final String TABLE_MEDS = "medication";
    private static final String TABLE_MR = "med_record";
    private static final String TABLE_PB = "pig_breeds";

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
    private static final String KEY_LOCID = "loc_id";
    private static final String KEY_LOCNAME = "loc_name";
    private static final String KEY_ADD = "address";

    // Table House
    private static final String KEY_HOUSEID = "house_id";
    private static final String KEY_HOUSENO = "house_no";
    private static final String KEY_HNAME = "house_name";
    private static final String KEY_FUNCTION = "function";

    // Table Pen
    private static final String KEY_PENID = "pen_id";
    private static final String KEY_PENNO = "pen_no";

    // Table Pig Groups
    private static final String KEY_GNAME = "group_name";

    // Table Pig Breeds
    private static final String KEY_BREEDID = "breed_id";
    private static final String KEY_BREEDNAME = "breed_name";

    // Table Pig
    private static final String KEY_PIGID = "pig_id";
    private static final String KEY_BOARID = "boar_id";
    private static final String KEY_SOWID = "sow_id";
    private static final String KEY_FOSTER = "foster_sow";
    private static final String KEY_WEEKF = "week_farrowed";
    private static final String KEY_GENDER = "gender";
    private static final String KEY_FDATE = "farrowing_date";
    private static final String KEY_PIGSTAT = "pig_status";

    // Table Weight Record
    private static final String KEY_WRID = "record_id";
    private static final String KEY_RECDATE = "record_date";
    private static final String KEY_RECTIME = "record_time";
    private static final String KEY_WEIGHT = "weight";
    private static final String KEY_REMARKS = "remarks";

    // Table RFID Tags
    private static final String KEY_TAGID = "tag_id";
    private static final String KEY_TAGRFID = "tag_rfid";
    private static final String KEY_LABEL = "label";
    private static final String KEY_TAGSTAT = "status";

    // Table Feeds
    private static final String KEY_FEEDID = "feed_id";
    private static final String KEY_FEEDNAME = "feed_name";
    private static final String KEY_FEEDTYPE = "feed_type";

    // Table Feed Transaction
    private static final String KEY_FTID = "ft_id";
    private static final String KEY_QUANTITY = "quantity";
    private static final String KEY_UNIT = "unit";
    private static final String KEY_DATEGIVEN = "date_given";
    private static final String KEY_TIMEGIVEN = "time_given";
    private static final String KEY_PRODDATE = "prod_date";

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
                + KEY_LOCID + " INTEGER PRIMARY KEY,"
                + KEY_LOCNAME + " TEXT,"
                + KEY_ADD + " TEXT" + ")";
        db.execSQL(CREATE_LOC_TABLE);

        String CREATE_HOUSE_TABLE = "CREATE TABLE " + TABLE_HOUSE + "("
                + KEY_HOUSEID + " INTEGER PRIMARY KEY,"
                + KEY_HOUSENO + " TEXT,"
                + KEY_HNAME + " TEXT,"
                + KEY_FUNCTION + " TEXT,"
                + KEY_LOCID + " INTEGER,"
                + "FOREIGN KEY(" + KEY_LOCID + ") REFERENCES "
                + TABLE_LOC + "(" + KEY_LOCID + ")" + ")";
        db.execSQL(CREATE_HOUSE_TABLE);

        String CREATE_PEN_TABLE = "CREATE TABLE " + TABLE_PEN + "("
                + KEY_PENID + " INTEGER PRIMARY KEY,"
                + KEY_PENNO + " TEXT,"
                + KEY_FUNCTION + " TEXT,"
                + KEY_HOUSEID + " INTEGER,"
                + "FOREIGN KEY(" + KEY_HOUSEID + ") REFERENCES "
                + TABLE_HOUSE + "(" + KEY_HOUSEID + ")" + ")";
        db.execSQL(CREATE_PEN_TABLE);

        String CREATE_GROUP_TABLE = "CREATE TABLE " + TABLE_GROUP + "("
                + KEY_GNAME + " TEXT PRIMARY KEY,"
                + KEY_PENID + " INTEGER,"
                + "FOREIGN KEY(" + KEY_PENID + ") REFERENCES "
                + TABLE_PEN + "(" + KEY_PENID + ")" + ")";
        db.execSQL(CREATE_GROUP_TABLE);

        String CREATE_BREED_TABLE = "CREATE TABLE " + TABLE_PB + "("
                + KEY_BREEDID + " INTEGER PRIMARY KEY,"
                + KEY_BREEDNAME + " TEXT" + ")";
        db.execSQL(CREATE_BREED_TABLE);

        String CREATE_PIG_TABLE = "CREATE TABLE " + TABLE_PIG + "("
                + KEY_PIGID + " INTEGER PRIMARY KEY,"
                + KEY_BOARID + " TEXT,"
                + KEY_SOWID + " TEXT,"
                + KEY_FOSTER + " TEXT,"
                + KEY_WEEKF + " TEXT,"
                + KEY_GENDER + " TEXT,"
                + KEY_FDATE + " DATE,"
                + KEY_PIGSTAT + " TEXT,"
                + KEY_PENID + " INTEGER,"
                + KEY_BREEDID + " INTEGER,"
                + KEY_GNAME + " TEXT,"
                + KEY_SYNCSTAT + " TEXT,"
                /*
                + "FOREIGN KEY(" + KEY_BOARID + ") REFERENCES "
                + TABLE_PIG + "(" + KEY_PIGID + "),"
                + "FOREIGN KEY(" + KEY_SOWID + ") REFERENCES "
                + TABLE_PIG + "(" + KEY_PIGID + "),"
                + "FOREIGN KEY(" + KEY_FOSTER + ") REFERENCES "
                + TABLE_PIG + "(" + KEY_PIGID + "),"
                */
                + "FOREIGN KEY(" + KEY_GNAME + ") REFERENCES "
                + TABLE_GROUP + "(" + KEY_GNAME + "),"
                + "FOREIGN KEY(" + KEY_PENID + ") REFERENCES "
                + TABLE_PEN + "(" + KEY_PENID + "),"
                + "FOREIGN KEY(" + KEY_BREEDID + ") REFERENCES "
                + TABLE_PB + "(" + KEY_BREEDID + ")" + ")";
        db.execSQL(CREATE_PIG_TABLE);

        String CREATE_WEIGHT_TABLE = "CREATE TABLE " + TABLE_WEIGHT + "("
                + KEY_WRID + " INTEGER PRIMARY KEY,"
                + KEY_RECDATE + " DATE,"
                + KEY_RECTIME + " TIME,"
                + KEY_WEIGHT + " DECIMAL(10,2),"
                + KEY_PIGID + " INTEGER,"
                + KEY_REMARKS + " TEXT,"
                + KEY_SYNCSTAT + " TEXT,"
                + " FOREIGN KEY(" + KEY_PIGID + ") REFERENCES "
                + TABLE_PIG + "(" + KEY_PIGID + ")" + ")";
        db.execSQL(CREATE_WEIGHT_TABLE);

        String CREATE_RFIDTAGS_TABLE = "CREATE TABLE " + TABLE_RFID_TAGS + "("
                + KEY_TAGID + " INTEGER PRIMARY KEY,"
                + KEY_TAGRFID + " TEXT,"
                + KEY_PIGID + " INTEGER,"
                + KEY_LABEL + " TEXT,"
                + KEY_TAGSTAT + " TEXT,"
                + " FOREIGN KEY(" + KEY_PIGID + ") REFERENCES "
                + TABLE_PIG + "(" + KEY_PIGID + ")" + ")";
        db.execSQL(CREATE_RFIDTAGS_TABLE);

        String CREATE_FEEDS_TABLE = "CREATE TABLE " + TABLE_FEEDS + "("
                + KEY_FEEDID + " INTEGER PRIMARY KEY,"
                + KEY_FEEDNAME + " TEXT,"
                + KEY_FEEDTYPE + " TEXT" + ")";
                //+ KEY_PRODDATE + " DATE" + ")";
        db.execSQL(CREATE_FEEDS_TABLE);

        String CREATE_FT_TABLE = "CREATE TABLE " + TABLE_FT + "("
                + KEY_FTID + " INTEGER PRIMARY KEY,"
                + KEY_QUANTITY + " DECIMAL(10,2),"
                + KEY_UNIT + " TEXT,"
                + KEY_DATEGIVEN + " DATE,"
                + KEY_TIMEGIVEN + " TIME,"
                + KEY_PIGID + " INTEGER,"
                + KEY_FEEDID + " INTEGER,"
                + KEY_PRODDATE + " DATE,"
                + KEY_SYNCSTAT + " TEXT,"
                + "FOREIGN KEY(" + KEY_PIGID + ") REFERENCES "
                + TABLE_PIG + "(" + KEY_PIGID + "),"
                + "FOREIGN KEY(" + KEY_FEEDID + ") REFERENCES "
                + TABLE_FEEDS + "(" + KEY_FEEDID + ")" + ")";

        db.execSQL(CREATE_FT_TABLE);

        String CREATE_MEDS_TABLE = "CREATE TABLE " + TABLE_MEDS + "("
                + KEY_MEDID + " INTEGER PRIMARY KEY,"
                + KEY_MEDNAME + " TEXT,"
                + KEY_MEDTYPE + " TEXT" + ")";
        db.execSQL(CREATE_MEDS_TABLE);

        String CREATE_MEDREC_TABLE = "CREATE TABLE " + TABLE_MR + "("
                + KEY_MRID + " INTEGER PRIMARY KEY,"
                + KEY_DATEGIVEN + " DATE,"
                + KEY_TIMEGIVEN + " TIME,"
                + KEY_QUANTITY + " DECIMAL(10,2),"
                + KEY_UNIT + " TEXT,"
                + KEY_PIGID + " INTEGER,"
                + KEY_MEDID + " INTEGER,"
                + KEY_SYNCSTAT + " TEXT,"
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
        values.put(KEY_LOCID, loc_id);
        values.put(KEY_LOCNAME, loc_name);
        values.put(KEY_ADD, loc_add);

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
        values.put(KEY_LOCID, loc_id);

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

    public void addGroup(String group_name, String pen_id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_GNAME, group_name);
        values.put(KEY_PENID, pen_id);

        // Inserting Row
        long id = db.insert(TABLE_GROUP, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New pig group inserted into sqlite: " + id);
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

    public void addTags(String tag_id, String tag_rfid, String pig_id,
                        String label,String status){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TAGID, tag_id);
        values.put(KEY_TAGRFID, tag_rfid);
        values.put(KEY_PIGID, pig_id);
        values.put(KEY_LABEL, label);
        values.put(KEY_TAGSTAT, status);

        // Inserting Row
        long id = db.insert(TABLE_RFID_TAGS, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New tag inserted into sqlite: " + id);
    }

    /**
     * Storing pig details in database
     */
    public void addPig(String pig_id, String boar_id, String sow_id, String foster_sow,
                       String week_farrowed, String gender, String farrowing_date, String pig_status,
                       String pen_id, String breed_id, String group_name, String sync_status) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PIGID, pig_id);
        values.put(KEY_BOARID, boar_id);
        values.put(KEY_SOWID, sow_id);
        values.put(KEY_FOSTER, foster_sow);
        values.put(KEY_WEEKF, week_farrowed);
        values.put(KEY_GENDER, gender);
        values.put(KEY_FDATE, farrowing_date);
        values.put(KEY_PIGSTAT, pig_status);
        values.put(KEY_PENID, pen_id);
        values.put(KEY_BREEDID, breed_id);
        values.put(KEY_GNAME, group_name);
        values.put(KEY_SYNCSTAT, sync_status);

        // Inserting Row
        long id = db.insert(TABLE_PIG, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New pig inserted into sqlite: " + id);
    }

    public void addWeightRec(String record_id, String record_date, String record_time,
                         String weight, String pig_id, String remarks, String sync_status) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_WRID, record_id);
        values.put(KEY_RECDATE, record_date);
        values.put(KEY_RECTIME, record_time);
        values.put(KEY_WEIGHT, weight);
        values.put(KEY_PIGID, pig_id);
        values.put(KEY_REMARKS, remarks);
        values.put(KEY_SYNCSTAT, sync_status);

        // Inserting Row
        long id = db.insert(TABLE_WEIGHT, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New weight record inserted into sqlite: " + id);
    }

    public void addWeightRecByAuto(String weight, String pig_id, String date,
                                   String time, String remarks, String sync_status) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_RECDATE, date);
        values.put(KEY_RECTIME, time);
        values.put(KEY_WEIGHT, weight);
        values.put(KEY_PIGID, pig_id);
        values.put(KEY_REMARKS, remarks);
        values.put(KEY_SYNCSTAT, sync_status);

        // Inserting Row
        long id = db.insert(TABLE_WEIGHT, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New weight record inserted into sqlite: " + id);
    }

    public void addFeeds(String feed_id, String feed_name, String feed_type) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_FEEDID, feed_id);
        values.put(KEY_FEEDNAME, feed_name);
        values.put(KEY_FEEDTYPE, feed_type);
        //values.put(KEY_PRODDATE, prod_date);

        // Inserting Row
        long id = db.insert(TABLE_FEEDS, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New feed inserted into sqlite: " + id);
    }

    public void feedPigRec(String ft_id, String quantity, String unit, String date_given,
                           String time_given, String pig_id, String feed_id,
                           String prod_date, String sync_status) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_FTID, ft_id);
        values.put(KEY_QUANTITY, quantity);
        values.put(KEY_UNIT, unit);
        values.put(KEY_DATEGIVEN, date_given);
        values.put(KEY_TIMEGIVEN, time_given);
        values.put(KEY_PIGID, pig_id);
        values.put(KEY_FEEDID, feed_id);
        values.put(KEY_PRODDATE, prod_date);
        values.put(KEY_SYNCSTAT, sync_status);

        // Inserting Row
        long id = db.insert(TABLE_FT, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New pig feed record inserted into sqlite: " + id);
    }

    public void feedPigRecAuto(String quantity, String unit, String date_given, String time_given,
                               String pig_id, String feed_id, String prod_date, String sync_status) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_QUANTITY, quantity);
        values.put(KEY_UNIT, unit);
        values.put(KEY_DATEGIVEN, date_given);
        values.put(KEY_TIMEGIVEN, time_given);
        values.put(KEY_PIGID, pig_id);
        values.put(KEY_FEEDID, feed_id);
        values.put(KEY_PRODDATE, prod_date);
        values.put(KEY_SYNCSTAT, sync_status);

        // Inserting Row
        long id = db.insert(TABLE_FT, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New pig feed record inserted into sqlite: " + id);
    }

    public void feedPigRecByGroup(String quantity, String unit, String date_given,
                                  String time_given, String[] pig_ids, String feed_id,
                                  String prod_date, String sync_status) {
        SQLiteDatabase db = this.getWritableDatabase();

        for(int i = 0;i < pig_ids.length;i++) {
            ContentValues values = new ContentValues();
            values.put(KEY_QUANTITY, quantity);
            values.put(KEY_UNIT, unit);
            values.put(KEY_DATEGIVEN, date_given);
            values.put(KEY_TIMEGIVEN, time_given);
            values.put(KEY_PIGID, pig_ids[i]);
            values.put(KEY_FEEDID, feed_id);
            values.put(KEY_PRODDATE, prod_date);
            values.put(KEY_SYNCSTAT, sync_status);

            // Inserting Row
            long id = db.insert(TABLE_FT, null, values);

            Log.d(TAG, "New pig feed record inserted into sqlite: " + id);
        }

        db.close(); // Closing database connection
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

    public void addMedRec(String mr_id, String date_given, String time_given, String quantity,
                          String unit, String pig_id, String med_id, String sync_status) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_MRID, mr_id);
        values.put(KEY_DATEGIVEN, date_given);
        values.put(KEY_TIMEGIVEN, time_given);
        values.put(KEY_QUANTITY, quantity);
        values.put(KEY_UNIT, unit);
        values.put(KEY_PIGID, pig_id);
        values.put(KEY_MEDID, med_id);
        values.put(KEY_SYNCSTAT, sync_status);

        // Inserting Row
        long id = db.insert(TABLE_MR, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New pig med record inserted into sqlite: " + id);
    }

    public void addMedRecAuto(String date_given, String time_given, String quantity, String unit,
                          String pig_id, String med_id, String sync_status) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_DATEGIVEN, date_given);
        values.put(KEY_TIMEGIVEN, time_given);
        values.put(KEY_QUANTITY, quantity);
        values.put(KEY_UNIT, unit);
        values.put(KEY_PIGID, pig_id);
        values.put(KEY_MEDID, med_id);
        values.put(KEY_SYNCSTAT, sync_status);

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

    public ArrayList<HashMap<String, String>> getNewPigs(){
        ArrayList<HashMap<String,String>> list = new ArrayList<>();

        String selectQuery = "SELECT pig_id, boar_id, sow_id, foster_sow, week_farrowed, gender," +
                " farrowing_date, pig_status, pen_id, breed_id FROM " + TABLE_PIG +
                " WHERE sync_status = 'new' AND pig_status IS NOT 'boar' AND pig_status " +
                "IS NOT 'sow'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        Log.d(TAG, String.valueOf(cursor.getCount()));
        // Move to first row
        cursor.moveToFirst();

        for(int i = 0; i < cursor.getCount();i++)
        {
            HashMap<String, String> result = new HashMap<String, String>();

            result.put(KEY_PIGID, cursor.getString(0));
            result.put(KEY_BOARID, cursor.getString(1));
            result.put(KEY_SOWID, cursor.getString(2));
            result.put(KEY_FOSTER, cursor.getString(3));
            result.put(KEY_WEEKF, cursor.getString(4));
            result.put(KEY_GENDER, cursor.getString(5));
            result.put(KEY_FDATE, cursor.getString(6));
            result.put(KEY_PIGSTAT, cursor.getString(7));
            result.put(KEY_PENID, cursor.getString(8));
            result.put(KEY_BREEDID, cursor.getString(9));

            cursor.moveToNext();
            list.add(result);
        }

        cursor.close();
        db.close();
        // return user
        //Log.d(TAG, "Fetching pigs from Sqlite: " + list.toString());

        return list;
    }

    public ArrayList<HashMap<String, String>> getUpdatedPigs(){
        ArrayList<HashMap<String,String>> list = new ArrayList<>();

        String selectQuery = "SELECT pig_id, boar_id, sow_id, foster_sow, week_farrowed, gender," +
                " farrowing_date, pig_status, pen_id, breed_id FROM " + TABLE_PIG +
                " WHERE sync_status = 'false' AND pig_status IS NOT 'boar' AND pig_status " +
                "IS NOT 'sow'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        Log.d(TAG, String.valueOf(cursor.getCount()));
        // Move to first row
        cursor.moveToFirst();

        for(int i = 0; i < cursor.getCount();i++)
        {
            HashMap<String, String> result = new HashMap<String, String>();

            result.put(KEY_PIGID, cursor.getString(0));
            result.put(KEY_BOARID, cursor.getString(1));
            result.put(KEY_SOWID, cursor.getString(2));
            result.put(KEY_FOSTER, cursor.getString(3));
            result.put(KEY_WEEKF, cursor.getString(4));
            result.put(KEY_GENDER, cursor.getString(5));
            result.put(KEY_FDATE, cursor.getString(6));
            result.put(KEY_PIGSTAT, cursor.getString(7));
            result.put(KEY_PENID, cursor.getString(8));
            result.put(KEY_BREEDID, cursor.getString(9));

            cursor.moveToNext();
            list.add(result);
        }

        cursor.close();
        db.close();
        // return user
        //Log.d(TAG, "Fetching pigs from Sqlite: " + list.toString());

        return list;
    }


    public ArrayList<HashMap<String, String>> getWeightRecs() {
        ArrayList<HashMap<String, String>> list = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_WEIGHT + " WHERE "
                + KEY_SYNCSTAT + " = 'new'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        Log.d(TAG, String.valueOf(cursor.getCount()));
        // Move to first row
        cursor.moveToFirst();

        for(int i = 0; i < cursor.getCount();i++)
        {
            HashMap<String, String> result = new HashMap<String, String>();

            result.put(KEY_RECDATE, cursor.getString(1));
            result.put(KEY_RECTIME, cursor.getString(2));
            result.put(KEY_WEIGHT, cursor.getString(3));
            result.put(KEY_PIGID, cursor.getString(4));
            result.put(KEY_REMARKS, cursor.getString(5));

            cursor.moveToNext();
            list.add(result);

        }

        cursor.close();
        db.close();
        // return user
        //Log.d(TAG, "Fetching weight records from Sqlite: " + list.toString());

        return list;
    }

    public ArrayList<HashMap<String, String>> getFeedRecs() {
        ArrayList<HashMap<String, String>> list = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_FT + " WHERE sync_status = 'new'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        Log.d(TAG, String.valueOf(cursor.getCount()));
        // Move to first row
        cursor.moveToFirst();

        for(int i = 0; i < cursor.getCount();i++) {
            HashMap<String, String> result = new HashMap<String, String>();

            result.put(KEY_QUANTITY, cursor.getString(1));
            result.put(KEY_UNIT, cursor.getString(2));
            result.put(KEY_DATEGIVEN, cursor.getString(3));
            result.put(KEY_TIMEGIVEN, cursor.getString(4));
            result.put(KEY_PIGID, cursor.getString(5));
            result.put(KEY_FEEDID, cursor.getString(6));
            result.put(KEY_PRODDATE, cursor.getString(7));

            cursor.moveToNext();
            list.add(result);

        }

        cursor.close();
        db.close();
        // return user
       // Log.d(TAG, "Fetching pig feed record from Sqlite: " + list.toString());

        return list;
    }

    public ArrayList<HashMap<String, String>> getMedRecs() {
        ArrayList<HashMap<String, String>> list = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_MR + " WHERE sync_status = 'new'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        Log.d(TAG, String.valueOf(cursor.getCount()));
        // Move to first row
        cursor.moveToFirst();

        for(int i = 0; i < cursor.getCount();i++) {
            HashMap<String, String> result = new HashMap<String, String>();

            result.put(KEY_DATEGIVEN, cursor.getString(1));
            result.put(KEY_TIMEGIVEN, cursor.getString(2));
            result.put(KEY_QUANTITY, cursor.getString(3));
            result.put(KEY_UNIT, cursor.getString(4));
            result.put(KEY_PIGID, cursor.getString(5));
            result.put(KEY_MEDID, cursor.getString(6));

            cursor.moveToNext();
            list.add(result);

        }

        cursor.close();
        db.close();
        // return user
       // Log.d(TAG, "Fetching pig med record from Sqlite: " + list.toString());

        return list;
    }

    public ArrayList<HashMap<String, String>> getLocs(String _function) {
        ArrayList<HashMap<String, String>> list = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_LOC + " WHERE address LIKE '%" +
                _function + "%'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        Log.d(TAG, String.valueOf(cursor.getCount()));
        // Move to first row
        cursor.moveToFirst();

        for(int i = 0; i < cursor.getCount();i++) {
            HashMap<String, String> result = new HashMap<String, String>();

            result.put(KEY_LOCID, cursor.getString(0));
            result.put(KEY_LOCNAME, cursor.getString(1));
            result.put(KEY_ADD, cursor.getString(2));

            cursor.moveToNext();
            list.add(result);

        }

        cursor.close();
        db.close();
        // return user
        Log.d(TAG, "Fetching location from Sqlite: " + list.toString());

        return list;
    }

    public HashMap<String, String> getFeedNameAndType(String _id) {
        HashMap<String, String> list = new HashMap<>();

        String selectQuery = "SELECT feed_name, feed_type FROM " + TABLE_FEEDS
                + " WHERE feed_id = '" + _id + "'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            list.put(KEY_FEEDNAME, cursor.getString(0));
            list.put(KEY_FEEDTYPE, cursor.getString(1));
        }

        cursor.close();
        db.close();

        Log.d(TAG, "Getting result: " + list.toString());

        return list;
    }

    public String getFeedName(String _id) {
        String result = "";

        String selectQuery = "SELECT feed_name FROM " + TABLE_FEEDS
                + " WHERE feed_id = '" + _id + "'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            result = cursor.getString(0);
        }

        Log.d(TAG, "Getting result: " + result);

        cursor.close();
        db.close();

        return result;
    }

    public String getMedName(String _id) {
        String result = "";

        String selectQuery = "SELECT med_name FROM " + TABLE_MEDS
                + " WHERE med_id = '" + _id + "'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            result = cursor.getString(0);
        }

        Log.d(TAG, "Getting meds: " + result);

        cursor.close();
        db.close();

        return result;
    }

    public HashMap<String, String> getFeedType(String _name) {

        HashMap<String, String> list = new HashMap<String, String>();

        String selectQuery = "SELECT feed_type FROM " + TABLE_FEEDS + " WHERE feed_name = '" +
                _name + "'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // Move to first row
        cursor.moveToFirst();

        if (cursor.getCount() > 0) {
            list.put(KEY_FEEDTYPE, cursor.getString(0));
        }

        cursor.close();
        db.close();

        // return user
        Log.d(TAG, "Getting feed type: " + list.toString());

        return list;
    }

    public HashMap<String, String> getMedNameAndType(String _id) {

        HashMap<String, String> list = new HashMap<String, String>();

        String selectQuery = "SELECT med_name, med_type FROM " + TABLE_MEDS
                + " WHERE med_id = '" + _id + "'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            list.put(KEY_MEDNAME, cursor.getString(0));
            list.put(KEY_MEDTYPE, cursor.getString(1));
        }

        // return user
        Log.d(TAG, "Getting Med: " + list.toString());

        cursor.close();
        db.close();

        return list;
    }

    public ArrayList<HashMap<String, String>> getPensByLocs(String location) {
        ArrayList<HashMap<String, String>> list = new ArrayList<>();

        String selectQuery = "SELECT a.pen_id, a.pen_no, a.function, a.house_id FROM "
                + TABLE_PEN + " a JOIN " + TABLE_HOUSE
                + " b ON(a.house_id = b.house_id) JOIN " + TABLE_LOC
                + " c ON(b.loc_id = c.loc_id) WHERE c.loc_id = '"
                + location + "'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        Log.d(TAG, String.valueOf(cursor.getCount()));
        // Move to first row
        cursor.moveToFirst();

        for(int i = 0; i < cursor.getCount();i++) {
            HashMap<String, String> result = new HashMap<String, String>();

            result.put(KEY_PENID, cursor.getString(0));
            result.put(KEY_PENNO, cursor.getString(1));
            result.put(KEY_FUNCTION, cursor.getString(2));
            result.put(KEY_HOUSEID, cursor.getString(3));
            cursor.moveToNext();
            list.add(result);

        }

        cursor.close();
        db.close();
        // return user
        Log.d(TAG, "Fetching pens from Sqlite: " + list.toString());

        return list;
    }

    public ArrayList<HashMap<String, String>> getPensByHouse(String house_id) {
        ArrayList<HashMap<String, String>> list = new ArrayList<>();

        String selectQuery = "SELECT pen_id, pen_no, function FROM "
                + TABLE_PEN + " WHERE house_id = '" + house_id + "'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        Log.d(TAG, String.valueOf(cursor.getCount()));
        // Move to first row
        cursor.moveToFirst();

        for(int i = 0; i < cursor.getCount();i++) {
            HashMap<String, String> result = new HashMap<String, String>();

            result.put(KEY_PENID, cursor.getString(0));
            result.put(KEY_PENNO, cursor.getString(1));
            result.put(KEY_FUNCTION, cursor.getString(2));
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

    public ArrayList<HashMap<String, String>> getMedPens(String house_id) {
        ArrayList<HashMap<String, String>> list = new ArrayList<>();

        String selectQuery = "SELECT pen_id, pen_no, function FROM "
                + TABLE_PEN + " WHERE house_id = '" + house_id + "' AND function = 'medication'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        Log.d(TAG, String.valueOf(cursor.getCount()));
        // Move to first row
        cursor.moveToFirst();

        for(int i = 0; i < cursor.getCount();i++) {
            HashMap<String, String> result = new HashMap<String, String>();

            result.put(KEY_PENID, cursor.getString(0));
            result.put(KEY_PENNO, cursor.getString(1));
            result.put(KEY_FUNCTION, cursor.getString(2));
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

    public ArrayList<HashMap<String, String>> getHouses(String location) {
        ArrayList<HashMap<String, String>> list = new ArrayList<>();

        String selectQuery = "SELECT a.house_id, a.house_no, a.house_name, a.function FROM "
                + TABLE_HOUSE + " a JOIN " + TABLE_LOC
                + " b ON(a.loc_id = b.loc_id) WHERE b.loc_id = '" + location + "'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        Log.d(TAG, String.valueOf(cursor.getCount()));
        // Move to first row
        cursor.moveToFirst();

        for(int i = 0; i < cursor.getCount();i++) {
            HashMap<String, String> result = new HashMap<String, String>();

            result.put(KEY_HOUSEID, cursor.getString(0));
            result.put(KEY_HOUSENO, cursor.getString(1));
            result.put(KEY_HNAME, cursor.getString(2));
            result.put(KEY_FUNCTION, cursor.getString(3));
            cursor.moveToNext();
            list.add(result);

            Log.d(TAG, "Getting result: " + list.toString());

        }

        cursor.close();
        db.close();
        // return user
        Log.d(TAG, "Fetching house from Sqlite: " + list.toString());

        return list;
    }

    public ArrayList<HashMap<String, String>> getPen(String _id) {
        ArrayList<HashMap<String, String>> list = new ArrayList<>();

        String selectQuery = "SELECT pen_no, function FROM " + TABLE_PEN
                + " WHERE pen_id = '" + _id + "'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        Log.d(TAG, String.valueOf(cursor.getCount()));
        // Move to first row
        cursor.moveToFirst();

        for(int i = 0; i < cursor.getCount();i++) {
            HashMap<String, String> result = new HashMap<String, String>();

            result.put(KEY_PENNO, cursor.getString(0));
            result.put(KEY_FUNCTION, cursor.getString(1));
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
        ArrayList<HashMap<String, String>> list = new ArrayList<>();

        String selectQuery = "SELECT tag_id, tag_rfid, label FROM " + TABLE_RFID_TAGS
                + " WHERE status = 'inactive'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        cursor.moveToFirst();

        for(int i = 0; i < cursor.getCount();i++) {
            HashMap<String, String> result = new HashMap<String, String>();

            result.put(KEY_TAGID, cursor.getString(0));
            result.put(KEY_TAGRFID, cursor.getString(1));
            result.put(KEY_LABEL, cursor.getString(2));
            cursor.moveToNext();
            list.add(result);

        }

        cursor.close();
        db.close();
        // return user
        Log.d(TAG, "Fetching tags from Sqlite: " + list.toString());

        return list;
    }

    public ArrayList<HashMap<String, String>> getFeeds() {
        ArrayList<HashMap<String, String>> list = new ArrayList<>();

        String selectQuery = "SELECT feed_id, feed_name, feed_type status FROM "
                + TABLE_FEEDS;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        cursor.moveToFirst();

        for(int i = 0; i < cursor.getCount();i++) {
            HashMap<String, String> result = new HashMap<String, String>();

            result.put(KEY_FEEDID, cursor.getString(0));
            result.put(KEY_FEEDNAME, cursor.getString(1));
            result.put(KEY_FEEDTYPE, cursor.getString(2));
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

    public ArrayList<HashMap<String, String>> getMeds() {
        ArrayList<HashMap<String, String>> list = new ArrayList<>();

        String selectQuery = "SELECT med_id, med_name, med_type status FROM "
                + TABLE_MEDS;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        cursor.moveToFirst();

        for(int i = 0; i < cursor.getCount();i++) {
            HashMap<String, String> result = new HashMap<String, String>();

            result.put(KEY_MEDID, cursor.getString(0));
            result.put(KEY_MEDNAME, cursor.getString(1));
            result.put(KEY_MEDTYPE, cursor.getString(2));
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

    public ArrayList<HashMap<String, String>> getWeightRec(String _id) {
        ArrayList<HashMap<String, String>> list = new ArrayList<>();

        /*
        String selectQuery = "SELECT weight, strftime('%W %Y', record_date), record_time FROM "
                + TABLE_WEIGHT + " WHERE pig_id = '" + _id +
                "' AND record_date >= (SELECT MIN(record_date) FROM "
                + TABLE_WEIGHT + ") AND record_date <= date('now') AND record_time >= " +
                "(SELECT MIN(record_time) FROM " + TABLE_WEIGHT + ") AND record_time <= date('now')"
                + " ORDER BY record_date, record_time";
        */

        String selectQuery = "SELECT weight, strftime('%W %Y', record_date), remarks, record_time FROM "
                + TABLE_WEIGHT + " WHERE pig_id = '" + _id + "' ORDER BY record_date, record_time";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        cursor.moveToFirst();

        for(int i = 0; i < cursor.getCount();i++) {
            HashMap<String, String> result = new HashMap<String, String>();

            result.put(KEY_WEIGHT, cursor.getString(0));
            result.put(KEY_RECDATE, cursor.getString(1));
            result.put(KEY_REMARKS, cursor.getString(2));
            cursor.moveToNext();
            list.add(result);

            Log.d(TAG, "Getting result: " + list.toString());
        }

        cursor.close();
        db.close();

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

    public ArrayList<HashMap<String, String>> getPigsByLoc(String _loc) {
        ArrayList<HashMap<String, String>> list
                = new ArrayList<>();

        String selectQuery = "SELECT a.pig_id, a.gender, b.breed_name FROM "
                + TABLE_PIG + " a JOIN " + TABLE_PB
                + " b ON(a.breed_id = b.breed_id) JOIN " + TABLE_PEN
                + " c ON(a.pen_id = c.pen_id) JOIN " + TABLE_HOUSE
                + " d ON(c.house_id = d.house_id) JOIN " + TABLE_LOC
                + " e ON(d.loc_id = e.loc_id) WHERE e.loc_id = '" + _loc + "'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        cursor.moveToFirst();

        for(int i = 0; i < cursor.getCount();i++) {
            HashMap<String, String> result = new HashMap<String, String>();

            result.put(KEY_PIGID, cursor.getString(0));
            result.put(KEY_GENDER, cursor.getString(1));
            result.put(KEY_BREEDNAME, cursor.getString(2));
            cursor.moveToNext();
            list.add(result);

            Log.d(TAG, "Getting result: " + list.toString());
        }

        cursor.close();
        db.close();
        // return user
        Log.d(TAG, "Fetching pigs from Sqlite: " + list.toString());

        return list;
    }

        public ArrayList<HashMap<String, String>> getPigsByPen(String _pen) {
        ArrayList<HashMap<String, String>> list
                = new ArrayList<>();

        String selectQuery = "SELECT a.pig_id, a.gender, b.breed_name FROM "
                + TABLE_PIG + " a JOIN " + TABLE_PB +
                " b ON(a.breed_id = b.breed_id) WHERE a.pen_id = '" + _pen + "'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        cursor.moveToFirst();

        for(int i = 0; i < cursor.getCount();i++) {
            HashMap<String, String> result = new HashMap<String, String>();

            result.put(KEY_PIGID, cursor.getString(0));
            result.put(KEY_GENDER, cursor.getString(1));
            result.put(KEY_BREEDNAME, cursor.getString(2));
            cursor.moveToNext();
            list.add(result);

            Log.d(TAG, "Getting result: " + list.toString());
        }

        cursor.close();
        db.close();
        // return user
        Log.d(TAG, "Fetching pigs from Sqlite: " + list.toString());

        return list;
    }

    public HashMap<String, String> getThePig(String _id) {

        HashMap<String, String> list = new HashMap<String, String>();

        /*
        String selectQuery = "SELECT a.boar_id, a.sow_id, a.foster_sow, a.week_farrowed, "+
                "a.gender, b.breed_name, (SELECT weight FROM " + TABLE_WEIGHT +
                " wr WHERE record_date = (SELECT MAX(record_date) FROM " + TABLE_WEIGHT +
                " wr2 WHERE wr.pig_id = wr2.pig_id) AND a.pig_id = wr.pig_id) as weight, " +
                "(SELECT feed_name FROM " + TABLE_FEEDS + " f JOIN " + TABLE_FT +
                " ft ON(f.feed_id = ft.feed_id) WHERE ft.pig_id = a.pig_id AND ft.time_given = " +
                "(SELECT MAX(time_given) FROM " + TABLE_FT + " ft2 WHERE ft.pig_id = ft2.pig_id)" +
                " AND ft.date_given = (SELECT MAX(date_given) FROM " + TABLE_FT +
                " ft2 WHERE ft.pig_id = ft2.pig_id))" +
                " as feed_name, c.tag_id, c.tag_rfid, c.label, (SELECT med_name FROM " + TABLE_MEDS +
                " m JOIN " + TABLE_MR + " mr ON(m.med_id = mr.med_id) WHERE mr.pig_id = a.pig_id " +
                "AND mr.time_given = (SELECT MAX(time_given) FROM " + TABLE_MR +
                " mr2 WHERE mr.pig_id = mr2.pig_id) AND mr.date_given = (SELECT MAX(date_given) " +
                "FROM " + TABLE_MR + " mr2 WHERE mr.pig_id = mr2.pig_id)) as med_name FROM " +
                TABLE_PIG + " a INNER JOIN " + TABLE_PB +
                " b ON(a.breed_id = b.breed_id) INNER JOIN " + TABLE_RFID_TAGS +
                " c ON(a.pig_id = c.pig_id) WHERE a.pig_id = '" + _id + "'";
        */

        String selectQuery = "SELECT a.boar_id, a.sow_id, a.foster_sow, a.week_farrowed, a.gender, " +
                "b.breed_name, c.tag_id, c.tag_rfid, c.label " +
                "FROM " + TABLE_PIG + " a JOIN " + TABLE_PB + " b ON(a.breed_id = b.breed_id) " +
                "JOIN " + TABLE_RFID_TAGS + " c ON(a.pig_id = c.pig_id) WHERE a.pig_id = '" +
                _id + "'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // Move to first row
        cursor.moveToFirst();

        if (cursor.getCount() > 0) {
            list.put(KEY_BOARID, cursor.getString(0));
            list.put(KEY_SOWID, cursor.getString(1));
            list.put(KEY_FOSTER, cursor.getString(2));
            list.put(KEY_WEEKF, cursor.getString(3));
            list.put(KEY_GENDER, cursor.getString(4));
            list.put(KEY_BREEDNAME, cursor.getString(5));
            list.put(KEY_TAGID, cursor.getString(6));
            list.put(KEY_TAGRFID, cursor.getString(7));
            list.put(KEY_LABEL, cursor.getString(8));
        }

        cursor.close();
        db.close();

        // return user
        Log.d(TAG, "Getting the pig: " + list.toString());

        return list;
    }

    public HashMap<String, String> getPigFeed(String _id) {

        HashMap<String, String> list = new HashMap<String, String>();
        String selectQuery = "SELECT a.feed_name, a.feed_type, b.date_given, b.time_given FROM "
                + TABLE_FEEDS + " a JOIN " + TABLE_FT +
                " b ON(b.feed_id = a.feed_id) WHERE b.pig_id = '" + _id
                + "' ORDER BY b.date_given DESC, b.time_given DESC LIMIT 1";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            list.put(KEY_FEEDNAME, cursor.getString(0));
            list.put(KEY_FEEDTYPE, cursor.getString(1));
        }

        cursor.close();
        db.close();
        // return user
        Log.d(TAG, "Fetching latest feed from Sqlite: " + list.toString());

        return list;
    }

    public HashMap<String, String> getPigMed(String _id) {

        HashMap<String, String> list = new HashMap<String, String>();
        String selectQuery = "SELECT a.med_name, a.med_type, b.date_given, b.time_given FROM "
                + TABLE_MEDS + " a JOIN " + TABLE_MR +
                " b ON(b.med_id = a.med_id) WHERE b.pig_id = '" + _id
                + "' ORDER BY b.date_given DESC, b.time_given DESC LIMIT 1";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            list.put(KEY_MEDNAME, cursor.getString(0));
            list.put(KEY_MEDTYPE, cursor.getString(1));
        }

        cursor.close();
        db.close();
        // return user
        Log.d(TAG, "Fetching latest medication from Sqlite: " + list.toString());

        return list;
    }

    public HashMap<String, String> getPigWeight(String _id) {

        HashMap<String, String> list = new HashMap<String, String>();
        String selectQuery = "SELECT weight, record_date, record_time FROM "
                + TABLE_WEIGHT + " WHERE pig_id = '" + _id
                + "' ORDER BY record_date DESC, record_time DESC LIMIT 1";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            list.put(KEY_WEIGHT, cursor.getString(0));
        }

        cursor.close();
        db.close();
        // return user
        Log.d(TAG, "Fetching latest weight from Sqlite: " + list.toString());

        return list;
    }

    public HashMap<String, String> getPigGroup(String _id) {

        HashMap<String, String> list = new HashMap<String, String>();
        String selectQuery = "SELECT a.group_name, a.pen_id, b.pen_no, b.function FROM "
                + TABLE_PIG + " a JOIN " + TABLE_PEN
                + " b ON(a.pen_id = b.pen_id) WHERE a.pig_id = '"
                + _id + "'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            list.put(KEY_GNAME, cursor.getString(0));
            list.put(KEY_PENID, cursor.getString(1));
            list.put(KEY_PENNO, cursor.getString(2));
            list.put(KEY_FUNCTION, cursor.getString(3));
        }

        cursor.close();
        db.close();
        // return user
        Log.d(TAG, "Fetching pigs from Sqlite: " + list.toString());

        return list;
    }

    public ArrayList<HashMap<String, String>> getBoars(){
        ArrayList<HashMap<String, String>> list
                = new ArrayList<>();

        String selectQuery = "SELECT a.pig_id, b.breed_name FROM "
                + TABLE_PIG + " a JOIN " + TABLE_PB +
                " b ON(a.breed_id = b.breed_id) WHERE a.gender = 'M' AND a.pig_status = 'boar'";

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
                " b ON(a.breed_id = b.breed_id) WHERE a.gender = 'F' AND a.pig_status = 'sow'";

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

    public ArrayList<HashMap<String, String>> getSowNot(String sow_id){
        ArrayList<HashMap<String, String>> list
                = new ArrayList<>();

        String selectQuery = "SELECT a.pig_id, b.breed_name FROM "
                + TABLE_PIG + " a JOIN " + TABLE_PB
                + " b ON(a.breed_id = b.breed_id) WHERE a.pig_id IS NOT '"
                + sow_id + "' AND a.gender = 'F' AND a.pig_status = 'sow'";

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

    public ArrayList<HashMap<String, String>> getBoarNot(String boar_id){
        ArrayList<HashMap<String, String>> list
                = new ArrayList<>();

        String selectQuery = "SELECT a.pig_id, b.breed_name FROM "
                + TABLE_PIG + " a JOIN " + TABLE_PB
                + " b ON(a.breed_id = b.breed_id) WHERE a.pig_id IS NOT '"
                + boar_id + "' AND a.gender = 'F' AND a.pig_status = 'boar'";

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

    public String getRFID(String _rfid) {

        String result = "";
        String selectQuery = "SELECT label FROM " + TABLE_RFID_TAGS + " WHERE tag_id = '"
                + _rfid + "'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        cursor.moveToFirst();

        if (cursor.getCount() > 0) {
            String res = cursor.getString(0);
            if(res != null){
                result = res;
            }
        }

        cursor.close();
        db.close();

        return result;
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
    public boolean isPigExists(String pig_id) {
        Boolean isExists;
        String selectQuery = "SELECT * FROM " + TABLE_PIG + " WHERE pig_id = '" + pig_id + "'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        isExists = cursor.getCount() > 0;
        cursor.close();
        db.close();

        return isExists;
    }

    public void updateTag(String tag_id, String pig_id, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE rfid_tags " +
                "SET pig_id = '" + pig_id + "', status = '" + status + "' " +
                "WHERE tag_id = '" + tag_id + "'";
        db.execSQL(query);
        db.close();

        Log.d(TAG, "Updated RFID tag");
    }

    public void updateTagLabel(String tag_id, String label) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE rfid_tags " +
                "SET label = '" + label + "' " +
                "WHERE tag_id = '" + tag_id + "'";
        db.execSQL(query);
        db.close();

        Log.d(TAG, "Updated RFID tag");
    }

    public void updateBoarParent(String pig_id, String boar_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE pig " +
                "SET boar_id = '" + boar_id + "', sync_status = 'false' " +
                "WHERE pig_id = '" + pig_id + "'";
        db.execSQL(query);
        db.close();

        Log.d(TAG, "Updated Boar Parent");
    }

    public void updateSowParent(String pig_id, String sow_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE pig " +
                "SET sow_id = '" + sow_id + "', sync_status = 'false' " +
                "WHERE pig_id = '" + pig_id + "'";
        db.execSQL(query);
        db.close();

        Log.d(TAG, "Updated Sow Parent");
    }

    public void updateFosterSowParent(String pig_id, String foster_sow) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE pig " +
                "SET foster_sow = '" + foster_sow + "', sync_status = 'false' " +
                "WHERE pig_id = '" + pig_id + "'";
        db.execSQL(query);
        db.close();

        Log.d(TAG, "Updated Foster Sow");
    }

    public void updatePigPen(String pig_id, String pen_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE pig " +
                "SET pen_id = '" + pen_id + "' " +
                "WHERE pig_id = '" + pig_id + "'";
        db.execSQL(query);
        db.close();

        Log.d(TAG, "Updated Holding Pen");
    }

    /**
     * Re create database: Delete all tables and create them again
     */
    public void deleteTables() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows

        db.delete(TABLE_FT, null, null);
        db.delete(TABLE_MR, null, null);
        db.delete(TABLE_WEIGHT, null, null);
        db.delete(TABLE_FEEDS, null, null);
        db.delete(TABLE_MEDS, null, null);
        db.delete(TABLE_RFID_TAGS, null, null);
        db.delete(TABLE_PIG, null, null);
        db.delete(TABLE_PB, null, null);
        db.delete(TABLE_PEN, null, null);
        db.delete(TABLE_HOUSE, null, null);
        db.delete(TABLE_LOC, null, null);

        db.close();

        Log.d(TAG, "Deleted all tables info from sqlite");
    }

}