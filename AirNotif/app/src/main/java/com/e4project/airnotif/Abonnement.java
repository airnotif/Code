package com.e4project.airnotif;

import java.io.Serializable;

/**
 * Classe qui représente un abonnement
 * L'abonnement se fait par un utilisateur sur un Moyen Industriel (MI)
 * Cette classe implémente l'interface Serializable afin d'être transmise d'une activité à une autre via l'Intent
 * @author Nicolas RACIC
 * @version 07/12/2017
 */
public class Abonnement implements Serializable {

    private boolean isChecked;
    private String text;

    /**
     * Constructeur de la classe
     * @param bool le boolean représentant l'abonnement (oui/non)
     * @param text le TextView représentant le nom
     */
    public Abonnement(final boolean bool, final String text) {
        this.isChecked = bool;
        this.text = text;
    }

    /**
     * Constructeur par défaut créant un abonnement false avec un nom vide
     */
    public Abonnement() {
        this(false, "");
    }

    /**
     * Renvoie true si la personne est abonnée
     * @return true si la personne est abonnée
     */
    public boolean isChecked() {
        return this.isChecked;
    }

    /**
     * Renvoie le nom de la personne
     * @return le nom de la personne
     */
    public String getText() {
        return this.text;
    }

    /**
     * Remplace le boolean représentant l'abonnement (oui/non)
     * @param bool le boolean représentant l'abonnement (oui/non)
     */
    public void setChecked(final boolean bool) {
        this.isChecked = bool;
    }

    /**
     * Remplace le string représentant le nom
     * @param textView le nouveau nom
     */
    public void setText(final String textView) {
        this.text = textView;
    }
}
