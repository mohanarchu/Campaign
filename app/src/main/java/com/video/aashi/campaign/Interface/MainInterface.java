package com.video.aashi.campaign.Interface;


import com.google.gson.JsonObject;
import com.video.aashi.campaign.affialation.AffiPojo;
import com.video.aashi.campaign.affialation.PartyPojo;
import com.video.aashi.campaign.affialation.SuccessPojo;
import com.video.aashi.campaign.login.LoginPojo;
import com.video.aashi.campaign.login.otp.OtpPojo;
import com.video.aashi.campaign.voterlist.VoterPojo;
import com.video.aashi.campaign.votersview.CitizenPojo;
import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface MainInterface {
    @POST("Campaign/AppCampaignLoginValidate")
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    Observable<LoginPojo> getLoginstatus(@Body JsonObject jsonObject);

    @POST("Campaign/AppCampaignOTPValidate")
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    Observable<OtpPojo> getBooths(@Body JsonObject jsonObject);

    @Multipart
    @POST("CitizenRegister/AppCitizenRegisterCreate")
    Observable<ResponseBody> CitizenRegister(@Part("Booth_Id") RequestBody boothid,
                                             @Part("VoterIdNumber") RequestBody voternumber,
                                             @Part MultipartBody.Part image,
                                             @Part("CandidateName") RequestBody cantidatename,
                                             @Part("FatherName") RequestBody fathername,
                                             @Part("CandidateAge") RequestBody age,
                                             @Part("CandidateSex") RequestBody sex,
                                             @Part("DoorNumber") RequestBody doornumber,
                                             @Part("User_Id") RequestBody userid,
                                             @Part("ALL_DATA") RequestBody alldata);

    @POST("Campaign/AppVotersList")
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    Observable<VoterPojo> getVoters(@Body JsonObject jsonObject);
    @POST("Campaign/AppCampaignManagerDetail")
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    Observable<OtpPojo> getmanager(@Body JsonObject jsonObject);
    @POST("CitizenRegister/AppCitizenRegisterList")
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    Observable<CitizenPojo> CitizenList(@Body JsonObject jsonObject);
    @POST("CitizenRegister/AppCitizenRegisterUpdate")
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    Observable<SuccessPojo> UpdateCitizen(@Body JsonObject jsonObject );
    @POST("Settings/AppAfflictionTypeList")
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    Observable<AffiPojo> AffiList( );
    @POST("Settings/AppPartyTypeList")
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    Observable<PartyPojo> PartyList( );
}