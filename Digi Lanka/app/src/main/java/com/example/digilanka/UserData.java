package com.example.digilanka;

public class UserData {
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
    private byte[] nicFrontImage;
    private byte[] nicBackImage;
    private byte[] birthCertificateFront;
    private byte[] birthCertificateBack;
    private byte[] photoReceiptImage;

    public UserData(String travelDocument, String surname, String otherName, String nicNumber,
                    String permanentAddress, String district, String dateOfBirth, String birthCertificateNumber,
                    String gender, String profession, String dualCitizenshipNumber, String phoneNumber,
                    String emailAddress, byte[] nicFrontImage, byte[] nicBackImage,
                    byte[] birthCertificateFront, byte[] birthCertificateBack, byte[] photoReceiptImage) {
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
        this.nicBackImage = nicBackImage;
        this.birthCertificateFront = birthCertificateFront;
        this.birthCertificateBack = birthCertificateBack;
        this.photoReceiptImage = photoReceiptImage;
    }

    // Getters and setters
}
