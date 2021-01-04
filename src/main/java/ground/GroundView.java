package ground;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import battle.conf.Configs;
import unit.HuLuCharacter;

public class GroundView {
	public static int grWidth = 10; // 战场大小
	public static int grHght = 7;
	public static HuLuCharacter[][] charaMap;
	public static Lock mapLock = new ReentrantLock();

	public static void init() {
		charaMap = new HuLuCharacter[grHght][grWidth];
		for (int i = 0; i < grHght; i++)
			for (int j = 0; j < grWidth; j++)
				charaMap[i][j] = null;
	}

	public static void clear() {
		if (charaMap == null)
			return;
		for (int i = 0; i < grHght; i++)
			for (int j = 0; j < grWidth; j++)
				charaMap[i][j] = null;
	}

	public static int xToRealX(int x) {
		return Configs.MARGIN_LEFT + x * Configs.TILE_LEN;
	}

	public static int yToRealY(int y) {
		return Configs.MARGIN_TOP + y * Configs.TILE_LEN;
	}

	public static int realXToX(double rx) {
		return (int) (rx - Configs.MARGIN_LEFT + Configs.TILE_LEN / 2) / Configs.TILE_LEN;
	}

	public static int realYToY(double ry) {
		return (int) (ry - Configs.MARGIN_TOP + Configs.TILE_LEN / 2) / Configs.TILE_LEN;
	}

	public static IntPt ptToRealPt(IntPt pt) {
		return ptToRealPt(pt.x, pt.y);
	}

	public static IntPt ptToRealPt(int vx, int vy) {
		return new IntPt(Configs.MARGIN_LEFT + vx * Configs.TILE_LEN, Configs.MARGIN_TOP + vy * Configs.TILE_LEN);
	}

	public static IntPt realPtToPt(double rx, double ry) {
		rx -= Configs.MARGIN_LEFT;
		ry -= Configs.MARGIN_TOP;
		return new IntPt(rx / Configs.TILE_LEN, ry / Configs.TILE_LEN);
	}

	/**
	 * 将战场的某一个方格设置为某个角色或清空
	 * 
	 * @param chara 角色
	 * @param srcX   虚拟原横坐标
	 * @param srcY   虚拟原纵坐标
	 * @param desX    虚拟目的横坐标
	 * @param desY    虚拟目的纵坐标
	 */
	public static void set(HuLuCharacter chara, double srcX, double srcY, double desX, double desY) {
		IntPt srcPos = realPtToPt(desX + Configs.TILE_LEN / 2, desY + Configs.TILE_LEN / 2);
		IntPt desPos = realPtToPt(srcX + Configs.TILE_LEN / 2, srcY + Configs.TILE_LEN / 2);
		if (srcPos.x < 0 || srcPos.y < 0 || srcPos.x >= grWidth || srcPos.y >= grHght)
			return;
		mapLock.lock();
		if (!(desPos.x < 0 || desPos.y < 0 || desPos.x >= grWidth || desPos.y >= grHght))
			charaMap[desPos.y][desPos.x] = null;
		charaMap[srcPos.y][srcPos.x] = chara;
		mapLock.unlock();
	}
}
