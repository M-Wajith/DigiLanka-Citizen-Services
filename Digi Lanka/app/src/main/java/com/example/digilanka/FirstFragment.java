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

public class FirstFragment extends Fragment {

    private Button buttonNext;
    private Button buttonBack;
    private EditText editNationalID, editSurname, editOtherName, editPermanentAddress, editDistrict;
    private RadioGroup radioGroupTravelDoc;
    private PassportViewModel passportViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_first, container, false);

        // Initialize the ViewModel
        passportViewModel = new ViewModelProvider(requireActivity()).get(PassportViewModel.class);

        // Get references to input fields
        buttonNext = view.findViewById(R.id.buttonNext);
        buttonBack = view.findViewById(R.id.backButton);
        editNationalID = view.findViewById(R.id.editNationalID);
        editSurname = view.findViewById(R.id.editSurname);
        editOtherName = view.findViewById(R.id.editOtherName);
        editPermanentAddress = view.findViewById(R.id.editPermanentAddress);
        editDistrict = view.findViewById(R.id.editDistrict);
        radioGroupTravelDoc = view.findViewById(R.id.radioGroupTravelDoc);

        // Populate fields with existing data from ViewModel if available
        populateFields();

        // Next button click listener
        buttonNext.setOnClickListener(v -> {
            if (validateInputs()) {
                // Save data to ViewModel
                saveDataToViewModel();

                // Navigate to the second fragment only if validation passes
                ((PassportActivity) getActivity()).navigateToSecondFragment();
            }
        });

        // Back button click listener
        buttonBack.setOnClickListener(v -> requireActivity().finish());

        return view;
    }

    // Populate fields with existing data from ViewModel
    private void populateFields() {
        if (passportViewModel.getNicNumber().getValue() != null) {
            editNationalID.setText(passportViewModel.getNicNumber().getValue());
        }
        if (passportViewModel.getSurname().getValue() != null) {
            editSurname.setText(passportViewModel.getSurname().getValue());
        }
        if (passportViewModel.getOtherName().getValue() != null) {
            editOtherName.setText(passportViewModel.getOtherName().getValue());
        }
        if (passportViewModel.getPermanentAddress().getValue() != null) {
            editPermanentAddress.setText(passportViewModel.getPermanentAddress().getValue());
        }
        if (passportViewModel.getDistrict().getValue() != null) {
            editDistrict.setText(passportViewModel.getDistrict().getValue());
        }
        if (passportViewModel.getTravelDocument().getValue() != null) {
            int selectedRadioButtonId = getTravelDocumentRadioButtonId(passportViewModel.getTravelDocument().getValue());
            if (selectedRadioButtonId != -1) {
                radioGroupTravelDoc.check(selectedRadioButtonId);
            }
        }
    }

    // Method to save data to ViewModel
    private void saveDataToViewModel() {
        passportViewModel.setNicNumber(editNationalID.getText().toString().trim());
        passportViewModel.setSurname(editSurname.getText().toString().trim());
        passportViewModel.setOtherName(editOtherName.getText().toString().trim());
        passportViewModel.setPermanentAddress(editPermanentAddress.getText().toString().trim());
        passportViewModel.setDistrict(editDistrict.getText().toString().trim());

        // Get the selected travel document type
        int selectedId = radioGroupTravelDoc.getCheckedRadioButtonId();
        if (selectedId != -1) {
            RadioButton selectedRadioButton = getView().findViewById(selectedId);
            passportViewModel.setTravelDocument(selectedRadioButton.getText().toString());
        }
    }

    // Method to validate all inputs
    private boolean validateInputs() {
        boolean isValid = true;

        // Check each field and set error if empty
        if (TextUtils.isEmpty(editNationalID.getText())) {
            editNationalID.setError("National Identity Card is required");
            isValid = false;
        }
        if (TextUtils.isEmpty(editSurname.getText())) {
            editSurname.setError("Surname is required");
            isValid = false;
        }
        if (TextUtils.isEmpty(editOtherName.getText())) {
            editOtherName.setError("Other Name is required");
            isValid = false;
        }
        if (TextUtils.isEmpty(editPermanentAddress.getText())) {
            editPermanentAddress.setError("Permanent Address is required");
            isValid = false;
        }
        if (TextUtils.isEmpty(editDistrict.getText())) {
            editDistrict.setError("District is required");
            isValid = false;
        }
        if (radioGroupTravelDoc.getCheckedRadioButtonId() == -1) {
            Toast.makeText(getActivity(), "Please select a travel document type", Toast.LENGTH_SHORT).show();
            isValid = false;
        }

        // Return true only if all fields are valid
        return isValid;
    }

    // Get the RadioButton ID based on the travel document type stored in ViewModel
    private int getTravelDocumentRadioButtonId(String travelDocument) {
        switch (travelDocument.toLowerCase()) {
            case "all countries":
                return R.id.radioAllCountries;
            case "middle east countries":
                return R.id.radioMiddleEast;
            case "emergency certificate":
                return R.id.radioEmergency;
            case "identity management":
                return R.id.radioIdentity;
            default:
                return -1;
        }
    }

}
