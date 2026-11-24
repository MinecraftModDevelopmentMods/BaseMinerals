import java.io.File;
import java.lang.reflect.Method;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/** Executes Eclipse's own Windows parser rather than approximating its quoting. */
public final class ParseEclipseArguments {
    public static void main(String[] args) throws Exception {
        NodeList attributes = DocumentBuilderFactory.newInstance().newDocumentBuilder()
                .parse(new File(args[0])).getElementsByTagName("stringAttribute");
        String program = null;
        for (int i = 0; i < attributes.getLength(); i++) {
            Element attribute = (Element) attributes.item(i);
            if ("org.eclipse.jdt.launching.PROGRAM_ARGUMENTS".equals(attribute.getAttribute("key")))
                program = attribute.getAttribute("value");
        }
        if (program == null) throw new IllegalArgumentException("Missing launch arguments");
        Method parse = Class.forName("org.eclipse.debug.core.DebugPlugin")
                .getDeclaredMethod("parseArgumentsWindows", String.class, boolean.class);
        parse.setAccessible(true);
        for (String argument : (String[]) parse.invoke(null, program, false)) {
            System.out.print(argument);
            System.out.print('\0');
        }
    }
}
