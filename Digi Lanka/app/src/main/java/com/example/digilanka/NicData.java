package com.example.digilanka;

public class NicData {

    // Fields for NicFirstFragment
    private String name;
    private String surname;
    private String dateOfBirth;
    private String birthCertNumber;
    private String gender;
    private String oldNic; // Old NIC field

    // Fields for NicSecondFragment
    private String civilStatus;
    private String profession;
    private String birthPlace;
    private String district;
    private String permanentAddress;
    private String mobileNumber;

    // Fields for NicThirdFragment (Updated for image handling)
    private String email; // Email field
    private String photoReceipt;
    private String birthCertFront;
    private String birthCertBack;
    private String policeComplaint;

    // Default Constructor
    public NicData() {
    }

    // Parameterized Constructor
    public NicData(String name, String surname, String dateOfBirth, String birthCertNumber, String gender,
                   String oldNic, String civilStatus, String profession, String birthPlace,
                   String district, String permanentAddress, String mobileNumber,
                   String email, String photoReceipt, String birthCertFront, String birthCertBack,
                   String policeComplaint) {
        this.name = name;
        this.surname = surname;
        this.dateOfBirth = dateOfBirth;
        this.birthCertNumber = birthCertNumber;
        this.gender = gender;
        this.oldNic = oldNic;
        this.civilStatus = civilStatus;
        this.profession = profession;
        this.birthPlace = birthPlace;
        this.district = district;
        this.permanentAddress = permanentAddress;
        this.mobileNumber = mobileNumber;
        this.email = email;
        this.photoReceipt = photoReceipt;
        this.birthCertFront = birthCertFront;
        this.birthCertBack = birthCertBack;
        this.policeComplaint = policeComplaint;
    }

    // Getters and Setters for NicFirstFragment fields
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getBirthCertNumber() {
        return birthCertNumber;
    }

    public void setBirthCertNumber(String birthCertNumber) {
        this.birthCertNumber = birthCertNumber;
    }

    public String getOldNic() {
        return oldNic;
    }

    public void setOldNic(String oldNic) {
        this.oldNic = oldNic;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    // Getters and Setters for NicSecondFragment fields
    public String getCivilStatus() {
        return civilStatus;
    }

    public void setCivilStatus(String civilStatus) {
        this.civilStatus = civilStatus;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getBirthPlace() {
        return birthPlace;
    }

    public void setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getPermanentAddress() {
        return permanentAddress;
    }

    public void setPermanentAddress(String permanentAddress) {
        this.permanentAddress = permanentAddress;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    // Getters and Setters for NicThirdFragment fields (Updated for image handling)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhotoReceipt() {
        return photoReceipt;
    }

    public void setPhotoReceipt(String photoReceipt) {
        this.photoReceipt = photoReceipt;
    }

    public String getBirthCertFront() {
        return birthCertFront;
    }

    public void setBirthCertFront(String birthCertFront) {
        this.birthCertFront = birthCertFront;
    }

    public String getBirthCertBack() {
        return birthCertBack;
    }

    public void setBirthCertBack(String birthCertBack) {
        this.birthCertBack = birthCertBack;
    }

    public String getPoliceComplaint() {
        return policeComplaint;
    }

    public void setPoliceComplaint(String policeComplaint) {
        this.policeComplaint = policeComplaint;
    }
}
