package battle.conf;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;

import ground.GroundView;
import ground.IntPt;
import ground.Tile;
import javafx.scene.image.Image;

/** 管理所有全局属性和图片，按照index寻址 */
public final class Configs {
	// MARK: 人物相关贴图，五个数组使用相同下标
	public static ArrayList<Image> commonImgs = new ArrayList<>();
	public static ArrayList<Image> attack1Imgs = new ArrayList<>();
	public static ArrayList<Image> attack2Imgs = new ArrayList<>();
	public static ArrayList<Image> attack3Imgs = new ArrayList<>();

	// MARK: 系统相关贴图
	public static ArrayList<Image> sysImgs = new ArrayList<>();

	// MARK: 人物图片的下标
	public static final int INDEX_HULU = 0;
	public static final int INDEX_OLDMAN = 7;
	public static final int INDEX_XIEZIJING = 8;
	public static final int INDEX_SNAKE = 9;
	public static final int INDEX_LOULO = 10;


	public enum IMG_INDEX {
		IMG_PRE_BK_GROUND,
		IMG_BK_GROUND,
		IMG_START,
		IMG_LOAD,
		IMG_EXIT,
		IMG_EXIT_HL,
		IMG_FLOAT_BAR,
		IMG_TMP_SKILL,
		IMG_SAVE,
		IMG_GAMEOVER,
		IMG_RIP,
		IMG_B_ATTACK1,
		IMG_B_ATTACK2,
		IMG_B_ATTACK3,
		IMG_HP,
		IMG_MP,
		IMG_JUST_CAMP,
		IMG_DARK_CAMP,
		IMG_SELECT_BK_GROUND,
		IMG_JUST_CAMP_H,
		IMG_DARK_CAMP_H,
		IMG_BATTLE_GRID,
		IMG_B_ATTACK1_H,
		IMG_B_ATTACK2_H,
		IMG_B_ATTACK3_H,
		IMG_START_H,
		IMG_LOAD_H,
		IMG_INFO_COL

	};


	private static void loadSysImg(){
		sysImgs.add(new Image("foreBkground2.PNG"));
		sysImgs.add(new Image("bkGround1.png"));

		sysImgs.add(new Image("startGame.PNG"));
		sysImgs.add(new Image("readFile.PNG"));
		sysImgs.add(new Image("exit0.PNG"));
		sysImgs.add(new Image("exit0h.PNG"));

		sysImgs.add(new Image("skillVol1.PNG"));
		sysImgs.add(new Image("tmpskill.PNG"));
		sysImgs.add(new Image("save.PNG"));
		sysImgs.add(new Image("END.PNG"));
		sysImgs.add(new Image("RIP.PNG"));
		sysImgs.add(new Image("attackVol1.PNG"));
		sysImgs.add(new Image("attackVol2.PNG"));
		sysImgs.add(new Image("attackVol3.PNG"));
		sysImgs.add(new Image("HP.PNG"));
		sysImgs.add(new Image("MP.PNG"));
		sysImgs.add(new Image("JustSide.PNG"));
		sysImgs.add(new Image("DarkSide.png"));
		sysImgs.add(new Image("bkGround4.PNG"));
		sysImgs.add(new Image("JustSide2.PNG"));
		sysImgs.add(new Image("DarkSide2.png"));
		sysImgs.add(new Image("backgroundSpec1.PNG"));
		sysImgs.add(new Image("attackVol1h.PNG"));
		sysImgs.add(new Image("attackVol2h.PNG"));
		sysImgs.add(new Image("attackVol3h.PNG"));
		sysImgs.add(new Image("startGame1.png"));
		sysImgs.add(new Image("readFile1.png"));
		sysImgs.add(new Image("infoCol.png"));
	}

	// MARK: 其他UI布局相关的数值属性
	private static double[] listRate;
	private static double[] listCDF;


	// 计算透视比例的累计量
	static {
		listRate = new double[] { 5,5.3,5.6,5.9,6.2,6.5,6.8,7.1, 0 };
		double baseRate = 0;
		listCDF = new double[9];
		listCDF[0] = 0;
		for (int i = 0; i < listRate.length - 1; i++) {
			baseRate += listRate[i];
			listCDF[i + 1] = baseRate;
		}
		listRate[7] = listRate[6];
		listCDF[8] = listCDF[7];
	}

	public static Tile isInTile(double x, double y, int row, int col) {
		Tile tile = genTileSize(row, col);
		if (y < tile.y || y > tile.y + tile.tileHgt)
			return null;
		double y_rate = (y - tile.y) / tile.tileHgt;
		double left_bound = tile.topX - (tile.bottomX - tile.topX) * y_rate;
		double right_bound = (tile.topX + tile.topWid)
				- ((tile.bottomX + tile.bottomWid) - (tile.topX + tile.topWid)) * y_rate;
		if (left_bound < x && right_bound > x) {
			return tile;
		}
		return null;
	}

	private static Tile genTileSize(int x, int y) {
		Tile tile = new Tile();
		tile.topWid = genWid(y);
		tile.bottomWid = genWid(y + 1);
		tile.tileHgt = TILE_LEN * 7.0 * listRate[y] / listCDF[7];
		tile.topX = TILE_LEN  * listCDF[7 - y] / listCDF[7] + tile.topWid * x + MARGIN_LEFT;
		tile.bottomX = TILE_LEN  * listCDF[7 - y - 1] / listCDF[7] + tile.bottomWid * x + MARGIN_LEFT;

		// 累积量算高度
		tile.y = TILE_LEN * 7.25 * listCDF[y] / listCDF[7] + MARGIN_TOP - 20;
		return tile;
	}

	public static Tile genMidSize(int x, int y) {
		IntPt vloc = GroundView.realPtToPt(x + 1, y + 1);
		double xRate = (x - GroundView.xToRealX(vloc.x)) / (double) TILE_LEN;
		double yRate = (y - GroundView.yToRealY(vloc.y)) / (double) TILE_LEN;
		Tile tile = genTileSize(vloc.x, vloc.y);
		tile.topX += tile.topWid * xRate;
		tile.bottomX += tile.bottomWid * xRate;
		tile.y += tile.tileHgt * yRate;
		return tile;
	}

	// 基础宽度的计算
	public static double genWid(int row) {
		return TILE_LEN * (4.0 + (listCDF[row] / listCDF[7])) / 5.0;
	}

	public static int WINDOWS_HGT;
	public static int WINDOWS_WID;

	// 战斗框距离四侧的距离
	public static int MARGIN_TOP;
	public static int MARGIN_BOT;
	public static int MARGIN_LEFT;
	public static int MARGIN_RIGHT;

	public static int TILE_W_NUMS;
	public static int TILE_H_NUMS;
	public static int TILE_LEN;

	public static int SELECTION_BAR_WID;
	public static int SELECTION_BAR_HGT;

	public static int INFO_BAR_WID;
	public static int INFO_BAR_HGT;

	public static int HINT_R;
	public static int HINT_PAD;

	// 图片名称 待改
	public final static String[] imgNameList = { "brother1", "brother2", "brother3", "brother4", "brother5", "brother6",
			"brother7", "grandpa", "scorption1", "snake1", "LouLo1" };


	private static void genSelectionBarSize(int preScreenHgt) {
		// 从系统获得窗口的高度
		SELECTION_BAR_WID = (int) (preScreenHgt / 3.0);
		SELECTION_BAR_HGT = (int) (SELECTION_BAR_WID * 2.5);
	}

	private static void genMarginSize(int preScreenHgt, int preMarginTop, int preMarginBot) {
		MARGIN_TOP = preMarginTop + TILE_LEN;
		MARGIN_LEFT = SELECTION_BAR_WID + TILE_LEN + 50;
		MARGIN_RIGHT = (int) (TILE_LEN * 1.5);
		MARGIN_BOT = preMarginBot;

	}

	private static void genTileSize(int preScreenHgt, int preMarginTop, int preMarginBot) {
		TILE_LEN = (preScreenHgt - MARGIN_TOP - MARGIN_BOT) / 10;
		TILE_H_NUMS = 7;
		TILE_W_NUMS = 10;
	}

	private static void genWinSize() {
		// 总体界面的宽度
		WINDOWS_WID = MARGIN_LEFT + TILE_W_NUMS * TILE_LEN + MARGIN_RIGHT;
		// 总体界面的高度
		WINDOWS_HGT = MARGIN_TOP + TILE_H_NUMS * TILE_LEN + MARGIN_BOT;
	}

	private static void genInfoBarSize() {
		// 技能栏的长宽
		INFO_BAR_HGT = TILE_LEN * 2 + 20;
		INFO_BAR_WID = TILE_LEN * 6;
	}

	private static void genHintCol() {
		// 提示栏的长宽
		HINT_R = TILE_LEN / 8;
		HINT_PAD = HINT_R * 3;
	}

	private static void generateAllParas() {
		Toolkit temKit = Toolkit.getDefaultToolkit();
		Dimension scrSizeDim = temKit.getScreenSize();
		int preScreenHgt = (int) (scrSizeDim.height * 6.0 / 7.0);

		genSelectionBarSize(preScreenHgt);

		int preMarginTop = (int) (0.05 * preScreenHgt);
		int preMarginBot = (int) (0.05 * preScreenHgt);

		genTileSize(preScreenHgt, preMarginTop, preMarginBot);

		genMarginSize(preScreenHgt, preMarginTop, preMarginBot);

		genWinSize();

		genInfoBarSize();

		genHintCol();
	}

	private static void loadComImg() {
		for (String s : imgNameList) {
			commonImgs.add(new Image(s + ".PNG"));
		}
	}

	private static void loadAttack1Img() {
		for (String s : imgNameList) {
			attack1Imgs.add(new Image(s + "a1.PNG"));
		}
	}

	private static void loadAttack2Img() {
		for (String s : imgNameList) {
			attack2Imgs.add(new Image(s + "a2.PNG"));
		}

	}

	private static void loadAttack3Img() {
		for (String s : imgNameList) {
			attack3Imgs.add(new Image(s + "a3.PNG"));
		}

	}



	private static void loadAllImgs() {

		loadComImg();
		loadAttack1Img();
		loadAttack2Img();
		loadAttack3Img();

		loadSysImg();

	}

	public static void loadAll() {
		loadAllImgs();

		generateAllParas();
	}
}
