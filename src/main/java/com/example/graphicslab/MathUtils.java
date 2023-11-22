package com.example.graphicslab;

public class MathUtils {

    public static int calcFactorial(int n) {
        int ans = 1;
        for (int i = 2; i <= n; ++i)
            ans *= i;

        return ans;
    }

    // Функция для вычисления базисных функций Бернштейна
    public static double bernstein(int n, int i, double t) {
        return (double) calcFactorial(n) / (calcFactorial(i) * calcFactorial(n - i)) * Math.pow(t, i) * Math.pow(1 - t, n - i);
    }
}
