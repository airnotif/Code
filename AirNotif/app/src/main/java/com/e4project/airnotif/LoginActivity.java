package com.e4project.airnotif;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
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

import static android.app.Notification.VISIBILITY_PUBLIC;

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

        final ImageButton notif = findViewById(R.id.login_notif);
        notif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        createNotification(null, null, "Nouvelle notification");
                    }
                }).start();
            }
        });

        final SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        ipAddress = prefs.getString("ipAddress", "NULL");
        port = prefs.getInt("port", 0);
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (TEST || Connection.checkIPAdress(ipAddress, port)) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            wifiConfig.getDrawable().setColorFilter(getResources().getColor(R.color.greenDark), PorterDuff.Mode.SRC_ATOP);
                        }
                    });
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
                editText1.setFilters(new InputFilter[]{new InputFilterMinMax("0", "255")});
                editText2.setFilters(new InputFilter[]{new InputFilterMinMax("0", "255")});
                editText3.setFilters(new InputFilter[]{new InputFilterMinMax("0", "255")});
                editText4.setFilters(new InputFilter[]{new InputFilterMinMax("0", "255")});
                editTextPort.setFilters(new InputFilter[]{new InputFilterMinMax("0", "65535")});
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
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    wifiConfig.getDrawable().setColorFilter(getResources().getColor(R.color.redDark), PorterDuff.Mode.SRC_ATOP);
                                    progressBarWifi.setVisibility(View.VISIBLE);
                                }
                            });
                            ipAddress = part1 + "." + part2 + "." + part3 + "." + part4;
                            port = Integer.parseInt(partPort);
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    if (TEST || Connection.checkIPAdress(ipAddress, port)) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                wifiConfig.getDrawable().setColorFilter(getResources().getColor(R.color.greenDark), PorterDuff.Mode.SRC_ATOP);
                                            }
                                        });
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
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            progressBarWifi.setVisibility(View.INVISIBLE);
                                        }
                                    });
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

                        Connection.openConnection(ipAddress, port);

                        if (TEST || Connection.checkUserAuthorization(userNameString)) {
                            userIdentityValidated = true;
                        } else{
                            Connection.closeConnection();
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

    private void createNotification(String contentTitle, String contentText, String subText) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        String NOTIFICATION_CHANNEL_ID = "my_channel_id_01";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My Notifications", NotificationManager.IMPORTANCE_DEFAULT);

            // Configure the notification channel.
            notificationChannel.setDescription("Channel description");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{0, 500, 300, 500, 300, 500, 300, 500});
            notificationManager.createNotificationChannel(notificationChannel);
        }

        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);

        notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_airnotif_logo_48x32)
                .setPriority(Notification.PRIORITY_MAX)
                .setContentTitle(contentTitle)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(contentText))
                .setContentText(contentText)
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(pendingIntent)
                .setSubText(subText)
                .setVisibility(VISIBILITY_PUBLIC);

        notificationManager.notify(/*notification id*/1, notificationBuilder.build());
    }
}
