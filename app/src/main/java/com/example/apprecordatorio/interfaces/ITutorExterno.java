package com.example.apprecordatorio.interfaces;

import com.example.apprecordatorio.entidades.Tutor;

import java.util.ArrayList;

public interface ITutorExterno {

    boolean add(Tutor t);
    ArrayList<Tutor> readAll();
    boolean delete(Tutor t);
    boolean update(Tutor t);
    Tutor readOne(int id);
}
