/*
 This code file defines a data storage standard.
 It can be used for any purpose, and is hereby released into the public domain.
 */
package net.apocalypselabs.symat.components;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Container to save tasks.
 * @author Skylar
 */
public class TaskList implements Serializable {
    
    private static final long serialVersionUID = 6754527404697894562L;
    
    private final ArrayList<SingleTask> tasks = new ArrayList<>();
    private String title = "Untitled";
    
    public void addTask(Task t) {
        SingleTask st = new SingleTask();
        st.name = t.toString();
        st.desc = t.getDesc();
        st.percent = t.getComplete();
        tasks.add(st);
    }
    
    public Task[] getTasks() {
        Task[] a = new Task[tasks.size()];
        for (int i = 0; i < tasks.size(); i++) {
            a[i] = new Task(
                    tasks.get(i).percent,
                    tasks.get(i).name,
                    tasks.get(i).desc);
        }
        return a;
    }
    
    public void setTitle(String t) {
        title = t;
    }
    
    public String getTitle() {
        return title;
    }
    
    public TaskList() {
        
    }
    
    class SingleTask implements Serializable {
        public String name = "";
        public String desc = "";
        public int percent = 0;
    }
}