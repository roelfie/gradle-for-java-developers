package top.kerstholt.gradle.plugin;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class MyCustomPlugin implements Plugin<Project> {

    @Override
    public void apply(Project project) {
        project.getTasks().register("myPluginTask", TaskFromMyCustomPlugin.class);
    }
}
