package com.example.digilanka;

// FormData.java
public class FormData {
    private String travelDocument;
    private String surname;
    private String otherName;
    private String nicNumber;
    private String permanentAddress;
    private String district;
    private String dateOfBirth;
    private String birthCertificateNumber;
    private String gender;
    private String profession;
    private String dualCitizenshipNumber;
    private String phoneNumber;
    private String emailAddress;
    private String nicFrontImage;

    // Constructor
    public FormData(String travelDocument, String surname, String otherName, String nicNumber,
                    String permanentAddress, String district, String dateOfBirth,
                    String birthCertificateNumber, String gender, String profession,
                    String dualCitizenshipNumber, String phoneNumber, String emailAddress,
                    String nicFrontImage) {
        this.travelDocument = travelDocument;
        this.surname = surname;
        this.otherName = otherName;
        this.nicNumber = nicNumber;
        this.permanentAddress = permanentAddress;
        this.district = district;
        this.dateOfBirth = dateOfBirth;
        this.birthCertificateNumber = birthCertificateNumber;
        this.gender = gender;
        this.profession = profession;
        this.dualCitizenshipNumber = dualCitizenshipNumber;
        this.phoneNumber = phoneNumber;
        this.emailAddress = emailAddress;
        this.nicFrontImage = nicFrontImage;
    }

    // Getters and Setters
    // ...
}
