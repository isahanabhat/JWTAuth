/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.jwtauth;

/**
 *
 * @author bhats
 */
public abstract class AbstractDB {
    public abstract boolean verifyUser(String username, String password);
}
