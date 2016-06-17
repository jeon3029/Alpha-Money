package com.example.patabear.project_layout;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;

public class MainActivity extends ActionBarActivity {

    // 데이터추가때 필요한 칸들
    TextView editTextDate;
    TextView editTextPrice;
    TextView editTextStore;
    TextView editTextCategory;
    TextView editTextMemo;
    ToggleButton toggleButton;
    TextView totalEI;

    // DB선언
    DBHelper mydb;
    ArrayList<struct> data;

    // DB로부터 값을 받아오기위한 변수들
    ArrayList<struct> arrayList;
    ArrayList<struct> arrayList_income;   // DB의 모든 데이터를 가져온 리스트
    ArrayList<struct> arrayList_expense;
    ArrayList<struct> arrayList_month;
    ArrayList<struct> arrayList_week;
    ArrayList<struct> arrayList_day;

    // 프래그먼트들
    Show_Chart_Month show_chart_month = new Show_Chart_Month();
    Show_Chart_Paymentmethod show_chart_paymentmethod = new Show_Chart_Paymentmethod();
    Show_Chart_Time show_chart_time = new Show_Chart_Time();
    Show_Chart_Week show_chart_week = new Show_Chart_Week();
    Show_Date_Day show_date_day = new Show_Date_Day();
    Show_Date_Month show_date_month = new Show_Date_Month();
    Show_Date_Week show_date_week = new Show_Date_Week();
    Show_List show_list = new Show_List();
    Show_Period show_period = new Show_Period();
    Show_Totalprice show_totalprice = new Show_Totalprice();
    Show_Basic show_basic = new Show_Basic();
    Add_Data add_data;

    // 버튼들
    Button buttonToday;
    Button buttonWeek;
    Button buttonMonth;
    Button buttonSpend;
    Button buttonIncome;
    Button buttonStatistics;


    Intent intent; // 보낼때 사용 보내는 형식은 ID, date, ~ ,gridY 를 putintent 하여 전송 + option
    // 받을때 사용 받는 형식은 From (어느 액티비티에서 왔는지), Del, ID (삭제여부와 삭제할 아이디)  + intent에 있던것들  추후 추가 가능
    // result 사용함

    // 상태를 저장하는 변수들
    int boundary1 = 100000;//노란 신호등 경계
    int boundary2 = 200000;//빨간 신호등 경계
    int period = 1;
    int basic = 1;
    int statistics = 1;
    int add_data_is_on = 0;
    int signal = 1; // 1= green 2 = yellow 3 = red
    // day,week,month 상태를 저장하기위한 period와 지출,수입,통계중 어느상태인지 저장하기위한 basic, 통계중 어느상태인지 저장하는 statistics
    // period 가 1==day 2==week 3==month
    // basic 이 1==지출 2==수입 3==통계
    // statistics 가 1==Month 2==Paymentmethod 3==Time 4==Week

    FragmentManager fragmentManager = getSupportFragmentManager();

    // 리스트와 값들을 출력하기위한 어댑터 및 뷰
    CustomAdapter customAdapter ;
    ListView listView;
    TextView priceView;
    ImageView signalLightView;

    // 프래그먼트실행시 반환 뷰
    View rootViewBasic;
    View rootViewList;
    View rootViewPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 이 버튼들은 프래그먼트에서 잡아온 버튼들로 액티비티 전환이나 프래그먼트 전환시 새로 버튼들을 잡아와야함
        buttonToday = (Button) findViewById(R.id.buttonToday);
        buttonWeek = (Button) findViewById(R.id.buttonWeek);
        buttonMonth = (Button) findViewById(R.id.buttonMonth);
        buttonSpend = (Button) findViewById(R.id.buttonSpend);
        buttonIncome = (Button) findViewById(R.id.buttonIncome);
        buttonStatistics = (Button) findViewById(R.id.buttonStatistics);

        mydb = new DBHelper(this);

        //OnItemClickListerner 추가

        arrayList_expense = mydb.onGetalldata(1); // DB의 모든 데이터
        arrayList_income = mydb.onGetalldata(2);


        if (savedInstanceState == null) {
            //프래그먼트 5가지 세팅
            fragmentManager.beginTransaction().add(R.id.layout1, show_basic).commit();
            fragmentManager.beginTransaction().add(R.id.layout2, show_date_day).commit();
            fragmentManager.beginTransaction().add(R.id.layout3, show_list).commit();
            fragmentManager.beginTransaction().add(R.id.layout4, show_totalprice).commit();
            fragmentManager.beginTransaction().add(R.id.layout5, show_period).commit();
        }

    }

    @Override
    protected void onActivityResult(int RequestCode, int ResultCode, Intent data) {
        switch (ResultCode) {
            case RESULT_OK:
                struct temp = new struct();
                int From, Del, ID, option;
                From = data.getIntExtra("From", 0);
                Del = data.getIntExtra("Del", 0);
                ID = data.getIntExtra("ID", 0);
                option = data.getIntExtra("option", 0);
                if (From == 1) {
                    if(Del == 1) {
                        mydb.onDeletedata(option, ID);
                    }
                    else {
                        temp.ID = data.getIntExtra("ID", 0);
                        temp.date = data.getStringExtra("date");
                        temp.price = data.getStringExtra("price");
                        temp.storeName = data.getStringExtra("storeName");
                        temp.category = data.getStringExtra("category");
                        temp.memo = data.getStringExtra("memo");
                        temp.gridX = data.getDoubleExtra("gridX", 0.000000);
                        temp.gridY = data.getDoubleExtra("gridY", 0.000000);

                        mydb.onUpdate(temp.date, temp.price, temp.storeName, temp.category, temp.memo
                                , Double.toString(temp.gridX), Double.toString(temp.gridY), option, temp.ID);
                    }
                }
                break;
            default:
                super.onActivityResult(RequestCode, ResultCode, data);
        }
        Refresh();
    }

    public class Show_Basic extends Fragment {

        public Show_Basic() {

        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            rootViewBasic = inflater.inflate(R.layout.show_basic, container, false);

            basic = 1;

            signalLightView = (ImageView) rootViewBasic.findViewById(R.id.signalLight);

            return rootViewBasic;
        }
    }

    public class Show_List extends Fragment {

        public Show_List() {

        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            rootViewList = inflater.inflate(R.layout.show_list, container, false);

            if(basic == 1)
                arrayList = arrayList_expense;
            else // option == 2   income
                arrayList = arrayList_income;

            customAdapter = new CustomAdapter(arrayList,getApplicationContext());
            listView = (ListView) rootViewList.findViewById(R.id.ListView_1);
            listView.setAdapter(customAdapter);

            //List View Item Click Event
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //TODO : 아이템 클릭 시 상세 정보 출력
                    if(basic == 1)
                        arrayList = arrayList_expense;
                    else // option == 2   income
                        arrayList = arrayList_income;

                    struct temp = arrayList.get(position);

                    intent = new Intent(getApplicationContext(), DetailActivity.class);

                    intent.putExtra("ID", temp.ID);
                    intent.putExtra("date", temp.date);
                    intent.putExtra("price", temp.price);
                    intent.putExtra("storeName", temp.storeName);
                    intent.putExtra("category", temp.category);
                    intent.putExtra("memo", temp.memo);
                    intent.putExtra("gridX", temp.gridX);
                    intent.putExtra("gridY", temp.gridY);
                    intent.putExtra("option", basic);

                    startActivityForResult(intent, 1);
                    //상세정보에는 가걱 상호명 ...등등 + 삭제, 지도 팝업창 띄우기
                }
            });
            return rootViewList;
        }
    }

    public class Show_Totalprice extends Fragment {
        public Show_Totalprice() {

        }
        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            if(basic == 1)
                arrayList = arrayList_expense;
            else // option == 2   income
                arrayList = arrayList_income;

            rootViewPrice = inflater.inflate(R.layout.show_totalprice, container, false);

            priceView = (TextView) rootViewPrice.findViewById(R.id.totalprice);
            totalEI = (TextView)rootViewPrice.findViewById(R.id.totalEI);

            //현재 상태에 따른 totalEI textView text 변환
            if(basic == 1){
                totalEI.setText("총 지출 금액");
            }
            else totalEI.setText("총 수입 금액");

            //TODO : 기간에 따른 다른 array list --> day, week, Month arraylist 나눠서 불러 실행
            if(period == 1)
                priceView.setText(CalcSum(arrayList));
            else if(period == 2)
                priceView.setText(CalcSum(arrayList));
            else if(period == 3)
                priceView.setText(CalcSum(arrayList));

            return rootViewPrice;
        }
    }

    public String CalcSum(ArrayList<struct> array) {
        int count = array.size();
        int sum = 0;
        String total;
        struct temp;

        for(int i = 0; i < count; i++)
        {
            temp = array.get(i);
            if(temp.price.length() == 0)
                continue;
            sum += Integer.parseInt(temp.price);
        }

        total = Integer.toString(sum);

        //TODO : 여기 신호등 상태를 저장하는 변수를 데이터베이스에 저장하여 꺼내써야함 수정필요

        if(basic == 1) {
            if(sum > boundary2) {
                signalLightView.setImageResource(R.drawable.redcircle);
                if(signal != 3){
                    signal = 3;
                    Toast toast = Toast.makeText(this, "Red!", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
            else if(sum >= boundary1) {
                if (signal != 2) {
                    signal = 2;
                    Toast toast = Toast.makeText(this, "Yellow!", Toast.LENGTH_SHORT);
                    toast.show();
                }
                signalLightView.setImageResource(R.drawable.yellowcircle);
            }
            else {
                signalLightView.setImageResource(R.drawable.greencircle);

                if(signal != 1){
                    signal = 1;
                    Toast toast = Toast.makeText(this, "Green!", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        }

        return total;
    }

    // 화면 리프레쉬
    public void Refresh() {
        arrayList_expense = null;
        arrayList_income = null;
        arrayList = null;

        arrayList_expense = mydb.onGetalldata(1);
        arrayList_income = mydb.onGetalldata(2);

        if(basic == 1) {
            arrayList = arrayList_expense;
            totalEI.setText("총 지출 금액");
        }
        else { // option == 2   income
            arrayList = arrayList_income;
            totalEI.setText("총 수입 금액");
        }

        customAdapter = new CustomAdapter(arrayList,getApplicationContext());
        listView.setAdapter(customAdapter);

        priceView.setText(CalcSum(arrayList));
    }

    public void onBackPressed() {
        if(add_data_is_on == 1) {
            add_data_is_on = 0;

            fragmentManager.beginTransaction().remove(add_data).commit();

            fragmentManager.beginTransaction().show(show_basic).commit();

            if (period == 1)
                fragmentManager.beginTransaction().show(show_date_day).commit();
            if (period == 2)
                fragmentManager.beginTransaction().show(show_date_week).commit();
            else
                fragmentManager.beginTransaction().show(show_date_month).commit();


            fragmentManager.beginTransaction().show(show_list).commit();

            if (basic != 3) {
                fragmentManager.beginTransaction().show(show_totalprice).commit();
                fragmentManager.beginTransaction().show(show_period).commit();
            }
        }
        else
            super.onBackPressed();
    }

    public void clickbuttonSpend(View v) { // 지출버튼 클릭시
        if(basic == 1)
            return;

        else {
            if (basic == 3) {
                fragmentManager.beginTransaction().show(show_totalprice).commit();
                fragmentManager.beginTransaction().show(show_period).commit();
            }
        }
            basic = 1;

            Refresh();
    }

    public void clickbuttonIncome(View v) { // 수입버튼 클릭시
        if(basic == 2)
            return;

        else {
            if (basic == 3) {
                fragmentManager.beginTransaction().show(show_totalprice).commit();
                fragmentManager.beginTransaction().show(show_period).commit();
            }
        }
            basic = 2;

        Refresh();

    }

    public void clickbuttonStatistics(View v) { // 통계버튼 클릭시
        if(basic == 3)
            return;
        else {
            basic = 3;
            if(period != 3)
                fragmentManager.beginTransaction().replace(R.id.layout2, show_date_month).commit();


            fragmentManager.beginTransaction().hide(show_totalprice).commit();
            fragmentManager.beginTransaction().hide(show_period).commit();


        }
    }

    public void clickbuttonToday(View v) { // 오늘버튼 클릭시
        if(period == 1)
            return;
        else {
            period = 1;

            fragmentManager.beginTransaction().replace(R.id.layout2, show_date_day).commit();

        }
    }

    public void clickbuttonWeek(View v) { // 이번주 버튼 클릭시
        if(period == 2)
            return;
        else {
            period = 2;

            fragmentManager.beginTransaction().replace(R.id.layout2, show_date_week).commit();

        }
    }

    public void clickbuttonMonth(View v) { //  이번달 버튼 클릭시
        if(period == 3)
            return;
        else {
            period = 3;

            fragmentManager.beginTransaction().replace(R.id.layout2, show_date_month).commit();
        }
    }

    // statistics 에 대한 버튼들의 이름은 buttonStatMonth, buttonStatPaymentmethod, buttonStatTime, buttonStatWeek 로 하는걸로

    public void clickbuttonStatMonth(View v) { // 통계의 월별클릭시
        if(statistics == 1)
            return;
        else {
            statistics = 1;
        }
    }

    public void clickbuttonStatPaymentmethod(View v) { // 통계의 결제수단클릭시
        if(statistics == 2)
            return;
        else {
            statistics = 2;
        }
    }

    public void clickbuttonStatTime(View v) { // 통계의 시간별 클릭시
        if(statistics == 3)
            return;
        else {
            statistics = 3;
        }
    }

    public void clickbuttonStatWeek(View v) { // 통계의 주별 클릭시
        if(statistics == 4)
            return;
        else {
            statistics = 4;
        }
    }

    public void clickAddData(View v) {
        add_data = new Add_Data();
        add_data_is_on = 1;

        fragmentManager.beginTransaction().hide(show_basic).commit();

        if(period == 1)
            fragmentManager.beginTransaction().hide(show_date_day).commit();
        if(period == 2)
            fragmentManager.beginTransaction().hide(show_date_week).commit();
        else
            fragmentManager.beginTransaction().hide(show_date_month).commit();

        fragmentManager.beginTransaction().hide(show_list).commit();

        if(basic != 3) {
            fragmentManager.beginTransaction().hide(show_totalprice).commit();
            fragmentManager.beginTransaction().hide(show_period).commit();
        }
        fragmentManager.beginTransaction().add(R.id.maincontainer, add_data).addToBackStack(null).commit();
    }

    public void savedata(View v) {
        editTextDate = (TextView) findViewById(R.id.editTextDate);
        editTextPrice = (TextView) findViewById(R.id.editTextPrice);
        editTextStore = (TextView) findViewById(R.id.editTextStore);
        editTextCategory = (TextView) findViewById(R.id.editTextCategory);
        editTextMemo = (TextView) findViewById(R.id.editTextMemo);
        toggleButton = (ToggleButton) findViewById(R.id.toggleButton);
        int option;
        if(toggleButton.isChecked()){
            option=2;
        }
        else option = 1;
        mydb.onInsertdata(editTextDate.getText().toString(), editTextPrice.getText().toString(), editTextStore.getText().toString(), editTextCategory.getText().toString(), editTextMemo.getText().toString(), "0", "0",option);

        add_data_is_on = 0;

        fragmentManager.popBackStack();
        fragmentManager.beginTransaction().remove(add_data).commit();
        fragmentManager.beginTransaction().show(show_basic).commit();

        if (period == 1)
            fragmentManager.beginTransaction().show(show_date_day).commit();
        if (period == 2)
            fragmentManager.beginTransaction().show(show_date_week).commit();
        else
            fragmentManager.beginTransaction().show(show_date_month).commit();


        fragmentManager.beginTransaction().show(show_list).commit();

        if (basic != 3) {
            fragmentManager.beginTransaction().show(show_totalprice).commit();
            fragmentManager.beginTransaction().show(show_period).commit();
        }
        Refresh();
        //TODO : period 에 따라 데이터 분류해서 더해줘야함 현재는 지출, 수입의 모든 데이터를 더해줌

    }

}

// add_data나 설정에 갔다온 후에 값을 업데이트하기위한 refresh 함수를 추가해 보는것도 생각해보자