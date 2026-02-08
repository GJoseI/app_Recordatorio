package com.example.apprecordatorio.retrofit;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {
    @FormUrlEncoded
    @POST("addPaciente")
    Call<ApiResponse> addPaciente(
            @Field("nombre") String nombre
    );

    @FormUrlEncoded
    @POST("addTutor")
    Call<ApiResponse> addTutor(
            @Field("nombre_usuario") String nombreUsuario,
            @Field("email") String email,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("loginTutor")
    Call<TutorLoginResponse> loginTutor(
            @Field("nombre_usuario") String username,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("vincularTutor")
    Call<VincularResponse> vincularTutor(
            @Field("id_tutor") int idTutor,
            @Field("id_paciente") int idPaciente
    );

    @FormUrlEncoded
    @POST("desvincular")
    Call<VincularResponse> desvincularTutor(
            @Field("id_tutor") int idTutor
    );

    @GET("getPaciente")
    Call<PacienteResponse> getPaciente(
            @Query("id") int id
    );
    @FormUrlEncoded
    @POST("updateAlarma")
    Call<ApiResponse> updateAlarma(

            @Field("id") int id,
            @Field("id_paciente") int idPaciente,
            @Field("titulo") String titulo,
            @Field("descripcion") String descripcion,
            @Field("tono") String tono,
            @Field("imagen") String imagen,

            @Field("estado") boolean estado,
            @Field("domingo") boolean domingo,
            @Field("lunes") boolean lunes,
            @Field("martes") boolean martes,
            @Field("miercoles") boolean miercoles,
            @Field("jueves") boolean jueves,
            @Field("viernes") boolean viernes,
            @Field("sabado") boolean sabado,

            @Field("baja_logica") boolean bajaLogica,
            @Field("hora") int hora,
            @Field("minuto") int minuto,
            @Field("updated_at") String updatedAt
    );
    @FormUrlEncoded
    @POST("addAlarma")
    Call<RecordatorioResponse> addAlarma(
            @Field("id_paciente") int idPaciente,
            @Field("titulo") String titulo,
            @Field("descripcion") String descripcion,
            @Field("tono") String tono,
            @Field("imagen") String imagen,
            @Field("estado") boolean estado,
            @Field("domingo") boolean domingo,
            @Field("lunes") boolean lunes,
            @Field("martes") boolean martes,
            @Field("miercoles") boolean miercoles,
            @Field("jueves") boolean jueves,
            @Field("viernes") boolean viernes,
            @Field("sabado") boolean sabado,
            @Field("hora") int hora,
            @Field("minuto") int minuto,
            @Field("updated_at") String updatedAt
    );

    @FormUrlEncoded
    @POST("updateNota")
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
    @POST("addNota")
    Call<RecordatorioResponse> addNota(
            @Field("id_paciente") int idPaciente,
            @Field("titulo") String titulo,
            @Field("descripcion") String descripcion,
            @Field("imagen") String imagen,
            @Field("updated_at") String updatedAt
    );

    @FormUrlEncoded
    @POST("readAllAlarmas")
    Call<AlarmasResponse> readAllAlarmasFrom(
            @Field("id_paciente") int idPaciente
    );
    @FormUrlEncoded
    @POST("readAllNotasFrom")
    Call<NotasResponse> readAllNotasFrom(
            @Field("id_paciente") int idPaciente
    );
    @FormUrlEncoded
    @POST("readAllAlarmasSync")
    Call<AlarmasResponse> readAllAlarmasSync(
            @Field("id_paciente") int idPaciente
    );
    @FormUrlEncoded
    @POST("readAllNotasSync")
    Call<NotasResponse> readAllNotasSync(
            @Field("id_paciente") int idPaciente
    );
    @FormUrlEncoded
    @POST("deleteNota")
    Call<ApiResponse> deleteNota(
            @Field("id") int id,
            @Field("id_paciente") int idPaciente,
            @Field("updated_at") String updatedAt
    );

    @FormUrlEncoded
    @POST("deleteAlarma")
    Call<ApiResponse> deleteAlarma(
            @Field("id") int id,
            @Field("id_paciente") int idPaciente,
            @Field("updated_at") String updatedAt
    );

    @FormUrlEncoded
    @POST("addSeguimiento.php")
    Call<ApiResponse> addSeguimiento(
            @Field("atendida") int atendida,
            @Field("id_alarma") int idAlarma,
            @Field("id_paciente") int idPaciente,
            @Field("fecha_hora") String fechaHora
    );
    @FormUrlEncoded
    @POST("readAllSeguimientoFrom.php")
    Call<SeguimientosResponse> readAllSeguimientoFrom(
            @Field("id_paciente") int idPaciente
    );
}
