package com.e4project.airnotif;

import java.io.Serializable;
import java.util.Date;

/**
 * Classe qui représente un Moyen Industriel (MI)
 * Cette classe implémente l'interface Serializable afin d'être transmise d'une activité à une autre via l'Intent
 * @author Nicolas RACIC
 * @version 07/12/2017
 */
public class MoyenIndustriel implements Serializable{

    private static final long serialVersionUID = 7526472295622776147L;

    /*-- [REF] --*/
    static class Ref implements Serializable {
        Date DATE;
        String STATION;
        String PROGRAM;
    }

    /*-- [SYSTEM] --*/
    static class System implements Serializable {
        static int INCONNU = 0;
        static int DEFAUT_CONNEXION_ETHERNET = 2;
        static int DEFAUT_SYSTEME_D_EXPLOITATION = 4;
        static int STATUT_API = 6;
        static int API_EN_PROG = 8;
        static int ARRET_MAINTENANCE = 10;
        static int DEFAUT_API = 15;
        static int ARRET_D_URGENCE = 20;
        static int INTRUSION = 30;
        static int HORS_SERVICE = 40;
        static int DEFAUT_APPLICATION = 45;
        static int MODE_MANUEL_MAINTENANCE = 50;
        static int DEFAUT_BLOQUANT_MAINTENANCE = 60;
        static int DEFAUT_BLOQUANT_EXPLOITATION = 70;
        static int MODE_MANUEL_EXPLOITATION = 80;
        static int MANQUE_AUTO = 90;
        static int MODE_AUTO = 100;

        int STATUS;
    }

    /*-- [PROCESS] --*/
    static class Process implements Serializable {
        static int INCONNU = 0;
        static int INOP = 90;
        static int DISPONIBLE = 100;
        static int TEST = 110;
        static int MAN = 120;
        static int TERM = 130;

        int STATUS;

        static int FAUX = 0;
        static int VRAI = 1;

        int NOGO;
    }

    /*-- [FUNCTION] --*/
    static class Function implements Serializable {
        static int USER = 0;
        static int DEV = 1;
        static int ADMIN = 2;

        int SESSION;
    }

    private Ref ref;
    private System system;
    private Process process;
    private Function function;

    private MoyenIndustriel(Ref ref, System system, Process process, Function function) {
        this.ref = ref;
        this.system = system;
        this.process = process;
        this.function = function;
    }

    public MoyenIndustriel() {
        this(new Ref(), new System(), new Process(), new Function());
    }

    public Ref getRef() {
        return ref;
    }

    public System getSystem() {
        return system;
    }

    public Process getProcess() {
        return process;
    }

    public Function getFunction() {
        return function;
    }

    public void setRef(Ref ref) {
        this.ref = ref;
    }

    public void setSystem(System system) {
        this.system = system;
    }

    public void setProcess(Process process) {
        this.process = process;
    }

    public void setFunction(Function function) {
        this.function = function;
    }
}
