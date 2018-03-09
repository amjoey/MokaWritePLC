package com.amjoey.mokawriteplc;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import Moka7.*;


public class MainActivity extends AppCompatActivity {

    int iMemoryAdd;
    int bMemoryVal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    S7Client client = new S7Client();

    public void readdb_val(View v){



        EditText mwAdd = (EditText) findViewById(R.id.txtMWAddress);
        EditText mwVal = (EditText) findViewById(R.id.txtValue);

        if (!mwAdd.getText().toString().equals("")){
            iMemoryAdd = Integer.parseInt(mwAdd.getText().toString());
        }else{
            iMemoryAdd = 0;
        }

        if (!mwVal.getText().toString().equals("")){
            bMemoryVal = Integer.parseInt(mwVal.getText().toString());
        }else{
            bMemoryVal = 0;
        }

        Context context = getApplicationContext();
        Toast.makeText(context, "WriteArea of DB1.DBD" + iMemoryAdd + " : "+bMemoryVal, Toast.LENGTH_LONG).show();

        new PlcReader().execute("");
    }

    private class PlcReader extends AsyncTask<String, Void, String>{

        String ret = "";



        @Override
        protected String doInBackground(String... params){


            try{
                client.SetConnectionType(S7.S7_BASIC);
                int res=client.ConnectTo("192.168.1.12",0,0);

                if(res==0){//connection OK
                     /*
                    byte[] data = new byte[4];
                    res = client.ReadArea(S7.S7AreaDB,1,10,2,data);
                  //  ret = "value of DB1.DBD25: "+ S7.GetFloatAt(data,0);
                    ret = "value of DB1.DBD10: "+ S7.GetWordAt(data,0);
                    */

                    byte[] dataWrite = new byte[2];
                    // S7.SetBitAt(dataWrite, 0, 1, true);
                    // S7.SetDIntAt(dataWrite,0,5);
                    S7.SetWordAt(dataWrite,0,bMemoryVal);

                    client.WriteArea(S7.S7AreaDB, 1, iMemoryAdd, 2, dataWrite);
                    ret = "WriteArea of DB1.DBD" + iMemoryAdd + " : "+bMemoryVal;



                }else{
                    ret = "ERR: "+ S7Client.ErrorText(res);
                }
                client.Disconnect();
            }catch (Exception e) {
                ret = "EXC: "+e.toString();
                Thread.interrupted();
            }
            return "executed";
        }

        @Override
        protected void onPostExecute(String result){


            TextView txout = (TextView) findViewById(R.id.textView);
            txout.setText(ret);
        }
    }

}
