package com.driver.godel.RefineCode.RefineActivities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.driver.godel.R;
import com.driver.godel.RefineCode.CountryCodeCheck;
import com.driver.godel.RefineCode.RefineUtils.Global;
import com.driver.godel.RefineCode.RefineUtils.SharedValues;
import com.driver.godel.response.ForgotPaswdResponse;
import com.driver.godel.response.changePasswordResp.ChangePasswordResponse;
import com.driver.godel.response.driverProfileResponse.Driver;
import com.driver.godel.response.driverProfileResponse.DriverProfileResponse;
import com.driver.godel.response.driverProfileResponse.ProfileData;
import com.driver.godel.response.getDriverDetails.GetProfileResponse;
import com.google.android.material.snackbar.Snackbar;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserAccountActivityNew extends GodelActivity implements View.OnClickListener {
    private Toolbar toolbar;
    TextView tvTitle, tvDriverUniqueID;
    ImageView ivBack, ivEditName, ivEditEmail, ivEditPassword, ivEditMobileNumber;
    Button btnSave;

    Bitmap bm;
    EditText etName, etEmail, etPassword, etphone, etVehicleNo, etVehicleName, etVehicleType;
    LinearLayout linearMain;
    String profileImage;
    CircleImageView ivDriverPic, ivDriverPic1;
    //Shared Prefs

    //Session Manage
    String driverPassword, driverId, password, vechicleNo, driverName, driverEmail, driverPic, vehicleNo, vehicleColor, vehicleName, status, latitude, longitude;
    String code, driverUniqueIDString, number;
    //Web Services
    Call<ForgotPaswdResponse> forgotPaswdResponseCall;
    ForgotPaswdResponse forgotPaswdResponse;
    Call<DriverProfileResponse> driverProfileResponseCall;
    DriverProfileResponse driverProfileResponse;
    Call<GetProfileResponse> getProfileResponseCall;
    GetProfileResponse getProfileResponse;
    Call<ChangePasswordResponse> changePasswordResponseCall;
    ChangePasswordResponse passwordResponse;

    private static final int REQUEST_CAMERA = 1888;
    private static final int SELECT_FILE = 1;
    String language_type;
    Dialog detailDialog;

    //SnackBar
    Snackbar snackbar;
    ScrollView scrollView;
    static Context context;
    ProgressDialog updateProfileProgressDialog, driverProfileProgress;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_account_new);

        preferences = getSharedPreferences(SharedValues.PREF_NAME, Context.MODE_PRIVATE);
        language_type = preferences.getString(SharedValues.LANGUAGE_SETTINGS,"");

            if(language_type.equals("1")&&language_type!=null){
                Locale myLocale = new Locale("sw");
                Resources res = getResources();
                DisplayMetrics dm = res.getDisplayMetrics();
                Configuration conf = res.getConfiguration();
                conf.locale = myLocale;
                res.updateConfiguration(conf, dm);

            }else if(language_type.equals("2")&&language_type!=null){
                Locale myLocale = new Locale("en");
                Resources res = getResources();
                DisplayMetrics dm = res.getDisplayMetrics();
                Configuration conf = res.getConfiguration();
                conf.locale = myLocale;
                res.updateConfiguration(conf, dm);
            }

        context = UserAccountActivityNew.this;
        //Initialization
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        btnSave = (Button) findViewById(R.id.btn_save);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvDriverUniqueID = (TextView) findViewById(R.id.tv_driver_unique_id);
        ivBack = (ImageView) findViewById(R.id.iv_back);
        ivDriverPic = (CircleImageView) findViewById(R.id.iv_user);
        ivDriverPic1 = (CircleImageView) findViewById(R.id.iv_user1);
        etName = (EditText) findViewById(R.id.et_name);
        etEmail = (EditText) findViewById(R.id.et_email);
        etPassword = (EditText) findViewById(R.id.et_password);
        etphone = (EditText) findViewById(R.id.et_mobile_number);
        etVehicleNo = (EditText) findViewById(R.id.et_vechicle_number);
        linearMain = (LinearLayout) findViewById(R.id.activity_user_account);
        ivEditName = (ImageView) findViewById(R.id.ib_edit_name);
        ivEditEmail = (ImageView) findViewById(R.id.ib_edit_email);
        ivEditPassword = (ImageView) findViewById(R.id.ib_edit_password);
        ivEditMobileNumber = (ImageView) findViewById(R.id.ib_edit_mobile);
        etVehicleName = (EditText) findViewById(R.id.et_vechicle_name);
        etVehicleType = (EditText) findViewById(R.id.et_vechicle_type);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        setSupportActionBar(toolbar);
        tvTitle.setText(getResources().getString(R.string.myaccounttitle));
        tvDriverUniqueID.setText(driverUniqueIDString);
        //Show/Hide Visibility
        ivBack.setVisibility(View.VISIBLE);
        //Set On Click Listener
        ivBack.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        ivDriverPic.setOnClickListener(this);
        ivEditName.setOnClickListener(this);
        ivEditEmail.setOnClickListener(this);
        ivEditPassword.setOnClickListener(this);
        ivEditMobileNumber.setOnClickListener(this);
        //Set PendingData
        driverUniqueIDString = preferences.getString(SharedValues.DRIVER_UNIQUES_CODE, "");
        driverId = preferences.getString(SharedValues.DRIVER_ID, "");
        driverPassword = preferences.getString(SharedValues.DRIVER_PASSWORD, "");
        code = preferences.getString(SharedValues.DRIVER_NUMBER_CODE, "");

        driverImage = preferences.getString(SharedValues.DRIVER_IMAGE, "");

        if (driverPassword != null) {
            etPassword.setText(driverPassword);
        }

        //Check Network Connection
        if (!isNetworkAvailable()) {
            showSnackBar();
        } else {
            //Get Driver Profile API
            getDriverProfileApi(driverId);
        }

        String countrycode = CountryCodeCheck.countrycheck(UserAccountActivityNew.this);

        String imagePath = Global.PROFILE_PIC_URL + countrycode + "/" + "driver_profile/" + driverId + "/" + driverImage;

        Log.e("IMAGE_PATH_LOG", imagePath);
        Log.e("driver Image: ", driverImage);

        if (!UserAccountActivityNew.this.isFinishing()) {
            Glide.with(context).load(imagePath).placeholder(R.drawable.ic_user).dontAnimate().into(ivDriverPic);
        }

    }

    @Override
    protected void onResume() {

        super.onResume();
//        language_type = preferences.getString(SharedValues.LANGUAGE_SETTINGS,"");

//        if (preferences.getString(SharedValues.SELECTED_COUNTRY, "") != null) {
//            if (preferences.getString(SharedValues.SELECTED_COUNTRY, "").trim().length() > 0) {
//                if (preferences.getString(SharedValues.SELECTED_COUNTRY, "").equals(SharedValues.SELECTED_COUNTRY_TANZANIA)) {

        String language_type = preferences.getString(SharedValues.LANGUAGE_SETTINGS,"");
        Log.d("MYPACKAGE_LOG","language_type_onResume--language_type1----"+language_type );
        String language_type2 = preferences.getString(SharedValues.LANGUAGE_Type2,"");
        Log.d("MYPACKAGE_LOG","language_type_onResume--language_type2----"+language_type2 );

        if(language_type.equals(language_type2)){
            Log.d("MYPACKAGE_LOG","language_type_onResume--equal1--"+language_type );
            if(language_type.equals("1")){
                Locale myLocale = new Locale("sw");
                Resources res = getResources();
                DisplayMetrics dm = res.getDisplayMetrics();
                Configuration conf = res.getConfiguration();
                conf.locale = myLocale;
                res.updateConfiguration(conf, dm);

            }else if(language_type.equals("2")){
                Log.d("MYPACKAGE_LOG","language_type--onResume_equals=2----"+language_type );
                Locale myLocale = new Locale("en");
                Resources res = getResources();
                DisplayMetrics dm = res.getDisplayMetrics();
                Configuration conf = res.getConfiguration();
                conf.locale = myLocale;
                res.updateConfiguration(conf, dm);
            }}
        else{
            Log.d("MYPACKAGE_LOG","language_type_on else----"+language_type +language_type2);
            editor.putString(SharedValues.LANGUAGE_Type2, language_type);
            editor.commit();
            Log.d("MYPACKAGE_LOG","editor--commit 1 & 2---"+language_type +language_type2);
            Intent intent = getIntent();
            finish();

            startActivity(intent);

        }
//

//        //Check Network Connection
//        if (!isNetworkAvailable()) {
//            showSnackBar();
//        } else {
//            //Get Driver Profile API
//            getDriverProfileApi(driverId);
//        }
//
//        String countrycode = CountryCodeCheck.countrycheck(UserAccountActivityNew.this);
//        String imagePath = Global.PROFILE_PIC_URL + countrycode + "/" + "driver_profile/" + driverId + "/" + driverImage;
//
//        Log.e("IMAGE_PATH_LOG", imagePath);
//        Log.e("driver Image: ", driverImage);
//
//        if (!UserAccountActivityNew.this.isFinishing()) {
//            Glide.with(context).load(imagePath).placeholder(R.drawable.ic_user).dontAnimate().into(ivDriverPic);
//        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;

            case R.id.btn_save:
                if (etName.getText().toString().isEmpty()) {
                    etName.setError(getResources().getString(R.string.namerequirederror));
                    etName.requestFocus();
                } else if (etEmail.getText().toString().isEmpty() || !isValidEmail(etEmail.getText().toString())) {
                    etEmail.setError(getResources().getString(R.string.entervalidemailerror));
                    etEmail.requestFocus();
                } else if (etPassword != null && etPassword.getText().toString().length() <= 0) {
                    etPassword.setError(getResources().getString(R.string.pleaseenterpassworderror));
                    etPassword.requestFocus();
                } else if (etPassword.getText().toString().length() < 6) {
                    etPassword.setError(getResources().getString(R.string.enterpasswordmin));
                    etPassword.requestFocus();
                } else if (etphone.getText().toString().isEmpty()) {
                    etphone.setError(getResources().getString(R.string.entermobilenumbererror));
                    etphone.requestFocus();
                } else {

                    String name = etName.getText().toString();
                    String email = etEmail.getText().toString();
                    password = etPassword.getText().toString();
                    String phone = etphone.getText().toString();
                    vechicleNo = etVehicleNo.getText().toString();
                    if (phone != null) {
                        if (phone.contains(code)) {
                            number = phone.substring(code.length(), phone.length());
                        }
                    }

                    String USER_TOKEN = preferences.getString(SharedValues.DRIVER_AUTH_TOKEN, "");
                    String Accept = Global.ACCEPT;
                    driverId = preferences.getString(SharedValues.DRIVER_ID, "");
                    if (USER_TOKEN == null || USER_TOKEN.length() <= 0 || USER_TOKEN.trim().equalsIgnoreCase("")) {
                        if (!(UserAccountActivityNew.this.isFinishing())) {
                            if (updateProfileProgressDialog != null && updateProfileProgressDialog.isShowing()) {
                                updateProfileProgressDialog.dismiss();
                            }
                        }
                        Global.signInIntent(this);
                    } else {

                        String country_code = CountryCodeCheck.countrycheck(context);
                        if (country_code != null && country_code.trim().length() > 0) {

                            //API HIT


                            driverProfileResponseCall = webRequest.apiInterface.getDriverProfile(country_code, Global.USER_AGENT, Accept, USER_TOKEN, driverId, driverId, name, email, password, number, vechicleNo, profileImage, "0", "0");

                            if (!isNetworkAvailable()) {
                                showSnackBar();
                            } else {
                                driverProfileUpdate(driverProfileResponseCall);
                            }


                        } else {
                            if (!(UserAccountActivityNew.this.isFinishing())) {
                                if (updateProfileProgressDialog != null && updateProfileProgressDialog.isShowing()) {
                                    updateProfileProgressDialog.dismiss();
                                }
                            }
                        }

                    }

                }
                break;

            case R.id.iv_user:
                //Open Image Dialog
                imageDialog();
                break;

            case R.id.ib_edit_name:
                //Hide Visibility
                ivEditName.setVisibility(View.GONE);
                etName.setEnabled(true);
                etName.requestFocus();
                etName.setSelection(etName.getText().length());
                //Show Button Visibility
                btnSave.setVisibility(View.VISIBLE);
                openKeyboard(etName);
                break;

            case R.id.ib_edit_email:
                //Hide Visibility
                ivEditEmail.setVisibility(View.GONE);
                //Enable
                etEmail.setEnabled(true);
                etEmail.requestFocus();
                etEmail.setSelection(etEmail.getText().length());

                //Show Button Visibility
                btnSave.setVisibility(View.VISIBLE);
                break;

            case R.id.ib_edit_password:
                //Pass Intent to Change Password
                Intent changePswdIntent = new Intent(UserAccountActivityNew.this, ChangePasswordActivityNew.class);
                startActivity(changePswdIntent);
                break;

            case R.id.ib_edit_mobile:
                //Hide Visibility
                ivEditMobileNumber.setVisibility(View.GONE);
                //Enable
                etphone.setEnabled(true);
                etphone.requestFocus();
                etphone.setSelection(etphone.getText().length());
                //Show Button Visibility
                btnSave.setVisibility(View.VISIBLE);
                openKeyboard(etphone);
                break;
        }
    }

    private void openKeyboard(EditText editText) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInputFromWindow(editText.getApplicationWindowToken(), InputMethodManager.SHOW_FORCED, 0);
    }

    //API HIT Update Driver Profile
    private void driverProfileUpdate(final Call<DriverProfileResponse> driverProfileResponseCall) {
        if (getProfileResponseCall != null) {
            getProfileResponseCall.cancel();
        }
        if (!(UserAccountActivityNew.this.isFinishing())) {
            if (updateProfileProgressDialog != null && updateProfileProgressDialog.isShowing()) {
                updateProfileProgressDialog.dismiss();
            }
        }
        if (updateProfileProgressDialog == null) {
            updateProfileProgressDialog = new ProgressDialog(this);
        }
        updateProfileProgressDialog.setMessage("Getting profile details...");
        updateProfileProgressDialog.setCancelable(false);
        updateProfileProgressDialog.show();

        driverProfileResponseCall.enqueue(new Callback<DriverProfileResponse>() {
            @Override
            public void onResponse(Call<DriverProfileResponse> call, Response<DriverProfileResponse> response) {
                if (!isNetworkAvailable()) {
                    showSnackBar();
                } else {
                    if (response.isSuccessful()) {
                        driverProfileResponse = response.body();
                        Log.d("USERACCOUNT_NEW", "Response=" + driverProfileResponse);

                        if (driverProfileResponse.getResponse() == 1) {

                            ProfileData profileData = driverProfileResponse.getData();
                            Driver driver = profileData.getDriver();
                            String driverName = driver.getDriver_name();
                            String driverEmail = driver.getDriver_email();
                            String driverPhoneNumber = driver.getDriver_phone();
                            String driverImage = driver.getDriver_image();
                            //Create Session
                            if (driverName != null && driverName.length() > 0) {
                                editor.putString(SharedValues.DRIVER_NAME, driverName);
                            }
                            if (driverEmail != null && driverEmail.length() > 0) {
                                editor.putString(SharedValues.DRIVER_EMAIL, driverEmail);
                            }
                            if (driverImage != null && driverImage.length() > 0) {
                                editor.putString(SharedValues.DRIVER_IMAGE, driverImage);
                            }
                            editor.commit();

//                            String imagePath = Global.BASE_URL + "backend/public/uploads/driver_profile/" + driverId + "/" + driverImage;

                            String countrycode = CountryCodeCheck.countrycheck(UserAccountActivityNew.this);

                            String imagePath = Global.PROFILE_PIC_URL + countrycode + "/" + "driver_profile/" + driverId + "/" + driverImage;

                            Glide.with(UserAccountActivityNew.this).load(imagePath).placeholder(R.drawable.ic_user).dontAnimate().into(ivDriverPic);
                            Glide.with(UserAccountActivityNew.this).load(imagePath).placeholder(R.drawable.ic_user).dontAnimate().into(ivDriverPic1);


                            //Show Toast
                            Toast.makeText(UserAccountActivityNew.this, "Profile Updated Successfully", Toast.LENGTH_SHORT).show();
                            //Show Visibility
                            ivEditName.setVisibility(View.VISIBLE);
//                            ivEditEmail.setVisibility(View.VISIBLE);
                            ivEditPassword.setVisibility(View.VISIBLE);
                            ivEditMobileNumber.setVisibility(View.VISIBLE);
                            //Hide Visibility Button
                            btnSave.setVisibility(View.GONE);
                            //disable EditText
                            etName.setEnabled(false);
                            etEmail.setEnabled(false);
                            etPassword.setEnabled(false);
                            etphone.setEnabled(false);
                        } else if (driverProfileResponse.getData().getMessage() != null) {
                            if (driverProfileResponse.getData().getMessage().equals("Unauthenticated")) {
                                Global.signInIntent(UserAccountActivityNew.this);
                            }
                        }
                    } else {
                        Toast.makeText(UserAccountActivityNew.this, "Try again", Toast.LENGTH_SHORT).show();
                    }
                }
                if (!(UserAccountActivityNew.this.isFinishing())) {
                    if (updateProfileProgressDialog != null && updateProfileProgressDialog.isShowing()) {
                        updateProfileProgressDialog.dismiss();
                    }
                }
            }

            @Override
            public void onFailure(Call<DriverProfileResponse> call, Throwable t) {
                if (!(UserAccountActivityNew.this.isFinishing())) {
                    if (updateProfileProgressDialog != null && updateProfileProgressDialog.isShowing()) {
                        updateProfileProgressDialog.dismiss();
                    }
                }
                t.printStackTrace();
            }
        });
    }

    //Show Image Dialog
    private void imageDialog() {
        final CharSequence[] items1 = {"Take Photo", "Choose from Gallery"};
        AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
        builder2.setTitle(getResources().getString(R.string.chooseanactiondialog));
        builder2.setCancelable(true);
        builder2.setItems(items1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items1[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA);
                } else if (items1[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
                }
            }
        });
        builder2.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            if (requestCode == REQUEST_CAMERA) {

                bm = (Bitmap) data.getExtras().get("data");

                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
                File destination = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis() + ".jpg");
                FileOutputStream fo;

                try {
                    destination.createNewFile();
                    fo = new FileOutputStream(destination);
                    fo.write(bytes.toByteArray());
                    fo.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //ivDriverPic.setImageBitmap(bm);

                ivDriverPic1.setImageBitmap(bm);

                updateBase64(bm);
            }

            else if (requestCode == SELECT_FILE) {

                if (resultCode == Activity.RESULT_OK) {
                    //addImageDialog.dismiss();
                    Uri tempUri = data.getData();
                    Bitmap bm = null;
                    if (data != null) {
                        try {
                            bm = decodeUri(tempUri);

//                            ivDriverPic1.setImageBitmap(bm);

                            Glide.with(context).load(tempUri).into(ivDriverPic1);

                            //ivDriverPic.setImageBitmap(bm);
                            updateBase64(bm);

                        } catch (OutOfMemoryError e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        } else {
            Intent intent = new Intent(UserAccountActivityNew.this, UserAccountActivityNew.class);
            startActivity(intent);
            finish();
        }
    }

    private Bitmap decodeUri(Uri selectedImage) throws FileNotFoundException {
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o);

        final int REQUIRED_SIZE = 100;

        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE) {
                break;
            }
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        return BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o2);
    }

    public void updateBase64(Bitmap bitmap) {

        // Bitmap bitmap = Bitmap.createBitmap(bm);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        profileImage = Base64.encodeToString(byteArray, Base64.DEFAULT);

        btnSave.setVisibility(View.VISIBLE);

       /*//decode base64 string to image
        byte[] imageBytes = Base64.decode(profileImage, Base64.DEFAULT);
        Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        ivDriverPic.setImageBitmap(decodedImage);*/

        byte[] decodedString = Base64.decode(profileImage, Base64.DEFAULT);
        InputStream inputStream = new ByteArrayInputStream(decodedString);
        Bitmap decodedBitmap = BitmapFactory.decodeStream(inputStream);

        ivDriverPic.setImageBitmap(decodedBitmap);

        View lastChild = scrollView.getChildAt(scrollView.getChildCount() - 1);
        int bottom = lastChild.getBottom() + scrollView.getPaddingBottom();
        int sy = scrollView.getScrollY();
        int sh = scrollView.getHeight();
        int delta = bottom - (sy + sh);

        scrollView.smoothScrollBy(0, delta);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
//        Intent intent = new Intent(this, MyPackages1.class);
//        startActivity(intent);
        finish();
    }

    //Show SnackBar
    public void showSnackBar() {
        snackbar = Snackbar.make(linearMain, "No Internet Connection", Snackbar.LENGTH_INDEFINITE);
        //Set Action CallBack
        snackbar.setAction("Retry", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isNetworkAvailable()) {
                    showSnackBar();
                } else {
                    snackbar.dismiss();
                }
            }
        });

        //Set Action Text Color
        snackbar.setActionTextColor(Color.WHITE);

        //Set Text Color
        TextView tv = (TextView) snackbar.getView().findViewById(R.id.snackbar_text);
        tv.setTextColor(Color.RED);
        snackbar.show();
    }

    //Show Change Password Dialog
    private void dialogChangePassword() {
        detailDialog = new Dialog(this);
        detailDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        detailDialog.setContentView(R.layout.dialog_change_password);
        detailDialog.setCancelable(false);
        detailDialog.setCanceledOnTouchOutside(false);
        detailDialog.show();

        final EditText etOldPassword, etNewPassword, etConfirmPassword;
        ImageView ivClose;
        Button btnSubmit;

        //Initialization
        etOldPassword = (EditText) detailDialog.findViewById(R.id.et_old_password);
        etNewPassword = (EditText) detailDialog.findViewById(R.id.et_new_password);
        etConfirmPassword = (EditText) detailDialog.findViewById(R.id.et_confirm_password);
        btnSubmit = (Button) detailDialog.findViewById(R.id.btn_submit);
        ivClose = (ImageView) detailDialog.findViewById(R.id.iv_close);

        //Close Dialog Click Listener
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detailDialog.dismiss();
            }
        });

        //Button Click Listener
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String newPassword = etNewPassword.getText().toString();
                String confirmPassword = etConfirmPassword.getText().toString();


                if (etOldPassword.getText().toString().isEmpty() || etOldPassword.getText().toString().equalsIgnoreCase("")) {
                    etOldPassword.setError(getResources().getString(R.string.oldpasswordrequirerror));
                } else if (driverPassword != null && !etOldPassword.getText().toString().matches(driverPassword)) {
                    etOldPassword.setError(getResources().getString(R.string.oldpasswordnotmatcherror));
                } else if (etNewPassword.getText().toString().isEmpty() || etNewPassword.getText().toString().equalsIgnoreCase("")) {
                    etNewPassword.setError(getResources().getString(R.string.newpassworderror));
                } else if (etConfirmPassword.getText().toString().isEmpty() || etConfirmPassword.getText().toString().equalsIgnoreCase("")) {
                    etConfirmPassword.setError(getResources().getString(R.string.confirmpasswordrequirerror));
                } else if (!confirmPassword.matches(newPassword)) {
                    etConfirmPassword.setError(getResources().getString(R.string.confirmpasswordnotmatched));
                } else {
                    String password = etConfirmPassword.getText().toString();

                    if (isNetworkAvailable()) {
                        //API HIT Change Password
                        changePasswordApi(password);
                    } else {
                        Snackbar(ivBack);
                    }

                }
            }
        });
    }

    public void Snackbar(final View layout) {
        final Snackbar mySnackbar = Snackbar.make(findViewById(android.R.id.content), R.string.NoInternet, Snackbar.LENGTH_INDEFINITE);
        View snackBarView = mySnackbar.getView();
        snackBarView.setBackgroundColor(Color.RED);
        TextView textView = (TextView) snackBarView.findViewById(R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        mySnackbar.setActionTextColor(Color.YELLOW);
        mySnackbar.setAction(getResources().getString(R.string.retrytext), new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isNetworkAvailable()) {
                    mySnackbar.dismiss();
                } else {
                    Snackbar(layout);
                }
            }
        });
        mySnackbar.show();
    }

    //Change Passwor API HIT
    private void changePasswordApi(String password) {

        String USER_TOKEN = preferences.getString(SharedValues.DRIVER_AUTH_TOKEN, "");
        String Accept = Global.ACCEPT;
        driverId = preferences.getString(SharedValues.DRIVER_ID, "");


        String country_code = CountryCodeCheck.countrycheck(context);
        if (country_code != null && country_code.trim().length() > 0) {


            changePasswordResponseCall = webRequest.apiInterface.changePasswordApi(country_code, Global.USER_AGENT, Accept, USER_TOKEN, driverId, driverId, password);
            changePasswordResponseCall.enqueue(new Callback<ChangePasswordResponse>() {
                @Override
                public void onResponse(Call<ChangePasswordResponse> call, Response<ChangePasswordResponse> response) {
                    if (response.isSuccessful()) {
                        passwordResponse = response.body();

                        if (passwordResponse.getResponse() == 1) {
                            detailDialog.dismiss();
                            Toast.makeText(UserAccountActivityNew.this, "" + passwordResponse.getData().getMessage(), Toast.LENGTH_SHORT).show();
                        } else if (passwordResponse.getData().getMessage() != null) {
                            if (passwordResponse.getData().getMessage().equals("Unauthenticated")) {
                                Global.signInIntent(UserAccountActivityNew.this);
                            }
                        } else {
                            detailDialog.dismiss();
                            Toast.makeText(UserAccountActivityNew.this, "" + passwordResponse.getData().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ChangePasswordResponse> call, Throwable t) {
                    t.printStackTrace();
                }
            });

        } else {
            if (detailDialog != null && detailDialog.isShowing()) {
                detailDialog.dismiss();
            }
        }
    }


    //Get Driver Profile
    private void getDriverProfileApi(String driverId) {
        if (getProfileResponseCall != null) {
            getProfileResponseCall.cancel();
        }
        if (!(UserAccountActivityNew.this.isFinishing())) {
            if (driverProfileProgress != null && driverProfileProgress.isShowing()) {
                driverProfileProgress.dismiss();
            }
        }
        if (driverProfileProgress == null) {
            driverProfileProgress = new ProgressDialog(this);
        }

        driverProfileProgress.setMessage("Getting profile details...");
        driverProfileProgress.setCancelable(false);
        driverProfileProgress.show();

        String USER_TOKEN = preferences.getString(SharedValues.DRIVER_AUTH_TOKEN, "");
        String Accept = Global.ACCEPT;
        driverId = preferences.getString(SharedValues.DRIVER_ID, "");
        if (USER_TOKEN == null || USER_TOKEN.length() <= 0 || USER_TOKEN.trim().equalsIgnoreCase("")) {
            Global.signInIntent(this);
        }


        String country_code = CountryCodeCheck.countrycheck(context);
        if (country_code != null && country_code.trim().length() > 0) {


            //API HIT
            getProfileResponseCall = webRequest.apiInterface.getProfileApi(country_code, Global.USER_AGENT, Accept, USER_TOKEN, driverId, driverId);
            getProfileResponseCall.enqueue(new Callback<GetProfileResponse>() {
                @Override
                public void onResponse(Call<GetProfileResponse> call, Response<GetProfileResponse> response) {
                    if (response.isSuccessful()) {
                        getProfileResponse = response.body();
                        //Check Response
                        if (getProfileResponse.getResponse() == 1) {
                            //Get Driver Data
                            String driverId = getProfileResponse.getData().getId();
                            String driverName = getProfileResponse.getData().getDriver_name();
                            String driverEmail = getProfileResponse.getData().getDriver_email();
                            String driverPhone = getProfileResponse.getData().getDriver_phone();
                            String vechicleIdFk = getProfileResponse.getData().getVehicle_id_fk();
                            String driverImage = getProfileResponse.getData().getDriver_image();
                            String vehicleType = getProfileResponse.getData().getVehicle_type();
                            String vehicleName = getProfileResponse.getData().getVehicle_name();
                            String vehicleNumber = getProfileResponse.getData().getVehicle_number();
                            String vehicleColor = getProfileResponse.getData().getVehicle_color();
                            String vehicleOwnerShip = getProfileResponse.getData().getVehicle_ownership();


//                        if (vechicleIdFk != null && vechicleIdFk.trim().length() > 0) {
//                            editor.putString(SharedValues.VEHICLE_ID, vechicleIdFk);
//                        } else {
//                            editor.putString(SharedValues.VEHICLE_ID, "");
//                        }
                            if (driverName != null && driverName.trim().length() > 0) {
                                editor.putString(SharedValues.DRIVER_NAME, driverName);
                            }
                            if (driverEmail != null && driverEmail.trim().length() > 0) {
                                editor.putString(SharedValues.DRIVER_EMAIL, driverEmail);
                            }
                            if (driverPhone != null && driverPhone.trim().length() > 0) {
                                editor.putString(SharedValues.DRIVER_PHONE, driverPhone);
                            }
                            if (driverImage != null) {
//                                        String imagePath = Global.BASE_URL + "backend/public/uploads/driver_profile/" + driverId + "/" + driverImage;
                                String countrycode = CountryCodeCheck.countrycheck(UserAccountActivityNew.this);
                                String imagePath = Global.PROFILE_PIC_URL + countrycode + "/" + "driver_profile/" + driverId + "/" + driverImage;
                                //Load Image using Glide
                                if (!UserAccountActivityNew.this.isFinishing()) {
                                    Glide.with(context).load(imagePath).placeholder(R.drawable.ic_user).dontAnimate().into(ivDriverPic);
                                    Glide.with(context).load(imagePath).placeholder(R.drawable.ic_user).dontAnimate().into(ivDriverPic1);
                                }
                                editor.putString(SharedValues.DRIVER_IMAGE, driverImage);
                            }
                            editor.commit();
                            //Set Driver Name
                            if (driverName != null) {
                                etName.setText(driverName);
                            }

                            //Set Driver Email
                            if (driverEmail != null) {
                                etEmail.setText(driverEmail);
                            }

                            //Set Driver Phone
                            if (driverPhone != null) {
                                if (code != null && code.length() > 0) {
                                    etphone.setText(code + "" + driverPhone);
                                } else {
                                    etphone.setText(driverPhone);
                                }
                                number = driverPhone;
                            }

                            //Set Vehicle Number
                            if (vehicleNumber != null) {
                                etVehicleNo.setText(vehicleNumber);
                            }

                            //Set Vehicle Name
                            if (vehicleName != null) {
                                etVehicleName.setText(vehicleName);
                            }

                            //Set Vehicle type
                            if (vehicleType != null) {
                                if(preferences.getString(SharedValues.SELECTED_COUNTRY, "").equals(SharedValues.SELECTED_COUNTRY_KENYA)
                                        ||preferences.getString(SharedValues.SELECTED_COUNTRY, "").equals(SharedValues.SELECTED_COUNTRY_UGANDA)){
                                    if (vehicleType.equalsIgnoreCase("0")) {
                                        etVehicleType.setText("Bike");
                                    } else if (vehicleType.equalsIgnoreCase("1")) {
                                        etVehicleType.setText("Van");
                                    } else if (vehicleType.equalsIgnoreCase("2")) {
                                        etVehicleType.setText("3- Tonne Truck");
                                    } else if (vehicleType.equalsIgnoreCase("3")) {
                                        etVehicleType.setText("5- Tonne Truck");
                                    } else if (vehicleType.equalsIgnoreCase("4")) {
                                        etVehicleType.setText("10- Tonne Truck");
                                    } else if (vehicleType.equalsIgnoreCase("5")) {
                                        etVehicleType.setText("28- Tonne Truck");
                                    } else if (vehicleType.equalsIgnoreCase("6")) {
                                        etVehicleType.setText("40- Tonne Truck");
                                    }

                                }else{
                                    if (vehicleType.equalsIgnoreCase("0")) {
                                        etVehicleType.setText("Bike");
                                    } else if (vehicleType.equalsIgnoreCase("1")) {
                                        etVehicleType.setText("Van");
                                    } else if (vehicleType.equalsIgnoreCase("2")) {
                                        etVehicleType.setText("Truck");
                                    }
                                }
                            }
                        } else if (getProfileResponse.getData().getMessage() != null) {
                            if (getProfileResponse.getData().getMessage().equals("Unauthenticated")) {
                                Global.signInIntent(UserAccountActivityNew.this);
                            }
                        } else {
                            Toast.makeText(context, "" + getProfileResponse.getData().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                    if (!UserAccountActivityNew.this.isFinishing()) {
                        if (driverProfileProgress != null && driverProfileProgress.isShowing()) {
                            driverProfileProgress.dismiss();
                        }
                    }
                }

                @Override
                public void onFailure(Call<GetProfileResponse> call, Throwable t) {
                    if (!UserAccountActivityNew.this.isFinishing()) {
                        if (driverProfileProgress != null && driverProfileProgress.isShowing()) {
                            driverProfileProgress.dismiss();
                        }
                    }
                    t.printStackTrace();
                }
            });

        }
    }

}
