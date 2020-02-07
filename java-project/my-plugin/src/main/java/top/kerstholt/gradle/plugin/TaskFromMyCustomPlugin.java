package top.kerstholt.gradle.plugin;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.TaskAction;

public class TaskFromMyCustomPlugin extends DefaultTask {

    @Input
    @Override
    public String getGroup() {
        return "My custom tasks";
    }

    @Input
    @Override
    public String getDescription() {
        return "Task #1 from my custom gradle plugin";
    }

    @TaskAction
    public void myPluginTask() {
        System.out.println("Hello from my custom plugin!");
    }
}
