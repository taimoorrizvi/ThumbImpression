package com.example.bahl.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by bahl on 4/8/2016.
 */
public class LoginResponse {
    // public

    public long UserId;
    public long CompanyId;
    public String CompanyName;
    public String UserName;
    public String WorkingDate;

    @SerializedName("BranchCd")
    @Expose
    public String BranchCode;

    @SerializedName("SubBranchCd")
    @Expose
    public String SubBranchCode;
    public String BranchName;
    public String SubBranchName;
    public String ProvinceName;
}
