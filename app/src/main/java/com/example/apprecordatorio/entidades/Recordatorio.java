package com.example.apprecordatorio.entidades;



import java.time.LocalDate;

    public class Recordatorio {
        private int id;

        private int idRemoto;
        private boolean pendingChanges;
        private long updatedAt;
        private String imagenUrl;
        private int pacienteId;
        private String descripcion;

        private boolean bajaLogica;
        private String titulo;

        public Recordatorio() {}



        public String getTitulo() {
            return titulo;
        }

        public void setTitulo(String titulo) {
            this.titulo = titulo;
        }

        public Recordatorio(boolean estado, String descripcion,
                            int id, String imagenUrl, int pacienteDNI, int tutorDNI, String titulo ) {
            this.descripcion = descripcion;
            this.id = id;
            this.imagenUrl = imagenUrl;
            this.pacienteId = pacienteDNI;
            this.titulo = titulo;
        }


        public int getPacienteId() {
            return pacienteId;
        }

        public void setPacienteId(int pacienteDNI) {
            this.pacienteId = pacienteDNI;
        }

        public String getImagenUrl() {
            return imagenUrl;
        }

        public void setImagenUrl(String imagenUrl) {
            this.imagenUrl = imagenUrl;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }


        public String getDescripcion() {
            return descripcion;
        }

        public void setDescripcion(String descripcion) {
            this.descripcion = descripcion;
        }

        public boolean isBajaLogica() {
            return bajaLogica;
        }

        public void setBajaLogica(boolean bajaLogica) {
            this.bajaLogica = bajaLogica;
        }

        public int getIdRemoto() {
            return idRemoto;
        }

        public void setIdRemoto(int idRemoto) {
            this.idRemoto = idRemoto;
        }

        public boolean isPendingChanges() {
            return pendingChanges;
        }

        public void setPendingChanges(boolean pendingChanges) {
            this.pendingChanges = pendingChanges;
        }

        public long getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(long updatedAt) {
            this.updatedAt = updatedAt;
        }
    }
