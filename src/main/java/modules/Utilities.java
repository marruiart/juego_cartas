package modules;

public class Utilities {

    private static String getClassesStr(String... classes) {
        String classStr = "";
        if (classes != null) {
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

}
