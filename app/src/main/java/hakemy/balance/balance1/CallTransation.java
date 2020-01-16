package hakemy.balance.balance1;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class CallTransation extends AppCompatActivity {
    private static final int RC_HANDLE_Phone_call_PERM=30;
    private   static String code_call_vodafone = "*858*",orange_code="*102*",we="*555*",et="*556*";

    private EditText nEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.call_transation);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE},RC_HANDLE_Phone_call_PERM);
        nEditText=(EditText) findViewById(R.id.number);
        nEditText.setText(String.valueOf( getIntent().getStringExtra("number")));

    }


    /**
     *
     * we button
     * @param view
     */
    public void we (View view)
    {

        if(!nEditText.getText().toString().isEmpty())
        {
            
            Make_USSD_Call(nEditText.getText().toString(),we);

        }
    }


    /**
     *
     * et button
     * @param view
     */
    public void et (View view)
    {

        if(!nEditText.getText().toString().isEmpty())
        {
            
            Make_USSD_Call(nEditText.getText().toString(),et);

        }
    }


    /**
     *
     * orange button
     * @param view
     */
    public void orange (View view)
    {

        if(!nEditText.getText().toString().isEmpty())
        {

            Make_USSD_Call(nEditText.getText().toString(),orange_code);

        }
           


    }

    /**
     *
     * vodafone button
     * @param view
     */
    public void vodafone (View view)
    {

        if(!nEditText.getText().toString().isEmpty())
        {
           
            Make_USSD_Call(nEditText.getText().toString(),code_call_vodafone);

        }
    }



    /**
     *
     * @param number phone number
     * @param code  code before phone number
     */

    public void Make_USSD_Call(String number,String code)
    {

        Intent in = new Intent(Intent.ACTION_CALL);
        String encodeHash = Uri.encode("#");
        in.setData(Uri.parse("tel:" + (code +number + encodeHash).toString().trim()));
        
            startActivity(in);
        

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        if (requestCode ==  RC_HANDLE_Phone_call_PERM)
        {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {

            }
            else
            {
                
            }

        }

    }


}

