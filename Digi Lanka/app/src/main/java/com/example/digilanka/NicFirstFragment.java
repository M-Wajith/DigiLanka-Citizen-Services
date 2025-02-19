package com.example.digilanka;

import android.os.Bundle;
import android.util.Log; // Import Log class
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class NicFirstFragment extends Fragment {

    // UI Elements
    private EditText editName;
    private EditText editSurname;
    private EditText editDateOfBirth;
    private EditText editBirthCertNumber;
    private EditText editOldNic; // New variable for Old Nic Number
    private RadioGroup radioGroupGender;

    private NicViewModel nicViewModel;

    public NicFirstFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize ViewModel here
        nicViewModel = new ViewModelProvider(requireActivity()).get(NicViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_nic_first, container, false);

        // Initialize UI elements
        editName = view.findViewById(R.id.editName);
        editSurname = view.findViewById(R.id.editSurname);
        editDateOfBirth = view.findViewById(R.id.editDateOfBirth);
        editBirthCertNumber = view.findViewById(R.id.editBirthCertNumber);
        editOldNic = view.findViewById(R.id.editOldNic); // Initialize the Old Nic Number EditText
        radioGroupGender = view.findViewById(R.id.radioGroupGender);

        Button buttonNext = view.findViewById(R.id.buttonNext);
        Button backButton = view.findViewById(R.id.backButton);

        // Set up button listeners
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleNextButtonClick();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleBackButtonClick();
            }
        });

        return view;
    }

    private void handleNextButtonClick() {
        String name = editName.getText().toString().trim();
        String surname = editSurname.getText().toString().trim();
        String dateOfBirth = editDateOfBirth.getText().toString().trim();
        String birthCertNumber = editBirthCertNumber.getText().toString().trim();
        String oldNic = editOldNic.getText().toString().trim(); // Get the Old Nic Number
        int selectedGenderId = radioGroupGender.getCheckedRadioButtonId();

        // Check for required fields
        if (name.isEmpty()) {
            Toast.makeText(getContext(), "Please enter your name", Toast.LENGTH_SHORT).show();
            return;
        }
        if (surname.isEmpty()) {
            Toast.makeText(getContext(), "Please enter your surname", Toast.LENGTH_SHORT).show();
            return;
        }
        if (dateOfBirth.isEmpty()) {
            Toast.makeText(getContext(), "Please enter your date of birth", Toast.LENGTH_SHORT).show();
            return;
        }
        if (birthCertNumber.isEmpty()) {
            Toast.makeText(getContext(), "Please enter your birth certificate number", Toast.LENGTH_SHORT).show();
            return;
        }
        if (oldNic.isEmpty()) { // Check if Old Nic Number is empty
            Toast.makeText(getContext(), "Please enter your Old Nic Number", Toast.LENGTH_SHORT).show();
            return;
        }
        if (selectedGenderId == -1) {
            Toast.makeText(getContext(), "Please select your gender", Toast.LENGTH_SHORT).show();
            return;
        }

        String gender = "";
        if (selectedGenderId == R.id.radioMale) {
            gender = "Male";
        } else if (selectedGenderId == R.id.radioFemale) {
            gender = "Female";
        }

        // Log the data being set to ViewModel
        Log.d("NicFirstFragment", "Name: " + name);
        Log.d("NicFirstFragment", "Surname: " + surname);
        Log.d("NicFirstFragment", "Date of Birth: " + dateOfBirth);
        Log.d("NicFirstFragment", "Birth Certificate Number: " + birthCertNumber);
        Log.d("NicFirstFragment", "Old NIC: " + oldNic);
        Log.d("NicFirstFragment", "Gender: " + gender);

        // Store data in ViewModel
        nicViewModel.setName(name);
        nicViewModel.setSurname(surname);
        nicViewModel.setDateOfBirth(dateOfBirth);
        nicViewModel.setBirthCertNumber(birthCertNumber);
        nicViewModel.setOldNic(oldNic); // Store the Old Nic Number in ViewModel
        nicViewModel.setGender(gender);

        // Navigate to the next fragment
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container_nic, new NicSecondFragment());
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void handleBackButtonClick() {
        // Handle back button click logic
        getParentFragmentManager().popBackStack();
    }
}
