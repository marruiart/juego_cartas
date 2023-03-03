package modules;

import java.util.ArrayList;

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

    public static String printDiv(Player player, String... classes) {
        String playerStr = player == null ? "" : player.toString();
        return printDiv(playerStr, classes);
    }

    public static String printDiv(String content, String... classes) {
        content = content == null ? "" : content;
        return String.format("<div %s>%s</div>", getClassesStr(classes), content);
    }

    public static String printButton(String type, String content, String... classes) {
        type = type == null ? "type='submit'" : String.format("type='%s'", type);
        content = content == null ? "" : content;
        return String.format("<button %s %s>%s</button>", getClassesStr(classes), type, content);
    }

    public static String printImg(String src, String alt, String... classes) {
        return printImg(src, alt, null, false, classes);
    }

    public static String printImg(String src, String alt, String inlineStyle, boolean inline, String... classes) {
        src = (src == null || src.equals("")) ? "" : String.format("src='%s'", src);
        alt = (alt == null || alt.equals("")) ? "" : String.format("alt='%s'", alt);
        inlineStyle = inline ? String.format("style='%s'", inlineStyle) : "";
        return String.format("<img %s %s %s %s>", getClassesStr(classes), src, alt, inlineStyle);
    }

    private static String getOptionsStr(ArrayList<String> options, String disabledOption) {
        String optStr = "";
        if (options.size() != 0) {
            for (String opt : options) {
                if (opt.equals(disabledOption))
                    optStr += String.format("<option value='%s' %s>%s</option>", opt.toLowerCase(), "disabled selected",
                            opt);
                else
                    optStr += String.format("<option value='%s'>%s</option>", opt.toLowerCase(), opt);
            }
        }
        return optStr;
    }

    public static String printSelect(String name, boolean enableSelect, ArrayList<String> options,
            String disabledOption, String... classes) {
        name = name == null ? "" : String.format("name='%s'", name);
        return String.format("<select %s %s%s>%s</select>", name, getClassesStr(classes),
                enableSelect ? "" : " disabled", options == null ? "" : getOptionsStr(options, disabledOption));
    }

    public static String printInput(String type, String name, String value, String... classes) {
        type = type == null ? "type='text'" : String.format("type='%s'", type);
        name = name == null ? "" : String.format("name='%s'", name);
        value = value == null ? "" : String.format("value='%s'", value);
        return String.format("<input %s %s %s %s/>", type, name, value, getClassesStr(classes));
    }
}
