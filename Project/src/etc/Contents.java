package etc;

public class Contents {
	public static final String PIPE_VALVE = "images/pipe_valve.png";
	public static final String PIPE_NONE = "images/pipe_none.png";
	public static final String PIPE0 = "images/pipe0.png";
	public static final String PIPE1 = "images/pipe1.png";
	public static final String PIPE2 = "images/pipe2.png";
	public static final String PIPE3 = "images/pipe3.png";
	public static final String PIPE4 = "images/pipe4.png";
	public static final String PIPE5 = "images/pipe5.png";
	public static final int countEasy = 120;
	public static final int countNormal = 60;
	public static final int countHard = 30;
	public static final int countNightmare = 10;
	public static int difficulty = countNightmare; // 이 부분으로 난이도 조절
	public static boolean gameEnd = false; 
	public static boolean gameEarlyEnd = false;
	public static String nickName = "noname"; // 닉네임 : 한글4자, 영어8자까지
	public static int scoreTimeTotal = 1000;
	public static int scoreTime = 1000;
	public static int scoreTile = 0;
	public static int finalScore = 0; // 승리 시 점수 들어감(GameFinishCheck.java) 패배 시 그대로 0점.
}
