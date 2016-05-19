
package com.example.bahl.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

//        import javax.annotation.Generated;
// Required fields
//  BranchCd  SubBranchCd VerificationStatus CertificateNo SessionId
// UserIdentificationDocumentId UserIdentificationDocumentNo  ContactNumber
// ContactNumber  CustomerCategoryId ImagebytesString BiometricFingerCode
// BiometricFormatCode TokenNo PurposeId ProductCategoryId

public class BiometricVerificationDto {


    @SerializedName("UserIdentificationDocumentId")
    @Expose
    public short UserIdentificationDocumentId;

    @SerializedName("UserIdentificationDocument")
    @Expose
    public String UserIdentificationDocument;

    @SerializedName("UserIdentificationDocumentNo")
    @Expose
    public String UserIdentificationDocumentNo;

    @SerializedName("ContactTypeId")
    @Expose
    public short ContactTypeId;

    @SerializedName("ContactType")
    @Expose
    public String ContactType;

    @SerializedName("ContactNumber")
    @Expose
    public String ContactNumber;

    @SerializedName("CustomerCategoryId")
    @Expose
    public short CustomerCategoryId;

    @SerializedName("CustomerCategory")
    @Expose
    public String CustomerCategory;

    @SerializedName("ImagebytesString")
    @Expose
    public String ImagebytesString;
/*
    @SerializedName("Imagebytes")
    @Expose
    public byte[] Imagebytes;*/

    @SerializedName("BiometricFingerCode")
    @Expose
    public short BiometricFingerCode;

    @SerializedName("PurposeId")
    @Expose
    public short PurposeId;

    @SerializedName("Purpose")
    @Expose
    public String Purpose;

    @SerializedName("ProductCategoryId")
    @Expose
    public short ProductCategoryId;

    @SerializedName("ProductCategory")
    @Expose
    public String ProductCategory;


}