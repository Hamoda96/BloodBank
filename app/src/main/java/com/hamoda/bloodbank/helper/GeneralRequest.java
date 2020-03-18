package com.hamoda.bloodbank.helper;

import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.hamoda.bloodbank.R;
import com.hamoda.bloodbank.adapter.SpinnerAdapter;
import com.hamoda.bloodbank.data.model.generalResponse.GeneralResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.hamoda.bloodbank.helper.HelperMethod.progressDialog;

public class GeneralRequest {


    public static void getSpinnerData(Activity activity, Call<GeneralResponse> call, final Spinner spinner
            , final SpinnerAdapter adapter, final Integer selectedId, final String hint, View view, final boolean show) {
        if (show) {
            if (progressDialog == null) {
                HelperMethod.showProgressDialog(activity, activity.getString(R.string.wait));
            } else {
                if (!progressDialog.isShowing()) {
                    HelperMethod.showProgressDialog(activity, activity.getString(R.string.wait));
                }
            }
        }
        call.enqueue(new Callback<GeneralResponse>() {
            @Override
            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                if (show) {
                    HelperMethod.dismissProgressDialog();
                }
                try {
                    if (response.body().getStatus() == 1) {
                        if (view != null) {
                            view.setVisibility(view.VISIBLE);
                        }
                    }

                    adapter.setData(response.body().getData(), hint);
                    spinner.setAdapter(adapter);
                    for (int i = 0; i < adapter.generalResponseDataList.size(); i++) {

                        if (adapter.generalResponseDataList.get(i).getId() == selectedId) {
                            spinner.setSelection(i);
                            break;
                        }
                    }

                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<GeneralResponse> call, Throwable t) {
                if (show) {
                    HelperMethod.dismissProgressDialog();
                }            }
        });
    }


    public static void getSpinnerData(Activity activity, Call<GeneralResponse> call, final Spinner spinner, final SpinnerAdapter adapter, final Integer selectedId, final String hint, View view, AdapterView.OnItemSelectedListener listener) {

        call.enqueue(new Callback<GeneralResponse>() {
            @Override
            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {

                try {

                    adapter.setData(response.body().getData(), hint);
                    spinner.setAdapter(adapter);
                    for (int i = 0; i < adapter.generalResponseDataList.size(); i++) {

                        if (adapter.generalResponseDataList.get(i).getId() == selectedId) {
                            spinner.setSelection(i);
                            break;
                        }
                    }

                    spinner.setOnItemSelectedListener(listener);
                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<GeneralResponse> call, Throwable t) {

            }
        });
    }

}
