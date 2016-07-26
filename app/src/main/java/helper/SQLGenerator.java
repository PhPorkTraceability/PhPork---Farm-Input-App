package helper;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Aspire V3 on 7/11/2016.
 */
public class SQLGenerator {
    private static String getFeedTransaction(SQLiteHandler sh) {
        SQLiteDatabase db = sh.getReadableDatabase();
        String feedTransList = "";
        String selectQuery = "SELECT `quantity`, `unit`, `date_given`, `time_given`, `pig_id`, " +
                "`feed_id`, `prod_date` FROM feed_transaction WHERE `sync_status` = 'new'";

        Cursor c = db.rawQuery(selectQuery, null);
        c.moveToFirst();

        for(int i=0; i<c.getCount(); i++) {
            feedTransList += "INSERT INTO `feed_transaction` (`quantity`, " +
                    "`unit`, `date_given`, `time_given`, `pig_id`, `feed_id`, `prod_date`) VALUES ";

            feedTransList += "(\'" + c.getString(0) + "\'";

            for (int j = 1; j < c.getColumnCount(); j++) {
                feedTransList += ", \'" + c.getString(j) + "\'";
            }

            feedTransList += ")\n";

            c.moveToNext();
        }
        c.close();

        feedTransList += "\n";

        return feedTransList;
    }

    private static String getMedRecord(SQLiteHandler sh) {
        SQLiteDatabase db = sh.getReadableDatabase();
        String feedTransList = "";
        String selectQuery = "SELECT `date_given`, `time_given`, `quantity`, `unit`, `pig_id`, " +
                "`med_id` FROM med_record WHERE `sync_status` = 'new'";

        Cursor c = db.rawQuery(selectQuery, null);
        c.moveToFirst();

        for(int i=0; i<c.getCount(); i++) {
            feedTransList += "INSERT INTO `med_record` (`date_given`, " +
                    "`time_given`, `quantity`, `unit`, `pig_id`, `med_id`) VALUES ";

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

    private static String getPigs(SQLiteHandler sh) {
        SQLiteDatabase db = sh.getReadableDatabase();
        String feedTransList = "";
        String selectQuery = "SELECT pig_id, boar_id, sow_id, foster_sow, week_farrowed, gender, " +
                "farrowing_date, pig_status, pen_id, breed_id, user, pig_batch FROM pig " +
                "WHERE sync_status = 'new'";

        Cursor c = db.rawQuery(selectQuery, null);
        c.moveToFirst();

        for(int i=0; i<c.getCount(); i++) {
            feedTransList += "INSERT IGNORE INTO `pig` (`pig_id`, `boar_id`, " +
                    "`sow_id`, `foster_sow`, `week_farrowed`, `gender`, `farrowing_date`, " +
                    "`pig_status`, `pen_id`, `breed_id`, `user`, `pig_batch`) VALUES "; //, `user`, `pig_batch`) VALUES ";

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

    private static String getWeightRecord(SQLiteHandler sh) {
        SQLiteDatabase db = sh.getReadableDatabase();
        String feedTransList = "";
        String selectQuery = "SELECT `record_date`, `record_time`, `weight`, " +
                "`pig_id`, `remarks` FROM weight_record WHERE `sync_status` = 'new'";

        Cursor c = db.rawQuery(selectQuery, null);
        c.moveToFirst();

        for(int i=0; i<c.getCount(); i++) {
            feedTransList += "INSERT INTO `weight_record` (`record_date`, " +
                    "`record_time`, `weight`, `pig_id`, `remarks`) VALUES ";

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
                getWeightRecord(sh);
    }
}
