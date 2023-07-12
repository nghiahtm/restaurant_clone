package com.example.restaurantmanager.view.admin;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.restaurantmanager.R;
import com.example.restaurantmanager.interfacez.SettingClickInterface;
import com.example.restaurantmanager.realm_object.PrinterIPAdress;
import com.example.restaurantmanager.realm_object.Setting;
import com.example.restaurantmanager.utils.DataUtil;
import com.example.restaurantmanager.utils.IPAdressUtil;

public class SettingActivity extends BaseAdminActivity implements SettingClickInterface {

    Setting setting;
    EditText txt_textPass;
    EditText txt_printer_bill_ip;
    EditText txt_printer_bill_port;
    EditText txt_printer_kuche_ip;
    EditText txt_printer_kuche_port;
    EditText txt_printer_sushi_ip;
    EditText txt_printer_sushi_port;
    EditText txt_printer_getrank_ip;
    EditText txt_printer_getrank_port;
    EditText txt_tax_rate;

    Button btn_Save;


    public void findViewByID(){
        txt_textPass = findViewById(R.id.txt_textPass);
        txt_printer_bill_ip = findViewById(R.id.txt_printer_bill_ip);
        txt_printer_bill_port = findViewById(R.id.txt_printer_bill_port);
        txt_printer_kuche_ip = findViewById(R.id.txt_printer_kuche_ip);
        txt_printer_kuche_port = findViewById(R.id.txt_printer_kuche_port);
        txt_printer_sushi_ip = findViewById(R.id.txt_printer_sushi_ip);
        txt_printer_sushi_port = findViewById(R.id.txt_printer_sushi_port);
        txt_printer_getrank_ip = findViewById(R.id.txt_printer_getrank_ip);
        txt_printer_getrank_port = findViewById(R.id.txt_printer_getrank_port);
        txt_tax_rate = findViewById(R.id.txt_tax_rate);
        btn_Save = findViewById(R.id.btn_Save);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        findViewByID();
        setTitle("Setting");
        realm.beginTransaction();
            setting = realm.where(Setting.class).equalTo("id", DataUtil.settingId).findFirst();
            if(setting == null){
                setting = realm.createObject(Setting.class, DataUtil.settingId);

            }
            if(setting.getBillPrinter()==null){
                realm.createEmbeddedObject(PrinterIPAdress.class,setting,"billPrinter");
            }
            if(setting.getGetrankPrinter()==null){
                realm.createEmbeddedObject(PrinterIPAdress.class,setting,"getrankPrinter");
            }
            if(setting.getSushiPrinter()==null){
                realm.createEmbeddedObject(PrinterIPAdress.class,setting,"sushiPrinter");
            }
            if(setting.getKuchePrinter()==null){
                realm.createEmbeddedObject(PrinterIPAdress.class,setting,"kuchePrinter");
            }
        realm.commitTransaction();
        txt_textPass.setText(setting.getAdminPassword());
        txt_printer_bill_ip.setText(setting.getBillPrinter().getIP());
        txt_printer_bill_port.setText(setting.getBillPrinter().getPort()+"");

        txt_printer_sushi_ip.setText(setting.getSushiPrinter().getIP());
        txt_printer_sushi_port.setText(setting.getSushiPrinter().getPort()+"");

        txt_printer_kuche_ip.setText(setting.getKuchePrinter().getIP());
        txt_printer_kuche_port.setText(setting.getKuchePrinter().getPort()+"");

        txt_printer_getrank_ip.setText(setting.getGetrankPrinter().getIP());
        txt_printer_getrank_port.setText(setting.getGetrankPrinter().getPort()+"");

        txt_tax_rate.setText(setting.getTaxRate().toString());



    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
    public void saveSetting(View view){
        if(!IPAdressUtil.verifyIP(txt_printer_bill_ip.getText().toString())&& !txt_printer_bill_ip.getText().toString().equals("")){
            Toast.makeText(context,"IP not validate",Toast.LENGTH_LONG).show();
            return;
        }
        if(!IPAdressUtil.verifyIP(txt_printer_getrank_ip.getText().toString())&& !txt_printer_getrank_ip.getText().toString().equals("")){
            Toast.makeText(context,"IP not validate",Toast.LENGTH_LONG).show();
            return;
        }
        if(!IPAdressUtil.verifyIP(txt_printer_kuche_ip.getText().toString())&& !txt_printer_kuche_ip.getText().toString().equals("")){
            Toast.makeText(context,"IP not validate",Toast.LENGTH_LONG).show();
            return;
        }
        if(!IPAdressUtil.verifyIP(txt_printer_sushi_ip.getText().toString())&& !txt_printer_sushi_ip.getText().toString().equals("")){
            Toast.makeText(context,"IP not validate",Toast.LENGTH_LONG).show();
            return;
        }

        realm.beginTransaction();
        setting.setAdminPassword(txt_textPass.getText().toString());
        setting.getBillPrinter().setIP(txt_printer_bill_ip.getText().toString());
        setting.getBillPrinter().setPort(Integer.parseInt(txt_printer_bill_port.getText().toString()));

        setting.getGetrankPrinter().setIP(txt_printer_getrank_ip.getText().toString());
        setting.getGetrankPrinter().setPort(Integer.parseInt(txt_printer_getrank_port.getText().toString()));

        setting.getKuchePrinter().setIP(txt_printer_kuche_ip.getText().toString());
        setting.getKuchePrinter().setPort(Integer.parseInt(txt_printer_kuche_port.getText().toString()));

        setting.getSushiPrinter().setIP(txt_printer_sushi_ip.getText().toString());
        setting.getSushiPrinter().setPort(Integer.parseInt(txt_printer_sushi_port.getText().toString()));

        String taxRateStr = txt_tax_rate.getText().toString().equals("")?"0":txt_tax_rate.getText().toString();
        setting.setTaxRate(Double.parseDouble(taxRateStr));
        realm.commitTransaction();
        finish();
    }



}