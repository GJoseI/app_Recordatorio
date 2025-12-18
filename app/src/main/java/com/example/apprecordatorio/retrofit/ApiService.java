package com.example.apprecordatorio.retrofit;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiService {
    @FormUrlEncoded
    @POST("addPaciente")
    Call<ApiResponse> addPaciente(
            @Field("nombre") String nombre
    );
    @FormUrlEncoded
    @POST("updateAlarma.php")
    Call<ApiResponse> updateAlarma(
            @Field("id") int id,
            @Field("id_paciente") int idPaciente,
            @Field("titulo") String titulo,
            @Field("descripcion") String descripcion,
            @Field("tono") String tono,
            @Field("imagen") String imagen,
            @Field("estado") int estado,
            @Field("domingo") int domingo,
            @Field("lunes") int lunes,
            @Field("martes") int martes,
            @Field("miercoles") int miercoles,
            @Field("jueves") int jueves,
            @Field("viernes") int viernes,
            @Field("sabado") int sabado,
            @Field("baja_logica") int bajaLogica,
            @Field("hora") int hora,
            @Field("minuto") int minuto,
            @Field("updated_at") String updatedAt
    );
    @FormUrlEncoded
    @POST("addAlarma.php")
    Call<ApiResponse> addAlarma(
            @Field("id_paciente") int idPaciente,
            @Field("titulo") String titulo,
            @Field("descripcion") String descripcion,
            @Field("tono") String tono,
            @Field("imagen") String imagen,
            @Field("estado") int estado,
            @Field("domingo") int domingo,
            @Field("lunes") int lunes,
            @Field("martes") int martes,
            @Field("miercoles") int miercoles,
            @Field("jueves") int jueves,
            @Field("viernes") int viernes,
            @Field("sabado") int sabado,
            @Field("baja_logica") int bajaLogica,
            @Field("hora") int hora,
            @Field("minuto") int minuto,
            @Field("updated_at") String updatedAt
    );

    @FormUrlEncoded
    @POST("updateNota.php")
    Call<ApiResponse> updateNota(
            @Field("id") int id,
            @Field("id_paciente") int idPaciente,
            @Field("titulo") String titulo,
            @Field("descripcion") String descripcion,
            @Field("imagen") String imagen,
            @Field("baja_logica") int bajaLogica,
            @Field("updated_at") String updatedAt
    );
    @FormUrlEncoded
    @POST("addNota.php")
    Call<ApiResponse> addNota(
            @Field("id_paciente") int idPaciente,
            @Field("titulo") String titulo,
            @Field("descripcion") String descripcion,
            @Field("imagen") String imagen,
            @Field("baja_logica") int bajaLogica,
            @Field("updated_at") String updatedAt
    );
    @FormUrlEncoded
    @POST("vincularTutorPaciente.php")
    Call<ApiResponse> vincularTutorPaciente(
            @Field("id_tutor") int idTutor,
            @Field("id_paciente") int idPaciente
    );

    @FormUrlEncoded
    @POST("readAllAlarmas.php")
    Call<AlarmasResponse> readAllAlarmasFrom(
            @Field("id_paciente") int idPaciente
    );
    @FormUrlEncoded
    @POST("readAllNotasFrom.php")
    Call<NotasResponse> readAllNotasFrom(
            @Field("id_paciente") int idPaciente
    );
    @FormUrlEncoded
    @POST("readAllAlarmasSync.php")
    Call<AlarmasResponse> readAllAlarmasSync(
            @Field("id_paciente") int idPaciente
    );
    @FormUrlEncoded
    @POST("readAllNotasSync.php")
    Call<NotasResponse> readAllNotasSync(
            @Field("id_paciente") int idPaciente
    );
}
