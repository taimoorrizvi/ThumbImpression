package com.example.bahl.Model;

import java.util.List;

/**
 * Created by Administrator on 5/16/2016.
 */
public class BiometricDetailDto {


    public Long CertificateNo;


    public String CNICSessionID;


    public String Remarks;


    public boolean Result;


    //  public int? TokenNo { get; set; }*/


    public String CNIC;


    public String CNICName;


    public String BirthPlace;


    public String DateofBirth;


    public String CardExpiry;


    public String Address;


    public String VerificationStatus;


    public String NadraMessage;


    public String CustomerImage;


    public boolean IsValid;

    public boolean IsNadraError;

    public boolean IsSaved;

    public boolean IsNadraVerified;

    public List<String> ErrorMessages;
}