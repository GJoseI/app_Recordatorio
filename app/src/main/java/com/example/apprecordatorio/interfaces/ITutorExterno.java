package com.example.apprecordatorio.interfaces;

import com.example.apprecordatorio.entidades.Tutor;

import java.util.ArrayList;

public interface ITutorExterno {
    boolean add(Tutor t);
    Tutor login(String username,String password);

    boolean vincular (Tutor t);
}
