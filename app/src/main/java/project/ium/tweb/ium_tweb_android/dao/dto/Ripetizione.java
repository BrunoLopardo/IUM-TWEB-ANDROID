package project.ium.tweb.ium_tweb_android.dao.dto;

import java.io.Serializable;
import java.util.Objects;

public class Ripetizione implements Serializable {

    private Corso corso;
    private Docente docente;
    private Prenotazione.DayName giorno;
    private Integer oraInizio;

    public Ripetizione(Corso corso, Docente docente, Prenotazione.DayName giorno, Integer oraInizio) {
        this.corso = corso;
        this.docente = docente;
        this.giorno = giorno;
        this.oraInizio = oraInizio;
    }

    public Ripetizione() {}

    public Corso getCorso() {
        return corso;
    }

    public Docente getDocente() {
        return docente;
    }

    public Prenotazione.DayName getGiorno() {
        return giorno;
    }

    public Integer getOraInizio() {
        return oraInizio;
    }

    public void setCorso(Corso corso) {
        this.corso = corso;
    }

    public void setDocente(Docente docente) {
        this.docente = docente;
    }

    public void setGiorno(Prenotazione.DayName giorno) {
        this.giorno = giorno;
    }

    public void setOraInizio(Integer oraInizio) {
        this.oraInizio = oraInizio;
    }

    @Override
    public String toString() {
        return "Ripetizione {" + "corso=" + corso + ", docente=" + docente + " , giorno=" + giorno + " , ora=" + oraInizio +"}";
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + Objects.hashCode(this.corso);
        hash = 89 * hash + Objects.hashCode(this.docente);
        hash = 89 * hash + Objects.hashCode(this.giorno);
        hash = 89 * hash + Objects.hashCode(this.oraInizio);
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
        final Ripetizione other = (Ripetizione) obj;
        if (!Objects.equals(this.corso, other.corso)) {
            return false;
        }
        if (!Objects.equals(this.docente, other.docente)) {
            return false;
        }
        if (this.giorno != other.giorno) {
            return false;
        }
        return Objects.equals(this.oraInizio, other.oraInizio);
    }

    public boolean identical(Ripetizione r) {
        if (this == r) {
            return true;
        }
        if (r == null) {
            return false;
        }

        if (!corso.identical(r.corso)) {
            return false;
        }
        if (!docente.identical(r.docente)) {
            return false;
        }
        if (this.giorno != r.giorno) {
            return false;
        }
        return Objects.equals(this.oraInizio, r.oraInizio);
    }
}
