package project.ium.tweb.ium_tweb_android.dao.dto;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class Prenotazione {

    @SerializedName(value = "ID", alternate = {"id", "Id"})
    private Integer ID;
    private Corso corso;
    private Docente docente;
    private Utente utente;
    private Stato stato;
    private DayName giorno;
    private Integer oraInizio;
    private Double createdAt;

    public Prenotazione(Integer ID, Corso corso, Docente docente, Utente utente, Stato stato, DayName giorno, Integer oraInizio, Double createdAt) {
        this.ID = ID;
        this.corso = corso;
        this.docente = docente;
        this.utente = utente;
        this.stato = stato;
        this.giorno = giorno;
        this.oraInizio = oraInizio;
        this.createdAt = createdAt;
    }

    public Prenotazione() {}


    public Integer getID() {
        return ID;
    }

    public Corso getCorso() {
        return corso;
    }

    public Docente getDocente() {
        return docente;
    }

    public Utente getUtente() {
        return utente;
    }

    public Stato getStato() {
        return stato;
    }

    public DayName getGiorno() {
        return giorno;
    }

    public Integer getOraInizio() {
        return oraInizio;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public void setCorso(Corso corso) {
        this.corso = corso;
    }

    public void setDocente(Docente docente) {
        this.docente = docente;
    }

    public void setUtente(Utente utente) {
        this.utente = utente;
    }

    public void setStato(Stato stato) {
        this.stato = stato;
    }

    public void setGiorno(DayName giorno) {
        this.giorno = giorno;
    }

    public void setOraInizio(Integer oraInizio) {
        this.oraInizio = oraInizio;
    }

    public void setCreatedAt(Double createdAt) {
        this.createdAt = createdAt;
    }

    public Double getCreatedAt() {
        return createdAt;
    }

    public enum DayName {
        LUN {
            @Override
            public String toString() {
                return "Lun";
            }
        },
        MAR {
            @Override
            public String toString() {
                return "Mar";
            }
        },
        MER {
            @Override
            public String toString() {
                return "Mer";
            }
        },
        GIO {
            @Override
            public String toString() {
                return "Gio";
            }
        },
        VEN {
            @Override
            public String toString() {
                return "Ven";
            }
        }
    }

    public enum Stato {
        ATTIVA {
            @Override
            public String toString() {
                return "Attiva";
            }
        },
        EFFETTUATA {
            @Override
            public String toString() {
                return "Effettuata";
            }
        },
        DISDETTA {
            @Override
            public String toString() {
                return "Disdetta";
            }
        }
    }

    @Override
    public String toString() {
        return "Prenotazione{ID=" + ID + ", corso=" + corso + ", docente=" + docente + ", utente=" + utente + ", stato=" + stato + ", giorno=" + giorno + ", oraInizio=" + oraInizio + ", createdAt=" + createdAt + '}';
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + Objects.hashCode(this.ID);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Prenotazione other = (Prenotazione) obj;
        return Objects.equals(this.ID, other.ID);
    }

    public boolean identical(Prenotazione p) {
        if (this == p) return true;
        if (p == null) return false;
        return Objects.equals(ID, p.ID) &&
                corso.identical(p.corso) &&
                docente.identical(p.docente) &&
                utente.identical(p.utente) &&
                stato == p.stato &&
                giorno == p.giorno &&
                Objects.equals(oraInizio, p.oraInizio) &&
                Objects.equals(createdAt, p.createdAt);
    }

}
