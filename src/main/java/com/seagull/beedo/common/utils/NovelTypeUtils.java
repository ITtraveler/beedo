package com.seagull.beedo.common.utils;

/**
 * Created by hgs on 2017/7/10.
 */
public class NovelTypeUtils {
    private static String[] typeName = {"玄幻魔法", "言情小说", "武侠修真", "现代都市", "历史军事", "科幻灵异", "耽美小说", "游戏竞技", "同人小说", "其他小说"};
    private static String abbreviation[] = {"玄幻 ", "言情 ", "仙侠 ", "都市 ", "军事", "科灵 ", "耽美", "游戏 ", "同人", "其他"};

    /**
     * 类型名得到对应Value值
     *
     * @param type
     * @return
     */
    public static int getTypeValue(String type) {
        for (int i = 0; i < typeName.length; i++) {
            if (typeName[i].equals(type)) {
                return i;
            }
        }
        return -1;
    }

    public static String getTypeName(int typeValue) {
        if (typeValue >= 0 && typeValue < typeName.length)
            return typeName[typeValue];
        else
            return "";
    }
}
