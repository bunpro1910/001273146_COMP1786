package vn.edu.greenwich.cw1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;

import java.util.Date;
import vn.edu.greenwich.cw1.database.BackupEntry;
import vn.edu.greenwich.cw1.database.TripDAO;
import vn.edu.greenwich.cw1.models.Backup;
import vn.edu.greenwich.cw1.models.TripItem;
import vn.edu.greenwich.cw1.models.Trip;
import vn.edu.greenwich.cw1.ui.trip.list.TripListFragment;
public class SettingActivity extends AppCompatActivity {
    protected TripDAO _db;
    protected Button settingBackup, settingResetDatabase;
    FragmentManager fragmentManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        setTitle(R.string.label_setting);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        fragmentManager =getSupportFragmentManager();
        _db = new TripDAO(this);

        settingBackup = findViewById(R.id.settingBackup);
        settingResetDatabase = findViewById(R.id.settingResetDatabase);

        settingBackup.setOnClickListener(v -> backup());
        settingResetDatabase.setOnClickListener(v -> resetDatabase());
    }

    protected void backup() {
        ArrayList<Trip> trips = _db.getTripList(null, null, false);
        ArrayList<TripItem> tripItems = _db.getTripListList(null, null, false);

        if (null != trips && 0 < trips.size() && null != tripItems && 0 < tripItems.size()) {
            String deviceName = Build.MANUFACTURER
                    + " " + Build.MODEL + " " + Build.VERSION.RELEASE
                    + " " + Build.VERSION_CODES.class.getFields()[android.os.Build.VERSION.SDK_INT].getName();

            Backup backup = new Backup(new Date(), deviceName,"ps8298j", trips, tripItems);

            FirebaseFirestore.getInstance().collection(BackupEntry.TABLE_NAME)
                    .add(backup)
                    .addOnSuccessListener(document -> {
                        Toast.makeText(this, R.string.notification_backup_success, Toast.LENGTH_SHORT).show();
                        Log.d(getResources().getString(R.string.label_backup_firestore), document.getId());
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, R.string.notification_backup_fail, Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    });
        } else {
            Toast.makeText(this, R.string.error_empty_list, Toast.LENGTH_SHORT).show();
        }
    }

    protected void resetDatabase() {
        _db.reset();
        finish();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);


        Toast.makeText(this, R.string.label_reset_database, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}