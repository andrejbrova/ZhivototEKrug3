package com.example.zhivototekrug3;


import com.google.firebase.auth.FirebaseUser;

import java.util.UUID;


public class Aktivnost {

    public String activityID, tmpVol, reportEld, reportVol, date, ratingVol, ratingEld, ownTel, ownMail, typeA, descA, time, rep, urg, loc, own, vol, state, ownName, volName, volTel, volmail;

    public Aktivnost(){

    }

    public Aktivnost(String typeA, String descA, String time, String rep, String urg, String loc, String own, String date) {
        this.activityID = UUID.randomUUID().toString();
        this.date = date;
        this.reportEld = "/";
        this.reportVol = "/";
        this.ratingVol = "/";
        this.tmpVol = "/";
        this.ratingEld = "/";
        this.ownMail="/";
        this.ownTel="/";
        this.typeA = typeA;
        this.descA = descA;
        this.time = time;
        this.rep = rep;
        this.urg = urg;
        this.loc = loc;
        this.own = own;
        this.vol = "none";
        this.volName ="/";
        this.state = "active";
        this.ownName = "none";
        this.volTel = "/";
        this.volmail ="/";
    }

    public String getOwnMail() {
        return ownMail;
    }

    public String getOwnTel() {
        return ownTel;
    }

    public void setOwnMail(String ownMail) {
        this.ownMail = ownMail;
    }

    public void setOwnTel(String ownTel) {
        this.ownTel = ownTel;
    }

    public void setRatingEld(String ratingEld) {
        this.ratingEld = ratingEld;
    }

    public String getRatingEld() {
        return ratingEld;
    }

    public void setTmpVol(String tmpVol) {
        this.tmpVol = tmpVol;
    }

    public String getTmpVol() {
        return tmpVol;
    }

    public void setRatingVol(String ratingVol) {
        this.ratingVol = ratingVol;
    }

    public String getRatingVol() {
        return ratingVol;
    }

    public String getReportEld() {
        return reportEld;
    }

    public String getReportVol() {
        return reportVol;
    }

    public void setReportEld(String reportEld) {
        this.reportEld = reportEld;
    }

    public void setReportVol(String reportVol) {
        this.reportVol = reportVol;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getVolmail() {
        return volmail;
    }

    public void setVolmail(String volmail) {
        this.volmail = volmail;
    }

    public String getVolTel() {
        return volTel;
    }

    public void setVolTel(String volTel) {
        this.volTel = volTel;
    }

    public String getOwnName() {
        return ownName;
    }

    public String getVolName() {
        return volName;
    }

    public void setVolName(String volName) {
        this.volName = volName;
    }

    public void setOwnName(String ownName) {
        this.ownName = ownName;
    }

    public String getTypeA() {
        return typeA;
    }

    public String getDescA() {
        return descA;
    }

    public String getTime() {
        return time;
    }

    public String getRep() {
        return rep;
    }

    public String getUrg() {
        return urg;
    }

    public String getLoc() {
        return loc;
    }

    public String getOwn() {
        return own;
    }

    public String getVol() {
        return vol;
    }

    public String getState() {
        return state;
    }

    public void setTypeA(String typeA) {
        this.typeA = typeA;
    }

    public void setDescA(String descA) {
        this.descA = descA;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setRep(String rep) {
        this.rep = rep;
    }

    public void setUrg(String urg) {
        this.urg = urg;
    }

    public void setLoc(String loc) {
        this.loc = loc;
    }

    public void setOwn(String own) {
        this.own = own;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setVol(String vol) {
        this.vol = vol;
    }


}
