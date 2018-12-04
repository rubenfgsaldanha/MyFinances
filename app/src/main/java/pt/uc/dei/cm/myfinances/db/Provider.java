package pt.uc.dei.cm.myfinances.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class Provider extends ContentProvider {
    private static final String TAG = "Provider";
    private static final int CONSTANTS_WALLET = 1;
    private static final int CONSTANT_WALLET_ID = 2;
    private static final int CONSTANTS_TRANSACTION = 3;
    private static final int CONSTANT_TRANSACTION_ID = 4;
    private static final UriMatcher MATCHER;
    private static final String TABLE_WALLET = "wallets";
    private static final String TABLE_TRANSACTION = "transactions";

    //Private DB instance
    private DataBaseHelper db = null;

    //BaseColumns interface provides names for the very common _ID and _COUNT columns.
    //Contract Class
    public static final class Constants implements BaseColumns {
        public static final Uri CONTENT_URI_WALLET =
                Uri.parse("content://pt.uc.dei.cm.myfinances.db.Provider/wallets");
        public static final Uri CONTENT_URI_TRANSACTION =
                Uri.parse("content://pt.uc.dei.cm.myfinances.db.Provider/transactions");

        public static final String DEFAULT_SORT_ORDER = "wallet_name";       /////////////////////////////////not final yet

        public static final String WALLET_NAME = "wallet_name";
        public static final String WALLET_BALANCE = "wallet_balance";

        public static final String TRANSACTION_DATE = "transaction_date";
        public static final String TRANSACTION_WALLET_NAME = "transaction_wallet_name";
        public static final String TRANSACTION_CATEGORY = "transaction_category";
        public static final String TRANSACTION_COMMENT = "transaction_comment";
        public static final String TRANSACTION_AMOUNT = "transaction_amount";
    }

    static {                                                //Para eu perceber o que me está a ser pedido
        MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
        MATCHER.addURI("pt.uc.dei.cm.myfinances.db.Provider",
                "wallets", CONSTANTS_WALLET);
        MATCHER.addURI("pt.uc.dei.cm.myfinances.db.Provider",
                "wallets/#", CONSTANT_WALLET_ID);

        MATCHER.addURI("pt.uc.dei.cm.myfinances.db.Provider",
                "transactions", CONSTANTS_TRANSACTION);
        MATCHER.addURI("pt.uc.dei.cm.myfinances.db.Provider",
                "transactions/#", CONSTANT_TRANSACTION_ID);
    }

    @Override
    public boolean onCreate() {
        Log.d(TAG,"OnCreate");
        db = new DataBaseHelper(getContext());

        return (!(db == null));
    }

    @Override
    public Cursor query(Uri url, String[] projection, String selection, String[] selectionArgs, String sort) {
        Log.d(TAG,"Query");
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String orderBy="";

        int uriType = MATCHER.match(url);
        switch(uriType){
            case CONSTANTS_WALLET:
                qb.setTables(TABLE_WALLET);
                orderBy = Constants.DEFAULT_SORT_ORDER;
                break;
            case CONSTANTS_TRANSACTION:
                qb.setTables(TABLE_TRANSACTION);
                orderBy = "transaction_wallet_name";
                break;
        }

        //selection = selection + "_ID = " + url.getLastPathSegment();

        Cursor c =
                qb.query(db.getReadableDatabase(), projection, selection,
                        selectionArgs, null, null, null);

        //Cursor adaptor receives notifications, watching the content URI for changes - register observer
        c.setNotificationUri(getContext().getContentResolver(), url);

        return (c);
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri url, ContentValues initialValues) {
        int uriType = MATCHER.match(url);

        String table="";
        Uri constantUri=null;

        switch(uriType){
            case CONSTANTS_WALLET:
                table = TABLE_WALLET;
                constantUri = Constants.CONTENT_URI_WALLET;
                break;
            case CONSTANTS_TRANSACTION:
                table = TABLE_TRANSACTION;
                constantUri = Constants.CONTENT_URI_TRANSACTION;
                break;
        }

        long rowID = db.getWritableDatabase().insert(table, null, initialValues);

        if (rowID > 0) {
            Uri uri = ContentUris.withAppendedId(constantUri, rowID);
            getContext().getContentResolver().notifyChange(uri, null);

            return (uri);
        }

        throw new SQLException("Failed to insert row into " + url);
    }

    @Override
    public int delete(Uri url, String where, String[] whereArgs) {
        int uriType = MATCHER.match(url);
        int count = -1;

        switch(uriType){
            case CONSTANTS_WALLET:
                count = db.getWritableDatabase().delete(TABLE_WALLET, where, whereArgs);
                break;
            case CONSTANTS_TRANSACTION:
                count = db.getWritableDatabase().delete(TABLE_TRANSACTION, where, whereArgs);
                break;
        }

        getContext().getContentResolver().notifyChange(url, null);

        return (count);
    }

    @Override
    public int update(Uri url, ContentValues values, String where, String[] whereArgs) {
        int uriType = MATCHER.match(url);
        int count = -1;

        switch(uriType){
            case CONSTANTS_WALLET:
                count = db.getWritableDatabase().update(TABLE_WALLET, values, where, whereArgs);
                break;
            case CONSTANTS_TRANSACTION:
                count = db.getWritableDatabase().update(TABLE_TRANSACTION, values, where, whereArgs);
                break;
        }

        getContext().getContentResolver().notifyChange(url, null);

        return (count);
    }
}
