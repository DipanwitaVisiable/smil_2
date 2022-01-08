package com.visiabletech.smilmobileapp.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.library.NavigationBar;
import com.library.NvTab;
import com.visiabletech.smilmobileapp.Adapter.TermExamQuestionAdapter;
import com.visiabletech.smilmobileapp.Adapter.SmallQuizQuestionAdapter;
import com.visiabletech.smilmobileapp.LoginActivity;
import com.visiabletech.smilmobileapp.Pogo.AnswerTable;
import com.visiabletech.smilmobileapp.Pogo.QuestionTable;
import com.visiabletech.smilmobileapp.R;
import com.visiabletech.smilmobileapp.Utils.CallStateListener;
import com.visiabletech.smilmobileapp.Utils.Const;
import com.visiabletech.smilmobileapp.Utils.CustomCountDownTimer;
import com.visiabletech.smilmobileapp.Utils.InternetConnection;
import com.visiabletech.smilmobileapp.Utils.NonSwipeableViewPager;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class TermExamQuestionActivity extends AppCompatActivity implements NavigationBar.OnTabClick{
  private ViewPager viewPager;
  private AVLoadingIndicatorView av_caf_loader;
  private ImageView noDataImage;
  private TextView tryAgainText;
  Button finishButton, submitButton, preButton;
  ProgressBar answerLoad;
  TextView textTimer, totalQuestion;
  String duration;
  SimpleDateFormat df;
  Calendar c;
  private String currentTime;
  public static CustomCountDownTimer countDownTimer;
  Context context;
  private ArrayList<QuestionTable> quesList = new ArrayList<>();
  private ArrayList<AnswerTable> ansList;
  HashMap<String, ArrayList<AnswerTable>> listHashMap = new HashMap<>();
  String quizId, test_id, test_finish_time, reg_id;
  NavigationBar bar;
  RelativeLayout l1_top;
  public long  elapsed_time;

  private StringRequest stringRequest_saveQuiz, stringRequest_getQuestion, stringRequest_submitTest, stringRequest_singleQues;
  private RequestQueue requestQueue_saveQuiz, requestQueue_getQuestion, requestQueue_submitTest, requestQueue_singleQues;
  private Spinner spinner;
  private int max_page=0;

  public TelephonyManager tm;
  private CallStateListener callStateListener;
  private String exam_ques;


  @SuppressLint("SimpleDateFormat")
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_term_exam_question);

    context = TermExamQuestionActivity.this;

    //Start- Register telephony manager to detect phone call
    callStateListener=new CallStateListener(context);
    tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
    tm.listen(callStateListener, PhoneStateListener.LISTEN_CALL_STATE);
    //End- Register telephony manager to detect phone call

    answerLoad = findViewById(R.id.ans_load);
    textTimer = findViewById(R.id.textTimer);
    bar = findViewById(R.id.navBar);
    totalQuestion = findViewById(R.id.text_no_of_question);
    finishButton = findViewById(R.id.test_finish_button);
    submitButton = findViewById(R.id.submit_ans);
    preButton = findViewById(R.id.pre_ans);
    viewPager = findViewById(R.id.viewpager);
    av_caf_loader = findViewById(R.id.av_caf_loader);
    noDataImage = findViewById(R.id.no_data_image);
    tryAgainText = findViewById(R.id.try_again_text);
    preButton.setVisibility(View.GONE);
    submitButton.setVisibility(View.GONE);
    c = Calendar.getInstance();
    df = new SimpleDateFormat("HH:mm:ss");
    currentTime = df.format(c.getTime());
    bar.setOnTabClick(this);
    l1_top=findViewById(R.id.l1_top);

    SharedPreferences device_token = getSharedPreferences("FIREBASE_TOKEN", Context.MODE_PRIVATE);
    reg_id=device_token.getString("FIREBASE_ID", "");

    preButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (viewPager.getCurrentItem() > 0) {
          viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
          bar.setCurrentPosition(viewPager.getCurrentItem());

          if (viewPager.getCurrentItem() == 0) {
            preButton.setVisibility(View.GONE);
          }
        }
      }
    });

    Intent intent = getIntent();
    quizId = intent.getStringExtra("quiz_id");
    Const.QUIZ_ID=intent.getStringExtra("quiz_id");
    duration = intent.getStringExtra("time");
    tryAgainText.setOnClickListener(new View.OnClickListener() {
      @RequiresApi(api = Build.VERSION_CODES.KITKAT)
      @Override
      public void onClick(View v) {
        if (InternetConnection.checkConnection(context)) {
          getAllQuestion();
        } else {
          Toast.makeText(context, "No Internet connection", Toast.LENGTH_SHORT).show();
        }
      }
    });

    submitButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
          WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE); //To disable user interaction
        saveQuiz();
      }
    });

    getAllQuestion();

  }
  private void setup(boolean reset, int count) {
    if (reset)
      bar.resetItems();
    bar.setTabCount(count);
    bar.animateView(3000);
    bar.setCurrentPosition(viewPager.getCurrentItem() <= 0 ? 0 : viewPager.getCurrentItem());
  }

  @Override
  public void onTabClick(int touchPosition, NvTab prev, NvTab nvTab) {
    if (touchPosition>max_page)
    {
      bar.setCurrentPosition(viewPager.getCurrentItem());
      Toast.makeText(context, "Please answer this question first", Toast.LENGTH_LONG).show();
    }
    else {
      nvTab=nvTab;
      viewPager.setCurrentItem(touchPosition);
      bar.setCurrentPosition(viewPager.getCurrentItem());
    }

  }
  public void finishTest(View view) {

    ContextThemeWrapper themedContext;
    themedContext = new ContextThemeWrapper(context, android.R.style.Theme_Holo_Light_Dialog_NoActionBar);
    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(themedContext);
    dialogBuilder.setCancelable(false);
    dialogBuilder.setTitle("Finish Test");
    dialogBuilder.setIcon(R.drawable.ic_alarm_clock);
    dialogBuilder.setMessage("Are you sure to finish the test?");
    dialogBuilder.setPositiveButton("Sure", null);
    dialogBuilder.setNegativeButton("I'm just kidding", null);
    final AlertDialog alertDialog = dialogBuilder.create();
    alertDialog.show();

    Button positiveButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
    positiveButton.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
    positiveButton.setAllCaps(false);
    positiveButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        onPositiveButtonClicked(alertDialog);

      }
    });

    Button negativeButton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
    negativeButton.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
    negativeButton.setAllCaps(false);
    negativeButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        onNegativeButtonClicked(alertDialog);

      }
    });


  }

  @SuppressLint("SimpleDateFormat")
  private void onPositiveButtonClicked(AlertDialog alertDialog) {
    Intent i = new Intent(context, ExamFinishActivity.class);
    Bundle bundle = new Bundle();
    bundle.putString("test_id", test_id);
    i.putExtras(bundle);
    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
    startActivity(i);

    alertDialog.dismiss();
    if (countDownTimer==null) {
    }
    else {
      countDownTimer.cancel();
    }
    finish();
  }

  private void onNegativeButtonClicked(AlertDialog alertDialog) {
    alertDialog.dismiss();
  }

  private void saveQuiz() {
    requestQueue_saveQuiz= Volley.newRequestQueue(context);
    answerLoad.setVisibility(View.VISIBLE);
    stringRequest_saveQuiz=new StringRequest(Request.Method.POST, Const.BASE_SERVER + "term_save_question",
      new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
          try {
            JSONObject jsonObject=new JSONObject(response);
            String status=jsonObject.getString("status");
            String message=jsonObject.getString("message");
            if (status.equalsIgnoreCase("200")) {
              if (submitButton.getText().toString().equalsIgnoreCase("Save & Finish")) {
                submitTestApiCall();
                countDownTimer.cancel();
                Const.answerStoreHash.clear();
                Const.testquestionAnswerStoreHash.clear();

              }
              if (submitButton.getText().toString().equalsIgnoreCase("Save & Next")) {
                answerLoad.setVisibility(View.GONE);
                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE); //To enable user interaction
                preButton.setVisibility(View.VISIBLE);//For disable swipe

              }
              bar.setCurrentPosition(viewPager.getCurrentItem());

            } else {
              answerLoad.setVisibility(View.GONE);
              Toast.makeText(context, message, Toast.LENGTH_LONG).show();
            }
          } catch (JSONException e) {
            e.printStackTrace();
            answerLoad.setVisibility(View.GONE);
          }

        }
      }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {

        Toast.makeText(context, "Try again", Toast.LENGTH_SHORT).show();
        answerLoad.setVisibility(View.GONE);

      }
    }

    ){

      @Override
      protected Map<String, String> getParams() throws AuthFailureError {
        Map<String,String>myParams=new HashMap<String, String>();
        myParams.put("student_id", Const.USER_ID);
        myParams.put("exam_id", quizId);
        myParams.put("question_id", Const.CHOOSE_QUESTION_ID);
        myParams.put("answer_id", Const.ANSWER_ID);
        myParams.put("exam_taken_id", test_id);

        return myParams;
      }
    };

    // Start- To remove timeout error
    stringRequest_saveQuiz.setRetryPolicy(new RetryPolicy() {
      @Override
      public int getCurrentTimeout() {
        return 50000;
      }

      @Override
      public int getCurrentRetryCount() {
        return 50000;
      }

      @Override
      public void retry(VolleyError error) throws VolleyError {

      }
    });
    // End- To remove timeout error

    requestQueue_saveQuiz.add(stringRequest_saveQuiz);

  }

  private void submitTestApiCall() {

    requestQueue_submitTest= Volley.newRequestQueue(context);
    stringRequest_saveQuiz=new StringRequest(Request.Method.POST, Const.BASE_SERVER + "term_submit_test",
      new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
          try {
            answerLoad.setVisibility(View.GONE);
            JSONObject jsonObject=new JSONObject(response);
            String status=jsonObject.getString("status");
            if (status.equalsIgnoreCase("200")) {
              Intent i = new Intent(context, ExamFinishActivity.class);
              Bundle bundle = new Bundle();
              bundle.putString("test_id", test_id);
              i.putExtras(bundle);
              i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
              startActivity(i);
              finish();

            } else {

              Toast.makeText(context, "Test not submitted", Toast.LENGTH_LONG).show();
            }
          } catch (JSONException e) {
            e.printStackTrace();
            answerLoad.setVisibility(View.GONE);
          }

        }
      }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {

        Toast.makeText(context, "Try again", Toast.LENGTH_SHORT).show();
        answerLoad.setVisibility(View.GONE);

      }
    }

    ){

      @Override
      protected Map<String, String> getParams() throws AuthFailureError {
        Map<String,String>myParams=new HashMap<String, String>();
        myParams.put("student_id", Const.USER_ID);
        myParams.put("exam_taken_id", test_id);

        return myParams;
      }
    };

    // Start- To remove timeout error
    stringRequest_saveQuiz.setRetryPolicy(new RetryPolicy() {
      @Override
      public int getCurrentTimeout() {
        return 50000;
      }

      @Override
      public int getCurrentRetryCount() {
        return 50000;
      }

      @Override
      public void retry(VolleyError error) throws VolleyError {

      }
    });
    // End- To remove timeout error

    requestQueue_saveQuiz.add(stringRequest_saveQuiz);

  }

  private void getAllQuestion() {
    requestQueue_getQuestion= Volley.newRequestQueue(context);
    answerLoad.setVisibility(View.VISIBLE);
    stringRequest_getQuestion=new StringRequest(Request.Method.POST, Const.BASE_SERVER + "term_start_test",
      new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
          try {
//                            answerLoad.setVisibility(View.GONE);
            bar.setVisibility(View.VISIBLE);
            l1_top.setVisibility(View.VISIBLE);
            JSONObject jsonObject = new JSONObject(response);
            String status = jsonObject.getString("status");
            if (status.equalsIgnoreCase("200")) {

              JSONArray question_array = jsonObject.getJSONArray("message");

              quesList.clear();
              listHashMap.clear();
              QuestionTable questionSetGet;
              AnswerTable answerSetGet;
              for (int i = 0; i < question_array.length(); i++) {

                JSONObject object1 = question_array.getJSONObject(i);
                questionSetGet = new QuestionTable();
                questionSetGet.setQuestion(object1.getString("question"));
                questionSetGet.setQuestionId(object1.getString("question_id"));
                questionSetGet.setDirections(object1.getString("question_desc"));
                test_id=object1.getString("exam_taken_id");
                quesList.add(questionSetGet);
                JSONArray answer_array = object1.getJSONArray("ans_arr");
                ansList = new ArrayList<>();
                for (int j = 0; j < answer_array.length(); j++) {
                  JSONObject object2 = answer_array.getJSONObject(j);
                  answerSetGet = new AnswerTable();
                  answerSetGet.setAnswerId(object2.getString("answer_id"));
                  answerSetGet.setAnswer(object2.getString("answer"));
                  ansList.add(answerSetGet);
                }
                listHashMap.put(questionSetGet.getQuestionId(), ansList);

              }
              //Start - to test question is available or not
              for (int i = 0; i <quesList.size() ; i++) {
                if (quesList.get(i).getQuestion().equals("")) {
                  getOnlyQuestionApiCall(quesList.get(i).getQuestionId(), i);
                }
              }

              if (quesList.get(0).getQuestion().equals("") || quesList.get(1).getQuestion().equals("")) {
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                  @Override
                  public void run() {
                    // Do something after 5s = 5000ms
                    answerLoad.setVisibility(View.GONE);
                    TermExamQuestionAdapter quizTestPageAdapter = new TermExamQuestionAdapter(context, quesList, listHashMap, submitButton);
                    viewPager.setAdapter(quizTestPageAdapter);
                    startTimer(Integer.parseInt(duration));
                    totalQuestion.setText(1 + "/" + listHashMap.size());
                    setup(true, listHashMap.size());
                  }
                }, 2000);
              }
              else {
                answerLoad.setVisibility(View.GONE);
                TermExamQuestionAdapter quizTestPageAdapter = new TermExamQuestionAdapter(context, quesList, listHashMap, submitButton);
                viewPager.setAdapter(quizTestPageAdapter);
                startTimer(Integer.parseInt(duration));
                totalQuestion.setText(1 + "/" + listHashMap.size());
                setup(true, listHashMap.size());
              }
              //End - to test question is available or not

                                /*TermExamQuestionAdapter quizTestPageAdapter = new TermExamQuestionAdapter(context, quesList, listHashMap, submitButton);
                                viewPager.setAdapter(quizTestPageAdapter);
                                startTimer(Integer.parseInt(duration));
                                totalQuestion.setText(1 + "/" + listHashMap.size());
                                setup(true, listHashMap.size());*/

              Const.TOTAL_QUES = String.valueOf(question_array.length());

              viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                  if (position == 0) {
                    preButton.setVisibility(View.GONE);
                  } else {
                    preButton.setVisibility(View.VISIBLE);// For disable swipe
                  }
                  if (position + 1 == listHashMap.size()) {
                    submitButton.setText("Save & Finish");
                  } else {
                    submitButton.setText("Save & Next");


                  }
                  bar.setCurrentPosition(position);


                  totalQuestion.setText(position + 1 + "/" + listHashMap.size());
                  submitButton.setVisibility(View.GONE);
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
              });

            }
            else if (status.equalsIgnoreCase("206"))
            {
              AlertDialog.Builder builder = new AlertDialog.Builder(context);
              builder.setMessage(R.string.force_logout)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                  public void onClick(DialogInterface dialog, int id) {
                    Intent intent = new Intent(context, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                  }
                });
              AlertDialog alert = builder.create();
              alert.show();
            }
            else {
              noDataImage.setVisibility(View.VISIBLE);
              tryAgainText.setVisibility(View.VISIBLE);
              Toast.makeText(context, "No Question found", Toast.LENGTH_LONG).show();
            }
            av_caf_loader.hide();

          } catch (JSONException e) {
            answerLoad.setVisibility(View.GONE);
            noDataImage.setVisibility(View.VISIBLE);
            tryAgainText.setVisibility(View.VISIBLE);
            av_caf_loader.hide();
            e.printStackTrace();
          }

        }
      }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {

        Toast.makeText(context, "Try again", Toast.LENGTH_SHORT).show();
        answerLoad.setVisibility(View.GONE);

      }
    }

    ){
      @Override
      protected Map<String, String> getParams() throws AuthFailureError {
        Map<String,String>myParams=new HashMap<String, String>();
        myParams.put("exam_id", quizId);
        myParams.put("student_id", Const.USER_ID);
        return myParams;
      }
    };

    // Start- To remove timeout error
    stringRequest_getQuestion.setRetryPolicy(new RetryPolicy() {
      @Override
      public int getCurrentTimeout() {
        return 50000;
      }

      @Override
      public int getCurrentRetryCount() {
        return 50000;
      }

      @Override
      public void retry(VolleyError error) throws VolleyError {

      }
    });
    // End- To remove timeout error

    requestQueue_getQuestion.add(stringRequest_getQuestion);

  }


  private void startTimer(final int minute) {
    countDownTimer = new CustomCountDownTimer(60 * minute * 1000, 500) {
      @SuppressLint({"DefaultLocale", "SetTextI18n"})
      @Override
      public void onTick(long leftTimeInMilliseconds) {
        long seconds = leftTimeInMilliseconds / 1000;
        textTimer.setText(String.format("%02d", seconds / 60) + ":" + String.format("%02d", seconds % 60));

        //Start code to calculate test timing
        elapsed_time = minute*60*1000 - leftTimeInMilliseconds;
      }

      @SuppressLint("SetTextI18n")
      @Override
      public void onFinish() {
        if (textTimer.getText().equals("00:00")) {
          textTimer.setText("STOP");
          Const.END_TIME = df.format(c.getTime());
          submitTestApiCall();
          countDownTimer.cancel();
          Const.answerStoreHash.clear();
          Const.testquestionAnswerStoreHash.clear();
          finish();
        }
      }
    }.start();

  }

  @Override
  public void onBackPressed() {
    Toast.makeText(context, "You have to complete this test", Toast.LENGTH_SHORT).show();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    tm.listen(callStateListener, PhoneStateListener.LISTEN_NONE);
  }


  private void getOnlyQuestionApiCall(final String questionId, final int ques_pos) {
    requestQueue_singleQues= Volley.newRequestQueue(context);
    StringRequest request = new StringRequest(Request.Method.POST, Const.BASE_SERVER + "term_get_small_question",
      new Response.Listener<String>() {
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onResponse(String response) {
          try {
            JSONObject object = new JSONObject(response);
            String status = object.getString("status");
            if (status.equals("200"))
            {
              JSONArray jsonArray = object.getJSONArray("message");
              JSONObject jsonObject1 = jsonArray.getJSONObject(0);
              exam_ques = jsonObject1.getString("question");
              quesList.get(ques_pos).setQuestion(jsonObject1.getString("question"));
            }


          } catch (JSONException e) {
            e.printStackTrace();
          }
        }
      }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {
      }
    }){
      @Override
      protected Map<String, String> getParams() {
        Map<String, String> params = new Hashtable<>();
        params.put("question_id", questionId);
        return params;
      }
    };


    request.setRetryPolicy(new RetryPolicy() {
      @Override
      public int getCurrentTimeout() {
        return 50000;
      }

      @Override
      public int getCurrentRetryCount() {
        return 50000;
      }

      @Override
      public void retry(VolleyError error) throws VolleyError {

      }
    });

    requestQueue_singleQues.add(request);
  }

}
