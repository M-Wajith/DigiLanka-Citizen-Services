package com.example.digilanka;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import android.util.Log;

public class NicViewModel extends ViewModel {

    private static final String TAG = "NicViewModel";

    // Fields for NicFirstFragment
    private final MutableLiveData<String> name = new MutableLiveData<>();
    private final MutableLiveData<String> surname = new MutableLiveData<>();
    private final MutableLiveData<String> dateOfBirth = new MutableLiveData<>();
    private final MutableLiveData<String> birthCertNumber = new MutableLiveData<>();
    private final MutableLiveData<String> gender = new MutableLiveData<>();
    private final MutableLiveData<String> oldNic = new MutableLiveData<>(); // Old NIC field

    // Fields for NicSecondFragment
    private final MutableLiveData<String> civilStatus = new MutableLiveData<>();
    private final MutableLiveData<String> profession = new MutableLiveData<>();
    private final MutableLiveData<String> birthPlace = new MutableLiveData<>();
    private final MutableLiveData<String> district = new MutableLiveData<>();
    private final MutableLiveData<String> permanentAddress = new MutableLiveData<>();
    private final MutableLiveData<String> mobileNumber = new MutableLiveData<>();

    // Fields for NicThirdFragment (Updated for image handling)
    private final MutableLiveData<String> email = new MutableLiveData<>(); // Add email field
    private final MutableLiveData<String> photoReceipt = new MutableLiveData<>();
    private final MutableLiveData<String> birthCertFront = new MutableLiveData<>();
    private final MutableLiveData<String> birthCertBack = new MutableLiveData<>();
    private final MutableLiveData<String> policeComplaint = new MutableLiveData<>();

    // Getters and Setters for NicFirstFragment fields
    public LiveData<String> getName() {
        return name;
    }
    public void setName(String name) {
        this.name.setValue(name);
        Log.d(TAG, "Name set: " + name);
    }

    public LiveData<String> getSurname() {
        return surname;
    }
    public void setSurname(String surname) {
        this.surname.setValue(surname);
        Log.d(TAG, "Surname set: " + surname);
    }

    public LiveData<String> getDateOfBirth() {
        return dateOfBirth;
    }
    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth.setValue(dateOfBirth);
        Log.d(TAG, "Date of Birth set: " + dateOfBirth);
    }

    public LiveData<String> getBirthCertNumber() {
        Log.d(TAG, "Date of Birth set: " + dateOfBirth);
        return birthCertNumber;
    }
    public void setBirthCertNumber(String birthCertNumber) {
        this.birthCertNumber.setValue(birthCertNumber);
        Log.d(TAG, "Birth Certificate Number set: " + birthCertNumber);
    }

    public LiveData<String> getOldNic() {
        return oldNic;
    }
    public void setOldNic(String oldNic) {
        this.oldNic.setValue(oldNic);
        Log.d(TAG, "Old NIC set: " + oldNic);
    }

    public LiveData<String> getGender() {
        return gender;
    }
    public void setGender(String gender) {
        this.gender.setValue(gender);
        Log.d(TAG, "Gender set: " + gender);
    }

    // Getters and Setters for NicSecondFragment fields
    public LiveData<String> getCivilStatus() {
        return civilStatus;
    }
    public void setCivilStatus(String civilStatus) {
        this.civilStatus.setValue(civilStatus);
        Log.d(TAG, "Civil Status set: " + civilStatus);
    }

    public LiveData<String> getProfession() {
        return profession;
    }
    public void setProfession(String profession) {
        this.profession.setValue(profession);
        Log.d(TAG, "Profession set: " + profession);
    }

    public LiveData<String> getBirthPlace() {
        return birthPlace;
    }
    public void setBirthPlace(String birthPlace) {
        this.birthPlace.setValue(birthPlace);
        Log.d(TAG, "Birth Place set: " + birthPlace);
    }

    public LiveData<String> getDistrict() {
        return district;
    }
    public void setDistrict(String district) {
        this.district.setValue(district);
        Log.d(TAG, "District set: " + district);
    }

    public LiveData<String> getPermanentAddress() {
        return permanentAddress;
    }
    public void setPermanentAddress(String permanentAddress) {
        this.permanentAddress.setValue(permanentAddress);
        Log.d(TAG, "Permanent Address set: " + permanentAddress);
    }

    public LiveData<String> getMobileNumber() {
        return mobileNumber;
    }
    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber.setValue(mobileNumber);
        Log.d(TAG, "Mobile Number set: " + mobileNumber);
    }

    // Getters and Setters for NicThirdFragment fields (Updated for image handling)
    public LiveData<String> getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email.setValue(email);
        Log.d(TAG, "Email set: " + email);
    }

    public LiveData<String> getPhotoReceipt() {
        return photoReceipt;
    }
    public void setPhotoReceipt(String photoReceipt) {
        this.photoReceipt.setValue(photoReceipt);
        Log.d(TAG, "Photo Receipt set: " + photoReceipt);
    }

    public LiveData<String> getBirthCertFront() {
        return birthCertFront;
    }
    public void setBirthCertFront(String birthCertFront) {
        this.birthCertFront.setValue(birthCertFront);
        Log.d(TAG, "Birth Certificate Front set: " + birthCertFront);
    }

    public LiveData<String> getBirthCertBack() {
        return birthCertBack;
    }
    public void setBirthCertBack(String birthCertBack) {
        this.birthCertBack.setValue(birthCertBack);
        Log.d(TAG, "Birth Certificate Back set: " + birthCertBack);
    }

    public LiveData<String> getPoliceComplaint() {
        return policeComplaint;
    }
    public void setPoliceComplaint(String policeComplaint) {
        this.policeComplaint.setValue(policeComplaint);
        Log.d(TAG, "Police Complaint set: " + policeComplaint);
    }

    public NicData getNicData() {
        return new NicData(
                name.getValue(),
                surname.getValue(),
                dateOfBirth.getValue(),
                birthCertNumber.getValue(),
                gender.getValue(),
                oldNic.getValue(),
                civilStatus.getValue(),
                profession.getValue(),
                birthPlace.getValue(),
                district.getValue(),
                permanentAddress.getValue(),
                mobileNumber.getValue(),
                email.getValue(),
                photoReceipt.getValue() != null ? photoReceipt.getValue() : null,
                birthCertFront.getValue() != null ? birthCertFront.getValue() : null,
                birthCertBack.getValue() != null ? birthCertBack.getValue() : null,
                policeComplaint.getValue() != null ? policeComplaint.getValue() : null
        );
    }
}
