package com.example.maddog;

import android.app.DatePickerDialog;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.hbb20.CountryCodePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.text.TextUtils;
import android.util.Patterns;

public class Register extends AppCompatActivity {

    private EditText firstNameField, lastNameField, birthdateField, ageShowText, emailField, contactNumberField;
    private RadioGroup genderRadioGroup;
    private Button toPasswordCreation;
    private CountryCodePicker countryCodePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize views
        firstNameField = findViewById(R.id.firstNameField);
        lastNameField = findViewById(R.id.lastNameField);
        birthdateField = findViewById(R.id.birthdateField);
        ageShowText = findViewById(R.id.ageShowText);
        emailField = findViewById(R.id.emailField);
        contactNumberField = findViewById(R.id.contactNumberField);
        genderRadioGroup = findViewById(R.id.genderRadioGroup);
        toPasswordCreation = findViewById(R.id.toPasswordCreation);
        countryCodePicker = findViewById(R.id.countryCodePicker);

        // Add focus change listeners to validate input fields when they lose focus
        addFocusChangeListener(firstNameField);
        addFocusChangeListener(lastNameField);
        addFocusChangeListener(birthdateField);
        addFocusChangeListener(emailField);
        addFocusChangeListener(contactNumberField);

        // Set up date picker for birthdateField
        birthdateField.setOnClickListener(v -> showDatePickerDialog());

        toPasswordCreation.setEnabled(true);

        // Set up button click listener for validation
        toPasswordCreation.setOnClickListener(v -> {
            if (validateInputs()) {
                // Proceed to next step
                Toast.makeText(Register.this, "All inputs are valid!", Toast.LENGTH_SHORT).show();
                // Intent to the next activity (e.g., Password Creation Activity)
            }
        });
    }

    private void addFocusChangeListener(EditText editText) {
        editText.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                validateField((EditText) v);
                submitButtonEnable();
            }
        });
    }

    private void validateField(EditText field) {
        int fieldId = field.getId();

        if (fieldId == R.id.firstNameField) {
            if (TextUtils.isEmpty(field.getText().toString())) {
                showError(field, "First name is required");
            }
        } else if (fieldId == R.id.lastNameField) {
            if (TextUtils.isEmpty(field.getText().toString())) {
                showError(field, "Last name is required");
            }
        } else if (fieldId == R.id.birthdateField) {
            if (TextUtils.isEmpty(field.getText().toString())) {
                showError(field, "Date of birth is required");
            }
        } else if (fieldId == R.id.emailField) {
            String email = field.getText().toString();
            if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                showError(field, "Please enter a valid email address");
            }
        } else if (fieldId == R.id.contactNumberField) {
            String phoneNumber = field.getText().toString();
            if (TextUtils.isEmpty(phoneNumber) || !Patterns.PHONE.matcher(phoneNumber).matches()) {
                showError(field, "Please enter a valid phone number");
            }
        }
    }

    private void submitButtonEnable() {

        boolean isValid = true;

        if (TextUtils.isEmpty(firstNameField.getText().toString())) {
            isValid = false;
        }

        if (TextUtils.isEmpty(lastNameField.getText().toString())) {
            isValid = false;
        }

        if (genderRadioGroup.getCheckedRadioButtonId() == -1) {
            isValid = false;
        }

        if (TextUtils.isEmpty(birthdateField.getText().toString())) {
            isValid = false;
        }

        String email = emailField.getText().toString();
        if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            isValid = false;
        }

        String phoneNumber = contactNumberField.getText().toString();
        if (TextUtils.isEmpty(phoneNumber) || !Patterns.PHONE.matcher(phoneNumber).matches()) {
            isValid = false;
        }

        toPasswordCreation.setEnabled(isValid);
    }

    private void showError(EditText field, String errorMessage) {
        // Try to get the TextInputLayout, if it's present
        View parent = (View) field.getParent().getParent();
        if (parent instanceof TextInputLayout) {
            TextInputLayout textInputLayout = (TextInputLayout) parent;
            textInputLayout.setError(errorMessage);
            textInputLayout.setErrorTextColor(ColorStateList.valueOf(Color.parseColor("#08141c")));
        } else {
            // If TextInputLayout is not present, set the error directly on the EditText
            field.setError(errorMessage);
        }
    }

    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    updateBirthDate(calendar);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void updateBirthDate(Calendar calendar) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        birthdateField.setText(sdf.format(calendar.getTime()));
        // Calculate and display age
        ageShowText.setText(calculateAge(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)));
    }

    private String calculateAge(int year, int month, int day) {
        Calendar today = Calendar.getInstance();
        int age = today.get(Calendar.YEAR) - year;

        if (today.get(Calendar.MONTH) < month || (today.get(Calendar.MONTH) == month && today.get(Calendar.DAY_OF_MONTH) < day)) {
            age--;
        }

        return String.valueOf(age);
    }

    private boolean validateInputs() {
        boolean isValid = true;

        if (TextUtils.isEmpty(firstNameField.getText().toString())) {
            showError(firstNameField, "First name is required");
            isValid = false;
        }

        if (TextUtils.isEmpty(lastNameField.getText().toString())) {
            showError(lastNameField, "Last name is required");
            isValid = false;
        }

        if (genderRadioGroup.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "Please select a gender", Toast.LENGTH_SHORT).show();
            isValid = false;
        }

        if (TextUtils.isEmpty(birthdateField.getText().toString())) {
            showError(birthdateField, "Date of birth is required");
            isValid = false;
        }

        String email = emailField.getText().toString();
        if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showError(emailField, "Please enter a valid email address");
            isValid = false;
        }

        String phoneNumber = contactNumberField.getText().toString();
        if (TextUtils.isEmpty(phoneNumber) || !Patterns.PHONE.matcher(phoneNumber).matches()) {
            showError(contactNumberField, "Please enter a valid phone number");
            isValid = false;
        }

        toPasswordCreation.setEnabled(isValid);
        return isValid;
    }
}
