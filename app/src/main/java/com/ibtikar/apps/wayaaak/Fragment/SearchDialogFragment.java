package com.ibtikar.apps.wayaaak.Fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hosamazzam.volleysimple.VolleySimple;
import com.ibtikar.apps.wayaaak.Adapters.List_Adapter;
import com.ibtikar.apps.wayaaak.Dialogs.Filter_Bottom_Sheet;
import com.ibtikar.apps.wayaaak.Models.Response.ListResponse;
import com.ibtikar.apps.wayaaak.Models.User;
import com.ibtikar.apps.wayaaak.R;
import com.ibtikar.apps.wayaaak.Tools.SwipeDismissTouchListener;
import com.ibtikar.apps.wayaaak.Tools.WayaaakAPP;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;

import static okhttp3.MultipartBody.FORM;

public class SearchDialogFragment  extends DialogFragment {

    VolleySimple volleySimple;
    RecyclerView result_list;
    List_Adapter listAdapter;
    LinearLayout empty_holder;
    EditText search_txt;
    ImageView search_ico, filter_ico, btnClose;
    Map<String, String> filtermap = new HashMap<>();
    User user;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.search_dialog_fragment, container, false);
        if (getArguments() != null) {
            String cityId = getArguments().getString(WayaaakAPP.KEY_CITY_ID);
            String catId = getArguments().getString(WayaaakAPP.KEY_CAT_ID);
            String areaId = getArguments().getString(WayaaakAPP.KEY_AREA_ID);
            search2(catId, cityId, areaId);
        }
        volleySimple = VolleySimple.getInstance(getContext());
        if (WayaaakAPP.getUserLoginState(getContext())) {
            user = WayaaakAPP.getUserLoginInfo(getContext());
            filtermap.put("user", String.valueOf(user.getId()));
        }
        initView(rootView);
        listener();

        setupSearchBox();

        return rootView;
    }

    private void setupSearchBox() {
        search_txt.requestFocus();
        //InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        //imm.showSoftInput(search_txt, InputMethodManager.SHOW_IMPLICIT);
       /* search_txt.requestFocus();
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(search_txt, InputMethodManager.SHOW_IMPLICIT);*/
        final Dialog dialog = new Dialog(getActivity());
        search_txt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                }
            }
        });

        search_txt.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {

                if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                        actionId == EditorInfo.IME_ACTION_DONE ||
                        actionId == EditorInfo.IME_ACTION_NEXT ||
                        keyEvent.getAction() == KeyEvent.ACTION_DOWN ||
                        keyEvent.getAction() == KeyEvent.KEYCODE_ENTER)
                {
                    filtermap.put("keyword", search_txt.getText().toString());
                    search(filtermap);
                    hideKeyboard();
                    return true;
                }


                return false;
            }
        });
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final LinearLayout root = new LinearLayout(getActivity());
        root.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(root);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.TOP);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        return dialog;
    }

    public void initView(View view) {
        result_list = view.findViewById(R.id.result_list);
        search_ico = view.findViewById(R.id.search_ico);
        filter_ico = view.findViewById(R.id.filter_ico);
        search_txt = view.findViewById(R.id.search_txt);
        empty_holder = view.findViewById(R.id.no_item_holder);
        btnClose = view.findViewById(R.id.im_btn_close);
        result_list.setLayoutManager(new GridLayoutManager(getContext(), 2));
        int resId = R.anim.layout_animation_fall_down;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(getActivity(), resId);
        result_list.setLayoutAnimation(animation);

    }

    public void listener() {
        search_ico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filtermap.put("keyword", search_txt.getText().toString());
                search(filtermap);
                hideKeyboard();
            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        filter_ico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filter_ico.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Filter_Bottom_Sheet filter_bottom_sheet = new Filter_Bottom_Sheet();
                        filter_bottom_sheet.show(getFragmentManager(), filter_bottom_sheet.getTag());
                        filter_bottom_sheet.setOnActionListner(new Filter_Bottom_Sheet.onActionListener() {
                            @Override
                            public void onSubmit(Map<String, String> map) {
                                System.out.println(map);
                                filtermap.putAll(map);
                                init();
                            }

                            @Override
                            public void onReset() {
                                filtermap.clear();
                                if (WayaaakAPP.getUserLoginState(getContext())) {
                                    filtermap.put("user", String.valueOf(user.getId()));
                                }
                                init();
                            }
                        });
                    }
                });
            }
        });
    }

    public void init() {
        String link = "allproducts";
        if (user != null) link += "/" + user.getId();
        ProgressDialog progressDialog = ProgressDialog.show(getContext(), "انتظر من فضلك", "", false, false);
        volleySimple.asyncStringGet(WayaaakAPP.BASE_URL + link, new VolleySimple.NetworkListener<String>() {
            @Override
            public void onResponse(String s) {
                ListResponse response = new Gson().fromJson(s, ListResponse.class);
                if (response.getStatus().equals("OK")) {
                    listAdapter = new List_Adapter(getContext(), getFragmentManager(), response.getProducts());
                    result_list.setAdapter(listAdapter);
                    if (listAdapter.getItemCount() != 0) empty_holder.setVisibility(View.GONE);
                    else {
                        empty_holder.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Exception e) {

            }
        }, progressDialog);
    }

    public void search(Map<String, String> map) {
        ProgressDialog progressDialog = ProgressDialog.show(getContext(), "انتظر من فضلك", "", false, false);
        volleySimple.asyncStringPost(WayaaakAPP.BASE_URL + "search", map, new VolleySimple.NetworkListener<String>() {
            @Override
            public void onResponse(String s) {

                Log.d("TAG", "onResponse: " + s);
                ListResponse response = new Gson().fromJson(s, ListResponse.class);
                if (response.getStatus().equals("OK")) {
                    listAdapter = new List_Adapter(getContext(), getFragmentManager(), response.getProducts());
                    result_list.setAdapter(listAdapter);
                    if (listAdapter.getItemCount() != 0) empty_holder.setVisibility(View.GONE);
                    else {
                        empty_holder.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Exception e) {

            }
        }, progressDialog);
    }

    private void search2(String catlId, String cityId, String areaId)
    {
        if (cityId == null)
            cityId = "";
        if (areaId == null)
            areaId = "";
        if (catlId == null)
            catlId = "";
        OkHttpClient client = new OkHttpClient();
        RequestBody body;
        body = new MultipartBody.Builder()
                .setType(FORM)
                .addFormDataPart("category", catlId)
                .addFormDataPart("city", cityId)
                .addFormDataPart("area",areaId).build();


        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(WayaaakAPP.BASE_URL + "search")
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {

               // Log.d("TAG", "onResponse: " + response.body().string());
                ListResponse response1 = new Gson().fromJson(response.body().string(), ListResponse.class);
                if (response1.getStatus().equals("OK")) {
                    //Log.d("TAG", "onResponse: " + response.body().string());

                    listAdapter = new List_Adapter(getContext(), getFragmentManager(), response1.getProducts());
//                    Log.d("TAG", "onResponse: " + response1.getProducts().get(0).getIsfavourite());
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            result_list.setAdapter(listAdapter);

                            if (listAdapter.getItemCount() != 0) empty_holder.setVisibility(View.GONE);
                            else {
                                empty_holder.setVisibility(View.VISIBLE);
                            }
                        }
                    });

                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
       Window window = getDialog().getWindow();
        window.getDecorView().setOnTouchListener(new SwipeDismissTouchListener(window.getDecorView(), null, new SwipeDismissTouchListener.OnDismissCallback() {

            @Override
            public void onDismiss(View view, Object token) {
                dismiss();
            }
        }));
    }
    public void hideKeyboard() {
        View view = getActivity().findViewById(android.R.id.content);
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
