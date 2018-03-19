package com.e4project.airnotif;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class LoginActivity extends AppCompatActivity {

    private static final boolean TEST = false;

    private String ipAddress;
    private int port;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText userNameEditText = findViewById(R.id.login_user_name);
        final Button signIn = findViewById(R.id.login_sign_in);
        final ProgressBar progressBar = findViewById(R.id.login_progress_bar);
        final ImageButton wifiConfig = findViewById(R.id.login_wifi_config);
        final ProgressBar progressBarWifi = findViewById(R.id.login_progress_bar_wifi);

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        ipAddress = prefs.getString("ipAddress", "NULL");
        port = prefs.getInt("port", 0);
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (TEST || Connection.checkIPAdress(ipAddress, port)) {
                    wifiConfig.getDrawable().setColorFilter(getResources().getColor(R.color.greenDark), PorterDuff.Mode.SRC_ATOP);
                }
            }
        }).start();

        wifiConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                LayoutInflater inflater = LoginActivity.this.getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.dialog_wifi_config, null);
                final EditText editText1 = dialogView.findViewById(R.id.dialog_wifi_1);
                final EditText editText2 = dialogView.findViewById(R.id.dialog_wifi_2);
                final EditText editText3 = dialogView.findViewById(R.id.dialog_wifi_3);
                final EditText editText4 = dialogView.findViewById(R.id.dialog_wifi_4);
                final EditText editTextPort = dialogView.findViewById(R.id.dialog_wifi_port);
                if (!ipAddress.equals("NULL")) {
                    String[] separated = ipAddress.split("\\.");
                    editText1.setText(separated[0]);
                    editText2.setText(separated[1]);
                    editText3.setText(separated[2]);
                    editText4.setText(separated[3]);
                }
                if (port != 0) {
                    editTextPort.setText(String.format(Locale.getDefault(), "%d", port));
                }
                editText1.addTextChangedListener(new CustomTextWatcher(editText1, editText2));
                editText2.addTextChangedListener(new CustomTextWatcher(editText2, editText3));
                editText3.addTextChangedListener(new CustomTextWatcher(editText3, editText4));
                editText4.addTextChangedListener(new CustomTextWatcher(editText4, editTextPort));
                editTextPort.addTextChangedListener(new CustomTextWatcher(editTextPort, null));
                editText1.setFilters(new InputFilter[]{ new InputFilterMinMax("0", "255")});
                editText2.setFilters(new InputFilter[]{ new InputFilterMinMax("0", "255")});
                editText3.setFilters(new InputFilter[]{ new InputFilterMinMax("0", "255")});
                editText4.setFilters(new InputFilter[]{ new InputFilterMinMax("0", "255")});
                editTextPort.setFilters(new InputFilter[]{ new InputFilterMinMax("0", "65535")});
                builder.setMessage("Adresse IP et port");
                builder.setView(dialogView);
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String part1 = editText1.getText().toString();
                        String part2 = editText2.getText().toString();
                        String part3 = editText3.getText().toString();
                        String part4 = editText4.getText().toString();
                        String partPort = editTextPort.getText().toString();
                        if (!part1.equals("") && !part2.equals("") && !part3.equals("") && !part4.equals("") && !partPort.equals("")) {
                            wifiConfig.getDrawable().setColorFilter(getResources().getColor(R.color.redDark), PorterDuff.Mode.SRC_ATOP);
                            progressBarWifi.setVisibility(View.VISIBLE);
                            ipAddress = part1 + "." + part2 + "." + part3 + "." + part4;
                            port = Integer.parseInt(partPort);
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    // Vérifier si l'IP est la bonne
                                    // --- simule la recherche de l'adresse IP ---
                                    try {
                                        Thread.sleep(2000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    if (TEST || Connection.checkIPAdress(ipAddress, port)) {
                                        wifiConfig.getDrawable().setColorFilter(getResources().getColor(R.color.greenDark), PorterDuff.Mode.SRC_ATOP);
                                        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
                                        SharedPreferences.Editor editor = prefs.edit();
                                        editor.putString("ipAddress", ipAddress);
                                        editor.putInt("port", port);
                                        editor.apply();
                                    } else {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast toast = Toast.makeText(getApplicationContext(), "Erreur lors de la connexion", Toast.LENGTH_LONG);
                                                toast.setGravity(Gravity.CENTER, 0, 0);
                                                toast.show();
                                            }
                                        });
                                    }
                                    progressBarWifi.setVisibility(View.INVISIBLE);
                                }
                            }).start();
                        }
                    }
                });
                builder.setNegativeButton("Retour", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
                builder.create().show();
            }
        });

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                signIn.setClickable(false);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        boolean userIdentityValidated = false;
                        final String userNameString = userNameEditText.getText().toString().trim();

                        // Vérifier si le nom d'utilisateur et le mot de passe sont corrects
                        // --- simule la recherche d'un utilisateur ---
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (TEST || Connection.checkUserAuthorization(userNameString, ipAddress, port)) {
                            userIdentityValidated = true;
                        }

                        if (userIdentityValidated) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = prefs.edit();
                                    editor.putBoolean("isConnected", true);
                                    editor.putString("userName", userNameString);
                                    editor.apply();
                                    ArrayList<String> data = new ArrayList<>();
                                    data.add(userNameString);
                                    Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    progressBar.setVisibility(View.INVISIBLE);
                                    signIn.setClickable(true);
                                    Toast toast = Toast.makeText(getApplicationContext(), "Nom d'utilisateur ou mot de passe incorrect", Toast.LENGTH_LONG);
                                    toast.setGravity(Gravity.CENTER, 0, 0);
                                    toast.show();
                                }
                            });
                        }
                    }
                }).start();

            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    class CustomTextWatcher implements TextWatcher {

        private EditText before;
        private EditText after;

        CustomTextWatcher(EditText before, EditText after) {
            this.before = before;
            this.after = after;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String enteredString = charSequence.toString();
            if (enteredString.startsWith("0")) {
                if (enteredString.length() > 0) {
                    this.before.setText(enteredString.substring(1));
                } else {
                    this.before.setText("");
                }
            }
            if (this.before != null && this.before.getText().toString().length() == 3 && this.after != null) {
                this.after.requestFocus();
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    }

    public class InputFilterMinMax implements InputFilter {

        private int min, max;

        public InputFilterMinMax(int min, int max) {
            this.min = min;
            this.max = max;
        }

        InputFilterMinMax(String min, String max) {
            this.min = Integer.parseInt(min);
            this.max = Integer.parseInt(max);
        }

        private boolean isInRange(int a, int b, int c) {
            return b > a ? c >= a && c <= b : c >= b && c <= a;
        }

        @Override
        public CharSequence filter(CharSequence charSequence, int i, int i1, Spanned spanned, int i2, int i3) {
            try {
                int input = Integer.parseInt(spanned.toString() + charSequence.toString());
                if (isInRange(min, max, input))
                    return null;
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            return "";
        }
    }
}
