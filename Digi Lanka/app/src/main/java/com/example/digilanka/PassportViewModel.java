package com.example.digilanka;

import android.net.Uri;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PassportViewModel extends ViewModel {
    private final MutableLiveData<String> nationalID = new MutableLiveData<>();
    private final MutableLiveData<String> travelDocument = new MutableLiveData<>();
    private final MutableLiveData<String> surname = new MutableLiveData<>();
    private final MutableLiveData<String> otherName = new MutableLiveData<>();
    private final MutableLiveData<String> nicNumber = new MutableLiveData<>();
    private final MutableLiveData<String> permanentAddress = new MutableLiveData<>();
    private final MutableLiveData<String> district = new MutableLiveData<>();
    private final MutableLiveData<String> dateOfBirth = new MutableLiveData<>();
    private final MutableLiveData<String> birthCertificateNumber = new MutableLiveData<>();
    private final MutableLiveData<String> gender = new MutableLiveData<>();
    private final MutableLiveData<String> profession = new MutableLiveData<>();
    private final MutableLiveData<String> dualCitizenshipNumber = new MutableLiveData<>();
    private final MutableLiveData<String> phoneNumber = new MutableLiveData<>();
    private final MutableLiveData<String> emailAddress = new MutableLiveData<>();
    private final MutableLiveData<Uri> nicFrontImage = new MutableLiveData<>();
    private final MutableLiveData<Uri> nicBackImage = new MutableLiveData<>();
    private final MutableLiveData<Uri> birthCertificateFront = new MutableLiveData<>();
    private final MutableLiveData<Uri> birthCertificateBack = new MutableLiveData<>();
    private final MutableLiveData<Uri> photoReceiptImage = new MutableLiveData<>();

    // Getters for LiveData
    public LiveData<String> getNationalID() { return nationalID; }
    public LiveData<String> getTravelDocument() { return travelDocument; }
    public LiveData<String> getSurname() { return surname; }
    public LiveData<String> getOtherName() { return otherName; }
    public LiveData<String> getNicNumber() { return nicNumber; }
    public LiveData<String> getPermanentAddress() { return permanentAddress; }
    public LiveData<String> getDistrict() { return district; }
    public LiveData<String> getDateOfBirth() { return dateOfBirth; }
    public LiveData<String> getBirthCertificateNumber() { return birthCertificateNumber; }
    public LiveData<String> getGender() { return gender; }
    public LiveData<String> getProfession() { return profession; }
    public LiveData<String> getDualCitizenshipNumber() { return dualCitizenshipNumber; }
    public LiveData<String> getPhoneNumber() { return phoneNumber; }
    public LiveData<String> getEmailAddress() { return emailAddress; }
    public LiveData<Uri> getNicFrontImage() { return nicFrontImage; }
    public LiveData<Uri> getNicBackImage() { return nicBackImage; }
    public LiveData<Uri> getBirthCertificateFront() { return birthCertificateFront; }
    public LiveData<Uri> getBirthCertificateBack() { return birthCertificateBack; }
    public LiveData<Uri> getPhotoReceiptImage() { return photoReceiptImage; }

    // Setters for updating values
    public void setNationalID(String nationalID) { this.nationalID.setValue(nationalID); }
    public void setTravelDocument(String travelDocument) { this.travelDocument.setValue(travelDocument); }
    public void setSurname(String surname) { this.surname.setValue(surname); }
    public void setOtherName(String otherName) { this.otherName.setValue(otherName); }
    public void setNicNumber(String nicNumber) { this.nicNumber.setValue(nicNumber); }
    public void setPermanentAddress(String permanentAddress) { this.permanentAddress.setValue(permanentAddress); }
    public void setDistrict(String district) { this.district.setValue(district); }
    public void setDateOfBirth(String dateOfBirth) { this.dateOfBirth.setValue(dateOfBirth); }
    public void setBirthCertificateNumber(String birthCertificateNumber) { this.birthCertificateNumber.setValue(birthCertificateNumber); }
    public void setGender(String gender) { this.gender.setValue(gender); }
    public void setProfession(String profession) { this.profession.setValue(profession); }
    public void setDualCitizenshipNumber(String dualCitizenshipNumber) { this.dualCitizenshipNumber.setValue(dualCitizenshipNumber); }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber.setValue(phoneNumber); }
    public void setEmailAddress(String emailAddress) { this.emailAddress.setValue(emailAddress); }
    public void setNicFrontImage(Uri uri) { this.nicFrontImage.setValue(uri); }
    public void setNicBackImage(Uri uri) { this.nicBackImage.setValue(uri); }
    public void setBirthCertificateFront(Uri uri) { this.birthCertificateFront.setValue(uri); }
    public void setBirthCertificateBack(Uri uri) { this.birthCertificateBack.setValue(uri); }
    public void setPhotoReceiptImage(Uri uri) { this.photoReceiptImage.setValue(uri); }

    // Method to get all passport data as a PassportData object
    public PassportData getPassportData() {
        return new PassportData(
                nationalID.getValue(),
                surname.getValue(),
                otherName.getValue(),
                permanentAddress.getValue(),
                district.getValue(),
                travelDocument.getValue(),
                dateOfBirth.getValue(),
                birthCertificateNumber.getValue(),
                profession.getValue(),
                dualCitizenshipNumber.getValue(),
                gender.getValue(),
                phoneNumber.getValue(),
                emailAddress.getValue(),
                nicFrontImage.getValue() != null ? nicFrontImage.getValue().toString() : null,
                nicBackImage.getValue() != null ? nicBackImage.getValue().toString() : null,
                birthCertificateFront.getValue() != null ? birthCertificateFront.getValue().toString() : null,
                birthCertificateBack.getValue() != null ? birthCertificateBack.getValue().toString() : null,
                photoReceiptImage.getValue() != null ? photoReceiptImage.getValue().toString() : null
        );
    }
}
