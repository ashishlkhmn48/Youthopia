package com.ashishlakhmani.youthopia.fragment;


import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.ashishlakhmani.youthopia.classes.FormValidationBackground;
import com.ashishlakhmani.youthopia.R;


public class CommonRegistrationFragment extends Fragment {

    public String gender;
    public TextView eventNameText, nameText, emailText;
    public EditText phno, numOfMembers, collegeName;
    public RadioButton maleRadio, femaleRadio;
    Button submitButton;


    private void initializeFields(View view) {
        eventNameText = (TextView) view.findViewById(R.id.eventNameText);
        nameText = (TextView) view.findViewById(R.id.nameText);
        emailText = (TextView) view.findViewById(R.id.email);
        phno = (EditText) view.findViewById(R.id.phnoText);
        numOfMembers = (EditText) view.findViewById(R.id.numberText);
        collegeName = (EditText) view.findViewById(R.id.collegeNameText);
        submitButton = (Button) view.findViewById(R.id.submitButton);
        maleRadio = (RadioButton) view.findViewById(R.id.maleRadio);
        femaleRadio = (RadioButton) view.findViewById(R.id.femaleRadio);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_common_registration, container, false);
        initializeFields(view);
        SharedPreferences sp = getContext().getSharedPreferences("login", Context.MODE_PRIVATE);
        eventNameText.setText(getArguments().getString("eventName").toUpperCase());
        nameText.setText(sp.getString("name", null).toUpperCase());
        emailText.setText(sp.getString("email", null));


        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (maleRadio.isChecked())
                    gender = "Male";
                else if (femaleRadio.isChecked())
                    gender = "Female";
                else
                    gender = "";

                final String eventName = eventNameText.getText().toString();
                final String name = nameText.getText().toString();
                final String email = emailText.getText().toString();
                final String numOfMembersText = numOfMembers.getText().toString();
                final String phnoText = phno.getText().toString();
                final String collegeNameText = collegeName.getText().toString();

                if (!phnoText.trim().isEmpty()) {
                    if (validatePhno(phnoText)) {
                        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext(),R.style.AppCompatAlertDialogStyle);
                        alertDialogBuilder.setTitle("Warning..!!!");
                        alertDialogBuilder.setIcon(R.drawable.warning);
                        alertDialogBuilder.setMessage("Check your details carefully.You can't register twice..!!!\nAre you sure?");
                        alertDialogBuilder.setCancelable(true);
                        alertDialogBuilder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                              dialog.dismiss();
                            }
                        });
                        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                FormValidationBackground formValidationBackground = new FormValidationBackground(getContext());
                                formValidationBackground.execute(eventName, name, gender, numOfMembersText, email, phnoText, collegeNameText);
                            }
                        });

                        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });

                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                    } else
                        Toast.makeText(getContext(), "Check you entries", Toast.LENGTH_SHORT).show();

                } else
                    Toast.makeText(getContext(), "Fill Required Fields", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }


    /*private boolean validateName(CharSequence s) {
        if (s.toString().trim().isEmpty()) {
            name.setError("Blank Entry..!!");
            name.requestFocus();
            return false;
        } else if (s.toString().length() > 40) {
            name.setError("Name too Large..");
            name.requestFocus();
            return false;
        } else
            return true;
    }*/

    /*private boolean validateEmail(CharSequence s) {
        int atposition = s.toString().indexOf("@");
        int dotposition = s.toString().lastIndexOf(".");
        if (atposition < 1 || dotposition < atposition + 2 || dotposition + 2 >= s.length() || s.toString().contains(" ")) {
            email.setError("Invalid Email..!!");
            email.requestFocus();
            return false;
        } else if (s.toString().length() > 40) {
            name.setError("Email too Large..");
            name.requestFocus();
            return false;
        } else
            return true;
    }*/

    private boolean validatePhno(CharSequence s) {
        if (s.toString().trim().length() == 10 &&
                (s.toString().startsWith("7") ||
                        s.toString().startsWith("8") ||
                        s.toString().startsWith("9")) && isNumber(s))
            return true;
        else {
            phno.setError("Invalid Phone Number..!!");
            phno.requestFocus();
            return false;
        }
    }

    public boolean isNumber(CharSequence s) {
        for (int i = 0; i < s.toString().length(); i++) {
            if (!(s.toString().charAt(i) >= 48 && s.toString().charAt(i) <= 57))
                return false;
        }
        return true;
    }
}
