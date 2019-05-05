package id.co.cng.spj;

import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import id.co.cng.spj.utility.PrefManager;

public class SettingActivity extends AppCompatActivity {

    private TextInputEditText serverLama, serverBaru;
    private Button ubah;
    private PrefManager prefManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        serverLama = findViewById(R.id.editText_setting);
        serverBaru = findViewById(R.id.editText2_setting);
        ubah = findViewById(R.id.button_setting);

        prefManager = new PrefManager(this);

        if (prefManager.catchString("server").equals("")) {
            prefManager.saveString("server", "192.168.43.106");
        }

        serverLama.setText(prefManager.catchString("server"));

        ubah.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                prefManager.saveString("server", String.valueOf(serverBaru.getText()));
                Toast.makeText(SettingActivity.this, "berhasil disimpan", Toast.LENGTH_SHORT).show();
                serverLama.setText(prefManager.catchString("server"));


            }
        });
    }
}
