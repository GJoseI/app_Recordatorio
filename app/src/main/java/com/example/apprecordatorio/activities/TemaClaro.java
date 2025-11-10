package com.example.apprecordatorio.activities;

import android.animation.ValueAnimator;
import android.content.Context;
import android.animation.ArgbEvaluator;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.view.View;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;

import com.example.apprecordatorio.R;

public class TemaClaro {
    private static final String PREFS_NAME = "config";
    private static final String KEY_MODO_CLARO = "modo_claro";

    // ✅ Aplica el tema guardado antes del setContentView()
    public static void aplicarTema(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        boolean modoClaro = prefs.getBoolean(KEY_MODO_CLARO, true);

        AppCompatDelegate.setDefaultNightMode(
                modoClaro ? AppCompatDelegate.MODE_NIGHT_NO : AppCompatDelegate.MODE_NIGHT_YES
        );
    }

    // ✅ Cambia el tema y guarda la preferencia
    public static void cambiarTema(Context context, boolean modoClaro) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit().putBoolean(KEY_MODO_CLARO, modoClaro).apply();

        AppCompatDelegate.setDefaultNightMode(
                modoClaro ? AppCompatDelegate.MODE_NIGHT_NO : AppCompatDelegate.MODE_NIGHT_YES
        );
    }

    // 🎨 (Opcional) Cambia el fondo animado del layout
    public static void animarCambioFondo(Context context, View layoutPrincipal, boolean modoClaro) {
        int colorFrom = ((ColorDrawable) layoutPrincipal.getBackground()).getColor();
        int colorTo = ContextCompat.getColor(context, modoClaro ? R.color.white : R.color.light_gray);

        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        colorAnimation.setDuration(500);
        colorAnimation.addUpdateListener(animator ->
                layoutPrincipal.setBackgroundColor((int) animator.getAnimatedValue())
        );
        colorAnimation.start();
    }

    // ✅ Devuelve el valor actual del modo
    public static boolean esModoClaro(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getBoolean(KEY_MODO_CLARO, true);
    }
}
