package tk.mybatis.generator;

import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * MyBatis Generator - Generate code directly
 */
public class Generator {
    public static void main(String[] args) throws Exception {
        // MBG warning messages during execution
        List<String> warnings = new ArrayList<String>();

        // Overwrite existing files when generated code is duplicated
        boolean overwrite = true;

        // Read MBG configuration file
        InputStream is = Generator.class.getResourceAsStream(
                "/generator/generatorConfig.xml");
        ConfigurationParser cp = new ConfigurationParser(warnings);
        Configuration config = cp.parseConfiguration(is);
        if (is != null) {
            is.close();
        }

        DefaultShellCallback callback = new DefaultShellCallback(overwrite);

        // Create MBG instance
        MyBatisGenerator myBatisGenerator =
                new MyBatisGenerator(config, callback, warnings);

        // Execute code generation
        myBatisGenerator.generate(null);

        // Output warning messages
        for (String warning : warnings) {
            System.out.println(warning);
        }
    }
}
