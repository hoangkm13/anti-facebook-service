package com.example.antifacebookservice.security.context;

public class DataContextHolder {
    private static final ThreadLocal<DataContext> threadLocal = new ThreadLocal<>();

    public static DataContext getDataContext() {
        return threadLocal.get();
    }

    public static void setDataContext(DataContext dataContext) {
        threadLocal.set(dataContext);
    }

    static void clear() {
        threadLocal.remove();
    }

}
