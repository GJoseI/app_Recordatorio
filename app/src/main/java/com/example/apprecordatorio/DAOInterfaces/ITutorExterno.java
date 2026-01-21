package com.example.apprecordatorio.DAOInterfaces;

import com.example.apprecordatorio.Entidades.Tutor;

public interface ITutorExterno {
    boolean add(Tutor t);
    Tutor login(String username,String password);

    boolean vincular (Tutor t);
}
