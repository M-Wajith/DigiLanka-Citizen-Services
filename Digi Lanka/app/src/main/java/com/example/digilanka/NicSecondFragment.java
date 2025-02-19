package com.example.digilanka;

import android.os.Bundle;
import android.util.Log; // Import Log class
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NicSecondFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NicSecondFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private RadioGroup radioGroupCivil;
    private EditText editProfession;
    private EditText editBirthPlace;
    private EditText editDistrict;
    private EditText editPermanentAddress;
    private EditText editMobileNumber;
    private Button buttonNext;
    private Button backButton;

    // ViewModel instance
    private NicViewModel nicViewModel;

    private static final String TAG = "NicSecondFragment"; // Tag for logging

    public NicSecondFragment() {
        // Required empty public constructor
    }

    public static NicSecondFragment newInstance(String param1, String param2) {
        NicSecondFragment fragment = new NicSecondFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        // Initialize the ViewModel
        nicViewModel = new ViewModelProvider(requireActivity()).get(NicViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_nic_second, container, false);

        // Initialize UI elements
        radioGroupCivil = view.findViewById(R.id.radioGroupCivil);
        editProfession = view.findViewById(R.id.editProfession);
        editBirthPlace = view.findViewById(R.id.editBirthPlace);
        editDistrict = view.findViewById(R.id.editDistrict);
        editPermanentAddress = view.findViewById(R.id.editPermanentAddress);
        editMobileNumber = view.findViewById(R.id.editMobileNumber);
        buttonNext = view.findViewById(R.id.buttonNext);
        backButton = view.findViewById(R.id.backButton);

        // Set up listeners
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleNextButtonClick();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle back button click
                getActivity().onBackPressed();
            }
        });

        return view;
    }

    private void handleNextButtonClick() {
        // Get selected civil status
        int selectedId = radioGroupCivil.getCheckedRadioButtonId();
        RadioButton selectedRadioButton = getView().findViewById(selectedId);

        String civilStatus = (selectedRadioButton != null) ? selectedRadioButton.getText().toString() : "";
        String profession = editProfession.getText().toString().trim();
        String birthPlace = editBirthPlace.getText().toString().trim();
        String district = editDistrict.getText().toString().trim();
        String permanentAddress = editPermanentAddress.getText().toString().trim();
        String mobileNumber = editMobileNumber.getText().toString().trim();

        // Log the values before validation
        Log.d(TAG, "Civil Status: " + civilStatus);
        Log.d(TAG, "Profession: " + profession);
        Log.d(TAG, "Birth Place: " + birthPlace);
        Log.d(TAG, "District: " + district);
        Log.d(TAG, "Permanent Address: " + permanentAddress);
        Log.d(TAG, "Mobile Number: " + mobileNumber);

        // Validate inputs
        if (validateInputs(civilStatus, profession, birthPlace, district, permanentAddress, mobileNumber)) {
            // Save data to ViewModel
            nicViewModel.setCivilStatus(civilStatus);
            nicViewModel.setProfession(profession);
            nicViewModel.setBirthPlace(birthPlace);
            nicViewModel.setDistrict(district);
            nicViewModel.setPermanentAddress(permanentAddress);
            nicViewModel.setMobileNumber(mobileNumber);

            // Log the data saved to ViewModel
            Log.d(TAG, "Data saved to ViewModel: " + nicViewModel.toString());

            // Proceed to the next fragment or activity
            Toast.makeText(getActivity(), "Data saved! Proceeding...", Toast.LENGTH_SHORT).show();
            // TODO: Implement navigation logic here
            // Navigate to the next fragment
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container_nic, new NicThirdFragment());
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }

    private boolean validateInputs(String civilStatus, String profession, String birthPlace, String district, String permanentAddress, String mobileNumber) {
        if (civilStatus.isEmpty() || profession.isEmpty() || birthPlace.isEmpty() ||
                district.isEmpty() || permanentAddress.isEmpty() || mobileNumber.isEmpty()) {
            Toast.makeText(getActivity(), "Please fill in all fields.", Toast.LENGTH_SHORT).show();
            return false;
        }
        // Additional validation for fields like mobile number format can be added here
        return true;
    }
}
