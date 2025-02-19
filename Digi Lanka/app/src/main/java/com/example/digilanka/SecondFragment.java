package com.example.digilanka;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

public class SecondFragment extends Fragment {
    private Button buttonNext;
    private Button buttonBack;
    private EditText editDateOfBirth, editBirthCertNumber, editProfession, editDualCitizenNumber;
    private RadioGroup radioGroupDualCitizen, radioGroupGender;
    private RadioButton radioYes, radioNo, radioMale, radioFemale;

    // ViewModel reference to store and access data
    private PassportViewModel passportViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_second, container, false);

        // Get references to UI elements
        buttonNext = view.findViewById(R.id.buttonNext);
        buttonBack = view.findViewById(R.id.backButton);
        editDateOfBirth = view.findViewById(R.id.editDateOfBirth);
        editBirthCertNumber = view.findViewById(R.id.editBirthCertNumber);
        editProfession = view.findViewById(R.id.editProfession);
        editDualCitizenNumber = view.findViewById(R.id.editDualCitizenNumber);
        radioGroupDualCitizen = view.findViewById(R.id.radioGroupDualCitizen);
        radioYes = view.findViewById(R.id.radioYes);
        radioNo = view.findViewById(R.id.radioNo);

        // Gender RadioGroup and RadioButtons
        radioGroupGender = view.findViewById(R.id.radioGroupGender);
        radioMale = view.findViewById(R.id.radioMale);
        radioFemale = view.findViewById(R.id.radioFemale);

        // Initialize ViewModel
        passportViewModel = new ViewModelProvider(requireActivity()).get(PassportViewModel.class);

        // Handle RadioGroup selection to enable/disable editDualCitizenNumber
        radioGroupDualCitizen.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radioYes) {
                editDualCitizenNumber.setEnabled(true);
                editDualCitizenNumber.setText(""); // Clear any previous text
            } else if (checkedId == R.id.radioNo) {
                editDualCitizenNumber.setEnabled(false);
                editDualCitizenNumber.setText(""); // Clear text if 'No' is selected
            }
        });

        // Next button click listener with validation and saving data to ViewModel
        buttonNext.setOnClickListener(v -> {
            if (validateInputs()) {
                // Store validated inputs into ViewModel
                saveDataToViewModel();

                // Navigate to the next fragment
                ((PassportActivity) getActivity()).navigateToThirdFragment();
            }
        });

        // Back button click listener
        buttonBack.setOnClickListener(v -> ((PassportActivity) getActivity()).navigateFirstFragment());

        return view;
    }

    // Method to validate all inputs
    private boolean validateInputs() {
        boolean isValid = true; // Flag to track validation status

        // Validate Date of Birth
        if (TextUtils.isEmpty(editDateOfBirth.getText())) {
            editDateOfBirth.setError("Date of Birth is required");
            isValid = false;
        }

        // Validate Birth Certificate Number
        if (TextUtils.isEmpty(editBirthCertNumber.getText())) {
            editBirthCertNumber.setError("Birth Certificate Number is required");
            isValid = false;
        }

        // Validate Profession
        if (TextUtils.isEmpty(editProfession.getText())) {
            editProfession.setError("Profession is required");
            isValid = false;
        }

        // Validate Radio Group selection for Dual Citizenship
        int selectedDualCitizenId = radioGroupDualCitizen.getCheckedRadioButtonId();
        if (selectedDualCitizenId == -1) {
            Toast.makeText(getActivity(), "Please select if you have obtained Dual Citizenship", Toast.LENGTH_SHORT).show();
            isValid = false;
        }

        // If 'Yes' is selected, check if Dual Citizen Number is provided
        if (selectedDualCitizenId == R.id.radioYes && TextUtils.isEmpty(editDualCitizenNumber.getText())) {
            editDualCitizenNumber.setError("Dual Citizen Number is required");
            isValid = false;
        }

        // Validate Gender selection
        int selectedGenderId = radioGroupGender.getCheckedRadioButtonId();
        if (selectedGenderId == -1) {
            Toast.makeText(getActivity(), "Please select your gender", Toast.LENGTH_SHORT).show();
            isValid = false;
        }

        // Display a single toast message if there are validation errors
        if (!isValid) {
            Toast.makeText(getActivity(), "Please fill in all required fields.", Toast.LENGTH_SHORT).show();
        }

        return isValid; // Return validation status
    }

    // Save data to ViewModel
    private void saveDataToViewModel() {
        // Store data in PassportViewModel
        passportViewModel.setDateOfBirth(editDateOfBirth.getText().toString());
        passportViewModel.setBirthCertificateNumber(editBirthCertNumber.getText().toString());
        passportViewModel.setProfession(editProfession.getText().toString());

        // Check if Dual Citizenship is selected and save the value
        if (radioGroupDualCitizen.getCheckedRadioButtonId() == R.id.radioYes) {
            passportViewModel.setDualCitizenshipNumber(editDualCitizenNumber.getText().toString());
        } else {
            passportViewModel.setDualCitizenshipNumber(null); // Set null if no Dual Citizenship
        }

        // Save the selected gender to the ViewModel
        int selectedGenderId = radioGroupGender.getCheckedRadioButtonId();
        if (selectedGenderId == R.id.radioMale) {
            passportViewModel.setGender("Male");
        } else if (selectedGenderId == R.id.radioFemale) {
            passportViewModel.setGender("Female");
        }
    }
}
