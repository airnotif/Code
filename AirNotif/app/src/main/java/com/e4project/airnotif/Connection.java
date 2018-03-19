package com.e4project.airnotif;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

class Connection {

    private static final String USER_AUTHORIZATION = "0";
    private static final String ALL_SUBSCRIPTIONS = "1";
    private static final String MY_SUBSCRIPTIONS = "2";
    private static final String ADD_SUBSCRIBE = "3";
    private static final String REMOVE_SUBSCRIBE = "4";

    @SuppressWarnings("unchecked")
    static boolean checkUserAuthorization(String userId, String ipAddress, int port) {
        ArrayList<String> request = new ArrayList<>();
        request.add(USER_AUTHORIZATION);
        request.add(userId);

        Object object = getResultObjectByRequest(request, ipAddress, port);
        try {
            if (object != null) {
                return ((ArrayList<String>) object).get(0).equals("true");
            }
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        return false;
    }

    static ArrayList<ArrayList<String>> getAllSubscriptions(String userId, String ipAddress, int port) {
        return getSubscriptions(ALL_SUBSCRIPTIONS, userId, ipAddress, port);
    }

    static ArrayList<ArrayList<String>> getMySubscriptions(String userId, String ipAddress, int port) {
        return getSubscriptions(MY_SUBSCRIPTIONS, userId, ipAddress, port);
    }

    @SuppressWarnings("unchecked")
    private static ArrayList<ArrayList<String>> getSubscriptions(String requestId, String userId, String ipAddress, int port) {
        ArrayList<String> request = new ArrayList<>();
        request.add(requestId);
        request.add(userId);
        Object object = getResultObjectByRequest(request, ipAddress, port);
        try {
            return (ArrayList<ArrayList<String>>) object;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        return null;
    }

    static boolean addSubscribe(String userId, String miId, String ipAddress, int port) {
        return changeSubscribe(ADD_SUBSCRIBE, userId, miId, ipAddress, port);
    }

    static boolean removeSubscribe(String userId, String miId, String ipAddress, int port) {
        return changeSubscribe(REMOVE_SUBSCRIBE, userId, miId, ipAddress, port);
    }

    @SuppressWarnings("unchecked")
    private static boolean changeSubscribe(String requestId, String userId, String miId, String ipAddress, int port) {
        ArrayList<String> request = new ArrayList<>();
        request.add(requestId);
        request.add(userId);
        request.add(miId);
        Object object = getResultObjectByRequest(request, ipAddress, port);
        try {
            if (object != null) {
                return ((ArrayList<String>) object).get(0).equals("true");
            }
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static Object getResultObjectByRequest(ArrayList<String> request, String ipAddress, int port) {
        try {
            InetAddress serverAddr = InetAddress.getByName(ipAddress);
            Socket socket = new Socket(serverAddr, port);

            ObjectOutputStream objectOutput = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream objectInput = new ObjectInputStream(socket.getInputStream());

            objectOutput.writeObject(request);

            Object result = null;
            try {
                result = objectInput.readObject();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            socket.close();
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    static boolean checkIPAdress(String ipAdress, int port) {
        try {
            InetAddress serverAddr = InetAddress.getByName(ipAdress);
            Socket socket = new Socket(serverAddr, port);
            socket.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}