package modules;

public class Utilities {

    private static String getClassesStr(String... classes) {
        String classStr = "";
        if (classes.length != 0) {
            classStr = "class='";
            for (String str : classes) {
                if (str.equals(classes[classes.length - 1]))
                    classStr += (str) + "'";
                else
                    classStr += (str + " ");
            }
        }
        return classStr;
    }

    public static String printAnchor(String href, String content, String... classes) {
        href = href == null ? "" : String.format("href='%s'", href);
        content = content == null ? "" : content;
        return String.format("<a %s %s>%s</a>", getClassesStr(classes), href, content);
    }

    public static String printDiv(Player content, String... classes) {
        return printDiv(content.toString(), classes);
    }

    public static String printDiv(String content, String... classes) {
        content = content == null ? "" : content;
        return String.format("<div %s>%s</div>", getClassesStr(classes), content);
    }

    public static String printImg(String src, String alt, String... classes) {
        return printImg(src, alt, null, false, classes);
    }

    public static String printImg(String src, String alt, String inlineStyle, boolean inline, String... classes) {
        src = src == null ? "" : String.format("src='%s'", src);
        alt = alt == null ? "" : String.format("alt='%s'", alt);
        return String.format("<img %s %s %s %s>", getClassesStr(classes), src, alt, inline ? inlineStyle : "");
    }
}
