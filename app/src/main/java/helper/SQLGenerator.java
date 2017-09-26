package helper;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Aspire V3 on 7/11/2016.
 */
public class SQLGenerator {
    private static String getFeedTransaction(SQLiteHandler sh) {
        SQLiteDatabase db = sh.getReadableDatabase();
        String list = "";
        String selectQuery = "SELECT `quantity`, `unit`, `date_given`, `time_given`, `pig_id`, " +
                "`feed_id`, `prod_date` FROM feed_transaction WHERE `sync_status` = 'new'";

        Cursor c = db.rawQuery(selectQuery, null);
        c.moveToFirst();

        for(int i=0; i<c.getCount(); i++) {
            list += "INSERT INTO `feed_transaction` (`quantity`, " +
                    "`unit`, `date_given`, `time_given`, `pig_id`, `feed_id`, `prod_date`) VALUES ";

            list += "(\'" + c.getString(0) + "\'";

            for (int j = 1; j < c.getColumnCount(); j++) {
                list += ", \'" + c.getString(j) + "\'";
            }

            list += ")\n";

            c.moveToNext();
        }
        c.close();

        list += "\n";

        return list;
    }

    private static String getMedRecord(SQLiteHandler sh) {
        SQLiteDatabase db = sh.getReadableDatabase();
        String list = "";
        String selectQuery = "SELECT `date_given`, `time_given`, `quantity`, `unit`, `pig_id`, " +
                "`med_id` FROM med_record WHERE `sync_status` = 'new'";

        Cursor c = db.rawQuery(selectQuery, null);
        c.moveToFirst();

        for(int i=0; i<c.getCount(); i++) {
            list += "INSERT INTO `med_record` (`date_given`, " +
                    "`time_given`, `quantity`, `unit`, `pig_id`, `med_id`) VALUES ";

            list += "(\'" + c.getString(0) + "\'";

            for(int j=1; j<c.getColumnCount(); j++) {
                list += ", \'" + c.getString(j) + "\'";
            }

            list += ")\n";

            c.moveToNext();
        }
        c.close();

        list += "\n";

        return list;
    }

    private static String getPigs(SQLiteHandler sh) {
        SQLiteDatabase db = sh.getReadableDatabase();
        String list = "";
        String selectQuery = "SELECT pig_id, boar_id, sow_id, foster_sow, week_farrowed, gender, " +
                "farrowing_date, pig_status, pen_id, breed_id, user, pig_batch FROM pig " +
                "WHERE sync_status = 'new'";

        Cursor c = db.rawQuery(selectQuery, null);
        c.moveToFirst();

        for(int i=0; i<c.getCount(); i++) {
            list += "INSERT OR REPLACE INTO `pig` (`pig_id`, `boar_id`, " +
                    "`sow_id`, `foster_sow`, `week_farrowed`, `gender`, `farrowing_date`, " +
                    "`pig_status`, `pen_id`, `breed_id`, `user`, `pig_batch`) VALUES ";

            list += "(\'" + c.getString(0) + "\'";

            for(int j=1; j<c.getColumnCount(); j++) {
                list += ", \'" + c.getString(j) + "\'";
            }


            list += ")\n";

            c.moveToNext();
        }
        c.close();

        list += "\n";

        return list;
    }

    private static String getWeightRecord(SQLiteHandler sh) {
        SQLiteDatabase db = sh.getReadableDatabase();
        String list = "";
        String selectQuery = "SELECT `record_date`, `record_time`, `weight`, " +
                "`pig_id`, `user`, `remarks` FROM weight_record WHERE `sync_status` = 'new'";

        Cursor c = db.rawQuery(selectQuery, null);
        c.moveToFirst();

        for(int i=0; i<c.getCount(); i++) {
            list += "INSERT INTO `weight_record` (`record_date`, " +
                    "`record_time`, `weight`, `pig_id`, `user`, `remarks`) VALUES ";

            list += "(\'" + c.getString(0) + "\'";

            for(int j=1; j<c.getColumnCount(); j++) {
                list += ", \'" + c.getString(j) + "\'";
            }

            list += ")\n";

            c.moveToNext();
        }
        c.close();

        list += "\n";

        return list;
    }

    private static String getRFIDTags(SQLiteHandler sh) {
        SQLiteDatabase db = sh.getReadableDatabase();
        String list = "";
        String selectQuery = "SELECT `tag_id`, `tag_rfid`, `pig_id`, " +
                "`label`, `status` FROM rfid_tags WHERE `sync_status` = 'new'";

        Cursor c = db.rawQuery(selectQuery, null);
        c.moveToFirst();

        for(int i=0; i<c.getCount(); i++) {
            list += "INSERT OR REPLACE INTO `rfid_tags` (`tag_id`, " +
                    "`tag_rfid`, `pig_id`, `label`, `status`) VALUES ";

            list += "(\'" + c.getString(0) + "\'";

            for(int j=1; j<c.getColumnCount(); j++) {
                list += ", \'" + c.getString(j) + "\'";
            }

            list += ")\n";

            c.moveToNext();
        }
        c.close();

        list += "\n";

        return list;
    }

    private static String getUserTrans(SQLiteHandler sh) {
        SQLiteDatabase db = sh.getReadableDatabase();
        String feedTransList = "";
        String selectQuery = "SELECT `user_id`, `date_edited`, `id_edited`, `type_edited`, " +
                "`prev_value`, `curr_value`, `pig_id`, `flag` FROM user_transaction WHERE `sync_status` = 'new'";

        Cursor c = db.rawQuery(selectQuery, null);
        c.moveToFirst();

        for(int i=0; i<c.getCount(); i++) {
            feedTransList += "INSERT INTO `user_transaction` (`user_id`, `date_edited`, `id_edited`, `type_edited`, " +
                    "`prev_value`, `curr_value`, `pig_id`, `flag`) VALUES ";

            feedTransList += "(\'" + c.getString(0) + "\'";

            for(int j=1; j<c.getColumnCount(); j++) {
                feedTransList += ", \'" + c.getString(j) + "\'";
            }

            feedTransList += ")\n";

            c.moveToNext();
        }
        c.close();

        feedTransList += "\n";

        return feedTransList;
    }

    public static String getAll(SQLiteHandler sh) {
        return getFeedTransaction(sh) +
                getMedRecord(sh) +
                getPigs(sh) +
                getWeightRecord(sh) +
                getRFIDTags(sh) +
                getUserTrans(sh);
    }

}
