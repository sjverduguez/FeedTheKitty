package com.example.stephanie.feedthekitty;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private EditText email;
    private EditText firstName;
    private EditText lastName;

    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = (EditText) findViewById(R.id.email);
        firstName = (EditText) findViewById(R.id.firstName);
        lastName = (EditText) findViewById(R.id.lastName);

        loginButton = (Button) findViewById(R.id.login_button);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (email.getText().toString().matches("")||
                        firstName.getText().toString().matches("") || lastName.getText().toString().matches("")) {
                    Toast.makeText(getApplicationContext(), "Please complete all fields",Toast.LENGTH_SHORT).show();
                } else {

                    WePay.createUser(getApplicationContext(), email.getText().toString(), firstName.getText().toString(),
                            lastName.getText().toString());

                    Intent intent = new Intent(LoginActivity.this, CreateEventActivity.class);
                    startActivity(intent);

                }

            }
        });




    }
}
