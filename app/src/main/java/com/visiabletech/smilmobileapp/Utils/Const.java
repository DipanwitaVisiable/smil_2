package com.visiabletech.smilmobileapp.Utils;

import com.visiabletech.smilmobileapp.Pogo.AnswerTable;
import com.visiabletech.smilmobileapp.Pogo.FreeTestAnswer;

import java.util.HashMap;
import java.util.List;

public class Const {

    //    public static final String BASE_SERVER = "http://sudhirmemorialinstituteliluah.com/snrmemorialtrust/webservices/websvc/";
//    public static final String BASE_SERVER = "http://nirbadhngo.com/snrmemorialtrust/webservices/websvc/";
    public static final String BASE_SERVER = "http://www.primers.co.in/snrmemorialtrust/webservices/websvc/"; // change on 18_11_2021
//    public static final String FILE_UPLOAD_IN_DRIVE = "https://nirbadhngo.com/snrmemorialtrust/googledrive_cntr/googledrive/fileUpload";
    public static final String FILE_UPLOAD_IN_DRIVE = "http://www.primers.co.in/snrmemorialtrust/googledrive_cntr/googledrive/fileUpload";

    public static String PAY_ONLINE_URL = "";
    public static String exam_id = "";
    public static String st_exam_id = "";
    public static String student_id = "";
    public static String mobile_no = "";
    public static String OPEN_HOME_FIRST_TIME = "";

    //For splash screen
    public static int updateFalg = 0;

    //For Login section
    public static String PROFILENAME = "";
    public static String USER_ROLE = "";
    public static String PhoneNo = "";
    public static String CLASS_NAME = "";
    public static String SECTION_NAME = "";


    //For Online classes section
    public static String USER_ID = "";
    public static String DAYS_ID = "";
    public static String DAYS_NAME = "";
    public static String PERIOD_ID = "";
    public static String PERIOD_NAME = "";

    // For Worksheet section
    public static String SUBJECT_ID = "";
    public static String SUBJECT_NAME = "";
    public static String CHAPTER_ID = "";
    public static String CHAPTER_NAME = "";
    public static String PDF_URL = "";

    //For Online test or Quiz test
    public static String QUIZ_ID = "";
    public static String END_TIME = "";
    public static String TOTAL_QUES = "";
    public static String CHOOSE_QUESTION_ID = "";
    public static String ANSWER_ID = "";
    public static String STUDENT_ID = "";
    public static int QUES_NO = 0;
    public static HashMap<String, String> answerStoreHash = new HashMap<>();
    public static HashMap<String, List<AnswerTable>> testquestionAnswerStoreHash = new HashMap<>();
    public static HashMap<String, String> answerQuestionStoreHash = new HashMap<>();
    public static HashMap<String, List<FreeTestAnswer>> questionAnswerStoreHash = new HashMap<>();
    public static HashMap<String, String> answerCheckHash = new HashMap<>();

    public static String JWT_TOKEN_ZOOM = "";

  //For Assignment section
  public static String FROM_SCREEN_TO_SUBJECT="";
  public static String TOPIC_ID="";
  public static String ASSIGNMENT_TOPIC_STATUS="";

}
