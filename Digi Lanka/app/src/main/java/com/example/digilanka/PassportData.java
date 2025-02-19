package com.example.digilanka;

public class PassportData {
    // Fields from the FirstFragment
    private String nationalID;
    private String surname;
    private String otherName;
    private String permanentAddress;
    private String district;
    private String travel_document;

    // Fields from the SecondFragment
    private String dateOfBirth;
    private String birthCertNumber;
    private String profession;
    private String dualCitizenNumber;

    // Fields from the ThirdFragment
    private String gender;
    private String phoneNumber;
    private String emailAddress;
    private String nicFrontImage;
    private String nicBackImage;
    private String birthCertificateFront;
    private String birthCertificateBack;
    private String photoReceiptImage;

    // Constructor
    public PassportData(String nationalID, String surname, String otherName, String permanentAddress, String district, String travel_document,
                        String dateOfBirth, String birthCertNumber, String profession, String dualCitizenNumber,
                        String gender, String phoneNumber, String emailAddress,
                        String nicFrontImage, String nicBackImage, String birthCertificateFront, String birthCertificateBack,
                        String photoReceiptImage) {
        this.nationalID = nationalID;
        this.surname = surname;
        this.otherName = otherName;
        this.permanentAddress = permanentAddress;
        this.district = district;
        this.travel_document = travel_document;
        this.dateOfBirth = dateOfBirth;
        this.birthCertNumber = birthCertNumber;
        this.profession = profession;
        this.dualCitizenNumber = dualCitizenNumber;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.emailAddress = emailAddress;
        this.nicFrontImage = nicFrontImage;
        this.nicBackImage = nicBackImage;
        this.birthCertificateFront = birthCertificateFront;
        this.birthCertificateBack = birthCertificateBack;
        this.photoReceiptImage = photoReceiptImage;
    }

    // Getters
    public String getNationalID() { return nationalID; }
    public String getSurname() { return surname; }
    public String getOtherName() { return otherName; }
    public String getPermanentAddress() { return permanentAddress; }
    public String getDistrict() { return district; }
    public String getTravelDocument() { return travel_document; }
    public String getDateOfBirth() { return dateOfBirth; }
    public String getBirthCertNumber() { return birthCertNumber; }
    public String getProfession() { return profession; }
    public String getDualCitizenNumber() { return dualCitizenNumber; }
    public String getGender() { return gender; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getEmailAddress() { return emailAddress; }
    public String getNicFrontImage() { return nicFrontImage; }
    public String getNicBackImage() { return nicBackImage; }
    public String getBirthCertificateFront() { return birthCertificateFront; }
    public String getBirthCertificateBack() { return birthCertificateBack; }
    public String getPhotoReceiptImage() { return photoReceiptImage; }
}
