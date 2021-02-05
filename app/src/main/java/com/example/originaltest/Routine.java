package com.example.originaltest;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Calendar;
import java.util.Date;

public class Routine extends Fragment {
    // 설문조사 탭 (나이, 생일, 달력 창)
    TextView AgeView, birthDateView;
    DatePickerDialog datePickerDialog;
    // 회원가입 탭 (약관 동의, 텍스트)
    CheckBox all_agree_box, agree_terms, agree_personal_info;
    TextView email, password, retype_password, name, cellphone, birth_date;
    String emailValidation = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@gmail.com";
    Button submit;
    // 로그인 모달 창
    Dialog dialog;
    String format;
    public static Routine newInstance () {
        Routine routine = new Routine();
        return routine;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_routine, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        // 루틴 버튼 클릭 시 로그인 모달창 띄우기
        Button routineBtn = getView().findViewById(R.id.routine);
        routineBtn.setOnClickListener(v -> {
            isLogined(false);
        });
    }
    // TextView 변경 실시간 화
    public void InitializeView() {
        AgeView = dialog.findViewById(R.id.text_view_age);
        birthDateView = dialog.findViewById(R.id.text_view_birth_date);
    }
    // 달력 클릭 시 TextView에 값 전달
    public DatePickerDialog.OnDateSetListener dataPickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            Calendar c = Calendar.getInstance();
            c.set(Calendar.YEAR, year);
            c.set(Calendar.MONTH, month);
            c.set(Calendar.DAY_OF_MONTH, day);
            AgeView.setText(Integer.toString(calculateAge(c.getTimeInMillis())));
            birthDateView.setText((month + 1) + "/" + day + "/" + year);
        }
    };
    // 나이 계산 현재 연도 - 탄생 연도 (단, 현재 연도가 탄생 월보다 작을 경우 나이 -1)
    int calculateAge(long date) {
        Calendar dob = Calendar.getInstance();
        dob.setTimeInMillis(date);

        Calendar today = Calendar.getInstance();

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
        if(today.get(Calendar.DAY_OF_MONTH) < dob.get(Calendar.DAY_OF_MONTH)) {
            age --;
        }
        return age;
    }

    /**
     * 로그인 여부 확인
     * @param option
     */
    private void isLogined (Boolean option) {
        if(option) {
            //로그인 되어있는 경우
        } else {
            // 로그인 안되어있는 경우
            // 모달창 내부 버튼 설정
            dialog = new Dialog(getContext());
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setContentView(R.layout.style_login);
            // 회원 가입 버튼 클릭시 창 이동
            Button signUpBtn = dialog.findViewById(R.id.btn_sign_up);
            signUpBtn.setOnClickListener(v1 -> {
                View signInPage = dialog.findViewById(R.id.sign_in_page);
                signInPage.setVisibility(View.INVISIBLE);
                View signUpPage = dialog.findViewById(R.id.sign_up_page);
                signUpPage.setVisibility(View.VISIBLE);
            });

            // 회원 가입 창 확인식
            password = dialog.findViewById(R.id.password);
            retype_password = dialog.findViewById(R.id.retype_password);
            name = dialog.findViewById(R.id.name);
            cellphone = dialog.findViewById(R.id.cellphone);
            birth_date = dialog.findViewById(R.id.birth_date);
            email = dialog.findViewById(R.id.email);
            submit = dialog.findViewById(R.id.submit);
            // ▽▽▽▽▽▽▽▽▽▽▽▽▽▽▽▽▽▽ 떡 코딩, 추후에 능지 상승 시 수정할것▽▽▽▽▽▽▽▽▽▽▽▽▽▽▽▽▽▽

            // 6글자 이상 , @gmail로 끝나는지 확인
            email.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }
                @Override
                public void afterTextChanged(Editable s) {
                    String inputEmail = email.getText().toString().trim();
                    if(inputEmail.matches(emailValidation)&& s.length() > 15) {
                        email.setBackgroundColor(Color.TRANSPARENT);
                    } else {
                        email.setBackgroundColor(Color.GRAY);
                        submit.setClickable(false);
                    }
                }
            });

            // △△△△△△△△△△△△△△△△△△ 떡 코딩, 추후에 능지 상승 시 수정할것△△△△△△△△△△△△△△△△△△
            all_agree_box = dialog.findViewById(R.id.agree_all_terms);
            agree_terms = dialog.findViewById(R.id.agree_terms);
            agree_personal_info = dialog.findViewById(R.id.agree_personal_info);
            all_agree_box.setOnClickListener(v -> {onCheckChanged((CheckBox)v);});
            agree_terms.setOnClickListener(v -> {onCheckChanged((CheckBox)v);});
            agree_personal_info.setOnClickListener(v -> {onCheckChanged((CheckBox)v);});
            all_agree_box.setChecked(agree_terms.isChecked() && agree_personal_info.isChecked());

            // 설문조사 탭 (임시 보류)
            InitializeView();
            // birthDate 버튼 클릭 시 달력 호출
            Button birthDateBtn = dialog.findViewById(R.id.btn_birth_date);
            birthDateBtn.setOnClickListener(v2 -> {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                // 달력 시작을 현재 날짜로 설정
                datePickerDialog = new DatePickerDialog(getActivity(), dataPickerListener, year, month, day);
                datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());;
                datePickerDialog.show();
            });
            // 닫기 버튼 클릭시 화면 닫힘
            ImageView closeBtn = dialog.findViewById(R.id.close);
            closeBtn.setOnClickListener(v12 -> dialog.dismiss());
            dialog.show();
        }
    }

    /**
     * 약관 동의 알고리즘
     * @param checkBox
     */
    private void onCheckChanged(CheckBox checkBox) {
        switch(checkBox.getId()) {
            case R.id.agree_all_terms :
                if(all_agree_box.isChecked()) {
                    agree_terms.setChecked(true);
                    agree_personal_info.setChecked(true);
                } else {
                    agree_terms.setChecked(false);
                    agree_personal_info.setChecked(false);
                }
            default:
                all_agree_box.setChecked(agree_terms.isChecked()&&agree_personal_info.isChecked());
        }
    }

    /**
     * 회원가입 정보 일치 확인 알고리즘
     */
    private void signUpCheck() {





    }

}